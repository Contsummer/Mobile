package com.example.doanmobile.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;

import java.util.List;

public class UsageStatsAdapter extends RecyclerView.Adapter<UsageStatsAdapter.ViewHolder> {

    private List<AppUsage> appUsageList;
    private OnItemClickListener onItemClickListener;
    public UsageStatsAdapter(List<AppUsage> appUsageList, OnItemClickListener onItemClickListener) {
        this.appUsageList = appUsageList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usage_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppUsage appUsage = appUsageList.get(position);
        holder.appName.setText(appUsage.getAppName());
        holder.appIcon.setImageDrawable(appUsage.getAppIcon());
        holder.useTime.setText(appUsage.getUseTime());
        holder.packagename = appUsage.getPackagename();
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(appUsage));
    }

    @Override
    public int getItemCount() {
        return appUsageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         String packagename;
        TextView appName, useTime;
        ImageView appIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            useTime = itemView.findViewById(R.id.app_usage_info);
            appIcon = itemView.findViewById(R.id.app_icon);
        }
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(AppUsage appUsage);
    }
}
