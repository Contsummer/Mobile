package com.example.doanmobile.Model;

public class Appdata {
    private String AppName ;
    private String PackageAppName;
    private long WeekStartTime ;
    private long TotalTimeUsed;
    private  int WeekNumber ;

    public String getAppName() {
        return AppName;
    }

    public Appdata(String appName, int weekNumber,  long weekStartTime, long totalTimeUsed, String packageAppName) {
        AppName = appName;
        WeekNumber = weekNumber;
        WeekStartTime = weekStartTime;
        TotalTimeUsed = totalTimeUsed;
        PackageAppName = packageAppName;
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


    public int getWeekNumber() {
        return WeekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        WeekNumber = weekNumber;
    }
}
