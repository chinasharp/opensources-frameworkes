package org.opensourceframework.netty.rpc.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opensourceframework.base.helper.ReflectHelper;
import org.opensourceframework.netty.rpc.core.dto.RpcRequest;
import org.opensourceframework.netty.rpc.core.dto.RpcResponse;
import org.opensourceframework.netty.rpc.core.manager.ProviderManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * 生产者通过NioNettyServer接受请求 并反射调用实际的bean执行 并返回执行结果
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 * @date  2020/12/30 下午3:14
 */
public class ProviderHandler {
	private static Logger logger = LoggerFactory.getLogger(ProviderHandler.class);
	private static ProviderHandler providerHandler;
	private static ServerBootstrap serverBootstrap;

	private ProviderHandler(){

	}

	public static ProviderHandler instance(){
		if(providerHandler == null) {
			providerHandler = new ProviderHandler();
			providerHandler.init();
		}
		return providerHandler;
	}

	private void init(){
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();

		try {
			serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(parentGroup, childGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);

			ProviderHandlerAdapter providerHandlerAdapter = new ProviderHandlerAdapter();
			ProviderChannelInitializer<NioSocketChannel> providerChannelInitializer = new ProviderChannelInitializer<>();

			serverBootstrap.option(ChannelOption.SO_BACKLOG, 100)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(providerChannelInitializer);
			logger.info("provider service is opened!");
			ChannelFuture channelFuture = serverBootstrap.bind(9090).sync();
			channelFuture.channel().closeFuture().sync();
		}catch (Exception e){

		}finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}

	class ProviderChannelInitializer<C extends Channel> extends ChannelInitializer{
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
			channel.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE , Delimiters.lineDelimiter()[0]));
			channel.pipeline().addLast(new ProviderHandlerAdapter());
			channel.pipeline().addLast(new StringEncoder());
		}
	}

	class ProviderHandlerAdapter extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if(msg instanceof ByteBuf) {
				String msgStr = ((ByteBuf)msg).toString(Charset.defaultCharset());
				RpcRequest request = JSON.parseObject(msgStr , RpcRequest.class);
				Object res = execute(request);

				ctx.channel().writeAndFlush(JSON.toJSONString(res) + "\r\n");
			}
		}

		public Object execute(RpcRequest rpcRequest) throws InvocationTargetException, IllegalAccessException {
			Class<?> rpcClass = rpcRequest.getRpcClass();
			Assert.notNull(rpcClass, "not found rpc class");
			ProviderManager providerManager = ProviderManager.instance();
			Object rpcBean = providerManager.getProviderBean(rpcClass);
			Assert.notNull(rpcBean, "not found rpc bean:" + rpcClass.getName());

			Class<?>[] paramTypes = rpcRequest.getParamTypes();
			String rpcMethod = rpcRequest.getRpcMethod();
			Method method = ReflectHelper.getAccessibleMethod(rpcBean , rpcMethod , paramTypes);
			Assert.notNull(method , "not found rpc class method:" + method.toString());

			Object[] paramValues = rpcRequest.getParamValues();
			for(int paramIndex = 0 ; paramIndex < paramValues.length ; paramIndex ++){
				Object paramValue = paramValues[paramIndex];
				Class<?> paramType = paramTypes[paramIndex];
				if(paramValue instanceof JSONObject){
					paramValues[paramIndex] =  ((JSONObject) paramValue).toJavaObject(paramType);
				}

				if(paramValue instanceof JSONArray){
					paramValues[paramIndex] = ((JSONArray)paramValue).toJavaList(paramType);
				}
			}

			Object obj = method.invoke(rpcBean , paramValues);

			RpcResponse rpcResponse = new RpcResponse(rpcRequest.getReqId() , obj);
			return rpcResponse;
		}
	}
}
