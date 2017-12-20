package com.wmt_demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wmt_demo.R;
import com.wmt_demo.model.Brand;

import java.util.List;

/**
 * Created by Vishal on 17-12-2017.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {

    private Context context;
    private List<Brand> brandList;
    public BrandAdapter(Context context, List<Brand> brandList){
        this.context = context;
        this.brandList = brandList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.brandName.setText(brand.getName());
        holder.description.setText(brand.getDescription());
        holder.createdAt.setText(brand.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView brandName, description, createdAt;

        public MyViewHolder(View itemView) {
            super(itemView);
            brandName = (TextView) itemView.findViewById(R.id.tvBrandName);
            description = (TextView) itemView.findViewById(R.id.tvBrandDescription);
            createdAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);

        }
    }
}
