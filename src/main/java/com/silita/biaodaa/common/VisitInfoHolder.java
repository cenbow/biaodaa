package com.silita.biaodaa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangxiahui on 17/7/26.
 */
public class VisitInfoHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitInfoHolder.class);

    protected static final ThreadLocal<String> userId = new ThreadLocal<>();

    protected static final ThreadLocal<String> uid = new ThreadLocal<>();



    public static String getUserId() {
        return VisitInfoHolder.userId.get();
    }

    public static void setUserId(String userId) {
        VisitInfoHolder.userId.set(userId);
    }

    public static String getUid() {
        return VisitInfoHolder.uid.get();
    }

    public static void setUid(String uid) {
        VisitInfoHolder.uid.set(uid);
    }




}
