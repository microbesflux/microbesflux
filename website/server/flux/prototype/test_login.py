from django.test.client import Client
c = Client()
response = c.post('/login/', {'username': 'xuyou@wustl.edu', 'password': '123'})
print response.status_code
response = c.get('/customer/details/')
print response.content