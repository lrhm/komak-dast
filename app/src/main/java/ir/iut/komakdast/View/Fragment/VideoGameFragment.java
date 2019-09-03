package ir.iut.komakdast.View.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

import ir.iut.komakdast.Adapter.CoinAdapter;
import ir.iut.komakdast.Adapter.DBAdapter;
import ir.iut.komakdast.Adapter.LevelsAdapter;
import ir.iut.komakdast.Interface.FinishLevel;
import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.Object.Level;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.LengthManager;
import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.View.Activity.MainActivity;
import ir.iut.komakdast.View.Custom.CheatDrawable;
import ir.iut.komakdast.View.Custom.KeyboardView;
import ir.iut.komakdast.View.Custom.ToastMaker;
import ir.iut.komakdast.View.Dialog.NextLevelDialog;


public class VideoGameFragment extends Fragment implements KeyboardView.OnKeyboardEvent, View.OnClickListener {

    private Integer levelId;
    private Integer packageId;

    KeyboardView keyboardView;
    PlayerView playerView;
    SimpleExoPlayer player;
    Tools tools;
    LengthManager lengthManager;
    ImageManager imageManager;
    ImageView imageView;
    Level level;
    DBAdapter db;
    private int packageSize;
    private MainActivity mainActivity;
    private CoinAdapter coinAdapter;
    private View[] cheatButtons;
    private boolean skiped = false;
    private boolean resulved = false;
    private View blackWidow;

    public VideoGameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            levelId = getArguments().getInt("LevelId");
            packageId = getArguments().getInt("id");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_game, container, false);
        mainActivity = (MainActivity) getActivity();

        tools = new Tools(getContext());
        lengthManager = ((MainApplication) getActivity().getApplication()).getLengthManager();
        imageManager = ((MainApplication) getActivity().getApplication()).getImageManager();
        db = DBAdapter.getInstance(getActivity());
        coinAdapter = new CoinAdapter(getActivity(), getActivity());

        level = db.getLevel(packageId, levelId);
        packageSize = db.getLevels(packageId).length;


        playerView = view.findViewById(R.id.player_view);

        setUpImagePlace(view);

        initExoPlayer();


        imageView = (ImageView) view.findViewById(R.id.imageView);

//        imageView.setOnClickListener(this);

//        imagePath = "file://" + getActivity().getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + level.getResources();

        if (level.getType().equals("1pic")) {
            Glide.with(getActivity()).load(level.getImagesPath(packageId, getActivity()).get(0)).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nextLevel(5);
                }
            });
        }
        if (level.getType().equals("4pics")) {

            imageView.setVisibility(View.GONE);
            init4Pics(view);

            view.findViewById(R.id.four_pic_next_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
        if (level.getType().equals("keyboard")) {
            imageView.setVisibility(View.GONE);

            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragment_game_keyboard_container);

            frameLayout.setVisibility(View.VISIBLE);

            keyboardView = new KeyboardView(getContext(), level.getAnswer());
            keyboardView.onKeyboardEvent = this;
            frameLayout.addView(keyboardView);

            ((MainActivity) getActivity()).setupCheatButton(packageId);

        }

        return view;
    }

    void init4Pics(final View parent) {

        ImageView imageView1 = parent.findViewById(R.id.imageView1);

        ImageView imageView2 = parent.findViewById(R.id.imageView2);

        ImageView imageView3 = parent.findViewById(R.id.imageView3);

        ImageView imageView4 = parent.findViewById(R.id.imageView4);

        ImageView[] fourPics = {imageView1, imageView2, imageView3, imageView4};


        ArrayList<String> pics = level.getImagesPath(packageId, getActivity());

        for (int i = 0; i < 4; i++) {

            fourPics[i].setVisibility(View.VISIBLE);

            fourPics[i].setTag(R.integer.click_id_four, i);

            fourPics[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    ((ImageView)view).setImageTintList(ColorStateList.valueOf(Color.parseColor("A5FF0000")));

                    int highlightColor = Color.parseColor("#A5FF0000");

                    if (level.getImagesPath(packageId, getActivity()).get(R.integer.click_id_four).contains(level.getAnswer())) {
                        highlightColor = Color.parseColor("#A500FF00");
//                        parent.findViewById(R.id.four_pic_next_button).setVisibility(View.VISIBLE);


                        nextLevel(10);
                    }

                    ((ImageView) view).setColorFilter(highlightColor);


//
//
//                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
//
//                    Paint redHighLight = new Paint();
//                    redHighLight.setColorFilter(colorFilter);
//                    redHighLight.setAlpha(190);
//
//                    ((ImageView) view).setColorFilter(redHighLight.getColorFilter());

                }
            });
