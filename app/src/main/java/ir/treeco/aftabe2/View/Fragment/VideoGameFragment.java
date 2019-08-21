package ir.treeco.aftabe2.View.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import ir.treeco.aftabe2.Adapter.CoinAdapter;
import ir.treeco.aftabe2.MainApplication;
import ir.treeco.aftabe2.R;
import ir.treeco.aftabe2.Util.ImageManager;
import ir.treeco.aftabe2.Util.LengthManager;
import ir.treeco.aftabe2.Util.Tools;
import ir.treeco.aftabe2.View.Custom.CheatDrawable;
import ir.treeco.aftabe2.View.Custom.KeyboardView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoGameFragment extends Fragment implements KeyboardView.OnKeyboardEvent {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    KeyboardView keyboardView;
    PlayerView playerView;
    SimpleExoPlayer player;
    Tools tools;
    LengthManager lengthManager;
    ImageManager imageManager;
    ImageView imageView;



    public VideoGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoGameFragment newInstance(String param1, String param2) {
        VideoGameFragment fragment = new VideoGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_game, container, false);

        tools = new Tools(getContext());
        lengthManager = ((MainApplication) getActivity().getApplication()).getLengthManager();
        imageManager = ((MainApplication) getActivity().getApplication()).getImageManager();

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragment_game_keyboard_container);

        keyboardView = new KeyboardView(getContext(), "سلام");
        keyboardView.onKeyboardEvent = this;
        frameLayout.addView(keyboardView);

        playerView = view.findViewById(R.id.player_view);

        setUpImagePlace(view);

        initExoPlayer();



        imageView = (ImageView) view.findViewById(R.id.imageView);

//        imageView.setOnClickListener(this);

//        imagePath = "file://" + getActivity().getFilesDir().getPath() + "/Packages/package_" + packageId + "/" + level.getResources();

        Picasso.with(getActivity()).load(R.drawable.abr).into(imageView);


        return view;
    }

    public void initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getContext());

        playerView.setPlayer(player);


        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "nashenavayan"));


        Uri mediaUri = Uri.parse("asset:///myVideo.mp4");

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
        super.onDestroy();

        player.release();
    }


    private void setUpImagePlace(View view) {
        FrameLayout box = (FrameLayout) view.findViewById(R.id.box);
        tools.resizeView(box, lengthManager.getLevelImageWidth(), lengthManager.getLevelImageHeight());

        ImageView frame = (ImageView) view.findViewById(R.id.frame);
        frame.setImageBitmap(imageManager.loadImageFromResource(R.drawable.frame, lengthManager.getLevelImageFrameWidth(), lengthManager.getLevelImageFrameHeight()));
        tools.resizeView(frame, lengthManager.getLevelImageFrameWidth(), lengthManager.getLevelImageFrameHeight());


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

    }

    @Override
    public void onHintClicked() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
