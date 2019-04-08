let makeAleaPos minX maxX minY maxY =
  let x = Random.float maxX in
   let y = Random.float maxY in
    (x-.minX, y-.minY)

class gameMap h w =
  object(self)
    val center = ref (0.0, 0.0)
    val height = (h : float)
    val width = (w : float)

    method getHeight () =
      height
    method getWidth () =
      width
  end;;

  (* player, un vehicule *)
  class player n =
    object(self)
      val pos = ref (0.0, 0.0)
      val name = (n : string)
      val v = ref (0.0, 0.0)
      val angle = ref (0.0, 0.0)
      val angleAdd = 10.0
      val speed = ref 0
      val incrSpeed = 1
      val maxSpeed = 3
      val collider = (10.0, 10.0)

     method init minX maxX minY maxY =
        let newPos = makeAleaPos minX maxX minY maxY in
          pos := newPos

     method getPos () =
        !pos
      method move () =
        print_string "move player";flush stdout
      method clock () =
        direction := !direction +. dirAngle
      method anticlock () =
        direction := !direction -. dirAngle
      method thrust () = (* pas exactement ca *)
        let (x, y) = !speed in
          speed := (x +. incrSpeed, y +. incrSpeed)
      method getName () =
        name
      method getAngle()=
        !angle
      method getSpeed() =
        !speed
      method getV() =
        !v
      method setNewPos x y =
        pos := (x,y);
        print_string("newPos : ("^string_of_float x^","^string_of_float y^")\n");flush stdout
      method isInCollisionWith (x : float) (y : float) =
        let (colX, colY) = collider in
          let (w,z) = !pos in
            (w -. colX <= x) && (x <= w +. colX) && (z -. colY <= y) && (y <= z +. colY)

    end;;
