package com.s23010843.essentialsmonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportHistoryAdapter extends RecyclerView.Adapter<ReportHistoryAdapter.ReportViewHolder> {
    
    private List<PriceReport> reports;
    
    public ReportHistoryAdapter(List<PriceReport> reports) {
        this.reports = reports;
    }
    
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_report_card_item, parent, false);
        return new ReportViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        PriceReport report = reports.get(position);
        
        // Format and display report data
        holder.reportIdTextView.setText("Report #" + report.getId());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", report.getPrice()));
        
        // Format date
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(report.getCreatedAt());
            holder.dateTextView.setText(date != null ? outputFormat.format(date) : "Unknown");
        } catch (Exception e) {
            holder.dateTextView.setText("Unknown");
        }
        
        // Show verification status
        if (report.isVerified()) {
            holder.statusTextView.setText("✓ Verified");
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.statusTextView.setText("⏳ Pending");
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
        }
        
        // Show product and store information
        String productInfo = (report.getProductName() != null ? report.getProductName() : "Unknown Product") + 
                           " at " + (report.getStoreName() != null ? report.getStoreName() : "Unknown Store");
        holder.availabilityTextView.setText(productInfo);
    }
    
    @Override
    public int getItemCount() {
        return reports != null ? reports.size() : 0;
    }
    
    public void updateReports(List<PriceReport> newReports) {
        this.reports = newReports;
        notifyDataSetChanged();
    }
    
    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView reportIdTextView;
        TextView priceTextView;
        TextView dateTextView;
        TextView statusTextView;
        TextView availabilityTextView;
        
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportIdTextView = itemView.findViewById(R.id.report_id_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            availabilityTextView = itemView.findViewById(R.id.availability_text_view);
        }
    }
}