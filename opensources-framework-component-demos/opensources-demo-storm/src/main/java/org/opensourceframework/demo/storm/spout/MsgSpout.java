package org.opensourceframework.demo.storm.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * TODO
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2021/2/22 下午5:55
 */
public class MsgSpout extends BaseRichSpout {
	public static ArrayBlockingQueue<String> msgQueue = new ArrayBlockingQueue<>(50);
	private SpoutOutputCollector spoutOutputCollector;

	@Override
	public void open(Map<String, Object> map, TopologyContext topologyContext,
			SpoutOutputCollector spoutOutputCollector) {
		spoutOutputCollector = spoutOutputCollector;
	}

	@Override
	public void nextTuple() {
		try {
			String msg = msgQueue.take();
			spoutOutputCollector.emit(new Values(msg));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("msg"));
	}
}
