#@+leo-ver=4-thin
#@+node:eric.20100607164326.1323:@file /home/eric/xueyang/kegg/kegg_dfba/parser/KeggPathway.py
#@@language python
#@@tabwidth -4
#@+others
#@+node:eric.20100607164326.1324:KeggPathway declarations
#@verbatim
#@+leo-ver=4-thin
#@verbatim
#@+node:eric.20100607163523.1244:@file /home/eric/xueyang/kegg/kegg_dfba/parser/KeggPathway.py
#@verbatim
#@@language python
#@verbatim
#@@tabwidth -4
#@verbatim
#@+others
#@verbatim
#@+node:eric.20100607163523.1245:KeggPathway declarations
#!/usr/bin/env python
"""
Basic class to represent Kegg pathways and nodes.

You can use the function parse_KGML.KGML2Graph to create a KeggPathway object from a KGML file.
"""
import networkx

#@-node:eric.20100607164326.1324:KeggPathway declarations
#@+node:eric.20100607164326.1325:class KeggPathway
#@verbatim
#@-node:eric.20100607163523.1245:KeggPathway declarations
#@verbatim
#@+node:eric.20100607163523.1246:class KeggPathway
class KeggPathway(networkx.DiGraph):
    """
    Represent a Kegg Pathway. Derived from networkx.Digraph, it adds:
    - reactions: a dictionary of all the reactions in the file

    >>> p = KeggPathway()
    >>> p.add_node('gene1', data={'type': 'gene', })
    >>> p.get_node('gene1')
    {'type': 'gene'}

    We can use parse_KGML.KGML2Graph to obtain a graph object:
    >>> from parse_KGML import KGML2Graph
    >>> graphfile = 'data/hsa00510.xml'
    >>> graph = KGML2Graph(graphfile)[1]


    To get a list of the nodes, use the .nodes() method
    >>> graph.nodes()[0:5]
    ['56', '54', '42', '48', '43']
    >>> len(graph.nodes())
    76
    >>> print [graph.get_node(n)['label'] for n in graph.nodes()][0:5]
    ['MGAT1', 'MGAT2', 'C01246', 'TITLE:N-Glycan biosynthesis', 'C03862']

    >>> graph.edges()[0:5]
    [('56', '57'), ('54', '55'), ('54', '37'), ('54', '58'), ('60', '62')]

    To get detailed informations on a node, use .get_node:
    >>> graph.get_node('10')
    {'xy': (580, 317), 'type': 'gene', 'label': 'ALG12'}

    All the annotations (such as node type, etc..), are stored in the .label attribute
    >>> graph.label['10']     #doctest: +ELLIPSIS
    {'xy': (580, 317), 'type': 'gene', 'label': 'ALG12'}

    To obtain a subgraph with only the genes of the pathway, it is recommended to use get_genes: 
    >>> genes_graph = graph.get_genes()
    >>> genes_graph.edges()[0:4]
    [('60', '62'), ('63', '3'), ('63', '2'), ('63', '72')]

    >>> for (node1, node2) in genes_graph.edges()[0:4]:
    ...     print genes_graph.get_node(node1)['label'], genes_graph.get_node(node2)['label']
    GCS1 DAD1...
    ALG5 DPM2
    ALG5 DPM3
    ALG5 DPAGT1



    """
    title = ''
    labels = {}
    reactions = {}


    #@    @+others
    #@+node:eric.20100607164326.1326:get_node
    #@verbatim
    #@    @+others
    #@verbatim
    #@+node:eric.20100607163523.1247:get_node
    def get_node(self, node):
        return self[node]

    #@-node:eric.20100607164326.1326:get_node
    #@+node:eric.20100607164326.1327:get_genes
    #@verbatim
    #@-node:eric.20100607163523.1247:get_node
    #@verbatim
    #@+node:eric.20100607163523.1248:get_genes
    def get_genes(self):
        """
        return a subgraph composed only by the genes

        >>> p = KeggPathway()

        >>> p.add_node('gene1', data={'type': 'gene'})
        >>> p.add_node('compound1', data={'type': 'compound'})

        >>> subgraph = p.get_genes()
        >>> print subgraph.nodes()
        ['gene1']
        """
#        subgraph = self.subgraph([node for node in self.nodes() if self.get_node(node)['type'] == 'gene'])
        genes = []
        labels = {}
        for node1 in self.nodes():
            if self.node[node1]['ntype'] == 'gene':
                genes.append(node1)
                labels[node1] = self.get_node(node1)
#            else:
#                self.labels.pop(node)
        subgraph = self.subgraph(genes)
        subgraph.title = self.title + ' (genes)'
        subgraph.labels = labels
        return subgraph

    #@-node:eric.20100607164326.1327:get_genes
    #@+node:eric.20100607164326.1328:neighbors_labels
    #@verbatim
    #@-node:eric.20100607163523.1248:get_genes
    #@verbatim
    #@+node:eric.20100607163523.1249:neighbors_labels
    def neighbors_labels(self, node):
        """
        like networkx.graph.neighbours, but returns gene label

        >>> p = KeggPathway()

        >>> p.add_node(1, data={'label': 'gene1'})
        >>> p.add_node(2, data={'label': 'gene2'})
        >>> p.add_edge(1, 2)

        >>> p.neighbors(1)
        [2]

        >>> p.neighbors_labels(1)
        {'gene1': ['gene2']}
        """
        neighbours = self.neighbors(node)
        labels = [self.get_node(n)['label'] for n in neighbours]
        return {self.get_node(node)['label']: labels}

    #@-node:eric.20100607164326.1328:neighbors_labels
    #@+node:eric.20100607164326.1329:__repr__
    #@verbatim
    #@-node:eric.20100607163523.1249:neighbors_labels
    #@verbatim
    #@+node:eric.20100607163523.1250:__repr__
    def __repr__(self):
        return self.title + ' pathway' # TODO: __init__ method to make sure self.title exists

    #@-node:eric.20100607164326.1329:__repr__
    #@-others
#@verbatim
    #@-node:eric.20100607163523.1250:__repr__
#@verbatim
    #@-others
#@-node:eric.20100607164326.1325:class KeggPathway
#@-others
#@verbatim
#@-node:eric.20100607163523.1246:class KeggPathway
#@verbatim
#@-others
#@verbatim
#@-node:eric.20100607163523.1244:@file /home/eric/xueyang/kegg/kegg_dfba/parser/KeggPathway.py
#@verbatim
#@-leo
#@-node:eric.20100607164326.1323:@file /home/eric/xueyang/kegg/kegg_dfba/parser/KeggPathway.py
#@-leo
