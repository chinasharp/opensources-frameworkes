package org.opensourceframework.demo.storm.topology;

import org.opensourceframework.demo.storm.bolt.WordMathBolt;
import org.opensourceframework.demo.storm.bolt.WordSplitBolt;
import org.opensourceframework.demo.storm.spout.MsgSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * 单词统计 Storm程序
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/22 下午2:32
 */
public class WordCountTopology {
	public static void main(String[] args) throws Exception {
		// 在main方法中，会去将spout和bolts组合起来，构建成一个拓扑
		TopologyBuilder builder = new TopologyBuilder();

		// 这里的第一个参数的意思，就是给这个spout设置一个名字
		// 第二个参数的意思，就是创建一个spout的对象
		// 第三个参数的意思，就是设置spout的executor有几个
		builder.setSpout("MsgSpout", new MsgSpout(), 2);
		builder.setBolt("WordSplitBolt", new WordSplitBolt(), 5)
				.setNumTasks(10)
				.shuffleGrouping("RandomSentence");
		// 这个很重要，就是说，相同的单词，从SplitSentence发射出来时，一定会进入到下游的指定的同一个task中
		// 只有这样子，才能准确的统计出每个单词的数量
		// 比如你有个单词，hello，下游task1接收到3个hello，task2接收到2个hello
		// 5个hello，全都进入一个task
		builder.setBolt("WordMathBolt", new WordMathBolt(), 10)
				.setNumTasks(20)
				.fieldsGrouping("SplitSentence", new Fields("word"));

		Config config = new Config();

		// 说明是在命令行执行，打算提交到storm集群上去
		if(args != null && args.length > 0) {
			config.setNumWorkers(3);
			try {
				StormSubmitter.submitTopology(args[0], config, builder.createTopology());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 说明是在eclipse里面本地运行
			config.setMaxTaskParallelism(20);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("WordCountTopology", config, builder.createTopology());

			Utils.sleep(60000);

			cluster.shutdown();
		}
	}
}
