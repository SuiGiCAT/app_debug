package com.example.myapplication1;

import androidx.annotation.RawRes;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication1.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

public class MapsActivity extends FragmentActivity implements OnMarkerClickListener,
        OnMapReadyCallback, GoogleMap.OnCircleClickListener {

//    private Marker mPerth;
//    private Marker mSydney;
//    private Marker mBrisbane;
//
//    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
//    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
//    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String temp = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // ????????????
        /*--------------------------------Last Report-----------------------------------*/

        // a user interface to search for local code
        LocalCode lc = new LocalCode("?????????","???????????????????????????????????????");
        lc.getIndex();

        // we use local code to search for hotel info
        HotelData hd = new HotelData(lc);
        hd.getSmallData();

        // now locate all hotels in osaka takatsuki area!
        for(int i = 0 ;i < hd.hotelList.size() ;i++){
            LatLng htl = new LatLng(hd.hotelList.get(i).latitude,hd.hotelList.get(i).longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(htl)
                    .snippet(hd.hotelList.get(i).access)
                    .title(hd.hotelList.get(i).hotelName));
        }
        //make a simple Heatmap also, choosing 3 areas around takatsuki for example
        LocalCode lc1 = new LocalCode("?????????","???????????????????????????");
        lc1.getIndex();
        HotelData hd1 = new HotelData(lc1);
        hd1.getSmallData();

        LocalCode lc2 = new LocalCode("?????????","?????????????????????????????????");
        lc2.getIndex();
        HotelData hd2 = new HotelData(lc2);
        hd2.getSmallData();

        LocalCode lc3 = new LocalCode("?????????","????????????????????????????????????");
        lc3.getIndex();
        HotelData hd3 = new HotelData(lc3);
        hd3.getSmallData();

        List<WeightedLatLng> latLngList = new ArrayList<>();
        latLngList.add(new  WeightedLatLng(new LatLng(34.846193,135.617413),hd.hotelList.size()));
        latLngList.add(new  WeightedLatLng(new LatLng(34.814739,135.651136),hd1.hotelList.size()));
        latLngList.add(new  WeightedLatLng(new LatLng(34.626861,135.600752),hd2.hotelList.size()));
        latLngList.add(new  WeightedLatLng(new LatLng( 34.573326,135.483118),hd3.hotelList.size()));
        addHeatMap(latLngList);

        //move camera to osaka
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.641332,135.562939)));
        /*---------------------------------JSON TEXT------------------------------------*/
//            LatLng kansaiS = new LatLng(20,20);
//        try {
//            URL url = new URL("https://www.jma.go.jp/bosai/forecast/data/forecast/270000.json");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            //???bufferedReader?????????InputStreamReader????????????????????????
//            StringBuilder stringBuilder = new StringBuilder();
//            String line = null;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line); // ????????????????????????
//            }
//            bufferedReader.close();
//            urlConnection.disconnect();
//
//            /* StringBuilder ??? JSON ?????? */
//            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
//            JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
//            JSONArray tSeries = new JSONArray(jsonObject.getString("timeSeries"));
//            JSONObject taObject = new JSONObject(tSeries.get(2).toString()); //?????????????????????
//            JSONArray areas = new JSONArray(taObject.getString("areas"));
//            JSONObject atObject = new JSONObject(areas.get(0).toString());
//            JSONArray temps = new JSONArray(atObject.getString("temps"));
//            temp = temps.getString(0);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mMap.addMarker(new MarkerOptions().position(kansaiS)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                .snippet(hd3.hotelList.size()+"")
//                .title(hd2.hotelList.size()+""));

        /*------------------------------------------------------------------*/

//        PolylineOptions rectOption = new PolylineOptions().add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.45, -122.0))
//                .add(new LatLng(37.45, -122.2))
//                .add(new LatLng(37.35, -122.2))
//                .add(new LatLng(37.35, -122.0)).color(Color.RED);// Closes the polyline?
//        Polyline Polyline = mMap.addPolyline(rectOption); //??????????????????????????????options???polyline
//
//        PolygonOptions rectOption2 = new PolygonOptions().add(new LatLng(37.35, -122.0),
//                new LatLng(37.45, -122.0),
//                new LatLng(37.45, -122.2),
//                new LatLng(37.35, -122.2),
//                new LatLng(37.35, -122.0)).strokeColor(Color.RED)
//                .fillColor(Color.BLUE);
//        Polygon polygon = mMap.addPolygon(rectOption2);
//
//        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(0, 0), new LatLng(0, 5), new LatLng(3, 5), new LatLng(0, 0))
//                .strokeColor(Color.RED)
//                .fillColor(Color.BLUE));
//        Polygon polygon2 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(0, 0), new LatLng(0, 5), new LatLng(3, 5))
//                .strokeColor(Color.RED)
//                .fillColor(Color.BLUE));
//
//        Polygon polygon3 = mMap.addPolygon(new PolygonOptions()
//                .add(new LatLng(0, 0), new LatLng(0, 5), new LatLng(3, 5),
//                        new LatLng(3, 0), new LatLng(0, 0))
//                .addHole(Arrays.asList(new LatLng(1, 1),
//                        new LatLng(1, 2), new LatLng(2, 2),
//                        new LatLng(2, 1), new LatLng(1, 1)))
//                .fillColor(Color.BLUE));
//
//        CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(37.4, -100.1))
//                .radius(2000)
//                .strokeWidth(10)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                .clickable(true);
        ; // In meters
        // Get back the mutable Circle
