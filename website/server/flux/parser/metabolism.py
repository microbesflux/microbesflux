""" A metabolism is one kegg file that contains compounds and enzymes.
Each enzyme controls reaction(s)"""
# from enzyme import Enzyme
from reaction import Reaction
from reference import ReactionDB
from helper import *
try:
    import xml.etree.cElementTree as ET
except:
    import elementtree.ElementTree as ET

class Metabolism:
    def __init__(self, f = None):
        self.title = ''
        self.name = ''
        self.compounds_id = {}  # String -> int (inner id)
        self.file = f
        
        self.active_gene = 0
        self.total_gene = 0
        self.active_orthlog = 0
        self.total_orthlog = 0
        
        self.reactiondb = ReactionDB()
        self.reactions = {}
        
        self.reaction_name_to_active_map = {}
        if f:
            print "[INFO] Read file", f
            self.__readfile(f)
    
    
    def __repr__(self):
        return self.title + "\t" + self.name
    
    def __mark_reaction_active(self, reaction_list, is_active):
        for r in reaction_list:
            self.reaction_name_to_active_map[r] = \
                self.reaction_name_to_active_map.get(r, False) \
                or is_active
    
    def __parse_compound(self, entry):
        """ Parse the compound name to an internal id
            This information might be useless, however
        """
        compound_name = remove_comma(entry.get('name'))
        compound_id = entry.get('id')
        self.compounds_id[compound_name] = compound_id
        pass
    
    def __parse_gene(self, entry):
        """ Parse the gene information
                each entry might have multiple genes
                because multiple genes controls one reaction
        """
        gene_names = entry.get('name')
        gene_list = map(remove_comma, gene_names.split())
        """ reaction_id is a really really bad design"""
        reactions = entry.get('reaction')
        regulated_reactions = []
        if reactions:
            regulated_reactions = map(remove_comma, reactions.split())
            active = is_active(entry)
            if active:
                self.active_gene+=1
            self.total_gene +=1
            self.__mark_reaction_active(regulated_reactions, active)
    
    def __parse_ortholog(self, entry):
        """
            Parse the ortholog information
            the only difference is that now we record it in
                self.ortholog_enzyme
        """
        ortholog_names = entry.get('name')
        ortholog_list = map(remove_comma, ortholog_names.split())
        reactions = entry.get('reaction')
        regulated_reactions = []
        if reactions:
            regulated_reactions = map(remove_comma, reactions.split())
            active = is_active(entry)
            self.__mark_reaction_active(regulated_reactions, active)
            if active:
                self.active_orthlog += 1
            self.total_orthlog +=1
    
    def __construct_reaction_from_kgml(self, reaction, rid, reversible):
        substrates = []
        products = []
        stoichiometry = {}

        for sub in reaction.getiterator('substrate'):
            name = remove_comma(sub.get('name'))
            substrates.append(name)
            stoichiometry[ name ] = self.reactiondb.get_stoichiometry(rid, name)

        for prod in reaction.getiterator('product'):
            name = remove_comma(prod.get('name'))
            products.append(name)
            stoichiometry[ name ] = self.reactiondb.get_stoichiometry(rid, name)

        r = Reaction(rid)
        r.substrates = substrates
        r.products = products
        r.stoichiometry = stoichiometry
        r.reversible = reversible
        return r
    
    def __construct_reaction_from_reactionlst(self, reaction, rid, reversible):
        substrates = []
        products = []
        stoichiometry = {}
        longname_map  = {}
        
        r = Reaction(rid)
        substrates = self.reactiondb.get_stoichiometry(rid, "_substrates_")
        products   = self.reactiondb.get_stoichiometry(rid, "_products_")
        # print "Get RID", rid
        # print "sub is", substrates
        if substrates == "None":
            return None
        for sub in substrates:
            stoichiometry[ sub ] = self.reactiondb.get_stoichiometry(rid, sub)
            longname_map[ sub ] = self.reactiondb.get_long_name(sub)
            # print sub, self.reactiondb.get_long_name(sub)
            
        # print 
        for prod in products:
            # print '"' + prod + '"', '"'+ self.reactiondb.get_long_name(prod)+'"'
            stoichiometry[ prod ] = self.reactiondb.get_stoichiometry(rid, prod)
            longname_map[ prod ] = self.reactiondb.get_long_name( prod )
            
        # print
        # print "===="
        r.substrates = substrates
        r.products = products
        r.stoichiometry = stoichiometry
        r.reversible = reversible
        r.longname_map = longname_map
        return r
    
    def __parse_reactions(self, tree):
        """
            Generate a reaction dictionary called reactions
            that maps name to reaction object
            Then set the reactions in enzyme to be right
        """
        for reaction in tree.getiterator('reaction'):
            rid = reaction.get('name')
            rid = remove_comma(rid.split()[0])
            reversible = False

            if reaction.get('type') == 'reversible':
                reversible = True
            
            r =  self.__construct_reaction_from_reactionlst(reaction, rid, reversible)
            if not r:
                return
            if r.products and r.substrates:
                self.reactions[rid] = r
    
    def __parse_map(self, entry):
        pass
    
    def __parse_group(self, entry):
        pass
    
    def __parse_entries(self, tree):
        for entry in tree.getiterator('entry'):
            t = entry.get('type')
            if not t:
                print entry
                continue
            if t == 'compound':
                self.__parse_compound(entry)
            elif t == 'ortholog':
                self.__parse_ortholog(entry)
            elif t == 'gene':
                self.__parse_gene(entry)
            elif t == 'map':
                self.__parse_map(entry)
            elif t == 'group':
                self.__parse_group(entry)
            else:
                print "[WARN] Unknown entry type >>>", t, "<<< Ignored"
    
    def __readfile(self, xmlfile):
        # print "=== Processing file", xmlfile, "======"
        tree = ET.parse(xmlfile)
        self.title = tree.getroot().get('title')  # human readable name
        # self.name = tree.getroot().get('name')    # like path:12301
        self.name = tree.getroot().get('name')    # like path:12301
        self.__parse_entries(tree)
        self.__parse_reactions(tree)
    
    
    def register_variable(self, variable_database, compound_database, is_need_inactive):
        for gene in self.gene_enzyme:
             gene.yield_variable(variable_database, compound_database, is_need_inactive)
        for ortholog in self.ortholog_enzyme:
             ortholog.yield_variable(variable_database, compound_database, is_need_inactive)
