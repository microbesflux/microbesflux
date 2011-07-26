#!/usr/bin/env python

""" Refactored version of the parser """
import sys,os
from constants import kegg_database
from parser.keggpathway import *

def generate_pathway(bac_name):
    # kegg_database = "/Users/youxu/ProgramInput/kegg_database/"
    filelist = os.listdir(kegg_database + bac_name + "/")
    pathway = PathwayNetwork(kegg_database, bac_name,  filelist)
    pathway.read_metabolisms()
    return pathway

usage = "python facet.py three_letter_words"
if __name__ == '__main__':
    if len(sys.argv) < 2 or len(sys.argv[1]) != 3:
        print usage
        sys.exit(-1)
    else:
        bac_name = sys.argv[1]
        p = generate_pathway(bac_name)
        
        print p.reactions['R04241']
        print p.reactions['R04241'].getJson()
