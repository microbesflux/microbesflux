""" An enzyme regulates a reaction """
class Enzyme:
    def __init__(self, genes, reactions = [], active = False, type = "G"):
        self.genes = genes
        self.genes_asstring = ' '.join(self.genes)
        self.active = active
        self.regulated_reactions = reactions
        self.regulated_reactions_asstring = ' '.join(self.regulated_reactions)
        self.reaction = None
        self.type = type

    def set_reaction(self, reaction):
        self.reaction = reaction

    def __repr__(self):
        sign = "-"
        if self.active:
            sign = "+"
        return self.type + sign + '\t' + self.genes_asstring + '\t||=\t' + self.regulated_reactions_asstring

    def yield_variable(self, variable_db, compounds_balance_db, is_inactive_needed):
        if is_inactive_needed or self.active:
            if len(self.regulated_reactions) > 0:
                last = str(len(variable_db))
                variable_db[self.regulated_reactions_asstring] = "V" + last
                #print self.genes_asstring, "====>", self.regulated_reactions_asstring, ":=>", variable_db[self.regulated_reactions_asstring]
