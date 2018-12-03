package com.example.rkjc.news_app_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    Context mContext;
    ArrayList<NewsItem> mNewsItems;

    public NewsRecyclerViewAdapter(Context context, ArrayList<NewsItem> newsItems){
        this.mContext = context;
        this.mNewsItems = newsItems;
    }

    public void setNewsItems(ArrayList<NewsItem> mNewsItems) {
        this.mNewsItems = mNewsItems;
    }

    @Override
    public NewsRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_item, parent, shouldAttachToParentImmediately);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView date;
        ImageView image;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            description = (TextView) itemView.findViewById(R.id.news_description);
            date = (TextView) itemView.findViewById(R.id.news_date);
            image = (ImageView) itemView.findViewById(R.id.news_image);
            Log.d("newsAdapter",   title + " " + description + " " + date);
        }

        void bind(final int listIndex) {
            title.setText(mNewsItems.get(listIndex).getTitle() + " . " + mNewsItems.get(listIndex).getTitle());
            description.setText(mNewsItems.get(listIndex).getPublishedAt() + " . " +  mNewsItems.get(listIndex).getDescription());
            date.setText(mNewsItems.get(listIndex).getPublishedAt());
            Picasso.get().load(mNewsItems.get(listIndex).getUrlToImage()).into(image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Uri urlString = Uri.parse(mNewsItems.get(getAdapterPosition()).getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, urlString);
            if(intent.resolveActivity(mContext.getPackageManager()) != null){
                mContext.startActivity(intent);
            }
        }
    }

}
