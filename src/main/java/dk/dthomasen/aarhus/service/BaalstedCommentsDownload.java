package dk.dthomasen.aarhus.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.fima.cardsui.views.CardUI;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.cards.CommentCard;
import dk.dthomasen.aarhus.models.BaalstedRating;
import dk.dthomasen.aarhus.models.BaalstedRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class BaalstedCommentsDownload extends AsyncTask<Integer, Void, BaalstedRatings>{

    protected final String TAG = this.getClass().getName();
    CardUI cardUI;
    Activity activity;

    public BaalstedCommentsDownload(CardUI cardUI, Activity activity) {
        this.cardUI = cardUI;
        this.activity = activity;
    }

    @Override
    protected BaalstedRatings doInBackground(Integer... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String url = "http://dthomasen.dk/aarhus/restful/BaalstedRatings/"+params[1];
            BaalstedRatings result = restTemplate.getForObject(url, BaalstedRatings.class);
            return result;

        }catch(Throwable e){
            Log.i(TAG, "Throwable message !!!!!! "+e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(BaalstedRatings result) {
        if(result.getBaalstedRatings().length == 0){
            AlertDialog.Builder noCommentsPopup = new AlertDialog.Builder(activity);

            noCommentsPopup.setMessage("Der er endnu ingen kommentarer til dette bålsted - Du kan blive den første!");
            noCommentsPopup.setTitle("Ingen kommentarer");
            noCommentsPopup.setPositiveButton("OK", null);
            noCommentsPopup.setCancelable(true);
            noCommentsPopup.create().show();
        }else{
            cardUI.clearCards();
            for(BaalstedRating rating : result.getBaalstedRatings()){
                cardUI.addCard(new CommentCard(rating.getName(), rating.getRating(), rating.getComment(), rating.getDato()));
                cardUI.refresh();
            }
        }
    }
}
