package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<WatchlistObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        String crypto;
        TextView title;
        TextView currentPrice;
        TextView myChange;
        ImageView icon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.watchlist_item_title);
            currentPrice = (TextView) itemView.findViewById(R.id.watchlist_item_current_price);
            myChange = (TextView) itemView.findViewById(R.id.watchlist_item_change);
            icon = (ImageView) itemView.findViewById(R.id.watchlist_item_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StockPageActivity.class);
                    intent.putExtra("crypto", "" + crypto.toLowerCase());
                    context.startActivity(intent);
                }
            });
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

        context = parent.getContext();
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_card_item_view, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    private String commaSeperateInteger(String num){
        return NumberFormat.getNumberInstance(Locale.US).format(Float.valueOf(num));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).crypto = mDataset.get(position).getCrypto();
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder)holder).currentPrice.setText(commaSeperateInteger(mDataset.get(position).getCurrentPrice()));
        ((DataObjectHolder)holder).myChange.setText(mDataset.get(position).getChange());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());

        if (mDataset.get(position).getChangeColor() == false) {
            ((DataObjectHolder)holder).myChange.setTextColor(Color.RED);
        }
        else {
            ((DataObjectHolder)holder).myChange.setTextColor(Color.parseColor("#ff99cc00"));
        }
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
