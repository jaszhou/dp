# usage:   run this to convert PEP XML to JSON and then import into MongoDB


import xmltodict, json
from lxml import etree



destname = 'UN.json'
print(destname)

srtname = 'UN_consolidated.xml'
print(srtname)


f = open(destname,'w')

filename = srtname



tree = etree.parse(filename)

root = tree.getroot()

#print(root.tag)

for child in root:
     print(child.tag)
     for grand in child:
         print(grand.tag)

     
     
#root=doc.getroot()

#o = xmltodict.parse(s)
#print(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))

#namespaces = {'': 'http://tempuri.org/sdnList.xsd'}

e = root.findall('.//INDIVIDUAL')

for w in e:
  o = xmltodict.parse(etree.tostring(w))
  print(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write('\n')

e = root.findall('.//ENTITY')

for w in e:
  o = xmltodict.parse(etree.tostring(w))
  print(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write(json.dumps(o, sort_keys=True,indent=4, separators=(',', ': ')))
  f.write('\n')


f.close()



  

