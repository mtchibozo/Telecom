# On va tenter ici de transformer un point, tel qu'il apparait dans les programmes clustering2, en un objet dictionnaire, plus facilement manipulable par json
# Il s'agira ensuite d'adapter le formalisme adopte dans alg0() (un peu plus complexe que celui de clustering2)

# Rappel de la syntaxe utilisee pour un point : [identifiant, numero de cluster, abscisse]

import os

from json import *
from stat import *

print("Hello World from python")
# T = Test2("a",123,456)
#print(T)


def point(a, x, y) :
    P = [a, x, y]
    return P


# fonction qui convertit un point sous forme de liste en un point sous forme de dictionnaire
def point_listToDict(P) :
    D = {"identifiant":"a","numeroDeCluster":0,"abscisse":0}
    D["identifiant"] = P[0]
    D["numeroDeCluster"] = P[1]
    D["abscisse"] = P[2]
    return D


# Convertit une bibliothèque au format Json
def converterToJson(D) :
    J = dumps(D)
    return J
    
    
    
def Test1(a,x,y):
    P = point(a,x,y)
    print("Voici le point avec lequel on travaille : ", P)
    D = point_listToDict(P)
    print("Voici sa version bibliothèque : ", D)
    J = converterToJson(D)
    print("Voici sa version Json : ", J)


# Comme Test1 mais sans les print
def Test2(a,x,y):
    P = point(a,x,y)
    D = point_listToDict(P)
    J = converterToJson(D)
    return J