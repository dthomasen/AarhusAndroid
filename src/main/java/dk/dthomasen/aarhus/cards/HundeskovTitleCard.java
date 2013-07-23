package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.HundeskovRatings;
import dk.dthomasen.aarhus.models.ShelterRatings;
import dk.dthomasen.aarhus.service.HundeskovRatingsDownload;
import dk.dthomasen.aarhus.service.ShelterRatingsDownload;

public class HundeskovTitleCard extends com.fima.cardsui.objects.Card {

    int hundeskovId;

    public HundeskovTitleCard(String title, int hundeskovId){
        super(title);
        this.hundeskovId = hundeskovId;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_static_rating, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);

        HundeskovRatings hundeskovRatings = new HundeskovRatings();

        new HundeskovRatingsDownload((RatingBar) view.findViewById(R.id.staticRatingBar)).execute(1, hundeskovId);
        return view;
    }
}
