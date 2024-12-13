package com.example.doanmobile.Model;

public class Appdata {
    private String AppName ;
    private String PackageAppName;
    private long WeekStartTime ;
    private long TotalTimeUsed;
    private  int LaunchCount ;
    private  int WeekNumber ;

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPackageAppName() {
        return PackageAppName;
    }

    public void setPackageAppName(String packageAppName) {
        PackageAppName = packageAppName;
    }

    public long getWeekStartTime() {
        return WeekStartTime;
    }

    public void setWeekStartTime(long weekStartTime) {
        WeekStartTime = weekStartTime;
    }

    public long getTotalTimeUsed() {
        return TotalTimeUsed;
    }

    public void setTotalTimeUsed(long totalTimeUsed) {
        TotalTimeUsed = totalTimeUsed;
    }

    public int getLaunchCount() {
        return LaunchCount;
    }

    public void setLaunchCount(int launchCount) {
        LaunchCount = launchCount;
    }

    public int getWeekNumber() {
        return WeekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        WeekNumber = weekNumber;
    }
}
