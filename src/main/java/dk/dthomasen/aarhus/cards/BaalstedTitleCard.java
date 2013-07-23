package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.service.BaalstedRatingsDownload;

public class BaalstedTitleCard extends com.fima.cardsui.objects.Card {

    int baalstedId;

    public BaalstedTitleCard(String title, int baalstedId){
        super(title);
        this.baalstedId = baalstedId;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_static_rating, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);

        new BaalstedRatingsDownload((RatingBar) view.findViewById(R.id.staticRatingBar)).execute(1, baalstedId);
        return view;
    }
}
