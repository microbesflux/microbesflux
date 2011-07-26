class JsonOperationTest(TestCase):
    def setUp(self):
        self.kegg_database = kegg_database
        self.bacname = "det"
        filelist = os.listdir(self.kegg_database + self.bacname + "/")
        self.pathway = PathwayNetwork(self.kegg_database, self.bacname, filelist)
        self.pathway.read_metabolisms()
        self.input_params = {}
        self.input_params['collection_name'] = "demo"

    def tear_down(self):
        self.pathway = None
        self.input_params = None

    def test_pathway_info(self):
        print "\nTest     | GetJSON         | pathway_info\t",
        expected = """[{"name":"Name of the pathway","value":"det"},{"name":"Number of all genes/orthologs","value":"2114"},{"name":"Number of annotated genes/orthologs","value":"254"},{"name":"Number of all pathways","value":"697"},{"name":"Number of active pathways","value":"377"}]"""
        result = new_get_json("pathway_info", self.input_params, self.pathway)
        self.assertEquals(result.__repr__(), expected)

    def test_pathway_fetch(self):
        print "\nTest     | GetJSON         | pathway_fetch\t",
        expected = """{"pk":"00451","reactionid":"R00451","reactants":"1 C00047","products":"1 C00680","arrow":"1","ko":false,"pathway":"det01100"}"""
        # expected = """{"reactants":"1 C00024","ko":false,"products":"1 C00022","arrow":"1","reactionid":"R01196","pk":"01196","pathway":"det00010"}"""
        result = new_get_json("pathway_fetch", self.input_params, self.pathway).__repr__()
        self.assertNotEqual(result.__repr__().find(expected), -1)
    
    def test_pathway_add(self):
        print "\nTest     | GetJSON         | pathway_add\t",
        self.input_params["ko"] = True
        self.input_params["reactants"] = "Rtest"
        self.input_params["products"] = "Ptest"
        self.input_params['arrow'] = "1"
        self.input_params["pathway"] = "det01100"
        r = new_get_json("pathway_add", self.input_params, self.pathway).__repr__()
        expected = """{"pk":"100001","reactionid":"R100001","ko":true,"reactants":"Rtest","arrow":"1","products":"Ptest","pathway":"det01100"}"""
        self.assertEquals(r, expected)
    
    def test_obj_fetch(self):
        print "\nTest     | GetJSON         | obj_fetch\t",
        r = new_get_json("user_obj_fetch", self.input_params, self.pathway).__repr__()        
        self.assertNotEqual(r.find("""{"pk":"01542","r":"R01542","w":"1"},"""), -1)
    
    def test_obj_update(self):
        print "\nTest     | GetJSON         | obj_update\t",
        self.input_params["w"] = "0.1"
        self.input_params["pk"] = "1542"
        r = new_get_json("user_obj_update", self.input_params, self.pathway).__repr__()
        expected = '{"pk":"1542","r":"R01542","w":"0.1"}'
        self.assertEquals(expected, r)
