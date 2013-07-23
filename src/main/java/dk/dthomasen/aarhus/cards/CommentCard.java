package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.ShelterRatings;
import dk.dthomasen.aarhus.service.RatingsDownload;

public class CommentCard extends com.fima.cardsui.objects.Card {
    String commenterName = "";
    String comment = "";
    String rating = "0";
    String dato = "";

    public CommentCard(String commenterName, String rating, String comment, String dato){
        super(commenterName);
        this.commenterName = commenterName;
        this.comment = comment;
        this.rating = rating;
        this.dato = dato;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_card, null);

        ((TextView) view.findViewById(R.id.commenterName)).setText(commenterName);
        ((RatingBar) view.findViewById(R.id.staticRatingBar)).setRating(Float.parseFloat(rating));
        ((TextView) view.findViewById(R.id.CardComment)).setText(comment);
        ((TextView) view.findViewById(R.id.commentDate)).setText(dato);

        return view;
    }
}
