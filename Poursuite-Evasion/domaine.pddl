(define (domain poursuit-evasion)

;remove requirements that are not needed
(:requirements :strips :typing)

(:types noeud intru)

(:predicates (positionIntru ?ni - noeud)
             (positionPoursuiveur ?np - noeud)
             (tunnel ?n1 - noeud ?n2 - noeud)
             (capture ?n - noeud ?n - noeud)
)

(:action intru-depacement 
    :parameters (?ni ?nd - noeud)
    :precondition (and (positionIntru ?ni)
                       (tunnel ?ni ?nd))
    :effect (and (not (positionIntru ?ni))
                 (positionIntru ?nd))
)

(:action poursuiveur-deplacement
    :parameters (?np ?nd - noeud)
    :precondition (and (positionPoursuiveur ?np)
                       (tunnel ?np ?nd))
    :effect (and (not (positionPoursuiveur ?np))
                 (positionPoursuiveur ?nd))
)

(:action capturer
    :parameters (?n - noeud)
    :precondition (and (positionIntru ?n)
                       (positionPoursuiveur ?n))
    :effect (and (capture ?n ?n) 
                (not (positionIntru ?n))
                (not (positionPoursuiveur ?n)))
)
)