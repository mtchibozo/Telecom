#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose des fonctions pour accéder aux informations extraites par Grobid d'un pdf et stockées au format XML
"""

from bs4 import BeautifulSoup
from XMLFinderGenerator import NoSuchTagException
from Metadata_interfacer import Status
# note : est un doublon de la mme fonction dans DataColectionMethods
def get_XML_SOUP(XML):
    XML_SOUP = BeautifulSoup(XML, "xml")
    return XML_SOUP

def get_tag(tag, soup):
    try:
        return soup.find_all(tag)[0]
    except:
        raise NoSuchTagException()
        
def get_data(article_soup):
    article_data={}
    status=Status()
    try:
        year = get_tag("title", article_soup).get_text()
        article_data["year"]=year
    except NoSuchTagException:
        status.hasTitle=False
    try:
        year = get_tag("date", article_soup).get_text()
        article_data["year"]=year
    except NoSuchTagException:
        status.hasYear=False
    authors_data={}
    list_author = article_soup.find_all("author")
    status.hasAuthors=(list_author!=[])
    for author in list_author:
        affiliation_set=get_affiliations(author)
        status.hasAffiliations = status.hasAffiliations or bool(affiliation_set)
        name=""
        for forename in author.find_all("forename"):
            name += forename.string+" "
        try:
            name += author.surname.string
        except:
            status.eachAuthorHasName=False
            if affiliation_set:
                status.allAffiliationsAssigned=False
                if "unnassigned_affiliations" in article_data:
                    article_data["unnassigned_affiliations"].update(affiliation_set)
                else:
                    article_data["unnassigned_affiliations"] = affiliation_set
        else:
            status.eachAuthorHasAffiliation = (affiliation_set!=set() and status.eachAuthorHasAffiliation)
        authors_data[name]=affiliation_set
    article_data["authors"]=authors_data
    return article_data, status
    


def get_affiliations(soup):
    affiliationSet=set()
    affiliation_list = soup.find_all("affiliation")
    for affiliation in affiliation_list:
        affiliationSet.add(affiliation.get_text(" ", strip=True))
    return affiliationSet

