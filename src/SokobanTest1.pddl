(define (problem Sokoban-Test1)
	(:domain SOKOBAN)
	(:objects 
		UP DOWN RIGHT LEFT  - direction
		floor_1_1 floor_2_1  - floor
		box_2_1 - box
	)
	
	(:init
		
		; Pusher is on tile :
		(pusherIsOn floor_1_1)
		
		; Targets are on tiles :
		(isTarget floor_2_1)
		
		; Bpxes are on tiles :
		(hasBoxOn floor_2_1 box_2_1)
		
		; Boxes are on target :
		(onTarget box_2_1)
		
		; Floor is clear :
		
		; Floor 1 is connected to Floor 2:
		(connected floor_1_1 floor_2_1)
		(connected floor_2_1 floor_1_1)
		
		; Going from floor 1, in the direction Direct, you'll find floor 2
		(tileInDirectionOf RIGHT floor_1_1 floor_2_1)
		(tileInDirectionOf LEFT floor_2_1 floor_1_1)
	)
	
	(:goal
		(and
			(onTarget box_2_1)
		)
	)
)
