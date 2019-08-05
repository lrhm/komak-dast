package ir.treeco.aftabe2.View.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pixplicity.easyprefs.library.Prefs;

import ir.treeco.aftabe2.R;
import ir.treeco.aftabe2.Util.SizeManager;
import ir.treeco.aftabe2.View.Fragment.IntroFragment;

/**
 * Created by al on 5/18/16.
 */
public class IntroActivity extends FragmentActivity implements IntroFragment.OnFragmentButtonListener {


    public static final String INTRO_SHOWN = "aftabe_intro_showed";
    FragmentAdapter adapter;
    IntroFragment[] fragments;
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        SizeManager.initSizes(this);

        pager = (ViewPager) findViewById(R.id.intro_activity_pager);


        fragments = new IntroFragment[2];

        fragments[1] = IntroFragment.getInstance(R.drawable.intro_one, 1);
        fragments[0] = IntroFragment.getInstance(R.drawable.intro_two, 0);

        fragments[0].setOnFragmentButtonListner(this);
        fragments[1].setOnFragmentButtonListner(this);

        adapter = new FragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


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
