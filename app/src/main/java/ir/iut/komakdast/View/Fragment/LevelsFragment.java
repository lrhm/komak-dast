package ir.iut.komakdast.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.iut.komakdast.Adapter.DBAdapter;
import ir.iut.komakdast.Adapter.LevelsAdapter;
import ir.iut.komakdast.Object.Level;
import ir.iut.komakdast.R;

public class LevelsFragment extends Fragment {

    private static final String TAG = "LevelsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_levels, container, false);

        int page = getArguments().getInt(PackageFragment.LEVEL_PAGE);
        int packageId = getArguments().getInt("id");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.levels_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        DBAdapter db = DBAdapter.getInstance(getActivity());
        Level[] levels = db.getLevels(packageId);

        LevelsAdapter adapter = new LevelsAdapter(getActivity(), packageId, page, levels);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        if (getActivity().getSupportFragmentManager().findFragmentByTag(LevelsAdapter.OFFLINE_GAME_FRAGMENT_TAG) != null) {
            VideoGameFragment gameFragment = (VideoGameFragment) getActivity().getSupportFragmentManager().findFragmentByTag(LevelsAdapter.OFFLINE_GAME_FRAGMENT_TAG);
            getActivity().getSupportFragmentManager().beginTransaction().remove(gameFragment).commitAllowingStateLoss();
        }
    }
}
