(* virtual server class *)
class virtual server port n =
object(s)
val port_num = port
val nb_pending = n
val sock = ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0

method start () =
let host = Unix.gethostbyname (Unix.gethostname()) in
	let h_addr = host.Unix.h_addr_list.(0) in
		let sock_addr = Unix.ADDR_INET(h_addr, port_num) in
			let str = Unix.gethostname() in
			print_string("listening to : "^str);
		     	Unix.bind sock sock_addr ;
			Unix.listen sock nb_pending ;
			while true do
				let(service_sock, client_sock_addr) = ThreadUnix.accept sock in
				s#treat service_sock client_sock_addr
			done
method virtual treat : Unix.file_descr -> Unix.sockaddr -> unit
end
;;
