#!/usr/bin/env python3.4
# -*- coding: utf-8
import numpy as np
import matplotlib.pyplot as plt
from sklearn.feature_extraction.text import CountVectorizer
from matplotlib import rc

font = {'family': 'Droid Sans',
        'weight': 'normal'}
rc('font', **font)


MainText = []
path = '/home/gp/IdeaProjects/Java+Python/python/MainTextHere.csv'
infile = open(path, mode='rt', encoding="utf-8")
news = ''
for row in csv.reader(infile):
    word = row.pop(0)
    p = morph.parse(word)[0]
    if ('Orgn' in p.tag) or ('Abbr' in p.tag) or ('Name' in p.tag)  or ('Surn' in p.tag) or ('Geox' in p.tag) or ('Trad' in p.tag):
        pnf = p.normal_form
        news = news + ' ' + pnf
    if word == '*':
        MainText.append(news)
        news = ''
infile.close()


vectorizer = CountVectorizer(input='content')
dtm = vectorizer.fit_transform(MainText)
vocab = vectorizer.get_feature_names()
type(dtm)
vocab = np.array(vocab)
dtm = dtm.toarray()  # convert to a regular array
vocab = np.array(vocab)

from sklearn.metrics.pairwise import cosine_similarity

dist = 1 - cosine_similarity(dtm)

print(np.round(dist, 2))
norms = np.sqrt(np.sum(dtm * dtm, axis=1, keepdims=True))
dtm_normed = dtm / norms
similarities = np.dot(dtm_normed, dtm_normed.T)



from scipy.cluster import hierarchy


Z = hierarchy.linkage( similarities, method='average' )
plt.figure()
hierarchy.dendrogram(Z, labels=MainText, color_threshold=0.01,  orientation='right', distance_sort=True, show_contracted=True )
plt.show()

labels = [str(x) for x in range(len(MainText))]
Z = hierarchy.linkage( similarities, method='average' )
plt.figure()
hierarchy.dendrogram(Z, labels=labels, color_threshold=0.01,  orientation='right', distance_sort=True, show_contracted=True )
plt.show()
