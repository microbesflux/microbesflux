#!/usr/bin/env python

""" Refactored version of the parser """
import sys,os
sys.path.append("/research-www/engineering/tanglab/")
os.environ['DJANGO_SETTINGS_MODULE'] = 'flux.settings'
print sys.path
from constants import kegg_database
from parser.keggpathway import *
import cPickle

def generate_pathway(bac_name, collection_name = ""):
    # kegg_database = "/Users/youxu/ProgramInput/kegg_database/"
    filelist = os.listdir(kegg_database + bac_name + "/")
    pathway = PathwayNetwork(kegg_database, bac_name,  filelist, collection_name)
    pathway.read_metabolisms()
    return pathway

def is_incell(s):
    if s.startswith("[c]"):
        return True
    else:
        return False
cset = set()
def _reformat(s):
    cs = s.split("+")
    for i in xrange(len(cs)):
        t = cs[i]
        flag = False
        t = t.replace(":", "_")
        t = t.replace("-", "_")
        t = t.strip()
        l = t.split()
        coef = ""
        if len(l) > 1:  # has coef
            coef = l[0].replace("(","")
            coef = coef.replace(")", "")
            compd = l[1]
        else:
            compd = t
        if compd.endswith("[e]"):
            compd = compd.replace("[e]", ".ext")
        elif compd.endswith("[c]"):
            compd = compd.replace("[c]", "")
        else:
            assert compd.find("[") == -1
        compd = compd.replace("(", "_")
        compd = compd.replace(")", "_")
        compd = compd.strip()
        compd = "c_" + compd
        if compd.endswith(".ext"):
            cset.add(compd[:-4])
        else:
            cset.add(compd)
        cs[i] = coef + " " + compd
    return cs

# 634

def reformat_reaction(s):
    reversible = True
    # s = s.split(':')[1]
    s = s.strip()
    if s.startswith("[c]"):
        s = s[5:]
    if s.find("<==>")!=-1:
        l, r = s.split("<==>")
        print "reversible"
    elif s.find("-->")!=-1:
        l, r = s.split("-->")
        reversible = False
    else:
        print "Impossible", s
    return " + ".join(_reformat(l)), reversible, " + ".join(_reformat(r))

def dump_ampl(p):
    f = open("iso.ampl", "w")
    mapf = open( "iso_map.txt", "w")
    reportfile = open("iso_header.txt", "w")
    p.output_ampl(f, mapf, reportfile, objective_type = "biomass" )
    f.close()
    mapf.close()
    reportfile.close()
    
usage = "python toy_test.py reaction_list"
if __name__ == '__main__':
    print sys.argv
    if len(sys.argv) < 2:
        print usage
        sys.exit(-1)
    else:
        bac_name = "TOY"	# An empty folder, dummy
        p = generate_pathway(bac_name, "toy_example")
        print p
    print p.reactions
    f = open(sys.argv[1])
    c = 0
    lb = {}
    ub = {}
    for l in f:
        c +=1
        reaction_name, reaction, _ub, _lb = l.split("%")
        reaction_name = reaction_name.strip()
        lb[reaction_name] = _lb.strip()
        ub[reaction_name] = _ub.strip()
        reaction = reaction.strip()
        if reaction.startswith("[e]"):
            continue
        re, arrow, prod = reformat_reaction(reaction)
        mname = "user"
        print l
        print re, arrow, prod
        p.add_pathway(reaction_name, "false", re, arrow, prod, mname)
        
    # print c
    bounds = p.get_bounds()
    # print bounds
    for r in bounds:
        # print r
        bounds[r][0] = lb[r]
    for r in bounds:
        # print r
        bounds[r][1] = ub[r]
    #bounds["L-LACD2"][0] = 4.1
    #bounds["L-LACD2"][1] = 4.2
    print len(cset)
    l = list(cset)
    l.sort()
    print "<<<<"
    for a in l:
        print a
    print ">>>>>"
    dump_ampl(p)
    f = open("toy/toy_pathway.pickle", "w")
    cPickle.dump(p, f)
    f.close()