#Partie II : Methodes Quadrature
#les fonctions se trouvent dans mesfonctions.R
load('projectData.Rdata')
source('mesfonctions.R')
source('evalTools.R')

#Rq : Les reponses sont pour a = 1 , b = 2.5

##Q2.1.a

simpsonInt(1,2.5,evalHeight,100) #Une premiere estimation de I est 4.824887

evalErrSimpson(evalHeight,1,2.5,100) #L'erreur a posteriori est d'ordre 10**(-5)

##Q2.1.b.i

#41 evaluations nous donnent un budget de 20 sous-intervalles d'integration

##Q2.1.b.ii

evalErrSimpson(evalHeight,1,2.5,41) #Avec un budget de 41, l'erreur est d'ordre 10**(-4)

##Q2.2

M = evalErrSimpsonLoop(10**(-5)) 
M #Le M le plus petit qui garantit une précision a 10^-5 pres est M = 51

evalErrSimpson(evalHeight,1,2.5,M) # I = 4.825031 a 10^-5 pres


#Partie III est dans Script-partie3.R

