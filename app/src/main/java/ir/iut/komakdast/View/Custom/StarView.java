package ir.iut.komakdast.View.Custom;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.SizeConverter;
import ir.iut.komakdast.Util.SizeManager;

/**
 * Created by al on 4/26/16.
 */
public class StarView extends RelativeLayout {

    private ImageView mStarView;
    private ImageView mStarPlaceHolder;
    private Integer mRotateDegree = null;
    private float mSize = 0.15f;

    public StarView(Context context) {
        super(context);

        init(context);
    }


    private void init(Context context) {

        ImageManager imageManager = ImageManager.getInstance(context);

        mStarPlaceHolder = new ImageView(context);
        mStarView = new ImageView(context);

        SizeConverter sizeConverter = SizeConverter.SizeConvertorFromWidth(SizeManager.getScreenWidth() * mSize, 193, 185);
        mStarView.setImageBitmap(imageManager.loadImageFromResource(R.drawable.star, sizeConverter.mWidth, sizeConverter.mHeight, ImageManager.ScalingLogic.FIT));
        mStarPlaceHolder.setImageBitmap(imageManager.loadImageFromResource(R.drawable.starplace, sizeConverter.mWidth, sizeConverter.mHeight, ImageManager.ScalingLogic.FIT));

        addView(mStarPlaceHolder);
        addView(mStarView);


    }

    public void setActive() {
        mStarView.setVisibility(View.VISIBLE);
    }

    public void setDeActivate() {

        mStarView.setVisibility(View.GONE);
    }

    public void rotate(int mRotateDegree) {
        RotateAnimation rotate = new RotateAnimation(0, mRotateDegree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(0);
        rotate.setFillAfter(true);


        startAnimation(rotate);
    }


}
