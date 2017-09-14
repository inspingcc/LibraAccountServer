package com.insping.libra.account;

import com.insping.common.utils.StringUtils;
import com.insping.libra.dao.db.DaoData;
import com.insping.libra.dao.db.SqlData;
import com.insping.log.LibraLog;
import org.apache.commons.codec.digest.DigestUtils;

import com.insping.Instances;
import com.insping.common.utils.TimeUtils;

public class User implements DaoData, Instances {
    private long uid;
    private String account;
    private String phoneNumber;
    private String email;
    private String password;
    private String salt;
    private String token;// 与游戏服务器握手的验证
    private long expiresTime;// token的过期时间
    private long registerTime;

    private String name;
    private String icon;
    private byte gender;// 0:男 . 1:女 . 2:保密
    private String address;
    private String introduction;

    public User() {

    }

    /**
     * @param type
     * @param account
     * @param password  采用常见的加密方式 sha1(sha1(password)+salt)
     */
    public User(AccountType type, String account, String password) {
        this.uid = accountMgr.idMake.incrementAndGet();
        switch (type) {
            case COMMON:
                this.account = account;
                break;
            case EMAIL:
                this.email = account;
                break;
            case PHONENUMBER:
                this.phoneNumber = account;
                break;
            default:
                LibraLog.error("new User is error by cauce is type is default");
        }
        this.salt = StringUtils.randomSalt();
        this.password = DigestUtils.sha1Hex(password + salt);
        this.registerTime = TimeUtils.nowLong();
        this.name = "libra_" + uid;
        this.icon = "blue_001";
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public User clone() {
        User user = new User();
        user.setUid(uid);
        user.setAccount(account);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setToken(token);
        user.setExpiresTime(expiresTime);
        user.setRegisterTime(registerTime);

        user.setName(name);
        user.setIcon(icon);
        user.setGender(gender);
        user.setAddress(address);
        user.setIntroduction(introduction);
        return user;
    }

    @Override
    public String table() {
        return DaoData.TABLE_NAME_USER;
    }

    @Override
    public String[] wheres() {
        return new String[]{DaoData.USER_ID};
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public void insertData(SqlData data) {

    }

    /**
     * 保存user数据
     */
    public void save() {
        //// TODO db
        //// 暂时
        //accountMgr.users.put(account, this);
        //accountMgr.accounts.put(account, "");
        dbMgr.getAppDao().saveDaoData(this);
    }

    @Override
    public void loadFromData(SqlData data) {
        uid = data.getLong(DaoData.USER_ID);
        account = data.getString(DaoData.USER_ACCOUNT);
        phoneNumber = data.getString(DaoData.USER_PHONENUMBER);
        email = data.getString(DaoData.USER_EMAIL);
        password = data.getString(DaoData.USER_PASSWORD);
        salt = data.getString(DaoData.USER_SALT);
        token = data.getString(DaoData.USER_TOKEN);
        registerTime = data.getLong(DaoData.USER_REGISTERTIME);
        name = data.getString(DaoData.USER_NAME);
        icon = data.getString(DaoData.USER_ICON);
        gender = data.getByte(DaoData.USER_GENDER);
        address = data.getString(DaoData.USER_ADDRESS);
        introduction = data.getString(DaoData.USER_INTRODUCTION);
    }

    @Override
    public void saveToData(SqlData data) {
        data.put(DaoData.USER_ID, uid);
        data.put(DaoData.USER_ACCOUNT, account);
        data.put(DaoData.USER_PHONENUMBER, phoneNumber);
        data.put(DaoData.USER_EMAIL, email);
        data.put(DaoData.USER_PASSWORD, password);
        data.put(DaoData.USER_SALT, salt);
        data.put(DaoData.USER_TOKEN, token);
        data.put(DaoData.USER_REGISTERTIME, registerTime);
        data.put(DaoData.USER_NAME, name);
        data.put(DaoData.USER_ICON, icon);
        data.put(DaoData.USER_GENDER, gender);
        data.put(DaoData.USER_ADDRESS, address);
        data.put(DaoData.USER_INTRODUCTION, introduction);
    }

    @Override
    public void over() {

    }

    @Override
    public boolean saving() {
        return false;
    }
}
