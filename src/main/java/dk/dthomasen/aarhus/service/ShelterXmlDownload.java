package dk.dthomasen.aarhus.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created by Dennis on 10-07-13.
 */
public class ShelterXmlDownload extends AsyncTask<Context, Void, Boolean>{

    private Context mainActivity;
    protected final String TAG = this.getClass().getName();
    @Override
    protected Boolean doInBackground(Context... params) {
        try {
            mainActivity = params[0];
            String xml = getXML("http://dthomasen.dk/aarhus/shelters/shelters.xml");
            writeToFile("shelters.xml",xml);
            return true;
        } catch (Throwable t) {
            Log.e("AsyncTask", "OMGCrash", t);
            // maybe throw it again
            throw new RuntimeException(t);
        }
    }

    private String getXML(String Link){
        String line = null;
        Log.d("getxml","get");
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Link);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            line = EntityUtils.toString(httpEntity, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
        } catch (MalformedURLException e) {
            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
        } catch (IOException e) {
            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
        }
        Log.d(TAG, line);
        return line;
    }

    private void writeToFile(String filename, String string){
        FileOutputStream outputStream;
            try {
            outputStream = mainActivity.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
