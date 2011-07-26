from view.json import Json

"""
    # TODO
    2. filter out the plus sign 
"""

def parse_reaction_part(compond_string):
    comp_list = compond_string.split('+')
    coef   = []
    result = []
    for comp in comp_list:
        test = comp.split()
        if len(test) == 1:
            coef.append(1.0)
            name = test[0]
        else:
            coef.append(float(test[0]))
            name = test[1]
    result.append(name)
    return (coef, result)
    
class Reaction:
    
    def __init__(self, name):
        self.name = name
        self.longname_map = {}
        self.reversible = False
        self.products = []
        self.substrates = []
        self.stoichiometry = {}
        self.ko = False
        self.metabolism = None
        self.active = True
        
    def __repr__(self):
        return self.name + str(self.reversible) + ' '.join(map(str, self.products)) + ' '.join(map(str, self.substrates))

    # TODO: later this => long name has to lookup the whole kegg
    def _get_long_name(self, s):
        # print "In R1, get long name for ", s
        if self.longname_map.has_key(s):
            return self.longname_map[s]
        else:
            return s
    
    def set_metabolism_name(self, m):
        self.metabolism = m
    
    #### Used by Report. 
    def quick_view(self):
        l = []
        if self.ko:
            l.append(" X ")
        else:
            l.append("   ")
        l.append(self.name)
        l.append(" : ")
        l.append(' + '.join(map(str, self.substrates)))
        if self.reversible:
            l.append(' <--> ')
        else:
            l.append(' ---> ')
        l.append(' + '.join(map(str, self.products)))
        return ''.join(l)
        
    def getJson(self):
        """ Return a JSON object """
        r = Json("object")
        r.add_pair("reactionid", self.name)
        s = []
        for t in self.substrates:
            s.append(str(self.stoichiometry[t]) + ' ' + self._get_long_name(t))
        r.add_pair("reactants", " + ".join(s))
        s = []
        for t in self.products:
            s.append(str(self.stoichiometry[t]) + ' ' + self._get_long_name(t))
        r.add_pair("products", " + ".join(s))
        if self.reversible:
            r.add_pair("arrow", "<==>")
        else:
            r.add_pair("arrow", "===>")

        ko_label = Json()
        ko_label.set_label('"ko"')
        ko_value = Json()

        if self.ko:
            ko_value.set_label("true")
        else:
            ko_value.set_label("false")

        r.add_pair(ko_label, ko_value)
        if self.metabolism:
            r.add_pair("pathway", self.metabolism)
        return r
    
