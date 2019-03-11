open Game
open Connection_handler

(* session *)
class session l m =
object(self)
  val playerList = l
  val map = m

  method start () =
    print_string "starting game session\n"

end;;

class game_manager =
  object(self)
    val mutable nbPlayerConnected = ref 0
    val mutable isGameRunning = false
    val mutable playerList = ( [] : (connexion_kart * player ) list )
    val nbPlayerBeforeStart = 1

    method run () =
      print_string "game_manager run\n"

    method userConnected con_hand name =
      (
        if self#isNameExist name then
        (
          print_string "userConnected if\n";
          false
          )
        else(
          print_string "userConnected else\n";
          let process () =
          (print_string (name^" connected\n");
          self#alertNewPlayerConnected name;
          incr nbPlayerConnected;
          print_string ("nb player connected : "^ string_of_int(!nbPlayerConnected) ^"\n");
          ignore((con_hand,(new player 0 0 name)) :: playerList);
          if not (self#isGameRunning ()) then
          (
           con_hand#welcome false "" (self#getNextObj());
           if List.length playerList > nbPlayerBeforeStart then
            self#startSession ();
          )
          else
            con_hand#welcome true (self#getScore()) (self#getNextObj()); ) in
         process ();
         true)
      )

    method getNextObj () =
      (
        "3"
      )

    method userDeconnected name = (
      let rec alert l =
        match l with
          | [] -> ()
          | (c,p) :: t -> (c#playerDeconnected name;
                    alert t )
      in
        alert playerList;
        decr nbPlayerConnected
        (*; remove player from list
        playerList <- (
          let rec removePlayer head l =
            match l with
            | (c,p) :: [] when p#getName == name -> []
            | (c,p) :: t when p#getName == name -> t
            | h :: (c,p) :: t when p#getName == name -> h
            | (c, p) :: t -> removePlayer (head :: (c,p)) t
            in
            removePlayer [] playerList
          )
          *)
    )

    method isNameExist name = (*
      let rec search l =
        (match l with
          | [] -> false
          | (c,p) :: t -> (if (String.compare p#getName name)==0 then
                            true
                           else search t)
          )
      in
        search playerList
    *) false

      method getScore () = (
          ""
        )

    method alertNewPlayerConnected name = (
      let rec alert l =
        match l with
          | [] -> ()
          | (c,p) :: t -> (c#newPlayerConnected name;
                    alert t )
      in
        alert playerList
      )

    method isGameRunning () =
      isGameRunning==true

    method startSession () =
    (* compte a rebours *)
    (* puis a la fin du compte a rebours *)
    (* new gameSession *)
      let session = (new session playerList (new gameMap 100 100)) in
        isGameRunning <- true;
        session#start ();
        isGameRunning <- false
  end;;
