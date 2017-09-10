package com.blog.ljtatum.tipcalculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.model.HistoryModel;

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
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return alTipHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDayDate, tvViewLocation;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDayDate = (TextView) itemView.findViewById(R.id.tv_day_date);
            tvViewLocation = (TextView) itemView.findViewById(R.id.tv_view_location);
        }
    }




}
