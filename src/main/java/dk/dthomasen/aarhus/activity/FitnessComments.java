package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.fima.cardsui.views.CardUI;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.SimpleDateFormat;
import java.util.Date;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.cards.CommentCard;
import dk.dthomasen.aarhus.models.FitnessRating;
import dk.dthomasen.aarhus.models.FitnessRatings;
import dk.dthomasen.aarhus.service.FitnessCommentUpload;
import dk.dthomasen.aarhus.service.Service;

/**
 * Created by Dennis on 04-07-13.
 */
public class FitnessComments extends Activity implements View.OnClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Activity activity = this;
    private int fitnessId;

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
        fitnessId = intent.getExtras().getInt("fitnessid");
        findViewById(R.id.ABAddComment).setOnClickListener(this);

        populateCards();
    }

    public void populateCards(){

        FitnessRatings result = Service.getInstance().getSavedFitnessRatings();

        CardUI cardUI = (CardUI) findViewById(R.id.commentsCardView);

        if(result.getFitnessRatings().length == 0){
            AlertDialog.Builder noCommentsPopup = new AlertDialog.Builder(activity);

            noCommentsPopup.setMessage("Der er endnu ingen kommentarer til denne fitness plads - Du kan blive den første!");
            noCommentsPopup.setTitle("Ingen kommentarer");
            noCommentsPopup.setPositiveButton("OK", null);
            noCommentsPopup.setCancelable(true);
            noCommentsPopup.create().show();
        }else{
            cardUI.clearCards();
            for(FitnessRating rating : result.getFitnessRatings()){
                cardUI.addCard(new CommentCard(rating.getName(), rating.getRating(), rating.getComment(), rating.getDato()));
                cardUI.refresh();
            }
        }
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

                    FitnessRating comment = new FitnessRating();
                    comment.setFitness_id(Integer.toString(fitnessId));
                    comment.setName(name.getText().toString());
                    comment.setComment(message.getText().toString());
                    comment.setRating(Float.toString(rating.getRating()));
                    comment.setDato(dateFormat.format(new Date()));
                    new FitnessCommentUpload(activity, comment).execute(1);

                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