//        Circle circle = mMap.addCircle(circleOptions);
//
//        List<PatternItem> patternItemList = Arrays.asList(new Dot()
//                , new Gap(20)
//                , new Dash(30)
//                , new Gap(20));
//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(-37.81319, 144.96298), new LatLng(-31.95285, 115.85734))
//                .width(10)
//                .color(Color.BLUE)
//                .geodesic(true));
//        line.setPattern(patternItemList);
//        line.setJointType(JointType.ROUND);
//        line.setStartCap(new RoundCap());
//        line.setEndCap(//??????????????????????????????????????????
//                new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone),
//                        16));
//        line.setTag("A");

        //???????????????????????????????????????????????????polyline . setpoints()???????????????????????????????????????
        // Add some markers to the map, and add a data object to each marker.
        // Add a marker in Sydney and move the camera


        /*---------------------------???MARK TEXT???------------------------------*/
//
//        LatLng kansaiD = new LatLng(34.877931, 135.576883);
//        LatLng kansaiA = new LatLng(34.878108, 135.575549);
//        LatLng kansaiB = new LatLng(34.877512, 135.576140);
//        LatLng kansaiC = new LatLng(34.878179, 135.576440);
//        LatLng kansaiK = new LatLng(34.878794, 135.575790);
//
//        mMap.addMarker(new MarkerOptions().position(kansaiD)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .snippet("????????????")
//                .title("Marker in D"));
//        mMap.addMarker(new MarkerOptions().position(kansaiA)
//                .snippet("??????????????????")
//                .title("Marker in A"));
//        mMap.addMarker(new MarkerOptions().position(kansaiB)
//                .snippet("??????????????????")
//                .title("Marker in B"));
//        mMap.addMarker(new MarkerOptions().position(kansaiC)
//                .snippet("???????????????")
//                .title("Marker in C"));
//        mMap.addMarker(new MarkerOptions().position(kansaiK)
//                .snippet("???????????????")
//                .title("Marker in K")
//        );
//
//        Polyline lineD_S = mMap.addPolyline(new PolylineOptions()
//                .add(kansaiD, kansaiS)
//                .width(10)
//                .color(Color.BLUE));
//        lineD_S.setPattern(patternItemList);
//
//        PolylineOptions rectKansai = new PolylineOptions()
//                .add(new LatLng(34.876239, 135.573200))
//                .add(new LatLng(34.878362, 135.573915))
//                .add(new LatLng(34.879777, 135.576799))
//                .add(new LatLng(34.879777, 135.579412))
//                .add(new LatLng(34.878220, 135.580570))
//                .add(new LatLng(34.876461, 135.580052))
//                .add(new LatLng(34.874844, 135.581975))
//                .add(new LatLng(34.873125, 135.581704))
//                .add(new LatLng(34.871891, 135.579362))
//                .add(new LatLng(34.876239, 135.573200))
//                .color(Color.RED);
//        Polyline PolyKansai = mMap.addPolyline(rectKansai);
//
//        CircleOptions circleOptionL = new CircleOptions()
//                .center(new LatLng(34.876580, 135.576367))
//                .radius(28)
//                .strokeWidth(10)
//                .strokeColor(Color.GREEN)
//                .fillColor(Color.argb(128, 255, 0, 0))
//                .clickable(true);
//        Circle circleL = mMap.addCircle(circleOptionL);

        /*------------------------------------------------------------------------------*/

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(10, 10))
//                .title("Marker z1")
//                .zIndex(1.0f));
//
//        mPerth = mMap.addMarker(new MarkerOptions().position(PERTH)
//                .alpha(0.7f).title("Perth!").flat(true)
//                .draggable(true));
//        mPerth.setTag(0);
//
//        mSydney = mMap.addMarker(new MarkerOptions().position(SYDNEY).title("Sydney!")
//                .visible(false));
//        mSydney.setTag(0);
//        // ??????????????????????????????90???????????????????????0.5,0.5??????????????????????????????????????????????????????
//        mBrisbane = mMap.addMarker(new MarkerOptions().position(BRISBANE).anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .title("?"));
//        mBrisbane.setTag(0);
//        //???????????????????????????????????????????????????????????????????????????
//        // Set a listener for marker click.
//        //?????????z-index??????????????????????????????z-index??????????????????????????????z-index??????0???
//        mMap.setOnMarkerClickListener(this);
//        mMap.setOnCircleClickListener(this);
    }

    private void addHeatMap(List<WeightedLatLng> latLngs) {
        // Create a heat map tile provider, passing it the latlngs of the areas.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .weightedData((latLngs))
                .radius(50)
                .maxIntensity(40)// set the maximum intensity
                .opacity(0.5)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker. ????????????????????????
        Integer clickCount = (Integer)marker.getTag();

        // Check if a click count was set, then display the click count
        if(clickCount != null){
            clickCount += 1;
            marker.setTag(clickCount);
            Toast.makeText(this,marker.getTitle() +
                    "has been clicked" +
                    clickCount + "times" , Toast.LENGTH_SHORT).show();
//          Toast ????????? View ???????????????????????????????????????????????????
//          Toast ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ???????????? / ?????????
//          Toast ????????????????????????????????????????????? Toast.makeText???
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that
        // the marker is centered and for the marker's info window to open, if it has one).

        return false;
    }

    @Override
    public void onCircleClick(Circle circle) {
        // Flip the r, g and b components of the circle's stroke color.???
        int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
        circle.setStrokeColor(strokeColor);
    }
    /*
    ????????????????????????????????????????????????draggable???????????????true??????????????????????????????
    ????????????????????????????????????????????????????????????????????????????????????????????????
     */
}
