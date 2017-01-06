#coding=utf-8
f_r = open("/data/20161002.big","r")
#f_r = open('/data/20161010/20161002/互联网/龚宇/42350ecfba38e8fbe31cbf9032fa570e.txt')

#str_lines = open('/data/20161010/20161002/互联网/龚宇/42350ecfba38e8fbe31cbf9032fa570e.txt','r').readlines()
import jieba
import re
f_w = open('20161002.big.split','w')
done = 0 
while not done:
	line = f_r.readline()
	if line != '':
		seg_list = jieba.cut(line)
		seg_list_tolist = list(seg_list)
		str_write = (" ".join(seg_list_tolist))
		str_write_zh_only = "".join(re.findall(u'[\u4e00-\u9fa5\s+]',str_write))
		f_w.write(str_write_zh_only.encode('utf-8'))
	else:
		done = 1
f_w.close()
f_r.close()