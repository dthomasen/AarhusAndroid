package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.FitnessRatings;
import dk.dthomasen.aarhus.service.FitnessRatingsDownload;

public class FitnessTitleCard extends com.fima.cardsui.objects.Card {

    int fitnessId;

    public FitnessTitleCard(String title, int fitnessId){
        super(title);
        this.fitnessId = fitnessId;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_static_rating, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);

        new FitnessRatingsDownload((RatingBar) view.findViewById(R.id.staticRatingBar)).execute(1, fitnessId);
        return view;
    }
}
