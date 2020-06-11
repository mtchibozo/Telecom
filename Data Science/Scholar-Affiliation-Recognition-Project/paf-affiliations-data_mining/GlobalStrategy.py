#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Contient le code qui déterminer les affiliations d'un auteur, ou d'un ensemble de documents 
"""

import TrackAuthorsByDataMiningHAL as tr
import DataCollectionFromHAL as dc
import JSON_interfacer as j
import AffiliationTracker as a
import URL_interfacer as u

# placeholder
def get_selective_track_MAP(collated_data, author_name):
    return {}

# placeholder
def get_all_track_MAP(collated_data):
    return {}

def get_surname_data(author_name):
    return author_name.split(" ")[-1]

def get_HAL_full_name_data(author_name, nb_results):
    author_name = u.quote_url(author_name)
    url = dc.url_assembler(nb_results, "authFullName_t", author_name)
    result = dc.request(url)
    return a.disambiguate_mappings(dc.collate_request_data(result))
    
def get_HAL_surname_data(author_name, nb_results):
    author_name = u.quote_url(author_name)
    url = dc.url_assembler(nb_results, "authLastName_t", author_name)
    result = dc.request(url)
    return a.disambiguate_mappings(dc.collate_request_data(result))

def retrieve_reference_data():
    return j.lists_to_sets_copy(j.json_to_python("reference_data.json"))
    
def blorb(author_name):
    try:
        collated_data=get_HAL_full_name_data(author_name, 100)
        yield collated_data
    except:
        collated_data=[]
    try:
        collated_data+=get_HAL_surname_data(author_name, 100)
        yield collated_data
    except:
        pass
    reference_data = retrieve_reference_data()
    for article_data in reference_data:
        a.complete_imperfect_data(article_data)
    yield collated_data+reference_data



def blorbu():
    data = retrieve_reference_data()
    for article_data in data:
        a.complete_imperfect_data(article_data)
    return data
    


def track_HAL(author_name):
    try:
        List_collated_docs=get_HAL_full_name_data(author_name, 10)
        track_MAP_HAL=track_selective_data(List_collated_docs, author_name)
    except Exception as e:
        print("unknown name to HAL")
        raise e
    for collated_data in List_collated_docs:
        a.resolve_HAL_data_without_HAL(collated_data)
    track_MAP_without_HAL=track_selective_data(List_collated_docs, author_name)
    return track_MAP_HAL, track_MAP_without_HAL


# Ajoute une affiliation sous l'année year dans le track_MAP de l'auteur.
def add_hit_to_tracker(year, affiliation, author_track_MAP):
    if year in author_track_MAP:
        if affiliation in author_track_MAP[year]:
            author_track_MAP[year][affiliation]+=1
        else:
            author_track_MAP[year][affiliation]=1
    else:
        author_track_MAP[year]={affiliation:1}

    

def update_selective_author_tracker(collated_doc, track_MAP, author_name):
    author_track_MAP=track_MAP[author_name] # author_name doit etre désambigué et dans track_MAP 
    year=collated_doc["year"]
    affiliation_MAP=collated_doc["authors"]
    if author_name in affiliation_MAP:
        affiliation_SET=affiliation_MAP[author_name]
        for affiliation in affiliation_SET:
            add_hit_to_tracker(year, affiliation, author_track_MAP)


def track_selective_data(List_collated_docs, author_name):
    track_MAP={author_name:{}}
    for collated_doc in List_collated_docs:
        update_selective_author_tracker(collated_doc, track_MAP, author_name)
    return track_MAP



def update_all_author_trackers(collated_doc, track_MAP):
    year=collated_doc["year"]
    affiliation_MAP=collated_doc["authors"]
    for author_name in affiliation_MAP:
        affiliation_SET=affiliation_MAP[author_name]
        for affiliation in affiliation_SET:
            if author_name in track_MAP:
                add_hit_to_tracker(year, affiliation, track_MAP[author_name])
            else:
                track_MAP[author_name]={}
                add_hit_to_tracker(year, affiliation, track_MAP[author_name])


def track_all_data(List_collated_docs):
    track_MAP={}
    for collated_doc in List_collated_docs:
        update_all_author_trackers(collated_doc, track_MAP)
    return track_MAP
