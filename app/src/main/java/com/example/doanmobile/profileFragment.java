package com.example.doanmobile;
import static com.example.doanmobile.R.id.recyclerView;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doanmobile.Model.AppUsage;
import com.example.doanmobile.Model.UsageStatsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment implements UsageStatsAdapter.OnItemClickListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TextView textView3 ;
    private UsageStatsAdapter adapter;
    private List<AppUsage> appUsageList;

    private long     starttime ;
    private long     endtime ;
    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView3 = view.findViewById(R.id.textView3);
        if (bundle != null) {
             starttime = bundle.getLong("start");
             endtime = bundle.getLong("endtime");
            String text = "Từ " + convertEpochToDate(starttime)   +"->" + convertEpochToDate(endtime) ;
            textView3.setText(text);
        }else{
            Calendar cal = Calendar.getInstance();
            endtime = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            starttime = cal.getTimeInMillis()  ;
            textView3.setText("Các ứng dụng dùng hôm nay ");
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appUsageList = new ArrayList<>();
        getUsageStats(starttime,endtime) ;
        adapter = new UsageStatsAdapter(appUsageList,this);
        recyclerView.setAdapter(adapter);
        return view ;
    }
    @Override
    public void onItemClick(AppUsage appUsage) {

        Bundle bundle = new Bundle();
        bundle.putString("app_name", appUsage.getAppName());
        bundle.putString("package_name",appUsage.getPackagename());
        DetailApp detailApp = new DetailApp();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        detailApp.setArguments(bundle);
        transaction.replace(R.id.frame_layout,detailApp);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    public static String convertEpochToDate(long epochTime) {
        Date date = new Date(epochTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }



    private void getUsageStats(long start, long end) {
        UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager pm = getActivity().getPackageManager();
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
                if (duration <= 86400000 && totalTimeInForeground >= 10000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if ((duration > 86400000 && duration <= 604800000) && totalTimeInForeground >= 90000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if ((duration > 604800000 && duration <= 2627136000L) && totalTimeInForeground >= 600000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                } else if (duration > 2627136000L && totalTimeInForeground >= 900000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground);
                }
            }

            Collections.sort(appUsageList, (a, b) -> Long.compare(b.getTime(), a.getTime()));
            if (appUsageList.size() > 20) appUsageList = new ArrayList<>(appUsageList.subList(0, 20));
            Log.d("appUsageListsuzer"  , String.valueOf(appUsageList.size()));
        }
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
            AppUsage test = new AppUsage();
            test.setAppName(appName);
            test.setAppIcon(appIcon);
            test.setUsageTime(useTime);
            test.setTime(totalTimeInForeground);
            test.setPackagename(packageName);
            appUsageList.add(new AppUsage(appName, appIcon, useTime, totalTimeInForeground));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


















}






























