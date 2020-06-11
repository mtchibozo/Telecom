#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Wed Jun 20 14:42:32 2018

@author: tchibozo
"""

import csv
path_to_gridfolder = "grid20180501\\"
import os
from Levenshtein import distance
import unicodedata



def getAddressFromUniversityId(id):
    os.chdir("grid20180501")
    with open('addresses.csv', 'r') as f:
        os.chdir(os.pardir)
        reader = csv.reader(f)
        for row in reader:
            if id in row:
                return(row[8],row[11],"lat : ",row[4]," long : ", row[5])
    return -1

   # row[8]:ville
   # row[11]: pays

def disambiguate(nom):
    if getId(nom)!=-1:
        print("nom connu")
        return getUniversity(getId(nom)), 0
    else: 
        print("Ceci n'est pas le nom officiel de cette institution")
        return findClosestUniversity(nom)
        
def getId(institution):
    L=['grid.csv','acronyms.csv','aliases.csv','addresses.csv','geonames.csv','institutes.csv','labels.csv']
    for file_name in L:
        id=getIdFromFile(file_name,institution)
        if id == -1:
            print("pas trouve dans le fichier : "+file_name)
        else:
            return id
    return id
            
def getIdFromFile(path_to_file,institution):
    os.chdir("grid20180501")
    with open(path_to_file, 'r') as f:
        os.chdir(os.pardir)
        reader = csv.reader(f)
        for row in reader:
            if institution in row:
                return(row[0])
        return -1



def getUniversity(gridId):
    os.chdir("grid20180501")
    with open("grid.csv", 'r') as f:
        os.chdir(os.pardir)
        reader = csv.reader(f)
        for row in reader:
            if gridId in row:
                return(row[1])
    return -1



def findClosestUniversity(nom):
    L=['grid.csv','acronyms.csv','aliases.csv','addresses.csv','geonames.csv','institutes.csv','labels.csv']
    tempId=0

    dist=50000

    for file in L:
        os.chdir("grid20180501")
        with open(file, 'r') as f:
            os.chdir(os.pardir)
            reader = csv.reader(f)
            for row in reader:
                for institution in row:
                    if distance(institution,nom)<dist:
                        tempId=row[0]
                        dist=distance(institution,nom)
                        institution=getUniversity(tempId)

    return getUniversity(tempId),dist

    





def string_normalisation(string) :
    words_list = string.split(" ")
    if len(words_list)>=2 :
        correct_letters = words_list[0][0] + words_list[-1]
        sans_accent = unicodedata.normalize('NFKD', correct_letters).encode('ascii', 'ignore')
        return sans_accent.lower()
    else : 
        return "Erreur" #Il n'y a pas 2 éléments dans le nom (donc pas au moins 1 prénom et 1 nom de famille) donc on considère que l'ambiguité est trop importante
    
    
def disambiguate_author(author_name):
    return author_name
    

    

