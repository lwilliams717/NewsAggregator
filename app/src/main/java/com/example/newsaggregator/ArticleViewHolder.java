package com.example.newsaggregator;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder{
        TextView headline;
        TextView date;
        TextView author;
        ImageView image;
        TextView articleContent;
        TextView pageNum;

public ArticleViewHolder(@NonNull View itemView){
        super(itemView);
        date = itemView.findViewById(R.id.article_date);
        author = itemView.findViewById(R.id.article_author);
        headline = itemView.findViewById(R.id.article_title);
        image = itemView.findViewById(R.id.article_image);
        articleContent = itemView.findViewById(R.id.article_text);
        pageNum = itemView.findViewById(R.id.article_number);
        }
}
