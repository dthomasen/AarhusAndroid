package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.models.FitnessRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class FitnessRatingsDownload extends AsyncTask<Integer, Void, FitnessRatings>{

    protected final String TAG = this.getClass().getName();
    RatingBar ratingbar;

    public FitnessRatingsDownload(RatingBar ratingBar) {
        this.ratingbar = ratingBar;
    }

    @Override
    protected FitnessRatings doInBackground(Integer... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String url = "http://dthomasen.dk/aarhus/restful/FitnessRatings/"+params[1];
            FitnessRatings result = restTemplate.getForObject(url, FitnessRatings.class);
            return result;
        }catch(Throwable e){
            Log.i(TAG, "Throwable message !!!!!! "+e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(FitnessRatings result) {
        ratingbar.setRating(Service.getInstance().getAverageFitnessRating(result));
        Service.getInstance().saveFitnessRatings(result);
    }
}
