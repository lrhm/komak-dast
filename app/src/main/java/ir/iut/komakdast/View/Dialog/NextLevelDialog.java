package ir.iut.komakdast.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import ir.iut.komakdast.Interface.FinishLevel;
import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.Object.Level;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.FontsHolder;
import ir.iut.komakdast.Util.LengthManager;
import ir.iut.komakdast.Util.UiUtil;

public class NextLevelDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Level level;
    private int packageSize;
    private int prize;
    private FinishLevel finishLevel;
    private LengthManager lengthManager;
    private boolean skiped;

    public NextLevelDialog(Context context, Level level, int packageSize, boolean skiped, int prize, FinishLevel finishLevel) {
        super(context);
        this.context = context;
        this.level = level;
        this.packageSize = packageSize + 1;
        this.prize = prize;
        this.finishLevel = finishLevel;
        this.skiped = skiped;
        lengthManager = ((MainApplication) context.getApplicationContext()).getLengthManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);


        setContentView(R.layout.dialog_next_level);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView prizeTextView = (TextView) findViewById(R.id.prizeTextView);

        ImageView scarf = findViewById(R.id.scarf_image_view);

        if (!level.isResolved() && !skiped) {
            String prizeString;
            if (prize == 30)
                prizeString = "+۳۰";
            else if (prize == 10)
                prizeString = "+۱۰";
            else
                prizeString = "+۵";

            customizeTextView(prizeTextView, prizeString, lengthManager.getLevelAuthorTextSize());

            Glide.with(getContext()).load(R.drawable.scarf).into(scarf);


        } else {
            prizeTextView.setVisibility(View.GONE);

            scarf.setVisibility(View.GONE);
        }

        int width = (int) (lengthManager.getScreenWidth() * 0.8f);
        int height = lengthManager.getHeightWithFixedWidth(

                R.drawable.dialog_background, (int) (lengthManager.getScreenWidth() * 0.8f)
        );

        ConstraintLayout container = findViewById(R.id.dialog_container);


//        container.setMaxWidth(width);
        container.setMinWidth(width);
        container.setMinHeight(height);

        ImageView background = findViewById(R.id.dialog_background);
        UiUtil.setWidth(background, width);
        UiUtil.setHeight(background, height);


        Glide.with(getContext()).load(R.drawable.dialog_background).into(background);




        ImageView nextButton = findViewById(R.id.next_level_button);
        Glide.with(getContext()).load(R.drawable.next_button_dialog).into(nextButton);

        nextButton.setOnClickListener(this);

        ImageView homeButton = findViewById(R.id.home_button);
        Glide.with(getContext()).load(R.drawable.hoem_button).into(homeButton);

        homeButton.setOnClickListener(this);

        ImageView tick = findViewById(R.id.tick);
        Glide.with(getContext()).load(R.drawable.tick).into(tick);

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