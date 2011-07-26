from parser.keggpathway import PathwayNetwork
from constants import kegg_database
from view.json import Json

from django.http import HttpResponse
import os

import re
digital_pattern = re.compile(r'.*(\d+)')

def retrieve_collection(collection_name, s):
      # s is session, this method is obsolete now
    if s['collection_name'] != collection_name:
        # input collection_name is not current in session
        return None
    return s['collection']

def save_required(method):
    return method in ["pathway_add", "pathway_update", "user_obj_update",
                      "user_obj_fetch", "model_sv", "model_bound_fetch"]

def new_get_json(method, input_params, pathway):
    r = ""
    if method == "pathway_add": # merged
        """ Input parameters: pathway, products, reactants"""
        ko = input_params['ko']
        reactants = input_params.get("reactants", "")
        arrow = input_params['arrow']
        products = input_params.get('products')
        pathway_name = input_params.get("pathway", "")
        new_key = ''
        # pk = 0
        if pathway_name == "BIOMASS":
            """ If user gives us biomass, we have to make sure that 
                the left side of the equation is updated
                reactants --> BIOMASS
            """
            # 1. Check if we can find a metabolism that contains USER:BIOMASS pathway
            for m in pathway.metabolism:
                if m.name == "USER:BIOMASS" and len(m.reactions) > 0:
                    new_key = m.reactions.keys()[0]
                    break
            # 2.1. If there is already a biomass, we have to update its reactant list 
            if len(new_key) > 0:
                reaction = pathway.reactions[new_key]
                ret = ""
                for i in reaction.substrates:
                    ret += str(reaction.stoichiometry[i])
                    ret += " "
                    ret += i
                    ret += " + "
                pathway.add_pathway(new_key, False, ret + reactants, arrow, "BIOMASS", "BIOMASS")
            else:       # 2.2. If there is no existing BIOMASS pathway, add a new BIOMASS 
                pathway.user_reaction += 1
                new_key = "Biomass" + str(pathway.user_reaction)
                pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
        
        elif pathway_name == "Inflow":
            pathway.user_reaction += 1
            new_key = "Inflow" + str(pathway.user_reaction)
            pathway.register_user_pathway(new_key)
            pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
        
        elif pathway_name == "Outflow":
            pathway.user_reaction += 1
            new_key = "Outflow" + str(pathway.user_reaction)
            pathway.register_user_pathway(new_key)
            pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
        
        elif pathway_name == "Heterologous Pathways":
            pathway.user_reaction += 1
            new_key = "Heterologous" + str(pathway.user_reaction)
            pathway.add_pathway(new_key, ko, reactants, arrow, products, pathway_name)
        else:
            pass
            
        j = Json("object")
        tpk = digital_pattern.match(new_key)
        pk = int(tpk.group(1))
        j.add_pair("pk", 100000 + pk)
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
    
    elif method == "pathway_update": # merged
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
        
        j = Json("object")
        j.add_pair("pk", key)
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
        
    elif method == "pathway_info": # merged
        t = map(str, pathway.statistics())
        r1 = Json("object")
        r1.add_pair("name" , "Name of the pathway")
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
        r.add_item(r1)
        r.add_item(r2)
        r.add_item(r3)
        r.add_item(r4)
        r.add_item(r5)
    
    elif method == "user_obj_update":   # changed
        key = str(input_params["pk"])
        if len(key) < 5:
            new_key = 'R' + '0' * (5 - len(key)) + key
        else:
            new_key = 'R' + key
        #w = input_params["w"][0].encode('ascii', 'ignore')
        w = input_params["w"]
        weight = float(w)
        pathway.objective[new_key] = weight
        
        r = Json()
        r.add_pair("pk", key)
        r.add_pair("r", new_key)
        r.add_pair("w", str(weight))
        return r

    elif method == "user_obj_fetch":    # changed
        objective_json = pathway.json_objective()
        return objective_json

    elif method == "model_sv":
        """ Output the SV=0 equations """
        sv = pathway.json_sv()
        # print "In Model SV, Now SV is", len(pathway.sv)
        t = len(pathway.sv)
        input_params['startRows'] = '0'
        input_params['endRow'] = str(t - 1)
        input_params['totalRows'] = str(t)     # later get_header will use it
        return "[" + sv + "]"

    elif method == "model_bound_fetch":
        bound = pathway.get_bounds_as_json()
        # print "In Model bound, Now bound is", len(pathway.bound)

        t = len(pathway.reactions)
        input_params['startRows'] = '0'
        input_params['endRow'] = str(t - 1)
        input_params['totalRows'] = str(t)     # later get_header will use it
        return "[" + bound + "]"

    elif method == "model_bound_update":
        bound = pathway.get_bounds()
        key = str(input_params["pk"][0])
        # print "key is", key
        if len(key) < 5:
            new_key = 'R' + '0' * (5 - len(key)) + key
        else:
            new_key = 'R' + key
        # print new_key
        
        lb = float(input_params["l"][0].encode('ascii', 'ignore'))
        ub = float(input_params["u"][0].encode('ascii', 'ignore'))
        # print lb, ub
        bound[new_key][0] = lb
        bound[new_key][1] = ub
        r += '"pk":"' + key + '",'
        r += '"r":"' + new_key + '",'
        r += '"l":"' + str(lb) + '",'
        r += '"u":"' + str(ub) + '"'
    else:
        r = ""
        pass
    return r

