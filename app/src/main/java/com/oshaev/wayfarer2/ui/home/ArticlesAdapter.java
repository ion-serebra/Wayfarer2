package com.oshaev.wayfarer2.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oshaev.wayfarer2.Article;
import com.oshaev.wayfarer2.R;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    ArrayList<Article> articles;
    OnArticleClickListener onArticleClickListener;

    public ArticlesAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }




    public interface OnArticleClickListener {
        void onArticleClick(int position);
    }

    public void onArticleClickListener(OnArticleClickListener listener)  {
        this.onArticleClickListener = listener;
    }


    @NonNull
    @Override
    public ArticlesAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.card_articles, parent, false);
        ArticleViewHolder holder = new ArticleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesAdapter.ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.articleTitleTextView.setText(article.getTitle());
        holder.articleSummaryTextView.setText(article.getSummary());

        Glide.with(holder.articleImageView.getContext()).
                load(article.getImgUrl()).into(holder.articleImageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImageView;
        TextView articleTitleTextView;
        TextView articleSummaryTextView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImageView = itemView.findViewById(R.id.articleImageView);
            articleTitleTextView = itemView.findViewById(R.id.articleTitleTextView);
            articleSummaryTextView = itemView.findViewById(R.id.articleSummaryTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(onArticleClickListener!=null)
                    {
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            onArticleClickListener.onArticleClick(position);
                        }
                    }
                }
            });

        }
    }
}
