#!/usr/bin/env python

""" 
	Author: Eric Xu, Washington University
		Nov. 2009

	Inspired by pyKegg package
	kudos to the original author 
"""


import xml.etree.cElementTree as ET
import pylab

def KGML2Graph(xmlfile, filter_by = ()):
    
    pathway = KeggPathway()
    nodes = {}
    genes = []
    pathway.reactions = {}
    pathway.relations = {}
    pathway.labels = {}     # dictionary to keep node labels (gene name?)

    tree = ET.parse(xmlfile)

    organism = tree.getroot().get('org')
    if organism == 'ko':
        entriestype = ('ortholog', 'map', 'compound',)
    elif organism == 'ec':
        raise NotImplementedError('Didn\'t implement EC pathways yet')
    else:   # this is an organism-specific pathway
        entriestype = ('gene', 'compound', 'map')

    # Get pathway title (store it in pathway.title)
    pathway.title = tree.getroot().get('title')
    pathway.name = tree.getroot().get('name')
    pathway.id = tree.getroot().get('id')
    
    # parse and add nodes
    for entry in tree.getiterator('entry'):

        node_type = entry.get('type')   # can be ('gene', 'compound', 'map'..)
        
        name = entry.get('name')
        node_id = entry.get('id')
        
        graphics = entry.find('graphics')
        node_title = graphics.get('name')
        node_x = int(graphics.get('x'))  # Storing the original X and Y to recreate KEGG layout
        node_y = int(graphics.get('y'))
        print "node is", node_title, "position is ", node_x, node_y 

        nodes[node_id] = (name, node_title, node_type)
        pathway.labels[node_id] = node_title
        pathway.add_node(node_id, xy=(node_x, node_y),ntype=node_type)
        print pathway.node[node_id]


    for rel in tree.getiterator('relation'):
        e1 = rel.get('entry1')
        e2 = rel.get('entry2')
        pathway.add_edge(e1, e2)
        pathway.relations[e1+'_'+e2] = rel
   

    for reaction in tree.getiterator('reaction'):

        id = reaction.get('name')
        substrates = []
        products = []

        for sub in reaction.getiterator('substrate'):
            substrates.append(sub.get('name'))

        for prod in reaction.getiterator('product'):
            products.append(sub.get('name'))

        pathway.reactions[id] = {'reaction': reaction, 'substrates': substrates, 'products': products}

    return tree, pathway, nodes, genes

def plot_starlike(pathway):
    pylab.figure()
    networkx.draw_circular(pathway, labels=pathway.labels)
    pylab.title(pathway.title)
    title = pathway.title.replace('/', '-') # TODO: which is the proper way to remove / in a filename?
    pylab.savefig('./plots/' + title + '.png')
    pylab.show()


def plot_original(pathway):
    pos = {}
    for node1 in pathway.nodes():
        print node1
        pos[node1] =  pathway.node[node1]['xy'] 
    pylab.figure()
    networkx.draw_networkx(pathway, pos, labels=pathway.labels)
    title = pathway.title.replace('/', '-') # TODO: which is the proper way to remove / in a filename?
    pylab.savefig('./plots/' + title + '_original_layout.png')
    pylab.show()


def convert_to_gml(pathway):
    """
    write the pathway to the gml format
    - http://www.infosun.fim.uni-passau.de/Graphlet/GML/
    """
    networkx.write_gml(pathway, pathway.title + '.gml')

if __name__ == '__main__':
    import sys
    import argparse
    logging.basicConfig(level=logging.DEBUG)
    parser = argparse.ArgumentParser(description='parse a KGML pathway file and convert it to python/gml/image')
    parser.add_argument('-pathwayfile', '--pathway', dest='pathwayfile', type=str, default='data/hsa00510.xml')
    parser.add_argument('-d', '-draw', dest='draw_to_image', action='store_true', default=False)
    parser.add_argument('-c', '-draw_circular', dest='draw_circular', action='store_true', default=False)
    parser.add_argument('-g', '-write_gml', dest='write_gml', action='store_true', default=False)
    args = parser.parse_args()
    # logging.debug(args)

    pathwayfile = args.pathwayfile

    (tree, pathway, nodes, genes) = KGML2Graph(pathwayfile)
    print "read parse done"
    if args.draw_circular:
        logging.debug('plotting')
        plot_starlike(pathway)    
        plot_starlike(pathway.get_genes())    

    if args.draw_to_image:
        plot_original(pathway)
        plot_original(pathway.get_genes())
    
    if args.write_gml:
        logging.debug('plotting')
        convert_to_gml(pathway)
