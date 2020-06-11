#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
module pour interagir avec les metadonnees d'un article
"""

def get_affiliations_from_data(article_data):
    affiliationSet=set()
    authors_data = article_data["authors"]
    for author_name in authors_data:
        affiliationSet.update(authors_data[author_name])
    return affiliationSet

class Status():
    """ classe Status qui décrit l'état des données d'un article """
    def __init__(self):
        self.hasYear = False
        self.hasAuthors = False
        self.eachAuthorHasName = True
        self.eachAuthorHasAffiliation= True
        self.allAffiliationsAssigned= False
        self.hasAffiliations = False
        self.hasTitle = False
        self.hasPdfUrl = False
        self.hasPathToPdf = False
        self.hasLongString = False
        self.Data={}
    
    def isA_OK(self):
        return (
                self.hasYear 
                and self.hasAuthors 
                and self.eachAuthorHasAffiliation 
                and self.eachAuthorHasName 
                and self.allAffiliationsAssigned 
                and self.hasAffiliations
                and self.hasTitle
                and (self.hasPdfUrl or self.hasPathToPdf)
                )
    def isGoodEnough(self):
         return (
                self.hasYear 
                and self.hasAuthors 
                and self.eachAuthorHasAffiliation 
                and self.allAffiliationsAssigned 
                )

    def almostGoodEnough(self):
         return (
                self.hasYear 
                and self.hasAuthors 
                and self.eachAuthorHasAffiliation 
                )
        
    def wrap(self, article_data):
        status = Status()
        self.hasYear="year" in article_data
        self.hasTitle = "title" in article_data
        authors_data=article_data["authors"]
        self.hasAuthors=bool(article_data["authors"])
        self.hasAffiliations = bool(get_affiliations_from_data(article_data))
        for author in authors_data:
            self.eachAuthorHasAffiliation = (bool(authors_data[author]) and status.eachAuthorHasAffiliation)
        self.allAffiliationsAssigned = not ("unassigned_affiliations" in article_data and bool(article_data["unassigned_affiliations"]))
        self.hasPdfUrl = "file_url" in article_data
        self.hasPathToPdf = "file_path" in article_data
        self.hasLongString = "longString" in article_data
        self.Data = article_data
