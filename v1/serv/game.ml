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
      val ve_radius = 10.0 (* rayon de collision d'un vehicule, hitbox*)

     method init maxX maxY =
        let newPos = makeAleaPos maxX maxY in
          pos := newPos
      method clock () =
        (*angle := mod_float (!angle +. angleAdd) (2.0*.pi)*)
	angle := mod_float (!angle +. angleAdd) (360.0)
      method anticlock () =
        (*angle := abs_float(mod_float (!angle -. angleAdd) (2.0*.pi))*)
	angle := mod_float (!angle -. angleAdd +. 360.0) (360.0)
      method thrust () =
        let (x,y) = !speedVec in
          let resultX = x +. incrSpeed and resultY = y +. incrSpeed in
            if(resultX < maxSpeed && resultY < maxSpeed) then
              speedVec := (resultX, resultY)
            else if (resultX < maxSpeed) then
              speedVec := (resultX, y)
            else if (resultY < maxSpeed) then
              speedVec := (x, resultY)


      method move () = (* avance, arene thorique *)
	let (x,y) = !pos in
	 let (vx,vy) = !speedVec in
	  let newX = (x + vx + map#getWidth ()) mod map#getWidth () in
	  let newY = (y + vy + map#getHeight ()) mod map#getHeight () in
	  pos := (newX, newY)

      method getPos () =
        !pos
      method getName () =
        name
      method getAngle()=
        !angle
      method getSpeedVec() =
        !speedVec
      method setSpeedVec vx vy =
	speedVec := (vx,vy)
      method setNewPos x y =
        pos := (x,y)

      method isInCollisionWith x y =
          let (w,z) = !pos in
	    if (((w -. x) *. (w -. x) +. (z -. y) *. (z -. y)) <= (ve_radius *. ve_radius)) then
		true
	    else 
		false


    end;;
