package com.example.sarahz.nytimessearch.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sarahz.nytimessearch.R;
import com.example.sarahz.nytimessearch.modals.Article;
import com.example.sarahz.nytimessearch.viewholders.ViewHolderWithImage;
import com.example.sarahz.nytimessearch.viewholders.ViewHolderWithText;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sarahz on 1/31/17.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TEXT = 0, IMAGE = 1;

    // define viewholder for our adaptor
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;
        public TextView tvPub;
        public TextView tvDate;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvPub = (TextView) itemView.findViewById(R.id.tvPub);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }

    private List<Article> mArticles;
    private Context mContext;

    public ArticleArrayAdapter(List<Article> articles, Context context) {
        this.mArticles = articles;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

//        View itemView = inflater.inflate(R.layout.item_article_result, parent, false);
//        ViewHolder viewHolder = new ViewHolder(itemView);

        switch (viewType) {
            case TEXT:
                View v1 = inflater.inflate(R.layout.text_viewholder, parent, false);
                viewHolder = new ViewHolderWithText(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.image_viewholder, parent, false);
                viewHolder = new ViewHolderWithImage(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.text_viewholder, parent, false);
                viewHolder = new ViewHolderWithText(v3);
                break;
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXT:
                ViewHolderWithText vh1 = (ViewHolderWithText) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case IMAGE:
                ViewHolderWithImage vh2 = (ViewHolderWithImage) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                ViewHolderWithText vh3 = (ViewHolderWithText) viewHolder;
                configureViewHolder1(vh3, position);
                break;
        }
    }

    private void configureViewHolder1(ViewHolderWithText vh1, int position) {
        Article article = (Article) mArticles.get(position);
        if (article != null) {
            TextView textView = vh1.tvTitle;
            textView.setText(article.getHeadline());

            TextView pub = vh1.tvPub;
            pub.setText(article.getPub());

            TextView date = vh1.tvDate;
            date.setText(article.getDate());
        }
    }

    private void configureViewHolder2(ViewHolderWithImage vh2, int position) {
        Article article = (Article) mArticles.get(position);

        TextView textView = vh2.tvTitle;
        textView.setText(article.getHeadline());

        TextView pub = vh2.tvPub;
        pub.setText(article.getPub());

        TextView date = vh2.tvDate;
        date.setText(article.getDate());

        ImageView imageView = vh2.ivImage;
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (!mArticles.get(position).getThumbnail().isEmpty()) {
            return IMAGE;
        } else {
            return TEXT;
        }
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //get the data item for position
//        Article article = (Article) this.getItem(position);
//
//        // check to see if existing view being reused
//        // not using a recycled view -> inflated the layout
//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
//        }
//        // find the image view
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
//
//        // clear out recycled image from overtView from last time
//
//        imageView.setImageResource(0);
//        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getHeadline());
//
//        String thumbnail = article.getThumbnail();
//        if (!TextUtils.isEmpty(thumbnail)) {
//            Picasso.with(getContext()).load(thumbnail).into(imageView);
//        }
//
//        return convertView;
//    }
}
