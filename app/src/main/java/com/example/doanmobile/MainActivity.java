package com.example.doanmobile;

import androidx.core.view.WindowInsetsControllerCompat;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.Model.AppUsage;
import com.example.doanmobile.Model.UsageStatsAdapter;
import com.example.doanmobile.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private UsageStatsAdapter adapter;
    private List<AppUsage> appUsageList;
    private List<List<AppUsage>>data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!isUsagePermissionGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightStatusBars(true);









        replaceFragment(new homeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new homeFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new profileFragment());
            } else if (item.getItemId() == R.id.setting) {
                replaceFragment(new settingFragment());
            }
            return true;
        });
    }
    private void getUsageStats(long start, long end) {
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager pm = getPackageManager();
        long duration = end - start;
        int interval;
        if (duration <= 86400000) {
            interval = UsageStatsManager.INTERVAL_DAILY;
        } else if (duration > 86400000 && duration <= 604800000) {
            interval = UsageStatsManager.INTERVAL_WEEKLY;
        } else if (duration > 604800000 && duration <= 2627136000L) {
            interval = UsageStatsManager.INTERVAL_MONTHLY;
        } else {
            interval = UsageStatsManager.INTERVAL_YEARLY;
        }

        List<UsageStats> stats = usm.queryUsageStats(interval, start, end);
        if (stats != null && !stats.isEmpty()) {
            for (UsageStats usageStats : stats) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();
                if (duration <= 86400000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if ((duration > 86400000 && duration <= 604800000) && totalTimeInForeground >= 90000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if ((duration > 604800000 && duration <= 2627136000L) && totalTimeInForeground >= 600000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if (duration > 2627136000L && totalTimeInForeground >= 900000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private boolean isUsagePermissionGranted() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void addAppUsageToList(String packageName, PackageManager pm, long totalTimeInForeground) {
        try {
         //  if (isSystem(packageName)) return;

            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            String appName = pm.getApplicationLabel(appInfo).toString();
            Drawable appIcon = pm.getApplicationIcon(appInfo);
            long minutes = totalTimeInForeground / 60000;
            long seconds = (totalTimeInForeground % 60000) / 1000;
            String useTime = minutes + " mins " + seconds + " secs";

            appUsageList.add(new AppUsage(appName, appIcon, useTime, totalTimeInForeground));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  List<AppUsage> getUsageStats1(long start, long end) throws PackageManager.NameNotFoundException {
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager pm = getPackageManager();
         List<AppUsage> appUsageList123 =new ArrayList<>();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);
        if (stats != null && !stats.isEmpty()) {
            long min =0;
            long sec =0;
            long duration2  =0;
            String usetime  ="";
            for (UsageStats usageStats : stats) {

                duration2 = usageStats.getTotalTimeInForeground();
                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(usageStats.getPackageName(), 0);
                    String appName = pm.getApplicationLabel(appInfo).toString();
                    Drawable appIcon = pm.getApplicationIcon(appInfo);
                    min= duration2 / 60000;
                    sec  = (duration2 % 60000) / 1000;
                    usetime = min+"p"+sec+"g";
                    appUsageList123.add(new AppUsage(appName,appIcon,usetime,duration2));
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return appUsageList123;
    }






}