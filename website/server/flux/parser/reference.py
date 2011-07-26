from alias import *
""" 
    Singleton class 
    using the Borg design pattern
"""

import pprint

class ReactionDB(object):
    __hivemind = {} 

    def __init__(self):
        self.__dict__ = self.__hivemind
        self.IMPOSSIBLE = "None"
        self.IMPOSSIBLE_STR = "Unknown Compound"
        if not hasattr(self, "has_read"):
            self.read_db()
        
    def read_db(self):
        self.compound_alias, self.compound_long_name = read_compound_alias_database()
        
        self.set_of_valid_names = set(self.compound_long_name.values())
        self.reaction_compound_relation = read_reactions_database()
        self.has_read = True
        self.__hivemind = self.__dict__

    def get_long_name(self, compound_short_name):
        r = self.compound_long_name.get(compound_short_name, self.IMPOSSIBLE_STR)
        
        if r == self.IMPOSSIBLE_STR:
            cpd_alias = self.compound_alias.get(compound_short_name, self.IMPOSSIBLE_STR)
            r = self.compound_long_name.get(cpd_alias, self.IMPOSSIBLE_STR)
        if r == self.IMPOSSIBLE_STR:
            print "[WARN] Can't find long name for -> \'" + compound_short_name + "'"
        return r
        
    def get_stoichiometry(self, reaction_name, molecular_name):
        a = self.reaction_compound_relation.get((reaction_name, molecular_name), self.IMPOSSIBLE)
        # 1000 is an impossible number,  if fail first, use alias
        if a == self.IMPOSSIBLE:
            cpd_alias = self.compound_alias.get(molecular_name, self.IMPOSSIBLE)
            if cpd_alias == self.IMPOSSIBLE:
                print "[WARN] Record not found (R,C)", reaction_name, molecular_name
                return self.IMPOSSIBLE
            else:
                a = self.reaction_compound_relation.get((reaction_name, cpd_alias), self.IMPOSSIBLE)
        if a == self.IMPOSSIBLE:
            print "[WARN] Record not found (R,C)", reaction_name, molecular_name
            return self.IMPOSSIBLE
        return a

    def is_a_valid_name(self, a_long_name):
        return a_long_name in self.set_of_valid_names
        
    def dump(self):
        pprint.pprint(self.reaction_compound_relation)
