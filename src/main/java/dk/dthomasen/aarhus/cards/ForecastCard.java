package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.service.Service;
import dk.dthomasen.aarhus.weather.Weather;

public class ForecastCard extends Card{

    Weather weather;
    Context context;

    public ForecastCard(Context context, String title, Weather weather){
        super(title);
        this.context = context;
        this.weather = weather;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_forecast, null);

        ((TextView) view.findViewById(R.id.CardForecastTitle)).setText(title);
        ((TextView) view.findViewById(R.id.day1)).setText(weather.getTodayDay());
        ((ImageView) view.findViewById(R.id.weathericon1)).setImageBitmap(Service.getInstance().mapCodeToIcon(context, weather.getTodayCode()));
        ((TextView) view.findViewById(R.id.temp1)).setText(weather.getTodayTemp()+"°C");
        ((TextView) view.findViewById(R.id.day2)).setText(weather.getTommorowDay());
        ((TextView) view.findViewById(R.id.temp2)).setText(weather.getTommorowTemp()+"°C");
        ((ImageView) view.findViewById(R.id.weathericon2)).setImageBitmap(Service.getInstance().mapCodeToIcon(context, weather.getTommorowCode()));
        ((TextView) view.findViewById(R.id.day3)).setText(weather.gettTommorowDay());
        ((TextView) view.findViewById(R.id.temp3)).setText(weather.gettTommorowTemp()+"°C");
        ((ImageView) view.findViewById(R.id.weathericon3)).setImageBitmap(Service.getInstance().mapCodeToIcon(context, weather.gettTommorowCode()));

        return view;
    }
}