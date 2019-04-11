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
    val time_between_sessions = 20
    val mutex = Mutex.create ()
    val nbPlayerConnected = ref 0
    val isGameRunning = ref false
    val mutable playerList = ( [] : ( connexion_kart * player ) list )
    val relance = ref false
    val mutable currentSession = new session [] (new gameMap 100.0 100.0) 0 st
    val isTimerOn = ref false

    method userConnected con_hand name =
      (
        if self#isNameExist name then
          false
        else
          (* process that register a new player and alert others players *)
          let process () =
          (
            let newPlayer = new player name in
              self#alertNewPlayerConnected name;
              self#setPlayerList ((con_hand, newPlayer) :: playerList);
              self#setNbPlayerConnected ((self#getNbPlayerConnected())+1);
              print_string ("Player '"^name^"' connected.\nNumber Player Connected : "^ string_of_int(!nbPlayerConnected) ^ "\n"); flush stdout;
              if not (self#getIsGameRunning ()) then
               con_hand#welcome false "" ""
              else
              (
                let curSession = self#getCurrentSession () in
                  con_hand#welcome true (curSession#getScores()) (curSession#getObjCoord());
                  curSession#registerNewPlayer con_hand newPlayer
              )
          ) in
         process ();

         (* launch a new session by triggering a new timer *)
         (if not(self#getIsGameRunning ()) && not(self#getIsTimerOn ()) then
           let _ = Thread.create timer (time_between_sessions, (self#startSession), ()) in
              self#setIsTimerOn true
          );
          true
      )

      method userDeconnected name = (
        (* remove player from list *)
        let rec removePlayer name head l =
          match l with
          | [] -> []
          | (c,p) :: [] -> if (p#getName()) == name then head else (List.append l head)
          | (c,p) :: t -> if (p#getName()) == name then (List.append t head)
                          else removePlayer name ((c,p) :: head) t
          in
        let result = removePlayer name [] playerList in
          self#setPlayerList result;
          self#setNbPlayerConnected ((self#getNbPlayerConnected()) - 1);
          (if (self#getIsGameRunning()) then
            (self#getCurrentSession())#playerLeft name);

        (* alert player deconnected to others *)
        let alert elem =
          let (c,p) = elem in
            c#playerDeconnected name
        in
          let pL = self#getPlayerList () in
            List.iter alert pL
      )

      method private startSession () =
        (* starting session *)
        let nbPC = (self#getNbPlayerConnected ()) in
          if nbPC > 0 then
              (let session = (new session (self#getPlayerList ()) (new gameMap 800.0 600.0) nbPC serv_tickrate) in
                self#setCurrentSession session;
                self#setIsGameRunning true;
                self#setIsTimerOn false;
                session#run ();
              (* end of session *)
                self#setIsGameRunning false;
                if ( (self#getNbPlayerConnected ()) > 0) then
                (
                  self#setIsTimerOn true;
                  timer (time_between_sessions, self#startSession, ())
                )
              )
            else
              self#setIsTimerOn false

    method private isNameExist name =
    let isIn = ref false in
      let search elem =
        let (c,p) = elem in
          (if (String.compare (p#getName()) name)==0 then
              isIn := true)
      in
        let pL = self#getPlayerList () in
          List.iter search pL;
          !isIn

    method private alertNewPlayerConnected name =
      let alert elem =
        let (c,p) = elem in
          c#newPlayerConnected name
      in
        let pL = self#getPlayerList () in
          List.iter alert pL

    method private setPlayerList l =
      Mutex.lock mutex;
      playerList <- l;
      Mutex.unlock mutex

    method private getPlayerList () =
      Mutex.lock mutex;
      let pL = playerList in (* working ? *)
      Mutex.unlock mutex;
      pL

    method private getIsGameRunning () =
      Mutex.lock mutex;
      let isGR = !isGameRunning in
      Mutex.unlock mutex;
      isGR

    method private setIsGameRunning b =
      Mutex.lock mutex;
      isGameRunning := b;
      Mutex.unlock mutex

    method private getIsTimerOn () =
      Mutex.lock mutex;
      let isTO = !isTimerOn in
      Mutex.unlock mutex;
      isTO

    method private setIsTimerOn b =
      Mutex.lock mutex;
      isTimerOn := b;
      Mutex.unlock mutex

    method private getCurrentSession () =
        Mutex.lock mutex;
        let session = currentSession in
        Mutex.unlock mutex;
        session

    method private setCurrentSession s =
        Mutex.lock mutex;
        currentSession <- s;
        Mutex.unlock mutex

    method private getNbPlayerConnected () =
        Mutex.lock mutex;
        let nb = !nbPlayerConnected in
          Mutex.unlock mutex;
          nb
    method private setNbPlayerConnected (n: int) =
      Mutex.lock mutex;
      nbPlayerConnected := n;
      Mutex.unlock mutex

  end;;
