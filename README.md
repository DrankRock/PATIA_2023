# PATIA - Sat Planner

Ce dépôt contient le rendu pour le projet de PATIA de Clarisse Deschamps et Matvei Pavlov. 

### Mise en place
```bash
git clone https://github.com/DrankRock/PATIA_2023.git
```

## Sokoban
### Travail Accomplis
- Un domain.pddl qui permet de résoudre des Sokoban présentés sous forme de problème pddl
- Un script java permettant de transformer un problème en json en un problème pddl
- Une interface graphique en python utilisant les elements précédents pour ouvrir un problème depuis un fichier json, le resoudre et affichier la solution

### Lancement
Depuis un Terminal :   
`python SokobanCreator.py`

Dans la fenêtre qui s'ouvre, vous pouvez charger un problème, puis le résoudre. Ci-dessous, une video montrant les fonctionnalités

https://user-images.githubusercontent.com/32172257/235259469-c47eaf10-2e77-409b-a775-35602c8a9b2e.mp4

Nous vous conseillons de ne pas utiliser autre chose que HSP. Nous utilisons les mêmes commandes Java que celles sur la documentation, mais il semble que certaines soient très lentes.

## Planner
### Travail accomplis
- un planner en Java, qui prend un problem.pddl et un domain.pddl, transforme le problème en SAT et résouds le problème en utilisant Sat4j .
- Un script bash pour compiler et exécuter ce planner avec des problèmes et domaines paramétrables.

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
`bash run.sh mr` compiles et lances le planner avec hanoi.    
`bash run.sh r -p customProblem.pddl -d customDomain.pddl` Lances le planner avec comme problème customProblem.pddl et comme domaine customDomain.pddl.   

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
Pour le rendu, il était demandé de rendre un exercice parmi une liste.   
Nous avons choisi de créer un programme pour résoudre le problème de Poursuite - Evasion. Notre domaine et deux exemples de problèmes sont disponibles dans le dossier [Poursuite-Evasion](https://github.com/DrankRock/PATIA_2023/tree/main/Poursuite-Evasion).

Afin de résoudre ces problèmes, vous pouvez lancer la commande : 

```
 java -cp lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.FF
       Poursuite-Evasion/domaine.pddl
       Poursuite-Evasion/p01.pddl
       -t 1000
```

ou

```
 java -cp lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.FF
       Poursuite-Evasion/domaine.pddl
       Poursuite-Evasion/p02.pddl
       -t 1000
```