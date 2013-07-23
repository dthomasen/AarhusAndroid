package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.RatingBar;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dk.dthomasen.aarhus.models.ShelterRatings;

/**
 * Created by Dennis on 10-07-13.
 */
public class ShelterRatingsDownload extends AsyncTask<Integer, Void, ShelterRatings>{

    /**
     * ID definitions:
     * 1 - ShelterRatings
     */
    protected final String TAG = this.getClass().getName();
    RatingBar ratingbar;

    public ShelterRatingsDownload(RatingBar ratingBar) {
        this.ratingbar = ratingBar;
    }

    @Override
    protected ShelterRatings doInBackground(Integer... params) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String url = "http://dthomasen.dk/aarhus/restful/shelterRatings/"+params[1];
            ShelterRatings result = restTemplate.getForObject(url, ShelterRatings.class);
            return result;
        }catch(Throwable e){
            Log.i(TAG, "Throwable message !!!!!! "+e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(ShelterRatings result) {
        ratingbar.setRating(Service.getInstance().getAverageShelterRating(result));
        Service.getInstance().saveShelterRatings(result);
    }
}
