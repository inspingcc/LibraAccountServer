package com.insping.libra.sock.net.handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.MessageLite;
import com.insping.Const;
import com.insping.common.utils.XmlUtils;
import com.insping.log.LibraLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HandlerManager {
    private HandlerManager() {

    }

    private static HandlerManager instance = new HandlerManager();

    public static HandlerManager getInstance() {
        return instance;
    }

    /**
     * 解析类对应的业务类的集合
     */
    private Map<String, ServerHandler> handlers = new HashMap<>();

    /**
     * 注册相关解析数据类型
     */
    public final void register() {
        // 注册业务类
        registerHandlers();
    }

    public void registerHandlers() {
        try {
            Document document = XmlUtils.load(System.getProperty("user.dir") + "/config/ServiceHandlers.xml");
            Element element = document.getDocumentElement();
            Element[] elements = XmlUtils.getChildrenByName(element, "ServiceHandler");
            for (int i = 0; i < elements.length; ++i) {
                String code = XmlUtils.getAttribute(elements[i], "orderCode");
                String handlerClassName = XmlUtils.getAttribute(elements[i], "handlerClass");

                Class<? extends ServerHandler> hclazz = (Class<? extends ServerHandler>) Class.forName(handlerClassName);
                ServerHandler handler = hclazz.newInstance();
                if (handler == null || !(handler instanceof ServerHandler)) {
                    LibraLog.info("HandlerManager-registerHandlers : handler is error! code = " + code + "handlerClassName: " + handlerClassName);
                    continue;
                }
                LibraLog.info("register handler: " + handlerClassName + ",code =" + code);
                if (handlers.containsKey(code)) {
                    System.out.println(new Exception("handlers>>>" + handlers.get(code) + "  had  registed by " + code));
                }
                handlers.put(code, handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //
    public void registerByClassName() {
        try {
            for (Class<? extends ServerHandler> c : getAllAssignedClass(ServerHandler.class)) {
                if (c.getSimpleName().length() < 7) {
                    LibraLog.error("HandlerManager-register : invalid handler! continue. className = " + c.getSimpleName());
                    continue;
                }
                String logicName = c.getSimpleName().substring(0, c.getSimpleName().length() - 7).toLowerCase();
                LibraLog.info("HandlerManager-register : handlers register  " + c.getName() + "  had  registed by " + logicName);

                handlers.put(logicName, c.newInstance());
            }
        } catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException e) {
            LibraLog.info("HandlerManager-register : Exception info = " + e.getMessage());
        }
    }

    public List<Class<? extends ServerHandler>> getAllAssignedClass(Class<?> cls)
            throws IOException, ClassNotFoundException {
        List<Class<? extends ServerHandler>> classes = new ArrayList<Class<? extends ServerHandler>>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add((Class<? extends ServerHandler>) c);
            }
        }
        return classes;
    }

    public List<Class<?>> getClasses(Class<?> cls) throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    private List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    /**
     * 根据消息类型,获取对应的业务类
     *
     * @param logicName
     * @return
     */
    public ServerHandler searchHandler(String logicName) {
        return handlers.get(logicName.toLowerCase());
    }

}
