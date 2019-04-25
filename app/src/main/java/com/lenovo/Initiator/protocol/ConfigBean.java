package com.lenovo.Initiator.protocol;

import java.util.List;

public class ConfigBean {

    private List<AppListBean> appList;
    private Service service;                       // is start the funtion restartpad when wifi not avaliable
    private Test test;                             // Test configuration

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<AppListBean> getAppList() {
        return appList;
    }

    public void setAppList(List<AppListBean> appList) {
        this.appList = appList;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public static class Test{
        private boolean foreCloseAccess;

        public boolean isForeCloseAccess() {
            return foreCloseAccess;
        }

        public void setForeCloseAccess(boolean foreCloseAccess) {
            this.foreCloseAccess = foreCloseAccess;
        }
    }
    public static class Service {
        private boolean restartPadIfBadWifi;
        private boolean log;
        private String logPostfix;
        private boolean gui;

        public boolean isLog() {
            return log;
        }

        public boolean isRestartPadIfBadWifi() {
            return restartPadIfBadWifi;
        }

        public void setRestartPadIfBadWifi(boolean restartPadIfBadWifi) {
            this.restartPadIfBadWifi = restartPadIfBadWifi;
        }

        public boolean getLog() {
            return log;
        }

        public void setLog(boolean log) {
            this.log = log;
        }

        public String getLogPostfix() {
            return logPostfix;
        }

        public void setLogPostfix(String logPostfix) {
            this.logPostfix = logPostfix;
        }

        public boolean isGui() {
            return gui;
        }

        public void setGui(boolean gui) {
            this.gui = gui;
        }
    }

    public static class AppListBean {
        /**
         * appName : billing
         * autoStart : true
         * startAppParam : com.lenovo.billing/com.lenovo.billing.views.activity.MainActivity
         */

        private String appName;
        private boolean autoStart;
        private String startAppParam;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public boolean isAutoStart() {
            return autoStart;
        }

        public void setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
        }

        public String getStartAppParam() {
            return startAppParam;
        }

        public void setStartAppParam(String startAppParam) {
            this.startAppParam = startAppParam;
        }
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "appList=" + appList +
                ", service=" + service +
                '}';
    }
}
