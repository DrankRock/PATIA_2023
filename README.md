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
