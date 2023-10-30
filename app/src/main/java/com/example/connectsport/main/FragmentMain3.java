package com.example.connectsport.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.connectsport.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentMain3 extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{
    GoogleMap mMap;
    private Button mTypeBtn;
    ArrayList<String> eventsTitles = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");
    List<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main3, container, false);
        mTypeBtn = (Button) view.findViewById(R.id.btnHydrido);
        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            boolean isHybrid = true;
            @Override
            public void onClick(View v) {
                if (isHybrid) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                isHybrid = !isHybrid;
            }
        });

        //Recorrer la base datos para guardar las localidads en un arraylist
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String campo = documentSnapshot.getString("eventsTitle");
                    if (!arrayList.contains(campo)) {
                        arrayList.add(campo);
                    }
                }
                for (String event : arrayList) {
                    Log.d("AÑADIDO..............................", "Event: " + event);
                    // Añadir marcador utilizando la ubicación del evento
                    addMarkerByLocationName(event);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error al leer los valores.", e);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                       return;
        }
        mMap.setMyLocationEnabled(true);
        /*//no mostrar el boton
        mMap.getUiSettings().setMyLocationButtonEnabled(false);*/

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    private void addMarkerByLocationName(String locationName) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Maneja la excepción de manera más significativa, posiblemente notificando al usuario sobre cualquier problema.
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
}
