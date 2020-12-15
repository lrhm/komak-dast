package xyz.lrhm.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xyz.lrhm.komakdast.API.Rest.AppAPIAdapter;
import xyz.lrhm.komakdast.API.Rest.Interfaces.OnFriendRequest;
import xyz.lrhm.komakdast.Adapter.Cache.FriendsHolder;
import xyz.lrhm.komakdast.Adapter.FriendsAdapter;
import xyz.lrhm.komakdast.MainApplication;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.FontsHolder;
import xyz.lrhm.komakdast.Util.ImageManager;
import xyz.lrhm.komakdast.Util.SizeManager;
import xyz.lrhm.komakdast.Util.Tools;
import xyz.lrhm.komakdast.Util.UiUtil;
import xyz.lrhm.komakdast.View.Activity.MainActivity;
import xyz.lrhm.komakdast.View.Custom.DialogDrawable;
import xyz.lrhm.komakdast.View.Custom.UserLevelView;


public class FriendRequestDialog extends Dialog implements View.OnClickListener, OnFriendRequest {
    Context context;
    RelativeLayout mDataContainer;
    Tools tools;
    ImageView mMatchButton;
    ImageView mChatButton;
    UserLevelView mUserLevelView;
    User mUser;
    boolean isActed = false;


    public FriendRequestDialog(Context context, User user) {
        super(context);
        this.context = context;
        tools = new Tools(context);
        mUser = user;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_match_request_view);

        mUserLevelView = (UserLevelView) findViewById(R.id.dialog_user_view_mark_view);
        mUserLevelView.setUser(mUser);
        mUserLevelView.setClick(false);

        mDataContainer = (RelativeLayout) findViewById(R.id.user_data_container);
        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams((int) (0.8 * SizeManager.getScreenWidth()), ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) (SizeManager.getScreenWidth() * 0.09);
        mDataContainer.setLayoutParams(layoutParams);
        tools.setViewBackground(mDataContainer, new DialogDrawable(getContext()));

        mMatchButton = (ImageView) findViewById(R.id.uv_match_button);
        mChatButton = (ImageView) findViewById(R.id.uv_start_chat_button);
        int size = (int) (SizeManager.getScreenWidth() * 0.1);

        ImageManager imageManager = ((MainApplication) getContext().getApplicationContext()).getImageManager();

        mMatchButton.setImageBitmap(imageManager.loadImageFromResource(R.drawable.yes, size, size));
        mChatButton.setImageBitmap(imageManager.loadImageFromResource(R.drawable.no, size, size));


        int padding = (int) (SizeManager.getScreenWidth() * 0.01);
        int leftMargin = (int) (SizeManager.getScreenWidth() * 0.8 - size * 2 - padding);
        UiUtil.setLeftMargin(mMatchButton, leftMargin / 2);
        UiUtil.setLeftMargin(mChatButton, padding);


        mChatButton.setOnClickListener(this);
        mMatchButton.setOnClickListener(this);


        String title = "درخواست دوستی";
        setUpTextView(R.id.dialog_match_request_title, title, true);
        findViewById(R.id.dialog_match_request_content).setVisibility(View.GONE);


        UiUtil.setTopMargin(findViewById(R.id.dialog_match_request_text_containers), (int) (UiUtil.getTextViewHeight(mUserLevelView.getUserNameTextView())
                + SizeManager.getScreenHeight() * 0.03)
        );


    }


    private void setUpTextView(int id, String text, boolean bold) {
        TextView titleTextView = (TextView) findViewById(id);

        titleTextView.setTypeface(FontsHolder.getFont(context, bold ? FontsHolder.SANS_BOLD : FontsHolder.SANS_MEDIUM));
        titleTextView.setText(text);

        UiUtil.setTextViewSize(titleTextView, (int) (SizeManager.getScreenHeight() * 0.1), 0.3f);

        int leftMargin = (int) (SizeManager.getScreenWidth() * 0.8 - UiUtil.getTextViewWidth(titleTextView));
        UiUtil.setLeftMargin(titleTextView, leftMargin / 2);

    }

    @Override
    public void onClick(View v) {


        isActed = true;

        if (v.getId() == R.id.uv_start_chat_button) {

        }

        if (v.getId() == R.id.uv_match_button) {
            AppAPIAdapter.requestFriend(Tools.getCachedUser(context), mUser.getId(), this);


            FriendsHolder friendsHolder = FriendsHolder.getInstance();
            friendsHolder.addFriendToList(mUser);
        }

        dismiss();


    }

    @Override
    public void dismiss() {
        if (!isActed)
            ((MainActivity) context).mFriendsAdapter.addUser(mUser, FriendsAdapter.TYPE_REQUEST);


        super.dismiss();
    }

    @Override
    public void onFriendRequestSent() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mUser.setIsFriend(true);
                ((MainActivity) context).mFriendsAdapter.addUser(mUser, FriendsAdapter.TYPE_FRIEND);

            }
        });
    }

    @Override
    public void onFriendRequestFailedToSend() {


    }
}

