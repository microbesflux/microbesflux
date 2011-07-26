    """
    ### TODO, this method will be deprecated soon
    def add_reaction(self, pk, reactants, arrow, products, ko):
        # print "In metabolism", self.name, " add/update reaction \"" + pk + "\""
        temp1 = reactants.split('+')
        reversible = False
        if arrow == '1':
            reversible = True
        rea = []
        pro = []
        sto = {}
        for i in temp1:
            number = 1
            name = ''
            test = i.split()
            if len(test) == 1:
                number = 1
                name = test[0]
            else:
                number = float(test[0]) # changed from int
                name = test[1]
            rea.append(name)
            sto[name] = number
        # print "reactant is", rea

        temp2 = products.split('+')
        for i in temp2:
            number = 1
            name = ''
            test = i.split()
            if len(test) == 1:
                number = 1
                name = test[0]
            else:
                number = float(test[0])
                name = test[1]
            pro.append(name)
            sto[name] = number
        # print " product is", pro
        # print " knockout is ", ko
        r = Reaction(pk)
        r.substrates.extend(rea)
        r.products.extend(pro)
        for i in sto:
            r.stoichiometry[i] = sto[i]
        r.reversible = reversible
        r.ko = ko
        self.reactions[pk] = r
        return self.reactions[pk]
    """
    
    
    """
    def update_reaction(self, public_key, reactants, arrow, products, ko):
        return self.add_reaction(public_key, reactants, arrow, products, ko)

        total_gene = set()
        active_gene = set()

        total_ortholog = set()
        active_ortholog = set()

        total_reactions = 0
        active_reactions = set()

        for i in self.metabolism:
            # Step 1: do stat on genes
            for j in i.gene_enzyme:
                total_gene.add(j.genes_asstring)
                if j.active:
                    active_gene.add(j.genes_asstring)
                    for t in j.regulated_reactions:
                        active_reactions.add(t)

            # Step 2: do stat on orthology
            for j in i.ortholog_enzyme:
                total_ortholog.add(j.genes_asstring)
                if j.active:
                    active_ortholog.add(j.genes_asstring)
                    for t in j.regulated_reactions:
                        active_reactions.add(t)
            total_reactions += len(i.reactions)
        rs = (len(total_gene) + len(total_ortholog), len(active_gene) + len(active_ortholog), total_reactions, len(active_reactions))
        return rs
        """
