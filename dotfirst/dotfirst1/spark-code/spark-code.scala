/*
从node1启动集群上的spark-shell
sudo -u spark spark-shell --num-executors 4 
*/

//读取文件。.big.removed是已经分词和移除停止词后的，不含kv。
val path = "hdfs:///data/20161002.big.removed"
val doc = sc.textFile(path)
//res0: String = "蜂蜜 柚子茶 喝 蜂蜜 柚子茶 味道 清香 可口 有美白 祛斑 嫩肤 养颜 通便 排毒 功效 食用 清热 降火 嫩白 皮肤 蜂蜜 柚子茶 味道 清香 ......."

//用空格分词。
val doc_split = doc.map(_.split(" ").toSeq)
//res1: Seq[String] = WrappedArray(蜂蜜, 柚子茶, 喝, 蜂蜜, 柚子茶, 味道, 清香, 可口, 有美白, 祛斑, 嫩肤, 养颜, 通便, 排毒, 功效, 食用, 清热, 降火, 嫩白, 皮肤, 蜂蜜, 柚子茶, 味道, 清香, 可口, 有美白, 祛斑, 嫩肤, 养颜, 通便, 排毒, 功效, 食用, 清热, 降火, 嫩白, 皮肤, 适合, 坐在, 办公室, 皮肤, 遭受, 电脑, 辐射损伤, 气色, 暗淡, 办公室, 女性, 疑问, 蜂蜜, 柚子茶, 喝好, 空腹, 饮用, 空腹, 饮用, 肠胃, 刺激, 导致, 腹泻, 肠胃, 不适, 症状, 饭后, 喝, 吃完饭, 喝茶, 影响, 铁, 吸收, 时间, 长, 诱发, 贫血, 最佳, 时间, 饭后, 小时, 胃肠, 营养, 吸收, 不良影响, 身体, 更好, 吸收, 蜂蜜, 柚子茶, 营养, 蜂蜜, 柚子茶, 冲泡, 滚烫, 开水, 破坏, 蜂蜜, 营养成分, 影响, 口味, 温开水, 冲泡, 最为, 适宜, 需作, 冷饮, 冰块, 冷却, 即可, 蜂蜜, 柚子茶, 用作, 调制, 鸡尾酒, 当成, 果酱, 涂抹, 面包, 做成, 柚子, 果冻, 食用, 图片, 来源)


//tfidf
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

val hashingTF = new HashingTF()
val tf: RDD[Vector] = hashingTF.transform(doc_split)
tf.cache()
val idf = new IDF(minDocFreq = 2).fit(tf)
val tfidf: RDD[Vector] = idf.transform(tf)

//保存至磁盘，以供恢复
tfidf.saveAsObjectFile("hdfs:///user/spark/20161002.big.removed.tfidf")


//计算前需要恢复tfidf变量。
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
val tfidf:RDD[Vector] = sc.objectFile("hdfs:///user/spark/20161002.big.removed.tfidf")

//计算第1个向量与其余所有向量的余弦相似度
val v = tfidf.first()
val pair = tfidf.map( x => (x,v) )
val dotfirst = pair.map{ p => 
    val x=p._1;    val y=p._2;
    val nx = Vectors.norm(x,2);     val ny = Vectors.norm(y,2); 
    val sq = Vectors.sqdist(x,y);
    (nx*nx+ny*ny-sq)/2/nx/ny
  }

//将余弦相似度的结果保存到文件。
dotfirst.saveAsTextFile("/user/spark/20161002.big.removed.tfidf.dotfirst.txt")

将文件从hdfs取到node1上。在node1上执行命令：
hadoop fs -get /user/spark/20161002.big.removed.tfidf.dotfirst.txt /data/

登录至node2,将该文件夹传输至node2上
ssh node2 -p 9922
cd /data
scp -r node1:/data/20161002.big.removed.tfidf.dotfirst.txt/ /data/

node2上打开ipython
ipython

读取数据
f = open("/data/20161002.big.removed.tfidf.dotfirst.txt/part-00000","r")
l = f.readlines()
lf = map(float,l) #将字符串转换为数值
len(lf) #应为2985308

可视化
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
plt.plot(lf)
plt.savefig("/data/ufirst.png")   #存入png图片