package xyz.lrhm.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
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

import xyz.lrhm.komakdast.API.Rest.Utils.ForceObject;
import xyz.lrhm.komakdast.Adapter.ForceAdapter;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.DownloadTask;
import xyz.lrhm.komakdast.Util.FontsHolder;
import xyz.lrhm.komakdast.Util.Logger;
import xyz.lrhm.komakdast.Util.SizeConverter;
import xyz.lrhm.komakdast.Util.SizeManager;
import xyz.lrhm.komakdast.Util.Tools;
import xyz.lrhm.komakdast.Util.UiUtil;
import xyz.lrhm.komakdast.View.Activity.MainActivity;
import xyz.lrhm.komakdast.View.Custom.ToastMaker;

public class ForceUpdateDialog extends Dialog implements View.OnClickListener, DownloadTask.DownloadTaskListener {

    private static final String TAG = "ForceUpdateDialog";
    Context context;
    ForceObject forceObject;

    TextView progresTextView;
    boolean textProgressVisiblity = false;
    String imageUri;

    public ForceUpdateDialog(Context context, ForceObject object) {
        super(context);

        this.context = context;
        forceObject = object;
        ForceAdapter.getInstance(context).setListener(this);
        imageUri = forceObject.getImageURL();
        textProgressVisiblity = forceObject.isForceDownload();

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

