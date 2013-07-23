package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.models.BaalstedRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class BaalstedRatingsDownload extends AsyncTask<Integer, Void, BaalstedRatings>{

    protected final String TAG = this.getClass().getName();
    RatingBar ratingbar;

    public BaalstedRatingsDownload(RatingBar ratingBar) {
        this.ratingbar = ratingBar;
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
        ratingbar.setRating(Service.getInstance().getAverageBaalstedRating(result));
        Service.getInstance().saveBaalstedRatings(result);
    }
}
