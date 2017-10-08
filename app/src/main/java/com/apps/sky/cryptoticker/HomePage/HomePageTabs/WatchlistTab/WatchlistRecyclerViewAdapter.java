package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<WatchlistObject> mDataset;
    private static MyClickListener myClickListener;
    private Integer cardType;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView currentPrice;
        TextView myChange;
        ImageView icon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.watchlistItemTitle);
            currentPrice = (TextView) itemView.findViewById(R.id.watchlistItemCurrentPrice);
            myChange = (TextView) itemView.findViewById(R.id.watchlistItemChange);
            icon = (ImageView) itemView.findViewById(R.id.watchlistItemIcon);

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

    public WatchlistRecyclerViewAdapter(ArrayList<WatchlistObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_card_item_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder)holder).currentPrice.setText(mDataset.get(position).getCurrentPrice());
        ((DataObjectHolder)holder).myChange.setText(mDataset.get(position).getChange());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());
    }

    public void addItem(WatchlistObject dataObj, int index) {
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
