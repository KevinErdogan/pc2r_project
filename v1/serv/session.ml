open Connection_handler
open Game

(* fun to print only 4 digits of floats *)
let my_print_float x =
  Printf.sprintf "%.4f" x

(* session *)
class session l m nbPlayer st =
  object(self)
  val serv_tickrate = st
  val mutable playerList = (l : (connexion_kart * player) list)
  val mutable playerPoints = ([] : (player * int) list )
  val map = (m : gameMap)
  val win_cap = 50
  val hasPlayerWin = ref false
  val objRadius = 10.0
  val nextObj = ref (0.0, 0.0)
  val isSessionEmpty = ref false
  val mutex = Mutex.create ()

  initializer
    (* init new objectif *)
    let h = map#getHeight() and w = map#getWidth() in
     let newPos = makeAleaPos h w in
      nextObj := newPos;
      (* for all players, put them at a random place and init their points to 0 *)
      let initAll elem =
        let (c,p) = elem in
          self#setPlayerPoints ( (p,0) :: playerPoints);
          p#init h w
      in
        List.iter initAll playerList

  method run () =
    self#sendStartSession ();
    while ( not( (self#getHasPlayerWin()) ) && not ( (self#getIsSessionEmpty()) ) ) do
      Unix.sleep(serv_tickrate);
      self#tick ();
      print_string("tick\n");flush stdout
    done;

  method private setPlayerList l =
    Mutex.lock mutex;
    playerList <- l;
    Mutex.unlock mutex

  method private getPlayerList () =
    Mutex.lock mutex;
    let pL = playerList in (* working ? *)
    Mutex.unlock mutex;
    pL

  method private setPlayerPoints l =
    Mutex.lock mutex;
    playerPoints <- l;
    Mutex.unlock mutex

  method private getPlayerPoints () =
    Mutex.lock mutex;
    let pP = playerPoints in (* working ? *)
    Mutex.unlock mutex;
    pP

  method private setHasPlayerWin b =
    Mutex.lock mutex;
    hasPlayerWin := b;
    Mutex.unlock mutex

  method private getHasPlayerWin () =
    Mutex.lock mutex;
    let hPW = !hasPlayerWin in
    Mutex.unlock mutex;
    hPW

  method private setNextObj o =
    Mutex.lock mutex;
    nextObj := o;
    Mutex.unlock mutex

  method private getNextObj () =
    Mutex.lock mutex;
    let nObj = !nextObj in
    Mutex.unlock mutex;
    nObj

  method private setIsSessionEmpty b =
      Mutex.lock mutex;
      isSessionEmpty := b;
      Mutex.unlock mutex

  method private getIsSessionEmpty () =
    Mutex.lock mutex;
    let isSess = !isSessionEmpty in
    Mutex.unlock mutex;
    isSess


  method sendStartSession () =
    let coords = self#getCoords ()
      and coord = self#getObjCoord () in
        let sender elem =
          let (c,p) = elem in
            c#startSession coords coord
        in
        let pL = self#getPlayerList() in
          List.iter sender pL

  method newObj () =
    let rec nobj l coord scores =
          match l with
            [] -> ()
            | (c,p) :: t -> c#sessionNewObj coord scores; nobj t coord scores
    in
        let scores = self#getScores () in
          let h = m#getHeight() and w = m#getWidth() in
            let (x,y) = makeAleaPos h w in
              let coord = "X"^my_print_float x^"Y"^ my_print_float y in
                let pL = self#getPlayerList() in
                  nobj pL coord scores;
                  nextObj := (x,y)

(*
      method receiveNewPos con_hand coord =
        let indexY = String.index coord 'Y' in
        let x = String.sub coord 1 (indexY-1)
          and y = String.sub coord (indexY+1) ((String.length coord)-indexY-1) in
            let rec search l con_hand x y =
              match l with
                [] -> ()
               | (c, p) :: t -> if c == con_hand then
                                  p#setNewPos (float_of_string x) (float_of_string y);
                                  let (w,z) = !nextObj in
                                    if p#isInCollisionWith w z then
                                      self#objPassed p
                                else
                                  search t con_hand x y
            in
          search playerList con_hand x y
*)
(*
      method receiveNewCom con_hand comms =
        ()
*)
    method listSearch l p =
      let rec search l p =
        match l with
          [] -> 0
        | (player, points) :: t -> if p == player then points else search t p
      in
        search l p

    method listPut l p v =
      let rec put head l p v =
        match l with
          [] -> []
          | (player, points) :: [] -> if p == player then (p,v) :: head else head
          | (player, points) :: t -> if p == player then
                                      ((p,v) :: (List.append t head))
                                     else
                                      put ( (player, points) :: head) t p v
      in
        self#setPlayerPoints (put [] l p v)

    method objPassed player =
      let rec incrPoint pL =
        match pL with
          [] -> ()
          | (c,p) :: t -> if p == player then
                            let pP = self#getPlayerPoints() in
                              (let points = (self#listSearch pP p) in
                                if (points+1) = win_cap then
                                  (self#win();self#setHasPlayerWin true) (* end session *)
                                else
                                  (self#listPut pP p (points+1)) )
                          else
                            incrPoint t
      in
      let pL = self#getPlayerList() in
        incrPoint pL;
        self#newObj ()

  method registerNewPlayer con_hand newPlayer =
    let h = m#getHeight() and w = m#getWidth() in
      newPlayer#init h w;
      self#setPlayerList ((con_hand, newPlayer) :: (self#getPlayerList()));
      self#setPlayerPoints ((newPlayer, 0) :: (self#getPlayerPoints()));
      con_hand#startSession (self#getCoords()) (self#getObjCoord())

  method playerLeft name =
    let rec removeFromList (head : (connexion_kart * player) list) (l : (connexion_kart * player) list) (name : string) =
      match l with
        [] -> head
      | (c,p) :: t -> if ((p#getName()) == name) then
                          List.append t head
                      else
                        removeFromList ((c,p) :: head) t name
    in
      self#setPlayerList (removeFromList [] (self#getPlayerList()) name);
      let pL = self#getPlayerList() in
        if (pL == []) then
          self#setIsSessionEmpty true

  method getScores () =
    let rec scoreMaker l str =
      match l with
        [] -> str
        | (p, v) :: t ->
          let aPlayerScore = (p#getName()) ^ ":" ^ string_of_int v in
            let concat = if (String.compare str "")==0 then
                            str ^ aPlayerScore
                          else
                            str ^ "|" ^ aPlayerScore in
          scoreMaker t concat
    in
    let pP = self#getPlayerPoints() in
      scoreMaker pP ""

   method getCoords () =
    let rec poseMaker l str =
      match l with
        [] -> str
      | (c,p) :: t -> let (x,y) = (p#getPos()) in
                        let aPlayerCoord = (p#getName())^":X" ^ my_print_float x ^ "Y" ^ my_print_float y in
                          let concat = (if (String.compare str "")==0 then
                                          str ^ aPlayerCoord
                                        else
                                          str ^ "|" ^ aPlayerCoord) in
                        poseMaker t concat
    in
      let pL = self#getPlayerList() in
        poseMaker pL ""

  method getVCoords () =
   let rec poseMaker l str =
     match l with
       [] -> str
     | (c,p) :: t -> let (x,y) = (p#getPos()) in
                      let (vX,vY) = p#getSpeedVec() and angle = p#getAngle() in
                       let aPlayerCoord = (p#getName())^":X" ^ my_print_float x ^ "Y" ^ my_print_float y ^ "VX" ^ my_print_float vX ^ "VY" ^ my_print_float vY ^ "T" ^ my_print_float angle in
                         let concat = (if (String.compare str "")==0 then
                                         str ^ aPlayerCoord
                                       else
                                         str ^ "|" ^ aPlayerCoord) in
                       poseMaker t concat
   in
   let pL = self#getPlayerList() in
     poseMaker pL ""

  method getObjCoord () =
    let (x,y) = self#getNextObj() in
      "X"^my_print_float x^"Y"^my_print_float y


  method private tick () =
     let vcoords = self#getVCoords () in
      let rec send l =
        match l with
          [] -> ()
        | (c,p) :: t -> c#sessionTickV vcoords; send t
      in
      let pL = self#getPlayerList() in
        send pL

   method private win () =
      let scores = self#getScores () in
        let rec winner l =
          match l with
            [] -> ()
          | (c,p) :: t -> c#sessionWin scores; winner t
      in
      let pL = self#getPlayerList() in
        winner pL

      end;;
