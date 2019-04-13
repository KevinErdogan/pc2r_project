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
  class player n mapHei mapWid =
    object(self)
      val pos = ref (0.0, 0.0)
      val angle = ref 0.0
      val speedVec = ref (0.0, 0.0)
      val mapH = mapHei
      val mapW = mapWid
      val name = (n : string)
      val score = ref 0
      val thrustit = 0.1
      val maxSpeed = 1.0
      val minSpeed = -1.0
      val circleCollider = 10.0
      val pi = (4.0 *. atan 1.0)
      val turnit = (4.0 *. atan 1.0) /. 10.0
      val ve_radius = 10.0 (* rayon de collision d'un vehicule, hitbox*)
      val vo_radius = 25.0 (* hitbox obstacle *)

     method init maxX maxY =
        let newPos = makeAleaPos maxX maxY in
          pos := newPos
      method clock () =
	angle := (!angle -. turnit)
      method anticlock () =
	angle := (!angle +. turnit)
      
      method thrust () = (* modifie le vecteur vitesse *)
	let (vx,vy) = !speedVec in
	 let resultX = vx +. thrustit *. (cos !angle) 
	 and resultY = vy +. thrustit *. (sin !angle) in
         (if (resultX < maxSpeed && resultX > minSpeed && 
		resultY < maxSpeed && resultY > minSpeed) then
		speedVec := (resultX, resultY)
	 else if (resultX < maxSpeed && resultX > minSpeed) then
		speedVec := (resultX, vy)
	 else if (resultY < maxSpeed && resultY > minSpeed) then
		speedVec := (vx, resultY));


      method move () = (* avance, arene thorique *)
	let (x,y) = !pos in
	 let (vx,vy) = !speedVec in
	  let newX = mod_float (x +. vx +. mapW)  (mapW) in
	  let newY = mod_float (y +. vy +. mapH) (mapH) in
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
      method getScore () = 
	!score
      method setScore s =
        score := s

      method isInCollisionWith x y =
          let (w,z) = !pos in
	    if (((w -. x) *. (w -. x) +. (z -. y) *. (z -. y)) <= (ve_radius *. ve_radius)) then
		true
	    else 
		false

     method isInCollisionWithObs x y =
          let (w,z) = !pos in
	    if (((w -. x) *. (w -. x) +. (z -. y) *. (z -. y)) <= (ve_radius *. vo_radius)) then
		true
	    else 
		false

      method addCommAngle toAdd =
	angle := !angle +. toAdd

      method addCommsNbThrust  n =
	let rec thrustXTimes x = 
	   match x with 
	    0 -> ()
	    | _ -> self#thrust (); thrustXTimes (x-1)
	 in thrustXTimes n


    end;;
