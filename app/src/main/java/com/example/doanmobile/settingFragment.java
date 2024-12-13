package com.example.doanmobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView ipv6,ipv4,speed,brand,combanyName,Sdkversion,Osversion,phoneName;


    public settingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingFragment newInstance(String param1, String param2) {
        settingFragment fragment = new settingFragment();
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

    @SuppressLint({"MissingInflatedId", "NewApi"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ipv6 = rootView.findViewById(R.id.ipv6);
        ipv4 = rootView.findViewById(R.id.ipv4);
        speed = rootView.findViewById(R.id.speed);
        brand = rootView.findViewById(R.id.brand);
        combanyName = rootView.findViewById(R.id.combanyName);
        Sdkversion = rootView.findViewById(R.id.Sdkversion);
        Osversion = rootView.findViewById(R.id.Osversion);
        phoneName = rootView.findViewById(R.id.phoneName);
        String deviceName = Build.MODEL;
        String deviceNameString = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        @SuppressLint({"NewApi", "LocalSuppress"}) String osVersion = Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
        int sdkVersion = Build.VERSION.SDK_INT;
        String deviceId = Build.ID;
        String brand1 = Build.BRAND;
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)  // Yêu cầu mạng có khả năng kết nối Internet
                .build();
        Network network = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (network == null) {
            Log.d("WiFi Info", "Không có kết nối mạng.");
        }else{
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        LinkProperties linkProperties = cm.getLinkProperties(network);
        phoneName.setText("Tên Điện thoại :"+deviceNameString);
        brand.setText("Thương liệu :"  +brand1);
        Osversion.setText("Phiên bản andorid:  "+ osVersion);
        Sdkversion.setText("Phiên bản SDK:  "+ sdkVersion);
        combanyName.setText("Công ty sản xuất : "+manufacturer);
        speed.setText("tốc độ mang : "+ wifiInfo.getLinkSpeed()+" Mbpds");
            StringBuilder ipv4s = new StringBuilder();
            StringBuilder ipv6s = new StringBuilder();
            extractFirstIP(linkProperties.getLinkAddresses().toString(),ipv4s,ipv6s);
            ipv4.setText("IPV4: "+ipv4s);
            ipv6.setText("IPV6: "+ipv6s);
        }


        return rootView ;
    }







    public static void extractFirstIP(String ipList, StringBuilder ipv4Result, StringBuilder ipv6Result) {
        // Regex để nhận diện IPv4 và IPv6
        String ipv4Pattern = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}(?:/\\d+)?\\b";
        String ipv6Pattern = "\\b(?:[a-fA-F0-9:]+:+)+[a-fA-F0-9]+(?:/\\d+)?\\b";

        // Tìm IPv4
        Matcher ipv4Matcher = Pattern.compile(ipv4Pattern).matcher(ipList);
        if (ipv4Matcher.find()) {
            ipv4Result.append(ipv4Matcher.group());
        }

        Matcher ipv6Matcher = Pattern.compile(ipv6Pattern).matcher(ipList);
        if (ipv6Matcher.find()) {
            ipv6Result.append(ipv6Matcher.group());
        }
    }

    public static class RetrofitClient1 {


    }
}


