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
public class Baalpladser extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng havreballe = new LatLng(56.137314, 10.203216);
    LatLng lystrupSoenderskov = new LatLng(56.233364, 10.233634);
    LatLng tranbjergSkov = new LatLng(56.100032, 10.115652);
    LatLng ndrStrandmark = new LatLng(56.093136, 10.247694);
    LatLng sdrStrandmark = new LatLng(56.08622, 10.248334);
    LatLng ballehage = new LatLng(56.121338, 10.224553);
    LatLng floejstrupSyd = new LatLng(56.059114, 10.259632);
    LatLng mariendalStrand = new LatLng(56.05325, 10.266391);
    LatLng warming = new LatLng(56.176844, 10.223534);
    LatLng blommehaven = new LatLng(56.109069, 10.236004);
    LatLng floejstrupNord = new LatLng(56.080965, 10.252906);
    LatLng riisSkov = new LatLng(56.176958, 10.224011);
    LatLng dausRiisSkov = new LatLng(56.17658, 10.223089);
    LatLng digevejen = new LatLng(56.060971, 10.259325);
    LatLng skoedstrup = new LatLng(56.258494, 10.292587);
    LatLng skjoeldhoejkilen = new LatLng(56.16818, 10.130951);
    LatLng praestevangen = new LatLng(56.164225, 10.168077);
    LatLng oernereden = new LatLng(56.101081, 10.236555);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baalpladser);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Bålpladser");
        ab.setSubtitle("Alle bålpladser i Aarhus");

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
        map.addMarker(new MarkerOptions().position(havreballe)
                .title("Bålplads i Havreballe Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lystrupSoenderskov)
                .title("Bålhus i Lystrup Sønderskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergSkov)
                .title("Bålplads i Tranbjerg Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(ndrStrandmark)
                .title("Bålplads ved Moesgård Strand - Ndr. Strandmark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(sdrStrandmark)
                .title("Bålplads ved Moesgård Strand - Sdr. Strandmark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(ballehage)
                .title("Bålplads ved Ballehage")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(floejstrupSyd)
                .title("Bålplads i Fløjstrup Skov - Syd")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mariendalStrand)
                .title("Bålplads på Mariendal Strand")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(warming)
                .title("Bålplads Warming på Tumlepladsen i Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(blommehaven)
                .title("Bålplads på stranden ved Blommehaven Camping")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(floejstrupNord)
                .title("Bålplads i Fløjstrup Skov - Nord")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(riisSkov)
                .title("Bålhus på Tumlepladsen i Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(dausRiisSkov)
                .title("Bålplads Daus på Tumlepladsen i Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(digevejen)
                .title("Bålplads ved Digevejen i Fløjstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skoedstrup)
                .title("Bålplads i Skødstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoeldhoejkilen)
                .title("Bålplads i Skjoldhøjkilen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(praestevangen)
                .title("Bålplads i Præstevangen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(oernereden)
                .title("Bålplads ved Ørnereden")
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
                longitude = havreballe.longitude;
                latitude = havreballe.latitude;
                break;
            case 1:
                longitude = lystrupSoenderskov.longitude;
                latitude = lystrupSoenderskov.latitude;
                break;
            case 2:
                longitude = tranbjergSkov.longitude;
                latitude = tranbjergSkov.latitude;
                break;
            case 3:
                longitude = ndrStrandmark.longitude;
                latitude = ndrStrandmark.latitude;
                break;
            case 4:
                longitude = sdrStrandmark.longitude;
                latitude = sdrStrandmark.latitude;
                break;
            case 5:
                longitude = ballehage.longitude;
                latitude = ballehage.latitude;
                break;
            case 6:
                longitude = floejstrupSyd.longitude;
                latitude = floejstrupSyd.latitude;
                break;
            case 7:
                longitude = mariendalStrand.longitude;
                latitude = mariendalStrand.latitude;
                break;
            case 8:
                longitude = warming.longitude;
                latitude = warming.latitude;
                break;
            case 9:
                longitude = blommehaven.longitude;
                latitude = blommehaven.latitude;
                break;
            case 10:
                longitude = floejstrupNord.longitude;
                latitude = floejstrupNord.latitude;
                break;
            case 11:
                longitude = riisSkov.longitude;
                latitude = riisSkov.latitude;
                break;
            case 12:
                longitude = dausRiisSkov.longitude;
                latitude = dausRiisSkov.latitude;
                break;
            case 13:
                longitude = digevejen.longitude;
                latitude = digevejen.latitude;
                break;
            case 14:
                longitude = skoedstrup.longitude;
                latitude = skoedstrup.latitude;
                break;
            case 15:
                longitude = skjoeldhoejkilen.longitude;
                latitude = skjoeldhoejkilen.latitude;
                break;
            case 16:
                longitude = praestevangen.longitude;
                latitude = praestevangen.latitude;
                break;
            case 17:
                longitude = oernereden.longitude;
                latitude = oernereden.latitude;
                break;
        }
        intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
        startActivity(intent);
    }
}
