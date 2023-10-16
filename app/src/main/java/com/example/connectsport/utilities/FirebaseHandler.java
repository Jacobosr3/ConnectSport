package com.example.connectsport.utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler {

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    public FirebaseHandler() {
        // Obtiene la instancia de la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addFood(String eventName) {
        // Agrega un elemento a la lista de alimentos en la base de datos de Firebase
        databaseReference.child("eventsList").push().setValue(eventName);
    }

    public void removeFood(String eventId) {
        if (eventId != null) {
            // Elimina un elemento de la lista de alimentos en la base de datos de Firebase
            DatabaseReference foodRef = databaseReference.child("eventsList").child(eventId);
            foodRef.removeValue();
        }
    }
}
