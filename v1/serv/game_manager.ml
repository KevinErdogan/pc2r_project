open Connection_handler
open Game
open Session

let timer arg =
let (time,func, argg) = arg in
  let t = ref time in
    (while !t > 0 do
      Unix.sleep(1);
      print_string("timer : " ^ string_of_int !t ^ "\n");flush stdout;
      decr t
    done);
    func argg

class game_manager st srt =
  object(self)
    val serv_tickrate = st
    val serv_refresh_tickrate = srt
    val time_between_sessions = 5
    val mutex = Mutex.create ()
    val nbPlayerConnected = ref 0
    val isGameRunning = ref false
    val mutable playerList = ( [] : ( connexion_kart * player ) list )
    val relance = ref false
    val mutable currentSession = new session [] (new gameMap 100.0 100.0) 0 st
    val isTimerOn = ref false

    method userConnected con_hand name =
      (
        Mutex.lock mutex;
        if self#isNameExist name then
          (Mutex.unlock mutex;
          false)
        else
          let process () =
          (
            print_string (name^" connected\n");
            incr nbPlayerConnected;
            self#alertNewPlayerConnected name;
            print_string ("nb player connected : "^ string_of_int(!nbPlayerConnected) ^"\n");flush stdout;
            let newPlayer = new player name in
            playerList <- (con_hand, newPlayer) :: playerList;
            ( if not (!isGameRunning) then
             con_hand#welcome false "" ""
            else
              con_hand#welcome true (currentSession#getScores()) (currentSession#getObjCoord());
              currentSession#newPlayerJoined con_hand newPlayer
            )
          ) in
         process ();

         (if not(!isGameRunning) && not(!isTimerOn) then
           let _ = Thread.create timer (time_between_sessions, (self#startSession), self) in
              isTimerOn := true
          );

        Mutex.unlock mutex;
        true
      )

    method userDeconnected name = (
      Mutex.lock mutex;
      (* remove player from list *)
      let rec removePlayer name head l =
        match l with
        | [] -> []
        | (c,p) :: [] -> if (p#getName()) == name then head else (List.append l head)
        | (c,p) :: t -> if (p#getName()) == name then (List.append t head)
                        else removePlayer name ((c,p) :: head) t
        in
      let result = removePlayer name [] playerList in

        playerList <- result;
        decr nbPlayerConnected;
        (if(!isGameRunning) then
          currentSession#playerLeft name);

      let rec alert l =
        match l with
          | [] -> ()
          | (c,p) :: t -> (c#playerDeconnected name;
                    alert t )
      in
        alert playerList;
        Mutex.unlock mutex;
    )

    method private isNameExist name =
      let rec search l =
        (match l with
          | [] -> false
          | (c,p) :: t -> (if (String.compare (p#getName()) name)==0 then
                            true
                           else search t)
          )
      in
        search playerList

    method private alertNewPlayerConnected name = (
      let rec alert l =
        match l with
          | [] -> ()
          | (c,p) :: t -> (c#newPlayerConnected name;
                    alert t )
      in
        alert playerList
      )

    method getCurrentSession () =
        currentSession
    method setCurrentSession session =
      currentSession <- session
    method setIsGameRunning b =
      isGameRunning := b
    method isGameRunning () =
      !isGameRunning
    method setIsTimerOn b =
      isTimerOn := b;
    method getPlayerList () =
      playerList
    method getNbPlayerConnected () =
      !nbPlayerConnected
    method getServerTickrateTime () =
      (1/serv_tickrate)

    method startSession gm =
      (* starting session *)
      let playerList = gm#getPlayerList()
        and nbPlayer = gm#getNbPlayerConnected() in
          if nbPlayer > 0 then
            (let session = (new session playerList (new gameMap 100.0 100.0) nbPlayer serv_tickrate) in
              gm#setCurrentSession session;
              gm#setIsGameRunning true;
              gm#setIsTimerOn false;
              session#init ();
              session#run ();
            (* end of session *)
              gm#setIsGameRunning false;
              gm#setIsTimerOn true;
              timer (time_between_sessions, gm#startSession, gm)
              )
          else
            gm#setIsTimerOn false
  end;;
