package com.insping.log;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insping.libra.account.User;

public class LibraLog {

    private final static Logger logger = LoggerFactory.getLogger(LibraLog.class);

    /**
     * 可以动态修改log级别 载入log4j配置文件
     */
    public static void init() {
        PropertyConfigurator.configureAndWatch(System.getProperty("user.dir") + "/config/log4j.properties", 5000L);
    }

    public static void error(Logger logger, String str, Throwable throwable) {
        logger.error(str, throwable);
    }

    public static void error(String str, Throwable throwable) {
        logger.error(str, throwable);
    }

    public static void error(Logger logger, String info) {
        logger.error(info);
    }

    public static void error(String str) {
        logger.error(str);
    }

    public static void error(String resource, Exception ex) {
        String str = ex.getMessage();
        logger.error("<" + resource + ">" + str);
    }

    public static void info(Logger logger, String info) {
        logger.info(info);
    }

    public static void info(String info) {
        logger.info(info);
    }

    public static void info(String info, User user) {
        StringBuffer stringBuffer = new StringBuffer(info);
        if (user == null) {
            stringBuffer.append("user = null");
        } else {
            stringBuffer.append(">>>").append("|uid = ").append(user.getUid()).append("|account = ")
                    .append(user.getAccount()).append("|phoneNumber = ").append(user.getPhoneNumber());
        }
        logger.info(stringBuffer.toString());
    }

    public static void debug(String str) {
        logger.debug(str);
    }

    public static void debug(Logger logger, String info) {
        logger.debug(info);
    }

}
