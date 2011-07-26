#!/usr/bin/env python
from pprint import pprint
try:
    from collections import defaultdict
except:
    from defaultdict import defaultdict

# from reference import ReactionDB
from compounddb import CompoundDB

from enzyme import Enzyme
from reaction import Reaction
from metabolism import Metabolism
from view.json import Json

# Model View Controller
class OptimizationModel:
    def __init__(self):
        self.pathway = None
        self.variable = None
        self.constraints = None
        self.bounds = None
        self.objective = None
    
    def json_view(self):
        pass
    
    def ampl_view(self, f, mapfile, model_type="fba", additional_file = None):
        """ Output an AMPL file """
        print "In AMPL VIEW", model_type
        for var in self.variable:
            name = self.variable[var]
            mapfile.write(name)
            mapfile.write(" ")
            mapfile.write(var)
            mapfile.write("\n")
            
        f.write("# variables\n")
        defined = {}
        for var in self.variable:
            name = self.variable[var]
            if defined.get(name, False):
                continue
            defined[name] = True
            b = self.bounds[var]
            if b:
                f.write("var " + name + ">=" + str(b[0]) + ", <=" + str(b[1]) + ";\n")
            else:
                f.write("var " + name + ";\n")
        
        f.write("# Objective function \n")
        f.write("maximize Obj: \n")
        f.write("0 ")
        for item in self.objective:
            var = self.variable.get(item, False)
            if var:
                weight = self.objective[item]
                f.write(" + " + str(weight) + " * " + var)
        f.write(";\n")
        
        # Replace RXXXX to VXXXX in the SV=0 representation
        f.write("# Constraints\n")
        for compound in self.constraints:
            if compound.endswith(".ext"):
                continue
            linear_equation = self.constraints[compound]
            if len(linear_equation) == 0:
                continue
            target = []
            for item in linear_equation:
                content = item.split()  # +  N  R00010
                reaction = content[-1]  #   The R00010 Part
                var = self.variable.get(reaction, False)
                if var:
                    target.append(' '.join(content[:-1]) + " * " + var)
            if len(target) > 0:
                tempc = str(compound)
                tempc = tempc.replace("-", "_")
                tempc = tempc.replace("+", "_")
                tempc = tempc.replace("*", "_")
                tempc = tempc.replace("/", "_")
                f.write("subject to " + tempc + ":\n")
                f.write("\t")
                f.write(' '.join(target))
                f.write(" = 0 ;\n")
        
        f.write("option solver ipopt;\n")
        if model_type == "fba":
            f.write("solve > /dev/null ;\n")
            f.write("""printf " === Flux Results ===\\n"; """ )
            f.write("""printf "Objective function value %6.6f\\n", Obj;  \n""")
            f.write("""for {k in 1.._nvars} {printf "%s\\t%6.6f\\n", _varname[k], _var[k];}\n""")
        elif model_type == "dfba":
            first_line = additional_file.readline()[:-1]
            header = first_line.split()
            for h in header:
                h.strip()
            v_position = {}
            count = 1
            for l in additional_file.readlines():
                f.write('printf "===== Data Point ' + str(count) + ' ====\\n";\n')
                count +=1 
                l = l[:-1]
                values = l.split()
                for v in values:
                    v.strip()
                f.write("""# DFBA part \n""")
                for i, v in enumerate(header):
                    print i, v
                    f.write("fix " + self.variable[v] + ":=" + values[i] + ";\n")
                f.write("solve > /dev/null ;\n");
                f.write("""printf "Objective function value %6.6f\\n", Obj;  \n""")
                f.write("""for {k in 1.._nvars} {printf "%s\\t%6.6f\\n", _varname[k], _var[k];}\n""")
                for v in header:
                    f.write("unfix " + self.variable[v] + ";\n")
                
            f.write("# All dfba sections done\n")
""" 
    One PathwayNetwork for each bacteria
"""

### The centeral class for this project ### 

