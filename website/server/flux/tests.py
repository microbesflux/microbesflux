#!/usr/bin/env python
import unittest
import os, sys

from django.test import TestCase
from django.test.client import Client
from parser.keggpathway import PathwayNetwork
from view.foundations import *
from view.json import Json
from constants import kegg_database
from django.test.client import Client
from parser.reaction import Reaction

# TODO:
""" Let Xueyang organize the model file, so that I can do the final round of testing. """
class PathwayTest(TestCase):
    def setUp(self):
        self.kegg_database = kegg_database
        self.bacname = "det"
        filelist = os.listdir(self.kegg_database + self.bacname + "/")
        self.pathway = PathwayNetwork(self.kegg_database, self.bacname, filelist)
        self.pathway.read_metabolisms()
    
    def tearDown(self):
        self.pathway = None
    
    def test_create(self):
        print "\nTest     | PathwayNetwork  | metabolism\t",
        self.assertTrue(self.pathway.total_gene > 0)
    
    def test_reactions(self):
        print "\nTest     | PathwayNetwork  | reactions\t",
        self.assertEqual(len(self.pathway.reactions), 364)
    
    def test_reactions_fetch(self):
        print "\nTest     | PathwayNetwork  | fetch reactions\t",
        self.assertTrue(self.pathway.reactions['R08774'] != None)
    
    def test_pathway_add(self):
        print "\nTest     | PathwayNetwork  | pathway_add\t",
        self.pathway.add_pathway('R19999', False, "TestL", "1", "TestR", "BIOMASS")
        self.assertEqual(self.pathway.reactions['R19999'].ko, False)
    
    def test_pathway_update(self):
        print "\nTest     | PathwayNetwork  | pathway_update\t",
        self.pathway.update_pathway('R01229', True, "TestL", "1", "TestR", "det01100")
        self.assertTrue(self.pathway.reactions['R01229'].ko)
        self.assertTrue(self.pathway.reactions['R01229'].name, "R01229")
    

class ReactionTest(TestCase):
    def setUp(self):
        pass
    
    def tearDown(self):
        pass
    
    def test_reaction(self):
        print "\nTest     | ReactionObject  | GetJson\t",
        r = Reaction("Test")
        r.reversible = False
        r.products = ["A", "B"]
        r.substrates = ["C", "D"]
        r.stoichiometry = {"A":1, "B":2, "C":3, "D":4}
        r.longname_map = {"A":"LongA", "B":"LongB", "C":"LongC", "D":"LongD"}
        r.ko = False
        expected = """{"pk":"est","reactionid":"Test","reactants":"3 LongC + 4 LongD","products":"1 LongA + 2 LongB","arrow":"===>","ko":false}"""
        # expected = """{"ko":false,"reactants":"3 C + 4 D","products":"1 A + 2 B","arrow":"0","reactionid":"Test","pk":"est"}"""
        result = r.getJson().__repr__()
        self.assertEquals(expected, result)

class JsonTest(TestCase):
    def setUp(self):
        self.json = Json()
    
    def test_json_bool(self):
        print "\nTest     | JsonObject      | bool as json value\t",
        self.json.set_value(True)
        self.assertEquals("true", repr(self.json))
        self.json.set_value(False)
        self.assertEquals("false", repr(self.json))
    
    def test_json_number(self):
        print "\nTest     | JsonObject      | number as json value\t",
        self.json.set_value(2.5)
        self.assertEquals("2.5", repr(self.json))
    
    def test_json_obj(self):
        print "\nTest     | JsonObject      | object pairs\t",
        self.json.type = "object"
        self.json.add_pair("eric", 123)
        self.json.add_pair("gretchen", True)
        self.json.add_pair("random", "random")
        self.assertEquals ("""{"eric":123,"gretchen":true,"random":"random"}""", repr(self.json))
    


