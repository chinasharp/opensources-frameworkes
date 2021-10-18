package org.opensourceframework.netty.rpc.core.handler;

import com.alibaba.fastjson.JSON;
import org.opensourceframework.netty.rpc.core.dto.RpcRequest;
import org.opensourceframework.netty.rpc.core.dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过Netty Client 发送调用请求
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午3:14
 */
public class ConsumerHandler {
	private static Logger logger = LoggerFactory.getLogger(ConsumerHandler.class);
	private static Bootstrap bootstrap;
	private static final String MSG_DELIMITER_STRING = "\r\n";

	private static ConsumerHandler consumerHandler;

	private ConsumerHandler() {
		init();
	}

	public static ConsumerHandler instance(){
		if(consumerHandler == null){
			consumerHandler = new ConsumerHandler();
		}
		return consumerHandler;
	}

	/**
	 * cosumer netty 调用 provider
	 *
	 * @param rpcRequest
	 * @return
	 */
	public RpcResponse invoke(RpcRequest rpcRequest){
		RpcResponse rpcResponse = null;
		String reqId = rpcRequest.getReqId();
		try {
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9090).sync();

			String msg = JSON.toJSONString(rpcRequest) + MSG_DELIMITER_STRING;
			channelFuture.channel().writeAndFlush(msg);
			channelFuture.channel().closeFuture().sync();
			rpcResponse = (RpcResponse)channelFuture.channel().attr(AttributeKey.valueOf(reqId)).get();
		}catch (Exception e){
			e.printStackTrace();
			rpcResponse = new RpcResponse(reqId , e.getMessage());
		}
		return rpcResponse;
	}

	/**
	 * 初始化Netty 客户端配置
	 *
	 */
	private void init() {
		bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class);

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		bootstrap.group(eventLoopGroup);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		ConsumerChannelInitializer<NioSocketChannel> consumerInitializer = new ConsumerChannelInitializer<>();
		bootstrap.handler(consumerInitializer);
	}

	class ConsumerChannelInitializer<C extends Channel> extends ChannelInitializer{

		/**
		 * This method will be called once the {@link Channel} was registered. After the method returns this instance
		 * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
		 *
		 * @param channel the {@link Channel} which was registered.
		 * @throws Exception is thrown if an error occurs. In that case it will be handled by
		 *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
		 *                   the {@link Channel}.
		 */
		@Override
		protected void initChannel(Channel channel) throws Exception {
			channel.pipeline().addLast(new StringDecoder());
			channel.pipeline().addLast(new ConsumerHandlerAdapter());
			channel.pipeline().addLast(new StringEncoder());
		}
	}

	class ConsumerHandlerAdapter extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			RpcResponse rpcResponse = JSON.parseObject(msg.toString() , RpcResponse.class);
			ctx.channel().attr(AttributeKey.valueOf(rpcResponse.getReqId())).set(rpcResponse);
			ctx.channel().close();
		}
	}
}
