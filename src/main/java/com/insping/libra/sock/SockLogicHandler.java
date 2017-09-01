package com.insping.libra.sock;

import com.insping.Instances;
import com.insping.libra.proto.ReqServerHeartbeat.HeartbeatData;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.handler.ServerHandler;
import com.insping.libra.world.LibraConfig;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author houshanping
 *
 */
public class SockLogicHandler extends ChannelHandlerAdapter implements Instances {

	public SockLogicHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		// 发送心跳
		HeartbeatData.Builder builder = HeartbeatData.newBuilder();
		builder.setType(0);// 0:注册 1:正常心跳
		builder.setServerID(LibraConfig.SERVER_ID);
		builder.setServerStatus(0);
		builder.setServerKey("serverkey");
		builder.setServerDesc("desc");
		ctx.writeAndFlush(builder.build());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		LibraMessage message = (LibraMessage) msg;
		ServerHandler handler = handlerMgr.searchHandlerByRequestClass(message.getHead().getProtocolID());
		if (handler == null) {
			System.out.println("protocolID is :" + message.getHead().getProtocolID() + ",handler is null.");
		}
		try {
			LibraMessage libraMessage = handler.doLogic(ctx, message);
			ctx.writeAndFlush(libraMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
