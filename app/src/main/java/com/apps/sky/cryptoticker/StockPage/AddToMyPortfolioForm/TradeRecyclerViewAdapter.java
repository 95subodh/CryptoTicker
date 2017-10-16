package com.apps.sky.cryptoticker.StockPage.AddToMyPortfolioForm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class TradeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TradeObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView tradeNumber;
        TextView costTextView;
        TextView quantityTextView;
        EditText costInputView;
        EditText quantityInputView;
        ImageButton closeButton;

        public DataObjectHolder(View itemView) {
            super(itemView);

            tradeNumber = (TextView) itemView.findViewById(R.id.trade_number_text_view);
            costTextView = (TextView) itemView.findViewById(R.id.trade_cost_text_view);
            costInputView = (EditText) itemView.findViewById(R.id.trade_cost_input_view);
            quantityTextView = (TextView) itemView.findViewById(R.id.trade_quantity_text_view);
            quantityInputView = (EditText) itemView.findViewById(R.id.trade_quantity_input_view);
            closeButton = (ImageButton) itemView.findViewById(R.id.close_btn);

            itemView.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TradeRecyclerViewAdapter(ArrayList<TradeObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_details_card_item_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).tradeNumber.setText(mDataset.get(position).getTradeNumber());
        ((DataObjectHolder)holder).costInputView.setText(mDataset.get(position).getCost());
        ((DataObjectHolder)holder).quantityInputView.setText(mDataset.get(position).getQuantity());
    }

    public void addItem(TradeObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
