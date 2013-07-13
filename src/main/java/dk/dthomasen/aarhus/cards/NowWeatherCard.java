package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.activity.TouchImageViewActivity;
import dk.dthomasen.aarhus.service.DownloadImage;
import dk.dthomasen.aarhus.service.Service;

public class NowWeatherCard extends Card{

    Context context;
    String tempNow;
    String weathercode;
    String wind;

    public NowWeatherCard(Context context, String title, String tempNow, String weathercode, String wind){
        super(title);
        this.context = context;
        this.tempNow=tempNow;
        this.weathercode=weathercode;
        this.wind = wind;;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_nowweather, null);

        ((TextView) view.findViewById(R.id.maintitle)).setText(title);
        ((TextView) view.findViewById(R.id.CardTempRightNow)).setText(tempNow);
        ((ImageView) view.findViewById(R.id.weatherNowIcon)).setImageBitmap(Service.getInstance().mapCodeToIcon(context, weathercode));
        ((TextView) view.findViewById(R.id.CardWindRightNow)).setText("Vind: "+wind+" km/t");

        return view;
    }
}