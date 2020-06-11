#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Propose des méthodes pour sauvegarder et charger des modèles de machine learning
"""

import pickle


def dumpModel(model, file_name):
    pickle.dump(model, open(file_name,  'wb'))

def loadModel(file_name):
    return pickle.load(open(file_name, 'rb')
