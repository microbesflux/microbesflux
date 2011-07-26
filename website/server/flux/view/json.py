# Put the python to json code here
#  http://www.json.org/
# Type polymophism is for static typing language
class Json:
    def __init__(self, type = "object"):
        self.type = type
        self.object_container = {}
        self.array_container = []
        self.object_order_list = []
        self.value = None
        pass

    def add_item(self, j):
        assert self.type == "array"
        self.array_container.append(j)

    def set_value(self, value):
        self.type = "value"
        self.value = value
        if self.value == "":  # special treatment for empty string
            self.value = '""'

    def set_label(self, label):
        self.type = "label"
        self.value = label
        
    def add_pair(self, key, value):
        self.object_order_list.append(key)
        self.object_container[key] = value

    def _value_repr(self, v):
        if type(v) == type(True):
            if v: return 'true'
            return 'false'
        elif type(v) == type(""):
            return self._deco_string(v)
        elif type(v) == type(1.2) or type(v) == type(0):
            return str(v)
        elif type(v) == type(self):
            return v.__repr__()
        elif type(v) == type(u""):  # unicode string
            return self._deco_string(str(v))
        else:
            return '""'

    def _deco_string(self, s):
        return '"' + s + '"'
    
    def _deco_key(self, s):
        if type(s) == type(""):
            return '"' + s + '"'
        elif type(s) == type(self):
            return s.value

    def __repr__(self):
        if self.type == "object":
            r = '{'
            r += ','.join([self._deco_key(key) + ':' + self._value_repr(self.object_container[key])
                           for key in self.object_order_list])
            return r + '}'

        elif self.type == "array":
            r = '['
            r += ','.join([items.__repr__() for items in self.array_container])
            return r + ']'

        elif self.type == "value":
            return self._value_repr(self.value)  # string, number, true, false and null

        elif self.type == "label":
            return self.value
        else:
            return ""
