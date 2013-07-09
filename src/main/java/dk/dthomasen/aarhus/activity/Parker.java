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
public class Parker extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /*** Locations ***/
    LatLng aarslev = new LatLng(56.157513, 10.08495);
    LatLng bananen = new LatLng(56.274149, 10.307294);
    LatLng bavneparken = new LatLng(56.107821, 10.090546);
    LatLng baekvejsparken = new LatLng(56.033471, 10.191357);
    LatLng engdalgaardsparken = new LatLng(56.066685, 10.20794);
    LatLng forteparken = new LatLng(56.194384, 10.23349);
    LatLng harlevBypark = new LatLng(56.144527, 9.989923);
    LatLng hasselagerparken = new LatLng(56.106813, 10.101226);
    LatLng hjulbjergvejs = new LatLng(56.115544, 10.17472);
    LatLng holmeBypark = new LatLng(56.109395, 10.17872);
    LatLng hoergaardsparken = new LatLng(56.198478, 10.235118);
    LatLng koltPark = new LatLng(56.109815, 10.072917);
    LatLng krohaven = new LatLng(56.056502, 10.20176);
    LatLng langenæsparken = new LatLng(56.142681, 10.177377);
    LatLng lokalparkenElmely = new LatLng(56.265672, 10.307259);
    LatLng maarsletPark = new LatLng(56.063826, 10.16437);
    LatLng ryparken = new LatLng(56.167428, 10.153197);
    LatLng skjoldhoejkilen = new LatLng(56.169621, 10.138341);
    LatLng skjoldhøjparken = new LatLng(56.171111, 10.09827);
    LatLng skjoldhøjskoven = new LatLng(56.171059, 10.11574);
    LatLng stavtrupSkovplantning = new LatLng(56.138587, 10.121558);
    LatLng stenhoejgaardsparken = new LatLng(56.036723, 10.200915);
    LatLng soeparken = new LatLng(56.150511, 10.108603);
    LatLng tilstBypark = new LatLng(56.188778, 10.09915);
    LatLng toftegaardsparken = new LatLng(56.223208, 10.29768);
    LatLng vibyPark = new LatLng(56.127123, 10.165041);
    LatLng aeblehaven = new LatLng(56.239345, 10.229519);
    LatLng oelstedGroenning = new LatLng(56.233098, 10.13646);
    LatLng oesterbyMose = new LatLng(56.08241, 10.128229);
    LatLng aabakken = new LatLng(56.067462, 10.159425);
    LatLng aabyPark = new LatLng(56.152345, 10.163998);
    LatLng aaparken = new LatLng(56.155743, 10.194859);
    LatLng vorrevangsparken = new LatLng(56.186558, 10.205578);
    LatLng indelukket = new LatLng(56.243416, 10.238611);
    LatLng hovmarken = new LatLng(56.237317, 10.239212);
    LatLng hansBrogesBakker = new LatLng(56.15646, 10.114162);
    LatLng klokkerparken = new LatLng(56.164594, 10.156517);
    LatLng hjulbjergparken = new LatLng(56.10999, 10.188603);
    LatLng katterhoej = new LatLng(56.10544, 10.215597);
    LatLng vestervangsparken = new LatLng(56.041002, 10.071304);
    LatLng tranbjergLaurbaerpark = new LatLng(56.087893, 10.149921);
    LatLng TranbjergGroennloekkepark = new LatLng(56.093045, 10.12798);
    LatLng vibyEnge = new LatLng(56.144233, 10.17372);
    LatLng moelleparken = new LatLng(56.156424, 10.200438);
    LatLng marienlystparken = new LatLng(56.178232, 10.16059);
    LatLng hasleBakker = new LatLng(56.166377, 10.140782);
    LatLng botaniskHave = new LatLng(56.160668, 10.192212);
    LatLng raadhusparken = new LatLng(56.152054, 10.20142);
    LatLng praestevangen = new LatLng(56.163496, 10.167834);
    LatLng mindeparken = new LatLng(56.129958, 10.202859);
    LatLng hedeskovparken = new LatLng(56.234888, 10.226411);
    LatLng skanseparken = new LatLng(56.145406, 10.205734);
    LatLng sktOlofKirkegaard = new LatLng(56.158315, 10.212624);
    LatLng baadepladsStadevej = new LatLng(56.149729, 10.099068);
    LatLng tranbjergCenterpark = new LatLng(56.085833, 10.125139);
    LatLng vennelystparken = new LatLng(56.164447, 10.207092);
    LatLng tangkrogen = new LatLng(56.138663, 10.212432);

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

        ab.setTitle("Parker");
        ab.setSubtitle("Alle parker i Aarhus");

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
        map.addMarker(new MarkerOptions().position(aarslev)
                .title("Aarslev Møllepark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(bananen)
                .title("Bananen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(bavneparken)
                .title("Bavneparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(baekvejsparken)
                .title("Bækvejsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(engdalgaardsparken)
                .title("Engdalgårdsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(forteparken)
                .title("Forteparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(harlevBypark)
                .title("Harlev Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hasselagerparken)
                .title("Hasselagerparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hjulbjergvejs)
                .title("Hjulbjergvejs Lokalpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(holmeBypark)
                .title("Holme Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hoergaardsparken)
                .title("Hørgårdsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(koltPark)
                .title("Kolt Park")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(krohaven)
                .title("Krohaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(langenæsparken)
                .title("Langenæsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lokalparkenElmely)
                .title("Lokalparken v. Elmely")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(maarsletPark)
                .title("Mårslet Park")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(ryparken)
                .title("Ryparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilen)
                .title("Skjoldhøjkilen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoldhøjparken)
                .title("Skjoldhøjparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoldhøjskoven)
                .title("Skjoldhøjskoven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(stavtrupSkovplantning)
                .title("Stavtrup Skovplantning")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(stenhoejgaardsparken)
                .title("Stenhøjgårdsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(soeparken)
                .title("Søparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tilstBypark)
                .title("Tilst Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(toftegaardsparken)
                .title("Toftegårdsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vibyPark)
                .title("Viby Park")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aeblehaven)
                .title("Æblehaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(oelstedGroenning)
                .title("Ølsted Grønning")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(oesterbyMose)
                .title("Østerby Mose")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aabakken)
                .title("Åbakken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aabyPark)
                .title("Åby Park")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(aaparken)
                .title("Åparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vorrevangsparken)
                .title("Vorrevangsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(indelukket)
                .title("Indelukket")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hovmarken)
                .title("Hovmarken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hansBrogesBakker)
                .title("Hans Broges Bakker")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(klokkerparken)
                .title("Klokkerparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hjulbjergparken)
                .title("Hjulbjergparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(katterhoej)
                .title("Katterhøj")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vestervangsparken)
                .title("Vestervangsparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergLaurbaerpark)
                .title("Tranbjerg Laurbærpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(TranbjergGroennloekkepark)
                .title("Tranbjerg Grønløkkepark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vibyEnge)
                .title("Viby Enge")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(moelleparken)
                .title("Mølleparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(marienlystparken)
                .title("Marienlystparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hasleBakker)
                .title("Hasle Bakker")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(botaniskHave)
                .title("Botanisk Have")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(raadhusparken)
                .title("Rådhusparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(praestevangen)
                .title("Præstevangen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mindeparken)
                .title("Mindeparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(hedeskovparken)
                .title("Hedeskovparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skanseparken)
                .title("Skanseparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(sktOlofKirkegaard)
                .title("Skt. Olof Kirkegård")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(baadepladsStadevej)
                .title("Bådeplads - Stadevej")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tranbjergCenterpark)
                .title("Tranbjerg Centerpark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(vennelystparken)
                .title("Vennelystparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tangkrogen)
                .title("Tangkrogen")
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
                longitude = aarslev.longitude;
                latitude = aarslev.latitude;
                break;
            case 1:
                longitude = bananen.longitude;
                latitude = bananen.latitude;
                break;
            case 2:
                longitude = bavneparken.longitude;
                latitude = bavneparken.latitude;
                break;
            case 3:
                longitude = baekvejsparken.longitude;
                latitude = baekvejsparken.latitude;
                break;
            case 4:
                longitude = engdalgaardsparken.longitude;
                latitude = engdalgaardsparken.latitude;
                break;
            case 5:
                longitude = forteparken.longitude;
                latitude = forteparken.latitude;
                break;
            case 6:
                longitude = harlevBypark.longitude;
                latitude = harlevBypark.latitude;
                break;
            case 7:
                longitude = hasselagerparken.longitude;
                latitude = hasselagerparken.latitude;
                break;
            case 8:
                longitude = hjulbjergvejs.longitude;
                latitude = hjulbjergvejs.latitude;
                break;
            case 9:
                longitude = holmeBypark.longitude;
                latitude = holmeBypark.latitude;
                break;
            case 10:
                longitude = hoergaardsparken.longitude;
                latitude = hoergaardsparken.latitude;
                break;
            case 11:
                longitude = koltPark.longitude;
                latitude = koltPark.latitude;
                break;
            case 12:
                longitude = krohaven.longitude;
                latitude = krohaven.latitude;
                break;
            case 13:
                longitude = langenæsparken.longitude;
                latitude = langenæsparken.latitude;
                break;
            case 14:
                longitude = lokalparkenElmely.longitude;
                latitude = lokalparkenElmely.latitude;
                break;
            case 15:
                longitude = maarsletPark.longitude;
                latitude = maarsletPark.latitude;
                break;
            case 16:
                longitude = ryparken.longitude;
                latitude = ryparken.latitude;
                break;
            case 17:
                longitude = skjoldhoejkilen.longitude;
                latitude = skjoldhoejkilen.latitude;
                break;
            case 18:
                longitude = skjoldhøjparken.longitude;
                latitude = skjoldhøjparken.latitude;
                break;
            case 19:
                longitude = skjoldhøjskoven.longitude;
                latitude = skjoldhøjskoven.latitude;
                break;
            case 20:
                longitude = stavtrupSkovplantning.longitude;
                latitude = stavtrupSkovplantning.latitude;
                break;
            case 21:
                longitude = stenhoejgaardsparken.longitude;
                latitude = stenhoejgaardsparken.latitude;
                break;
            case 22:
                longitude = soeparken.longitude;
                latitude = soeparken.latitude;
                break;
            case 23:
                longitude = tilstBypark.longitude;
                latitude = tilstBypark.latitude;
                break;
            case 24:
                longitude = toftegaardsparken.longitude;
                latitude = toftegaardsparken.latitude;
                break;
            case 25:
                longitude = vibyPark.longitude;
                latitude = vibyPark.latitude;
                break;
            case 26:
                longitude = aeblehaven.longitude;
                latitude = aeblehaven.latitude;
                break;
            case 27:
                longitude = oelstedGroenning.longitude;
                latitude = oelstedGroenning.latitude;
                break;
            case 28:
                longitude = oesterbyMose.longitude;
                latitude = oesterbyMose.latitude;
                break;
            case 29:
                longitude = aabakken.longitude;
                latitude = aabakken.latitude;
                break;
            case 30:
                longitude = aabyPark.longitude;
                latitude = aabyPark.latitude;
                break;
            case 31:
                longitude = aaparken.longitude;
                latitude = aaparken.latitude;
                break;
            case 32:
                longitude = vorrevangsparken.longitude;
                latitude = vorrevangsparken.latitude;
                break;
            case 33:
                longitude = indelukket.longitude;
                latitude = indelukket.latitude;
                break;
            case 34:
                longitude = hovmarken.longitude;
                latitude = hovmarken.latitude;
                break;
            case 35:
                longitude = hansBrogesBakker.longitude;
                latitude = hansBrogesBakker.latitude;
                break;
            case 36:
                longitude = klokkerparken.longitude;
                latitude = klokkerparken.latitude;
                break;
            case 37:
                longitude = hjulbjergparken.longitude;
                latitude = hjulbjergparken.latitude;
                break;
            case 38:
                longitude = katterhoej.longitude;
                latitude = katterhoej.latitude;
                break;
            case 39:
                longitude = vestervangsparken.longitude;
                latitude = vestervangsparken.latitude;
                break;
            case 40:
                longitude = tranbjergLaurbaerpark.longitude;
                latitude = tranbjergLaurbaerpark.latitude;
                break;
            case 41:
                longitude = TranbjergGroennloekkepark.longitude;
                latitude = TranbjergGroennloekkepark.latitude;
                break;
            case 42:
                longitude = vibyEnge.longitude;
                latitude = vibyEnge.latitude;
                break;
            case 43:
                longitude = moelleparken.longitude;
                latitude = moelleparken.latitude;
                break;
            case 44:
                longitude = marienlystparken.longitude;
                latitude = marienlystparken.latitude;
                break;
            case 45:
                longitude = hasleBakker.longitude;
                latitude = hasleBakker.latitude;
                break;
            case 46:
                longitude = botaniskHave.longitude;
                latitude = botaniskHave.latitude;
                break;
            case 47:
                longitude = raadhusparken.longitude;
                latitude = raadhusparken.latitude;
                break;
            case 48:
                longitude = praestevangen.longitude;
                latitude = praestevangen.latitude;
                break;
            case 49:
                longitude = mindeparken.longitude;
                latitude = mindeparken.latitude;
                break;
            case 50:
                longitude = hedeskovparken.longitude;
                latitude = hedeskovparken.latitude;
                break;
            case 51:
                longitude = skanseparken.longitude;
                latitude = skanseparken.latitude;
                break;
            case 52:
                longitude = sktOlofKirkegaard.longitude;
                latitude = sktOlofKirkegaard.latitude;
                break;
            case 53:
                longitude = baadepladsStadevej.longitude;
                latitude = baadepladsStadevej.latitude;
                break;
            case 54:
                longitude = tranbjergCenterpark.longitude;
                latitude = tranbjergCenterpark.latitude;
                break;
            case 55:
                longitude = vennelystparken.longitude;
                latitude = vennelystparken.latitude;
                break;
            case 56:
                longitude = tangkrogen.longitude;
                latitude = tangkrogen.latitude;
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
