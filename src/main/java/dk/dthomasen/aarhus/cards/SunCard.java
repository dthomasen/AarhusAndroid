package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.service.Service;

public class SunCard extends Card{

    String sunup;
    String sundown;

    public SunCard(String title, String sunup, String sundown){
        super(title);
        this.sunup = sunup;
        this.sundown = sundown;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_sun, null);

        ((TextView) view.findViewById(R.id.CardSunTitle)).setText(title);
        ((TextView) view.findViewById(R.id.CardSunUp)).setText("Opgang: kl."+Service.getInstance().convert12To24Hour(sunup));
        ((TextView) view.findViewById(R.id.CardSunDown)).setText("Nedgang: kl."+Service.getInstance().convert12To24Hour(sundown));

        return view;
    }
}