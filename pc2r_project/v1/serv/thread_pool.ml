open Condition
    (*
class queue nb =
val tab
val allocsize = nb
val begin = 0
  val sz = 0
  val isBlocking = 1
  mutex m
  t cv = Condition.create
  method empty () =
    sz==0
  method full () =
    sz == allocsize
  initializer
    tab = ...
  method size () =
    mutex.lock;
    sz
  method setBlocking =
    mutex.lock
      isBlocking = !isBlocking
  method pop =
    while empty () && isBlocking do
      cv#create mutex
    done
      if empty () then null
          if full () then cv#notify_all()
              (* ... *)
end;;


class thread_pool nb =
  object(self)
    val nb_max = nb

    method create () =


  end;;
*)
