def process(m, q, request):
    """ A facet method to process all requests """
    pathway = request.session["collection"]
    json = new_get_json(m, q, pathway)
    #print "JSON is", json
    # print "q in process", q
    payload = get_header(m, q) + json + get_tail(m, q)
    s = get_callback(q)
    if s:
        # print type(s)
        # print s
        # print type(payload)
        con = s + "( " + payload + " )"
    else:
        con = payload
    if (save_required(m)):
        # print "Going to save in request ", m
        request.session.save()
    return HttpResponse(content = con, status = 200, content_type = "text/html")

def get_header(method, param):
    if method in [ "user_obj_update", "model_bound_update"]:
        return "{ response: { status : 0, data : { "
    elif method in ["pathway_add", "pathway_update", ]:
        return "{ response: { status : 0, data : "
    elif method in ["pathway_fetch", "user_obj_fetch", "optfetch", "model_sv", "model_bound_fetch"]:
        return "{ response: { status : 0, startRow:" + param['startRows'] + ", endRow:" + param['endRow'] + ", totalRows:" + param['totalRows'] + ", data :"
    else:
        return ""

def get_tail(method, param):
    if   method in [ "user_obj_update", "model_bound_update"]:
        return "}   }   }"
    elif method in ["pathway_add", "pathway_update", ]:    # later will all use JSON format, move here
        return "}  }"
    elif method in ["pathway_fetch", "user_obj_fetch", "optfetch", "model_sv", "model_bound_fetch"]:
        return "}    }"
    else:
        return ""

def get_callback(req):
    for key in req:
        if key == u"callback":
             r = req[key]
             if type(r) == type([]):
                 return str(r[0])
             else:
                 return r
    return False
    
    

    # TODO: remove this method
    def _dict_to_json(self, json):
        r = ''
        for key in json.keys():
            if type(json[key]) == type('a') or type(json[key]) == type(u'a'):
                r += '"' + key + '":"'
                r += json[key] + '",\n'
            elif type(json[key]) == type(False):
                r += '"' + key + '":'
                r += str(json[key]).lower() + ',\n'
        return r


