#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Utilise un modèle de machine learning pour concevoir un fonction is_affiliation(string)
"""
from keras.models import load_model as load
import pickle

model_filepath = "telecom_overtrained_model.h5"
tfidf_vect_filepath = "telecom_overtrained_model_tfidf.pickle"
model = load(model_filepath)

tfidf_vect = pickle.load(open(tfidf_vect_filepath, 'rb'))

def is_affiliation(string):
    print(string)
    return bool(model.predict_classes(tfidf_vect.transform([string]).toarray())[0][0])