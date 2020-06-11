#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose plusieurs stratégies pour déterminer les affiliations d'une liste de fichiers pdf, avec un système de recours si une stratégie échoue.
"""
import disambiguate as d
import LocalPath
import TextAssembler as t
import Metadata_interfacer as m
import URL_interfacer as u
from ReferenceDataBuilder import temp_fix_hhtpresponse_to_file
import ClassifierUser as c


ngram_assembler = t.ngram_assembler_generator(3, 6)

def is_affiliation_through_levenstein(string):
    print(string)
    print(d.disambiguate(string))
    return d.disambiguate(string)[1]<10

#is_affiliation = is_affiliation_through_levenstein

is_affiliation = c.is_affiliation

#def affiliation_finder_generator(is_affiliation_function):
#    def find_affiliations(longString):
#        MAP_affiliations_found_to_positions={}
#        NGramMAP = ngram_assembler(longString)
#        for ngram in NGramMAP:
#            if is_affiliation(ngram):
#                MAP_affiliations_found_to_positions[ngram]=NGramMAP[ngram]
#        return MAP_affiliations_found_to_positions
#    return find_affiliations

def affiliation_finder_generator(is_affiliation_function):
    def find_affiliations(LinesList):
        MAP_affiliations_found_to_positions={}
        for i in range(len(LinesList)):
            line = LinesList[i]
            if is_affiliation(line):
                NGramMAP = ngram_assembler(line)
                for ngram in NGramMAP:
                    MAP_affiliations_found_to_positions[ngram]=NGramMAP[ngram]+100*i
        return MAP_affiliations_found_to_positions
    return find_affiliations

affiliation_finder = affiliation_finder_generator(is_affiliation)
#def are_interlaced(tupple1, tuppl2):
#    min1 = min(tupple1)
#    min2 = min(tupple2)
#    max1 = max(tupple1)
#    max2 = max(tupple2)
#    return (max1)

# possibilité : faire des classes d'entrelacement des ngram ?

def disambiguate_affiliations_found(MAP_affiliations_found_to_positions, disambiguisationMAP={}):
    MAP_disambiguated_affiliations_position={}
    MAP_disambiguated_affiliations_dist={}
    MAP_disambiguated_to_ngrams={}
    MAP_ngram_to_disambiguated={}
    for affiliation_ngram in MAP_affiliations_found_to_positions:
        disambiguated_affiliation, dist= d.disambiguate(affiliation_ngram)
        MAP_ngram_to_disambiguated[affiliation_ngram]=disambiguated_affiliation
        if disambiguated_affiliation in MAP_disambiguated_affiliations_position:
            MAP_disambiguated_to_ngrams[disambiguated_affiliation].append(affiliation_ngram)
            if dist < MAP_disambiguated_affiliations_dist[disambiguated_affiliation]:
                MAP_disambiguated_affiliations_dist[disambiguated_affiliation]=dist
                MAP_disambiguated_affiliations_position=MAP_affiliations_found_to_positions[affiliation_ngram]
        else:
            MAP_disambiguated_to_ngrams[disambiguated_affiliation]=[affiliation_ngram]
            MAP_disambiguated_affiliations_dist[disambiguated_affiliation]=dist
    return MAP_disambiguated_affiliations_position

line_assembler = t.limited_tline_assembler_generator(10)
data_extractor = t.data_extractor_generator(line_assembler, SelectedPagesList=[0, 1], has_limit=False, limit=40)

def get_longString(pdf_file):
    return t.List_to_string(data_extractor(pdf_file))


def get_pdf_from_url(pdf_url):
    rb=u.request(pdf_url)
    rb=temp_fix_hhtpresponse_to_file(rb)
    return rb

def get_pdf_from_path(pdf_path):
    rb=open(pdf_path, 'rb')
    return rb

def get_year(longString):
    max_year = 0
    for year in range(1900, 2018):
        if str(year) in longString and year > max_year:
            max_year = year
    assert (max_year!=0)
    return str(max_year)


# Cette fonction renvoie, à partir des dictionnaires contenant les auteurs et les affiliations et leur ordre d'apparition, 
# 2 éléments: le dictionnaire associant auteurs et affiliations si possible, et un chiffre représentant la situation rencontrée

def mapAuthorsToAffiliations(authors_dictionnary, affiliations_dictionnary):
    
    author_keys = sorted(authors_dictionnary) # Si dico_ref est un dictionnaire, sorted(dico_ref) renvoie la liste triée de ses clés
    affiliations_keys = sorted(affiliations_dictionnary)
    
    
    if (author_keys==[]) or (affiliations_keys==[]) :
        return {}, 3 # La situation 3 correspond aux cas d'erreur, ou non traités: ici, on a pas d'auteurs ou d'affiliations
    
    n = len(author_keys)
    if (author_keys[n-1] < affiliations_keys[0]) and (n==len(affiliations_keys)) :
        # Ici, on est dans la situation 2 : tous les auteurs se trouvent avant toutes les affiliations, et on a 
        # autant d'auteurs que d'affiliations (sinon on ne sait pas attribuer les affiliations)
        # On fait l'hypothèse que les auteurs sont dans le même ordre que leurs affiliations correspondantes
        dictionnary = {}
        for i in range(n): 
            dictionnary[ authors_dictionnary[author_keys[i]] ] = [ affiliations_dictionnary.get(affiliations_keys[i]) ]
        return dictionnary, 2
        
    
    # Un autre cas se présente: on considère qu'on a un auteur, puis son(ses) affiliation(s) : c'est la situation 1
    order = sorted(author_keys + affiliations_keys)
    l = len(order)
    
    i = 0
    dictionnary = {}
    while (i<l): 
        if order[i] in author_keys : 
            j = 0
            L = []
            print(i+j+1)
            while ((i+j+1)<l) and (order[i+j+1] in affiliations_keys): #On regarde toutes les affiliations qui sont après un auteur
                L.append( affiliations_dictionnary[order[i+j+1]] ) 
                j+=1
            if (j==0) : #Cela signifie qu'un auteur n'a pas d'affiliation(s)
                return {}, 3
            else : #On passe à l'auteur suivant
                dictionnary[ authors_dictionnary[order[i]]] = L
                i = i+j+1

    
    return dictionnary, 1


""" authors_dictionnary = {}
authors_dictionnary[1] = 'O. Hudry'
authors_dictionnary[5] = 'A. Vergne'
authors_dictionnary[7] = 'E. Leborgne'

