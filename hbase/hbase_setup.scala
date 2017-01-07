/*
1. sbt相关/opt/cloudera/parcels/CDH-5.9.0-1.cdh5.9.0.p0.23/etc/hbase/conf.dist1. 设置HDFS的属性：dfs.replication = 1(初始为3，没有必要)

2. 增加内存，重启机器

3. 重启后，zookeeper出问题，发现是node4,5,6的防火墙没关

4. 关闭防火墙后，开启hbase还是有问题，master起不来。查看log发现是hbase用户无权访问/hbase文件夹：“Permission denied: user=hbase, access=WRITE, inode="/user":hdfs:supergroup:dr”。发现是误删/hbase文件夹，而要建立这个文件夹需要在CM中操作：“Home -> hbase -> Actions -> "Create /hbase root dir…"。这一步在安装hbase时有过操作，我的误删导致不得不重新操作一遍。

参考文档：http://grokbase.com/t/cloudera/scm-users/12bmwzhdwe/accesscontrolexception-permission-denied-user-hbase

5 hbase shell

在服务器窗口输入“hbase shell”即打开hbase shell，如下图：

Clipboard Image.png

可以进行增删查找表的基本操作。参考文档http://www.cnblogs.com/nexiyi/p/hbase_shell.html

5. 在spark中操作Hbase

5.1 原理

Spark操作HBase其实是和java client操作HBase的原理是一致的：scala和java都是基于jvm的语言，只要将hbase的类加载到classpath内，即可调用操作，其它框架类似。

相同点 ：即都是当作client来连接HMaster，然后利用hbase的API来对Hbase进行操作。

不同点 ：唯一不同的是：Spark可以将Hbase的数据来当作RDD处理，从而利用Spark来进行并行计算。

参考：http://www.tuicool.com/articles/ERBFny

5.2 设置环境变量

export CLASSPATH=$CLASSPATH:/opt/cloudera/parcels/CDH-5.9.0-1.cdh5.9.0.p0.23/lib/hadoop/lib/native:/opt/cloudera/parcels/CDH-5.9.0-1.cdh5.9.0.p0.23/lib/hbase/lib/native/Linux-amd64-64:/usr/local/scala/scala-2.11.8/lib
参考文档：https://archive.cloudera.com/cdh5/cdh/5/hbase/book.html#scala

上述配置解决了错误：java.io.IOException: Cannot run program "/etc/spark/conf.cloudera.spark_on_yarn/yarn-conf/topology.py" (in directory "/root"): error=13, Permission denied
5.3 sbt相关

sbt和maven都是将scala工程进行打包的方式，使用spark-submit执行程序。这个方法暂时搁置，首先用spark-shell中进行hbase操作。

5.4 spark-shell

使用命令：sudo -u spark spark-shell  --master=yarn --num-executors 4  （其中--master yarn-client可以省略）

参考文档：http://lecluster.delaurent.com/spark-on-hbase-with-spark-shell/
*/

import org.apache.hadoop.hbase.{HBaseCofiguration, HTableDescriptor}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
val tableName = "t1"
val hconf = HBaseConfiguration.create()
val admin = new HBaseAdmin(hconf)
admin.listTables
/*
报错：zookeeper Client Session 0x0 for server null
发现在hbase的master（也就是node3）是成功的，如下：

Clipboard Image.png

查看hbase的配置文件位置：/etc/hbase/conf/hbase-site.xml，配置hbase.zookeeper.quorum正常。node1报上述错误的原因是没有显式配置hbase.zookeeper.quorum。

参考文档https://www.zybuluo.com/xtccc/note/91427


更正方法是增加一行代码，如下：

*/

import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
val tableName = "t1"
val hconf = HBaseConfiguration.create()
hconf.set("hbase.zookeeper.quorum", "node3:2181,node4:2181,node5:2181")
val admin = new HBaseAdmin(hconf)
admin.listTables
/*
6. spark-shell下遍历20161010的文件内容，输出：file_name,content,label

6.1  scala读取文件名称

一层遍历：
*/

import java.io.File
def getListOfFiles(dir: String, depth: Int):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
        if (depth == 1){
            d.listFiles.toList
        } else{
            d.listFiles.filter(_.isDirectory).toList.flatMap(x => getListOfFiles(x.toString(),depth-1))}} else {
        List[File]()
    }
}
//val files = getListOfFiles("/root/",2)
val files = getListOfFiles("/data/20161002/",2)

