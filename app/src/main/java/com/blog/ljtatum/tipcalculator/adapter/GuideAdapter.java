package com.blog.ljtatum.tipcalculator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.gui.CircleImageView;
import com.blog.ljtatum.tipcalculator.model.GuideModel;

import java.util.ArrayList;

/**
 * Created by LJTat on 4/1/2017.
 */
public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GuideModel> alGuide;

    /**
     * Constructor
     *
     * @param context    Interface to global information about an application environment
     * @param guideModel List of GuideModels {@link com.blog.ljtatum.tipcalculator.model.GuideModel}
     */
    public GuideAdapter(@NonNull Context context, @NonNull ArrayList<GuideModel> guideModel) {
        mContext = context;
        alGuide = guideModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_guide, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivFlag.setImageDrawable(ContextCompat.getDrawable(mContext, alGuide.get(position).countryFlagIcon));
        if (position % 2 == 0) {
            holder.ivFlag.setBorderColor(ContextCompat.getColor(mContext, R.color.material_deep_orange_300_color_code));
            holder.ivFlag.setBorderWidth(10);
        } else {
            holder.ivFlag.setBorderColor(ContextCompat.getColor(mContext, R.color.material_purple_300_color_code));
            holder.ivFlag.setBorderWidth(10);
        }
        holder.tvCountryName.setText(alGuide.get(position).countryName);
        holder.tvCountryReq.setText(alGuide.get(position).countryReq);
        holder.tvCountryDesc.setText(alGuide.get(position).countryDesc);
    }

    @Override
    public int getItemCount() {
        return alGuide.size();
    }

    /**
     * View holder class
     * <p>A ViewHolder describes an item view and metadata about its place within the RecyclerView</p>
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivFlag;
        private TextView tvCountryName, tvCountryReq, tvCountryDesc;

        ViewHolder(View itemView) {
            super(itemView);

            ivFlag = itemView.findViewById(R.id.iv_flag);
            tvCountryName = itemView.findViewById(R.id.tv_guide_country);
            tvCountryReq = itemView.findViewById(R.id.tv_guide_tip_req);
            tvCountryDesc = itemView.findViewById(R.id.tv_guide_desc);
        }
    }
}
