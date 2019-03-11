(* virtual connexion handler *)
let gen_num =
	let c = ref 0 in
 ( fun() -> incr c; !c)

class virtual connexion sd (sa : Unix.sockaddr) b =
object (self)
val s_descr = sd
val s_addr = sa
val inchan = Unix.in_channel_of_descr sd
val outchan = Unix.out_channel_of_descr sd

val mutable numero = 0
val mutable debug = b

method set_debug b = debug <- b
 
initializer
 numero <- gen_num();
 if debug then (
  Printf.printf "TRACE.connexion : objet traitant %d cree\n" numero ;
  print_newline())

 method start () = Thread.create (fun x -> self#run x) ()

 method stop() =
  if debug then (
   Printf.printf "TRACE.connexion : fin objet traitant %d\n" numero ;
   print_newline () );
	 close_in inchan;
	 close_out outchan;
   Unix.close s_descr

 method virtual run : unit -> unit
 end ;;
