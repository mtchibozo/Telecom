import argparse
import os

from json import *
from stat import *


"""

NB1 : Ce programme a pour vocation d'etre execute depuis un programme java ou une fenetre de terminal. En ce sens, il faut bien veiller a ce que les instructions proprement dites, comme "dictList = liste_stringToList(liste)" (par opposition aux definitions de fonctions comme "def liste_stringToList(liste)" ou les imports) soient ecrites a la fin du programme (dans la partie EXECUTION), ie a la fin des definitions, car autrement, le programme ne comprend pas les appels.



NB2 : Dans ce programme, il est souvent question de "version 1", "version 2". Cela fait allusion aux deux versions possibles d un exercice, selon qu on les voit sous l angle de alg0 ou de clustering.

VERSION 1 : [id, weightsVector, difficultiesVector]
VERSION 2 : [id, numeroDeCluster, abscisse]



*** OBJECTIF DU PROGRAMME ***

- transformer l argument qu on lui passe en liste d exercices. Dans ce programme, l exercice est defini ainsi en premiere version : [identifiant, numero de cluster, abscisse]
et ainsi en deuxieme version : [id, weightsvector, difficultiesVector]
pour le choix de la version, choisir l une des deux fonctions point_dictToList dans la partie execution


*** FONCTIONNEMENT DU PROGRAMME ***

- le programme peut etre lance depuis le terminal, en tapant l'instruction suivante :
python + chemin du programme + liste d'exercices sous la forme d'une chaine de caracteres (attention au formalisme!)
ex (version 1) :
python <chemin du programme> '[{"identifiant": "a", "numeroDeCluster": 123, "abscisse": 456}]'
ex (version 2) :
python <chemin du programme> '[{"id":0,"weightsVector":[0.5,0.25,0.25],"difficultiesVector":[1,2,3]},{"id":111,"weightsVector":[0.1,0.1,0.1],"difficultiesVector":[1,1,1]},{"id":222,"weightsVector":[0.2,0.2,0.2],"difficultiesVector":[2,2,2]}]'

ATTENTION DERNIERE MODIFICATION : vu que finalement on recupere les json d'objets java plus complexes, la commande qui sera desormais envoyee sera plutot de la forme :
'{"sortedIndicator" : 0, "exerciceTable" : "[{"id":0,"weightsVector":[0.5,0.25,0.25],"difficultiesVector":[1,2,3]},{"id":111,"weightsVector":[0.1,0.1,0.1],"difficultiesVector":[1,1,1]},{"id":222,"weightsVector":[0.2,0.2,0.2],"difficultiesVector":[2,2,2]}]"}'
--> ouai non bof ce truc en fait


- point important : il se peut que la definition d un point change en fonction des besoins. Dans ce cas, c est la fonction "exercice_dictToList(D)" qu il faut remplacer


*** SCHEMA GLOBAL DU PROGRAMME ***

##################################################################################################
"liste"  --->  dictList  --->  listsList  --->  ...  --->  sortedListsList  --->  sortedDictList
##################################################################################################

ou :
-liste : chaine de caractere recue lors de l execution du programme
-dictList : objet de type liste de dictionnaires obtenu par la conversion de la chaine liste
-listsList : objet de type liste de listes (et pas de dictionnaires)
-sortedListsList : liste de listes(=exos), rangees dans un ordre intelligent (cad qu elles sont dans l ordre dans lequel l'eleve doivent les bosser)
-sortedDictList : liste de dictionnaires(=exos), rangees dans un ordre intelligent (cad qu elles sont dans l ordre dans lequel l'eleve doivent les bosser)

"""




##################################
### RECUPERATION DE L ARGUMENT ###
##################################


# on lui signale qu il y a un (unique) argument a detecter
parser = argparse.ArgumentParser()
parser.add_argument("liste")

# args recupere l argument et Liste recupere l objet "liste" de args
args = parser.parse_args()
liste = args.liste




######################################################
### FONCTIONS OUTILS : PASSAGE DE JSON A UNE LISTE ###
######################################################


# liste est une chaine de caracteres, et on veut la convertir en liste (grace a json)
def liste_stringToList(liste):
    dictList = loads(liste)
    return dictList


# dictList est une liste de dictionnaires, on souhaite transformer chaque dictionnaire en liste
def dictListToListsList(dictList):
    listsList = []
    for dictionary in dictList :
        L = exercice_dictToList2(dictionary)
        listsList.append(L)
    return listsList


def listsListToDictList2(L):
    dictList = []
    for Point in L :
        D = {}
        D["id"]=Point[0]
        D["weightsVector"] = Point[1]
        D["difficultiesVector"] = Point[2]
        dictList.append(D.copy())
    return dictList


# P est une chaine de caracteres, et on veut le convertir en dictionnaire (grace a Json)
def point_stringToDict(Point):
    D = loads(Point)
    return D


# P est deja un dictionnaire, et on le transforme en liste (version 1)
def exercice_dictToList1(D):
    #creation de la liste
    L = []
    # remplissage de la liste
    L.append(D["identifiant"]) # ATTENTION C EST PAS BON CA
    L.append(D["numeroDeCluster"])
    L.append(D["abscisse"])
    return L


# P est deja un dictionnaire, et on le transforme en liste (version 2)
def exercice_dictToList2(D):
    #creation de la liste
    L = []
    # remplissage de la liste
    L.append(D["id"])
    L.append(D["weightsVector"])
    L.append(D["difficultiesVector"])
    return L


#################
### EXECUTION ###
#################


# L'argument recu "liste" (de type string) est transforme en liste de dictionnnaires
dictList = liste_stringToList(liste)

# La liste de dictionnaires dictList est transformee en liste de listes listsList
listsList = dictListToListsList(dictList)

# TRAITEMENT DE listsList
listsList.append([88,[0,0,0],[0,0,0]])
  #on imagine un traitement qui doit aboutir a une sortedListsList...
  # En fait on va peut etre l envoyer a un autre programme qui analysera lui le vecteur level dans chaque matiere
sortedListsList = listsList


# Conversion en dictionnaire
sortedDictList = listsListToDictList2(sortedListsList)


# Conversion au format json (ie string)
json = dumps(sortedDictList)

# on affiche l'objet json obtenu (afin qu'il soit lu par java)
print(json)