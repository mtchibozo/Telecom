#!/usr/bin/env python2
# -*- coding: UTF-8 -*-

import time
import os
from bs4 import BeautifulSoup
from urllib.request import urlopen, Request
import requests
import urllib
import JSON_interfacer as JSON


## Ouverture de ListeFichiersPDF et recuperation des champs pertinents: Auteur, Affiliation





def getTitleList(pathToDir, file):
    os.chdir(pathToDir)      
    lines=[]
    with open(file) as f:
        for line in f:
            print(line)
            lines.append(line)
    CorrectTitles=[]
    for line in lines:
        if (line[-6:-4]=='..'):
            CorrectTitles.append(line[:-6])
        else:
            CorrectTitles.append(line[:-5])
    return CorrectTitles



    
    
def getDataFromPDF(titre):
    ## A  ajouter plus tard: passage automatique par Grobid avec le script; pour l'instant, ça ne marche qu'avec 
    ## les PDFs déjà traités
    title_string = titre
    
    url_string='http://givingsense.eu/sembib/data/srcPdf/paper/'+urllib.parse.quote(titre)+'-header.xml'

    try:
        with urlopen(url_string) as xml_doc:
            soup = BeautifulSoup(xml_doc, "xml")
            list_date = soup.find_all("date")


            if list_date != []:
                # De façon arbitraire, si il y a plusieurs dates, on choisit la 1ere.
                date=list_date[0]
                year_string = date.string

            else :
                year_string = ""


            list_author = soup.find_all("author")
            L = []
            for author in list_author:
                list2 = author.find_all("surname")
                # Ce test sert à vérifier qu'il y a bien un nom et que cet auteur n'est pas juste constitué d'affiliations
                # (ce qui est une erreur possible)
                if list2 != [] :
                    list_forename=author.find_all("forename")
                    name = ""
                    for forename in list_forename:
                        name += forename.string
                    name += author.surname.string
                    list_affiliation=author.find_all("orgName")
                    if list_affiliation != [] :
                        ## Encore une fois, si il y a ambiguité sur les affiliations, on choisit arbitrairement la 1ere
                        affi = list_affiliation[0].string
                    else:
                        affi = ""
                    L.append([name,affi])

                else :
                    print("Des affiliations ont ete mises a part")
                    print("\n")

            return to_json(url_string, title_string, "", year_string, L)
    except:
        print("getDataFromPDF: unable to open ", url_string)
        return None

      
        

def to_json(url_string, title_string, date_string, year_string, list_authors):  
    json_string="{\n"
    json_string+="\"url\": \""+url_string+"\",\n"
    json_string+="\"title\": \""+title_string+"\",\n"
    #json_string+="\"date\": \""+date_string+"\",\n"
    json_string+="\"year\": \""+year_string+"\",\n"
    json_string+="\"authors\": ["
    for author in list_authors:
        json_string+="\n{ \"name\": \""+author[0]+"\", "
        json_string+="\"affiliation\": { \"name\": \""+author[1]+"\" } },"  
    json_string=json_string[:-1]
    json_string+="\n]\n}"
    return json_string



        
        
        

def getAffFromPDF(titre, aff_set, fichier):
    url='http://givingsense.eu/sembib/data/srcPdf/paper/'+urllib.parse.quote(titre)+'-header.xml'
    print(url)
    # print(checkURL(url))
    try:
        with urlopen(url) as xml_doc:
            #print("test0")
            #req = Request(url, data=None, headers={
            #'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.47 Safari/537.36'})


            #req = Request(url)
            #req.add_header('Referer', 'http://www.python.org/')

            #print("test1")

            soup = BeautifulSoup(xml_doc, "xml")
            list_aff = soup.find_all("affiliation")
            for affiliation in list_aff :
                aff_str = affiliation.get_text(" ", strip=True)
                print(aff_str)
                if (aff_str not in aff_set):
                    aff_set.add(aff_str)
                    fichier.write(aff_str + "\n")
    except:
        print("getAffFromPDF: unable to open ", url)
        return None


        
def getAffFromAllDocs(pathToDir, file):
    # title_list = getTitleList(pathToDir, file)
    titres = boucleSurTitres(pathToDir, file)
    aff_set = set()
    with open("liste_affiliations.txt", "a") as fichier :
        for titre in titres:
            getAffFromPDF(titre, aff_set, fichier)
            time.sleep(0.4)
        fichier.close()



def checkURL(url):
    request = requests.get(url)
    if request.status_code == 200:
        return request
    else:
        return None
        

    

pathToDir="/cal/homes/jcastan/"
file = "ListeFichiersPDF.txt"

def boucleSurTitres(pathToDir, file):
    #os.chdir(pathToDir)
    with open(file) as f:
        for line in f:
            if (line[-6:-4] == '..'):
                res = line[:-6]
                yield(res)
            else:
                res = line[:-5]
                yield(res)

#titre = getTitleList(pathToDir, file)[20]
#print(getDataFromPDF(titre))
#print(checkURL('http://givingsense.eu/sembib/data/srcPdf/paper/3d Extraction by Structural Matching of Sar and Optical Features-header.xml'))
#print(checkURL('http://givingsense.eu/sembib/data/srcPdf/paper/(2,1)-separating Systems Beyond the Probabilistic Bound-header.xml'))
getAffFromAllDocs(pathToDir, file)

#title_list = getTitleList(pathToDir, file)

#
