package dk.dthomasen.aarhus.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
public class Hundeskove extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{

    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng mollerupSkov = new LatLng(56.201274, 10.206041);
    LatLng vestereng = new LatLng(56.187644, 10.183239);
    LatLng riisSkov = new LatLng(56.170835, 10.220119);
    LatLng aarslevSkov = new LatLng(56.152006, 10.076365);
    LatLng havreballeSkov = new LatLng(56.134845, 10.190038);
    LatLng moesgaardSkov = new LatLng(56.09363, 10.245453);
    LatLng tranbjergGroenloekkepark = new LatLng(56.09321, 10.118762);
    LatLng skoedstrupSkov = new LatLng(56.259251, 10.289459);
    LatLng lystrupSkov = new LatLng(56.232253, 10.23169);
    LatLng baermoseSkov = new LatLng(56.250421, 10.128719);
    LatLng trueSkovBlankhoej = new LatLng(56.175776, 10.068083);
    LatLng trueSkovSkjoldhoejvej = new LatLng(56.171361, 10.101685);
    LatLng solbjergHundeskov = new LatLng(56.047385, 10.083921);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hundeskove);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via GPS \nVent ventligst...", Toast.LENGTH_LONG).show();
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via netværks udbyder. \nVent ventligst...", Toast.LENGTH_LONG).show();
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
        map.addMarker(new MarkerOptions().position(mollerupSkov)
                .title("Mollerup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vestereng)
                .title("Vestereng")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(riisSkov)
                .title("Riis skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aarslevSkov)
                .title("Årslev Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(havreballeSkov)
                .title("Havreballe Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moesgaardSkov)
                .title("Moesgård Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergGroenloekkepark)
                .title("Tranbjerg Grønløkkepark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skoedstrupSkov)
                .title("Skødstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lystrupSkov)
                .title("Lystrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(baermoseSkov)
                .title("Bærmoseskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(trueSkovBlankhoej)
                .title("True Skov Blankhøj")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(trueSkovSkjoldhoejvej)
                .title("True skov Skjoldhøjvej")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(solbjergHundeskov)
                .title("Solbjerg Hundeskov")
                .snippet("Tryk for rutevejledning"));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i(TAG, "MARKER " + marker.getTitle());
        Location userlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Intent intent = new Intent();
        Double longitude = 0.0;
        Double latitude = 0.0;

        switch (Integer.valueOf(marker.getId().replace("m",""))){
            case 0:
                longitude = mollerupSkov.longitude;
                latitude = mollerupSkov.latitude;
                break;
            case 1:
                longitude = vestereng.longitude;
                latitude = vestereng.latitude;
                break;
            case 2:
                longitude = riisSkov.longitude;
                latitude = riisSkov.latitude;
                break;
            case 3:
                longitude = aarslevSkov.longitude;
                latitude = aarslevSkov.latitude;
                break;
            case 4:
                longitude = havreballeSkov.longitude;
                latitude = havreballeSkov.latitude;
                break;
            case 5:
                longitude = moesgaardSkov.longitude;
                latitude = moesgaardSkov.latitude;
                break;
            case 6:
                longitude = tranbjergGroenloekkepark.longitude;
                latitude = tranbjergGroenloekkepark.latitude;
                break;
            case 7:
                longitude = skoedstrupSkov.longitude;
                latitude = skoedstrupSkov.latitude;
                break;
            case 8:
                longitude = lystrupSkov.longitude;
                latitude = lystrupSkov.latitude;
                break;
            case 9:
                longitude = baermoseSkov.longitude;
                latitude = baermoseSkov.latitude;
                break;
            case 10:
                longitude = trueSkovBlankhoej.longitude;
                latitude = trueSkovBlankhoej.latitude;
                break;
            case 11:
                longitude = trueSkovSkjoldhoejvej.longitude;
                latitude = trueSkovSkjoldhoejvej.latitude;
                break;
            case 12:
                longitude = solbjergHundeskov.longitude;
                latitude = solbjergHundeskov.latitude;
                break;
        }
        intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
        startActivity(intent);
    }
}
