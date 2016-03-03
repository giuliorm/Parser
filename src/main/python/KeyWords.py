#!/usr/bin/env python3.4
# -*- coding: utf-8
import sys
import pymorphy2
import csv

morph = pymorphy2.MorphAnalyzer()

s = sys.stdin.readline().strip()

arr = s.strip().split(" ")
response = ''
while len(arr) != 0:
    p = morph.parse(arr.pop(0))[0]
    if ('Orgn' in p.tag) or ('Abbr' in p.tag) or ('Name' in p.tag) or ('Surn' in p.tag) or ('Geox' in p.tag) or ('Trad' in p.tag):
        pnf = p.normal_form
        response += pnf + " "
sys.stdout.write(response + '\n')
sys.stdout.flush()
