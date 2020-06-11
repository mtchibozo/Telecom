#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Jun 19 10:07:24 2018

@author: xavier
"""


import DataCollectionMethods as dcm
#import disambiguate as d

def assemble_author_search_url():
    url= "https://api.archives-ouvertes.fr/search/?q=authFullName_s:\"P. Chomaz\"&wt=xml&"
    url+="fl=uri_s,docid,producedDate_tdate,title_s,authIdHasStructure_fs,structIsChildOf_fs,halId_s,docType_s&"
    url+="rows=1000"
    return url

#### !!!! Tree_To_String_List=dcm. <- pick a method



def author_data_to_file(author_name, file_name):
    url=assemble_author_search_url()
    dcm.request_to_file(url, file_name)


def disambiguate_affiliation(affiliation):
    return affiliation


def disambiguate_author(author_name):
    return author_name


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
    Affiliation_Parent_MAP=collated_doc["global_Affiliation_Parent_MAP"]
    if author_name in affiliation_MAP:
        affiliation_SET=dcm.tree_to_path_strings(dcm.root_affiliations(affiliation_MAP[author_name], Affiliation_Parent_MAP), Affiliation_Parent_MAP)
        for affiliation in affiliation_SET:
            disambiguated_affiliation=disambiguate_affiliation(affiliation)
            add_hit_to_tracker(year, disambiguated_affiliation, author_track_MAP)


def track_selective_data(List_collated_docs, author_name):
    disambiguated_author_name=disambiguate_author(author_name)
    track_MAP={disambiguated_author_name:{}}
    for collated_doc in List_collated_docs:
        update_selective_author_tracker(collated_doc, track_MAP, disambiguated_author_name)
    return track_MAP



def update_all_author_trackers(collated_doc, track_MAP):
    year=collated_doc["year"]
    affiliation_MAP=collated_doc["authors"]
    Affiliation_Parent_MAP=collated_doc["global_Affiliation_Parent_MAP"]
    for author_name in affiliation_MAP:
        disambiguated_author_name=disambiguate_author(author_name)
        affiliation_SET=dcm.tree_to_path_strings(dcm.root_affiliations(affiliation_MAP[author_name], Affiliation_Parent_MAP), Affiliation_Parent_MAP)
        for affiliation in affiliation_SET:
            disambiguated_affiliation=disambiguate_affiliation(affiliation)
            if disambiguated_author_name in track_MAP:
                add_hit_to_tracker(year, disambiguated_affiliation, track_MAP[disambiguated_author_name])
            else:
                track_MAP[disambiguated_author_name]={}
                add_hit_to_tracker(year, disambiguated_affiliation, track_MAP[disambiguated_author_name])


def track_all_data(List_collated_docs):
    track_MAP={}
    for collated_doc in List_collated_docs:
        update_all_author_trackers(collated_doc, track_MAP)
    return track_MAP


url="https://api.archives-ouvertes.fr/search/?q=authFullName_s:%22P.%20Chomaz%22&wt=xml&fl=uri_s,docid,producedDate_tdate,title_s"
url+=",authIdHasStructure_fs,structIsChildOf_fs,halId_s,docType_s&rows=1000"
print(url)
print("\n")
file_name="track_author_chomaz.json"
author_name="P. Chomaz"
#dcm.request_to_file(url, file_name) # done once is enough
List_collated_docs=dcm.lists_to_sets_copy(dcm.json_to_python(file_name))
track_MAP=track_selective_data(List_collated_docs, author_name)
MAP=track_MAP[author_name]
for year in range (1988, 2016):
    year=str(year)
    if year in MAP:
        print(year)
        print(MAP[year])
        print("\n=================================================\n")#dcm.request_to_json(url, file_name)

