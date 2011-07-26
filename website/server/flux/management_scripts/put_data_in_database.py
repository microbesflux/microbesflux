from django.core.management import setup_environ
import sys
sys.path.append('/research-www/engineering/tanglab/')

from flux import settings
setup_environ(settings)

from flux.models import Compound

import cPickle
from flux.constants import baseurl
from flux.parser.helper import *


def put_data_in():
    data = read_pickle(baseurl + 'kegg/compound_lite.pk')
    alias = data[0]
    longname = data[1]

    for key in alias:
        a = alias[key]
        l = longname.get(key, "")
        c = Compound(name = key, alias = a, long_name = l)
        c.save()

def query(n):
    c = Compound.objects.get(name__exact=n)
    print c

def long_name_query(n):
    c = Compound.objects.get(long_name__exact=n)
    print c

if __name__ == "__main__":
    query("C15064")
    long_name_query("Ethylmalonyl-CoA")
    long_name_query("H2O")
    
    