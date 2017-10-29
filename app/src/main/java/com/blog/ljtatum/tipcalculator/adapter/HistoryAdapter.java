package com.blog.ljtatum.tipcalculator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.framework.model.HistoryModel;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.utils.Utils;

import java.util.ArrayList;

/**
 * Created by LJTat on 6/11/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<HistoryModel> alTipHistory;

    /**
     * Constructor
     *
     * @param context      Interface to global information about an application environment
     * @param historyModel List of GuideModels {@link com.app.framework.model.HistoryModel}
     */
    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModel) {
        mContext = context;
        alTipHistory = historyModel;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        // set background color
        if (position % 2 == 0) {
            holder.llWrapper.setBackgroundColor(FrameworkUtils.getColor(mContext, R.color.material_light_blue_200_color_code));
        } else {
            holder.llWrapper.setBackgroundColor(FrameworkUtils.getColor(mContext, R.color.material_light_green_200_color_code));
        }

        // set values
        holder.tvItemDayDate.setText(mContext.getResources().getString(R.string.day_date, alTipHistory.get(position).day,
                FrameworkUtils.convertDateFormat(alTipHistory.get(position).date, "MM/dd/yyyy")));
        holder.tvItemQuality.setText(mContext.getResources().getString(R.string.tip_quality,
                Utils.getTipQuality(mContext, Integer.parseInt(alTipHistory.get(position).tipPercent))));
        holder.tvItemTipAmount.setText(mContext.getResources().getString(R.string.tip_amount,
                alTipHistory.get(position).tipAmount));
        holder.tvItemTipPercentage.setText(mContext.getResources().getString(R.string.tip_percent,
                alTipHistory.get(position).tipPercent));
        holder.tvItemBillAmount.setText(mContext.getResources().getString(R.string.bill_amount,
                alTipHistory.get(position).totalBill));
        holder.tvItemLocation.setText(mContext.getResources().getString(R.string.location,
                alTipHistory.get(position).address));
    }

    @Override
    public int getItemCount() {
        return alTipHistory.size();
    }

    /**
     * Method is used to update return trip data
     *
     * @param historyModel List of return trips. Populated from model class
     *                     {@link com.app.framework.model}
     */
    public void updateData(@NonNull ArrayList<HistoryModel> historyModel) {
        if (historyModel.size() > 0 && !historyModel.isEmpty()) {
            alTipHistory = historyModel;
            notifyDataSetChanged();
        }
    }

    /**
     * View holder class
     * <p>A ViewHolder describes an item view and metadata about its place within the RecyclerView</p>
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llWrapper;
        private TextView tvItemDayDate, tvItemQuality, tvItemTipAmount, tvItemTipPercentage,
                tvItemLocation, tvItemBillAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            llWrapper = (LinearLayout) itemView.findViewById(R.id.ll_wrapper);
            tvItemDayDate = (TextView) itemView.findViewById(R.id.tv_item_day_date);
            tvItemQuality = (TextView) itemView.findViewById(R.id.tv_item_quality);
            tvItemTipAmount = (TextView) itemView.findViewById(R.id.tv_item_tip_amount);
            tvItemTipPercentage = (TextView) itemView.findViewById(R.id.tv_item_tip_percentage);
            tvItemLocation = (TextView) itemView.findViewById(R.id.tv_item_location);
            tvItemBillAmount = (TextView) itemView.findViewById(R.id.tv_item_bill_amount);
        }
    }

}
