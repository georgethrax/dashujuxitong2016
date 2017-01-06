#coding=utf-8
#将多个空格转换为单个空格；移除停止词
stops = open('stop.txt').read().split()
f_kv = open('/data/20161002.big.kv')
f_removed = open('20161002.big.kv.removed','w')
#dict_stop={}
#for word in stops:
#	dict_stop[word] = 1

for kv in f_kv:
	k,v = kv.split(',')
	words = v.split()
	words_removed = filter(lambda w:w not in stops,words)
	str_write = k+','+' '.join(words_removed)+'\n'
	f_removed.write(str_write)
f_removed.close()