""" SmartGWT uses a mechanism called "callback"
    to update data sources. It transfers a callback=xxx
    parameter to server. Server is responsible 
    to put the return JSON object as a parameter 
    to the callback function. 
    
    This decorator extracts the callback param 
    from the GET request, and encapsulate the 
    return value into the callback field.
"""
def ajax_callback(f):
    def newfn(arg):
        result = f(arg)
        if arg.GET.get('callback', False):
            payload = arg.GET['callback'] + "(" + result.__repr__() + ")"
        else:
            payload = result.__repr__()
        return HttpResponse(content = payload, status = 200, content_type = "text/html")
    return newfn
    
    
""" This decorator transforms a list of Json objects 
    into a table that starts from _startRow and ends 
    with _endRow. 
    
    SmartGWT defines datasource with lazy fetch. Pushing
    all data to the client is not necessary. 
"""
def table_response_envelope(f):
    def newfn(arg):
        tempresult = f(arg)

        startRow = int(arg.GET['_startRow'])
        endRow = int(arg.GET['_endRow'])
        totalRow = len(tempresult.array_container)

        if endRow >= totalRow:
            endRow = totalRow - 1

        response = Json("object")

        status_label = Json()
        status_label.set_label("status")
        status_indicate_label = Json()
        status_indicate_label.set_label("0")
        response.add_pair(status_label, status_indicate_label)

        startrow_label = Json()
        startrow_label.set_label("startRow")
        startrow_value = Json()
        startrow_value.set_label(str(startRow))
        response.add_pair(startrow_label, startrow_value)

        endrow_label = Json()
        endrow_label.set_label("endRow")
        endrow_value = Json()
        endrow_value.set_label(str(endRow))
        response.add_pair(endrow_label, endrow_value)

        totalrow_label = Json() # note here might be a bug that those labels are not out
        totalrow_label.set_label("totalRows")
        totalrow_value = Json()
        totalrow_value.set_label(str(totalRow))
        response.add_pair(totalrow_label, totalrow_value)

        jarray = tempresult.array_container[startRow:endRow + 1]
        newjson = Json("array")
        newjson.array_container = jarray

        data_label = Json()
        data_label.set_label("data")
        response.add_pair(data_label, newjson)

        result = Json("object")
        response_label = Json()
        response_label.set_label("response")
        result.add_pair(response_label, response)
        return result
    return newfn

""" SmartGWT defines the return format of datasource (the 
    so-called data envelop). This decorator encapsulates 
    the data into such an envelop."""
def response_envelope(f):
    def newfn(arg):
        response = Json("object")
        status_label = Json()
        status_label.set_label("status")

        status_indicate_label = Json()
        status_indicate_label.set_label("0")

        data_label = Json()
        data_label.set_label("data")
        response_label = Json()
        response_label.set_label("response")
        response.add_pair(status_label, status_indicate_label)
        response.add_pair(data_label, f(arg))
        result = Json("object")
        result.add_pair(response_label, response)
        return result
    return newfn

def generate_pathway(bac_name, collection_name = ""):
    # kegg_database = "/Users/youxu/ProgramInput/kegg_database/"
    filelist = os.listdir(kegg_database + bac_name + "/")
    pathway = PathwayNetwork(kegg_database, bac_name, filelist, collection_name)
    pathway.read_metabolisms()
    return pathway
