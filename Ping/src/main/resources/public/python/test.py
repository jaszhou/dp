# usage:   run this to convert PEP XML to JSON and then import into MongoDB


import xmltodict, json
from lxml import etree
import xml.etree.ElementTree as ET



destname = 'PEP1.json'
print(destname)

srtname = 'ENTITY.XML'
print(srtname)


f = open(destname,'w')
filename = srtname


doc = etree.parse(filename)

e = doc.findall(".//entity")

for w in e:
  o = xmltodict.parse(etree.tostring(w))
  print(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write('\n')
f.close()



  

