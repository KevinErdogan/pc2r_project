class gameMap h w =
  object(self)
    val xTor=0.0
    val yTor=0.0
    val height = h
    val width = w
  end;;

  (* player *)
  class player x y n =
    object(self)
      val mutable x = x
      val mutable y = y
      val mutable name = (n : string)
      val mutable theta = 0.0
      val delta = 10.0
      val mutable vX = 0.0
      val mutable vY = 0.0
      val maxVX = 10.0
      val maxVY = 10.0

      method move () =
        print_string "move player"
      method clock () =
        print_string "clock method"
      method anticlock () =
          print_string "anticlock method"
      method boost () =
        print_string "boost method"
      method getName () =
        name

    end;;
