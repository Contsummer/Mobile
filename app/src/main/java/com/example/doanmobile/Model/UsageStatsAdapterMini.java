package com.example.doanmobile.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;

import java.util.List;

public class UsageStatsAdapterMini extends RecyclerView.Adapter<UsageStatsAdapterMini.ViewHolder> {

    private List<AppUsage> appUsageList;

    public UsageStatsAdapterMini(List<AppUsage> appUsageList) {
        this.appUsageList = appUsageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usage_stats_mini, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppUsage appUsage = appUsageList.get(position);
        holder.appName.setText(appUsage.getAppName());
        holder.appIcon.setImageDrawable(appUsage.getAppIcon());
        holder.useTime.setText(appUsage.getUseTime());
    }

    @Override
    public int getItemCount() {
        return appUsageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView appName, useTime;
        ImageView appIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            useTime = itemView.findViewById(R.id.app_usage_info);
            appIcon = itemView.findViewById(R.id.app_icon);
        }
    }
}
