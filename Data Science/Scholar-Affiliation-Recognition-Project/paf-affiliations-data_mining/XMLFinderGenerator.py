#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose un générateur de méthodes pour extraire des informations d'un objet BeautifulSoup XML
"""

class NoSuchTagException(Exception):
    """ raise when the search for a tag returns an empty list """
    
    
def finder_generator(tag_name, tag_attr_name, tag_attr_string):
    def tag_filter(tag):
        return tag.name==tag_name and tag.has_attr(tag_attr_name) and tag[tag_attr_name]==tag_attr_string
    
    if tag_name == 'arr':
        def get_matching_tags(doc):
            find_list = doc.find_all(tag_filter)
            if find_list == []:
                raise NoSuchTagException()
            return doc.find_all(tag_filter)[0].strings
                # si il y a plusieurs tags possibles on prend le premier
    else:
        def get_matching_tags(doc):
            find_list = doc.find_all(tag_filter)
            if find_list == []:
                raise NoSuchTagException()
            return doc.find_all(tag_filter)[0].string
    
    return get_matching_tags