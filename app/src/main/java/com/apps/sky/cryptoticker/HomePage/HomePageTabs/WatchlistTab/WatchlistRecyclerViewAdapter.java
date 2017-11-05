package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<WatchlistObject> mDataset;
    private WatchlistTab fragment;
    private static Context context;
    private MyGlobalsFunctions myGlobalsFunctions;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        String cryptoID;
        TextView title;
        TextView currentPrice;
        TextView myChange;
        TextView maxDayPrice;
        TextView minDayPrice;
        ImageView icon;
        ImageButton closeBtn;

        DataObjectHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.watchlist_item_title);
            currentPrice = itemView.findViewById(R.id.watchlist_item_current_price);
            myChange = itemView.findViewById(R.id.watchlist_item_change);
            icon = itemView.findViewById(R.id.watchlist_item_icon);
            closeBtn = itemView.findViewById(R.id.close_btn);
            minDayPrice = itemView.findViewById(R.id.watchlist_item_low);
            maxDayPrice = itemView.findViewById(R.id.watchlist_item_high);

//            RelativeLayout priceView = itemView.findViewById(R.id.price_view);
//            RelativeLayout highLowView = itemView.findViewById(R.id.high_low_view);
//            RelativeLayout lowerLayout = itemView.findViewById(R.id.lower_layout);
//            priceView.setW

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StockPageActivity.class);
                    intent.putExtra("cryptoID", "" + cryptoID.toLowerCase());
                    context.startActivity(intent);
                }
            });
        }
    }

    WatchlistRecyclerViewAdapter(ArrayList<WatchlistObject> myDataset, WatchlistTab fragment) {
        mDataset = myDataset;
        this.fragment = fragment;
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
        ((DataObjectHolder)holder).currentPrice.setText(myGlobalsFunctions.commaSeperateInteger2(mDataset.get(position).getCurrentPrice()));
        ((DataObjectHolder)holder).myChange.setText(mDataset.get(position).getChange());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());
        ((DataObjectHolder)holder).maxDayPrice.setText(myGlobalsFunctions.commaSeperateInteger2(mDataset.get(position).getMaxDayPrice()));
        ((DataObjectHolder)holder).minDayPrice.setText(myGlobalsFunctions.commaSeperateInteger2(mDataset.get(position).getMinDayPrice()));

        final int pos = holder.getAdapterPosition();
        ((DataObjectHolder)holder).closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.items.remove(pos);
                myGlobalsFunctions.storeListToFile( context.getString(R.string.crypto_watchlist_file), context.getString(R.string.crypto_watchlist_dir), fragment.items);
                deleteItem(pos);
            }
        });

        if (!mDataset.get(position).getChangeColor()) { ((DataObjectHolder)holder).myChange.setTextColor(context.getResources().getColor(R.color.valueNegative)); }
        else { ((DataObjectHolder)holder).myChange.setTextColor(context.getResources().getColor(R.color.valuePositive)); }
    }

    private void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, getItemCount() - index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
