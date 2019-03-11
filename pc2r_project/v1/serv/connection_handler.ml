open Virtual_connection_handler

(* connexion handler *)
class connexion_kart s sa b gm =
object(self)
  inherit connexion s sa b

val game_manager = gm
val mutable name = ""
val mutable isConnected = true

 method run () =
    let connect = Str.regexp "CONNECT/[a-z]+/" in
    let exit = Str.regexp "EXIT/[a-z]+/" in
    let newPos = Str.regexp "NEWPOS/X[0-9].[0-9]Y[0-9].[0-9]/" in

    let rec input_reader b =
    while b do
        (let input =  input_line inchan in
        print_string("input is  : \n");
        print_string(input);
        print_newline ();
        flush stdout;
        (
        if Str.string_match connect input 0 then
          (self#connect (String.sub input 8 ((String.length input)-10)))
        else if Str.string_match exit input 0 then
          (self#exit (String.sub input 5 ((String.length input)-6)))
        else if Str.string_match newPos input 0 then
          (self#treatCoord (String.sub input 5 ((String.length input)-6)))
          );
        input_reader isConnected)
      done
      in
      input_reader isConnected

 method connect n = (
   if (gm#userConnected self n) then
    name <- n
   else
    output outchan "DENIED/" 0 7
   )

 method welcome b score nextObj = (
   let msg =
     if b then
      "WELCOME/jeu/" ^ score ^ "/" ^ nextObj ^ "/"
     else
      "WELCOME/attente/"
   in
    output outchan msg 0 (String.length msg)
  )

  method newPlayerConnected name = (
    let msg = "NEWPLAYER/" ^ name ^ "/" in
      output outchan msg 0 (String.length msg)
  )
  method playerDeconnected name = (
    let msg = "PLAYERLEFT/" ^ name ^ "/" in
      output outchan msg 0 (String.length msg)
  )

 method exit name = (
   gm#userDeconnected name;
   self#stop ();
   isConnected <- false
  )
 method treatCoord coords = (
  print_string "treatCoord"
  )

end;;
