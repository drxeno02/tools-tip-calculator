package com.blog.ljtatum.tipcalculator.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
     * @param context
     * @param guideModel
     */
    public GuideAdapter(Context context, ArrayList<GuideModel> guideModel) {
        mContext = context;
        alGuide = guideModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_guide, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivFlag.setImageDrawable(ContextCompat.getDrawable(mContext, alGuide.get(position).countryFlagIcon));
        if (position % 2 == 0) {
            holder.ivFlag.setBorderColor(ContextCompat.getColor(mContext, R.color.orange_light));
            holder.ivFlag.setBorderWidth(8);
        } else {
            holder.ivFlag.setBorderColor(ContextCompat.getColor(mContext, R.color.purple_light));
            holder.ivFlag.setBorderWidth(8);
        }
        holder.tvCountryName.setText(alGuide.get(position).countryName);
        holder.tvCountryReq.setText(alGuide.get(position).countryReq);
        holder.tvCountryDesc.setText(alGuide.get(position).ccountryDesc);
    }

    @Override
    public int getItemCount() {
        return alGuide.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivFlag;
        private TextView tvCountryName, tvCountryReq, tvCountryDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFlag = (CircleImageView) itemView.findViewById(R.id.iv_flag);
            tvCountryName = (TextView) itemView.findViewById(R.id.tv_guide_country);
            tvCountryReq = (TextView) itemView.findViewById(R.id.tv_guide_tip_req);
            tvCountryDesc = (TextView) itemView.findViewById(R.id.tv_guide_desc);
        }
    }
}