//            fourPics[i].setTag(level.getPics());
            Glide.with(getActivity()).load(pics.get(i)).into(fourPics[i]);

        }


    }

    public void initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getContext());

        playerView.setPlayer(player);


        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "nashenavayan"));


        Uri mediaUri = Uri.parse(level.getVideoPath(packageId, getActivity()));

        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).
                createMediaSource(mediaUri);

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);

        playerView.hideController();
//        playerView.setUseController(false);

//        playerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                player.setPlayWhenReady(!isPlaying());
//
//                return false;
//            }
//        });


    }

    private boolean isPlaying() {
        return player != null
                && player.getPlaybackState() != Player.STATE_ENDED
                && player.getPlaybackState() != Player.STATE_IDLE
                && player.getPlayWhenReady();
    }

    @Override
    public void onDestroy() {
        if (level.getType().equals("keyboard"))
            ((MainActivity) getActivity()).hideCheatButton();

        super.onDestroy();
        if (player != null)
            player.release();
    }


    private void setUpImagePlace(View view) {
        FrameLayout box = (FrameLayout) view.findViewById(R.id.box);

        tools.resizeView(box, lengthManager.getLevelImageWidth(), lengthManager.getLevelImageHeight());

        ImageView frame = (ImageView) view.findViewById(R.id.frame);

        tools.resizeView(frame, lengthManager.getLevelImageFrameWidth(), lengthManager.getLevelImageFrameHeight());

        Glide.with(getContext()).load(R.drawable.frame).into(frame);
//        frame.setImageBitmap(imageManager.loadImageFromResource(R.drawable.frame, lengthManager.getLevelImageFrameWidth(), lengthManager.getLevelImageFrameHeight()));




        if (level.getType().equals("keyboard")) {
            cheatButtons = new View[]{
                    view.findViewById(R.id.cheat_remove_some_letters),
                    view.findViewById(R.id.cheat_reveal_a_letter),
                    view.findViewById(R.id.cheat_skip_level)
            };

            for (View cheatView : cheatButtons) {
                cheatView.setOnClickListener(this);

                ViewGroup.LayoutParams layoutParams = cheatView.getLayoutParams();
                layoutParams.width = lengthManager.getCheatButtonWidth();
                layoutParams.height = lengthManager.getCheatButtonHeight();
            }

            String[] cheatTitles = new String[]{
                    "حذف چند حرف",
                    "نمایش یک حرف",
                    "رد کردن مرحله"
            };

            int[] cheatCosts = new int[]{
                    CoinAdapter.ALPHABET_HIDING_COST,
                    CoinAdapter.LETTER_REVEAL_COST,
                    CoinAdapter.SKIP_LEVEL_COST
            };

            for (int i = 0; i < 3; i++)
                tools.setViewBackground(
                        cheatButtons[i],
                        new CheatDrawable(  // TODO: 8/7/15 bad performance
                                view.getContext(),
                                i,
                                cheatTitles[i],
                                level.isResolved() ? "مفت" : tools.numeralStringToPersianDigits("" + cheatCosts[i])
                        )
                );


            blackWidow = view.findViewById(R.id.black_widow);
        }
    }


    private void nextLevel(int prize) {

        if (!level.isResolved() && !resulved && !skiped)
            coinAdapter.earnCoins(prize);
        db.resolveLevel(packageId, levelId);
        resulved = true;

//        tools.backUpDB();
        new NextLevelDialog(getActivity(), level, packageSize, skiped, prize,
                new FinishLevel() {
                    @Override
                    public void NextLevel() {


                        Bundle bundle = new Bundle();
                        int levelID = level.getId() + 1;
                        bundle.putInt("LevelId", levelID);
                        bundle.putInt("id", packageId);

                        VideoGameFragment gameFragment = new VideoGameFragment();
                        gameFragment.setArguments(bundle);

                        if (mainActivity == null && getActivity() != null)
                            mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, gameFragment, LevelsAdapter.OFFLINE_GAME_FRAGMENT_TAG);
                            transaction.commitAllowingStateLoss();

                        }
                    }

                    @Override
                    public void Home() {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAllAnswered(String guess) {
        if ((guess.replace("آ", "ا").replace("/", "")).equals((level.getAnswer().replace(".",
                "")).replace("آ", "ا").replace("/", ""))) {


            if (!level.isResolved()) {
                coinAdapter.earnCoins(CoinAdapter.LEVEL_COMPELETED_PRIZE);
            }

            nextLevel(30);

        }
    }

    @Override
    public void onHintClicked() {

    }


    private long lastTimeClicked = 0;
    private long treshHold = 850;

    @Override
    public void onClick(View view) {

        long clickTime = System.currentTimeMillis();
        if (clickTime - lastTimeClicked < treshHold)
            return;

        lastTimeClicked = clickTime;

        switch (view.getId()) {
            case R.id.cheat_remove_some_letters:
                ((MainActivity) getActivity()).toggleCheatButton();
                cheatHazf();
                break;

            case R.id.cheat_reveal_a_letter:
                ((MainActivity) getActivity()).toggleCheatButton();
                cheatAzafe();
                break;

            case R.id.cheat_skip_level:
                ((MainActivity) getActivity()).toggleCheatButton();
                cheatNext();
                break;
        }
    }


    public void showCheats() {
        for (View view : cheatButtons)
            view.setVisibility(View.VISIBLE);

        blackWidow.setVisibility(View.VISIBLE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(blackWidow, "alpha", 0, 0.60f),
                ObjectAnimator.ofFloat(cheatButtons[0], "translationX", -cheatButtons[0].getWidth(), 0),
                ObjectAnimator.ofFloat(cheatButtons[1], "translationX", +cheatButtons[1].getWidth(), 0),
                ObjectAnimator.ofFloat(cheatButtons[2], "translationX", -cheatButtons[2].getWidth(), 0)
        );


        animatorSet.setDuration(600).start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (getActivity() == null)
                    return;


                ((MainActivity) getActivity()).disableCheatButton(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void hideCheats() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(blackWidow, "alpha", 0.60f, 0),
                ObjectAnimator.ofFloat(cheatButtons[0], "translationX", 0, -cheatButtons[0].getWidth()),
                ObjectAnimator.ofFloat(cheatButtons[1], "translationX", 0, +cheatButtons[1].getWidth()),
                ObjectAnimator.ofFloat(cheatButtons[2], "translationX", 0, -cheatButtons[2].getWidth())
        );

        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (View view : cheatButtons) {
                    view.setVisibility(View.GONE);
                    view.clearAnimation();
                }

                blackWidow.setVisibility(View.GONE);
                if (getActivity() == null)
                    return;


                ((MainActivity) getActivity()).disableCheatButton(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet.setDuration(600).start();
    }

    private void cheatNext() {
        if (level.isResolved()) {
            nextLevel(30);
        } else if (coinAdapter.spendCoins(CoinAdapter.SKIP_LEVEL_COST)) {
            skiped = true;
            resulved = true;
            nextLevel(30);
        }
    }

    private void cheatHazf() {
        if (level.isResolved()) {
            keyboardView.removeSome();
        } else if (coinAdapter.spendCoins(CoinAdapter.ALPHABET_HIDING_COST)) {
            if (!keyboardView.removeSome()) {
                String toastText = "نمیشه دیگه";
                ToastMaker.show(getContext(), toastText, Toast.LENGTH_SHORT);
                coinAdapter.earnCoins(CoinAdapter.ALPHABET_HIDING_COST);
            }
        }

    }


    private void cheatAzafe() {
        if (level.isResolved()) {
            keyboardView.showOne();
        } else if (coinAdapter.spendCoins(CoinAdapter.LETTER_REVEAL_COST)) {
            if (!keyboardView.showOne()) {
                String toastText = "نمیشه دیگه";
                ToastMaker.show(getContext(), toastText, Toast.LENGTH_SHORT);
                coinAdapter.earnCoins(CoinAdapter.LETTER_REVEAL_COST);
            }
        }

    }
}
