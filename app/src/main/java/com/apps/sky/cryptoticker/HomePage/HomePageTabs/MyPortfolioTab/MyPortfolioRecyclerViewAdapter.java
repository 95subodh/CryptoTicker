package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.AddToMyPortfolioFormActivity;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.CryptoTradeObject;
import com.apps.sky.cryptoticker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class MyPortfolioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyPortfolioObject> mDataset;
    private MyPortfolioTab fragment;
    private static Context context;
    private MyGlobalsFunctions myGlobalsFunctions;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        String cryptoID;
        TextView title;
        TextView currentPrice;
        TextView myProfit;
        ImageView icon;
        ImageButton closeBtn;

        private DataObjectHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.my_portfolio_item_title);
            currentPrice = itemView.findViewById(R.id.my_portfolio_item_current_price);
            myProfit = itemView.findViewById(R.id.my_portfolio_item_my_profit);
            icon = itemView.findViewById(R.id.my_portfolio_item_icon);
            closeBtn = itemView.findViewById(R.id.close_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddToMyPortfolioFormActivity.class);
                    intent.putExtra("cryptoID", "" + cryptoID.toLowerCase());
                    intent.putExtra("only_details", true);
                    context.startActivity(intent);
                }
            });
        }
    }

    MyPortfolioRecyclerViewAdapter(ArrayList<MyPortfolioObject> myDataset, MyPortfolioTab fragment) {
        mDataset = myDataset;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        myGlobalsFunctions = new MyGlobalsFunctions(context);
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_portfolio_card_item_view, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).cryptoID = mDataset.get(position).getCryptoID();
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder)holder).currentPrice.setText(myGlobalsFunctions.commaSeperateInteger(mDataset.get(position).getCurrentPrice()));
        ((DataObjectHolder)holder).myProfit.setText(mDataset.get(position).getMyProfit());
        ((DataObjectHolder)holder).icon.setImageBitmap(mDataset.get(position).getIcon());

        final int pos = holder.getAdapterPosition();
        ((DataObjectHolder)holder).closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
            fragment.myPortfolioItems.remove(pos);
            deleteItem(pos);
            String json = gson.toJson(fragment.myPortfolioItems, type);
            myGlobalsFunctions.storeStringToFile(context.getString(R.string.crypto_my_portfolio_file), context.getString(R.string.crypto_my_portfolio_dir), json);
            }
        });
    }

    private void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
