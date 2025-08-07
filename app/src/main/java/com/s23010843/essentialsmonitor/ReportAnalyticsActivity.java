package com.s23010843.essentialsmonitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReportAnalyticsActivity extends AppCompatActivity {
    private RecyclerView reportHistoryRecyclerView;
    private TextView totalReportsTextView;
    private TextView verifiedReportsTextView;
    
    private DatabaseHelper databaseHelper;
    private List<PriceReport> userReports;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_analytics);
        
        // Check if user is logged in
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        if (!isLoggedIn) {
            Toast.makeText(this, "Please log in to view your reports", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeComponents();
        setupRecyclerView();
        loadUserReports();
    }
    
    private void initializeComponents() {
        databaseHelper = new DatabaseHelper(this);
        reportHistoryRecyclerView = findViewById(R.id.report_history_recycler_view);
        totalReportsTextView = findViewById(R.id.total_reports_text_view);
        verifiedReportsTextView = findViewById(R.id.verified_reports_text_view);
        
        // Set up toolbar if needed
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Reports");
        }
    }
    
    private void setupRecyclerView() {
        reportHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Adapter will be set when data is loaded
    }
    
    private void loadUserReports() {
        try {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String userEmail = prefs.getString("user_email", "");
            
            if (!userEmail.isEmpty()) {
                User user = databaseHelper.getUser(userEmail);
                
                if (user != null) {
                    // Get user's reports (this method needs to be added to DatabaseHelper)
                    userReports = getUserReports(user.getId());
                    
                    // Setup adapter
                    ReportHistoryAdapter adapter = new ReportHistoryAdapter(userReports);
                    reportHistoryRecyclerView.setAdapter(adapter);
                    
                    // Update statistics
                    updateReportStatistics();
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading reports: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private List<PriceReport> getUserReports(int userId) {
        return databaseHelper.getAllPriceReports();
    }
    
    private void updateReportStatistics() {
        if (userReports != null) {
            int totalReports = userReports.size();
            int verifiedReports = 0;
            
            for (PriceReport report : userReports) {
                if (report.isVerified()) {
                    verifiedReports++;
                }
            }
            
            totalReportsTextView.setText("Total Reports: " + totalReports);
            verifiedReportsTextView.setText("Verified Reports: " + verifiedReports);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}