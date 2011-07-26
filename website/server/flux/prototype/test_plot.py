import networkx as nx
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot
import matplotlib.pyplot as plt


G=nx.Graph()
G.add_node(1)
G.add_nodes_from([2,3,4,5,6,7,8,9,10])
#nx.draw_graphviz(G)
#nx_write_dot(G, 'node.png')
nx.draw(G)
plt.savefig("node.png")

