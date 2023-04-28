(define (domain SOKOBAN)

    ;remove requirements that are not needed
    (:requirements :strips :typing)

    (:types ;todo: enumerate types and their hierarchy here e.g. car truck bus - vehicle
        box tile direction - objects ; no pusher object because we only need to know where he is
        floor - tile ; wall tiles are not part of the graph
    )

    ; un-comment following line if constants are needed
    ;(:constants )

    (:predicates ;todo: define predicates here
        (isClear ?f - floor) ;tile of type floor has nothing on it
        (isTarget ?f - floor) ; tile of type floor is a target
        (hasBoxOn ?f - floor ?b - box) ; floor f is occupied by box b
        (pusherIsOn ?f - floor) ;position of pusher on f
        (connected ?from - floor ?to - floor) ;connected tiles
        (tileInDirectionOf ?d - direction ?t1 - floor ?t2 - floor) ; t1 --d--> t2
        (onTarget ?b - box)
        (notIsTarget ?f - floor)
    )

    (:action moveOnEmpty ;move from from to to 
        :parameters (
            ?from - floor
            ?to - floor
            ?d - direction
        )
        :precondition (and 
            (pusherIsOn ?from)
            (isClear ?to)
            (connected ?to ?from)
            (tileInDirectionOf ?d ?from ?to)
            
        )
        :effect (and 
            (not (isClear ?to))
            (not (pusherIsOn ?from))
            (pusherIsOn ?to)
            (isClear ?from)
        )
    )

    (:action moveBox ; move box from normal tile to normal tile
        :parameters (
            ?from - floor
            ?to - floor
            ?toPlusOne - floor
            ?direct - direction
            ?b - box
        )
        :precondition (and 
            (pusherIsOn ?from)
            (hasBoxOn ?to ?b)
            (tileInDirectionOf ?direct ?to ?toPlusOne)
            (tileInDirectionOf ?direct ?from ?to)
            (connected ?from ?to)
            (isClear ?toPlusOne)
            (notIsTarget ?toPlusOne)
            (notIsTarget ?to)
        )
        :effect (and 
            (pusherIsOn ?to)
            (hasBoxOn ?toPlusOne ?b)
            (isClear ?from)
            (not (pusherIsOn ?from))
            (not (hasBoxOn ?to ?b))
            (not (isClear ?toPlusOne))
        )
    )
    (:action moveBoxOnTarget ; move box from normal tile to target tile
        :parameters (
            ?from - floor
            ?to - floor
            ?toPlusOne - floor
            ?direct - direction
            ?b - box
        )
        :precondition (and 
            (pusherIsOn ?from)
            (hasBoxOn ?to ?b)
            (tileInDirectionOf ?direct ?to ?toPlusOne)
            (tileInDirectionOf ?direct ?from ?to)
            (connected ?from ?to)
            (isClear ?toPlusOne)

            (isTarget ?toPlusOne)
        )
        :effect (and 
            (pusherIsOn ?to)
            (hasBoxOn ?toPlusOne ?b)
            (isClear ?from)
            (onTarget ?b)
            
            (not (pusherIsOn ?from))
            (not (hasBoxOn ?to ?b))
            (not (isClear ?toPlusOne))
        )
    )

    (:action moveBoxOutOfTarget ; move box from target tile to normal tile
        :parameters (
            ?from - floor
            ?to - floor
            ?toPlusOne - floor
            ?direct - direction
            ?b - box
        )
        :precondition (and 
            (pusherIsOn ?from)
            (hasBoxOn ?to ?b)
            (tileInDirectionOf ?direct ?to ?toPlusOne)
            (tileInDirectionOf ?direct ?from ?to)
            (connected ?from ?to)
            (isClear ?toPlusOne)

            (notIsTarget ?toPlusOne)
            (isTarget ?to)
            (onTarget ?b)
        )
        :effect (and 
            (pusherIsOn ?to)
            (hasBoxOn ?toPlusOne ?b)
            (isClear ?from)

            (not (onTarget ?b))
            (not (pusherIsOn ?from))
            (not (hasBoxOn ?to ?b))
            (not (isClear ?toPlusOne))
        )
    )
)
