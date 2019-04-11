open Connection_handler
open Game_manager

(* virtual server class *)
class virtual server port n =
object(s)
val port_num = port
val nb_pending = n
val sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0

method start () =
	let h_addr = Unix.inet_addr_of_string "127.0.0.1" in
		let sock_addr = Unix.ADDR_INET(h_addr, port_num) in
 			Unix.setsockopt sock Unix.SO_REUSEADDR true;
		 	Unix.bind sock sock_addr ;
     	Unix.listen sock nb_pending ;
		 	print_string("Starting Server on port : "^string_of_int(port_num)^" ...\n");flush stdout;
			while true do
				let(service_sock, client_sock_addr) = Unix.accept sock in
				s#treat service_sock client_sock_addr
			done
method virtual treat : Unix.file_descr -> Unix.sockaddr -> unit
end
;;

(* server class *)
class server_kart port n st srt =
object(s)
 inherit server port n
 val game_manager = (new game_manager st srt)

 method treat s sa =
  ignore( (new connexion_kart s sa true game_manager)#start())
end;;


(* main method *)
let main () =
 if Array.length Sys.argv < 3 then
  print_string("usage : server port num\n")
 else
  let port = int_of_string(Sys.argv.(1))
  and n = int_of_string(Sys.argv.(2)) in
	( let server = (new server_kart port n 2 3) in
		server#start() ) ;;

main();;
