import google.appengine.ext.db as db 
import geo.geomodel 
import webapp2
from random import random
import json


class MyTagModel(geo.geomodel.GeoModel):
  created = db.StringProperty()
  content = db.StringProperty(multiline=True)
  date = db.DateTimeProperty(auto_now_add=True)
  
  def toDict(self):
    prop = {}
    prop["id"]= self.key().id()
    prop["loc"] = [self.location.lat,self.location.lon]
    return prop 