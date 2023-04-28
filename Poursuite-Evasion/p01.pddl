(define (problem p01)
  (:domain poursuit-evasion)
  (:objects a b c d e - noeud)
  (:init
    (tunnel a b)
    (tunnel b a)
    (tunnel b c)
    (tunnel c b)
    (tunnel b d)
    (tunnel d b)
    (tunnel b e)
    (tunnel e b)
    (tunnel d e)
    (tunnel e d)
    (positionIntru c)
    (positionPoursuiveur e)
  )
  (:goal (and (positionIntru a)
              (positionPoursuiveur c)
        )
  )
)
