package ir.iut.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;

import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.Interface.FinishLevel;
import ir.iut.komakdast.Object.Level;
import ir.iut.komakdast.Util.FontsHolder;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.LengthManager;
import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.View.Custom.DialogDrawable;
import ir.iut.komakdast.R;

public class FinishDailog extends Dialog implements View.OnClickListener {
    private Context context;
    private Level level;
    private int packageSize;
    private int prize;
    private FinishLevel finishLevel;
    private ImageManager imageManager;
    private LengthManager lengthManager;
    private boolean skiped;

    public FinishDailog(Context context, Level level, int packageSize, boolean skiped, int prize, FinishLevel finishLevel) {
        super(context);
        this.context = context;
        this.level = level;
        this.packageSize = packageSize + 1;
        this.prize = prize;
        this.finishLevel = finishLevel;
        this.skiped = skiped;
        imageManager = ((MainApplication) context.getApplicationContext()).getImageManager();
        lengthManager = ((MainApplication) context.getApplicationContext()).getLengthManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);

//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.level_finished);
        View container = findViewById(R.id.container);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
        layoutParams.leftMargin = layoutParams.rightMargin = lengthManager.getStoreDialogMargin();

        View contents = findViewById(R.id.contents);

        DialogDrawable drawable = new DialogDrawable(context);
        drawable.setTopPadding(lengthManager.getLevelFinishedDialogTopPadding());
        Tools tools = new Tools(context);
        tools.setViewBackground(contents, drawable);

        int padding = lengthManager.getLevelFinishedDialogPadding();
        contents.setPadding(padding, 0, padding, padding);

        TextView prizeTextView = (TextView) findViewById(R.id.prize);
        if (!level.isResolved() && !skiped) {
            String prizeString;
            if (prize == 30)
                 prizeString = "+۳۰";
            else if (prize == 10)
                prizeString = "+۱۰";
            else
                prizeString = "+۵";

            customizeTextView(prizeTextView, prizeString, lengthManager.getLevelAuthorTextSize());

            tools.resizeView(prizeTextView, lengthManager.getPrizeBoxSize(), lengthManager.getPrizeBoxSize());
            tools.setViewBackground(prizeTextView, new BitmapDrawable(context.getResources(), imageManager.loadImageFromResource(R.drawable.coin, lengthManager.getPrizeBoxSize(), lengthManager.getPrizeBoxSize())));

            ObjectAnimator.ofFloat(prizeTextView, "rotation", 0, 315).setDuration(0).start();
        } else {
            prizeTextView.setVisibility(View.GONE);
        }

        ImageView tickView = (ImageView) findViewById(R.id.tickView);
        ((ViewGroup.MarginLayoutParams) tickView.getLayoutParams()).rightMargin = (int) (0.125 * lengthManager.getTickViewSize());
        ((ViewGroup.MarginLayoutParams) tickView.getLayoutParams()).leftMargin = (int) (0.125 * lengthManager.getTickViewSize());

        tickView.setImageBitmap(imageManager.loadImageFromResource(R.drawable.yes, lengthManager.getTickViewSize(), lengthManager.getTickViewSize()));
        tools.resizeView(tickView, lengthManager.getTickViewSize(), lengthManager.getTickViewSize());
        ImageView nextButton = (ImageView) findViewById(R.id.next_level_button);
        ImageView homeButton = (ImageView) findViewById(R.id.home_button);


        if (level.getId() + 1 < packageSize) {
            nextButton.setImageBitmap(imageManager.loadImageFromResource(R.drawable.next_button, lengthManager.getLevelFinishedButtonsSize(), lengthManager.getLevelFinishedButtonsSize()));
            tools.resizeView(nextButton, lengthManager.getLevelFinishedButtonsSize(), lengthManager.getLevelFinishedButtonsSize());
            nextButton.setOnClickListener(this);

        } else {
            nextButton.setVisibility(View.GONE);
            findViewById(R.id.separatorSpace).setVisibility(View.GONE);
        }

        homeButton.setImageBitmap(imageManager.loadImageFromResource(R.drawable.home_button, lengthManager.getLevelFinishedButtonsSize(), lengthManager.getLevelFinishedButtonsSize()));
        tools.resizeView(homeButton, lengthManager.getLevelFinishedButtonsSize(), lengthManager.getLevelFinishedButtonsSize());
        homeButton.setOnClickListener(this);

        TextView levelSolution = (TextView) findViewById(R.id.level_solution);
        if(level.getType().equals("keyboard"))
        customizeTextView(levelSolution,level.getAnswer().replace(".", " ").replace("/"," "), lengthManager.getLevelSolutionTextSize());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_button:
                finishLevel.Home();
                break;

            case R.id.next_level_button:
                finishLevel.NextLevel();
                break;
        }

        dismiss();
    }

    private void customizeTextView(TextView textView, String label, float textSize) {
        textView.setText(label);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, lengthManager.getStoreItemFontSize());
        textView.setTextColor(Color.WHITE);
        textView.setShadowLayer(1, 2, 2, Color.BLACK);
        textView.setTypeface(FontsHolder.getSansBold(textView.getContext()));
    }
}