/*
参考：http://alvinalexander.com/scala/how-to-list-files-in-directory-filter-names-scala

7. Hbase行列设计，数据导入

7.1 表的设计

tablename：Text rowkey：label1_label2_文件ID；Column Family：‘feature’；cloumn：‘date’，‘content’，‘label1’，‘label2’
7.2 表的创建：
*/

//  ~/mps/CreateTable.scala work on Spark-shell
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import scala.collection.JavaConversions._
val hconf = HBaseConfiguration.create()
hconf.set("hbase.zookeeper.quorum", "node3:2181,node4:2181,node5:2181")
val conn = ConnectionFactory.createConnection(hconf)
val admin = conn.getAdmin

val userTable = TableName.valueOf("Text")
val tableDescr = new HTableDescriptor(userTable)
tableDescr.addFamily(new HColumnDescriptor("feature".getBytes))
println("Creating table `Text`. ")
if (admin.tableExists(userTable)) {
    admin.disableTable(userTable)
    admin.deleteTable(userTable)
}
admin.createTable(tableDescr)
println("Done!")

/*
参考：https://gist.github.com/wuchong/95630f80966d07d7453b

7.3 txt文件预处理

遍历文件，并且把所有的行用“ ”连接起来
*/

import scala.util.matching.Regex
for (file <- files){
    val file_dir = file.toString().split("/",6)
    val row_key =  file_dir(3)+'_'+ file_dir(4)+'_'+file_dir(5).substring(0,file_dir(5).length-4)
    val date = file_dir(2)
    val label_1 = file_dir(3)
    val label_2 = file_dir(4)

    //get content by file name
    val file_content=(Source.fromFile(file.toString()).getLines.toList drop 3).mkString(""," ","").replaceAll("</title>", "").replaceAll("<title>", "").replaceAll("<content>", "").replaceAll("</content>", "")
    val pattern = new Regex("<image-\\d{1,3}>")
    val file_content1 = pattern replaceAllIn(file_content, "")//去除无用的</image>等字符串
}
/*
总的代码（后续又加了正则表达式，）
*/
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import scala.collection.JavaConversions._
import java.io.File
import scala.util.matching.Regex
import scala.io.Source

val hconf = HBaseConfiguration.create()
hconf.set("hbase.zookeeper.quorum", "node3:2181,node4:2181,node5:2181")

//Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
val conn = ConnectionFactory.createConnection(hconf)

//从Connection获得 Admin 对象(相当于以前的 HAdmin)
val admin = conn.getAdmin

//本例将操作的表名
val userTable = TableName.valueOf("Text")

//获取text表
val table = conn.getTable(userTable)

//读取列表并且写入Hbase
def putFilesToHbase(dir: String, depth: Int):Unit = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
        if (depth == 1){
            val file_list = d.listFiles.toList
            for (file <- file_list){
                val file_dir = file.toString().split("/",6)
                if(file_dir(5).length>=5 &&  file_dir(5).substring(file_dir(5).length-4,file_dir(5).length)==".txt"){
                    val row_key =  file_dir(3)+'_'+ file_dir(4)+'_'+file_dir(5).substring(0,file_dir(5).length-4)
                    println(row_key)
                    val date = file_dir(2)
                    val label_1 = file_dir(3)
                    val label_2 = file_dir(4)
                     //get content by file name
                    val source = Source.fromFile(file.toString())
                    val file_content=(source.getLines.toList drop 3).mkString(""," ","").replaceAll("</title>", "").replaceAll("<title>", "").replaceAll("<content>", "").replaceAll("</content>", "")
                    val pattern = new Regex("<image-\\d{1,3}>")
                    val file_content1 = pattern replaceAllIn(file_content, "")
                    source.close()

                    //写入Hbase
                    val p = new Put(row_key.getBytes)
                    p.addColumn("feature".getBytes,"content".getBytes, file_content1.getBytes)
                    p.addColumn("feature".getBytes,"label_1".getBytes, label_1.getBytes)
                    p.addColumn("feature".getBytes,"label_2".getBytes, label_2.getBytes)
                    p.addColumn("feature".getBytes,"date".getBytes, "20161002".getBytes)
                    table.put(p)
                }
            }

        } else{
            val file_list = d.listFiles.filter(_.isDirectory).toList
            for (file  <- file_list){
                putFilesToHbase(file.toString(),depth-1)}}
    }
}
putFilesToHbase("/data/20161002/生活",2) //将生活文件夹下的两层文件写入Hbase