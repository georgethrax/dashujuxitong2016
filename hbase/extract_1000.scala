/*
执行环境：Spark-shell，命令：sudo -u spark spark-shell  --master=yarn --num-executors 4

写入hdfs:///user/spark/extract_1000下，每个类一篇文档，每篇文章一行。
*/

import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import scala.collection.JavaConversions._
import java.io.File
import scala.util.matching.Regex
import scala.io.Source
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.{Base64, Bytes}
import org.apache.hadoop.mapred.JobConf
import java.io.PrintWriter

val conf = HBaseConfiguration.create()
conf.set("hbase.zookeeper.quorum", "node3:2181,node4:2181,node5:2181")

conf.set(TableInputFormat.INPUT_TABLE, "Text")

val dirs = Array("互联网","理财","生活","金融","健康","默认大分类","政策","职场","科技","保险");
for(dir <- dirs){
    conf.set(TableInputFormat.SCAN_ROW_START,dir+"_")
    conf.set(TableInputFormat.SCAN_ROW_STOP,dir+"a")

    val text = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    //获得随机组合数，1000
    val text_sample = text.takeSample(false,1000) //false means 无放回的抽样，1000means长度
    var text_contents = Array[String]()
    text_sample.foreach{ case (_,result) =>
        val key = Bytes.toString(result.getRow)
        println(key)
        val content = Bytes.toString(result.getValue("feature".getBytes,"content".getBytes))
        //println(content)
        text_contents = text_contents:+content
    }
    //取以上数据中的text并且放入List[String],放入RDD并且写入HDFS
    val sample_rdd = sc.parallelize(text_contents)
    sample_rdd.repartition(1).saveAsTextFile("hdfs:///user/spark/extract_1000/"+dir)
}
