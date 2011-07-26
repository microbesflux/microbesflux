from django.contrib.auth.models import User
from django.db import models
from constants import baseurl

""" This Profile model describes a user's optimization problem
and pathway target
"""

class Profile(models.Model):
    name = models.CharField(max_length = 30)
    user = models.ForeignKey(User)
    diskfile    = models.FileField(max_length=100, upload_to= baseurl + "temp")
    status      = models.CharField(max_length= 30)
    model_type  = models.CharField(max_length= 10)
    result_url  = models.URLField(max_length=200)
    submitted   = models.BooleanField()
    submitted_date = models.CharField(max_length=30)
    dfba_file   = models.FileField(max_length=100, upload_to=baseurl + "temp")

class Compound(models.Model):
    name      = models.CharField(max_length = 10)
    alias     = models.CharField(max_length = 10)
    long_name = models.CharField(max_length =140)
    
    def __unicode__(self):
        return self.name + " A:" + self.alias + " L:" + self.long_name

class Task(models.Model):
    task_id   =  models.AutoField(primary_key = True)
    task_type =  models.CharField(max_length = 10)
    main_file =  models.CharField(max_length = 50)
    additional_file =  models.CharField(max_length = 50)
    email     = models.EmailField(max_length = 75)
    status    = models.CharField(max_length = 10)
    def __unicode__(self):
        return str(self.task_id) + "," + self.main_file + "," + self.task_type + "," + self.email + "," + self.status + "," + self.additional_file
    