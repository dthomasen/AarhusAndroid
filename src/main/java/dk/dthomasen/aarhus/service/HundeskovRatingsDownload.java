package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.models.HundeskovRatings;
import dk.dthomasen.aarhus.models.ShelterRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class HundeskovRatingsDownload extends AsyncTask<Integer, Void, HundeskovRatings>{

    protected final String TAG = this.getClass().getName();
    RatingBar ratingbar;

    public HundeskovRatingsDownload(RatingBar ratingBar) {
        this.ratingbar = ratingBar;
    }

    @Override
    protected HundeskovRatings doInBackground(Integer... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String url = "http://dthomasen.dk/aarhus/restful/HundeskoveRatings/"+params[1];
            HundeskovRatings result = restTemplate.getForObject(url, HundeskovRatings.class);
            return result;
        }catch(Throwable e){
            Log.i(TAG, "Throwable message !!!!!! "+e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(HundeskovRatings result) {
        ratingbar.setRating(Service.getInstance().getAverageHundeskovRating(result));
        Service.getInstance().saveHundeskovRatings(result);
    }
}
