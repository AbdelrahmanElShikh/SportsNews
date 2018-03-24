package com.example.android.footballnews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bodiy_000 on 02-Mar-18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;
    private Context context;

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public List<News> getNewsList() {
        return newsList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News newsItem = newsList.get(position);
        holder.title.setText(newsItem.getArticleTitle());
        holder.date.setText(newsItem.getArticleDate());
        holder.sectionName.setText(newsItem.getSectionName());
        holder.autorName.setText(newsItem.getAuthorName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(newsItem.getWebUrl()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView sectionName;
        TextView autorName;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            date = itemView.findViewById(R.id.date);
            sectionName = itemView.findViewById(R.id.sectionName);
            autorName = itemView.findViewById(R.id.autorNAme);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }

    public void clear() {
        final int size = newsList.size();
        newsList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void swap(List<News> news) {
        if (news == null || news.size() == 0)
            return;
        if (newsList != null && newsList.size() > 0)
            newsList.clear();
        newsList.addAll(news);
        notifyDataSetChanged();
    }
}
