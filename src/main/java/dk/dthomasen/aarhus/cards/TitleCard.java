package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import dk.dthomasen.aarhus.R;

public class TitleCard extends com.fima.cardsui.objects.Card {

	public TitleCard(String title){
		super(title);
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_title, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		return view;
	}
}
