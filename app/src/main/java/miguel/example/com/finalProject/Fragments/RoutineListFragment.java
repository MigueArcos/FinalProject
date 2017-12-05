package miguel.example.com.finalProject.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import miguel.example.com.finalProject.Adapters.RoutineListAdapter;
import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Models.Routine;
import miguel.example.com.finalProject.R;

/**
 * Created by Miguel on 19/07/2017.
 */
public class RoutineListFragment extends Fragment implements FirebaseServices.RoutineReadyListener{
    private RecyclerView list;
    private RoutineListAdapter adapter;
    private List<Routine> data;
    private SwipeRefreshLayout refreshLayout;
    private TextView emptyList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routine_list, container, false);
        list = rootView.findViewById(R.id.list);
        emptyList = rootView.findViewById(R.id.empty_list);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        adapter = new RoutineListAdapter();
        list.setAdapter(adapter);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh);
        refreshLayout.setRefreshing(true);
        FirebaseServices.getInstance(getActivity()).getRoutine("routine", this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirebaseServices.getInstance().getRoutine("routine", RoutineListFragment.this);
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyList.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
                super.onChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onRoutineReady(List<Routine> routineList) {
        data = routineList;
        adapter.setData(data);
        refreshLayout.setRefreshing(false);
        Log.d("sommme", ""+data.size());
    }

    @Override
    public void onRoutineError(String error) {
        Log.d("Error getting routine", error);
        refreshLayout.setRefreshing(false);
    }
}
