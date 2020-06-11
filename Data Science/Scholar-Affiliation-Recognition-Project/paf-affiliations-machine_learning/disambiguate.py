#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Wed Jun 20 14:42:32 2018

@author: tchibozo
"""

import csv
from Levenshtein import distance

path_to_gridfolder = "grid20180501\\"


def disambiguate(nom):
    if getId(nom)!=-1:
        print("nom connu")
        return getUniversity(getId(nom))
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
    with open(path_to_file, 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            if institution in row:
                return(row[0])
        return -1



def getUniversity(gridId):
    with open("grid.csv", 'r') as f:
        reader = csv.reader(f)
        for row in reader:
            if gridId in row:
                return(row[1])
    return -1



def findClosestUniversity(nom):
    L=['grid.csv','acronyms.csv','aliases.csv','addresses.csv','geonames.csv','institutes.csv','labels.csv']
    tempId=0
    dist=50
    for file in L:
        with open(file, 'r') as f:
            reader = csv.reader(f)
            for row in reader:
                for institution in row:
                    if distance(institution,nom)<dist:
                        tempId=row[0]
                        dist=distance(institution,nom)
                        institution=getUniversity(tempId)
    return getUniversity(tempId),"la distance entre le nom donné et le nom officiel est de", dist
