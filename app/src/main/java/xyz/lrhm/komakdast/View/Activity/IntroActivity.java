package xyz.lrhm.komakdast.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.SizeConverter;
import xyz.lrhm.komakdast.Util.SizeManager;
import xyz.lrhm.komakdast.View.Fragment.IntroFragment;

/**
 * Created by al on 5/18/16.
 */
public class IntroActivity extends FragmentActivity implements IntroFragment.OnFragmentButtonListener {


    public static final String INTRO_SHOWN = "komakdast_intro_showed";
    FragmentAdapter adapter;
    IntroFragment[] fragments;
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        SizeManager.initSizes(this);

        ImageView background = findViewById(R.id.background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);

        Glide.with(this).load(R.drawable.package_0_front).into((ImageView) findViewById(R.id.imageView6));

        Glide.with(this).load(R.drawable.package_1_front).into((ImageView) findViewById(R.id.imageView5));

        Glide.with(this).load(R.drawable.package_2_front).into((ImageView) findViewById(R.id.imageView7));


        Button button = findViewById(R.id.button);
        SizeConverter buttonSize = SizeConverter.SizeConvertorFromWidth(SizeManager.getScreenWidth() * 0.4f, 776, 245);


        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        params.width = buttonSize.mWidth;
        params.height = buttonSize.mHeight;
        button.setLayoutParams(params);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putBoolean(INTRO_SHOWN, true);

                Intent intent = new Intent(IntroActivity.this, LoadingActivity.class);

                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void onClick(int id) {
        if (id == 0) {
            pager.setCurrentItem(1, true);
            return;
        }

        Prefs.putBoolean(INTRO_SHOWN, true);

        Intent intent = new Intent(this, LoadingActivity.class);

        startActivity(intent);

        finish();

    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }


}
