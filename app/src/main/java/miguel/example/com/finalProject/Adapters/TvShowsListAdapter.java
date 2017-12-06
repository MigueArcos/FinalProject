package miguel.example.com.finalProject.Adapters;

/**
 * Created by 79812 on 05/12/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import miguel.example.com.finalProject.Models.TvShow;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;


public class TvShowsListAdapter extends RecyclerView.Adapter<TvShowsListAdapter.ItemView> {
    private List<TvShow> data;
    private AdapterActions listener;

    public TvShowsListAdapter(AdapterActions listener) {
        data = new ArrayList<>();
        this.listener = listener;
    }


    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<TvShow> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);
        ItemView holder = new ItemView(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.icon.setImageUrl(data.get(position).getImage().getMedium(), VolleySingleton.getInstance().getImageLoader());
        holder.showName.setText(data.get(position).getName());
        holder.showSummary.setText(Html.fromHtml(data.get(position).getSummary()));
        holder.showGenre.setText("Género(s): "+data.get(position).getFormattedGenre());
        holder.showLanguage.setText("Idioma: "+data.get(position).getLanguage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface AdapterActions {
        void onClick(int position);
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NetworkImageView icon;
        private TextView showName, showSummary, showGenre, showLanguage;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.icon);
            showName = itemView.findViewById(R.id.show_name);
            showSummary = itemView.findViewById(R.id.show_summary);
            showGenre = itemView.findViewById(R.id.show_genre);
            showLanguage = itemView.findViewById(R.id.show_language);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }


}
