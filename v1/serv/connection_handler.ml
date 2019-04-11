open Virtual_connection_handler

(* connexion handler *)
class connexion_kart s sa b gm =
object(self)
  inherit connexion s sa b

val m = Mutex.create ()
val game_manager = gm
val name = ref ""
val isRun = ref true
val isConnected = ref false

 method run () =
    let connect = Str.regexp "CONNECT/[a-z]+/"
    and exit = Str.regexp "EXIT/[a-z]+/"
    (* and newPos = Str.regexp "NEWPOS/X[0-9]+.[0-9]+Y[0-9]+.[0-9]+/" REMPLACER PAR NEWCOM *)
    and newCom = Str.regexp "NEWCOM/A-?[0-9]+.[0-9]+T[0-9]+/" in

    let rec input_reader b =
        (if !b then
         (
          try
            let input =  input_line inchan in
            (
              print_string("Input is : " ^ input ^"\n");flush stdout;
              (
                if Str.string_match connect input 0 then
                  self#connect (String.sub input 8 ((String.length input)-9))
                else if Str.string_match exit input 0 then
                  self#exit (String.sub input 5 ((String.length input)-7))
                (*else if Str.string_match newPos input 0 then
                  self#newPos (String.sub input 7 ((String.length input)-9))*)
                  else if Str.string_match newCom input 0 then
                    self#newCom (String.sub input 7 ((String.length input)-9))
              );
              input_reader b
            )
            with
              e -> let msg = Printexc.to_string e in
                    print_string(msg);flush stdout;
                    if !isRun then (* ici connexion perdu, sinon le client a appele EXIT *)
                    self#exit !name
          )
        )
      in
        input_reader isRun

 method connect n =
   if (not(!isConnected) && game_manager#userConnected self n) then
    (name := n;
    isConnected:=true)
   else
    (self#sendOut "DENIED/\n";
    isRun := false;
    self#stop ())

 method welcome b score nextObj ocoords=
   let msg =
     if b then
      "WELCOME/jeu/" ^ score ^ "/" ^ nextObj ^ "/" ^ ocoords ^"/\n"
     else
      "WELCOME/attente/\n"
      (*"WELCOME/attente/"^ score ^ "/" ^ nextObj ^ "/" ^ ocoords ^"/\n"*) (*protocole C ??*)
   in
    self#sendOut msg

  method newPlayerConnected name =
    let msg = "NEWPLAYER/" ^ name ^ "/\n" in
      self#sendOut msg

  method playerDeconnected name =
    let msg = "PLAYERLEFT/" ^ name ^ "/\n" in
      self#sendOut msg

  method startSession coords coord =
    let msg = "SESSION/" ^ coords ^ "/"^ coord ^"/\n" in
      self#sendOut msg

  method sessionNewObj coord scores =
    let msg = "NEWOBJ/" ^ coord ^"/"^ scores ^"/\n" in
      self#sendOut msg

  method sessionTickV vcoords =
    let msg = "TICK/" ^ vcoords ^"/\n" in
      self#sendOut msg

  method sessionWin scores =
    let msg = "WINNER/" ^ scores ^"/\n" in
      self#sendOut msg

  method sendOut msg =
    Mutex.lock m;
    output outchan msg 0 (String.length msg);
    flush outchan;
    Mutex.unlock m

  method exit n =
   if !isConnected && (String.compare !name n)==0 then
    (game_manager#userDeconnected !name;
    isRun := false;
    self#stop ())

(*
  method newPos (coord:string) =
    if !isConnected && (game_manager#isGameRunning ()) then
      (game_manager#getCurrentSession())#receiveNewPos self coord
*)
  method newCom comms =()
  (*
        if !isConnected && (game_manager#isGameRunning ()) then
          (game_manager#getCurrentSession())#receiveNewCom self comms *)

end;;
