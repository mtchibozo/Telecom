"""

OBJECTIF : faire des paquets (clusters) d'exercices sur l'axe des abscisse
du graphe "distance de l'exo a l'exo utile moyen
 
Ce graphe est suppose avoir ete construit de la facon suivante 
- axe des abscisses : distance de l'exercice a l'exercice utile moyen, du point de vue des poids
- axe des ordonnees : distance de l'exercice a l'exercice utile moyen, du point de vue de la difficulte

"""



# On definit P comme un triplet : [ref sur l'exo, numero du cluster qui le contient, abscisse]

# le numero de cluster 0 correspond aux points "bruits" (du coup, la liste BRUITS definie dans DBSCAN n'est pas absolument necessaire au bon fonctionnement du programme)



# donc 1ere etape : transformer tous les exos recus de la BDD en triplets



"""

[IMPORTANT]

POUR LANCER L'ALGORITHME, IL SUFFIT globalClustering(D) DANS LA CONSOLE,  AVEC :
D EGAL A D1(), D2(), D3(), D4() ou D5()


NB : les valeurs possibles pour MinPts et eps sont modifiables directement dans la fonction globalClustering (cf les 2 boucles for)


"""



from pylab import *



### Une serie de jeux de "faux" exercices qui serviront A faire les tests


def D1():
    D = []
    P1 = ['a', 0, 0]
    P2 = ['b', 0, 1]
    P3 = ['c', 0, 1.5]
    P4 = ['d', 0, 5]
    P5 = ['e', 0, 6]
    P6 = ['f', 0, 7]
    P7 = ['g', 0, 15]
    D.append(P1)
    D.append(P2)
    D.append(P3)
    D.append(P4)
    D.append(P5)
    D.append(P6)
    D.append(P7)
    return D
    
    
def D2():
    D = []
    P1 = ['a', 0, 0]
    P2 = ['b', 0, 1]
    P3 = ['c', 0, 2]
    P4 = ['d', 0, 3]
    P5 = ['e', 0, 4]
    P6 = ['f', 0, 5]
    P7 = ['g', 0, 6]
    D.append(P1)
    D.append(P2)
    D.append(P3)
    D.append(P4)
    D.append(P5)
    D.append(P6)
    D.append(P7)
    return D


def D3():
    D = []
    P1 = list(['a', 0, 0])
    P2 = list(['b', 0, 1])
    P3 = list(['c', 0, 3])
    P4 = list(['d', 0, 4])
    P5 = list(['e', 0, 6])
    P6 = list(['f', 0, 7])
    P7 = list(['g', 0, 9])
    D.append(P1)
    D.append(P2)
    D.append(P3)
    D.append(P4)
    D.append(P5)
    D.append(P6)
    D.append(P7)
    return D
    

def D4():
    D = []
    P1 = list(['a', 0, 1])
    P2 = list(['b', 0, 1.1])
    P3 = list(['c', 0, 1.3])
    P4 = list(['d', 0, 1.35])
    P5 = list(['e', 0, 5])
    P6 = list(['f', 0, 5.3])
    P7 = list(['g', 0, 5.5])
    P8 = list(['h', 0, 7])
    P9 = list(['i', 0, 9])
    P10 = list(['j', 0, 12])
    P11 = list(['k', 0, 14])
    D.append(P1)
    D.append(P2)
    D.append(P3)
    D.append(P4)
    D.append(P5)
    D.append(P6)
    D.append(P7)
    D.append(P8)
    D.append(P9)
    D.append(P10)
    D.append(P11)
    return D
    
def D5():
    D = []
    P1 = list(['a', 0, 1])
    P2 = list(['b', 0, 2])
    P3 = list(['c', 0, 3.5])
    P4 = list(['d', 0, 5])
    P5 = list(['e', 0, 6])
    P6 = list(['f', 0, 8])
    P7 = list(['g', 0, 10])
    P8 = list(['h', 0, 10.5])
    P9 = list(['i', 0, 11])
    P10 = list(['j', 0, 12])
    P11 = list(['k', 0, 18])
    D.append(P1)
    D.append(P2)
    D.append(P3)
    D.append(P4)
    D.append(P5)
    D.append(P6)
    D.append(P7)
    D.append(P8)
    D.append(P9)
    D.append(P10)
    D.append(P11)
    return D



### La fonction "main" (c'est celle-ci qu'il faut lancer pour demarrer correctement le programme)

    
def globalClustering(D):

    densiteTotale = 0
    MinPts = 0
    eps = 0
    clusterNumber = 0
    Dnouvelle = []
    print("Nous commencons avec le D suivant :", D)
    
    for MinPts2 in [2,3] :
        for eps2 in [1,2,3] :
            print("eps = ", eps2, " et MinPts = ", MinPts2)
            Dnouvelle = []
            for P in D :
                P2 = list(P)
                Dnouvelle.append(P2)
            Dnouvelle = DBSCAN(Dnouvelle, eps2, MinPts2)
            nouvelleDensite = globalDensity(Dnouvelle)
            newClusterNumber = clustersList(Dnouvelle)
            print("densiteTotale = ", densiteTotale, " et nouvelleDensite = ", nouvelleDensite)
            print("clusterNumber = ", clusterNumber, " et newClusterNumber = ", newClusterNumber)
            if (nouvelleDensite > densiteTotale) or ((nouvelleDensite == densiteTotale) and (newClusterNumber > clusterNumber)) :
                densiteTotale = nouvelleDensite
                clusterNumber = newClusterNumber
                MinPts = MinPts2
                eps = eps2
            print("Apres comparaison, densiteTotale = ", densiteTotale, " et donc eps = ", eps, " et MinPts = ", MinPts)
    D3 = DBSCAN(D, eps, MinPts)
    print("Voici le D final :", D3)
    graphicRepresentation(D3)


