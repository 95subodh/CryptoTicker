package com.apps.pinbit.cryptoticker.StockPage.StockNewsTab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.pinbit.cryptoticker.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsObject> mDataset;
    private static MyClickListener myClickListener;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        String url;
        TextView title;
        TextView author;
        TextView publishedAt;
        ImageView img;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title1);
            author = itemView.findViewById(R.id.news_url1);
            publishedAt = itemView.findViewById(R.id.news_date1);
            img = itemView.findViewById(R.id.news_image_url1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, WebViewActivity.class);
//                    intent.putExtra("url", url);
//                    context.startActivity(intent);
                    new FinestWebView.Builder(context).show(url);
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
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DataObjectHolder) holder).title.setText(mDataset.get(position).getTitle());
        ((DataObjectHolder) holder).author.setText(mDataset.get(position).getAuthor());
        ((DataObjectHolder) holder).url = mDataset.get(position).getURL();
        ((DataObjectHolder) holder).publishedAt.setText(mDataset.get(position).getPublishedDate());
        ((DataObjectHolder) holder).img.setImageDrawable(mDataset.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
