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
public class Skove extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng kammerherrensEge = new LatLng(56.087556, 10.238948);
    LatLng enemaerket = new LatLng(56.08644, 10.243685);
    LatLng observatoriehoej = new LatLng(56.126188, 10.194504);
    LatLng hoerhaven = new LatLng(56.11296, 10.228171);
    LatLng kalvehaven = new LatLng(56.137204, 10.202186);
    LatLng tumlepladsRiisSkov = new LatLng(56.17707, 10.224339);
    LatLng aabyhoejSkov = new LatLng(56.155648, 10.14398);
    LatLng brabrandSkov = new LatLng(56.15179, 10.126288);
    LatLng aarslevSkov = new LatLng(56.152006, 10.07645);
    LatLng gjellerupSkov = new LatLng(56.14912, 10.139411);
    LatLng mollerupSkov = new LatLng(56.203411, 10.20667);
    LatLng lystrupSoenderskov = new LatLng(56.232048, 10.232363);
    LatLng grumstolen = new LatLng(56.113817, 10.214935);
    LatLng svinboSkov = new LatLng(56.243047, 10.324513);
    LatLng vestereng = new LatLng(56.181261, 10.175712);
    LatLng klokkeskoven = new LatLng(56.132855, 10.116376);
    LatLng stautrupSkov = new LatLng(56.142853, 10.115057);
    LatLng tranbjergSkov = new LatLng(56.10035, 10.113475);
    LatLng vilhelmsborgSkov = new LatLng(56.064199, 10.20681);
    LatLng skoedstrupSkov = new LatLng(56.256584, 10.291518);
    LatLng hjortshøjVirupSkov = new LatLng(56.24252, 10.272719);
    LatLng hørretSkov = new LatLng(56.089393, 10.196899);
    LatLng brendstrupSkov = new LatLng(56.178753, 10.141766);
    LatLng vibyHøskov = new LatLng(56.129881, 10.142255);
    LatLng moesgårdHave = new LatLng(56.080596, 10.233734);
    LatLng fløjstrupSkov = new LatLng(56.068674, 10.257254);
    LatLng kirkeskov = new LatLng(56.120076, 10.200292);
    LatLng forstbotaniskHave = new LatLng(56.124651, 10.199898);
    LatLng lisbjergSkovGammel = new LatLng(56.231292, 10.175484);
    LatLng lisbjergSkovNy = new LatLng(56.233877, 10.164975);
    LatLng thorsskov = new LatLng(56.114809, 10.230162);
    LatLng hestehaven = new LatLng(56.124216, 10.217551);
    LatLng skaadeSkov = new LatLng(56.101093, 10.238818);
    LatLng havreballeSkov = new LatLng(56.13768, 10.206054);
    LatLng riisSkov = new LatLng(56.180554, 10.234038);
    LatLng moesgårdSkov = new LatLng(56.09622, 10.244785);
    LatLng hjoerretSkov = new LatLng(56.25798, 10.330645);
    LatLng mariendalSkov = new LatLng(56.049878, 10.257409);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skove);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Skove");
        ab.setSubtitle("Alle skove i Aarhus");

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

        map.addMarker(new MarkerOptions().position(kammerherrensEge)
                .title("Kammerherrens Ege")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(enemaerket)
                .title("Enemærket")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(observatoriehoej)
                .title("Observatoriehøj")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hoerhaven)
                .title("Hørhaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kalvehaven)
                .title("Kalvehaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tumlepladsRiisSkov)
                .title("Tumleplads i Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aabyhoejSkov)
                .title("Åbyhøj Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(brabrandSkov)
                .title("Brabrand Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aarslevSkov)
                .title("Årslev Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(gjellerupSkov)
                .title("Gjellerup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mollerupSkov)
                .title("Mollerup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lystrupSoenderskov)
                .title("Lystrup Sønderskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(grumstolen)
                .title("Grumstolen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(svinboSkov)
                .title("Svinbo Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vestereng)
                .title("Vestereng")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(klokkeskoven)
                .title("Klokkeskoven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(stautrupSkov)
                .title("Stautrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergSkov)
                .title("Tranbjerg Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vilhelmsborgSkov)
                .title("Vilhelmsborg Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skoedstrupSkov)
                .title("Skødstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hjortshøjVirupSkov)
                .title("Hjortshøj - Virup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hørretSkov)
                .title("Hørret Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(brendstrupSkov)
                .title("Brendstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vibyHøskov)
                .title("Viby Høskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moesgårdHave)
                .title("Moesgård Have")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(fløjstrupSkov)
                .title("Fløjstrup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kirkeskov)
                .title("Kirkeskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(forstbotaniskHave)
                .title("Forstbotanisk Have")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lisbjergSkovGammel)
                .title("Lisbjerg Skov, den gamle del")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lisbjergSkovNy)
                .title("Lisbjerg Skov, den nye del")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(thorsskov)
                .title("Thorsskov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hestehaven)
                .title("Hestehaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skaadeSkov)
                .title("Skåde Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(havreballeSkov)
                .title("Havreballe Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(riisSkov)
                .title("Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moesgårdSkov)
                .title("Moesgård Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hjoerretSkov)
                .title("Hjørret Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mariendalSkov)
                .title("Mariendal Skov")
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
                longitude = kammerherrensEge.longitude;
                latitude = kammerherrensEge.latitude;
                break;
            case 1:
                longitude = enemaerket.longitude;
                latitude = enemaerket.latitude;
                break;
            case 2:
                longitude = observatoriehoej.longitude;
                latitude = observatoriehoej.latitude;
                break;
            case 3:
                longitude = hoerhaven.longitude;
                latitude = hoerhaven.latitude;
                break;
            case 4:
                longitude = kalvehaven.longitude;
                latitude = kalvehaven.latitude;
                break;
            case 5:
                longitude = tumlepladsRiisSkov.longitude;
                latitude = tumlepladsRiisSkov.latitude;
                break;
            case 6:
                longitude = aabyhoejSkov.longitude;
                latitude = aabyhoejSkov.latitude;
                break;
            case 7:
                longitude = brabrandSkov.longitude;
                latitude = brabrandSkov.latitude;
                break;
            case 8:
                longitude = aarslevSkov.longitude;
                latitude = aarslevSkov.latitude;
                break;
            case 9:
                longitude = gjellerupSkov.longitude;
                latitude = gjellerupSkov.latitude;
                break;
            case 10:
                longitude = mollerupSkov.longitude;
                latitude = mollerupSkov.latitude;
                break;
            case 11:
                longitude = lystrupSoenderskov.longitude;
                latitude = lystrupSoenderskov.latitude;
                break;
            case 12:
                longitude = grumstolen.longitude;
                latitude = grumstolen.latitude;
                break;
            case 13:
                longitude = svinboSkov.longitude;
                latitude = svinboSkov.latitude;
                break;
            case 14:
                longitude = vestereng.longitude;
                latitude = vestereng.latitude;
                break;
            case 15:
                longitude = klokkeskoven.longitude;
                latitude = klokkeskoven.latitude;
                break;
            case 16:
                longitude = stautrupSkov.longitude;
                latitude = stautrupSkov.latitude;
                break;
            case 17:
                longitude = tranbjergSkov.longitude;
                latitude = tranbjergSkov.latitude;
                break;
            case 18:
                longitude = vilhelmsborgSkov.longitude;
                latitude = vilhelmsborgSkov.latitude;
                break;
            case 19:
                longitude = skoedstrupSkov.longitude;
                latitude = skoedstrupSkov.latitude;
                break;
            case 20:
                longitude = hjortshøjVirupSkov.longitude;
                latitude = hjortshøjVirupSkov.latitude;
                break;
            case 21:
                longitude = hørretSkov.longitude;
                latitude = hørretSkov.latitude;
                break;
            case 22:
                longitude = brendstrupSkov.longitude;
                latitude = brendstrupSkov.latitude;
                break;
            case 23:
                longitude = vibyHøskov.longitude;
                latitude = vibyHøskov.latitude;
                break;
            case 24:
                longitude = moesgårdHave.longitude;
                latitude = moesgårdHave.latitude;
                break;
            case 25:
                longitude = fløjstrupSkov.longitude;
                latitude = fløjstrupSkov.latitude;
                break;
            case 26:
                longitude = kirkeskov.longitude;
                latitude = kirkeskov.latitude;
                break;
            case 27:
                longitude = forstbotaniskHave.longitude;
                latitude = forstbotaniskHave.latitude;
                break;
            case 28:
                longitude = lisbjergSkovGammel.longitude;
                latitude = lisbjergSkovGammel.latitude;
                break;
            case 29:
                longitude = lisbjergSkovNy.longitude;
                latitude = lisbjergSkovNy.latitude;
                break;
            case 30:
                longitude = thorsskov.longitude;
                latitude = thorsskov.latitude;
                break;
            case 31:
                longitude = hestehaven.longitude;
                latitude = hestehaven.latitude;
                break;
            case 32:
                longitude = skaadeSkov.longitude;
                latitude = skaadeSkov.latitude;
                break;
            case 33:
                longitude = havreballeSkov.longitude;
                latitude = havreballeSkov.latitude;
                break;
            case 34:
                longitude = riisSkov.longitude;
                latitude = riisSkov.latitude;
                break;
            case 35:
                longitude = moesgårdSkov.longitude;
                latitude = moesgårdSkov.latitude;
                break;
            case 36:
                longitude = hjoerretSkov.longitude;
                latitude = hjoerretSkov.latitude;
                break;
            case 37:
                longitude = mariendalSkov.longitude;
                latitude = mariendalSkov.latitude;
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
