#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Mets en forme les données et compile un classifieur binaire pour reconnaitre des affiliations
"""

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
import pickle

affiliation_file="affiliation.txt"
not_affiliation_file="not_affiliation.txt"


def shuffle_raw_data(tuple_set):
    data_set, label_set=tuple_set
    assert len(data_set) == len(label_set), "data_set and label_set have unequal length"
    index = [i for i in range(len(data_set))]
    r.shuffle(index)
    return [data_set[i] for i in index], [label_set[i] for i in index]

def get_raw_data_labels(affiliation_file, not_affiliation_file):
    data_set=[]
    label_set=[]
    not_data_set=[]
    not_label_set=[]
    with open(affiliation_file, 'r') as afile:
        for row in afile:
            data_set.append(row)
            label_set.append(1)
    with open(not_affiliation_file, 'r') as nafile:
        for row in nafile:
            not_data_set.append(row)
            not_data_set.append(0)
    not_data_set, not_label_set = shuffle_raw_data((not_data_set, not_label_set))
    not_data_set=not_data_set[:len(data_set)]
    not_label_set=not_label_set[:len(data_set)]
    data_set+=not_data_set
    label_set+=not_label_set
    return data_set, label_set

tuple_set=get_raw_data_labels(affiliation_file, not_affiliation_file)
print(tuple_set[1][-1])




data_set, label_set = shuffle_raw_data(tuple_set)



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
proportion = 0.8
data_train_set, label_train_set, data_test_set, label_test_set=create_train_test_set(data_tuple, proportion)


tfidf_vect=TfidfVectorizer(ngram_range = (1,3), sublinear_tf=True)

def get_vocabulary():
    return tfidf_vect.vocabulary_

print(data_train_set)

data_train_set=tfidf_vect.fit_transform(data_train_set).toarray()
data_test_set=tfidf_vect.transform(data_test_set).toarray()

vocabulary = get_vocabulary()
dim = len(vocabulary)

model = Sequential()
model.add(Dense(64, input_dim=dim, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(64, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(1, activation='sigmoid'))

model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])

model.fit(data_train_set, label_train_set,
          epochs=2,
          batch_size=128)

score = model.evaluate(data_test_set, label_test_set, batch_size=128)
print(score)

#pickle.dump(model,open(v.classifierModelSaveName , 'wb'))
