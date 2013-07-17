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
public class Shelters extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng gjellerup = new LatLng(56.150408, 10.142253);
    LatLng hoerhavenSkoven = new LatLng(56.11467, 10.22718);
    LatLng lisbjerg = new LatLng(56.235431, 10.16946);
    LatLng moesgaardStrand = new LatLng(56.088373, 10.244415);
    LatLng mollerup = new LatLng(56.206293, 10.202737);
    LatLng hoerhavenBakken = new LatLng(56.11288, 10.229495);
    LatLng vestereng = new LatLng(56.186809, 10.183249);
    LatLng moesgaardSkov = new LatLng(56.097863, 10.232839);
    LatLng hoerhaven = new LatLng(56.113568, 10.230217);
    LatLng vilhelmsborg = new LatLng(56.066575, 10.197506);
    LatLng brendstrup = new LatLng(56.184156, 10.161816);
    LatLng skjoldhoejkilenAlfa = new LatLng(56.169204, 10.133973);
    LatLng skjoldhoejkilenGamma = new LatLng(56.169994, 10.124992);
    LatLng skjoldhoejkilenDelta = new LatLng(56.171274, 10.111871);
    LatLng skjoldhoejkilenEpsilon = new LatLng(56.171661, 10.104946);
    LatLng egaa = new LatLng(56.213604, 10.220277);
    LatLng lisbjergNySkov = new LatLng(56.226818, 10.171774);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelters);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Shelters/Madpakkehuse");
        ab.setSubtitle("Alle shelters/madpakkehuse i Aarhus");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via GPS", Toast.LENGTH_LONG).show();
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via netværks udbyder", Toast.LENGTH_LONG).show();
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
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
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
        map.addMarker(new MarkerOptions().position(gjellerup)
                .title("Shelter i Gjellerup Skov")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(hoerhavenSkoven)
                .title("Shelter i Hørhaven i skoven")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(lisbjerg)
                .title("Shelter i Lisbjerg gammel skov")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(moesgaardStrand)
                .title("Shelter ved Moesgård Strand")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(mollerup)
                .title("Shelter i Mollerup Skov")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(hoerhavenBakken)
                .title("Shelter i Hørhaven på bakken")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(vestereng)
                .title("Shelter på Vestereng")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(moesgaardSkov)
                .title("Shelter i Moesgård Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hoerhaven)
                .title("Dagshelter i Hørhaven")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(vilhelmsborg)
                .title("Shelter i Vilhelmsborg Skov")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(brendstrup)
                .title("Shelter i Brendstrup Skov")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilenAlfa)
                .title("Madpakkehus Alfa i Skjoldhøjkilen")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilenGamma)
                .title("Madpakkehus Gamma i Skjoldhøjkilen")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilenDelta)
                .title("Madpakkehus Delta i Skjoldhøjkilen")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilenEpsilon)
                .title("Madpakkehus Epsilon i Skjoldhøjkilen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(egaa)
                .title("Shelterplads ved Egå Engsø - fem stk.")
                .snippet("Tryk for yderligere info"));
        map.addMarker(new MarkerOptions().position(lisbjergNySkov)
                .title("Shelterplads i Lisbjerg ny skov - fem stk.")
                .snippet("Tryk for yderligere info"));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Location userlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Intent intent = new Intent();
        Double longitude = 0.0;
        Double latitude = 0.0;
        try{
        switch (Integer.valueOf(marker.getId().replace("m",""))){
            case 0:
                longitude = gjellerup.longitude;
                latitude = gjellerup.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 1:
                longitude = hoerhavenSkoven.longitude;
                latitude = hoerhavenSkoven.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 2:
                longitude = lisbjerg.longitude;
                latitude = lisbjerg.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 3:
                longitude = moesgaardStrand.longitude;
                latitude = moesgaardStrand.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 4:
                longitude = mollerup.longitude;
                latitude = mollerup.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 5:
                longitude = hoerhavenBakken.longitude;
                latitude = hoerhavenBakken.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 6:
                longitude = vestereng.longitude;
                latitude = vestereng.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 7:
                longitude = moesgaardSkov.longitude;
                latitude = moesgaardSkov.latitude;
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
                startActivity(intent);
                break;
            case 8:
                longitude = hoerhaven.longitude;
                latitude = hoerhaven.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 9:
                longitude = vilhelmsborg.longitude;
                latitude = vilhelmsborg.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 10:
                longitude = brendstrup.longitude;
                latitude = brendstrup.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 11:
                longitude = skjoldhoejkilenAlfa.longitude;
                latitude = skjoldhoejkilenAlfa.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 12:
                longitude = skjoldhoejkilenGamma.longitude;
                latitude = skjoldhoejkilenGamma.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 13:
                longitude = skjoldhoejkilenDelta.longitude;
                latitude = skjoldhoejkilenDelta.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 14:
                longitude = skjoldhoejkilenEpsilon.longitude;
                latitude = skjoldhoejkilenEpsilon.latitude;
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
                startActivity(intent);
                break;
            case 15:
                longitude = egaa.longitude;
                latitude = egaa.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
            case 16:
                longitude = lisbjergNySkov.longitude;
                latitude = lisbjergNySkov.latitude;
                intent = new Intent(this, SheltersInfo.class);
                intent.putExtra("longitude", Double.toString(longitude));
                intent.putExtra("latitude",Double.toString(latitude));
                startActivity(intent);
                break;
        }
        }catch (NullPointerException e){
            Toast.makeText(this, "Tænd for GPS for at benytte rutevejledning", Toast.LENGTH_LONG).show();
        }
    }
}
