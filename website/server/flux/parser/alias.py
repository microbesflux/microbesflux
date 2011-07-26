#!/usr/bin/env python
""" Parsing alias for compounds and reactions """
import os
import cPickle
from helper import *
from constants import baseurl
# from flux.models import Compound

""" 
    Return the mapping as 
    {compound_id: the_alias_compound_id}
"""

""" The same as relation is tricky """
""" A couple of things to note here
    1. there could be a "+" sign in compound names, we have to remove it
"""
def tidy_name(raw_name):
    r = raw_name[12:]  # name starts from the 12th col
    r = r.replace(";", "")  # Remove ";" 
    r = r.replace("+", "") # Remove plus sign
    r = r.replace(" ", "-") # Replace space from the compound name
    return r

def read_compound_alias_database():
    final_data = None
    if os.path.exists(baseurl + 'kegg/compound_lite.pk'):
        final_data = read_pickle(baseurl + 'kegg/compound_lite.pk')
    else:
        result = {}
        long_name = {}
        
        cfile = open(baseurl + 'kegg/compound_lite', 'r')
        l = linecrop(cfile)
        try:
            while (True):
                a1 = l.next()
                assert (a1[:5] == 'ENTRY')
                name = a1.split()[1]
                obs = a1.split()[2]
                if obs == 'Obsolete':
                    a2 = l.next()
                    result[name] = a2.split()[3]
                    l.next()
                else:
                    lname = l.next()    # NAME line
                    long_name[name] = tidy_name(lname)
                    a2 = l.next()       # advance to the remark line
                    if a2[0] == '/':
                        result[name] = name
                        continue
                    else:
                        assert a2[:6]=='REMARK'
                        temp = a2.split()[3:]
                        if len(temp) == 0:
                            result[name] = name
                        else:   # it has a "Same as:" field, we link everything in Same as: to the current field
                            for items in temp:
                                result[items] = name
                            result[name] = name
                            # result[name] = temp[0]
                        l.next()
        except StopIteration:
            pass
        final_data = (result, long_name)
        write_pickle(data=final_data, file= baseurl + 'kegg/compound_lite.pk')
        # Now inserting things into database
    return final_data
        
""" check the format like C00010(n) """
def is_poly(s):
    temp = s.split("(")
    if len(temp) == 1:
        return False
    else:
        return True

""" Make C00010(n) to C00010"""
def strip_poly(s):
    return s.split("(")[0]

""" A help function for read_reactions_database """
def process(db, i):
    name = i[:6]
    left, right = i[7:].split('=')
    # parse the left part
    db[(name, "_substrates_")] = []
    db[(name, "_products_")] = []
    for l in left[:-1].split(' + '):
        k = l.split()
        # print k
        if len(k) == 1:     # The coefficient is omitted, so assign it to 1
            if not is_poly(l):
                asub = l.strip()
                db[(name, asub)] = 1
                db[(name, "_substrates_")].append(asub)
            else:           # It is a poly molecular, so assign it 
                asub = strip_poly(l).strip()
                db[(name, asub)] = 100
                db[(name, "_substrates_")].append(asub)
        else:
            asub = k[1].strip()
            if not is_poly(k[1]):   
                db[(name, asub)] = toint(k[0])
            else:
                db[(name, asub)] = 0
            db[(name, "_substrates_")].append(asub)
            
    # process the right part
    for r in right[1:-1].split(' + '):
        k = r.split()
        # print k
        if len(k) == 1:
            if not is_poly(r):
                aprod  = r.strip()
                db[(name, aprod)] = 1
                db[(name, "_products_")].append(aprod)
            else:
                aprod  = strip_poly(r).strip()
                db[(name, aprod)] = 100
                db[(name, "_products_")].append(aprod)
        else:
            aprod  = k[1].strip()
            if not is_poly(k[1]):
                db[(name, aprod)] = toint(k[0])
            else:
                db[(name, aprod)] = 100 # toint(k[0])
            db[(name, "_products_")].append(aprod)
    # print "Products", db[(name, "_products_")]
    # print "Substrates", db[(name, "_substrates_")]

""" 
This function will return a dictionary with elements
        {( reaction_id, compound_id ) : coefficient} 
"""
def read_reactions_database():
    result = {}
    if os.path.exists(baseurl + 'kegg/reaction_lst.pk'):
        result = read_pickle(baseurl + 'kegg/reaction_lst.pk')
        return result
    else:
        rfile = open(baseurl + 'kegg/reaction.lst', 'r')
        for i in rfile:
            if i[0] == '#':
                continue
            else:
                process(result, i)
        write_pickle(data = result, file = baseurl + 'kegg/reaction_lst.pk')
        return result
