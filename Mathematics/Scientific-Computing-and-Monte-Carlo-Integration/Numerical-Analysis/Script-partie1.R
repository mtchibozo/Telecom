load('projectData.Rdata')
source('mesfonctions.R')
source('evalTools.R')

#Les fonctions se trouvent dans mesfonctions.R
#Rq: les reponses sont pour a = 1, b = 2.5

###Interpolation Polynomiale###

#Q1.a
###Noeuds Equidistants###
##Degre trop bas##
interpolLagrange(3,-1,1,100,'equi',runge,TRUE) #Pour un degre trop petit, l'interpolation est aberrante

##Degre trop eleve##
interpolLagrange(70,-1,1,100,'equi',runge,TRUE) #Pour un degre trop eleve, les erreurs deviennent trop importantes aux extremites

###Noeuds Tchebychev###
##Degre trop bas##
interpolLagrange(3,-1,1,100,'cheby',runge,TRUE) #Pour un degre trop petit, l'interpolation est abherrante

##Degre trop eleve##
interpolLagrange(70,-1,1,100,'cheby',runge,TRUE) #Pour un degre trop eleve, les erreurs deviennent trop importantes aux extremites

#Q1.b
interpolLagrange(15,-1,1,100,'cheby',runge,TRUE) #Pour ces parametres, l'interpolateur est satisfaisant

#Q1.c
###Noeuds equidistants###
#On conjecture que le rang n sera inferieur a 50

errorRank('equi')[1] #rang de la methode qui minimise l'erreur d'interpolation sur evalHeight : 3

#pour verifier la liste d'erreurs : erreurRank('equi')[2]

###Noeuds Tchebychev###

errorRank('cheby')[1] #rang de la methode qui minimise l'erreur d'interpolation sur evalHeight : 36

#pour verifier la liste d'erreurs : erreurRank('cheby')[2]

#Q2.a
piecewiseInterpol(10,15,1,2.5,10,"equi",evalHeight,TRUE)

#Q2.b

errorRank2(80)  #on trouve une erreur minimale pour  
#n = 24 noeuds 
#de type = 'cheby'
#neval = 80 intervalles


##Partie II : ouvrir Script-partie2.R