class UserViewTest(TestCase):
    def setUp(self):
        self.client = Client()
    
    def test_add(self):
        print "\nTest     | UserView        | /user/add/\t",
        response = self.client.post('/user/add/', {'username': 'eric', 'password': '123'})
        self.assertEquals(200, response.status_code)
        self.assertEqual("Successfully added", response.content)
    
    def test_login(self):
        print "\nTest     | UserView        | /user/login/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Successfully Login") != -1)
    
    def test_logout(self):
        print "\nTest     | UserView        | /user/logout/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/user/logout/')
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Logout successfully") != 1)
        response = self.client.get('/user/summary/')
        self.assertRedirects(response, 'http://testserver/?next=/user/summary/')
    
    def test_summary(self):
        return  # skipped for now
        print "\nTest     | UserView        | /user/summary/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/collection/create/', {'collection_name': 'demo1', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/pathway/fetch/',{"_startRow":0, "_endRow":"100"})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/model/sv/fetch/', {"_startRow":0, "_endRow":"100"})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/model/bound/fetch/', {"_startRow":0, "_endRow":"100"})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":"100"})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/collection/save/', {})
        self.assertTrue(response.content.find("Collection saved") != -1)
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/collection/select/', {'collection_name': 'demo1'})
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/model/optimization/')
        self.assertEquals(response.status_code, 200)
        response = self.client.get('/user/summary/?callback=test')
        self.assertEquals(response.status_code, 200)
    
    def test_change_pwd(self):
        print "\nTest     | UserView        | /user/password/change/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.post('/user/password/change/', {'newpassword':'1234'})
        response = self.client.get('/user/logout/')
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '1234'})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Successfully Login") != -1)
    
    def test_retrieve_pwd(self):
        print "\nTest     | UserView        | /user/password/retrieve\t",
        self.assertTrue(True)
    


class CollectionViewTest(TestCase):
    def SetUp(self):
        self.client = Client()
    
    def test_collection_create(self):
        print "\nTest     | CollectionView  | /collection/create/\t",
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Collection created") != -1)
    
    def test_collection_duplicated_create(self):
        print "\nTest     | CollectionView  | /collection/create/ duplicate names\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Collection created") != -1)
        
        response = self.client.get('/collection/save/', {})
        self.assertTrue(response.content.find("Collection saved") != -1)
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        # print "return content is", response.content
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Collection name") != -1)
    
    def test_collection_info(self):
        print "\nTest     | CollectionView  | /pathway/stat/\t",
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes','email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/stat/', {'collection_name': 'demo'})
        self.assertEquals(response.status_code, 200)
        expected = """[{"name":"Name of the pathway","value":"demo"},{"name":"Name of the organism","value":"det"},{"name":"Number of all genes/orthologs","value":"2417"},{"name":"Number of annotated genes/orthologs","value":"443"},{"name":"Number of all pathways","value":"364"},{"name":"Number of active pathways","value":"363"}]"""
        self.assertEquals(response.content, expected)
    
    def test_collection_save(self):
        print "\nTest     | CollectionView  | /collection/save/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes','email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Collection saved") != -1)
    
    def test_collection_select(self):
        print "\nTest     | CollectionView  | /collection/select/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        response = self.client.get('/collection/select/', {'collection_name': 'demo'})
        self.assertEquals(response.status_code, 200)
        self.assertTrue(response.content.find("Collection selected ") != -1)
    


class PathwayViewTest(TestCase):
    # fixtures = ['fixture1.json', 'cdata.json']
    def SetUp(self):
        self.client = Client()
    
    def test_pathway_add(self):
        # TODO fixture for models
        print "\nTest     | PathwayView     | /pathway/add/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        response = self.client.get('/collection/select/', {'collection_name': 'demo'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'TEST', 'ko':'false'})
        # print response.content
        expected = """{response:{status:0,data:{"pk":"100001","reactionid":"R100001","ko":"false","reactants":"TEST","arrow":"0","products":"BIOMASS","pathway":"BIOMASS"}}}"""
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Inflow', 'products':'C11111', 'reactants':'C22222+C11122', 'ko':'false'})
        print response.content
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Outflow', 'products':'C22222', 'reactants':'D2222', 'ko':'false'})
        print response.content
        response = self.client.get('/pathway/add/', {'arrow': '1', 'pathway': 'Heterologous Pathways', 'products':'C22223', 'reactants':'D2223', 'ko':'true'})
        print response.content
    
    def test_pathway_update(self):
        print "\nTest     | PathwayView     | /pathway/update/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        response = self.client.get('/collection/select/', {'collection_name': 'demo'})
        response = self.client.get('/pathway/update/', {'arrow': '1', 'pathway': 'det00670', 'products':'2 C00234', 'reactants':'1 C00445', 'ko':'true', 'pk':'1655', 'reactionid':'R01655'})
        expected = """{response:{status:0,data:{"pk":"1655","reactionid":"R01655","ko":"true","reactants":"1 C00445","arrow":"===>","products":"2 C00234","pathway":"det00670"}}}"""
        self.assertEquals(response.content, expected)
    
    def test_pathway_fetch(self):
        print "\nTest     | PathwayView     | /pathway/fetch/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(response.status_code, 200)
        expected = """{"reactants":"1 C00092","products":"1 C04006","arrow":"1","reactionid":"R07324","pk":"07324","pathway":"det01100"}"""
        self.assertTrue(response.content.find(expected) != 1)
        pass
    
    def test_pathway_add_fetch(self):
        print "\nTest     | PathwayView     | /pathway/add and fetch\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        response = self.client.get('/collection/select/', {'collection_name': 'demo'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'TEST', 'ko':'false'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(response.status_code, 200)
        pass

    # Usually skip this test because it is too time consuming.
    def test_pathway_add_check(self):
        print "\nTest     | PathwayView     | /pathway/check \t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/collection/save/', {})
        response = self.client.get('/collection/select/', {'collection_name': 'demo'})
        response = self.client.get('/pathway/check/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'H2O', 'reactants':'Ethylmalonyl-CoA', 'ko':'false'})
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, "Valid")
        
