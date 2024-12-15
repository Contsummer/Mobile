package com.example.doanmobile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.doanmobile.Model.AppUsage;
import com.example.doanmobile.Model.UsageStatsAdapterMini;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class homeFragment extends Fragment {

    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private Button date_picker, date_picker2, button3;
    private TextView textView5 ;
    private long starttime, endtime;
    private UsageStatsAdapterMini adapter;
    private RecyclerView recyclerView1 ;
    public homeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        date_picker = rootView.findViewById(R.id.date_picker);
        date_picker2 = rootView.findViewById(R.id.date_picker2);
        button3 = rootView.findViewById(R.id.button3);
        recyclerView1 =rootView.findViewById(R.id.recyclerView1);
        textView5 =rootView.findViewById(R.id.textView5);
        AnyChartView anyChartView = rootView.findViewById(R.id.any_chart_view);
        initDatePicker();
        initDatePicker2();
        initDate();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date1 = makeDateString(day , month + 1, year);
        textView5.setText(date1);
        long Now = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        long WeekBefforre = calendar.getTimeInMillis();
        List<DataEntry> datachart = new ArrayList<>();
        List<AppUsage> data = getUsageStats(WeekBefforre,Now);
        Collections.sort(data, (a, b) -> Long.compare(b.getTime(), a.getTime()));
        data = new ArrayList<>(data.subList(0, 10));

        for (AppUsage appUsage : data) {
             datachart.add(new ValueDataEntry(appUsage.getAppName(),appUsage.getTime()/1000/60 ));
            }
        date_picker.setOnClickListener(v -> datePickerDialog1.show());
        date_picker2.setOnClickListener(v -> datePickerDialog2.show());
        button3.setOnClickListener(v -> sendData());
        Cartesian cartesian = AnyChart.column();
        Column column = cartesian.column(datachart);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: } Mins");
        cartesian.data(datachart);
        anyChartView.setChart(cartesian);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("App");
        cartesian.xAxis(0).title().fontColor("White");
        cartesian.yAxis(0).title("Mins");
        cartesian.yAxis(0).title().fontWeight(700);
        cartesian.xAxis(0).title().fontWeight(700);
        cartesian.yAxis(0).title().fontColor("White");
        cartesian.xAxis(0).labels().fontColor("White");
        cartesian.yAxis(0).labels().fontColor("White");
        cartesian.yAxis(0).labels().fontWeight(700);
        cartesian.xAxis(0).labels().fontWeight(700);
        cartesian.xScroller(true);
        column.color("#213d90");
        cartesian.background().fill("#202042");
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsageStatsAdapterMini(data);
        recyclerView1.setAdapter(adapter);
        return rootView;
    }
    private List<AppUsage> getUsageStats(long start, long end) {
        UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager pm = getActivity().getPackageManager();
        List<AppUsage> data = new ArrayList<>();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, start, end);
        long timeer = 0 ;
        if (stats != null && !stats.isEmpty()) {
            for (UsageStats usageStats : stats) {
                String packageName = usageStats.getPackageName();
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();
                if (totalTimeInForeground >= 10000) {
                    addAppUsageToList(packageName, pm, totalTimeInForeground,data);
                    timeer+=totalTimeInForeground;
                }
            }
            Log.d("timeer",  String.valueOf(timeer));
            return  data;
        }
        return  new ArrayList<>();
    }
    private void addAppUsageToList(String packageName, PackageManager pm, long totalTimeInForeground, List<AppUsage> data) {
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) return;
            String appName = pm.getApplicationLabel(appInfo).toString();
            for (AppUsage appUsage : data) {
                if (appUsage.getAppName().equals(appName)) {
                    return;
                }
            }
            Drawable appIcon = pm.getApplicationIcon(appInfo);
            long minutes = totalTimeInForeground / 60000;
            long seconds = (totalTimeInForeground % 60000) / 1000;
            String useTime = minutes + " mins " + seconds + " secs";
            data.add(new AppUsage(appName, appIcon, useTime, totalTimeInForeground));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
















    private void initDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date1 = makeDateString(day - 1, month + 1, year);
        String date2 = makeDateString(day, month + 1, year);
        date_picker.setText(date1);
        date_picker2.setText(date2);

        endtime = cal.getTimeInMillis();
        starttime = cal.getTimeInMillis() - 86400000;
    }

    // Hàm khởi tạo DatePicker
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            date_picker.setText(date);
            starttime = convertToEpoch(day, month, year); // Chuyển ngày thành thời gian Epoch
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog1 = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            date_picker2.setText(date);
            endtime = convertToEpoch(day, month, year);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog2 = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public static long convertToEpoch(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    private String makeDateString(int day, int month, int year) {
        return String.format("%02d/%02d/%d", day, month, year);
    }

    private void sendData() {
        if (endtime - starttime < 0) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Lỗi dữ liệu")
                    .setMessage("Thời gian không hợp lệ. Vui lòng kiểm tra lại!")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
            initDate();
        } else {
            profileFragment profile = new profileFragment();
            Log.d("endtime", String.valueOf(endtime));
            Log.d("start", String.valueOf(starttime));
            Bundle bundle = new Bundle();
            bundle.putLong("endtime", endtime);
            bundle.putLong("start", starttime);
            profile.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, profile);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }







}
