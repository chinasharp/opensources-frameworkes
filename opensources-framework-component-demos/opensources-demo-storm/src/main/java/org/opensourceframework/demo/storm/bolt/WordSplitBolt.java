package org.opensourceframework.demo.storm.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/23 下午1:57
 */
public class WordSplitBolt extends BaseRichBolt {
	private OutputCollector outputCollector;

	@Override
	public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
		outputCollector = outputCollector;
	}

	@Override
	public void execute(Tuple tuple) {
		String msg = tuple.getStringByField("msg");
		String[] words = msg.split(" ");
		for(String word : words) {
			outputCollector.emit(new Values(word));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("word"));
	}
}
