package com.insping.libra.sock.net.handler;

import com.insping.Instances;
import com.insping.common.utils.TimeUtils;
import com.insping.libra.proto.ReqServerRegist.ServerRegistData;
import com.insping.libra.proto.ResGeneral.GeneralData;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.LibraMessageType;
import com.insping.libra.sock.net.response.GeneralResponse;
import com.insping.libra.world.LibraConfig;
import com.insping.log.LibraLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author houshanping
 *
 */
public class ServerRegistHandler extends ChannelInboundHandlerAdapter implements Instances {

	public ServerRegistHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		// 发送服务器注册消息
		ServerRegistData.Builder builder = ServerRegistData.newBuilder();
		builder.setServerID(LibraConfig.SERVER_ID);
		builder.setServerName(LibraConfig.SERVER_NAME);
		builder.setServerKey("serverkey");
		builder.setServerType(0);
		builder.setServerStatus(0);
		builder.setServerIp(LibraConfig.SERVER_IP);
		builder.setTime(TimeUtils.nowLong());
		builder.setServerDesc("desc");
		// 数据Head
		LibraHead head = new LibraHead();
		head.setDestServerID(LibraConfig.SERVER_ID);
		head.setSrcServerID(0);
		head.setType(LibraMessageType.REGIST_REQ.getValue());
		head.setProtocolID(0);
		head.setMessageID(0);
		LibraMessage message = new LibraMessage(head, builder.build());
		ctx.writeAndFlush(message);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		LibraMessage message = (LibraMessage) msg;
		if (message.getHead() != null && message.getHead().getType() == LibraMessageType.REGIST_RESP.getValue()) {
			GeneralData data = (GeneralData) message.getBody();
			if (data.getResultCode() == GeneralResponse.RESP_FAIL) {
				// 握手失败，关闭连接
				ctx.close();
			} else {
				LibraLog.info("ServerRegistHandler-channelRead :Server regist is success!");
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
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
