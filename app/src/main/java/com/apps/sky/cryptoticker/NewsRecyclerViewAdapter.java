package com.apps.sky.cryptoticker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsObject> mDataset;
    private static MyClickListener myClickListener;
    private Integer cardType;

    public static class DataObjectHolder1 extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView url;
        TextView publishedAt;
        ImageView img;

        public DataObjectHolder1(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.newsTitle1);
            url = (TextView) itemView.findViewById(R.id.newsUrl1);
            publishedAt = (TextView) itemView.findViewById(R.id.newsDate1);
            img = (ImageView) itemView.findViewById(R.id.newsImageUrl1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public static class DataObjectHolder2 extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView url;
        TextView publishedAt;

        public DataObjectHolder2(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.newsTitle1);
            url = (TextView) itemView.findViewById(R.id.newsUrl1);
            publishedAt = (TextView) itemView.findViewById(R.id.newsDate1);

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

    public NewsRecyclerViewAdapter(ArrayList<NewsObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

//        if (viewType == 0) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.news_card_item_view_2, parent, false);
//            DataObjectHolder2 dataObjectHolder = new DataObjectHolder2(view);
//            return dataObjectHolder;
//        }
//        else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_card_item_view_1, parent, false);
            DataObjectHolder1 dataObjectHolder = new DataObjectHolder1(view);
            return dataObjectHolder;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (mDataset.get(position).getImage() == null) {
//            ((DataObjectHolder2)holder).title.setText(mDataset.get(position).getTitle());
//            ((DataObjectHolder2)holder).url.setText(mDataset.get(position).getURL());
//            ((DataObjectHolder2)holder).publishedAt.setText(mDataset.get(position).getPublishedDate());
//        }
//        else {
            ((DataObjectHolder1)holder).title.setText(mDataset.get(position).getTitle());
            ((DataObjectHolder1)holder).url.setText(mDataset.get(position).getURL());
            ((DataObjectHolder1)holder).publishedAt.setText(mDataset.get(position).getPublishedDate());
            ((DataObjectHolder1)holder).img.setImageBitmap(mDataset.get(position).getImage());
//        if (mDataset.get(position).getImage() == null) {
//            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(0,0);
//            ((DataObjectHolder1)holder).img.setLayoutParams(parms);
//            ((DataObjectHolder1)holder).img.requestLayout();
//        }
//        }
    }

//    @Override
//    public void onBindViewHolder(DataObjectHolder1 holder, int position) {
//        holder.title.setText(mDataset.get(position).getTitle());
//        holder.url.setText(mDataset.get(position).getURL());
//        holder.publishedAt.setText(mDataset.get(position).getPublishedDate());
//        holder.img.setImageBitmap(mDataset.get(position).getImage());
//    }

    public void addItem(NewsObject dataObj, int index) {
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