class PathwayNetwork(object):
    def __init__(self, database, bacteria_name, filelist, collection_name = ""):
        self.database = database
        self.collection_name = collection_name        
        self.name = bacteria_name
        self.filelist = [i for i in filelist if i.endswith("xml")]
        self.reactions = {}
        
        self.compounddb = CompoundDB()
        self.user_reaction_index = 0
        self.objective = {}
        self.sv = defaultdict(list)
        self.bound = defaultdict(list)
        self.model = None
        self.possible_names = {}    # Remember all short name 
        self.user_pathway = {}  # For name checking
        
        self.active_gene = 0
        self.total_gene = 0
        self.active_orthlog = 0
        self.total_orthlog = 0
        
    ####################################################
    #############  metabolism ##########################
    ####################################################

    def read_metabolisms(self):
        for i in self.filelist:
            if i.find("01100")!=-1: # Skip this metabolism
                continue
            file_path = self.database + self.name + "/" + i
            self.add_metabolism_in_batch(Metabolism(file_path))

    def add_metabolism_in_batch(self, m):
        self.active_gene        += m.active_gene
        self.total_gene         += m.total_gene
        self.active_orthlog     += m.active_orthlog
        self.total_orthlog  += m.total_orthlog
                
        for name, r in m.reactions.iteritems():
            if r.substrates and r.products:
                if self.reactions.has_key(name):
                    # Merge two actions from two pathways, don't change anything
                    mat = self.reactions[name].metabolism + "/" + m.name
                    self.reactions[name].set_metabolism_name(mat)
                else:    # Construct a new reaction 
                    self.reactions[name] = r
                    for short_name in r.longname_map:
                        self.possible_names[short_name] = True
                        self.possible_names[r.longname_map[short_name]] = True
                    r.set_metabolism_name(m.name)
                    is_active = True
                    if m.reaction_name_to_active_map:
                        is_active = m.reaction_name_to_active_map[name]
                    r.active = is_active
    
    
    ####################################################
    #############  pathway info ########################
    ####################################################
    def statistics(self):
        active_reactions = 0
        for name, r in self.reactions.iteritems():
            if r.active:
                active_reactions+=1        
        return (self.total_gene   + self.total_orthlog, \
                self.active_gene  + self.active_orthlog, \
                len(self.reactions),  active_reactions)

    ####################################################
    #############  check names          ################
    ####################################################
    def check_valid_names(self, name):
        return self.compounddb.is_a_valid_name(name)
    
    ####################################################
    #############  pathway fetch/update ################
    ####################################################
    def register_user_pathway(self, user_pathway_name):
        self.user_pathway[user_pathway_name] = True
        
    def check_user_pathway(self, user_pathway_name):
        return self.user_pathway.has_key(user_pathway_name)
    
    ### similar to the process method in alias. 
    def __get_coef_and_name(self, part_of_equation):
        coef     = []
        compound = []
        part_of_equation = str(part_of_equation)
        for l in part_of_equation.split("+"):
            k = l.split()
            if len(k) == 1:
                coef.append(1.0)
                compound.append(k[0].strip())
            else:
                coef.append(float(k[0]))
                compound.append(k[1].strip())
        return (coef, compound)
    
    def __construct_reaction(self, rname, ko, reac, arrow, prod, mname):
        """ Code to parse and construct a reaction"""
        # Step 1. split reactants.
        rname = str(rname)
        mname = str(mname)
         
        r = Reaction(rname)
        left_coef,  left_comp = self.__get_coef_and_name(reac)
        right_coef, right_comp = self.__get_coef_and_name(prod)
        
        r.substrates.extend(left_comp)
        r.products.extend(right_comp)
        print "[Add pathway] The substrates are ", r.substrates
        print "[Add pathway] The products are ", r.products
        
        for i in xrange(len(left_coef)):
            r.stoichiometry[left_comp[i]] = left_coef[i]
        
        for i in xrange(len(right_coef)):
            r.stoichiometry[right_comp[i]] = right_coef[i]
        print "[Add pathway] r.stoichiometry is ", r.stoichiometry
        print "Get external arrow is ", arrow
        r.reversible = arrow
        r.arrow = arrow
        if ko == "false":
            r.ko = False
        else:
            r.ko = True
        r.metabolism = mname
        r.active = True
        return r

    def __invalidate_cached_bounds(self):
        self.objective = {}
        self.sv = defaultdict(list)
        self.bound = defaultdict(list)
        
    def add_pathway(self, reaction_name, ko, reactants, arrow, products, metabolism_name):
        if self.reactions.has_key(reaction_name):
            m = str(metabolism_name)
            self.reactions[reaction_name].set_metabolism_name(self.reactions[reaction_name].metabolism + "/" + m)
            return
        r = self.__construct_reaction(reaction_name, ko, reactants, arrow, products, metabolism_name)
        self.reactions[reaction_name] = r
        self.__invalidate_cached_bounds()

    def update_pathway(self, reaction_name, ko, reactants, arrow, products, metabolism_name):
        r = self.__construct_reaction(reaction_name, ko, reactants, arrow, products, metabolism_name)
        self.reactions[reaction_name] = r
        self.__invalidate_cached_bounds()
        
    ####################################################
    #############  Objective Function   ################
    ####################################################

    def _get_objective(self):
        # print "In get_objective",
        if len(self.objective) == 0:
            # Regenerate all reactions and objectives
            for r in self.reactions:
                self.objective[r] = 1
            for name in self.reactions:
                if self.reactions[name].ko:    # knockout
                    self.objective[name] = 0
        # Use cached version
        return self.objective

    def get_objective_weights(self):
        return self._get_objective()
        
    ####################################################
    #############  SV =0 constraints    ################
    ####################################################

    def get_sv(self):
        # print "Someone called GetSV"
        if len(self.sv) == 0:
            for name in self.reactions:
                rec = self.reactions[name]
                for t in rec.substrates:
                    self.sv[t].append(" + " + str(rec.stoichiometry[t]) + " " + rec.name)
                for t in rec.products:
                    self.sv[t].append(" - " + str(rec.stoichiometry[t]) + " " + rec.name)
        return self.sv

    def get_long_name(self, a_short_name):
        return self.compounddb.get_long_name(a_short_name)
        
    ####################################################
    #############  Variable bounds      ################
    ####################################################
    def get_bounds(self):
        if len(self.bound) == 0:
            for name in self.reactions:
                rec = self.reactions[name]
                ub = 100.0
                lb = 0.0
                if rec.reversible:
                    lb = -100.0
                if rec.ko:
                    ub = 0.0
                    lb = 0.0
                self.bound[name].append(lb)
                self.bound[name].append(ub)
        return self.bound

    def generate_constraints(self):
        # print "Generate constraint called", len(self.sv)
        self.get_sv()
        self.model.constraints = self.sv    # a dictionary

    def generate_variables(self):
        self.variable_database = {}
        self.compound_database = {}
        start = 0
        for name in self.reactions:
            if name == "Inflow1" or name == "Outflow2":
                print ">>>>>>>>> here" 
            r = self.reactions[name]
            if not r.active or r.ko:
                continue
            if name == "Inflow1" or name == "Outflow2":
                print ">>>>>>>>> here2"
            if not self.variable_database.has_key(name):
                self.variable_database[name] = "V" + str(start)
                start +=1
        self.model.variable = self.variable_database
        
    def generate_objectives(self, objective_type):
        # print "Generate Objective", len(self.objective)
        # print "Generate Objective"
        if objective_type == "user":
            self._get_objective()
            self.model.objective = self.objective
        else:
            # initialize things 
            self._get_objective()
            self.model.objective = self.objective
            for key in self.model.objective:
                self.model.objective[key] = 0.0
            reaction = self.reactions["BIOMASS0"]
            self.model.objective["BIOMASS0"] = 1.0
        
    def generate_bounds(self):
        # print "Generate Bound", len(self.bound)
        self.get_bounds()
        self.model.bounds = self.bound

    def generate_optimization_model(self, objective_type):
        self.model = OptimizationModel()
        self.generate_variables()
        self.generate_bounds()
        self.generate_objectives(objective_type)
        self.generate_constraints()
        return self.model
        
    def output_model_report(self, report_file):
        a, b, c, d = self.statistics()
        import datetime
        report_file.write("Model created: " + datetime.datetime.now().isoformat() + "\n")
        report_file.write("======================================\n")
        report_file.write("========  Part I: Genome Info ========\n")
        report_file.write("======================================\n")
        
        
        report_file.write("Name of the pathway: ")
        report_file.write(self.collection_name)
        report_file.write("\n")
        
        report_file.write("Name of the organism: ")
        report_file.write(self.name)
        report_file.write("\n")
        
        report_file.write("Number of all genes/orthologs: ")
        report_file.write(str(a))
        report_file.write("\n")
        
        
        report_file.write("Number of all genes/orthologs: ")
        report_file.write(str(a))
        report_file.write("\n")

        report_file.write("Number of annotated genes/orthologs: ")
        report_file.write(str(b))
        report_file.write("\n")

        report_file.write("Number of all pathways: ")
        report_file.write(str(c))
        report_file.write("\n")

        report_file.write("Number of active pathways: ")
        report_file.write(str(d))
        report_file.write("\n")

        report_file.write("======================================\n")
        report_file.write("========   Part II: Pathways =========\n")
        report_file.write("======================================\n")
        
        for n in self.reactions:
            r = self.reactions[n]
            if len(r.products) > 0 and len(r.substrates) > 0:
                report_file.write(r.quick_view())
                report_file.write("\n")

        report_file.write("======================================\n")
        report_file.write("========   Part III: AMPL File  ======\n")
        report_file.write("======================================\n")
        
        
    def output_ampl(self, amplfile, mapfile, reportfile, model_type="fba", additional_file = None, objective_type = "user"):
        
        print "I get object type as", objective_type 
        model = self.generate_optimization_model(objective_type)
        self.output_model_report(reportfile)
        model.ampl_view(amplfile, mapfile, model_type, additional_file)

    def output_sbml(self, f, model_name):
        from libsbml import libsbml as lb
        d=lb.SBMLDocument(3,1)
        m=d.createModel(model_name)
        c=m.createCompartment()
        c.setId("cell")
        c.setName("cell")
        
        species = set()
        for n in self.reactions:
            r = self.reactions[n]
            if not r.substrates:
                continue
            tempr=m.createReaction()
            tempr.setId(n)
            tempr.setReversible(r.reversible)
            tempr.setCompartment("cell")
            
            if r.ko:    # knockout
                tempr.setAnnotation("<microbesflux:KO xmlns:microbesflux=\"http://tanglab.engineering.wustl.edu/dtd.xml\">" + str(r.ko) +"</microbesflux:KO>")
                
            if r.metabolism.find("Inflow") != -1:
                tempr.setAnnotation("<microbesflux:user-reaction xmlns:microbesflux=\"http://tanglab.engineering.wustl.edu/dtd.xml\">Inflow</microbesflux:user-reaction>")
            
            if r.metabolism.find("Outflow") != -1:
                tempr.setAnnotation("<microbesflux:user-reaction xmlns:microbesflux=\"http://tanglab.engineering.wustl.edu/dtd.xml\">Outflow</microbesflux:user-reaction>")
            
            if r.metabolism.find("Heterologous") != -1:
                tempr.setAnnotation("<microbesflux:user-reaction xmlns:microbesflux=\"http://tanglab.engineering.wustl.edu/dtd.xml\">Heterologous</microbesflux:user-reaction>")
            
            if r.metabolism.find("BIOMASS") != -1:
                tempr.setAnnotation("<microbesflux:user-reaction xmlns:microbesflux=\"http://tanglab.engineering.wustl.edu/dtd.xml\">BIOMASS</microbesflux:user-reaction>")
            
            for reactant in r.substrates:
                species.add(reactant)
                temp_reac = tempr.createReactant()
                temp_reac.setSpecies(reactant)
                temp_reac.setStoichiometry(r.stoichiometry[reactant])
            for product in r.products:
                species.add(product)
                temp_prod = tempr.createProduct()
                temp_prod.setSpecies(product)
                temp_prod.setStoichiometry(r.stoichiometry[product])
            tempr.setName(n)
        
        for spe in species:
            s = m.createSpecies()
            ids = str(spe)
            
            ids = ids.replace("_", "")
            ids = ids.replace(" ", "")
            ids = ids.replace("-", "")
            ids = ids.replace("(", "")
            ids = ids.replace(")", "")
            ids = ids.replace("]", "")
            ids = ids.replace("[", "")
            
            s.setId(ids)
            s.setName(spe)
            s.setCompartment("cell")
        
        f.write("<?xml version='1.0' encoding='UTF-8'?>\n")
        f.write(d.toSBML())
    
    def external_compond_to_flux(self, name, signature):
        for rname, r in self.reactions.iteritems():
            if ' '.join(r.substrates).find(name + '.ext') != -1:
                signature.append(-1.0)
                return rname
            elif ' '.join(r.products).find(name + '.ext') != -1:
                signature.append(1.0)
                return rname
        return "ERROR"
    def flux_to_variable(self, flux_name):
        return self.variable_database[flux_name]
