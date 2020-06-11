import argparse
import os

from json import *
from stat import *

"""

PRINCIPE GENERAL :

recoit les exercices faits par l'utilisateur ainsi que son niveau actuel (vecteur), et renvoie son nouveau
niveau (vecteur)


TYPE D'ARGUMENT :

On va recevoir en argument des objets de cette sorte :
'[{"id":0,"weightsVector":[0.5,0.25,0.25],"difficultiesVector":[1,2,3],"mark":2},{"id":111,"weightsVector":[0.1,0.1,0.1],"difficultiesVector":[1,1,1],"mark":6},{"id":222,"weightsVector":[0.2,0.2,0.2],"difficultiesVector":[2,2,2],"mark":9}]' '[1,2,5]'


SCHEMAS DE FONCTIONNEMENT :



"""






##################################
### RECUPERATION DE L ARGUMENT ###
##################################


# on lui signale qu il y a un (unique) argument a detecter
parser = argparse.ArgumentParser()
parser.add_argument("completedExerciceString")
parser.add_argument("levelTableString")

# args recupere l argument et Liste recupere l objet "liste" de args
args = parser.parse_args()
levelTableString = args.levelTableString
completedExerciceString = args.completedExerciceString





######################################################
### FONCTIONS OUTILS : levelTable ####################
######################################################

# AVANT LE TRAITEMENT #

# levelTableString est une chaine de caracteres, on la convertit en liste
def levelTable_StringToList(levelTableString):
    levelTableList = loads(levelTableString)
    return levelTableList

# APRES LE TRAITEMENT #

# levelTableList est une liste, on souhaite la convertir en objet Json
def levelTable_ListToJson(levelTableList):
    levelTableJson = dumps(levelTableList)
    return levelTableJson


###################################################################
### FONCTIONS OUTILS : completedExerciceString ####################
###################################################################


# AVANT LE TRAITEMENT #

# completedExerciceString est une chaine de caracteres, et on veut la convertir en liste de dict (grace a json)
def completedExercice_StringToDictList(completedExerciceString):
    completedExerciceDictList = loads(completedExerciceString)
    return completedExerciceDictList


# dictList est une liste de dictionnaires, on souhaite transformer chaque dictionnaire en liste
def completedExercice_DictListToListList(completedExerciceDictList):
    completedExerciceListList = []
    for dictCompletedExercice in completedExerciceDictList :
        listCompletedExercice = completedExercice_DictToList(dictCompletedExercice)
        completedExerciceListList.append(listCompletedExercice)
    return completedExerciceListList


# dictCompletedExercice est UN dictionnaire, on souhaite le transformer en liste
def completedExercice_DictToList(dictCompletedExercice):
    #creation de la liste
    listCompletedExercice = []
    # remplissage de la liste
    listCompletedExercice.append(dictCompletedExercice["id"])
    listCompletedExercice.append(dictCompletedExercice["weightsVector"])
    listCompletedExercice.append(dictCompletedExercice["difficultiesVector"])
    listCompletedExercice.append(dictCompletedExercice["mark"])

    return listCompletedExercice


"""
# APRES LE TRAITEMENT #

def completedExercice_ListListToDictList(completedExerciceListList):
    completedExerciceDictList = []
    for listCompletedExercice in completedExerciceListList :
        dictCompletedExercice = completedExercice_ListToDict(listCompletedExercice)
        completedExerciceDictList.append(dictCompletedExercice.copy())
        
    return completedExerciceDictList


def completedExercice_ListToDict(listCompletedExercice):
    dictCompletedExercice = {}
    dictCompletedExercice["id"]=listCompletedExercice[0]
    dictCompletedExercice["weightsVector"] = listCompletedExercice[1]
    dictCompletedExercice["difficultiesVector"] = listCompletedExercice[2]
    dictCompletedExercice["mark"] = listCompletedExercice[3]
    
    return dictCompletedExercice


def completedExercice_DictToJson(completedExerciceDictList):
    completedExerciceJson = dumps(completedExerciceDictList)
    return completedExerciceJson
"""


###########################
### APPEL DE TRAITEMENT ###
###########################


def callForUpdate(levelTableList, completedExerciceListList):
    # appeler algo0()
    newLevelTableList = levelTableList
    return newLevelTableList



#################
### EXECUTION ###
#################


# Le premier argument recu "completedExerciceString" (de type string) est transforme en liste de dictionnnaires
completedExerciceDictList = completedExercice_StringToDictList(completedExerciceString)

# On transforme la liste de dictionnaires completedExerciceDictList en liste de listes
completedExerciceListList = completedExercice_DictListToListList(completedExerciceDictList)

# Le second argument recu "levelTableString" (de type string) est transforme en liste
levelTableList = levelTable_StringToList(levelTableString)


# TRAITEMENT DE levelTableList GRACE A completedExerciceListList
levelTableList = callForUpdate(levelTableList, completedExerciceListList)


# levelTableListe est de type list, on souhaite la transformer en dictionnaire
levelTableJson = levelTable_ListToJson(levelTableList)

# on affiche l'objet json obtenu (afin qu'il soit lu par java)
print(levelTableJson)

