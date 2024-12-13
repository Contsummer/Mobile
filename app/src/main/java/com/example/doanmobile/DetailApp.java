    package com.example.doanmobile;

    import android.annotation.SuppressLint;
    import android.app.usage.UsageStats;
    import android.app.usage.UsageStatsManager;
    import android.content.Context;
    import android.content.pm.ApplicationInfo;
    import android.content.pm.PackageInfo;
    import android.content.pm.PackageManager;
    import android.graphics.drawable.Drawable;
    import android.media.Image;
    import android.os.Build;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.example.doanmobile.Model.AppUsage;
    import com.example.doanmobile.Model.Appdata;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link DetailApp#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class DetailApp extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        private ImageView app_icon;
        private TextView app_name, totalTime, LastUse, packageName, setSdkVersion, minSdkVersion, firstInstallTime;

        public DetailApp() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailApp.
         */
        // TODO: Rename and change types and number of parameters
        public static DetailApp newInstance(String param1, String param2) {
            DetailApp fragment = new DetailApp();
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail_app, container, false);
            app_icon = rootView.findViewById(R.id.app_icon);
            app_name = rootView.findViewById(R.id.app_name);
            totalTime = rootView.findViewById(R.id.totalTime);
            LastUse = rootView.findViewById(R.id.LastUse);
            packageName = rootView.findViewById(R.id.packageName);
            setSdkVersion = rootView.findViewById(R.id.setSdkVersion);
            minSdkVersion = rootView.findViewById(R.id.minSdkVersion);
            List<Long> time = new ArrayList<Long>();
            Bundle bundle = getArguments();
            String appName = "";
            String package_name = "";
            if (bundle != null) {
                appName = bundle.getString("app_name");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            long day = calendar.getTimeInMillis();
            for (int i = 0; i <= 6; i++) {
                time.add(day);
                day += 86400000;
            }
            List<List<AppUsage>> Bigdata = new ArrayList<>();
            List<AppUsage> tmp = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                tmp = getUsageStatsDetail(time.get(i), time.get(i + 1), appName);
                Bigdata.add(tmp);
            }
            List<Appdata> data = new ArrayList<>();
            Api apiService = RetrofitClient.getApiService();
            Call<List<Appdata>> call  = apiService.getdataname(appName);
             call.enqueue(new Callback<List<Appdata>>() {
                 @Override
                 public void onResponse(Call<List<Appdata>> call, Response<List<Appdata>> response) {
                    if(response.isSuccessful()){
                        Log.d("TAG Successful ", "onResponse: ");
                    }else{
                        Log.d("TAG fail ", "onResponse: ");

                    }

                }

                 @Override
                 public void onFailure(Call<List<Appdata>> call, Throwable t) {

                 }
             });
            calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1);
            long sixMonthAgo = calendar.getTimeInMillis();
            calendar = Calendar.getInstance();
            AppUsage months3 = getUsageStats(sixMonthAgo, calendar.getTimeInMillis(), appName);
            app_icon.setImageDrawable(months3.getAppIcon());
            app_name.setText(appName);
            totalTime.setText("Tổng thời gian sử dụng" + timconverter(months3.getTotalUseTime()));
            LastUse.setText("lần cuối sử dụng : " + months3.getLasttimeUse());
            packageName.setText("Package name : " + months3.getPackagename());
            setSdkVersion.setText("SDKVersionTarget :" + months3.getSdkVersion() + "");
            minSdkVersion.setText("SDKMINVersion :" + months3.getMinSdkVersion() + "");
            return rootView;

        }


        @SuppressLint("NewApi")
        private AppUsage getUsageStats(long start, long end, String appName) {
            UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
            PackageManager pm = getActivity().getPackageManager();
            AppUsage data = new AppUsage();
            Map<String, UsageStats> testdata = usm.queryAndAggregateUsageStats(start, end);

            if (testdata != null && !testdata.isEmpty()) {
                for (Map.Entry<String, UsageStats> entry : testdata.entrySet()) {
                    UsageStats usageStats = entry.getValue();
                    String packageName = entry.getKey();
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
                        ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                        String appLabel = pm.getApplicationLabel(appInfo).toString();
                        if (appName == null || appLabel.equalsIgnoreCase(appName)) {
                            data.setPackagename(packageName);
                            data.setAppName(appLabel);
                            data.setAppIcon(pm.getApplicationIcon(appInfo));
                            data.setLasttimeUse(formatUsageDate(usageStats.getLastTimeVisible()));
                            data.setTotalUseTime(usageStats.getTotalTimeVisible());
                            data.setSdkVersion(appInfo.targetSdkVersion);
                            data.setMinSdkVersion(packageInfo.applicationInfo.minSdkVersion);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

        private String timconverter(long millis) {
            long seconds = (millis / 1000) % 60;
            long minutes = (millis / (1000 * 60)) % 60;
            long hours = (millis / (1000 * 60 * 60)) % 24;
            long days = millis / (1000 * 60 * 60 * 24);

            if (days > 0) {
                return String.format("%d days %02d:%02d:%02d", days, hours, minutes, seconds);
            } else {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }

        private String formatUsageDate(long millis) {
            Date date = new Date(millis);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return formatter.format(date);
        }


        @SuppressLint("NewApi")
        private List<AppUsage> getUsageStatsDetail(long start, long end, String appName) {
            UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
            PackageManager pm = getActivity().getPackageManager();
            List<AppUsage> appUsageList = new ArrayList<>();
            List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);
            if (stats != null && !stats.isEmpty()) {
                for (UsageStats usageStats : stats) {
                    String packageName = usageStats.getPackageName();
                    try {
                        PackageInfo packageInfo = pm.getPackageInfo(getContext().getPackageName(), 0);
                        ApplicationInfo appInfo = packageInfo.applicationInfo;
                        String appLabel = pm.getApplicationLabel(appInfo).toString();
                        if (appName == null || appLabel.equalsIgnoreCase(appName)) {
                            AppUsage data = new AppUsage();
                            data.setAppName(appLabel);
                            data.setAppIcon(pm.getApplicationIcon(appInfo));
                            data.setLasttimeUse(formatUsageDate(usageStats.getLastTimeUsed()));
                            data.setTotalUseTime(usageStats.getTotalTimeInForeground());
                            appUsageList.add(data);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return appUsageList;
        }


        private List<Appdata> getData(int week) {
            List<Appdata> data = new ArrayList<>();
            Api apiService = RetrofitClient.getApiService();
            Call<List<Appdata>> call  = apiService.loadData(week);
            call.enqueue(new Callback<List<Appdata>>() {
                @Override
                public void onResponse(Call<List<Appdata>> call, Response<List<Appdata>> response) {
                    Log.d("tesst reponse", "onResponse: " +response.body());
                }

                @Override
                public void onFailure(Call<List<Appdata>> call, Throwable t) {
                    Log.d("tesst reponse", "onResponse: " );
                }
            });
            return data;



        }
    }