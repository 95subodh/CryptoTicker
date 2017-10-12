package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.content.Context;
import android.content.Intent;
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

public class MyPortfolioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyPortfolioObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        String crypto;
        TextView title;
        TextView currentPrice;
        TextView myProfit;
        ImageView icon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.my_portfolio_item_title);
            currentPrice = (TextView) itemView.findViewById(R.id.my_portfolio_item_current_price);
            myProfit = (TextView) itemView.findViewById(R.id.my_portfolio_item_my_profit);
            icon = (ImageView) itemView.findViewById(R.id.my_portfolio_item_icon);

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

    public MyPortfolioRecyclerViewAdapter(ArrayList<MyPortfolioObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_portfolio_card_item_view, parent, false);
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
