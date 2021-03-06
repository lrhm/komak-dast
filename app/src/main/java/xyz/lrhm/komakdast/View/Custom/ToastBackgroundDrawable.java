package xyz.lrhm.komakdast.View.Custom;

import android.content.Context;

import xyz.lrhm.komakdast.R;

public class ToastBackgroundDrawable extends DialogDrawable {
    @Override
    public int getTopResourceId() {
        return R.drawable.toast_top;
    }

    @Override
    public int getCenterResourceId() {
        return R.drawable.toast_center;
    }

    @Override
    public int getBottomResourceId() {
        return R.drawable.toast_buttom;
    }

    public ToastBackgroundDrawable(Context context) {
        super(context);
    }
}
