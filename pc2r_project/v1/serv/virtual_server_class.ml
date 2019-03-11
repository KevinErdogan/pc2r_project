(* open Async_unix.Thread_pool *)

(* virtual server class *)
class virtual server port n =
object(s)
val port_num = port
val nb_pending = n
val sock = ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0
(* t = Async_unix.Thread_pool.create 8 *)
    (*game_manager gm = new game_manager*)

method start () =
(* let host = Unix.gethostbyname (Unix.gethostname()) in *)
	let h_addr = Unix.inet_addr_of_string "127.0.0.1" in
		let sock_addr = Unix.ADDR_INET(h_addr, port_num) in
 (*Unix.setsockopt sock SO_REUSEADDR true;*)
		 Unix.bind sock sock_addr ;
     Unix.listen sock nb_pending ;
		 print_string("Starting Server on port : "^string_of_int(port_num)^" ...\n");
			while true do
				let(service_sock, client_sock_addr) = ThreadUnix.accept sock in
				s#treat service_sock client_sock_addr
			done
method virtual treat : Unix.file_descr -> Unix.sockaddr -> unit
end
;;
