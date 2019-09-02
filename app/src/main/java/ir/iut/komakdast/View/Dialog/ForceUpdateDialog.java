package ir.iut.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ir.iut.komakdast.API.Rest.Utils.ForceObject;
import ir.iut.komakdast.API.Socket.Objects.Notifs.AdNotification;
import ir.iut.komakdast.API.Socket.SocketAdapter;
import ir.iut.komakdast.Adapter.ForceAdapter;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.DownloadTask;
import ir.iut.komakdast.Util.FontsHolder;
import ir.iut.komakdast.Util.Logger;
import ir.iut.komakdast.Util.SizeConverter;
import ir.iut.komakdast.Util.SizeManager;
import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.Util.UiUtil;
import ir.iut.komakdast.View.Activity.MainActivity;
import ir.iut.komakdast.View.Custom.ToastMaker;

public class ForceUpdateDialog extends Dialog implements View.OnClickListener, DownloadTask.DownloadTaskListener {

    private static final String TAG = "ForceUpdateDialog";
    Context context;
    ForceObject forceObject;
    AdNotification adNotification;
    TextView progresTextView;
    boolean textProgressVisiblity = false;
    String imageUri;

    public ForceUpdateDialog(Context context, ForceObject object) {
        super(context);
        SocketAdapter.disconnect();
        this.context = context;
        forceObject = object;
        ForceAdapter.getInstance(context).setListener(this);
        imageUri = forceObject.getImageURL();
        textProgressVisiblity = forceObject.isForceDownload();

    }

    public ForceUpdateDialog(Context context, AdNotification notification) {
        super(context);
        adNotification = notification;
        this.context = context;
        imageUri = adNotification.getImgUrl();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Logger.d(TAG, "url is " + imageUri);


        setContentView(R.layout.dialog_force_update);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = SizeManager.getScreenWidth();
        lp.height = SizeManager.getScreenHeight();
        getWindow().setAttributes(lp);


        ImageView imageView = (ImageView) findViewById(R.id.dialog_force_update_image);
        imageView.setOnClickListener(this);

        SizeConverter sizeConverter = SizeConverter.SizeConvertorFromWidth(SizeManager.getScreenWidth(), 900, 1600);
        UiUtil.setWidth(imageView, sizeConverter.mWidth);
        UiUtil.setHeight(imageView, sizeConverter.mHeight);

        Glide.with(context).load(Uri.parse(imageUri)).into(imageView);

        if (textProgressVisiblity) {
            progresTextView.setVisibility(View.VISIBLE);

            progresTextView = (TextView) findViewById(R.id.dialog_force_update_progress);
            UiUtil.setTextViewSize(progresTextView, (int) (SizeManager.getScreenWidth() * 0.3), 0.2f);
            progresTextView.setTypeface(FontsHolder.getNumeralSansBold(context));


            UiUtil.setTopMargin(progresTextView, (int) (SizeManager.getScreenHeight() * 0.1f));
        }
    }

    @Override
    public void onClick(View view) {

        if (forceObject != null && forceObject.isForceUpdate())
            ForceAdapter.getInstance(context).openCafeBazzarAppPage((MainActivity) context);

        if (adNotification != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(adNotification.getRedirectURL()));
            context.startActivity(intent);
            dismiss();

        }
    }

    @Override
    public void onBackPressed() {


//        super.onBackPressed();
        if (forceObject != null)
            ((MainActivity) context).onBackPressed();
        else
            super.onBackPressed();

    }

    @Override
    public void onProgress(final int progress) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progresTextView.setText(Tools.numeralStringToPersianDigits(progress + "%"));

            }
        });
    }

    @Override
    public void onDownloadSuccess() {

    }

    @Override
    public void onDownloadError(String error) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ToastMaker.show(context, context.getResources().getString(R.string.connection_to_internet_sure), Toast.LENGTH_SHORT);

            }
        });

    }
}

