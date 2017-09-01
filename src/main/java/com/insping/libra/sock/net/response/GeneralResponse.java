package com.insping.libra.sock.net.response;

import com.insping.libra.proto.ResGeneral.GeneralData;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.UserInfo;

public class GeneralResponse {
	public static final byte RESP_SUCC = 0;
	public static final byte RESP_FAIL = 1;

	UserInfo userInfo = null;
	GeneralData.Builder builder = null;

	public GeneralResponse() {
		
	}
	
	public GeneralResponse(UserInfo userInfo, LibraMessage message) {
		// super(LibraHead.createHead(uid, 1),GeneralData.newBuilder().build());
		this.userInfo = userInfo;
		builder = GeneralData.newBuilder();
		builder.setProtocolID(message.getHead().getProtocolID());
		builder.setResultCode(RESP_SUCC);
	}

	public void fail() {
		builder.setResultCode(RESP_FAIL);
	}

	public void fail(String des) {
		builder.setResultCode(RESP_FAIL);
		builder.setDesc(des);
	}

	public boolean isSucc() {
		return builder.getResultCode() == RESP_SUCC;
	}

	public void setDesc(String desc) {
		builder.setDesc(desc);
	}

	public void modifyUserInfo(long uid) {
		this.userInfo.setUid(uid);
	}

	public LibraMessage result() throws Exception {
		return new LibraMessage(LibraHead.createHead(this.userInfo.getUid(), 1), builder.build());
	}
}
