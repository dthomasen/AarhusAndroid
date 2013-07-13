package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;

public class DescCard extends com.fima.cardsui.objects.Card {

	public DescCard(String title, String description){
		super(title, description, null);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_desc, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.CardDescription)).setText(desc);
		return view;
	}
}
