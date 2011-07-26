#!/usr/bin/env python
""" This will plot the network constructed from KGML"""

#import networkx as nx
#import matplotlib
#matplotlib.use('Agg')
#import matplotlib.pyplot
#import matplotlib.pyplot as plt
from parser.keggpathway import PathwayNetwork
from constants import kegg_database
import sys
import os
# import random

def generate_pathway(bac_name):
    filelist = os.listdir(kegg_database + bac_name + "/")
    pathway = PathwayNetwork(kegg_database, bac_name,  filelist)
    pathway.read_metabolisms()
    return pathway
    
def write_down(pathway):
    node_adj = {}
    for rname, r in pathway.reactions.iteritems():
        if r.products and r.substrates:
            if r.name not in node_adj:
                node_adj[r.name] = []
            for s in r.substrates:
                for p in r.products:
                    if s not in node_adj:
                        node_adj[s] = []
                    node_adj[s].append(r.name)
                    node_adj[r.name].append(p)
    f = open(pathway.name + ".adjlist", "w")
    for key, item in node_adj.iteritems():
        f.write(key)
        f.write(' ')
        f.write(' '.join(item))
        f.write('\n')
    f.close()

    
if __name__ == '__main__':
    if len(sys.argv) < 2 or len(sys.argv[1]) != 3:
        print usage
        sys.exit(-1)
    else:
        bac_name = sys.argv[1]
        p = generate_pathway(bac_name)
        print "Number of reactions is", len(p.reactions)
        write_down(p)