class ModelViewObjective(TestCase):
    def SetUp(self):
        self.client = Client()
    
    def test_objective_fetch(self):
        print "\nTest     | ModelViewObjective   | /model/objective/fetch/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        # print response.content
        expected1 =  """{response:{status:0,startRow:0,endRow:363,totalRows:364,data:"""
        expected2 = """{"pk":"00425","r":"R00425","w":"1"}"""
        self.assertTrue(response.content.find(expected1) != -1)
        self.assertTrue(response.content.find(expected2) != -1)
    
    def test_objective_update(self):
        print "\nTest     | ModelViewObjective   | /model/objective/update/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        response = self.client.get('/model/objective/update/', {"pk":"299", "r":"R00299", "w":"0.4"})
        expected = """{response:{status:0,data:{"pk":"299","r":"R00299","w":"0.4"}}}"""
        self.assertEquals(expected, response.content)

"""
        (r'^task/list/', task_list),
        (r'^task/add/', task_add),
        (r'^task/remove/', task_remove),
        (r'^task/mark/', task_mark),
        (r'^task/mail/', task_mail),
"""

class TaskTest(TestCase):
    
    def test_task_add(self):
        print "\nTest     | TaskView   | /task/add/\t",
        response = self.client.get('/task/add/', {"type":"fba", "task":"test", "email":"youxu@wustl.edu", "file":"NULL"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/task/add/', {"type":"dfba", "task":"test", "email":"youxu@wustl.edu", "file":"NULL"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/task/add/', {"type":"dfba", "task":"test", "email":"xueyang@wustl.edu", "file":"one_more"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/task/add/', {"type":"svg", "task":"test", "email":"youxu@wustl.edu", "file":"NULL"})
        self.assertEquals(response.status_code, 200)

        response = self.client.get('/task/list/')
        expected = """1,test,fba,youxu@wustl.edu,TODO,NULL\n2,test,dfba,youxu@wustl.edu,TODO,NULL\n3,test,dfba,xueyang@wustl.edu,TODO,one_more\n4,test,svg,youxu@wustl.edu,TODO,NULL"""
        self.assertEquals(response.content, expected)
    
    def test_task_remove(self):
        print "\nTest     | TaskView   | /task/remove/\t",
        response = self.client.get('/task/add/', {"type":"t1", "task":"test", "email":"youxu@wustl.edu", "file":"NULL"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/task/add/', {"type":"t2", "task":"ttest2", "email":"youxu@wustl.edu", "file":"NULL"})
        response = self.client.get('/task/list/')
        self.assertEquals(response.status_code, 200)        
        
        tid = response.content[0]
        response = self.client.get('/task/remove/', {"tid":tid})
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, "Task Removed")
        
        response = self.client.get('/task/list/')
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, str(int(tid)+1) +",ttest2,t2,youxu@wustl.edu,TODO,NULL")
        
    def test_task_mark(self):
        print "\nTest     | TaskView   | /task/mark/\t",
        response = self.client.get('/task/add/', {"type":"type1", "task":"test", "email":"youxu@wustl.edu", "file":"NULL"})
        response = self.client.get('/task/list/')
        self.assertEquals(response.status_code, 200)
        tid = response.content[0]
        
        response = self.client.get('/task/mark/', {"tid":tid})
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, "Task Marked")
        response = self.client.get('/task/list/')
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, tid + ",test,type1,youxu@wustl.edu,Enqueue,NULL")
        
    def test_task_mail(self):
        print "\nTest     | TaskView   | /task/mail/\t",
        response = self.client.get('/task/add/', {"type":"dfba", "task":"demo.ampl", "email":"youxu@wustl.edu", "file":"NULL"})
        response = self.client.get('/task/list/')
        self.assertEquals(response.status_code, 200)
        tid = response.content[0]
        response = self.client.get('/task/mail/', {"tid":tid})
        self.assertEquals(response.status_code, 200)
        self.assertEquals(response.content, " Mail sent ")
        
class ModelViewBoundTest(TestCase):
    
    def SetUp(self):
        self.client = Client()
        
    def test_bound_update(self):
        print "\nTest     | ModelViewObjective   | /model/objective/update/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/model/bound/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)
        # print response.content
        response = self.client.get('/model/bound/update/', {"pk":"01354", "r":"R01354", "l":"4.9", "u":"5.5"})
        self.assertEquals(response.status_code, 200)

class ModelViewOptimization(TestCase):
    
    def SetUp(self):
        self.client = Client()
        
    def test_add_pathway_should_appear(self):
        print "\nTest     | ModelViewObjective   | /add pathway verify\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Heterologous Pathways', 'products':'ADP', 'reactants':'ATP', 'ko':'false'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Heterologous Pathways', 'products':'ATP', 'reactants':'ADP', 'ko':'false'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)
        print response.content
        # assert()
    
    def test_fba_job_submit0(self):
        
        print "\nTest     | ModelViewObjective   | /optimization/ for user\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})

        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})

        response = self.client.get('/model/bound/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)

        response = self.client.get('/model/sv/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get("/model/optimization/?obj_type=0")#user
        print response.content

    def test_fba_job_submit1(self):
        
        print "\nTest     | ModelViewObjective   | /optimization for biomass\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})

        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/model/objective/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})

        response = self.client.get('/model/bound/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)

        response = self.client.get('/model/sv/fetch/', {"_startRow":0, "_endRow":1000, "callback":"eric"})
        self.assertEquals(response.status_code, 200)
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'899 C00200', 'ko':'false'})
        
        response = self.client.get("/model/optimization/?obj_type=1")# biomass
        print response.content

