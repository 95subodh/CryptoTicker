package com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class TradeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TradeObject> mDataset;
    private static Context context;
    private MyGlobalsFunctions myGlobalsFunctions;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        String cryptoID;
        TextView tradeNumber;
        TextView costTextView;
        TextView quantityTextView;
        EditText costInputView;
        EditText quantityInputView;
        ImageButton closeButton;
        ImageButton editButton;

        DataObjectHolder(View itemView) {
            super(itemView);

            tradeNumber = itemView.findViewById(R.id.trade_number_text_view);
            costTextView = itemView.findViewById(R.id.trade_cost_text_view);
            costInputView = itemView.findViewById(R.id.trade_cost_input_view);
            quantityTextView = itemView.findViewById(R.id.trade_quantity_text_view);
            quantityInputView = itemView.findViewById(R.id.trade_quantity_input_view);
            closeButton = itemView.findViewById(R.id.close_btn);
            editButton = itemView.findViewById(R.id.edit_btn);

            itemView.setClickable(false);
        }
    }

    TradeRecyclerViewAdapter(ArrayList<TradeObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        myGlobalsFunctions = new MyGlobalsFunctions(context);
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_details_card_item_view, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((DataObjectHolder)holder).cryptoID = mDataset.get(position).getCryptoID();
        ((DataObjectHolder)holder).tradeNumber.setText(mDataset.get(position).getTradeNumber());
        ((DataObjectHolder)holder).costInputView.setText(mDataset.get(position).getCost());
        ((DataObjectHolder)holder).quantityInputView.setText(mDataset.get(position).getQuantity());

        final int pos = holder.getAdapterPosition();
        ((DataObjectHolder)holder).costInputView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (context instanceof AddToMyPortfolioFormActivity) {
                    if (((DataObjectHolder) holder).costInputView.getText().toString().trim().isEmpty() ||
                            ((DataObjectHolder) holder).quantityInputView.getText().toString().trim().isEmpty()) {
                        ((AddToMyPortfolioFormActivity) context).submit.setEnabled(false);
                        ((AddToMyPortfolioFormActivity) context).addTrade.setEnabled(false);
                    } else {
                        ((AddToMyPortfolioFormActivity) context).submit.setEnabled(true);
                        ((AddToMyPortfolioFormActivity) context).addTrade.setEnabled(true);
                    }
                }
                mDataset.get(pos).setCost(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { mDataset.get(pos).setCost(editable.toString()); }
        });

        ((DataObjectHolder)holder).quantityInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (context instanceof AddToMyPortfolioFormActivity) {
                    if (((DataObjectHolder)holder).costInputView.getText().toString().trim().isEmpty() ||
                            ((DataObjectHolder)holder).quantityInputView.getText().toString().trim().isEmpty()) {
                        ((AddToMyPortfolioFormActivity) context).submit.setEnabled(false);
                        ((AddToMyPortfolioFormActivity) context).addTrade.setEnabled(false);
                    }
                    else {
                        ((AddToMyPortfolioFormActivity) context).submit.setEnabled(true);
                        ((AddToMyPortfolioFormActivity) context).addTrade.setEnabled(true);
                    }
                }

                mDataset.get(pos).setQuantity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { mDataset.get(pos).setQuantity(editable.toString()); }
        });

        ((DataObjectHolder)holder).closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof AddToMyPortfolioFormActivity) {
                    ((AddToMyPortfolioFormActivity) context).deleteItem(pos);
                }
            }
        });

        ((DataObjectHolder)holder).editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ((DataObjectHolder)holder).costInputView.setEnabled(true);
            ((DataObjectHolder)holder).quantityInputView.setEnabled(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
