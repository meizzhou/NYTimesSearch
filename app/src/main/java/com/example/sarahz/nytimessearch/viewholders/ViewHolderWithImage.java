package com.example.sarahz.nytimessearch.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sarahz.nytimessearch.R;

/**
 * Created by sarahz on 2/3/17.
 */

public class ViewHolderWithImage extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView tvTitle;
    public TextView tvPub;
    public TextView tvDate;
    public ImageView ivImage;

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public void setIvImage(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolderWithImage(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        tvPub = (TextView) itemView.findViewById(R.id.tvPub);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvImage() {
        return ivImage;
    }
}
