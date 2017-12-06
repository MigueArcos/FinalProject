package miguel.example.com.finalProject.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import miguel.example.com.finalProject.Activities.ViewTvShowSiteActivity;
import miguel.example.com.finalProject.Adapters.TvShowsListAdapter;
import miguel.example.com.finalProject.Models.TvShow;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;

/**
 * Created by Miguel on 19/07/2017.
 */
public class TvShowsListFragment extends Fragment implements VolleySingleton.TvShowsListener, TvShowsListAdapter.AdapterActions {
    private RecyclerView list;
    private TvShowsListAdapter adapter;
    private List<TvShow> data;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_shows_list, container, false);
        list = rootView.findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        adapter = new TvShowsListAdapter(this);
        list.setAdapter(adapter);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh);
        refreshLayout.setRefreshing(true);
        VolleySingleton.getInstance(getActivity()).getTvShowsList(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VolleySingleton.getInstance(getActivity()).getTvShowsList(TvShowsListFragment.this);
            }
        });

        return rootView;
    }


    @Override
    public void onClick(int position) {
        Intent i = new Intent(getActivity(), ViewTvShowSiteActivity.class);
        i.putExtra("officialSite", data.get(position).getOfficialSite());
        i.putExtra("name", data.get(position).getName());
        i.putExtra("url", data.get(position).getUrl());
        startActivity(i);
    }

    @Override
    public void onTvShowsReady(List<TvShow> tvShows) {
        data = tvShows;
        adapter.setData(tvShows);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onTvShowsError(String error) {
        Log.d("TVShows", "An error has occurred with tv shows");
        refreshLayout.setRefreshing(false);
    }
}
