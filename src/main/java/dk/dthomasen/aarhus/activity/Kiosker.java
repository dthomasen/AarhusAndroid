package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dk.dthomasen.aarhus.R;

/**
 * Created by Dennis on 04-07-13.
 */
public class Kiosker extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{

    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng havreballeSkovbrynet = new LatLng(56.13872, 10.205476);
    LatLng tangkrogen = new LatLng(56.139317, 10.207303);
    LatLng permanente = new LatLng(56.176728, 10.230386);
    LatLng hestehaven = new LatLng(56.126349, 10.213655);
    LatLng riisskov = new LatLng(56.177535, 10.219719);
    LatLng havreballeStadion = new LatLng(56.137018, 10.195244);
    LatLng mindeparken = new LatLng(56.130207, 10.20298);
    LatLng bellevue = new LatLng(56.19051, 10.247237);
    LatLng aakrog = new LatLng(56.202745, 10.282155);
    LatLng skaade = new LatLng(56.101315, 10.237039);
    LatLng moesgaardStrand = new LatLng(56.08897, 10.247685);
    LatLng moesgaardMuseum = new LatLng(56.087183, 10.225767);
    LatLng blommehaven = new LatLng(56.110284, 10.232246);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kiosker);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Kiosker");
        ab.setSubtitle("Kiosker nær skove/parker i Aarhus");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via GPS \nVent venligst...", Toast.LENGTH_LONG).show();
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via netværks udbyder. \nVent venligst...", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Aktiver din GPS eller netværks placering", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            //Show some generic error dialog because something must have gone wrong with location manager.
        }
        setUpMapIfNeeded();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.slidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
        overridePendingTransition(0, 0);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        setUpMapIfNeeded();

        if(locationManager != null)
        {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if( mListener != null )
        {
            mListener.onLocationChanged( location );

            LatLngBounds bounds = this.map.getProjection().getVisibleRegion().latLngBounds;

            if(!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude())))
            {
                //Move the camera to the user's location once it's available!
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.

            if (map != null)
            {
                setUpMap();
            }

            //This is how you register the LocationSource
            map.setLocationSource(this);
        }
    }

    private void setUpMap()
    {
        addTrainingMarkers();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.170016, 10.201158), 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        map.setOnInfoWindowClickListener(this);
    }

    private void addTrainingMarkers(){
        map.addMarker(new MarkerOptions().position(havreballeSkovbrynet)
                .title("Kiosk ved Havreballe Skov; Skovbrynet")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tangkrogen)
                .title("Kiosk ved Tangkrogen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(permanente)
                .title("Kiosk ved Den Permanente")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hestehaven)
                .title("Kiosk ved Hestehaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(riisskov)
                .title("Kiosk ved Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(havreballeStadion)
                .title("Kiosk ved Havreballe Skov, Stadion Allé")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mindeparken)
                .title("Kiosk ved Mindeparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(bellevue)
                .title("Kiosk ved Bellevue Strandpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aakrog)
                .title("Kiosk ved Åkrog Strandpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skaade)
                .title("Kiosk ved Skåde Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moesgaardStrand)
                .title("Kiosk ved Moesgård Strand")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moesgaardMuseum)
                .title("Kiosk ved Moesgård Museum")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(blommehaven)
                .title("Kiosk ved Blommehaven")
                .snippet("Tryk for rutevejledning"));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Location userlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Intent intent = new Intent();
        Double longitude = 0.0;
        Double latitude = 0.0;

        switch (Integer.valueOf(marker.getId().replace("m",""))){
            case 0:
                longitude = havreballeSkovbrynet.longitude;
                latitude = havreballeSkovbrynet.latitude;
                break;
            case 1:
                longitude = tangkrogen.longitude;
                latitude = tangkrogen.latitude;
                break;
            case 2:
                longitude = permanente.longitude;
                latitude = permanente.latitude;
                break;
            case 3:
                longitude = hestehaven.longitude;
                latitude = hestehaven.latitude;
                break;
            case 4:
                longitude = riisskov.longitude;
                latitude = riisskov.latitude;
                break;
            case 5:
                longitude = havreballeStadion.longitude;
                latitude = havreballeStadion.latitude;
                break;
            case 6:
                longitude = mindeparken.longitude;
                latitude = mindeparken.latitude;
                break;
            case 7:
                longitude = bellevue.longitude;
                latitude = bellevue.latitude;
                break;
            case 8:
                longitude = aakrog.longitude;
                latitude = aakrog.latitude;
                break;
            case 9:
                longitude = skaade.longitude;
                latitude = skaade.latitude;
                break;
            case 10:
                longitude = moesgaardStrand.longitude;
                latitude = moesgaardStrand.latitude;
                break;
            case 11:
                longitude = moesgaardMuseum.longitude;
                latitude = moesgaardMuseum.latitude;
                break;
            case 12:
                longitude = blommehaven.longitude;
                latitude = blommehaven.latitude;
                break;
        }
        try{
        intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
        startActivity(intent);
        }catch(NullPointerException e){
            Toast.makeText(this, "Tænd for GPS eller Placeringsdeling for at benytte rutevejledning", Toast.LENGTH_LONG).show();
        }
    }
}
