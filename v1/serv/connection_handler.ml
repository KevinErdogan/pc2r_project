open Virtual_connection_handler

(* connexion handler *)
exception Fin ;;
let my_input_line fd = 
 let s = " " and r = ref "" in
  while(ThreadUnix.read fd s 0 1 > 0) && s.[0] <> '\n'
   do r:= !r ^s done ;
    !r ;;
class connexion_maj sd sa b =
object(self)
 inherit connexion sd sa b
 method run () =
  Printf.printf "Methode run()"
end;;
