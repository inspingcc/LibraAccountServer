package com.insping.libra.sock.net.codec.data;

public enum LibraMessageType {

	SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2), REGIST_REQ((byte) 3), REGIST_RESP((byte) 4), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6), MESSAGE_REQ((byte) 7), MESSAGE_RESP((byte) 8);

	private byte value;

	private LibraMessageType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public static LibraMessageType search(byte value) {
		for (LibraMessageType type : values()) {
			if (type.value == value) {
				return type;
			}
		}
		return null;
	}
}
