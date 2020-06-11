#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Assemble un fichier JSON à partir d'une liste de PDF Grobidés. 
"""
import LocalPath
import URL_interfacer as u
import GROBID_Extractor as g 
import JSON_interfacer as j
import TextAssembler as t
import Metadata_interfacer as m
from io import StringIO

from os import listdir
from os.path import isfile, join
TEIpath = "../../TEI"
onlyfiles = [TEIpath+f for f in listdir(TEIpath) if isfile(join(TEIpath, f))]



def correct_title(line):
    return line.replace("..", ".")[:-5]

def build_grobid_url(title):
    return "http://givingsense.eu/sembib/data/srcPdf/paper/"+u.quote(title)+"-header.xml"

def build_raw_file_url(title):
    return "http://givingsense.eu/sembib/data/srcPdf/"+u.quote(title)+".pdf"

def getTitleList(pdf_list_file):
    CorrectTitles=[]
    with open(pdf_list_file) as f:
        for line in f:
            CorrectTitles.append(correct_title(line))
    return CorrectTitles

def generateTitlesIterator(pdf_list_file):
    for line in pdf_list_file:
        yield correct_title(line)

def get_reference_data(pdf_list_file):
    data_list=[]
    titlesIterator = generateTitlesIterator(pdf_list_file)
    for title in titlesIterator:
        print(title)
        try:
            r=u.request(build_grobid_url(title))
        except u.BadURLException:
            print("--- badURL")
            continue
        except:
            print("--- unable to open")
            continue
        grobidSOUP=g.get_XML_SOUP(r)
        data, status = g.get_data(grobidSOUP)
        data["file_url"]=build_raw_file_url(title)
        data["title"]=title
        data_list.append(data)
        print("...")
    return data_list

def get_and_dump_reference_data(reference_filename, pdf_list_filename):
    with open(pdf_list_filename, 'r') as pf:
        try:
            data=get_reference_data(pf)
            print(data)
            j.json_to_file(j.sets_to_lists_copy(data), reference_filename)
        except:
            return data

def load_reference_data(reference_filename):
    return j.lists_to_sets_copy(j.json_to_python(reference_filename))
    

def writeAffiliationInFile(affiliation_file, affiliationSetToWrite):
    for affiliation in affiliationSetToWrite:
        affiliation_file.write(affiliation+"\n")

def isAffiliationInLine(affiliationSet, line):
    for affiliation in affiliationSet:
        if affiliation in line:
            return True
    return False


def get_fileNotAffiliationSet(FileLinesSet, fileAffiliationSet):
    fileNotAffiliationSet=set()
    for line in FileLinesSet:
        if not isAffiliationInLine(fileAffiliationSet, line):
            fileNotAffiliationSet.add(line)
    return fileNotAffiliationSet
        
# doit renvoyer les ligne d'un pdf qui ne sont pas 
def writeNotAffiliationInFile(not_affiliation_file, notAffiliationSetToWrite):
    for line in notAffiliationSetToWrite:
        not_affiliation_file.write(line+"\n")

def temp_fix_hhtpresponse_to_file(file):
    with open("temp.pdf", 'wb') as temp:
        temp.write(file.read())
    return open("temp.pdf", 'rb')

data_extractor=t.data_extractor_generator(t.tline_assembler, SelectedPagesList=[0, 1, -1], has_limit=True, limit=20)

def create_not_affiliation_file(reference_data, not_affiliation_filename):
    with open(not_affiliation_filename, "a") as nf :
        for article_data in reference_data:
            try:
                url = article_data["file_url"]
                print(url)
                rb=u.request(url)
            except u.BadURLException:
                print("BadUrlexception in raw file")
                continue
            else:
                fileAffiliationSet=m.get_affiliations_from_data(article_data)
                rb=temp_fix_hhtpresponse_to_file(rb)
                try:
                    FileLinesSet=set(data_extractor(rb))
                    notAffiliationSet=get_fileNotAffiliationSet(FileLinesSet, fileAffiliationSet)
                    writeNotAffiliationInFile(nf, notAffiliationSet)
                    article_data["longString"] = t.List_to_string(FileLinesSet) # as a side effect, we can update data to have longstring.
                except:
                    continue

def create_affiliation_file(reference_data, affiliation_filename):
    affiliationSet=set()
    with open(affiliation_filename, "a") as f :
        for article_data in reference_data:
            title=article_data["title"]
            print(title) #
            fileAffiliationSet=m.get_affiliations_from_data(article_data)
            if fileAffiliationSet == set():
                    continue        
            newAffiliationSet=fileAffiliationSet.difference(affiliationSet)
            print(newAffiliationSet)
            affiliationSet=affiliationSet.union(fileAffiliationSet)
            writeAffiliationInFile(f, newAffiliationSet)

#def get_status(article_data):
#    status = g.Status()
#    status.hasYear="year" in article_data
#    authors_data=article_data["authors"]
#    status.hasAuthors=bool(article_data["authors"])
#    status.hassAffiliations = bool(g.get_affiliations_from_data(article_data))
#    for author in authors_data:
#        status.eachAuthorHasAffiliation = (bool(authors_data[author]) and status.eachAuthorHasAffiliation)
#    status.allAffiliationsAssigned = not ("unassigned_affiliations" in article_data)
#    return status



