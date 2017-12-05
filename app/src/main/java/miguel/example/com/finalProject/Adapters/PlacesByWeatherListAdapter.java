package miguel.example.com.finalProject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import miguel.example.com.finalProject.Models.OpenWeather;
import miguel.example.com.finalProject.Models.Place;
import miguel.example.com.finalProject.R;
import miguel.example.com.finalProject.VolleySingleton;


/**
 * Created by Miguel on 20/06/2016.
 */
public class PlacesByWeatherListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Place> data;
    private AdapterActions listener;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OpenWeather weather;

    public PlacesByWeatherListAdapter(List<Place> data, AdapterActions listener) {
        this.data = data;
        this.listener = listener;
    }


    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<Place> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setWeather(OpenWeather weather) {
        this.weather = weather;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
            return new ItemView(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.icon.setImageResource(data.get(position).getIcon());
        if (position == 0) {
            ((HeaderViewHolder) holder).bindHolder();
        } else {
            ((ItemView) holder).bindHolder(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
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
        private TextView title, subtitle;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }

        public void bindHolder(int position) {
            icon.setImageUrl(data.get(position - 1).getIcon(), VolleySingleton.getInstance().getImageLoader());
            title.setText(data.get(position - 1).getName());
            subtitle.setText(data.get(position - 1).getVicinity());
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition() - 1);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView icon;
        private TextView weatherDescription, weatherTemperature, weatherMinTemp, weatherMaxTemp;

        HeaderViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            weatherDescription = itemView.findViewById(R.id.weather_description);
            weatherTemperature = itemView.findViewById(R.id.weather_temperature);
            weatherMinTemp = itemView.findViewById(R.id.weather_min_temperature);
            weatherMaxTemp = itemView.findViewById(R.id.weather_max_temperature);
        }

        public void bindHolder() {
            if (weather != null){
                //icon.setImageUrl(weather.getWeather().getIcon(), VolleySingleton.getInstance().getImageLoader());
                weatherDescription.setText(weather.getWeather()[0].getDescription());
                weatherTemperature.setText(String.valueOf(weather.getMain().getTemp()));
                weatherMinTemp.setText(String.valueOf(weather.getMain().getTemp_min()));
                weatherMaxTemp.setText(String.valueOf(weather.getMain().getTemp_max()));
            }
        }

    }
}