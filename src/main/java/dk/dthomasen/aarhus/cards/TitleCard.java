package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.ShelterRatings;
import dk.dthomasen.aarhus.service.RatingsDownload;

public class TitleCard extends com.fima.cardsui.objects.Card {

    int shelterId;

    public TitleCard(String title, int shelterId){
        super(title);
        this.shelterId = shelterId;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_static_rating, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);

        ShelterRatings shelterRatings = new ShelterRatings();

        new RatingsDownload((RatingBar) view.findViewById(R.id.staticRatingBar)).execute(1,shelterId);
        return view;
    }
}
