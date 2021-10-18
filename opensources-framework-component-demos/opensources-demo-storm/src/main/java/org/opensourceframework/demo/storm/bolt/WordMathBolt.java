package org.opensourceframework.demo.storm.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/23 下午2:02
 */
public class WordMathBolt extends BaseRichBolt {
	private Map<String, Long> wordCounts = new HashMap<String, Long>();
	private OutputCollector outputCollector;
	@Override
	public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
		outputCollector = outputCollector;
	}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");

		Long count = wordCounts.get(word);
		if(count == null){
			count = 0L;
		}
		count ++;

		outputCollector.emit(new Values(word , count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("word" , "count"));
	}
}
