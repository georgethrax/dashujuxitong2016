#coding:utf-8
#目的，把20161002.index 和 vectors-20161002.txt 合并，然后把合并后的文件转换为.bin文件，供word2vec的distance调用，可以实时返回最近邻结果
f_index = open('/data/20161002.index')
f_vectors = open('20161002.big.removed.dv2')
f_output = open('indexdv.txt','w')

f_output.write('2985308 200\n')

i=0
for line in f_index:
	l = line.split('/')[3:]
	str_l = '/'.join(l)
	v = f_vectors.readline().split() 
	str_v = ' '.join(v)
	str_output = str_l + ' ' + str_v + '\n'
	f_output.write(str_output)
	i = i+1
print('i=%d\n' %i)
