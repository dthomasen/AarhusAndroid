package dk.dthomasen.aarhus.service;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.BaalstedRating;

/**
 * Created by Dennis on 10-07-13.
 */
public class BaalstedCommentUpload extends AsyncTask<Integer, Void, BaalstedRating> {

    protected final String TAG = this.getClass().getName();
    Activity activity;
    BaalstedRating message;

    public BaalstedCommentUpload(Activity activity, BaalstedRating message) {
        this.activity = activity;
        this.message = message;
    }

    @Override
    protected BaalstedRating doInBackground(Integer... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String url = "http://dthomasen.dk/aarhus/restful/addBaalstedRating";
        Log.i(TAG, message.toString());
        BaalstedRating response = restTemplate.postForObject(url, message, BaalstedRating.class);
        return response;
    }

    protected void onPostExecute(BaalstedRating result) {
        if(result != null){
            Toast.makeText(activity, "Kommentar er indsendt", Toast.LENGTH_LONG).show();
            new BaalstedCommentsDownload(((CardUI) activity.findViewById(R.id.commentsCardView)), activity).execute(1, Integer.parseInt(message.getBaalsted_id()));
        }else{
            Toast.makeText(activity, "Der opstop en fejl i kommentar indsendelsen", Toast.LENGTH_LONG).show();
        }
    }
}
