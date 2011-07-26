import cPickle
import os.path
from flux.models import Profile

from django.contrib.auth.decorators import login_required
from django.http import HttpResponse
from django.core.files import File
from django.core.files.base import ContentFile
from view.foundations import *

def get_collection_list(u):
    p = Profile.objects.filter(user = u)
    result = [x.name for x in p]
    return result

def rename_collection(u, collection_name, new_name):
    try:
        p = Profile.objects.get(name = collection_name, user = u)
    except Profile.DoesNotExist:
        return
    p.name = newname
    p.save()

def get_collection_from_disk(u, collection_name):
    try:
        p = Profile.objects.get(name = collection_name, user = u)
    except Profile.DoesNotExist:
        return False
    p.diskfile.open(mode = 'rb')
    collection_obj = cPickle.load(p.diskfile)
    p.diskfile.close()
    return collection_obj

def save_collection_to_disk(u, collection_name, obj):
    try:
        p = Profile.objects.get(name = collection_name, user = u)
    except Profile.DoesNotExist:
        p = Profile()
        p.user = u
        p.name = collection_name
        p.save()
    # print "user", p.user
    # print "to save", p.name
    file_content = ContentFile(cPickle.dumps(obj))
    p.diskfile.save(p.name, file_content, save = True)

@login_required
def collection_save(request):  # actually SaveAs, the same as save
    collection = request.session['collection']
    collection_name = request.session['collection_name']
    save_collection_to_disk(request.user, collection_name, collection)
    return HttpResponse(content = """Collection saved """, status = 200, content_type = "text/html")


def collection_create(request):
    """ Give a name of this collection, save it somewhere """
    collection_name = request.GET['collection_name']
    bac_name = request.GET['bacteria'].split()[0]
    email = request.GET['email']
    profile = None
    if request.user and request.user.is_authenticated():
        u = request.user
        try:
            profile = Profile.objects.get(name = collection_name, user = u)
        except Profile.DoesNotExist:
            pass
    if not profile:
        if bac_name == "TOY":
            import cPickle
            f = open("/research-www/engineering/tanglab/flux/toy/toy_pathway.pickle", "rb")
            p = cPickle.load(f)
        else:
            p = generate_pathway(bac_name, collection_name)
        request.session['collection'] = p
        request.session['collection_name'] = collection_name
        request.session['email'] = email
        request.session['provided_email'] = email
        request.session.save()
        return HttpResponse(content = """Collection created """, status = 200, content_type = "text/html")
    else:
        return HttpResponse(content = """Collection name is already in use """, status = 200, content_type = "text/html")

@login_required
def collection_rename(request):
    """ User will rename a collection """
    collection_name = request.session['collection_name']
    new_name = request.GET['new_name']
    rename_collection(request.user, collection_name, new_name)
    request.session['collection_name'] = new_name
    return HttpResponse(content = """ Collection renamed. """, status = 200, content_type = "text/html")


@login_required
def collection_saveas(request):
    """ User will rename a collection """
    collection_name = request.session['collection_name']
    new_name = request.GET['new_name']
    uname = request.user.username
    collection = request.session['collection']
    save_collection_to_disk(request.user, new_name, collection)
    request.session['collection_name'] = new_name
    return HttpResponse(content = """ Collection renamed. """, status = 200, content_type = "text/html")

# This is deprecated 
def collection_info(request):
    collection_name = request.session['collection_name']
    pathway_network = request.session['collection']
    t = map(str, pathway_network.statistics())
    r1 = Json("object")
    r1.add_pair("name" , "Name of the pathway")
    r1.add_pair("value", collection_name)

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

    result = Json("array")
    result.add_item(r1)
    result.add_item(r2)
    result.add_item(r3)
    result.add_item(r4)
    result.add_item(r5)
    return HttpResponse(content = result.__repr__(), status = 200, content_type = "text/html")


@login_required
def collection_select(request):
    """ Pick a particular collection "KeggPathway"""
    collection_name = request.GET['collection_name']
    # print "Going to select collection", collection_name
    collection = get_collection_from_disk(request.user, collection_name)
    if collection:
        request.session['collection'] = collection
        request.session['collection_name'] = collection_name
        return HttpResponse(content = """Collection selected """, status = 200, content_type = "text/html")
    else:
        return HttpResponse(content = """Can not find designated collection """, status = 200, content_type = "text/html")

@login_required
def collection_summary(request):
    u = request.user
    clist = get_collection_list(u)
    result = " ".join(clist)
    return HttpResponse(content = result, status = 200, content_type = "text/html")

