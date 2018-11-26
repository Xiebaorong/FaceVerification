package com.example.zd_x.faceverification.utils.Glide;

import android.content.Context;
import android.widget.ImageView;

public class FaceverificationGlide {
    public static void loadInto(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .fitCenter()
                .placeholder(null)
                .into(imageView);
    }
}
