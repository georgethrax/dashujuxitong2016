import sys
if len(sys.argv)!=3:
	f_input = open('/data/lx/big2kv/20161002.big.kv')
	f_output = open('20161002.big.nkv','w')
else:
	f_input = open(sys.argv[1])
	f_output = open(sys.argv[2],'w')
i = 0
for line in f_input:
	k,v = line.split(',')
	v = ' '.join(v.split())
	f_output.write(str(i)+','+ k + ',' + v + '\n')
	if i % 100000 == 0:
		print('i=%d\n' %i)
	i = i + 1
f_output.close()