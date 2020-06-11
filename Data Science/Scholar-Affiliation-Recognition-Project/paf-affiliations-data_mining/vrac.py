#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jun 21 10:11:11 2018

@author: xavier
"""

# transcrit les informations données au format json
def to_json(url_string, title_string, date_string, year_string, affiliation_MAP):  
    json_string="{\n"
    json_string+="\"url\": \""+url_string+"\",\n"
    json_string+="\"title\": \""+title_string+"\",\n"
    json_string+="\"date\": \""+date_string+"\",\n"
    json_string+="\"year\": \""+year_string+"\",\n"
    json_string+="\"authors\": "
    json_string+=json.dumps(affiliation_MAP, ensure_ascii=False, indent=0)
#   json_string+="[\n"
#    for author in affiliation_MAP:
#        json_string+="{ \"name\": \""+author+"\", "
#        json_string+="\"affiliations\": [ "
#        for path_string in tree_to_path_strings(affiliation_MAP[author]):
#            json_string+="{ \"name\": \""+path_string+"\" }, "
#        json_string=json_string[:-2] # enlève la dernière virgule
#        json_string+=" ] }, \n"
#    json_string=json_string[:-3] # enlève la dernière virgule
#   json_string+="\n]"
    json_string+="\n}"
    return json_string

# transcrit les informations utiles d'un tag doc en json
def doc_to_json(doc):
    url_string=get_pdf_url(doc)
    title_string=get_title(doc)
    date_string=get_date(doc)
    year_string=date_string[0:4]
    affiliation_MAP=get_affiliation_MAP(doc)
    return to_json(url_string, title_string, date_string, year_string, affiliation_MAP)


def python_to_json(url_string, title_string, date_string, year_string, affiliation_MAP):
    List=[url_string, title_string, date_string, year_string, affiliation_MAP]
    return json.dumps(List)

# transcris tous les docs résultants de l'url en json et les écrit dans un fichier file_name
def request_to_json(url, file_name):
    XML_SOUP=get_XML_SOUP(request(url))
    List_Docs=get_List_Docs(XML_SOUP)
    with open(file_name, 'w') as f:
        f.write("[")
        for doc in List_Docs[:-1]:
            f.write(doc_to_json(doc))
            f.write(", \n")
        f.write(doc_to_json(List_Docs[-1]))
        f.write("]")

# =========================================================================

# méthode récursive qui renvoie le dictionnaire des arbre des parents de affiliation
def recursive_tree(affiliation, childOf_MAP):
    if affiliation not in childOf_MAP:
            return {}
    else:
        return {parent:recursive_tree(parent, childOf_MAP) for parent in childOf_MAP[affiliation]}

# renvoie tous les arbre d'affiliations ayant une racine dans affiliationList
def affiliation_tree(affiliationSet, childOf_MAP):
    affiliationBase_Set = affiliationSet.copy()
    for affiliation in affiliationSet:
        if affiliation in childOf_MAP:
            affiliationBase_Set.difference_update(childOf_MAP[affiliation])
                # enlève de affiliationBase_Set les affiliations parentes d'affiliations déjà dans affiliationSet
                # en effet elle se retrouveront dans les arbres ayant ces dernières pour racines
                # les affiliations restantes ne sont soit pas parentes,
                # soit parentes sans qu'aucun des enfants ne soit dans affiliationSet
    affiliation_TREE={}
    for affiliation in affiliationBase_Set:
        affiliation_TREE.update({affiliation:recursive_tree(affiliation, childOf_MAP)})
    return affiliation_TREE


# renvoie un dictionnaire associant chaque auteur à un arbre d'affiliations
def compute_affiliation_TREE(affiliation_MAP, childOf_MAP):
    affiliation_TREE={}
    for author in affiliation_MAP:
        affiliationSet=affiliation_MAP[author]
        affiliation_TREE[author]=affiliation_tree(affiliationSet, childOf_MAP)
    return affiliation_TREE

# idem qu'au dessus mais à partir du seul paramètre doc
# renvoie un dictionnaire associant chaque auteur à un arbre d'affiliations
def get_affiliation_TREE(doc):
    affiliation_MAP=get_affiliation_MAP(doc)
    childOf_MAP=get_structIsChildOf_MAP(doc)
    return compute_affiliation_TREE(affiliation_MAP, childOf_MAP)

# renvoie un childOf_MAP dont les parents sont tous dans affiliationSet
def strict_ChildOf_MAP(childOf_MAP, affiliationSet):
    strict_childOf_MAP={}
    for affiliation in childOf_MAP:
        if childOf_MAP[affiliation] in affiliationSet:
            strict_childOf_MAP[affiliation]=childOf_MAP[affiliation]
    return strict_childOf_MAP

# renvoie un dictionnaire d'arbres d'affiliations où chaque arbre ne contient que des 
# affiliations directement listées comme celles de l'auteur
def compute_strict_affiliation_TREE(affiliation_MAP, childOf_MAP):
    affiliation_TREE={}
    for author in affiliation_MAP:
        affiliationSet=affiliation_MAP[author]
        strict_childOf_MAP=strict_ChildOf_MAP(childOf_MAP, affiliationSet)
        strict_childOf_MAP=strict_ChildOf_MAP(childOf_MAP, affiliationSet)
        affiliation_TREE[author]=affiliation_tree(affiliationSet, strict_childOf_MAP)
    return affiliation_TREE

# idem qu'au dessus mais à partir du seul paramètre doc
# renvoie un dictionnaire d'arbres d'affiliations où chaque arbre ne contient que des 
# affiliations directement listées comme celles de l'auteur
def get_strict_affiliation_TREE(doc):
    affiliation_MAP=get_affiliation_MAP(doc)
    childOf_MAP=get_structIsChildOf_MAP(doc)
    return compute_strict_affiliation_TREE(affiliation_MAP, childOf_MAP)


### ****  Structure d'arbre ad-hoc :
    # vocabulaire : on appelle parent d'un noeud les noeuds qui en descendent dans l'arbre
    # structure recursive : 
        # à chaque noeud-enfant est associé un dictionnaire dont les clés sont les parents du noeud
        # arbre vide : {}
        # racine seule : {root:{}}
        # racine et deux enfants : {root:{child1:{}, child2:{}}}
        # deux arbre dans la meme structure : 
            # {root1:{child1:{}, child2:{}}, root2:{child3:{},child4{}}}
        # et ainsi de suite...
    
# =========================================================================


# Modifie StringSet recursivement pour y ajouter séparement toutes les affiliations dans TREE
def tree_to_separate_strings(TREE, StringSet):
    for affiliation in TREE:
        StringSet.add(affiliation)
        tree_to_separate_strings(TREE[affiliation], StringSet)

# Ajoute les affiliations récursivement à tree_string sans doublons
# Les enfants sont toujours aoutés avant les parents
def tree_to_unique_string(TREE, tree_string, StringSet):
    for affiliation in TREE:
        if affiliation not in StringSet:
            tree_string+=" --- "+affiliation
            StringSet.add(affiliation)
    for affiliation in TREE:
        tree_string=tree_to_unique_string(TREE[affiliation], tree_string, StringSet)
    return tree_string

# Pour chaque affiliation dans TREE, ajoute à StringsList une string commençant
# par <affiliation> et y ajoute les parents, etc... sans doublons
def concatenate_affiliations(TREE):
    StringList=[]
    for affiliation in TREE:
        tree_string=tree_to_unique_string(TREE[affiliation], affiliation, set())
        StringList.append(tree_string)
    return StringList

# Renvoie récursivement une liste contenant une string pour chaque chemin dans l'arbre
def tree_to_recursive_path_strings(TREE, path_string):
    if TREE=={}:
        return [path_string]
    Path_Strings=[]
    for affiliation in TREE:
        for path in tree_to_recursive_path_strings(TREE[affiliation], path_string+" --- "+affiliation):
            Path_Strings.append(path)
    return Path_Strings

# renvoie un liste de chaque chemin dans l'arbre
def tree_to_path_strings(TREE):
    Path_Strings=[]
    for affiliation in TREE:
        Path_Strings+=tree_to_recursive_path_strings(TREE[affiliation], affiliation)
    return Path_Strings


# ===========================================

def sets_to_lists_in_place(almost_jsonable_structure):
    if type(almost_jsonable_structure).__name__ == 'set':
        almost_jsonable_structure=list(almost_jsonable_structure)
        print(almost_jsonable_structure)
        for item in almost_jsonable_structure:
            sets_to_lists_in_place(item)
    elif type(almost_jsonable_structure).__name__ == 'dict':
        for item in almost_jsonable_structure:
            sets_to_lists_in_place(almost_jsonable_structure[item])
            
def lists_to_sets_in_place(json_loaded_structure):
    if type(json_loaded_structure).__name__ == 'set':
        json_loaded_structure=list(json_loaded_structure)
        for item in json_loaded_structure:
            sets_to_lists_in_place(item)
    elif type(json_loaded_structure).__name__ == 'dict':
        for item in json_loaded_structure:
            sets_to_lists_in_place(json_loaded_structure[item])
