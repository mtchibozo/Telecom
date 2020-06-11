#Partie III : Methode de Richardson
load('projectData.Rdata')
source('mesfonctions.R')
source('evalTools.R')

#Les fonctions sont dans mesfonctions.R

#La methode de Richardson fonctionne en x = 0
#Il faut utiliser une fonction qui translate evalHeight de 1 : evalHeightTranslate
n=15; t=1; delta = 1/2
#Parametres : n=15. On remarque que les erreurs naives et de Richardson ne varient plus au dela de 15
#Parametres : t=1. Pour t=1 l'erreur richardson est raisonablement proche de l'erreur naive
#Parametres : delta=1/2. Pour delta = 1/2 l'erreur richardson est raisonablement proche de l'erreur naive
plotRichardson(n,t,delta)

