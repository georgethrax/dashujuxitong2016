# 大数据系统2016
组号18，项目号8，文本相似度建模在保险行业的应用
##1. 环境说明
本项目所处理的数据集是公司提供的13GB文本，存储在公司为我们小组提供的6台独立于生产环境的服务器上。我们的代码几乎都需要在公司服务器上运行。

我们在node[1,3-6]这5台服务器上搭建了集群。我们的一部分程序需要在集群中运行。集群的配置详见项目报告。

我们的另一部分程序可在node2上单机运行。这部分程序主要是Python程序，需要安装jieba,numpy,scipy,matplotlib等Python库。

##2. 代码说明
###2.1 数据集的分布分析和清洗
####2.1.1 分布分析

[dirwalk/dirwalk.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/dirwalk.py)
分析公司所提供的原始文本的目录结构和标签分布情况。

[dirwalk/figure_1.png](https://github.com/georgethrax/dashujuxitong2016/blob/master/figure_1.png)
公司所提供的原始文本的文件字节数分布直方图

[dirwalk/figure_2.png](https://github.com/georgethrax/dashujuxitong2016/blob/master/figure_2.png)
公司所提供的原始文本的文件字节数分布折线图

[dirwalk/histogram.npy](https://github.com/georgethrax/dashujuxitong2016/blob/master/histogram.npy)
直方图的numpy数据文件，包含了直方图的数据信息。

[dirwalk/nplenghts.npy](https://github.com/georgethrax/dashujuxitong2016/blob/master/nplenghts.npy)
一个numpy数据文件，包含了原始数据集中各个小文件的长度。

####2.1.2 数据清洗

[dirwalk/makebigtxt.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/makebigtxt.py)
将公司所提供的所有文件连接到一个单独的大文件中，文件后缀为.big。这个步骤非常重要，公司的原始数据集是2,985,308个小的文本文件，需要将其转换为一个单独的大文件便于处理。

[dirwalk/cutbigtxt.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/cutbigtxt.py)
中文分词，将.big文件转换为.split文件，其中每行对应原来的一个文本，中文词与词之间用一个空格隔开，且去除了非中文字符。

[dirwalk/remover.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/remover.py)
移除停止词程序。将已经做完中文分词的.split文件中出现在stop.txt中的词去除掉，输出.removed文件。

[dirwalk/stop.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/stop.txt)
remover.py所需要的停止词词典。

[dirwalk/big2kv.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/dirwalk/big2kv.py)
将.big文件转换为.kv文件，其中每行对应原来的一个文本，是用逗号','隔开的两部分，第一部分是文本原始路径，第二部分是文本内容。

[dirwalk/kv2nkv.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/kv2nkv.py)
将.kv文件转换为.nkv文件，其中每行对应原来的一个文本，是用逗号','隔开的3部分，第一部分是文本序号(0,1,2,...)，第二部分是文本原始路径，第三部分是文本内容。
注：big2kv和kn2nkv不是必要的，但会对在spark中使用RDD有所帮助。

[hbase/extract_1000.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/hbase/extract_1000.txt)
每个分类抽取1000篇文章（from Hbase）

[hbase/hbase_setup.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/hbase/hbase_setup.txt)
Hbase设置与数据导入
###2.2 基于TFIDF的文本相似度

[dotfirst/dotfirst1/spark-code/spark-code.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/dotfirst/dotfirst1/spark-code/spark-code.txt)
在公司服务器集群上使用spark基于tfidf计算首篇文本与其余所有文本的相似度，并在服务器上用matplotlib绘图。代码、操作步骤都在该文件中。

[dotfirst/dotfirst1/dotfirst.1.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/dotfirst/dotfirst1/dotfirst.1.txt)
基于tfidf的首篇文本与其余所有文本的相似度数据文件。

[dotfirst/dotfirst1/dotfirst.1.png](https://github.com/georgethrax/dashujuxitong2016/blob/master/dotfirst/dotfirst1/dotfirst.1.png)
基于tfidf的首篇文本与其余所有文本的相似度图。

###2.3 基于Word2vec的文本相似度
####2.3.1 word2vec 词向量训练
[word2vec-train/trunk](https://github.com/georgethrax/dashujuxitong2016/blob/master/word2vec-train/trunk)
词向量训练程序，来自[https://code.google.com/p/word2vec/](https://code.google.com/p/word2vec/)

[word2vec-train/shell-script.txt](https://github.com/georgethrax/dashujuxitong2016/blob/master/word2vec-train/shell-script.txt)
运行词向量训练的 Linux Shell 命令。

####2.3.2 由词向量得到文档向量
[wv2dv/src/wv2dv.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/wv2dv/src/wv2dv.py)
利用训练好的词向量文件，把每篇文档转化为一个文档向量。


####2.3.3 由文档向量计算余弦相似度
[wv2dv/src/dotfirst.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/wv2dv/src/dotfirst.py)
由文档向量计算余弦相似度

[dotfirst/dotfirst2/](https://github.com/georgethrax/dashujuxitong2016/blob/master/dotfirst/dotfirst2/)
将相似度计算结果可视化。

####2.3.4 查询与输入文档最相似的50篇文档
[wv2dv/src/merge_index_dv.py](https://github.com/georgethrax/dashujuxitong2016/blob/master/wv2dv/src/merge_index_dv.py)
将文档向量与其对应的原始路径信息结合起来.

[wv2dv/src/distance.c](https://github.com/georgethrax/dashujuxitong2016/blob/master/wv2dv/src/distance.c)
查询与输入文档最相似的50篇文档。输入一篇文档的原始路径，返回与其最相似的50篇文档的原始路径。本程序是在word2vec原始程序中的同名文件的基础上做了修改。
