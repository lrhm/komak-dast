package ir.iut.komakdast.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.ogaclejapan.smarttablayout.MySmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import ir.iut.komakdast.Adapter.AdItemAdapter;
import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.SizeConverter;
import ir.iut.komakdast.Util.SizeManager;

public class MainFragment extends Fragment {

    public FragmentPagerItemAdapter fragmentPagerItemAdapter;
    public ViewPager viewPager;
    public final static String FRAGMENT_TYPE = "fragment_type";
    AdItemAdapter adItemAdapter;
    public AppBarLayout mAppBarLayout;
    public MySmartTabLayout mSmartTabLayout;
    private ImageManager imageManager;
    ImageView shaderImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        adItemAdapter = new AdItemAdapter(getActivity());
        imageManager = ((MainApplication) getActivity().getApplication()).getImageManager();

        fragmentPagerItemAdapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("    " + "آفلاین" + "    ", PackagesFragment.class, new Bundler().putInt(FRAGMENT_TYPE, 1).get())
                .add("    " + "آنلاین" + "    ", OnlineMenuFragment.class)
                .create()
        );

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerItemAdapter);

        viewPager.setCurrentItem(0);


        mSmartTabLayout = (MySmartTabLayout) view.findViewById(R.id.viewpagertab);
        mSmartTabLayout.setViewPager(viewPager);

        mSmartTabLayout.getLayoutParams().height = (int) (SizeManager.getScreenHeight() * 0.08);


        shaderImageView = (ImageView) view.findViewById(R.id.shadeview);
        SizeConverter shadeConverter = SizeConverter.SizeConvertorFromWidth(SizeManager.getScreenWidth(), 1857, 23);
        shaderImageView.setImageBitmap(imageManager.loadImageFromResource(R.drawable.shade, shadeConverter.mWidth, shadeConverter.mHeight, ImageManager.ScalingLogic.FIT));
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shaderImageView.getLayoutParams();
//        params.topMargin = (int) (SizeManager.getScreenHeight() * 0.08);


        return view;
    }

    public FriendListFragment getFriendListFragment() {
        OnlineMenuFragment onlineMenuFragment = (OnlineMenuFragment) fragmentPagerItemAdapter.getPage(1);
        return onlineMenuFragment.getFriendListFragment();
    }


}