### DBSCAN

def DBSCAN(D, eps, MinPts) :
    numeroDeCluster = 0
    D2 = list(D)                                                    #initialisation liste des points non visites (initialisee a D)
    D3 = []                                                         #initialisation liste des points visites (initialement vide)
    BRUITS = []                                                     #initialisation liste des bruits
    while D2 != [] :                                                #pour chaque point P non visite des donnees D
        P = D2[0]
        D2.remove(P)
        D3.append(P)                                                #on ajoute P aux points visites 
        PtsVoisins = epsilonVoisinage(D, P, eps)                    #nouvelle liste des points voisins de P
        if len(PtsVoisins) < MinPts :
            BRUITS.append(P)
            P[1] = 0  #normalement inutile                     
        else :
            numeroDeCluster += 1                                  #si assez de voisins, P fonde nouveau cluster
            etendreCluster(D, D2, D3, P, PtsVoisins, numeroDeCluster, eps, MinPts)
    return D
    

def etendreCluster(D, D2, D3, P, PtsVoisins, numeroDeCluster, eps, MinPts):
    P[1] = numeroDeCluster
    for P2 in PtsVoisins :                                           # pour tous les points voisins de P
        if P2 != P :                                                  # on travaille pour les voisins de P non egaux a P
            if P2 in D2 :                                             # si P2 n'a pas encore ete visite
                D3.append(P2)                                         # marquer P2 comme visite
                D2.remove(P2)                                         # le retirer de la liste des non visites
                PtsVoisins2 = epsilonVoisinage(D,P2,eps)              # nouvelle liste des points voisins de P2
                if len(PtsVoisins2) >= MinPts :                        #si assez de voisins, on les ajoute aux voisins de P
                    for P3 in PtsVoisins2 :
                        if P3 not in PtsVoisins :
                            PtsVoisins.append(P3)
            if P2[1] == 0 :
                P2[1] = numeroDeCluster                               # si P2 dans aucun cluster, l'attribuer a ce cluster


# renvoie tous les points de D qui sont a une distance inferieure a epsilon de P
def epsilonVoisinage(D, P, eps):
    VoisinsDeP = []
    for P3 in D :
        if abs(P3[2]-P[2]) <= eps :
            VoisinsDeP.append(P3)
    return VoisinsDeP


### calcul de densite


# renvoie une liste de tous les numeros de clusters
def allClusters(D) :
    clusterNumbers = []
    isNoise = 0
    for P in D :
        if P[1] == 0 :                                              # on traite a part le cas des bruits
            isNoise = 1
        elif not (P[1] in clusterNumbers) :
            clusterNumbers.append(P[1])
    if isNoise == 1 :                                                # si des bruits ont ete rencontres
        clusterNumbers.append(0)                                     # on ajoute le "cluster" 0 a la FIN de la liste
    return  clusterNumbers


# renvoie la liste des clusters (qui sont eux-meme des listes)
def clustersList(D) :
    clustersList = []
    clusterNumbers = allClusters(D)                                 # la liste de tous les numeros de clusters
    
    for currentCluster in clusterNumbers :                          # on parcourt tous les numeros de clusters
        newCluster = []                                             # on cree un cluster associe a ce nouveau numero de cluster
        for P in D :
            if P[1] == currentCluster :
                newCluster.append(P)
        clustersList.append(newCluster)
    print ("La liste de tous les clusters est ", clustersList)
    return clustersList


# renvoie une liste de doublets (numero de cluster, densite)
def allDensities(D) :
    allDensities = []
    clustersList2 = clustersList(D)
    clusterNumber = 1
    for cluster in clustersList2 :
        if cluster[0][1] != 0 :
            if cluster[0][1] != clusterNumber :
                print("ATTENTION : decalage entre les clusters et leur numero! ", "clusterNumber = ", clusterNumber, " alors que le cluster courant est ", cluster[0][1])
                return
            l = len(cluster)
            numberOfPoints = l
            length = cluster[l-1][2]-cluster[0][2]              # differences d'abscisses entre le premier et le dernier point
            density = numberOfPoints/length
            allDensities.append([clusterNumber, density])
            clusterNumber += 1
    print ("Voici la liste des densites par cluster : ", allDensities)
    return allDensities


# fait la moyenne de toutes les densites calculees dans allDensities
def globalDensity(D) :
    globalDensity = 0
    allDensities2 = allDensities(D)
    if len(allDensities2) != 0 :
        numberOfDensities = len(allDensities2)
        for density in allDensities2 :
            globalDensity += density[1]
        globalDensity = globalDensity/numberOfDensities
        return globalDensity
    else :
        return 0



### Representation graphique


def representationGraphique2(D):
    l = len(D)
    i = 0
    tousLesClusters = []                                    # liste de tous les numeros de clusters
    while i < l :                                           # on parcourt D
        numeroDeCluster = D[i][1]
        if not (numeroDeCluster in tousLesClusters) :       # on regarde si on a affaire a un nouveau cluster
            tousLesClusters.append(numeroDeCluster)
            x = []
            y = []
            j = i
            while j < l :                                   # on cherche tous les points qui possedent ce cluster
                if D[j][1] == numeroDeCluster :
                    x.append(D[j][2])
                    y.append(1)
                j +=1
            plot(x,y,"o")
        i += 1
    show()
    
    
def graphicRepresentation(D) :
    clustersList2 = clustersList(D)
    for cluster in clustersList2 :
        x = []
        y = []
        for P in cluster :
            x.append(P[2])
            y.append(1)
        plot(x,y,"o")
    show()
    

# test2