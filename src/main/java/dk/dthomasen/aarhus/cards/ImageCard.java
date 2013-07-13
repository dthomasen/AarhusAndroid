package dk.dthomasen.aarhus.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.cardsui.objects.Card;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.activity.TouchImageViewActivity;
import dk.dthomasen.aarhus.service.DownloadImage;

public class ImageCard extends Card implements View.OnClickListener{

    String imageUrl1;
    String imageUrl2;
    String imageUrl3;
    String imageUrl4;
    Context context;

    public ImageCard(Context context, String title, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4){
        super(title, imageUrl1);
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.context = context;
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_picture, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        if(imageUrl1 != ""){
            new DownloadImage((ImageView)view.findViewById(R.id.cardImage1)).execute(imageUrl1);
        }else{
            ((ImageView)view.findViewById(R.id.cardImage1)).setVisibility(View.GONE);
        }
        if(imageUrl2 != ""){
            new DownloadImage((ImageView)view.findViewById(R.id.cardImage2)).execute(imageUrl2);
        }else{
            ((ImageView)view.findViewById(R.id.cardImage2)).setVisibility(View.GONE);
        }
        if(imageUrl3 != ""){
            new DownloadImage((ImageView)view.findViewById(R.id.cardImage3)).execute(imageUrl3);
        }else{
            ((ImageView)view.findViewById(R.id.cardImage3)).setVisibility(View.GONE);
        }
        if(imageUrl4 != ""){
            new DownloadImage((ImageView)view.findViewById(R.id.cardImage4)).execute(imageUrl4);
        }else{
            ((ImageView)view.findViewById(R.id.cardImage4)).setVisibility(View.GONE);
        }

        ((ImageView)view.findViewById(R.id.cardImage1)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.cardImage2)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.cardImage3)).setOnClickListener(this);
        ((ImageView)view.findViewById(R.id.cardImage4)).setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, TouchImageViewActivity.class);
        if(v.getId() == R.id.cardImage1){
            intent.putExtra("url",imageUrl1);
            context.startActivity(intent);
        }else if(v.getId() == R.id.cardImage2){
            intent.putExtra("url",imageUrl2);
            context.startActivity(intent);
        }else if(v.getId() == R.id.cardImage3){
            intent.putExtra("url",imageUrl3);
            context.startActivity(intent);
        }else if(v.getId() == R.id.cardImage4){
            intent.putExtra("url",imageUrl4);
            context.startActivity(intent);
        }
    }
}