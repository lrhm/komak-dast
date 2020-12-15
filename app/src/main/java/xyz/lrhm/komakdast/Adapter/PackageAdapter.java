package xyz.lrhm.komakdast.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import xyz.lrhm.komakdast.API.Rest.AppAPIAdapter;
import xyz.lrhm.komakdast.API.Rest.Interfaces.OnPackageBuyListener;
import xyz.lrhm.komakdast.Adapter.Cache.PackageSolvedCache;
import xyz.lrhm.komakdast.MainApplication;
import xyz.lrhm.komakdast.Object.PackageObject;
import xyz.lrhm.komakdast.Object.User;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.DownloadTask;
import xyz.lrhm.komakdast.Util.FontsHolder;
import xyz.lrhm.komakdast.Util.ImageManager;
import xyz.lrhm.komakdast.Util.LengthManager;
import xyz.lrhm.komakdast.Util.Logger;
import xyz.lrhm.komakdast.Util.PackageTools;
import xyz.lrhm.komakdast.Util.SizeManager;
import xyz.lrhm.komakdast.Util.Tools;
import xyz.lrhm.komakdast.Util.UiUtil;
import xyz.lrhm.komakdast.View.Activity.MainActivity;
import xyz.lrhm.komakdast.View.Custom.ToastMaker;
import xyz.lrhm.komakdast.View.Dialog.PackagePurchaseDialog;
import xyz.lrhm.komakdast.View.Fragment.PackageFragment;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    private static final String TAG = "PackageAdapter";
    public static final String PACKAGE_LEVEL_LIST_TAG = "LevelsActivityNew1";
    private ArrayList<PackageObject> packageObjects;
    private Activity context;
    private Tools tools;
    private LengthManager lengthManager;

    public PackageAdapter(Activity context, PackageObject[] packageObjectss) {
        this.context = context;
        this.packageObjects = new ArrayList<>();

        if (packageObjectss == null)
            packageObjectss = DBAdapter.getInstance(context).getPackages();
        if (packageObjectss != null)
            Collections.addAll(packageObjects, packageObjectss);

        Collections.sort(packageObjects, new Comparator<PackageObject>() {
            @Override
            public int compare(PackageObject object, PackageObject t1) {
                return t1.getId() - object.getId();
            }
        });

        tools = new Tools(context);
        lengthManager = ((MainApplication) context.getApplicationContext()).getLengthManager();
    }

    public void removePackage(PackageObject object) {
        packageObjects.remove(object);
        notifyDataSetChanged();

    }

    public void updatePackage(PackageObject object) {
        int i;
        for (i = 0; i < packageObjects.size(); i++)
            if (packageObjects.get(i).getId() == object.getId())
                break;
        try {
            PackageObject packageObject = packageObjects.get(i);
            if (packageObject.getId() == object.getId()) {
                packageObjects.remove(i);
                addPackage(object);
            }

        } catch (Exception e) {

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DownloadTask.DownloadTaskListener {
        ImageView imageView;
        TextView textView;
        ImageView packagePrice;
        ImageView packageDone;
        TextView price;

        private long lastTimeClicked = 0;
        private long timeStamp = 1000;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) itemView.findViewById(R.id.itemPackage);
            textView = (TextView) itemView.findViewById(R.id.package_progress_dl);
            price = (TextView) itemView.findViewById(R.id.package_coin_text);
            packagePrice = (ImageView) itemView.findViewById(R.id.package_coin_image);
            packageDone = (ImageView) itemView.findViewById(R.id.package_done);

            if (SizeManager.getScreenWidth() < 800)
                textView.setShadowLayer(0.5f, 1, 1, Color.BLACK);
            textView.setTypeface(FontsHolder.getNumeralSansMedium(v.getContext()));
            UiUtil.setTextViewSize(textView, (int) (SizeManager.getScreenWidth() * 0.3), 0.4f);

            if (SizeManager.getScreenWidth() < 800)
                price.setShadowLayer(0.5f, 1, 1, Color.BLACK);
            price.setTypeface(FontsHolder.getNumeralSansMedium(v.getContext()));
            UiUtil.setTextViewSize(price, (int) (SizeManager.getScreenWidth() * 0.3), 0.13f);


            int packageSize = (int) (SizeManager.getScreenWidth() * 0.47);
            imageView.getLayoutParams().height = (int) (packageSize * 0.95);
            imageView.getLayoutParams().width = packageSize;

            UiUtil.setWidth(packagePrice, packageSize);
            UiUtil.setHeight(packagePrice, packageSize);

            int textSize = (int) (packageSize * 0.11);
            UiUtil.setLeftMargin(price, textSize);
            UiUtil.setTopMargin(price, (int) (packageSize * 0.09));
            UiUtil.setWidth(price, (int) (packageSize * 0.22));
            UiUtil.setHeight(price, (int) (packageSize * 0.22));


            price.setText(Tools.numeralStringToPersianDigits(""));
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() - lastTimeClicked < timeStamp)
                return;

            lastTimeClicked = System.currentTimeMillis();

            final int id = packageObjects.get(getAdapterPosition()).getId();
            final PackageObject packageObject = packageObjects.get(getAdapterPosition());



            if (!packageObject.isPackageDownloaded(context)) { // not downloaded


                Logger.d(TAG, "package is not downloaded ");
                final CoinAdapter coinAdapter = ((MainActivity) context).getCoinAdapter();


                new PackagePurchaseDialog(context, packageObject).setYesClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        User myUser = Tools.getCachedUser(context);

                        int intPrice = ((myUser != null && myUser.isPackagePurchased(packageObject.getId()))) ? 0 : packageObject.getPrice();
                        intPrice = PackageSolvedCache.getInstance().isPackagePurchased(packageObject.getId()) ? 0 : intPrice;

                        if (coinAdapter.spendCoins(intPrice)) {
                            AppAPIAdapter.buyPackage(id, new OnPackageBuyListener() {
                                @Override
                                public void onPurchasedBefore() {


                                    Logger.d(TAG, "before");
                                }

                                @Override
                                public void onPurchaseSuccess() {

                                    Logger.d(TAG, "success");
                                }

                                @Override
                                public void onFail() {

                                    Logger.d(TAG, "fail");
                                }
                            });
                            PackageSolvedCache.getInstance().onBuyPackage(packageObject.getId());
                            price.setText(Tools.numeralStringToPersianDigits("0"));
                            PackageTools.getInstance(context).downloadPackage(packageObject, ViewHolder.this);
                            ToastMaker.show(context, "درحال دانلود....", Toast.LENGTH_SHORT);
                        }
                    }
                }).show();


            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);

                PackageFragment packageFragment = new PackageFragment();
                packageFragment.setArguments(bundle);

                FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, packageFragment, PACKAGE_LEVEL_LIST_TAG);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        }


        @Override
        public void onProgress(final int progress) {
            Logger.d(TAG, "on progress" + progress);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (textView.getVisibility() != View.VISIBLE)
                        textView.setVisibility(View.VISIBLE);
                    textView.setText(Tools.numeralStringToPersianDigits(progress + "") + "%");

                    if (progress == 100)
                        textView.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onDownloadSuccess() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastMaker.show(context, "دانلود شد", Toast.LENGTH_SHORT);

                    packagePrice.setVisibility(View.GONE);
                    price.setVisibility(View.GONE);
                    ColorMatrix matrix = new ColorMatrix();

                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageView.setColorFilter(filter);

                    textView.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onDownloadError(String error) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastMaker.show(context, context.getResources().getString(R.string.try_later), Toast.LENGTH_SHORT);

                    textView.setVisibility(View.GONE);
                }
            });

        }
    }

    @Override
    public PackageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_package, viewGroup, false);
        int packageSize = (int) (SizeManager.getScreenWidth() * 0.5);
        v.setLayoutParams(new RecyclerView.LayoutParams(
                packageSize,
                packageSize));

