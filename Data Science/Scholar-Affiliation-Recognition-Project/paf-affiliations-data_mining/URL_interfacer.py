#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose des fonctions pour récupérer les contenu d'un page désignée par son URL
"""

from urllib.request import urlopen
from urllib.parse import quote
import requests
from time import sleep


# future improvement : make it possible to store the faulty url
class BadURLException(Exception):
    """" raise when the url doesn't exist or doesn't work """

## renvoie une string à partir d'une string url.
#def request(url):
#    result = urlopen(url)
#    return result.read()

# renvoie true si un url existe, false sinon
def checkURL(url):
    request = requests.get(url, timeout = 15)
    return request.status_code == 200

# renvoie un objet exposant les meme comprtement qu'on objet file
# est susceptible de renvoyer une BadURLException ou une autre exception en cas d'autres problèmes
def request(url):
    if checkURL(url):
        sleep(0.6)
        return  urlopen(url, timeout = 60)
    else :
        raise BadURLException()

def quote_url(string):
    return quote(string)