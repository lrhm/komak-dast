package xyz.lrhm.komakdast.View.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import xyz.lrhm.komakdast.API.Rest.AppAPIAdapter;
import xyz.lrhm.komakdast.API.Rest.Interfaces.UserFoundListener;
import xyz.lrhm.komakdast.API.Rest.Utils.ForceObject;

import xyz.lrhm.komakdast.Adapter.Cache.AppListAdapter;
import xyz.lrhm.komakdast.Adapter.Cache.FriendsHolder;
import xyz.lrhm.komakdast.Adapter.CoinAdapter;
import xyz.lrhm.komakdast.Adapter.DBAdapter;
import xyz.lrhm.komakdast.Adapter.ForceAdapter;
import xyz.lrhm.komakdast.Adapter.FriendsAdapter;
import xyz.lrhm.komakdast.MainApplication;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.FontsHolder;
import xyz.lrhm.komakdast.Util.ImageManager;
import xyz.lrhm.komakdast.Util.LengthManager;
import xyz.lrhm.komakdast.Util.Logger;
import xyz.lrhm.komakdast.Util.SizeManager;
import xyz.lrhm.komakdast.Util.Tools;
import xyz.lrhm.komakdast.Util.UiUtil;
import xyz.lrhm.komakdast.View.Custom.StarView;
import xyz.lrhm.komakdast.View.Custom.ToastMaker;
import xyz.lrhm.komakdast.View.Dialog.ForceUpdateDialog;
import xyz.lrhm.komakdast.View.Dialog.FriendRequestDialog;
import xyz.lrhm.komakdast.View.Fragment.PackageFragment;
import xyz.lrhm.komakdast.View.Fragment.PackagesFragment;
import xyz.lrhm.komakdast.View.Fragment.StoreFragment;
import xyz.lrhm.komakdast.View.Fragment.VideoGameFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener,
      CoinAdapter.CoinsChangedListener,
       UserFoundListener, ForceAdapter.ForceListener {


    private static final String MAIN_FRAGMENT_TAG = "FRAGMENT_MAIN_TAG";
    private ArrayList<FriendRequestDialog> mCachedFriendRequestDialogs = new ArrayList<>();
    private boolean isInOnlineGame = false;
    private Object matchRqResultLock = new Object();

    ImageView background;
    private DBAdapter db;
    private Tools tools;
    private ImageView cheatButton;
    private ImageView logo;
    private boolean areCheatsVisible = false;
    private int currentLevel;
    private TextView digits;
    private CoinAdapter coinAdapter;
    private LengthManager lengthManager;
    private ImageManager imageManager;
    private boolean store = false;
    private ImageView coinBox;


//    private GoogleSignInOptions mGoogleSignInOptions;
//    private GoogleApiClient mGoogleApiClient;
//    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "MainActivity";
    private User myUser = null;
    private ArrayList<UserFoundListener> mUserFoundListeners;
    private LinearLayout starContainer;
    private StarView[] starViews;
    public FriendsAdapter mFriendsAdapter;
    private long matchResultTime = 0;

    private Button creditsButton;


    // this is for preventing multiple instances


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        coinAdapter = new CoinAdapter(this, this);

        AppAPIAdapter.setContext(this);

        SizeManager.initSizes(this);


//        SocketAdapter.setContext(this);
//        SocketAdapter.addSocketListener(this);
//        SocketAdapter.addFriendRequestListener(this);
//        SocketAdapter.addFriendSocketListener(this);

        initActivity();


//        setVolumeControlStream(AudioManager.STREAM_MUSIC);

//        ToastMaker.show(this, "THIS IS SPARTA ! VERSION " + BuildConfig.VERSION_CODE, Toast.LENGTH_SHORT);


//        HiddenAdapter.getInstance().createHiddenUsr();

        AppListAdapter.getInstance(this);


    }


    private void initActivity() {


        SizeManager.initSizes(this);

        mUserFoundListeners = new ArrayList<>();

        tools = new Tools(getApplication());
        lengthManager = ((MainApplication) getApplicationContext()).getLengthManager();
        imageManager = ((MainApplication) getApplicationContext()).getImageManager();

        digits = (TextView) findViewById(R.id.digits);
        cheatButton = (ImageView) findViewById(R.id.cheat_button);
        logo = (ImageView) findViewById(R.id.logo);

        hideCheatButton();


//        starContainer = (LinearLayout) findViewById(R.id.star_container);
//        initStars();


        cheatButton.setOnClickListener(this);

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cheatButton.getLayoutParams();
//        layoutParams.leftMargin = (int) (0.724 * lengthManager.getScreenWidth());
//        layoutParams.topMargin = (int) (0.07 * lengthManager.getScreenWidth());
        UiUtil.setWidth(cheatButton, lengthManager.getCheatButtonSize());
        UiUtil.setHeight(cheatButton, lengthManager.getCheatButtonSize());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (fragmentManager.getBackStackEntryCount() != 0) throw new IllegalStateException();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);

