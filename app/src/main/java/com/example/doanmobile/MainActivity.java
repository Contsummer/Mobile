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
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.Model.AppUsage;
import com.example.doanmobile.Model.Appdata;
import com.example.doanmobile.Model.LoginRespon;
import com.example.doanmobile.Model.UsageStatsAdapter;
import com.example.doanmobile.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    if (dayOfWeek == Calendar.MONDAY) {

            calendar.add(Calendar.WEEK_OF_YEAR, -5);
            long starttime =  calendar.getTimeInMillis();

             calendar.add(Calendar.WEEK_OF_YEAR, +2);
            long endtime =  calendar.getTimeInMillis();
           List<Appdata> data =  getUsageStats(starttime,endtime );
        Log.d("Testdata", "onCreate: "  + data.size());
        Collections.sort(data, (a, b) -> Long.compare(b.getTotalTimeUsed(), a.getTotalTimeUsed()));
        data = new ArrayList<>(data.subList(0, 10));
        if(data.size() >0){
            Api apiService = RetrofitClient.getApiService();
            Call<String> call = apiService.save(data);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("Main Response", "onResponse: " + response.body());
                    } else {
                        Log.e("API Error", "Error: " + response.code() + " - " + response.message());
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("API Error", "Failure: " + t.getMessage());
                }
            });
        }
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

    private List<Appdata> getUsageStats(long start, long end) {
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager pm = this.getPackageManager();
        List<Appdata> data = new ArrayList<>();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, start, end);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(start));
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        long totalUsageTime = 0;
        if (stats != null && !stats.isEmpty()) {
            for (UsageStats usageStats : stats) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();
                if (totalTimeInForeground >= 10000) {
                    try {
                        ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) continue;
                        String appName = pm.getApplicationLabel(appInfo).toString();
                        boolean alreadyAdded = false;

                        for (Appdata appUsage : data) {
                            if (appUsage.getAppName().equals(appName)) {
                                alreadyAdded = true;
                                break;
                            }
                        }

                        if (!alreadyAdded) {
                            long minutes = totalTimeInForeground / 60000;
                            long seconds = (totalTimeInForeground % 60000) / 1000;

                            data.add(new Appdata(appName ,weekOfYear, start , totalTimeInForeground  , packageName));
                        }

                        totalUsageTime += totalTimeInForeground;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("TotalUsageTime", String.valueOf(totalUsageTime));
        }

        return data;
    }






}