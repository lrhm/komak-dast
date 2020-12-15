package xyz.lrhm.komakdast.View.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.lrhm.komakdast.Adapter.DBAdapter;
import xyz.lrhm.komakdast.Adapter.PackageAdapter;
import xyz.lrhm.komakdast.Object.PackageObject;
import xyz.lrhm.komakdast.R;
import xyz.lrhm.komakdast.Util.PackageTools;

public class PackagesFragment extends Fragment implements PackageTools.OnNewPackageFoundListener {
    private RecyclerView recyclerView;
    private PackageAdapter adapter;
    int type;
    private DBAdapter db;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package, container, false);

        db = DBAdapter.getInstance(getActivity());
//        type = getArguments().getInt(MainFragment.FRAGMENT_TYPE);

        recyclerView = (RecyclerView) view.findViewById(R.id.package_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        PackageObject[] downloadedPackage = db.getPackages();

        PackageTools.getInstance(getContext()).checkForNewPackage(this);


        adapter = new PackageAdapter(getActivity(), downloadedPackage);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNewPackage(final PackageObject packageObject) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.addPackage(packageObject);

            }
        });

    }

    @Override
    public void onPackageInvalid(final PackageObject packageObject) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                adapter.removePackage(packageObject);
            }
        });
    }

    @Override
    public void onPackageOffer(PackageObject newOffer) {
        adapter.updatePackage(newOffer);
    }
}