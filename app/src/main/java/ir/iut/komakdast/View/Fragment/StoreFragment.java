package ir.iut.komakdast.View.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import ir.iut.komakdast.Adapter.CoinAdapter;
import ir.iut.komakdast.Adapter.DBAdapter;
import ir.iut.komakdast.MainApplication;
import ir.iut.komakdast.R;
import ir.iut.komakdast.Util.FontsHolder;
import ir.iut.komakdast.Util.ImageManager;
import ir.iut.komakdast.Util.LengthManager;
import ir.iut.komakdast.Util.StoreAdapter;
import ir.iut.komakdast.Util.Tools;
import ir.iut.komakdast.Util.UiUtil;
import ir.iut.komakdast.View.Activity.MainActivity;
import ir.iut.komakdast.View.Custom.DialogDrawable;

public class StoreFragment extends Fragment {
    private Tools tools;
    private DBAdapter db;
    private ImageManager imageManager;
    private LengthManager lengthManager;
    CoinAdapter coinAdapter;

    public static final String SKU_VERY_SMALL_COIN = "very_small_coin";
    public static final String SKU_SMALL_COIN = "small_coin";
    public static final String SKU_MEDIUM_COIN = "medium_coin";
    public static final String SKU_BIG_COIN = "big_coin";

    static final int[] buttonIds = new int[]{
            R.id.very_small_coin,
            R.id.small_coin,
            R.id.medium_coin,
            R.id.big_coin
    };


    private View layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_store, container, false);

        coinAdapter = ((MainActivity) getActivity()).getCoinAdapter();

        tools = new Tools(getActivity());
        db = DBAdapter.getInstance(getActivity());
        imageManager = ((MainApplication) getActivity().getApplicationContext()).getImageManager();
        lengthManager = ((MainApplication) getActivity().getApplicationContext()).getLengthManager();

        int margin = lengthManager.getStoreDialogMargin();
        layout.setPadding(margin, margin, margin, margin);
        View dialog = layout.findViewById(R.id.dialog);
        tools.setViewBackground(dialog, new DialogDrawable(getActivity()));

        int padding = lengthManager.getStoreDialogPadding();
        dialog.setPadding(padding, padding, padding, padding);


        for (int i = 0; i < StoreAdapter.getSKUs().length; i++) {
            final int finalI = i;
            layout.findViewById(buttonIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    coinAdapter.earnCoinDiffless(StoreAdapter.getRevenues()[finalI]);
//                    ((MainActivity) getActivity()).purchase(StoreAdapter.getSKUs()[finalI], StoreAdapter.getPrices()[finalI]);
                }
            });
        }

//        final View reviewBazaar = layout.findViewById(R.id.review_cafebazaar);
//        if (db.getCoinsReviewed()) {
//            reviewBazaar.setVisibility(View.GONE);
//        } else {
//            reviewBazaar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    db.updateReviewed(true);
//
//
//                    Intent browserIntent = new Intent(Intent.ACTION_EDIT, Uri.parse("http://cafebazaar.ir/app/ir.treeco.komakdast2/?l=fa"));
//                    startActivity(browserIntent);
//
//                    coinAdapter.earnCoins(StoreAdapter.getCommentBazaarAmount());
//
//                    reviewBazaar.setVisibility(View.GONE);
//                }
//            });
//        }

        final View tapsell = layout.findViewById(R.id.tapsell_free_coin);
        tapsell.setVisibility(View.GONE);

//        setupInstaAndMore();

        ImageView shopTitle = (ImageView) layout.findViewById(R.id.shop_title);
        Bitmap shopTitleBitmap = imageManager.loadImageFromResource(R.drawable.shoptitle, lengthManager.getShopTitleWidth(), -1);

        shopTitle.setImageBitmap(shopTitleBitmap);
        tools.resizeView(shopTitle, shopTitleBitmap.getWidth(), shopTitleBitmap.getHeight());
        ((ViewGroup.MarginLayoutParams) shopTitle.getLayoutParams()).bottomMargin = lengthManager.getShopTitleBottomMargin();

        setupItemsList();

        return layout;
    }
//
//    void setupInstaAndMore() {
//        final View telegram = layout.findViewById(R.id.review_telegram);
//
//        if (StoreAdapter.isTelegramUsed())
//            telegram.setVisibility(View.GONE);
//        else
//            telegram.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (StoreAdapter.startTelegramIntent(getContext())) {
//                        coinAdapter.earnCoins(StoreAdapter.getTelegramAmount());
//                        StoreAdapter.useTelegram();
//                        telegram.setVisibility(View.GONE);
//                    }
//                }
//
//            });
//
//        final View insta = layout.findViewById(R.id.review_insta);
//
//        if (StoreAdapter.isInstaUsed())
//            insta.setVisibility(View.GONE);
//        else
//            insta.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (StoreAdapter.startInstaIntent(getContext())) {
//                        coinAdapter.earnCoins(StoreAdapter.getInstaAmount());
//                        StoreAdapter.useInsta();
//                        insta.setVisibility(View.GONE);
//                    }
//                }
//            });
//    }

    private void setupItemsList() {

        int[] revenues = StoreAdapter.getRevenues();
        int[] prices = StoreAdapter.getPrices();

        LinearLayout itemsList = (LinearLayout) layout.findViewById(R.id.items_list);

        FrameLayout[] items = new FrameLayout[4];
        for (int i = 0; i < items.length; i++)
            items[i] = (FrameLayout) itemsList.getChildAt(i);

        for (int i = 0; i < items.length; i++) {
            String persianPrice = "فقط " + tools.numeralStringToPersianDigits("" + prices[i]) + " تومان";

            if (i == 6)
                persianPrice = "فالو اینستا";
            if (i == 5)
                persianPrice = "عضو کانال تلگرام";
            if (i == 4) {
                persianPrice = "تبلیغ ببین سکه ببر";

            }
            setupItem(items[i], persianPrice, revenues[i], i % 2 == 1);
        }
    }

    private void customizeTextView(TextView textView, String label) {
        textView.setText(label);

        textView.setTypeface(FontsHolder.getSansMedium(textView.getContext()));

        UiUtil.setTextViewSize(textView, lengthManager.getStoreItemHeight(), 0.275f);
        textView.setTextColor(Color.WHITE);

        textView.setShadowLayer(1, 2, 2, Color.BLACK);
    }

    private void setupItem(FrameLayout item, String label, int revenueAmount, boolean reversed) {
        final ViewGroup.LayoutParams itemLayoutParams = item.getLayoutParams();
        itemLayoutParams.height = lengthManager.getStoreItemHeight();
        itemLayoutParams.width = lengthManager.getStoreItemWidth();

        ImageView itemBackground = (ImageView) item.findViewById(R.id.item_background);
        itemBackground.setImageBitmap(imageManager.loadImageFromResource(reversed ? R.drawable.single_button_green : R.drawable.single_button_red, lengthManager.getStoreItemWidth(), lengthManager.getStoreItemHeight()));

        TextView title = (TextView) item.findViewById(R.id.label);
        customizeTextView(title, label);

        TextView revenue = (TextView) item.findViewById(R.id.price);
        customizeTextView(revenue, tools.numeralStringToPersianDigits("" + revenueAmount));

//        if (reversed) {
//            LinearLayout textViews = (LinearLayout) item.findViewById(R.id.text_views);
//            tools.reverseLinearLayout(textViews);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).setStore(false);
    }
}