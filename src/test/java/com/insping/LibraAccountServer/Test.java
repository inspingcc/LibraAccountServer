package com.insping.LibraAccountServer;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {

	public static void main(String[] args) {
		String passwd = "123123";
		System.out.println(DigestUtils.sha1Hex(passwd));
	}

}
