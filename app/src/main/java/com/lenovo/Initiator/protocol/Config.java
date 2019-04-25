package com.lenovo.Initiator.protocol;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static boolean restartPadIfBadWifi = false;
    public static boolean log = false;
    public static String logPostfix;
    public static boolean foreCloseAccess = false;
    public static boolean gui = false;
    public static List<ConfigBean.AppListBean> appList = new ArrayList<>();

    public static void override(ConfigBean configBean) {
        if (configBean == null) return;
        restartPadIfBadWifi = configBean.getService().isRestartPadIfBadWifi();
        appList = configBean.getAppList();
        logPostfix = configBean.getService().getLogPostfix();
        log = configBean.getService().getLog();
        foreCloseAccess = configBean.getTest().isForeCloseAccess();
        gui = configBean.getService().isGui();
    }
}
