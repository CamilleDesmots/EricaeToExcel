# EricaeToExcel

## Contexte
A partir des exports de ERICAE créer un fichier EXCEL permettant d'effectuer des statistiques.
Lors de l'export ERICAE crée un fichier avec l'extension ".er2". Le fichier est au format XML.
Le fichier d'export change de format d'un registre à l'autre car dans l'export il n'y a que les propriétés qui ont été utilisées dans ce registre. 
Par exemple si dans un registre vous n'avez aucune sortie, la date de sortie ne figurera pas dans l'export.
L'intégration par import dans EXCEL peut vite devenir compliquée donc...

## But du programme
Le programme va donc créer une feuille EXCEL (.xlsx) avec toutes les données.
Les dates initialement exprimées en Epoch et les codes seront traduits.