//        PackageFragment packageFragment = new PackageFragment();
//        packageFragment.setArguments(bundle);


        PackagesFragment packagesFragment = new PackagesFragment();

//        transaction.addToBackStack(null);

//        VideoGameFragment videoGameFragment = new VideoGameFragment();

        fragmentTransaction.replace(R.id.fragment_container, packagesFragment, MAIN_FRAGMENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        setUpCoinBox();
        setUpHeader();
        setOriginalBackground(R.drawable.circles);


//        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//          //      .requestIdToken(getString(R.string.server_client_id))
//                .build();
//
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
//                .build();




//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);

//        if (!Prefs.getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false)) {
//        Intent intent = new Intent(this, RegistrationIntentService.class);
//        startService(intent);
//        }

//        coinAdapter.earnCoins(5000);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Logger.d(TAG, "onNewIntent");

        checkExtras(intent.getExtras());
    }

    public void initStars() {
        starViews = new StarView[3];
        for (int i = 0; i < 3; i++) {
            starViews[i] = new StarView(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = i == 1 ? 0 : (int) (SizeManager.getScreenHeight() * 0.015);
            lp.bottomMargin = (int) (SizeManager.getScreenHeight() * 0.01);

            starContainer.addView(starViews[i], lp);

        }
        starViews[0].rotate(-30);
        starViews[2].rotate(30);

    }

    public void setStarsDeactive() {

        for (StarView starView : starViews)
            starView.setDeActivate();

    }

    public void setStarts(int score) {

        setStarsDeactive();

        ArrayList<Integer> idxs = new ArrayList<>();
        if (score >= 1)
            idxs.add(0);
        if (score >= 2)
            idxs.add(1);
        if (score >= 4)
            idxs.add(2);
        for (Integer integer : idxs)
            starViews[integer].setActive();
    }







    private void setHeaderVisiblity(boolean visible) {
        int headerViewsVisibility = (!visible ? View.GONE : View.VISIBLE);
        logo.setVisibility(headerViewsVisibility);
        creditsButton.setVisibility(headerViewsVisibility);
        coinBox.setVisibility(headerViewsVisibility);
        digits.setVisibility(headerViewsVisibility);


    }



    private void setUpCoinBox() {
        coinBox = (ImageView) findViewById(R.id.coin_box);

        Glide.with(this).load(R.drawable.coinholder).into(coinBox);

//        int coinBoxWidth = lengthManager.getScreenWidth() * 8 / 20;
//        int coinBoxHeight = lengthManager.getHeightWithFixedWidth(R.drawable.coinholder, coinBoxWidth);
//        coinBox.setImageBitmap(imageManager.loadImageFromResource(R.drawable.coinholder, coinBoxWidth, coinBoxHeight));
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coinBox.getLayoutParams();
//        layoutParams.topMargin = lengthManager.getScreenWidth() / 15;
//        layoutParams.leftMargin = lengthManager.getScreenWidth() / 50;
//
//        RelativeLayout.LayoutParams digitsLayoutParams = (RelativeLayout.LayoutParams) digits.getLayoutParams();
//        digitsLayoutParams.topMargin = lengthManager.getScreenWidth() * 32 / 400;
//        digitsLayoutParams.leftMargin = lengthManager.getScreenWidth() * 377 / 3600;
//        digitsLayoutParams.width = (int) (0.98 * lengthManager.getScreenWidth() / 5);
//
//
//        Logger.d(TAG, "density dpi is " + coinBoxHeight);

        if (SizeManager.getScreenWidth() < 800)
            digits.setShadowLayer(0.5f, 1, 1, Color.BLACK);
        digits.setTypeface(FontsHolder.getNumeralSansMedium(this));
//        digits.setTextSize(TypedValue.COMPLEX_UNIT_PX, coinBoxHeight * 0.475f);

        coinBox.setOnClickListener(this);

        CoinAdapter coinAdapter = new CoinAdapter(getApplicationContext(), this);
        coinAdapter.setCoinsChangedListener(this);

//        coinBox.setVisibility(View.GONE);
    }

    private void setUpHeader() {

        Glide.with(this).load(R.drawable.cheat_button).into(cheatButton);

//        RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
//        header.setLayoutParams(new LinearLayout.LayoutParams(
//                lengthManager.getScreenWidth(),
//                lengthManager.getHeaderHeight()
//        ));
//
//        if (logo == null) logo = (ImageView) findViewById(R.id.logo);
////
////        logo.setImageBitmap(imageManager.loadImageFromResource(
////                R.drawable.header, lengthManager.getScreenWidth(),
////                lengthManager.getScreenWidth() / 4
////        ));
////
//        UiUtil.setWidth(logo, (int) (lengthManager.getScreenWidth() * 0.2f));
//
//        UiUtil.setLeftMargin(logo,(int) (lengthManager.getScreenWidth() * 0.7) );

        Glide.with(this).load(R.drawable.new_icon).into(logo);

//        creditsButton = (Button) findViewById(R.id.activity_main_credits_button);
//
//        UiUtil.setLeftMargin(creditsButton, (int) (lengthManager.getScreenWidth() * 0.55));
//        UiUtil.setWidth(creditsButton, (int) (SizeManager.getScreenWidth() * 0.4));
//        UiUtil.setHeight(creditsButton, lengthManager.getScreenWidth() / 4);

//        creditsButton.setOnClickListener(new View.OnClickListener() {
//
//            long lastTimeClicked = 0;
//            int counter = 2;
//
//            @Override
//            public void onClick(View v) {
//                Fragment main = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
//                if (!main.isVisible()) {
//                    return;
//                }
//                if (counter == 2 && Prefs.getBoolean("Credits_Hint", true)) {
//
//                    Prefs.putBoolean("Credits_Hint", false);
//                    ToastMaker.show(MainActivity.this, "راه مخفی", Toast.LENGTH_SHORT);
//                }
//
//                long current = System.currentTimeMillis();
//
//                if (current - lastTimeClicked > 1000) {
//                    counter = 2;
//                } else {
//                    counter--;
//                }
//                lastTimeClicked = current;
//                if (counter == 0) {
//
//                    Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

    }

    public void setOriginalBackground(int drawable) {

        if (background == null)
            background = (ImageView) findViewById(R.id.background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
//        background.setImageBitmap(imageManager.loadImageFromResource(R.drawable.logo1, lengthManager.getScreenWidth(), lengthManager.getScreenHeight()));
//        background.setImageDrawable(new BackgroundDrawable(this, new int[]{
//                Color.parseColor("#6b92ea"),//F3c51c
//                Color.parseColor("#5180ea"),
//                Color.parseColor("#386fea")
//        }, drawable));
    }

    private final static long THRESH_HOLD = 1000;
    private long lastTimeClicked = 0;

    @Override
    public void onClick(View v) {

        long current = System.currentTimeMillis();
        if (current - lastTimeClicked < THRESH_HOLD)
            return;
        lastTimeClicked = current;

        switch (v.getId()) {
            case R.id.cheat_button:
                toggleCheatButton();
                break;

            case R.id.coin_box:
                if (!store) {
                    store = true;
                    StoreFragment storeFragment = new StoreFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, storeFragment);
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                }
                break;


        }
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public void setupCheatButton(int id) {
        cheatButton.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);
//        creditsButton.setVisibility(View.INVISIBLE);
        areCheatsVisible = false;
        currentLevel = id;


    }

    public void hideCheatButton() {
        if (cheatButton != null)
            cheatButton.setVisibility(View.INVISIBLE);
        if (logo != null) logo.setVisibility(View.VISIBLE);
        if (creditsButton != null) creditsButton.setVisibility(View.VISIBLE);
    }

    public boolean isCheatsVisible() {
        return areCheatsVisible;
    }

    public void toggleCheatButton() {
        disableCheatButton(false);
        if (!areCheatsVisible) {


            Glide.with(this).load(R.drawable.next_button).into(cheatButton);
            areCheatsVisible = true;

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof VideoGameFragment)
                ((VideoGameFragment) fragment).showCheats();

        } else {


            Glide.with(this).load(R.drawable.cheat_button).into(cheatButton);
            areCheatsVisible = false;

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof VideoGameFragment)
                ((VideoGameFragment) fragment).hideCheats();
        }
    }

    public void disableCheatButton(boolean enable) {
        cheatButton.setClickable(enable);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Logger.d(TAG, "onActivityResult");

//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }


    }





    @Override
    public void changed(int newAmount) {
        digits.setText(tools.numeralStringToPersianDigits("" + newAmount));


        if (myUser != null) AppAPIAdapter.updateCoin(myUser);
    }


    @Override
    public void onGetUser(User user) {
        Logger.d(TAG, "got the user successfully " + (new Gson()).toJson(user));

        for (UserFoundListener userFoundListener : mUserFoundListeners)
            userFoundListener.onGetUser(user);

    }

    @Override
    public void onGetError() {
        Logger.d(TAG, "didnet get the user");

        for (UserFoundListener userFoundListener : mUserFoundListeners)
            userFoundListener.onGetError();
    }

    @Override
    public void onGetMyUser(final User mUser) {

        Logger.d("TAG", "on get my user main");


        this.myUser = mUser;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UserFoundListener userFoundListener : mUserFoundListeners)
                    userFoundListener.onGetMyUser(MainActivity.this.myUser);



                if (CoinAdapter.shouldCheckUser()
                        && !AppAPIAdapter.isIsUpdateCoinInProgress()
                        && mUser.isFromServer()
                        && mUser.getCoins() + coinAdapter.getCoinDiff() != coinAdapter.getCoinsCount()
                        && mUser.getCoins() >= 0) {
                    coinAdapter.setCoinsCount(mUser.getCoins());
                    CoinAdapter.addCoinDiff(-CoinAdapter.getCoinDiff());
                }

                if (mUser.isFromServer())
                    AppAPIAdapter.updateCoin(mUser);

            }
        });

        if (mUser.isFromServer()) {
            Logger.d(TAG, "my user coins " + mUser.getCoins());
            Logger.d(TAG, "adapter coin " + coinAdapter.getCoinsCount());
            Logger.d(TAG, "coin diff" + coinAdapter.getCoinDiff());
        }
    }

    @Override
    public void onForceLogout() {

        Prefs.remove(Tools.SHARED_PREFS_TOKEN);
        Prefs.remove(Tools.USER_SAVED_DATA);


        FriendsHolder.getInstance().clearAll();
        mFriendsAdapter.clearAll();
    }




    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void onForceUpdate(final ForceObject forceObject) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing())
                    new ForceUpdateDialog(MainActivity.this, forceObject).show();

                else if (!isPaused)
                    new Handler().postDelayed(this, 1000);
            }
        }, 2000);
    }

    @Override
    public void onForceDownload(final ForceObject forceObject) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing())
                    new ForceUpdateDialog(MainActivity.this, forceObject).show();
                else if (!isPaused)
                    new Handler().postDelayed(this, 1000);
            }
        }, 2000);
    }

    @Override
    public void onShowPopup(final ForceObject object) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing())
                    ForceAdapter.getInstance(MainActivity.this).showNewVersionPopup(MainActivity.this, object);
                else if (!isPaused)
                    new Handler().postDelayed(this, 1000);
            }
        }, 2000);

    }


    public void purchase(String sku, int price) {


    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void signInWithGoogle() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

//    private void handleSignInResult(GoogleSignInResult result) {
//
//        Logger.d(TAG, "handleSignInResult:" + result.getStatus().getStatus());
//
//
//        if (result == null)
//            return;
//
//        String TAG = "GoogleSignInResult";
//        Logger.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            final GoogleToken googleToken = new GoogleToken(acct.getIdToken());
//
//
//            AppAPIAdapter.isOldUser(googleToken, new OldUserListener() {
//                @Override
//                public void isOldUser(boolean oldUser) {
//                    if (oldUser) {
//
//
//                        googleToken.setUsername(RandomString.nextString());
//                        AppAPIAdapter.getMyUserByGoogle(googleToken, MainActivity.this);
//
//                        return;
//                    }
//                    new UsernameChooseDialog(MainActivity.this, googleToken, MainActivity.this).show();
//
//
//                }
//            });
//
//
//        } else {
//        }
//    }

    public User getMyUser() {
        return myUser;
    }

    public void addUserFoundListener(UserFoundListener userFoundListener) {
        mUserFoundListeners.add(userFoundListener);
    }



    private long lastOnFinish = 0;
    private Object lock = new Object();



    public void setGameResult(boolean doSet) {

        int visibily = (doSet) ? View.VISIBLE : View.GONE;

        setHeaderVisiblity(!doSet);
        starContainer.setVisibility(visibily);
//        if (doSet)
//            setOnlineGameVisibilityGone();
    }



    int backPressedCount = 0;
    long backPressedTime = 0;

    @Override
    public void onBackPressed() {


        if (true) {
            Fragment main = getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (main.isVisible()) {

                if (System.currentTimeMillis() - backPressedTime > 2000) {
                    backPressedCount = 0;
                }
                backPressedTime = System.currentTimeMillis();

                backPressedCount++;
                if (backPressedCount == 2) {

                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);

                } else {

                    ToastMaker.show(this, "برای خروج یک بار دیگر بازگشت را بزن", Toast.LENGTH_SHORT);
                }
                return;
            }


            super.onBackPressed();
        }


    }


    private static boolean isPaused = false;
    private static long pauseTime = 0;


    @Override
    protected void onPause() {

        Logger.d(TAG, "onPAuse");



        pauseTime = System.currentTimeMillis();

//        SocketAdapter.disconnect();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isPaused && System.currentTimeMillis() - pauseTime >= 2 * 55 * 1000)
//                    SocketAdapter.disconnect();
//
//            }
//        }, 2 * 60 * 1000);

        isPaused = true;

        super.onPause();
    }

    @Override
    protected void onResume() {


        isPaused = false;


//        SocketAdapter.reconnect();


        super.onResume();


        checkExtras(getIntent().getExtras());


//        AppAPIAdapter.tryToLogin(this);

//        checkForceUpdate();


        Logger.d(TAG, "super.onResume ended");
    }


    public void setFriendsAdapter(FriendsAdapter mFriendsAdapter) {
        this.mFriendsAdapter = mFriendsAdapter;
    }

    public boolean isInOnlineGame() {
        return isInOnlineGame;
    }


    public void setIsInOnlineGame(boolean isInOnlineGame) {
        this.isInOnlineGame = isInOnlineGame;
        if (isInOnlineGame)
            setStarsDeactive();
    }


    public void checkExtras(Bundle bundle) {


        if (bundle == null)
            return;






    }

    private void checkForceUpdate() {
        ForceAdapter.getInstance(this).addListener(this);
        ForceAdapter.getInstance(this).check();
    }


    public CoinAdapter getCoinAdapter() {
        return coinAdapter;
    }


}
