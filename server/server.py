import google.appengine.ext.db as db 
import geo.geomodel 
import webapp2
from random import random
import json


from models import MyTagModel



class MainPage(webapp2.RequestHandler):
  def get(self):
    test = MyTag(location=db.GeoPt(0+random()*10, 0+random()*10),
                          created='Me')
    test.update_location();
    test.put() 
    self.response.headers['Content-Type'] = 'text/plain'
    self.response.write('Hello, webapp2 World22!')
    


class SearchService(webapp2.RequestHandler):
  def post(self):
    try:
      lat = float( self.request.get('lat') );
      lon = float( self.request.get('lon') );
      strokes = str( self.request.get('strokes') );
    except ValueError:
      self.response.write( json.dumps( {"state":"error", "result":"Bad params"} ))
      return
      
    model = MyTagModel(location=db.GeoPt(lat, lon),
                  content = strokes, 
                  created='Me')
                  
    model.update_location();
    model.put()
     
    self.response.headers['Content-Type'] = 'text/plain'
    self.response.write( json.dumps( {"state":"ok", "result":"Tagged!"} ))    


  def get(self):
    try:
      lat = float( self.request.get('lat') );
      lon = float( self.request.get('lon') );
    except ValueError:
      self.response.write( json.dumps( {"state":"error", "result":"Bad params"} ))
      return
    
      
    
    results =MyTagModel.proximity_fetch(
                    MyTagModel.all(),
                    geo.geotypes.Point(lat, lon))
    
    output = [result.toDict() for result in results]
    
    #output = []
    #
    #delta = 0.04
    #for i in range(10):
    #  output.append({"loc":[lat+random()*(delta)-(delta*0.5), lon+random()*(delta)-(delta*0.5)]});

    self.response.headers['Content-Type'] = 'text/plain'
    self.response.write( json.dumps( {"state":"ok", "result":output} ))
     



app = webapp2.WSGIApplication([('/', MainPage),("/s", SearchService)],
                              debug=True)