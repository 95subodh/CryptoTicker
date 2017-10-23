package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<WatchlistObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;
    public static WatchlistTab watchlistTab;
    private MyGlobalsFunctions myGlobalsFunctions;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        String cryptoID;
        TextView title;
        TextView currentPrice;
        TextView myChange;
        ImageView icon;
        ImageButton closeBtn;

        public DataObjectHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.watchlist_item_title);
            currentPrice = (TextView) itemView.findViewById(R.id.watchlist_item_current_price);
            myChange = (TextView) itemView.findViewById(R.id.watchlist_item_change);
            icon = (ImageView) itemView.findViewById(R.id.watchlist_item_icon);
            closeBtn = (ImageButton) itemView.findViewById(R.id.close_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StockPageActivity.class);
                    intent.putExtra("cryptoID", "" + cryptoID.toLowerCase());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public WatchlistRecyclerViewAdapter(ArrayList<WatchlistObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        myGlobalsFunctions = new MyGlobalsFunctions(context);
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_card_item_view, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).cryptoID = mDataset.get(position).getCryptoID();
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder)holder).currentPrice.setText(myGlobalsFunctions.commaSeperateInteger(mDataset.get(position).getCurrentPrice()));
        ((DataObjectHolder)holder).myChange.setText(mDataset.get(position).getChange());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());

        final int pos = ((DataObjectHolder)holder).getAdapterPosition();
        ((DataObjectHolder)holder).closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> watchlistitems = myGlobalsFunctions.retrieveListFromFile(context.getString(R.string.crypto_watchlist_file), context.getString(R.string.crypto_watchlist_dir));
                watchlistitems.remove(pos);
                myGlobalsFunctions.storeListToFile( context.getString(R.string.crypto_watchlist_file), context.getString(R.string.crypto_watchlist_dir), watchlistitems);
                deleteItem(pos);
            }
        });

        if (!mDataset.get(position).getChangeColor()) { ((DataObjectHolder)holder).myChange.setTextColor(Color.RED); }
        else { ((DataObjectHolder)holder).myChange.setTextColor(Color.parseColor("#ff99cc00")); }
    }

    private void deleteItem(int index) {
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
