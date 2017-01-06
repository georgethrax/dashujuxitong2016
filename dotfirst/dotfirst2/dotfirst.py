#coding:utf-8
import numpy as np

f_dv = open('20161002.big.removed.dv2')
f_dotfirst = open('dvdotfirst.txt','w')

line0= f_dv.readline()
v0 = np.array(line0.split()).astype(float)	#取首个向量

ls_dotfirst = []
ls_dotfirst.append(1.0)

for line in f_dv:
	try:
		v = np.array(line.split()).astype(float)	#取每个向量
		sim = np.dot(v,v0) / np.sqrt(( np.dot(v,v) * np.dot(v0,v0) ))
		ls_dotfirst.append(sim)
	except:
		ls_dotfirst.append(0)	

str_ls_dotfirst = map(str,ls_dotfirst)
f_dotfirst.write('\n'.join(str_ls_dotfirst))
