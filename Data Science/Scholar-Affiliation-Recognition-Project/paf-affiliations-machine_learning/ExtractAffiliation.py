#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jun 21 16:37:41 2018

@author: xavier
"""
import csv

import numpy as np
import random as r

from nltk.corpus import stopwords
stop = set(stopwords.words('english')) 
stop.update(stopwords.words('french')) 
stop.update(['.', ',', '"', "'", '?', '!', ':', ';', '(', ')', '[', ']', '{', '}'])

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import CountVectorizer

from keras.models import Sequential
from keras.layers import Dense, Dropout

import machineLearning_metricsMethods as m

affiliation_file="affiliation.txt"
not_affiliation_file="not_affiliation.txt"

raw_data_file=""

def get_shuffled_raw_data_labels(raw_data_file):
    data_set=[]
    label_set=[]
    with open(raw_data_file, newline='') as csvfile:
        data_reader = csv.reader(csvfile, delimiter=' ', quotechar='|')
        rows=[row for row in data_reader]
        r.shuffle(rows)
        for row in rows:
            data_set.append(row[0])
            label_set.append(row[1])
    return data_set, label_set

data_set, label_set= get_shuffled_raw_data_labels(raw_data_file)
proportion = 0.8


tfidf_vect=TfidfVectorizer(ngram_range = (1,3), stop_words = stop, sublinear_tf=True, min_df=2, max_df=0.5, max_features=2500)
def vectorize(data_set):
    return tfidf_vect.transform(data_set)

data_set=vectorize(data_set)

def create_train_test_set(data_tuple, proportion):
    data_set, label_set=data_tuple
    n=len(data_set)
    s = int(n*proportion)
    data_train_set=data_set[:s]
    label_train_set=label_set[:s]
    data_test_set=data_set[s:]
    label_test_set=label_set[s:]
    return data_train_set, label_train_set, data_test_set, label_test_set

data_tuple=data_set, label_set
data_train_set, label_train_set, data_test_set, label_test_set=create_train_test_set(data_tuple, proportion)







model = Sequential()
model.add(Dense(64, input_dim=20, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(64, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(1, activation='sigmoid'))

model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy',])

model.fit(data_train_set, label_train_set,
          epochs=10,
          batch_size=128)

score = model.evaluate(data_test_set, label_test_set, batch_size=128)