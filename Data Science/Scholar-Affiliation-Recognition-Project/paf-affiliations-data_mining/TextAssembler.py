#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Utilise les méthodes de PDF_DataExtractor pour extraire du texte d'un fichier pdf
"""

import PDF_DataExtractor as pdfd

class Analysis_list_storage:
    def __init__(self, has_limit=False, limit=10):
        self.Text=[]
        self.has_limit = has_limit
        if self.has_limit:
            self.limit = limit
        
    def is_full(self):
        return self.has_limit and len(self.Text) >= self.limit
    
    def add(self, stringList):
        for string in stringList:
            if self.is_full():
                break
            self.Text.append(string)
        
    def getData(self):
        return self.Text
    
def assembler_wrapper(assembler):
    def wrapper(page_layout, analysis_storage):
        for item in page_layout:
            if analysis_storage.is_full():
                break
            try:
                analysis_storage.add(assembler(item))
            except:
                continue
            
    return wrapper

def tbox_assembler(item):
    return [item.get_text()]

def tline_assembler(item):
    return item.get_text().split("\n")

def limited_tline_assembler_generator(line_limit):
    def limited_tline_assembler(item):
        return item.get_text().split("\n")[:line_limit]
    return limited_tline_assembler

# example
limited_tline_assembler = limited_tline_assembler_generator(2)

def data_extractor_generator(assembler, SelectedPagesList=[], has_limit=False, limit=10):
    interpreter = pdfd.create_layout_interpreter()
    get_page_layout = pdfd.page_layout_generator(interpreter)
    wrapped_assembler = assembler_wrapper(assembler)

    def data_extractor(pdf_file):
        document = pdfd.parse_document(pdf_file)
        pages_iterator=pdfd.pages_iterator(document)
        storage = Analysis_list_storage(has_limit=False, limit=10)
        if SelectedPagesList!=[]:
            pages = [page for page in pages_iterator]
            try:
                selected_pages = [pages[i] for i in SelectedPagesList]
            except:
                print("pages not in range")
                print("iterating over every page as default fix")
        else:
            selected_pages = pages_iterator
        for page in selected_pages:
            if storage.is_full():
                break
            page_layout = get_page_layout(page)
            wrapped_assembler(page_layout, storage)
        return storage.getData()
            
    return data_extractor


def List_to_string(StringList):
    string=""
    for stringItem in StringList:
        string+=stringItem+" "
    return string

def ngram_assembler_generator(min_n, max_n):
    def ngram_assembler(longString):
        longString=longString.replace("  ", " ")
        longString=longString.replace("\n", "")
        StringList=longString.split(" ")
        n=len(StringList)
        NGrams={}
        for l in range(min_n+1, max_n+1):
            for i in range(n-l):
                NGrams[List_to_string(StringList[i:i+l])]=(i, i+l-1) # associe à chaque ngram l'indide de début et de fin
        return NGrams
    return ngram_assembler

# example
data_extractor=data_extractor_generator(tline_assembler, SelectedPagesList=[0, 1, -1], has_limit=False, limit=10)

def process_pdf(pdf_path):
    with open(pdf_path, 'rb') as pdf_file:
        data=data_extractor(pdf_file)
        return data