class UploadTest(TestCase):
    def test_upload(self):
        print "\nTest     | UploadTest   | /model/upload/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'1 ATP', 'ko':'false'})
        expected = """{response:{status:0,data:{"pk":"100001","reactionid":"R100001","ko":"false","reactants":"1 ATP","arrow":"0","products":"BIOMASS","pathway":"BIOMASS"}}}"""
        # print response.content
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Inflow', 'products':'C01111', 'reactants':'C02222', 'ko':'false'})
        # print response.content
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Outflow', 'products':'C02222', 'reactants':'C02223', 'ko':'false'})
        # print response.content
        
        f = open("toupload.txt")
        response = self.client.post('/model/upload/', {'uploadFormElement': f} )
        f.close() 
        # print response.content
        self.assertTrue(response.content.find("Successfully Uploaded") != -1)

class TestSbml(TestCase):
    def test_sbml(self):
        print "\nTest     | TestSbml   | /model/sbml/\t",
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        response = self.client.get('/pathway/update/', {'arrow': '1', 'pathway': 'det00670', 'products':'2 C00234', 'reactants':'1 C00445', 'ko':'true', 'pk':'1655', 'reactionid':'R01655'})
        response = self.client.get('/pathway/add/', {'arrow': '1', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'1 ATP', 'ko':'true'})
        response = self.client.get('/model/sbml/')
        print response

class TestDFBA(TestCase):
    def test_upload(self):
        pass
        
    def test_dfba(self):
        print "\nTest     | TestDFBA   | /model/dfba/\t",
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'BIOMASS', 'products':'BIOMASS', 'reactants':'1 ATP', 'ko':'false'})
        expected = """{response:{status:0,data:{"pk":"100001","reactionid":"R100001","ko":"false","reactants":"1 ATP","arrow":"0","products":"BIOMASS","pathway":"BIOMASS"}}}"""
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Inflow', 'products':'C01111', 'reactants':'C02222', 'ko':'false'})
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Outflow', 'products':'C02222', 'reactants':'C02223', 'ko':'false'})
        f = open("toupload.txt")
        response = self.client.post('/model/upload/', {'uploadFormElement': f} )
        f.close() 
        self.assertTrue(response.content.find("Successfully Uploaded") != -1)
        response = self.client.get('/model/dfba/', {'provided_email':'xu.mathena@gmail.com',})
        print response

"""
class TestSvg(TestCase):
    def test_svg(self):
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
	response = self.client.get('/model/svg/')
	print response
	
class TestOpt(TestCase):
    def test_opt(self):
        response = self.client.get('/collection/create/', {'collection_name': 'newdemo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        response = self.client.get('/model/optimization/')
        # response = self.client.get('/task/mail/', {'tid':'2011-03-30-14:19:24.919410'})
        
        self.assertEquals(response.status_code, 200)
        print response
"""
from view.collection_view import get_collection_from_disk
from view.collection_view import save_collection_to_disk

