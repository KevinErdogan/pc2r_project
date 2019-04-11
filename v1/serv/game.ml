let makeAleaPos maxX maxY =
  let midX = (maxX/.2.0) and midY = (maxY/.2.0) in
    let x = Random.float maxX in
     let y = Random.float maxY in
      (x-.midX, y-.midY)

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
      val angle = ref 0.0
      val speedVec = ref (0.0, 0.0)

      val name = (n : string)
      val angleAdd = 10.0
      val incrSpeed = 1.0
      val maxSpeed = 3.0
      val circleCollider = 10.0
      val pi = 3.14

     method init maxX maxY =
        let newPos = makeAleaPos maxX maxY in
          pos := newPos
      method clock () =
        angle := mod_float (!angle +. angleAdd) (2.0*.pi)
      method anticlock () =
        angle := abs_float(mod_float (!angle -. angleAdd) (2.0*.pi))
      method thrust () =
        let (x,y) = !speedVec in
          let resultX = x +. incrSpeed and resultY = y +. incrSpeed in
            if(resultX < maxSpeed && resultY < maxSpeed) then
              speedVec := (resultX, resultY)
            else if (resultX < maxSpeed) then
              speedVec := (resultX, y)
            else if (resultY < maxSpeed) then
              speedVec := (x, resultY)

      method getPos () =
        !pos
      method getName () =
        name
      method getAngle()=
        !angle
      method getSpeedVec() =
        !speedVec
      method setNewPos x y =
        pos := (x,y)

      method isInCollisionWith (x : float) (y : float) = ()
      (*
        let radius = circleCollider in
          let (w,z) = !pos in
            (w -. colX <= x) && (x <= w +. colX) && (z -. colY <= y) && (y <= z +. colY)*)


    end;;
