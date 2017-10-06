package com.apps.sky.cryptoticker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class MyPortfolioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyPortfolioObject> mDataset;
    private static MyClickListener myClickListener;
    private Integer cardType;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView currentPrice;
        TextView myProfit;
        ImageView icon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.myPortfolioItemTitle);
            currentPrice = (TextView) itemView.findViewById(R.id.myPortfolioItemCurrentPrice);
            myProfit = (TextView) itemView.findViewById(R.id.myPortfolioItemMyProfit);
            icon = (ImageView) itemView.findViewById(R.id.myPortfolioItemIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyPortfolioRecyclerViewAdapter(ArrayList<MyPortfolioObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_portfolio_card_item_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder)holder).currentPrice.setText(mDataset.get(position).getCurrentPrice());
        ((DataObjectHolder)holder).myProfit.setText(mDataset.get(position).getMyProfit());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());
    }

    public void addItem(MyPortfolioObject dataObj, int index) {
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
