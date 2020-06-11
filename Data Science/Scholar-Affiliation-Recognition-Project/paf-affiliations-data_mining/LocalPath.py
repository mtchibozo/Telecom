#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Propose des méthodes pour obtenir le répertoire du fichier qui contient ce script, et pour en faire le répertoire de travail
"""

import os

# note : this is the most reliable way to achieve this result. However, it might not be very good practice
def self_path():
    return os.path.dirname(os.path.realpath( __file__ ))

def reself_directory():
    os.chdir(self_path()) 

# this will be executed on executing any file importing this script. This might also not be very good practice
reself_directory()
