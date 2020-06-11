
#DONE:prendre l'entete du pdf y ajouter a gauche  et droite le texte pour avoir le XML
#recuperer les données importantes:  getAffiliation, getAuthor
#beautifulsoup
#reiterer sur Liste FichiersPDF

import webbrowser
import os

## Ouverture du Doc XML à partir de l'URL


titre='Alignment Kernels for Audio Classification with Application to Music Instrument Recognition'
url='http://givingsense.eu/sembib/data/srcPdf/paper/'+titre+'-header.xml'
webbrowser.open(url)

## Ouverture de ListeFichiersPDF et Ouverture des XML

import webbrowser
import os
os.chdir("C:\Users\Max Tchibozo\Desktop\paf-affiliations")      #Il faut adapter le chemin
file  = open("ListeFichiersPDF.txt")
i=0
while i < 10:
    titre = file.readline()
    n=len(titre)
    newtitre=titre[:n-5]                                         #  Il faut enlever .pdf à la fin
    url='http://givingsense.eu/sembib/data/srcPdf/paper/'+newtitre+"-header.xml"
    webbrowser.open(url)
    i = i +1
file.close()

#print(file.readlines()) pour toures les lignes

## Ouverture de ListeFichiersPDF et recupération des champs pertinents: Auteur, Affiliation

import webbrowser
import os
from bs4 import BeautifulSoup


os.chdir("C:\Users\Max Tchibozo\Desktop\paf-affiliations")      
file  = open("ListeFichiersPDF.txt")
with open("ListeFichiersPDF.txt") as foo:
    lineAmount = len(foo.readlines())
i=50
while i==50:             # i<lineAmount
    titre = file.readline()
    n=len(titre)
    newtitre=titre[:n-5]                                         
    url='http://givingsense.eu/sembib/data/srcPdf/paper/'+newtitre+"-header.xml"
    with open(url) as fp:
        soup = BeautifulSoup(fp)
    soup = BeautifulSoup("<html>data</html>")
    for p in soup.find_all('p'):
        print p
    i = i +1
file.close()

    

