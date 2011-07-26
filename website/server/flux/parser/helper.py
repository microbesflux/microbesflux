#!/usr/bin/env python

import cPickle

def linecrop(l):
    for line in l:
        yield line.rstrip('\n')

def read_pickle(name):
    f = open(name, 'rb')
    return cPickle.load(f)
    f.close()

def write_pickle(data, file):
    f = open(file, 'wb')
    cPickle.dump(data, f)
    f.close()

def toint(s):
    if s == 'n':
        return 1
    elif s == '(n+1)':
        return 1
    elif s == '(n-1)' or s== 'n-1':
        return 1
    elif s == '2n':
        return 1
    elif s == '(m+n)':
        return 1
    elif s == 'm':
        return 1
    else:
        return int(s)

""" This function is to remove things like cpd:C07030
TODO: This function need to be rewritten as
    just remove comma and return a list of things
            however, sometimes there are cases like cpd:C07030 cpd:C07031 in the file
            which is because kegg data is noisy. we have to remove it """
def remove_comma(s):
    if s.find(' ') != -1:
        s = s.split()[0] # we have to take the part that is seperated by a comma
    if s.find(':') != -1:
        l = s.split(':')
        return l[1]
    else:
        return s

def is_active(entry):
    graphics = entry.find('graphics')
    if graphics.get('bgcolor') == "#BFFFBF":
        return True
    else:
        return False
