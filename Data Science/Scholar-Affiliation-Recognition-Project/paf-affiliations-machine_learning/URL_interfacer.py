#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jun 22 17:28:56 2018

@author: xavier
"""

from urllib.request import urlopen
import requests

# renvoie une string à partir d'une string url.
def request(url):
    result = urlopen(url)
    return result.read()

# renvoie true si un url existe, false sinon
def checkURL(url):
    request = requests.get(url)
    return request.status_code == 200