from django.contrib.auth.models import User

class CollectionLogicTest(TestCase):
    def test_collection_save_load(self):
        obj = [1, 2, 3]
        u = User.objects.get(email = "t@t.com")
        save_collection_to_disk(u, "test", obj)
        o = get_collection_from_disk(u, "test")
        self.assertEquals(obj, o)

class BoundKnockOutTest(TestCase):
    def test_back_and_forth_switch(self):
        print "\nTest     | TestBoundKnockout   | /misc/bugs/\t",
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})        
        response = self.client.get('/model/objective/fetch/',  {'_startRow':0, '_endRow':1000})
        self.assertEquals(200, response.status_code)
        response = self.client.get('/model/bound/fetch/',  {'_startRow':0, '_endRow':1000})
        
        # print response.content
        
        response = self.client.get('/pathway/update/', {'arrow': '1', 'pathway': 'det00670', 'products':'2 C00234', 'reactants':'1 C00445', 'ko':'true', 'pk':'386', 'reactionid':'R01867'})
        # {"pk":386,"r":"R01867","l":"-100.0","u":"100.0"},
        response = self.client.get('/model/bound/fetch/',  {'_startRow':0, '_endRow':1000})
        print response.content
    
    
class OtherBugTest(TestCase):
    def test_back_and_forth_switch(self):
        print "\nTest     | TestBug   | /misc/bugs/\t",
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'det D.ethenogenes', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})        
        response = self.client.get('/model/objective/fetch/',  {'_startRow':0, '_endRow':1000})
        self.assertEquals(200, response.status_code)
        response = self.client.get('/model/bound/fetch/',  {'_startRow':0, '_endRow':1000})
        self.assertEquals(200, response.status_code)
        response = self.client.get('/model/sv/fetch/',  {'_startRow':0, '_endRow':1000})
        self.assertEquals(200, response.status_code)
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        self.assertEquals(200, response.status_code)
        
        
class TestToy(TestCase):
    def test_toy_constract(self):
        response = self.client.get('/collection/create/', {'collection_name': 'demo', 'bacteria': 'TOY A.Toy.Example', 'email':'xu.mathena@gmail.com'})
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        print response.content
    
""" This tests the TOY example """
class TestToyOptimization(TestCase):
    def test_toy_dfba(self):
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        
        response = self.client.get('/collection/create/', {'collection_name': 'toyd', 'bacteria': 'TOY A.Toy.Example', 'email':'xu.mathena@gmail.com'})
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Inflow', 'products':'c_g6p', 'reactants':'1 c_glucose', 'ko':'false'})
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Outflow', 'products':'c_Acetate', 'reactants':'1 c_accoa', 'ko':'false'})
        
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        print response.content
        
        f = open("toy/toy_dfba_upload.txt")
        response = self.client.post('/model/upload/', {'uploadFormElement': f} )
        f.close() 
        self.assertTrue(response.content.find("Successfully Uploaded") != -1)
        
        response = self.client.get('/model/dfba/?obj_type=0')
        print response.content
    
    def test_toy_fba(self):
        response = self.client.post('/user/login/', {'username': 't@t.com', 'password': '123'})
        
        response = self.client.get('/collection/create/', {'collection_name': 'toyf', 'bacteria': 'TOY A.Toy.Example', 'email':'xu.mathena@gmail.com'})
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Inflow', 'products':'c_g6p', 'reactants':'1 c_glucose', 'ko':'false'})
        
        response = self.client.get('/pathway/add/', {'arrow': '0', 'pathway': 'Outflow', 'products':'c_Acetate', 'reactants':'1 c_accoa', 'ko':'false'})
        
        response = self.client.get('/pathway/fetch/', {'_startRow':0, '_endRow':1000})
        print response.content
        
        response = self.client.get('/model/optimization/?obj_type=0')
        print response.content
        
# response = self.client.get('/task/list/')
""" This tests the report composition module """
class TestReportGenerate(TestCase):
    def test_report_generate(self):
        ## We already have something called Demoxx
        response = self.client.get('/task/add/', {"type":"fba", "task":"test", "email":"xu.mathena@gmail.com", "file":"NULL"})
        self.assertEquals(response.status_code, 200) 
        response = self.client.get('/task/mail/', {"tid":1})
        print response.content
    