package com.insping.libra.sock.net.handler;

import java.util.Map;

import com.google.protobuf.MessageLite;
import com.insping.Instances;
import com.insping.common.i18.I18nGreeting;
import com.insping.common.utils.JsonUtil;
import com.insping.common.utils.StringUtils;
import com.insping.libra.proto.ReqHttpMessage.ReqHttpMessageData;
import com.insping.libra.proto.ResHttpMessage.ResHttpMessageData;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.LibraMessageType;
import com.insping.libra.sock.net.response.ReturnObject;
import com.insping.log.LibraLog;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author houshanping
 */
public class SockLogicHandler extends ChannelInboundHandlerAdapter implements Instances {

    private ChannelHandlerContext ctx;

    private LibraMessage request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        LibraMessage request = (LibraMessage) msg;
        if (request.getBody() == null || request.getHead() == null) {
            return;
        }
        this.request = request;
        if (request.getHead().getType() != LibraMessageType.MESSAGE_REQ.getValue()) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (!(request.getBody() instanceof MessageLite)) {
            sendErrorMsg(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        ReqHttpMessageData data = (ReqHttpMessageData) request.getBody();
        Map<String, String> params = JsonUtil.JsonToObjectMap(data.getHttpReqMessage(), String.class, String.class);

        //TODO 简单的处理相关数据(优化可以考虑使用线程池)
        String data1 = params.get("action");
        if (StringUtils.isNull(params.get("action"))) {
            sendErrorMsg(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        ServerHandler handler = handlerMgr.searchHandler(params.get("action"));
        if (handler == null) {
            sendErrorMsg(I18nGreeting.SERVER_HANDLER_EXECPTION);
            return;
        }
        ReturnObject resp = new ReturnObject();
        try {
            handler.doLogic(resp, params);
            sendMessage(resp);
        } catch (Exception e) {
            LibraLog.error("handler is exception.e = " + e.getMessage());
            resp.fail("logic server is exception!");
            sendMessage(resp);
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

    /**
     * 发送错误消息
     *
     * @param data
     */
    private void sendErrorMsg(String data) {
        ReturnObject obj = new ReturnObject();
        obj.fail(666, data);
        sendMessage(obj);
    }

    private void sendMessage(ReturnObject obj) {
        ResHttpMessageData.Builder builder = ResHttpMessageData.newBuilder();
        builder.setHttpResMessage(JsonUtil.ObjectToJsonString(obj));

        LibraMessage response = new LibraMessage(request.getHead(), builder.build());
        response.getHead().setType(LibraMessageType.MESSAGE_RESP.getValue());
        ctx.writeAndFlush(response);
    }
}
