#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
fait des requetes HAL pour récupérer les métadonnées
"""


from bs4 import BeautifulSoup

import URL_interfacer as URL

import JSON_interfacer as JSON

from XMLFinderGenerator import finder_generator

### =========================================================================================
### ***** Récupérer les résultats de requetes à l'API HAL en XML *****
### =========================================================================================

# assemble l'url : changer les valeurs dans le corps de la méthode pour changer la requete
def url_assembler(nb_results="18", search_field="*", search_string="*"):
    prefix = "https://api.archives-ouvertes.fr/search/?q="
    response_format="&wt=xml"
    fields = "&fl=uri_s,docid,producedDate_tdate,title_s,authIdHasStructure_fs,structIsChildOf_fs,halId_s,docType_s,structName_s,structAddress_s"
    return prefix+search_field+":"+search_string+response_format+fields+"&rows="+str(nb_results)

# renvoie une string à partir d'une string url.
request = URL.request

# crée un objet BeautifulSoup à partir d'une string ou d'un objet file en XML
def get_XML_SOUP(XML):
    XML_SOUP = BeautifulSoup(XML, "xml")
    return XML_SOUP

# renvoie une liste des tags <doc> dans l'objet XML_SOUP
def get_List_Docs(XML_SOUP):
    return XML_SOUP.find_all("doc")


### =========================================================================================
### ***** Méthodes pour extraire du XML certaines informations facilement accessibles *****
### =========================================================================================

# Les méthodes suivantes renvoient le contenu des tags de doc spécifiés.

get_docid = finder_generator('int', 'name', "docid")

get_title_iterator = finder_generator('arr', 'name', "title_s")

def get_title(doc):
    for title in get_title_iterator(doc):
        return title

get_date = finder_generator('date', 'name', "producedDate_tdate")

def get_year(doc):
    return get_date(doc)[:4]

get_uri = finder_generator('str', 'name', "uri_s")

get_halId = finder_generator('str', 'name', "halId_s")

get_doctype = finder_generator('str', 'name', "docType_s")

get_fullNames = finder_generator('str', 'name', "authFullName_s")


get_structIsChildOf_iterator = finder_generator('arr', 'name', "structIsChildOf_fs")

get_affiliations = finder_generator('arr', 'name', "authIdHasStructure_fs")

### =========================================================================================
### ***** Méthodes pour extraire du XML les affiliations des auteurs *****
### =========================================================================================

# renvoie un dictionnaire associant un set d'affiliations parentes à chaque affiliation
def get_global_Structure_to_Parent_MAP(doc):
    Strings_iterator = get_structIsChildOf_iterator(doc)
    Structure_to_Parent_MAP={}
    for string in Strings_iterator:
        Split=string.split("_")
        child=Split[1]
        parent=Split[-1]
        if(child!=parent): # élimine les structures parentes d'elles-memes
            if child not in Structure_to_Parent_MAP:
                Structure_to_Parent_MAP[child]={parent}
            else:
                Structure_to_Parent_MAP[child].add(parent)
    return Structure_to_Parent_MAP


# renvoie un Structure_to_Parent_MAP dont les parents sont tous dans affiliationSet
def strict_Structure_to_Parent_MAP(Structure_to_Parent_MAP, affiliationSet):
    strict_Structure_to_Parent_MAP={}
    for affiliation in Structure_to_Parent_MAP:
        if Structure_to_Parent_MAP[affiliation] in affiliationSet:
            strict_Structure_to_Parent_MAP[affiliation]=Structure_to_Parent_MAP[affiliation]
    return strict_Structure_to_Parent_MAP

# ajoute une affiliation à l'affiliationSet d'un auteur - crée le set si nécéssaire
def add_author_affiliation(author, affiliation, affiliation_MAP):
    if author not in affiliation_MAP:
        affiliation_MAP[author]={affiliation}
    else:
        affiliation_MAP[author].add(affiliation)

# renvoie un dictionnaire associant chaque auteur à un set de ses affiliations
def get_affiliation_MAP(doc):
    Strings = get_affiliations(doc)
    affiliation_MAP={}
    for string in Strings:
        Split=string.split("_")
        author=Split[2]
        affiliation=Split[6]
        add_author_affiliation(author, affiliation, affiliation_MAP)
    return affiliation_MAP


# renvoie les affiliations d'un auteur non parentes d'autres affiliations de l'auteur 
def root_affiliations(affiliationSet, Structure_to_Parent_MAP):
    rootAffiliations_Set = affiliationSet.copy()
    for affiliation in affiliationSet:
        if affiliation in Structure_to_Parent_MAP:
            rootAffiliations_Set.difference_update(Structure_to_Parent_MAP[affiliation])
    return rootAffiliations_Set


### =========================================================================================
### ***** Transcrire les arbres d'affiliation en chaines de caractères *****
### =========================================================================================

# remplit recursivement un set genealogy contenant toutes les affiliations mères de affiliation
def complete_recursive_genealogy(affiliation, genealogy, Structure_to_Parent_MAP):
    genealogy.add(affiliation)
    if affiliation in Structure_to_Parent_MAP:
        for parent in Structure_to_Parent_MAP[affiliation]:
            complete_recursive_genealogy(parent, genealogy, Structure_to_Parent_MAP)

# renvoie un set genealogy contenant affiliation et toutes les affiliations mères de affiliation
def complete_genealogy(affiliation, Structure_to_Parent_MAP):
    genealogy = {}
    complete_recursive_genealogy(affiliation, genealogy, Structure_to_Parent_MAP)
    return genealogy


# Ajoute les affiliations récursivement à tree_string sans doublons
# Les enfants sont toujours aoutés avant les parents
def tree_to_recursive_unique_string(affiliationSet, Structure_to_Parent_MAP, tree_string, AlreadyParsedSet):
    parent_Set={}
    for affiliation in affiliationSet:
        if affiliation in Structure_to_Parent_MAP and Structure_to_Parent_MAP[affiliation] not in AlreadyParsedSet:
            tree_string+=" --- "+affiliation
            AlreadyParsedSet.add(affiliation)
            parent_Set.add(Structure_to_Parent_MAP[affiliation])
    tree_string=tree_to_recursive_unique_string(parent_Set, Structure_to_Parent_MAP, tree_string, AlreadyParsedSet)
    return tree_string

# Pour chaque affiliation dans affiliationSet, ajoute à StringsList une string commençant
# par <affiliation> et y ajoute les parents, etc... sans doublons
def tree_to_unique_string(affiliationSet, Structure_to_Parent_MAP):
    StringSet={}
    for affiliation in affiliationSet:
        tree_string=tree_to_recursive_unique_string({affiliation}, Structure_to_Parent_MAP, affiliation, set())
        StringSet.add(tree_string)
    return StringSet

# Renvoie récursivement une liste contenant une string pour chaque chemin dans l'arbre
def tree_to_recursive_path_strings(affiliation, path_string, Structure_to_Parent_MAP):
    #Path_Strings=[path_string] # stocke tous les sous chemins aussi
    Path_Strings=[]
    if affiliation in Structure_to_Parent_MAP:
        parentSet = Structure_to_Parent_MAP[affiliation]
        for parent in parentSet:
            parent_path_string=path_string+" --- "+parent
            Path_Strings+=tree_to_recursive_path_strings(parent, parent_path_string, Structure_to_Parent_MAP)
        return Path_Strings
    return [path_string]

# renvoie un liste de chaque chemin dans l'arbre
def tree_to_path_strings(affiliationSet, Structure_to_Parent_MAP):
    Path_Strings=[]
    for affiliation in affiliationSet:
        Path_Strings+=tree_to_recursive_path_strings(affiliation, affiliation, Structure_to_Parent_MAP)
    return Path_Strings


### =========================================================================================
### ***** Méthodes pour extraire du XML les adresses des affiliations *****
### =========================================================================================

# !!! TO BE Modified after XMLFinderGenerator
def structName_tag_filter(tag):
    return tag.name=='arr' and tag.has_attr('name') and tag['name']=="structName_s"

def get_structNames(doc):
    structName_List=doc.find_all(structName_tag_filter)
    if structName_List!=[]:
        return [string for string in structName_List[0].strings]
    return []

def address_tag_filter(tag):
    return tag.name=='arr' and tag.has_attr('name') and tag['name']=="structAddress_s"

def get_structAddress(doc):
    structAddress_List=doc.find_all(address_tag_filter)
    if structAddress_List!=[]:
        return [string for string in structAddress_List[0].strings]
    return []

def get_address_MAP(doc):
    Address_MAP={}
    structNames=get_structNames(doc)
    structAddress=get_structAddress(doc)
    if len(structNames)==1 and len(structAddress)==1:
        Address_MAP[structNames[0]]=structAddress[0]
    return Address_MAP

### =========================================================================================
### ***** Méthodes pour extraire l'url du document pdf *****
### =========================================================================================

# renvoie l'url potentielle du pdf du document
def get_pdf_url(doc):
    url=get_uri(doc)
    if url[-2]=="v":
        url=url[:-2]
    elif url[-3]=="v":
        url=url[:-2]
    return url+"/document"

# !!! Autres méthodes à ajouter pour gérer les cas où il n'y a pas de document !!!


### =========================================================================================
### ***** Collater les données souhaitées en une structure python *****
### =========================================================================================

# renvoie un dictionnaire dont les clés sont les champs de données souhaitées
def collate_doc_data(doc):
    collated_doc={}
    try:
        collated_doc["file_url"]=get_pdf_url(doc)
    except:pass
    try:
        collated_doc["title"]=get_title(doc)
    except: pass
    try:
        collated_doc["year"]=get_year(doc)
    except: pass
    try:
       collated_doc["authors"]=sets_to_lists_copy(get_affiliation_MAP(doc))
    except: pass
    try:
        collated_doc["global_Structure_to_Parent_MAP"]=sets_to_lists_copy(get_global_Structure_to_Parent_MAP(doc))
    except: pass
    try:
        Address_MAP=get_address_MAP(doc)
    except: pass
    else:
        if Address_MAP!={}:
            print("!!!***=== Address determined ! ===***!!!")
            collated_doc["affiliation_addresses"]=Address_MAP
    assert collated_doc!={}
    return collated_doc

# renvoie un liste de dictionnaires correspondant chacun à un doc
def collate_request_data(result):
    List_Docs=get_List_Docs(get_XML_SOUP(result))
    List_collated_docs=[]
    for doc in List_Docs:
        try:
            List_collated_docs.append(collate_doc_data(doc))
        except Exception as e:
            print("failed to collate this doc")
            print(e)
    return List_collated_docs
    
### =========================================================================================
### ***** Interactions avec le format json *****
### =========================================================================================

# transcrit en string json 
to_json=JSON.to_json

# écrit une string dans le fichier file_name
json_to_file=JSON.json_to_file

def request_to_file(url, file_name):
    json_to_file(sets_to_lists_copy(collate_request_data(request(url))), file_name)

# transforme un objet python en objet python jsonable directement
sets_to_lists_copy = JSON.sets_to_lists_copy

# operation (presque) réciproque
lists_to_sets_copy = JSON.lists_to_sets_copy

# renvoie une structure python composée de listes et dictionnaires correspondant au fichier json
json_to_python = JSON.json_to_python
    

### =========================================================================================
### ***** Debuggage et tests *****
### =========================================================================================


# affiche certaines informations extraites d'un doc
def display_info_doc(doc):
    title=get_title(doc)
    print(title)
    date=get_date(doc)
    print(date)
    MAP=get_affiliation_MAP(doc)
    print(MAP)
    url = get_uri(doc)
    print(url)
    docid=get_docid(doc)
    print(docid)
    print("\n")
    print("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n")
    print("\n")

# affiche pour chaque doc dans XML_SOUP certaines informations extraites
def display_SOUP_info():
    XML_SOUP=get_XML_SOUP(request(url_assembler()))
    print("SOUP is READY :)")
    print("\n")
    for doc in get_List_Docs(XML_SOUP):
        print("================== DOC BEGIN =========" )
        display_info_doc(doc)
        test_tree(doc)
        print("\n")

# methode temporaire pour debugger les arbres d'affiliation
def test_tree(doc):
    MAP=get_affiliation_MAP(doc)
    print("MAP")
    print(MAP)
    print("TREE")
    Structure_to_Parent_MAP=get_global_Structure_to_Parent_MAP(doc)
    print(Structure_to_Parent_MAP)
    for author in MAP:
        print("\n=======================================")
        print(author)
        print("=======================================")
        affiliationSet=root_affiliations(MAP[author], Structure_to_Parent_MAP)
        Paths = tree_to_path_strings(affiliationSet, Structure_to_Parent_MAP)
        #Strings=set()
        #tree_to_separate_strings(TREE, Strings)
        #Strings=tree_to_path_strings(TREE)
        for string in Paths:
            print(string)
        
    