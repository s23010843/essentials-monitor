package com.s23010843.essentialsmonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private final List<Product> products;
    private final String[] cardColors = {"#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FECA57", "#FF9FF3", "#54A0FF"};
    
    public ProductAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }
    
    @SuppressLint("NotifyDataSetChanged")
    public void updateProducts(List<Product> newProducts) {
        this.products.clear();
        this.products.addAll(newProducts);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_product_card_item, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }
    
    @Override
    public int getItemCount() {
        return products.size();
    }
    
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView productNameTextView;
        private final TextView categoryTextView;
        private final TextView brandTextView;
        private final TextView descriptionTextView;
        private final ImageView productImageView;
        private final CardView cardView;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            categoryTextView = itemView.findViewById(R.id.category_text_view);
            brandTextView = itemView.findViewById(R.id.brand_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            productImageView = itemView.findViewById(R.id.product_image_view);
            cardView = itemView.findViewById(R.id.product_card_view);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = products.get(position);
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_id", product.getId());
                    context.startActivity(intent);
                }
            });
        }
        
        public void bind(Product product) {
            productNameTextView.setText(product.getName());
            categoryTextView.setText(product.getCategory());
            brandTextView.setText(product.getBrand());
            descriptionTextView.setText(product.getDescription());
            
            // Set colorful card background
            int position = getAdapterPosition();
            int colorIndex = position % cardColors.length;
            cardView.setCardBackgroundColor(Color.parseColor(cardColors[colorIndex]));
            
            // Load product image
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                productImageView.setImageResource(R.drawable.ic_product_placeholder);
                productImageView.setVisibility(View.VISIBLE);
            } else {
                productImageView.setVisibility(View.GONE);
            }
        }
    }
}