//        int padding = (int) (SizeManager.getScreenWidth() * 0.01 );
//
//        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
//        params.leftMargin = padding * 2;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PackageAdapter.ViewHolder viewHolder, int i) {


        int onePercent = (int) (SizeManager.getScreenWidth() * 0.013);

        if (i % 2 == 0) {
            viewHolder.imageView.setPadding(onePercent, 0, 0, 0);
            UiUtil.setLeftMargin(viewHolder.packagePrice, onePercent);
            UiUtil.setLeftMargin(viewHolder.packageDone, onePercent);
//            UiUtil.setLeftMargin(viewHolder.price, onePercent * 2);

            viewHolder.price.setPadding((int) (onePercent * 2), 0, 0, 0);

        } else {

            viewHolder.imageView.setPadding(0, 0, onePercent, 0);


        }


        int id = packageObjects.get(i).getId();
        PackageObject packageObject = packageObjects.get(i);

        DBAdapter dbAdapter = DBAdapter.getInstance(context);

        User myUser = Tools.getCachedUser(context);

        int intPrice = ((myUser != null && myUser.isPackagePurchased(id))) ? 0 : packageObjects.get(i).getPrice();
        intPrice = PackageSolvedCache.getInstance().isPackagePurchased(id) ? 0 : intPrice;

        String imagePath = "file://" + context.getFilesDir().getPath() + "/package_" + id + "_" + "front" + ".png";

        if (packageObject.isThereOffer() && !packageObject.isPackageDownloaded(context)
                && intPrice != 0)
            imagePath = "file://" + packageObject.getOfferImagePathInSD(context);

        Glide.with(context).load(Uri.parse(imagePath)).fitCenter().into(viewHolder.imageView);


        File file = new File(context.getFilesDir().getPath() + "/Packages/package_" + id + "/");

        String assetsPath = " Packages/package_" + id + "/";



//        Level[] list = dbAdapter.getLevels(id);
//        if (list != null && list[list.length - 1].isResolved()) {
//            Glide.with(context).load(R.drawable.package_win).into(viewHolder.packageDone);
//            viewHolder.packageDone.setVisibility(View.VISIBLE);
//        }

        Glide.with(context).load(R.drawable.package_price).into(viewHolder.packagePrice);

        if (!packageObject.isPackageDownloaded(context)) {


            viewHolder.packagePrice.setVisibility(View.VISIBLE);
            viewHolder.price.setVisibility(View.VISIBLE);
            viewHolder.price.setText(Tools.numeralStringToPersianDigits(intPrice + ""));


            if (!packageObject.isThereOffer())
                ImageManager.getInstance(context).toGrayscale(viewHolder.imageView);

        } else
            viewHolder.packagePrice.setVisibility(View.GONE);

        if (!(new File(context.getFilesDir().getPath() + "/package_" + id + "_" + "front" + ".png").exists())
                ) {
            viewHolder.packagePrice.setVisibility(View.GONE);
            viewHolder.price.setVisibility(View.GONE);
        }
        if (packageObject.isThereOffer() && !packageObject.isPackageDownloaded(context)) {
            viewHolder.packagePrice.setVisibility(View.VISIBLE);
            viewHolder.price.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        if (packageObjects == null)
            return 0;
        return packageObjects.size();
    }

    public void addPackage(PackageObject packageObject) {

        if (packageObject.shouldShowPackage(context)) {
            packageObjects.add(packageObject);
            notifyDataSetChanged();
        }

    }
}
