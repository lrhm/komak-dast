package ir.treeco.aftabe2.View.Fragment;

import android.os.Bundle;

import ir.treeco.aftabe2.Util.Logger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.treeco.aftabe2.API.Rest.AftabeAPIAdapter;
import ir.treeco.aftabe2.API.Rest.Interfaces.BatchUserFoundListener;
import ir.treeco.aftabe2.Adapter.LeaderboardAdapter;
import ir.treeco.aftabe2.Object.User;
import ir.treeco.aftabe2.Util.Tools;


/**
 * Created by al on 1/22/16.
 */
public class LeaderboardFragment extends Fragment implements BatchUserFoundListener {


    LeaderboardAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAdapter = new LeaderboardAdapter(null);
        recyclerView.setAdapter(mAdapter);

        AftabeAPIAdapter.getLoaderboard(Tools.getCachedUser(getActivity()), this);

        return recyclerView;
    }

    @Override
    public void onGotUserList(User[] users) {

        Logger.d("TAG", "on got leaderboad size of " + users.length);
        for (int i = 0; i < users.length; i++) {
            mAdapter.addUser(users[i]);
        }
    }

    @Override
    public void onGotError() {


    }
}
