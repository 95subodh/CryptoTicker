package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;
    private static String url;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView author;
        TextView publishedAt;
        ImageView img;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title1);
            author = (TextView) itemView.findViewById(R.id.news_url1);
            publishedAt = (TextView) itemView.findViewById(R.id.news_date1);
            img = (ImageView) itemView.findViewById(R.id.news_image_url1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", url.toString());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public NewsRecyclerViewAdapter(ArrayList<NewsObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_card_item_view_1, parent, false);
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder)holder).title.setText(mDataset.get(position).getTitle());
            ((DataObjectHolder)holder).author.setText(mDataset.get(position).getAuthor());
            url = mDataset.get(position).getURL();
            ((DataObjectHolder)holder).publishedAt.setText(mDataset.get(position).getPublishedDate());
            ((DataObjectHolder)holder).img.setImageBitmap(mDataset.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
