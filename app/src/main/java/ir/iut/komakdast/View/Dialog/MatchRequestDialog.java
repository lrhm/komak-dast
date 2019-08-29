package ir.iut.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ir.iut.komakdast.API.Socket.SocketAdapter;
import ir.iut.komakdast.Adapter.Cache.MatchRequestCache;
import ir.iut.komakdast.Adapter.CoinAdapter;
import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.Object.User;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.FontsHolder;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.SizeManager;
import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.Util.UiUtil;
import ir.iut.komakdast.View.Activity.MainActivity;
import ir.iut.komakdast.View.Custom.DialogDrawable;
import ir.iut.komakdast.View.Custom.UserLevelView;
import ir.iut.komakdast.View.Fragment.GameResultFragment;


public class MatchRequestDialog extends Dialog implements View.OnClickListener, Runnable {
    Context context;
    RelativeLayout mDataContainer;
    Tools tools;
    ImageView mMatchButton;
    ImageView mChatButton;
    UserLevelView mUserLevelView;
    User mUser;
    Boolean toSend; // dialogs appear before requesting an online game , and on an online game request ! toSend is for that
    CoinAdapter coinAdapter;
    View.OnClickListener yesClick;
    private boolean accepted = false;

    public MatchRequestDialog(Context context, User user) {
        super(context);
        this.context = context;
        coinAdapter = ((MainActivity) context).getCoinAdapter();
        tools = new Tools(context);
        mUser = user;
        toSend = false;

    }

    public MatchRequestDialog(Context context, User user, boolean toSend, View.OnClickListener yesClick) {
        super(context);
        this.context = context;
        tools = new Tools(context);
        mUser = user;
        this.toSend = toSend;
        coinAdapter = ((MainActivity) context).getCoinAdapter();

        this.yesClick = yesClick;

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

        String title = "درخواست بازی";
        String content = "۱۰۰ سکه";
        setUpTextView(R.id.dialog_match_request_title, title, true);
        setUpTextView(R.id.dialog_match_request_content, content, false);

        mChatButton.setOnClickListener(this);
        mMatchButton.setOnClickListener(this);


        UiUtil.setTopMargin(findViewById(R.id.dialog_match_request_text_containers), (int) (UiUtil.getTextViewHeight(mUserLevelView.getUserNameTextView())
                + SizeManager.getScreenHeight() * 0.03)
        );

        if (!toSend) {
            setCanceledOnTouchOutside(false);
        }

        new Handler().postDelayed(this, 30000);

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


        if (v.getId() == R.id.uv_start_chat_button) {
//            acceptOrDeclineMatch(false, v);
        }

        if (v.getId() == R.id.uv_match_button) {
            acceptOrDeclineMatch(true, v);
        }
        dismiss();


    }

    @Override
    public void dismiss() {
        if (!toSend && !accepted) {
            SocketAdapter.responseToMatchRequest(mUser.getId(), false);
        } else if (!toSend && accepted) {
            MatchRequestCache.getInstance().remove(this);
            MatchRequestCache.getInstance().dismissAll();
        }

        Fragment gameResultFragment = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(GameResultFragment.TAG);
        if (gameResultFragment != null)
            ((MainActivity) context).getSupportFragmentManager().popBackStack();


        super.dismiss();


    }

    public void acceptOrDeclineMatch(boolean accepted, View v) {

        if (!toSend) {
            if (accepted) {

                if (!coinAdapter.spendCoinDiffless(100)) {
                    SocketAdapter.responseToMatchRequest(mUser.getId(), false);
                    return;
                }
                SocketAdapter.responseToMatchRequest(mUser.getId(), accepted);
                this.accepted = true;

                new LoadingDialog(context).show();

            }
        } else {
            if (accepted)
                yesClick.onClick(v);
        }

    }

    @Override
    public void run() {

        if (isShowing())
            dismiss();

    }
}

