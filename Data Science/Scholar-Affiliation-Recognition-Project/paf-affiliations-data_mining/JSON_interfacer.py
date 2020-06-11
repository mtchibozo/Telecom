#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jun 22 17:28:25 2018

@author: xavier
"""
import json

### =========================================================================================
### ***** Ecrire les données dans un fichier json *****
### =========================================================================================

# transcrit en string json 
def to_json(jsonable_structure):
    return json.dumps(jsonable_structure, ensure_ascii=False, indent=0)

# écrit une string dans le fichier file_name
def json_to_file(jsonable_structure, file_name):
    with open(file_name, 'w') as f:
        f.write(to_json(jsonable_structure))
        f.close()

### =========================================================================================
### ***** Récupérer les données depuis un fichier json *****
### =========================================================================================

# renvoie une structure python composée de listes et dictionnaires correspondant au fichier json
def json_to_python(file_name):
    with open(file_name, 'r') as f:
        f.seek(0)
        return json.load(f)

### =========================================================================================
### ***** Convertir des objets non jsonables en objets jsonable et vice versa *****
### =========================================================================================

def sets_to_lists_copy(almost_jsonable_structure):
    if type(almost_jsonable_structure).__name__ in ['set', 'list']:
        jsonable_structure=[]
        for item in almost_jsonable_structure:
            jsonable_structure.append(sets_to_lists_copy(item))
    elif type(almost_jsonable_structure).__name__ == 'dict':
        jsonable_structure = {}
        for item in almost_jsonable_structure:
            jsonable_structure[item]=sets_to_lists_copy(almost_jsonable_structure[item])
    else:
        jsonable_structure = almost_jsonable_structure
    return jsonable_structure


def lists_to_sets_copy(json_loaded_structure):
    if type(json_loaded_structure).__name__ == 'list':
        set_structure=set()
        for item in json_loaded_structure:
            if type(item).__name__ in ['list', 'set', 'dict']: # or not in list of immutables, but this should be enough
                set_structure=[]
                for item in json_loaded_structure:
                    set_structure.append(lists_to_sets_copy(item))
                break
        else:
            for item in json_loaded_structure:
                set_structure.add(lists_to_sets_copy(item))
    elif type(json_loaded_structure).__name__ == 'dict':
        set_structure = {}
        for item in json_loaded_structure:
            set_structure[item]=(lists_to_sets_copy(json_loaded_structure[item]))
    else:
        set_structure = json_loaded_structure
    return set_structure

