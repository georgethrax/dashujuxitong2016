# 大数据系统2016
组号18，项目号8，文本相似度建模在保险行业的应用
##1. 环境说明
本项目所处理的数据集是公司提供的13GB文本，存储在公司为我们小组提供的6台独立于生产环境的服务器上。我们的代码几乎都需要在公司服务器上运行。

我们在node[1,3-6]这5台服务器上搭建了集群。我们的一部分程序需要在集群中运行。集群的配置详见项目报告。

我们的另一部分程序可在node2上单机运行。这部分程序主要是Python程序，需要安装jieba,numpy,scipy,matplotlib等Python库。



##2. 代码说明
###2.1 数据集的分布分析和清洗
####2.1.1 分布分析

[/dirwalk/dirwalk.py]()
分析公司所提供的原始文本的目录结构和标签分布情况。

[/dirwalk/figure_1.png]()
公司所提供的原始文本的文件字节数分布直方图

[/dirwalk/figure_2.png]()
公司所提供的原始文本的文件字节数分布折线图

[/dirwalk/histogram.npy]()
直方图的numpy数据文件，包含了直方图的数据信息。

[/dirwalk/nplenghts.npy]()
一个numpy数据文件，包含了原始数据集中各个小文件的长度。

####2.1.2 数据清洗

[/dirwalk/makebigtxt.py]()
将公司所提供的所有文件连接到一个单独的大文件中，文件后缀为.big。这个步骤非常重要，公司的原始数据集是2,985,308个小的文本文件，需要将其转换为一个单独的大文件便于处理。

[/dirwalk/cutbigtxt.py]()
中文分词，将.big文件转换为.split文件，其中每行对应原来的一个文本，中文词与词之间用一个空格隔开，且去除了非中文字符。


[/dirwalk/remover.py]()
移除停止词程序。将已经做完中文分词的.split文件中出现在stop.txt中的词去除掉，输出.removed文件。

[/dirwalk/stop.txt]()
remover.py所需要的停止词词典。

[/dirwalk/big2kv.py](/dirwalk/big2kv.py)
将.big文件转换为.kv文件，其中每行对应原来的一个文本，是用逗号','隔开的两部分，第一部分是文本原始路径，第二部分是文本内容。

[/dirwalk/kv2nkv.py]()
将.kv文件转换为.nkv文件，其中每行对应原来的一个文本，是用逗号','隔开的3部分，第一部分是文本序号(0,1,2,...)，第二部分是文本原始路径，第三部分是文本内容。

注：big2kv和kn2nkv不是必要的，但会对在spark中使用RDD有所帮助。

###2.2 基于TFIDF的文本相似度


###2.3 基于Word2vec的文本相似度

创建文档时点击“普通文档”后面的**下拉三角**。
> 很好
> 很强大
> 很时髦

放个[链接](http://zijingzhiyuanzhe.cn)
分割线 
---

---
放个图

![图片名称](http://mp.weixin.qq.com/mp/qrcode?scene=10000004&size=102&__biz=MjM5NDczNDYyNQ==)

放个代码
```
printf("HW\n");
```