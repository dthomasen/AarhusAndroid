package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
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
public class Legepladser extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /**LOCATIONS**/
    LatLng botaniskHave = new LatLng(56.158299, 10.193471);
    LatLng baekvejsparken = new LatLng(56.033114, 10.191484);
    LatLng ekkodalen = new LatLng(56.17265, 10.180933);
    LatLng forteparken = new LatLng(56.195473, 10.229691);
    LatLng frederiksbjerg = new LatLng(56.147318, 10.189365);
    LatLng harlev = new LatLng(56.144787, 9.992165);
    LatLng havrebakken = new LatLng(56.182933, 10.202671);
    LatLng hoejvangsvej = new LatLng(56.129923, 10.121945);
    LatLng kasted = new LatLng(56.20792, 10.129597);
    LatLng kirkedammen = new LatLng(56.140351, 10.17877);
    LatLng klokkerparken = new LatLng(56.161486, 10.157051);
    LatLng kolt = new LatLng(56.110687, 10.074745);
    LatLng kaergårdsparkens = new LatLng(56.038766, 10.089497);
    LatLng bellevue = new LatLng(56.191244, 10.247466);
    LatLng hullet = new LatLng(56.200355, 10.178756);
    LatLng indelukket = new LatLng(56.243779, 10.236919);
    LatLng skovly = new LatLng(56.063835, 10.20143);
    LatLng steenBille = new LatLng(56.173494, 10.214595);
    LatLng tietgensPlads = new LatLng(56.144483, 10.203031);
    LatLng ahWingesVej = new LatLng(56.175466, 10.204201);
    LatLng holmeBypark = new LatLng(56.110161, 10.180304);
    LatLng byvangen = new LatLng(56.130417, 10.16977);
    LatLng baunevej = new LatLng(56.107378, 10.096877);
    LatLng falkevej = new LatLng(56.168533, 10.179699);
    LatLng hunderosevej = new LatLng(56.267975, 10.306178);
    LatLng lillehammervej = new LatLng(56.17789, 10.194065);
    LatLng skaarups = new LatLng(56.187769, 10.114934);
    LatLng tranbjergHovedgade = new LatLng(56.09365, 10.1354);
    LatLng beringsminde = new LatLng(56.160496, 10.121334);
    LatLng vaarkjaerpark = new LatLng(56.125666, 10.147261);
    LatLng mollerupSkov = new LatLng(56.20603, 10.202514);
    LatLng lisbjerg = new LatLng(56.221957, 10.165294);
    LatLng marienlyst = new LatLng(56.18004, 10.160817);
    LatLng mindeparken = new LatLng(56.129701, 10.20393);
    LatLng gellerupSkov = new LatLng(56.153237, 10.143299);
    LatLng praestevangen = new LatLng(56.163991, 10.168206);
    LatLng skjoldhoejkilen = new LatLng(56.170021, 10.125356);
    LatLng skoleparken = new LatLng(56.191834, 10.231047);
    LatLng egaa = new LatLng(56.213607, 10.220287);
    LatLng soeparken = new LatLng(56.151117, 10.106169);
    LatLng tilst = new LatLng(56.190739, 10.099955);
    LatLng vorrevangsparken = new LatLng(56.185695, 10.201189);
    LatLng oelsted = new LatLng(56.233039, 10.136307);
    LatLng oernereden = new LatLng(56.100977, 10.236652);
    LatLng aaby = new LatLng(56.15316, 10.165752);
    LatLng aakrogen = new LatLng(56.203013, 10.2823);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legepladser);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Legepladser");
        ab.setSubtitle("Alle legepladser i Aarhus");

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
        map.addMarker(new MarkerOptions().position(botaniskHave)
                .title("Botanisk Haves legeplads - Udflugtslegepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(baekvejsparken)
                .title("Bækvejsparkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(ekkodalen)
                .title("Ekkodalen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(forteparken)
                .title("Forteparkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(frederiksbjerg)
                .title("Frederiksbjerg Byparks Legeplads - Store legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(harlev)
                .title("Harlev Byparks Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(havrebakken)
                .title("Havrebakkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hoejvangsvej)
                .title("Højvangsvej, Stautrup]")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kasted)
                .title("Kasted Legeplads - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kirkedammen)
                .title("Kirkedammens legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(klokkerparken)
                .title("Klokkerparkens Legeplads - Store legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kolt)
                .title("Kolt Parks Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(kaergårdsparkens)
                .title("Kærgårdsparkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(bellevue)
                .title("Legepladsen \"Bellevue\" - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hullet)
                .title("Legepladsen \"Hullet\" (Skejby) - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(indelukket)
                .title("Legepladsen \"Indelukket\" - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skovly)
                .title("Legepladsen \"Skovly\" (Beder) - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(steenBille)
                .title("Legepladsen \"Steen Bille\" - Store legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tietgensPlads)
                .title("Legepladsen \"Tietgens Plads\" - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(ahWingesVej)
                .title("Legepladsen A.H. Winges Vej")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(holmeBypark)
                .title("Legepladsen Holme Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(byvangen)
                .title("Legepladsen i Byvangen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(baunevej)
                .title("Legepladsen på Baunevej - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(falkevej)
                .title("Legepladsen på Falkevej - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hunderosevej)
                .title("Legepladsen på Hunderosevej - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lillehammervej)
                .title("Legepladsen på Lillehammervej - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skaarups)
                .title("Legepladsen på Skaarups Alle - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergHovedgade)
                .title("Legepladsen på Tranbjerg Hovedgade - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(beringsminde)
                .title("Legepladsen ved Beringsminde - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vaarkjaerpark)
                .title("Legepladsen Vårkjærpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mollerupSkov)
                .title("Legeredskaberne i Mollerup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lisbjerg)
                .title("Lisbjerg Legeplads - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(marienlyst)
                .title("Marienlyst Naturlegeplads - Store legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mindeparken)
                .title("Mindeparkens Legeplads - Udflugtslegepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(gellerupSkov)
                .title("Motionsruten i Gellerup Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(praestevangen)
                .title("Præstevangens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilen)
                .title("Skjoldhøjkilen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skoleparken)
                .title("Skoleparkens Legeplads - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(egaa)
                .title("Stenalderlegeplads - Egå Engsø")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(soeparken)
                .title("Søparkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tilst)
                .title("Tilst Byparks Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vorrevangsparken)
                .title("Vorrevangsparkens Legeplads - Mellemstore legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(oelsted)
                .title("Ølsted Legeplads - Små legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(oernereden)
                .title("Ørneredens legeplads")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aaby)
                .title("Åby Parks Legeplads - Store legepladser")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aakrogen)
                .title("Åkrogens Legeplads - Mellemstore legepladser")
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
                longitude = botaniskHave.longitude;
                latitude = botaniskHave.latitude;
                break;
            case 1:
                longitude = baekvejsparken.longitude;
                latitude = baekvejsparken.latitude;
                break;
            case 2:
                longitude = ekkodalen.longitude;
                latitude = ekkodalen.latitude;
                break;
            case 3:
                longitude = forteparken.longitude;
                latitude = forteparken.latitude;
                break;
            case 4:
                longitude = frederiksbjerg.longitude;
                latitude = frederiksbjerg.latitude;
                break;
            case 5:
                longitude = harlev.longitude;
                latitude = harlev.latitude;
                break;
            case 6:
                longitude = havrebakken.longitude;
                latitude = havrebakken.latitude;
                break;
            case 7:
                longitude = hoejvangsvej.longitude;
                latitude = hoejvangsvej.latitude;
                break;
            case 8:
                longitude = kasted.longitude;
                latitude = kasted.latitude;
                break;
            case 9:
                longitude = kirkedammen.longitude;
                latitude = kirkedammen.latitude;
                break;
            case 10:
                longitude = klokkerparken.longitude;
                latitude = klokkerparken.latitude;
                break;
            case 11:
                longitude = kolt.longitude;
                latitude = kolt.latitude;
                break;
            case 12:
                longitude = kaergårdsparkens.longitude;
                latitude = kaergårdsparkens.latitude;
                break;
            case 13:
                longitude = bellevue.longitude;
                latitude = bellevue.latitude;
                break;
            case 14:
                longitude = hullet.longitude;
                latitude = hullet.latitude;
                break;
            case 15:
                longitude = indelukket.longitude;
                latitude = indelukket.latitude;
                break;
            case 16:
                longitude = skovly.longitude;
                latitude = skovly.latitude;
                break;
            case 17:
                longitude = steenBille.longitude;
                latitude = steenBille.latitude;
                break;
            case 18:
                longitude = tietgensPlads.longitude;
                latitude = tietgensPlads.latitude;
                break;
            case 19:
                longitude = ahWingesVej.longitude;
                latitude = ahWingesVej.latitude;
                break;
            case 20:
                longitude = holmeBypark.longitude;
                latitude = holmeBypark.latitude;
                break;
            case 21:
                longitude = byvangen.longitude;
                latitude = byvangen.latitude;
                break;
            case 22:
                longitude = baunevej.longitude;
                latitude = baunevej.latitude;
                break;
            case 23:
                longitude = falkevej.longitude;
                latitude = falkevej.latitude;
                break;
            case 24:
                longitude = hunderosevej.longitude;
                latitude = hunderosevej.latitude;
                break;
            case 25:
                longitude = lillehammervej.longitude;
                latitude = lillehammervej.latitude;
                break;
            case 26:
                longitude = skaarups.longitude;
                latitude = skaarups.latitude;
                break;
            case 27:
                longitude = tranbjergHovedgade.longitude;
                latitude = tranbjergHovedgade.latitude;
                break;
            case 28:
                longitude = beringsminde.longitude;
                latitude = beringsminde.latitude;
                break;
            case 29:
                longitude = vaarkjaerpark.longitude;
                latitude = vaarkjaerpark.latitude;
                break;
            case 30:
                longitude = mollerupSkov.longitude;
                latitude = mollerupSkov.latitude;
                break;
            case 31:
                longitude = lisbjerg.longitude;
                latitude = lisbjerg.latitude;
                break;
            case 32:
                longitude = marienlyst.longitude;
                latitude = marienlyst.latitude;
                break;
            case 33:
                longitude = mindeparken.longitude;
                latitude = mindeparken.latitude;
                break;
            case 34:
                longitude = gellerupSkov.longitude;
                latitude = gellerupSkov.latitude;
                break;
            case 35:
                longitude = praestevangen.longitude;
                latitude = praestevangen.latitude;
                break;
            case 36:
                longitude = skjoldhoejkilen.longitude;
                latitude = skjoldhoejkilen.latitude;
                break;
            case 37:
                longitude = skoleparken.longitude;
                latitude = skoleparken.latitude;
                break;
            case 38:
                longitude = egaa.longitude;
                latitude = egaa.latitude;
                break;
            case 39:
                longitude = soeparken.longitude;
                latitude = soeparken.latitude;
                break;
            case 40:
                longitude = tilst.longitude;
                latitude = tilst.latitude;
                break;
            case 41:
                longitude = vorrevangsparken.longitude;
                latitude = vorrevangsparken.latitude;
                break;
            case 42:
                longitude = oelsted.longitude;
                latitude = oelsted.latitude;
                break;
            case 43:
                longitude = oernereden.longitude;
                latitude = oernereden.latitude;
                break;
            case 44:
                longitude = aaby.longitude;
                latitude = aaby.latitude;
                break;
            case 45:
                longitude = aakrogen.longitude;
                latitude = aakrogen.latitude;
                break;
        }
        intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
        startActivity(intent);
    }
}
