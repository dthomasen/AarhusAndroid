package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.SimpleDateFormat;
import java.util.Date;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.ShelterRating;
import dk.dthomasen.aarhus.service.CommentUpload;
import dk.dthomasen.aarhus.service.CommentsDownload;
import dk.dthomasen.aarhus.service.Service;

/**
 * Created by Dennis on 04-07-13.
 */
public class SheltersComments extends Activity implements View.OnClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Activity activity = this;
    private int shelterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();
        ab.setTitle("Kommentarer");
        ab.setCustomView(R.layout.commentactionbar);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        shelterId = intent.getExtras().getInt("sheltername");
        findViewById(R.id.ABAddComment).setOnClickListener(this);
        new CommentsDownload(((CardUI) findViewById(R.id.commentsCardView)), this).execute(1, shelterId);

    }

    @Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ABAddComment){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.addcomment);
            dialog.setTitle("Postér kommentar");

            Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel_comment);
            // if button is clicked, close the custom dialog
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button postButton = (Button) dialog.findViewById(R.id.btn_post_comment);

            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText name = (EditText) dialog.findViewById(R.id.Addcommenter_name);
                    EditText message = (EditText) dialog.findViewById(R.id.Addcomment_message);
                    RatingBar rating = (RatingBar) dialog.findViewById(R.id.AddRatingBar);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    ShelterRating comment = new ShelterRating();
                    comment.setShelter_id(Integer.toString(shelterId));
                    comment.setName(name.getText().toString());
                    comment.setComment(message.getText().toString());
                    comment.setRating(Float.toString(rating.getRating()));
                    comment.setDato(dateFormat.format(new Date()));
                    new CommentUpload(activity, comment).execute(1);

                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
