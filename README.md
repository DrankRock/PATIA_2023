# Patia - Sat Planner

Ce repo contient le rendu pour le projet de Patia de Clarisse Deschamps et Matvei Pavlov. 

### Mise en place
```bash
git clone https://github.com/DrankRock/PATIA_2023.git
```

## Sokoban
### Travail Accomplis
- Un domain.pddl qui permet de résoudre des Sokoban presenté sous forme de probleme pddl
- Un script java permettant de transformer un problème en json en un probleme pddl
- Une interface graphique en python utilisant les elements précédents pour ouvrir un probleme depuis un fichier json, le resoudre et affichier la solution

### Lancement
Depuis un Terminal :   
`python SokobanCreator.py`

Dans la fenetre qui s'ouvre, vous pouvez charger un problème, puis le résoudre. Ci dessous, une video montrant les fonctionnalités

https://user-images.githubusercontent.com/32172257/235259469-c47eaf10-2e77-409b-a775-35602c8a9b2e.mp4

Nous vous conseillons de ne pas utiliser autre chose que HSP. Nous utilisons les mêmes commandes Java que celles sur la documentation, mais il semble que certaines soient très lentes.

## Planner
### Travail accomplis
- un planner en Java, qui prends un problem.pddl et un domain.pddl, transforme le problème en SAT et résouds le problème en utilisant Sat4j .
- Un script bash pour compiler et executer ce planneur avec des problèmes et domaines parametrables.

### Lancement
Depuis un Terminal :
#### Commandes possibles
```bash
$ bash run.sh --help
Usage: ./run.sh [OPTIONS] [ARGUMENTS]

Arguments:
  m, make       - Compile the code
  r, run        - Run the code
  mr, make-run  - Compile and run the code
  -h, --help    - Show this help

Options:
  -p <problem>  - Specify the problem.pddl to run
  -d <domain>    - Specify the domain.pddl to run

Notes : By default, the problem and domain are for hanoi
```
#### Exemples
`bash run.sh mr` compiles et lances le planner avec hanoi   
`bash run.sh r -p customProblem.pddl -d customDomain.pddl` Lances le planneur avec comme problème customProblem.pddl et comme domaine customDomain.pddl.   

#### Sortie pour le hanoi_problem.pddl
```
.. Recapitulatif de chaque etape ..
[OurPlanner] * * Deepness 14 is satisfiable !

found plan as follows:

00: (   unstack bleu blanc) [0]
01: (  put-down bleu right) [0]
02: (  unstack blanc rouge) [0]
03: (put-down blanc middle) [0]
04: (   pick-up bleu right) [0]
05: (     stack bleu blanc) [0]
06: (   pick-up rouge left) [0]
07: ( put-down rouge right) [0]
08: (   unstack bleu blanc) [0]
09: (   put-down bleu left) [0]
10: ( pick-up blanc middle) [0]
11: (    stack blanc rouge) [0]
12: (    pick-up bleu left) [0]
13: (     stack bleu blanc) [0]

time spent:       0.00 seconds parsing 
                  0.03 seconds encoding 
                  0.00 seconds searching
                  0.03 seconds total time

memory used:      0.22 MBytes for problem representation
                  0.00 MBytes for searching
                  0.22 MBytes total
```

## Poursuite - Evasion
### Travail Accomplis
Pour le rendu, il etait demandé de rendre un exercice parmis une liste.   
Nous avons choisis de créer un programme pour résoudre le problème de Poursuite - Evasion. Notre domaine et deux exemples de problèmes sont disponibles dans le dossier [Poursuite-Evasion](https://github.com/DrankRock/PATIA_2023/tree/main/Poursuite-Evasion).

Malheuresement, nous n'avons pas de script de lancement sur ce dépôt, ils sont sur le reseau de l'IM2AG. Nous pouvons cependant vous l'envoyer si vous le souhaitez dès que possible. Nous l'ajouterons dans tous les cas à ce dépôt dès que nous l'aurons.
