package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import com.fima.cardsui.views.CardUI;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.cards.CommentCard;
import dk.dthomasen.aarhus.cards.TitleCard;
import dk.dthomasen.aarhus.models.ShelterRating;
import dk.dthomasen.aarhus.models.ShelterRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class CommentsDownload extends AsyncTask<Integer, Void, ShelterRatings>{

    /**
     * ID definitions:
     * 1 - ShelterRatings
     */
    protected final String TAG = this.getClass().getName();
    CardUI cardUI;

    public CommentsDownload(CardUI cardUI) {
        this.cardUI = cardUI;
    }

    @Override
    protected ShelterRatings doInBackground(Integer... params) {
        try{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String url = "";
        if(params[0] == 1){
            Log.i(TAG, "checked url");
            url = "http://dthomasen.dk/aarhus/restful/shelterRatings/"+params[1];
        }
        Log.i(TAG, "URL IS!!!!!!: "+url);
        ShelterRatings result = restTemplate.getForObject(url, ShelterRatings.class);
        return result;
        }catch(Throwable e){
            Log.i(TAG, "Throwable message !!!!!! "+e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(ShelterRatings result) {
        for(ShelterRating rating : result.getShelterRatings()){
            cardUI.addCard(new CommentCard(rating.getName(), rating.getRating(), rating.getComment()));
            cardUI.refresh();
        }
        //cardUI.refresh();
    }
}
