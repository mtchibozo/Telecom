import argparse
import os

from json import *
from stat import *

"""

objet type passe en argument : "[1,2,5,10,12]"

"""


##################################
### RECUPERATION DE L ARGUMENT ###
##################################


# on lui signale qu il y a un (unique) argument a detecter
parser = argparse.ArgumentParser()
parser.add_argument("levelVectorString")

# args recupere l argument et Liste recupere l objet "liste" de args
args = parser.parse_args()
levelVectorString = args.levelVectorString




######################################################
### FONCTIONS OUTILS : PASSAGE DE JSON A UNE LISTE ###
######################################################


# levelVectorString est une chaine de caracteres, on la convertit en liste
def stringToList(levelVectorString):
    levelVectorList = loads(levelVectorString)
    return levelVectorList



#################
### EXECUTION ###
#################


levelVectorList = stringToList(levelVectorString)

print(levelVectorList)







