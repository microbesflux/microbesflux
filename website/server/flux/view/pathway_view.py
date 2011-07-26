# from view.foundations import new_get_json
from view.foundations import ajax_callback
from view.foundations import response_envelope
from view.foundations import table_response_envelope
from view.json import Json
from parser.reaction import parse_reaction_part

import re
digital_pattern = re.compile(r'.*(\d+)')

def _get_compound_list(compond_string):
    coef, comp_list = parse_reaction_part(compond_string)
    return comp_list

def _check_user_compounds_validity(pathway, compond_string):
    if pathway.name == "TOY":
        return True
    coef, comp_list = parse_reaction_part(compond_string)
    for c in comp_list:
        print "Going to check name [" + c.strip() + "]"
        if not pathway.check_valid_names(c.strip()):
            return False
    return True
    
def _attach_suffix(compond_string, suffix):
    comp_list = compond_string.split('+')
    new_list = []
    for comp in comp_list:
        number = 1
        name = ''
        padding = ''
        test = comp.split()
        if len(test) == 1:
            number = ""
            name = test[0]
        else:
            number = float(test[0])
            padding = " "
            name = test[1]  # potential bug here incase it has blank???
        
        new_list.append(str(number) + padding + name + suffix)
    return "+".join(new_list)
    
def _process_pathway_fetch(pathway):
    public_key = 0
    ret = Json("array")
    for rname in pathway.reactions:
        r = pathway.reactions[rname]
        if not r.products or not r.substrates:
            continue 
        json = r.getJson()
        json.add_pair("pk", public_key)
        public_key += 1
        ret.add_item(json)
    return ret

def _process_pathway_add_check(input_params, pathway):
    reactants = input_params.get("reactants", "")
    products = input_params.get('products')
    pathway_name = input_params.get("pathway", "")
    if pathway_name == "BIOMASS":
        return _check_user_compounds_validity(pathway, reactants)
    elif pathway_name == "Inflow":
        return _check_user_compounds_validity(pathway, products)
    elif pathway_name == "Outflow":
        return _check_user_compounds_validity(pathway, reactants)
    else:
        return _check_user_compounds_validity(pathway, reactants) and \
        _check_user_compounds_validity(pathway, products)

def _process_pathway_add(input_params, pathway):
    public_key = len(pathway.reactions)
    
    ko = input_params['ko']
    print "Input ko value is ", ko
    
    reactants = input_params.get("reactants", "")
    arrow = input_params['arrow']
    products = input_params.get('products')
    pathway_name = input_params.get("pathway", "")
    if pathway_name == "BIOMASS":
        if pathway.reactions.has_key("BIOMASS0"):   # Already there
            reaction = pathway.reactions["BIOMASS0"]
            ret = ""
            for i in reaction.substrates:
                ret += str(reaction.stoichiometry[i])
                ret += " "
                ret += i
                ret += " + "
            pathway.update_pathway("BIOMASS0", False, ret + reactants, arrow, "BIOMASS", "BIOMASS")    # update
            new_key = "BIOMASS0"
        else:
            new_key = "BIOMASS0"
            pathway.add_pathway("BIOMASS0", ko, reactants, arrow, "BIOMASS", "BIOMASS")
    
    elif pathway_name == "Inflow":
        pathway.user_reaction_index += 1
        new_key = "Inflow" + str(pathway.user_reaction_index)
        reactants = _attach_suffix( reactants, ".ext" )
        pathway.register_user_pathway(new_key)
        pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
    
    elif pathway_name == "Outflow":
        pathway.user_reaction_index += 1
        new_key = "Outflow" + str(pathway.user_reaction_index)
        products = _attach_suffix( products, ".ext" )
        pathway.register_user_pathway(new_key)
        pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
    
    elif pathway_name == "Heterologous Pathways":
        pathway.user_reaction_index += 1
        new_key = "Heterologous" + str(pathway.user_reaction_index)
        pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
    else:
        pass
        
    j = Json("object")
    tpk = digital_pattern.match(new_key)
    pk = int(tpk.group(1))
    j.add_pair("pk", public_key)
    j.add_pair("reactionid", new_key)
    j.add_pair("ko", ko)
    j.add_pair("reactants", reactants)
    if arrow == '1':
        j.add_pair("arrow", "<==>")
    else:
        j.add_pair("arrow", "===>")
    j.add_pair("products", products)
    j.add_pair("pathway", pathway_name)
    return j


def _process_pathway_update(input_params, pathway):
    key = input_params["pk"]
    new_key = input_params["reactionid"]
    ko = input_params['ko']
    reactants = input_params.get("reactants", "")
    arrow = input_params['arrow']        
    if arrow == "<==>":
        arrow = '1'
    else:
        arrow = '0'
    products = input_params.get('products')
    pathway_name = input_params.get("pathway", "")
    pathway.update_pathway(new_key, ko, reactants, arrow, products, pathway_name)
    
    # Pack results into a JSON object
    j = Json("object")
    j.add_pair("pk", key)   # respect input pathway
    j.add_pair("reactionid", new_key)
    j.add_pair("ko", ko)
    j.add_pair("reactants", reactants)
    if arrow == '1':
        j.add_pair("arrow", "<==>")
    else:
        j.add_pair("arrow", "===>")
    j.add_pair("products", products)
    j.add_pair("pathway", pathway_name)
    return j


def _process_pathway_info(input_params, pathway, cname):
    t = map(str, pathway.statistics())
    
    r0 = Json("object")
    r0.add_pair("name" , "Name of the pathway")
    r0.add_pair("value", cname)
    
    r1 = Json("object")
    r1.add_pair("name" , "Name of the organism")
    r1.add_pair("value", pathway.name)
    
    r2 = Json("object")
    r2.add_pair("name", "Number of all genes/orthologs")
    r2.add_pair("value", t[0])
    
    r3 = Json("object")
    r3.add_pair("name", "Number of annotated genes/orthologs")
    r3.add_pair("value", t[1])
    
    r4 = Json("object")
    r4.add_pair("name", "Number of all pathways")
    r4.add_pair("value", t[2])
    
    r5 = Json("object")
    r5.add_pair("name", "Number of active pathways")
    r5.add_pair("value", t[3])
    
    r = Json("array")
    r.add_item(r0)
    r.add_item(r1)
    r.add_item(r2)
    r.add_item(r3)
    r.add_item(r4)
    r.add_item(r5)
    return r

# Done with new PK
@ajax_callback
@response_envelope
def pathway_add(request):
    pathway = request.session["collection"]
    result = _process_pathway_add(request.GET, pathway)
    request.session.save()
    return result

# Done with new PK
@ajax_callback
@response_envelope
def pathway_update(request):
    pathway = request.session["collection"]
    result = _process_pathway_update(request.GET, pathway)
    request.session.save()
    return result

# Done with new PK
@ajax_callback
@table_response_envelope
def pathway_fetch(request):
    q = request.GET
    method = "pathway_fetch"
    pathway = request.session["collection"]
    return _process_pathway_fetch(pathway)

# Done with new PK
@ajax_callback
def pathway_info(request):
    pathway = request.session["collection"]
    collection_name = request.session["collection_name"]
    result = _process_pathway_info(request.GET, pathway, collection_name)
    return result

# Done with new PK
@ajax_callback
def pathway_add_check(request):
    pathway = request.session["collection"]
    if _process_pathway_add_check(request.GET, pathway):
        return "Valid"
    else:
        return "Invalid"
