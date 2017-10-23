package com.blog.ljtatum.tipcalculator.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blog.ljtatum.tipcalculator.R;
import com.app.framework.model.HistoryModel;
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
     * @param context
     * @param historyModel
     */
    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModel) {
        mContext = context;
        alTipHistory = historyModel;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) { //history_item
        holder.tvItemDayDate.setText(mContext.getResources().getString(R.string.day_date,
                alTipHistory.get(position).day, alTipHistory.get(position).date));
        holder.tvItemQuality.setText(mContext.getResources().getString(R.string.tip_quality,
                Utils.getTipQuality(mContext, Integer.parseInt(alTipHistory.get(position).tipPercent))));
        holder.tvItemTipAmount.setText(mContext.getResources().getString(R.string.tip_amount,
                alTipHistory.get(position).tipAmount));
        holder.tvItemTipPercentage.setText(mContext.getResources().getString(R.string.tip_percent,
                alTipHistory.get(position).tipPercent));
        holder.tvItemLocation.setText(mContext.getResources().getString(R.string.location,
                alTipHistory.get(position).featureName));
    }

    @Override
    public int getItemCount() {
        return alTipHistory.size();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemDayDate, tvItemQuality, tvItemTipAmount, tvItemTipPercentage,
                tvItemLocation;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemDayDate = (TextView) itemView.findViewById(R.id.tv_item_day_date);
            tvItemQuality = (TextView) itemView.findViewById(R.id.tv_item_quality);
            tvItemTipAmount = (TextView) itemView.findViewById(R.id.tv_item_tip_amount);
            tvItemTipPercentage = (TextView) itemView.findViewById(R.id.tv_item_tip_percentage);
            tvItemLocation = (TextView) itemView.findViewById(R.id.tv_item_location);
        }
    }

    /**
     * Method is used to update return trip data
     *
     * @param historyModel List of return trips. Populated from model class
     *              {@link com.app.framework.model}
     */
    public void updateData(@NonNull ArrayList<HistoryModel> historyModel) {
        if (historyModel.size() > 0 && !historyModel.isEmpty()) {
            alTipHistory = historyModel;
            notifyDataSetChanged();
        }
    }

}
