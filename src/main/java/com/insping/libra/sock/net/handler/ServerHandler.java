package com.insping.libra.sock.net.handler;

import java.util.Map;

import com.insping.Instances;
import com.insping.libra.sock.net.response.ReturnObject;

/**
 * 
 * @author houshanping
 * @since 2017-07-12
 */
public abstract class ServerHandler implements Instances{

	public abstract void doLogic(ReturnObject resp, Map<String, String> params) throws Exception;

}
