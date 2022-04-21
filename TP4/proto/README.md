# Comment exécuter proto

1. Télécharger le fichier zip qui contient le document proto. À l’intérieur, vous devriez trouver ceci :

- proto\
    - main.py
    - setup.py
    - TP1.jar

2) Assurez-vous d’avoir une version de python >= 3.8 et dans le même répertoire que setup.py, exécuter la commande suivante : 

```sh
python -m setup install
```

Vous installerez ainsi toutes les dépendances de *proto* tout en créant une fonction accessible de 
n'importe où sur votre ordinateur par ligne de commande.

Proto peut être exécuter avec différents arguments qui sont résumés dans le tableau suivant.

## Paramètres de *Proto*
| **Paramètre** | **Description**                                                        | **Valeur par défaut**               |
|---------------|------------------------------------------------------------------------|-------------------------------------|
| -h ou  --help | Commande pour afficher l’aide sur les paramètres                       |                                     |
| --url         | Url du répertoire à analyser                                           | https://github.com/jfree/jfreechart |
| --branch-name | Nom de la branch principale à analyser. Généralement  main ou master   | master                              |
| --type        | Extention du type de fichier à analyser                                | java                                |
| --out         | Endroit où les csv produit par le TP1 seront enregistré temporairement | ./output_analyse/                   |
| --start       | Index du commit le plus récent à évaluer                               | 0                                   |
| --end         | Index du commit le plus ancien jusqu’où analyser (0 => Jusqu’à la fin) | 0                                   |


3) Exécuter proto avec la commande suivante pour évaluer les 5 derniers commits: 

```shell
proto --url https://github.com/jfree/jfreechart --start 0 --end 5
```

Le résultat de l'analyse se trouvera dans output.csv à l'endroit que la fonction a été appelé.

4) Une fois terminer, si vous voulez désinstaller proto de votre ordinateur, exécuter : 

```sh
pip uninstall proto -y
```

