open Virtual_server_class
open Connection_handler

(* server class *)
class server_maj port n =
object(s)
 inherit server port n
 method treat s sa =
  ignore( (new connexion_maj s sa true)#start())
end;;


(* main method *)
let main () = 
 if Array.length Sys.argv < 3 then 
  print_string("usage : server port num\n")
 else
  let port = int_of_string(Sys.argv.(1))
  and n = int_of_string(Sys.argv.(2)) in
	( 
	print_string("Starting Server on port : "^string_of_int(port)^" ...\n");	
	let server = (new server_maj port n ) in
		server#start() ) ;;

main();;
