from bs4 import BeautifulSoup
from urllib.request import urlopen

#Detection d'erreurs


    
url = "http://givingsense.eu/sembib/data/srcPdf/paper/3d%20Realistic%20Animation%20of%20a%20Tennis%20Player-header.xml" 
xml_doc = urlopen(url) 



def list_authors(xml_doc) :
    soup = BeautifulSoup(xml_doc, "xml")
    list = soup.find_all("author")
    return list 


def erreur_aff(aut) :
    l2 = aut.find_all("affiliation")
    if (l2==[]) :
        return 0
        # Type d'erreur 1 : un auteur n'a pas d'affiliation
    elif (len(l2)>1) :
        return 2
        # Type d'erreur 2 : un auteur a plus d'une affiliation
    else : 
        return 1
    
    
def erreur_doc(xml_doc) :
    L = list_authors(xml_doc)
    for aut in L :
        print(erreur_aff(aut))
        
    
def erreur_surname(aut) :
    l2 = aut.find_all("surname")
    return (l2!=[]) 
        