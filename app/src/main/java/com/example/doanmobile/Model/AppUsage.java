package com.example.doanmobile.Model;
import android.graphics.drawable.Drawable;

public class AppUsage {
    private String appName;
    private Drawable appIcon;
    private String usageTime;
    private String lasttimeUse;
    private long totalUseTime;
    private long firstInstallTime;

    public long getFirstInstallTime() {
        return firstInstallTime;
    }
    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    private  String  packagename ;
    private  int minSdkVersion;

    public int getSdkVersion() {
        return SdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        SdkVersion = sdkVersion;
    }

    private  int SdkVersion;
    private long time  ;
    private long version ;

    public long getVersion() {

        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public AppUsage() {
    }

    public long getTime() {
        return time;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setUsageTime(String usageTime) {
        this.usageTime = usageTime;
    }



    public String getLasttimeUse() {
        return lasttimeUse;
    }

    public void setLasttimeUse(String lasttimeUse) {
        this.lasttimeUse = lasttimeUse;
    }

    public long getTotalUseTime() {
        return totalUseTime;
    }

    public void setTotalUseTime(long totalUseTime) {
        this.totalUseTime = totalUseTime;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public AppUsage(String appName, Drawable appIcon, String usageTime , long time) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.usageTime = usageTime;

        this.time =time;
    }

    public String getUsageTime() {
        return usageTime;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getUseTime() {
        return usageTime;
    }
}
