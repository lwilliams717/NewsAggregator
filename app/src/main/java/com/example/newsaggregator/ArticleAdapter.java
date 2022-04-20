package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>{
    private static final String TAG = "Adapter";
    private final MainActivity main;
    private final ArrayList<Article> articleList;

    public ArticleAdapter(MainActivity main, ArrayList<Article> articles){
        this.main = main;
        this.articleList = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.article_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article art = articleList.get(position);
        holder.headline.setText(art.getTitle());

        //dateTimeZulu() handles time formatting alr
        //calling the method just puts that as the date for the view page
        holder.date.setText(art.dateTimeZulu());

        holder.author.setText(art.getAuthor());

        holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //Picasso!
        //Picasso.get().setLoggingEnabled(true);

        //Log.d(TAG, "onBindViewHolder: " + art.getUrlToImage());

        Picasso.get().load(art.getUrlToImage().replace("http:", "https:"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.brokenimage)
                .into(holder.image);

        //onclick listener for the picture
        holder.image.setOnClickListener(var -> imageClick(art.getUrl()));

        //sets the scrolling method so the description is scrollable
        holder.articleContent.setMovementMethod(new ScrollingMovementMethod());

        holder.articleContent.setText(art.getDesc());

        //need onclick listeners for the headline and the article text
        holder.headline.setOnClickListener(var -> imageClick(art.getUrl()));
        holder.articleContent.setOnClickListener(var -> imageClick(art.getUrl()));

        //# out of # page number at the bottom
        int ind = position + 1;
        String page = new StringBuilder().append(ind).append(" out of ").append(getItemCount()).toString();

        holder.pageNum.setText(page);
    }
    @Override
    public int getItemCount() {
        return articleList.size();
    }


    private void imageClick(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        main.startActivity(browserIntent);
    }

}
