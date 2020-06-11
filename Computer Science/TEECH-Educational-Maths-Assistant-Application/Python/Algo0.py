"""
Lexo : liste des exercices a recuperer de la base de donnees
Un exercice est une liste composee de 3 elements: son identifiant, un vecteur 
dont les coordonnees sont les proportions de chaque matiere, et un vecteur dont
les coordonnees sont les niveaux de difficulte de chaque matiere. Ainsi, on a 
pour i un entier, Lexo[i] = [ id, v1, v2]

Dans cet algorithme simplifie, on considere que l'utilisateur ne veut travailler
qu'une seule matiere, son indice sera designe par p.
On note lp le niveau de l'eleve dans cette matier, il appartient a {0,1,2,3}

"""

import numpy as np
import scipy as sc
import clustering2 as cl

def exos_a_traiter (p, lp, Lexo):

    L = [] # liste des suggestions 

    for i in range (0, len(Lexo)) : 

        e = Lexo[i]

        if e[2][p] == lp :

            L = L + [e]  # ajout dans L des exercices de niveau l(p)

    P=sorted(L, reverse = True,  key=lambda z: z[1][p]) #effectue le tri des exos sur le critere de la proportion de p
    

    return P





"""Android recoit la liste d'exos a traiter et les suggere un par un a l'utilisateur qui, a chaque fois qu'il en complete un, declare s'il l'a fait facilement ou non
Android renvoie un doublet (e,note) avec e l'exercice fait, et note la note de l'exercices"""



def ajout_exercice_traite (Lb, e, note) :# Lb est la liste des exos traites, chaque element de la liste est un vecteur dont la premiere coordonnee est l'exercice et la deuxieme le taux de reussite

    Lb = Lb + [ [e, b] ]


def passage_au_niveau_suivant (lp, Lb) :

    n = len(Lb)

    moy = 0  # taux de reussite sur les 10 derniers exercices effectues

    if n > 10 :

        for k in range (n-11, n-1) :

            moy = moy + Lb[k][1]

        moy = moy/10
        
        if 1<lp<3:
            
            if moy >= 0.7 :
                lp = lp + 1
            elif 0.2 < moy < 0.7 :
                lp = lp
            else :
                lp = lp - 1
                
        elif lp == 3 :
            if moy >= 0.7 :
                print ("Bravo !" )
            elif 0.2 < moy < 0.7 :
                lp = lp
            else :
                lp = lp - 1
                
        elif lp == 1 :
            if moy >= 0.7 :
                lp = lp + 1
            elif 0.2 < moy < 0.7 :
                lp = lp
            else :
                print("Courage, tu peux mieux faire!")
            
        
            
""" Desormais, il serait interessant d'etudier les cas d'egalite dans exos_a_traiter, cad lorsque deux exercices sont identiques.
L' algorithme suivant servirait a les departager . On propose d'effectuer un partitionnement des donnees base sur l'utilite de chaque exercice.
On utilise notamment l'algorithme DBscan """


""" La fonction suivante donne les different paquets d'exercices. Les exercices du meme paquets ont la meme proportion 
dans la matiere p"""

def exos_similaires(L,p):
    n= len(L)
    paquets=[[] for k in range(0,n)]
    for i in range(0,n):
        t=i
        while t<n and L[i][1][p]==L[t][1][p] :
            paquets[i] = paquets[i] + [L[t]]
            t = t+1
    s= []  # le reste du code permet d'eviter la repetion d'un meme exercices dans deux paquets differents
    for j in range(0,len(paquets)):
        s = s + [len(paquets[j])]
    P = []
    j=0
    while j<len(s):
        if s[j]==1:
            P = P + [paquets[j]]
            j = j + 1
        else:
            r = s[j]
            P = P + [paquets[j]]
            j = j + r
            
    return P

def distance(u,v):
    return sc.spatial.distance.euclidean(u, v) # represente la distance euclidienne entre deux vecteurs

#On pose e_u l'exercice utile moyen
# les parametres suivant sont a determiner
epsilon = 0.3
MinPts= 10



def recommandation(p, lp, Lexo,e_u):
    
    L= exos_a_traiter(p,lp,Lexo) 
    paquets = exos_similaires(L,p)
    D=[[] for k in range(0,len(paquets))]
    
    for i in range(0,len(paquets)):
        if len(paquets[i]) == 1:
            D[i] = paquets[i]
        else:   # on effectue le clustering lorsqu'un paquet contient plus d'un exercice
            
            for j in range(0,len(paquets[i])):
                D[i]=D[i]+[[paquets[i][j][0],0,distance(paquets[i][j][1],e_u[1]), distance(paquets[i][j][2],e_u[2])]] 
            # on effectue un clustering selon la distance entre les vecteurs proportion dans chaque paquet
            D[i]=cl.DBSCAN(D[i], epsilon, MinPts)
            D[i] = sorted(D[i],key=lambda x:(x[1],x[3]))  # on trie selon le deuxieme critere ( distance entre vecteur difficulte
    # le reste du code permet de garder les identifiant de chaque vecteur
    R=[]
    for k in range(0,len(D)):
        b = len(D[k])
        for i in range(0,b):
            R = R + [D[k][i][0]]
            
    return R
            
        

    
    
        
    
            
            
        
    
    

            

    




