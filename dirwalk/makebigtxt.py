#coding=utf-8
#makebigtxt.py

import os
rootpath = "/data/20161010/20161002/"
bigfilepath = "20161002.big"
indexfilepath = "20161002.index"


f_index = open(indexfilepath,"w")
for root,dirs,files in os.walk(rootpath):
	for file in files:
		f_index.write("%s\n" %(os.path.join(root,file)))
f_index.close()

f_big = open(bigfilepath,"w")
for root,dirs,files in os.walk(rootpath):
	for file in files:
		f_small = open(os.path.join(root,file),"r")
		str_small = f_small.read()
		f_small.close()
		f_big.write(str_small.replace('\r',' ').replace('\n',' ')+"\n")
f_big.close()