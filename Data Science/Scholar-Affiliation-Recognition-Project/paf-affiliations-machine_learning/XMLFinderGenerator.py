#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Jun 24 15:01:50 2018

@author: xavier
"""
    
def finder_generator(tag_name, tag_attr_name, tag_attr_string):
    def tag_filter(tag):
        return tag.name==tag_name and tag.has_attr(tag_attr_name) and tag[tag_attr_name]==tag_attr_string
    
    if tag_name == 'arr':
        def get_matching_tags(doc):
            return doc.find_all(tag_filter)[0].strings
    
    else:
        def get_matching_tags(doc):
            return get_matching_tags(doc)[0].string
    
    return get_matching_tags