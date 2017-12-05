package miguel.example.com.finalProject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;


/**
 * Created by Miguel on 20/06/2016.
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ItemView> {
    private List<Place> data;
    private AdapterActions listener;

    public PlacesListAdapter(List<Place> data, AdapterActions listener) {
        this.data = data;
        this.listener = listener;
    }


    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<Place> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        ItemView holder = new ItemView(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        //holder.icon.setImageResource(data.get(position).getIcon());
        holder.icon.setImageUrl(data.get(position).getIcon(), VolleySingleton.getInstance().getImageLoader());
        holder.title.setText(data.get(position).getName());
        holder.subtitle.setText(data.get(position).getVicinity());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface AdapterActions {
        void onClick(int position);

        void onSwipe(int position);
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NetworkImageView icon;
        private TextView title, subtitle;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }



}
