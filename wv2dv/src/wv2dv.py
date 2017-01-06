#coding:utf-8
import sys
import numpy as np
# usage: python wv2dv.py vectors-20161002.txt 20161002.big.removed 20161002.big.removed.dv
if len(sys.argv)!=4:
	exit()
f_wv = open(sys.argv[1])	#vectors-20161002.txt
f_d = open(sys.argv[2])		#/data/健康.big
f_dv = open(sys.argv[3],'w')	#健康.big.dv

num_words,num_dim = f_wv.readline().split()
num_words = int(num_words)
num_dim = int(num_dim)

list_wv = f_wv.readlines()	#5秒钟即可读取完2GB磁盘文件
f_wv.close()
dict_wv = {}
for line in list_wv:
	li_wv = line.split()
	word = li_wv[0]
	vector = map(float,li_wv[1:])
	dict_wv[word]=np.array(vector) 	#dictionary 比 list 查询快

del list_wv	#释放 2GB 内存

li_dv=[]
for line in f_d:
	#each line of the .big file
	words = line.split()
	li_vectors = []  # [w,w,w,...] => [v,v,v,...]
	for w in words:
		if w in dict_wv:
			li_vectors.append(dict_wv[w])

	#[v,v,v,...] => dv
	dv = np.mean(np.array(li_vectors),axis=0)	#len(dv) is 200
	try:
		if len(dv) !=200:
			dv = np.array([0]*200)
		li_s = map(lambda x:'%2.5g' %x, dv)
	except:
		dv = np.array([0]*200)
		li_s = map(lambda x:'%2.5g' %x, dv)
	f_dv.write(' '.join(li_s) + '\n')
#np.array(li_dv).savetxt(sys.argv[3],np.array(li_dv),fmt='%2.5g')
#np.loadtxt('xxx.txt')