affiliations_dictionnary = {}
affiliations_dictionnary[3] = 'Télécom ParisTech'
affiliations_dictionnary[8] = 'Télécom ParisTech'
affiliations_dictionnary[17] = 'Saint-Louis'

print(mapAuthorsToAffiliations(authors_dictionnary, affiliations_dictionnary)) """

def find_position_in_string(string, author):
    return len(string.split(author)[0].split(" "))

def get_author_order(LinesList, authorList):
    order_MAP={}
    for author in authorList:
        for i in range(len(LinesList)):
            line = LinesList[i]
            if author in line:
                order_MAP[author]=find_position_in_string(line, author)+100*i
                break
    return order_MAP

def status_wrapper(data):
    status = m.Status()
    status.wrap(data)
    return status

def disambiguate_affiliationSet(affiliationSet, treshold, disambiguisationMap={}):
    newAffiliationSet=set()
    for affiliation in affiliationSet:
        if affiliation in disambiguisationMap:
            disambiguated_affiliation, dist = disambiguisationMap[affiliation]
        else:
            disambiguated_affiliation, dist = d.disambiguate(affiliation)
            disambiguisationMap[affiliation]=(disambiguated_affiliation, dist)
        if dist<treshold:
            newAffiliationSet.add(disambiguated_affiliation)
    return newAffiliationSet

def disambiguate_affiliations_authors(affiliationMAP, disambiguisationMap={}):
    newAffiliationMAP={}
    for author in affiliationMAP:
        newAffiliationMAP[d.disambiguate_author(author)]=disambiguate_affiliationSet(affiliationMAP[author], 15, disambiguisationMap)
    return newAffiliationMAP
    

class NotEnoughDataException(Exception):
    """ raise quand des données clés qu'on ne sait pas retrouver (comme les auteurs) manquent """

def complete_imperfect_data(imperfect_collated_data):
    status = status_wrapper(imperfect_collated_data)
    if status.isA_OK():
        return
    if status.isGoodEnough():
        return
    if not status.hasAuthors:
        raise NotEnoughDataException()
    if status.hasLongString:
        longString = imperfect_collated_data["longString"]
    else:
        if not status.hasPdfUrl:
            try:
                pdf_path = imperfect_collated_data["file_path"]
                pdf_file = get_pdf_from_path(pdf_path)
            except:
                raise NotEnoughDataException()            
        else:
            try:
                url = imperfect_collated_data["file_url"]
                pdf_file = get_pdf_from_url(url)
            except:
                raise NotEnoughDataException()
        # because of raised exception ,this code is only reachable is we managed to get a pdf_file
        try:
            longString=get_longString(pdf_file)
        except Exception as e:
            print("probleme with getting longString in AffiliationTracker.complete_imperfect_data")
            raise e
    
    if not status.hasYear:
        try:
            year = get_year(longString)
        except: # raised by the assert in get_year
            raise NotEnoughDataException()
        else:
            imperfect_collated_data["year"] = year
    
    if status.eachAuthorHasAffiliation:
        imperfect_collated_data["authors"] = disambiguate_affiliations_authors(imperfect_collated_data["authors"])
    else:
        affiliation_to_order = disambiguate_affiliations_found(affiliation_finder(longString))
        authorsToAffiliations = mapAuthorsToAffiliations(affiliation_to_order)
        imperfect_collated_data["authors"] = authorsToAffiliations
    
def resolve_HAL_data_without_HAL(collated_data):
    status = status_wrapper(collated_data)
    if not status.hasAuthors:
        raise NotEnoughDataException()
    else:
        if not status.hasPdfUrl:
            try:
                pdf_path = collated_data["file_path"]
                pdf_file = get_pdf_from_path(pdf_path)
            except:
                raise NotEnoughDataException()            
        else:
            try:
                url = collated_data["file_url"]
                pdf_file = get_pdf_from_url(url)
            except:
                return
        # because of raised exception ,this code is only reachable is we managed to get a pdf_file
        try:
            LinesList = data_extractor(pdf_file)
        except Exception as e:
            print("probleme with getting longString in AffiliationTracker.resolve_HAL_data_without_HAL")
            raise e

    affiliation_to_order = disambiguate_affiliations_found(affiliation_finder(LinesList))
    authorsList = [author for author in collated_data["authors"]]
    authorsToAffiliations = mapAuthorsToAffiliations(get_author_order(LinesList, authorsList), affiliation_to_order)
    collated_data["authors"] = authorsToAffiliations

def disambiguate_mappings(data):
    disambiguisationMap={}
    for article_data in data:
        try:
            article_data["authors"] = disambiguate_affiliations_authors(article_data["authors"])
        except:
            print("data seems incomplete. Bypassing as default action")
            continue
    return data
    