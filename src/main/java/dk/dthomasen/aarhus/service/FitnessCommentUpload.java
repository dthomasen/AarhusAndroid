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
import dk.dthomasen.aarhus.models.FitnessRating;

/**
 * Created by Dennis on 10-07-13.
 */
public class FitnessCommentUpload extends AsyncTask<Integer, Void, FitnessRating> {

    protected final String TAG = this.getClass().getName();
    Activity activity;
    FitnessRating message;

    public FitnessCommentUpload(Activity activity, FitnessRating message) {
        this.activity = activity;
        this.message = message;
    }

    @Override
    protected FitnessRating doInBackground(Integer... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String url = "http://dthomasen.dk/aarhus/restful/addFitnessRating";
        Log.i(TAG, message.toString());
        FitnessRating response = restTemplate.postForObject(url, message, FitnessRating.class);
        return response;
    }

    protected void onPostExecute(FitnessRating result) {
        if(result != null){
            Toast.makeText(activity, "Kommentar er indsendt", Toast.LENGTH_LONG).show();
            new FitnessCommentsDownload(((CardUI) activity.findViewById(R.id.commentsCardView)), activity).execute(1, Integer.parseInt(message.getFitness_id()));
        }else{
            Toast.makeText(activity, "Der opstop en fejl i kommentar indsendelsen", Toast.LENGTH_LONG).show();
        }
    }
}
