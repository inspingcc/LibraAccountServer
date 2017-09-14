package com.insping.libra.sock.net.handler.impl;

import java.util.Map;

import com.insping.common.i18.I18nGreeting;
import com.insping.common.utils.JsonUtil;
import com.insping.common.utils.StringUtils;
import com.insping.libra.account.AccountType;
import com.insping.libra.account.User;
import com.insping.libra.sock.net.handler.ServerHandler;
import com.insping.libra.sock.net.response.ReturnObject;
import com.insping.log.LibraLog;

public class RegisterHandler extends ServerHandler {

    @Override
    public void doLogic(ReturnObject resp, Map<String, String> params) throws Exception {
        // 注册
        byte typeNum = Byte.valueOf(params.get("type"));//type 0:普通账号(字母和数字和下划线组成) 1:email账号 2:手机号码注册
        String account = params.get("account");
        String password = params.get("password");
        //String salt = params.get("salt");
        AccountType type = AccountType.searchType(typeNum);
        if (type == null) {
            resp.fail(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        // 检测账号合法性
        if (StringUtils.isNull(account) || StringUtils.isNull(password)) {
            resp.fail(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        // 账号是否已经存在
        if (accountMgr.accountIsExist(type, account)) {
            resp.fail(I18nGreeting.ACCOUNT_ALREADY_EXIST);
            return;
        }
        // 账号格式是否正确
        if (!accountMgr.accountIsValid(type, account)) {
            resp.fail(I18nGreeting.ACCOUNT_FORMAT_ERROR);
            return;
        }
        User user = accountMgr.accountRegister(type, account, password);
        if (user == null) {
            resp.fail(I18nGreeting.HTTP_PARAMS_INVALID);
            return;
        }
        LibraLog.info("RegisterHandler-doLogic : register success ,user:" + JsonUtil.ObjectToJsonString(user));
        resp.success(JsonUtil.ObjectToJsonString(user));
    }
}
