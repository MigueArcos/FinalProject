package miguel.example.com.finalProject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.Models.Routine;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;


public class RoutineListAdapter extends RecyclerView.Adapter<RoutineListAdapter.ItemView> {
    private List<Routine> data;

    public RoutineListAdapter() {
        data = new ArrayList<>();
    }


    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<Routine> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        ItemView holder = new ItemView(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        //holder.icon.setImageResource(data.get(position).getIcon());
        switch (data.get(position).getBaseActivity()){
            case "Visitar":
                holder.icon.setImageResource(R.drawable.visit_icon);
                break;
            case "Ver TV":
                holder.icon.setImageResource(R.drawable.tv_icon);
                break;
            case "Jugar":
                holder.icon.setImageResource(R.drawable.videogame_icon);
                break;
        }
        holder.routineActivity.setText(data.get(position).getActivity());
        holder.routineDate.setText(data.get(position).getDateTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView routineActivity, routineDate;

        ItemView(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            routineActivity = itemView.findViewById(R.id.routine_activity);
            routineDate = itemView.findViewById(R.id.routine_date);
        }
    }



}
