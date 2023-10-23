package com.example.connectsport.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.connectsport.R;
import com.example.connectsport.utilities.Events;

public class FragmentMain1 extends Fragment implements OnEventsClickListener, OnEventsLongClickListener {

    private ImageButton newEvents_btn;
    private RecyclerView feed_events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main1, container, false);
        newEvents_btn = view.findViewById(R.id.new_events);
        feed_events = view.findViewById(R.id.events_feed_recycler_view);

        // Listener para nuevo evento
        newEvents_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewEventsActivity.class);
            startActivity(intent);
        });

        // Cargamos los eventos por orden de creación ascendente
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = firestore.collection("events");
        Query query = eventsRef.orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Events> options = new FirestoreRecyclerOptions.Builder<Events>()
                .setQuery(query, Events.class)
                .build();

        // Configurar el RecyclerView con un LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        feed_events.setLayoutManager(layoutManager);

        // Configuramos el firestore recycler adapter
        FirestoreRecyclerAdapter<Events, EventsViewHolder> adapter = new FirestoreRecyclerAdapter<Events, EventsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull Events model) {
                String eventsId = this.getSnapshots().getSnapshot(position).getId();
                DocumentReference eventRef = firestore.collection("events").document(eventsId);
                model.setRef(eventRef);
                // Bind the event data to the view holder
                holder.bind(model);
            }

            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new view holder for the event items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item, parent, false);
                return new EventsViewHolder(view, FragmentMain1.this, FragmentMain1.this);
            }
        };

        feed_events.setAdapter(adapter);
        adapter.startListening();

        return view;
    }

    // Manejamos el click del recycler para abrir la vista detallada
    @Override
    public void onEventClick(int position) {
        // Obtener el adaptador del RecyclerView
        FirestoreRecyclerAdapter<Events, EventsViewHolder> adapter = (FirestoreRecyclerAdapter<Events, EventsViewHolder>) feed_events.getAdapter();

        // Obtener el objeto event correspondiente a la posición del elemento clicado
        Events events = adapter.getItem(position);

        Intent intent = new Intent(getActivity(), DetailedEventsActivity.class);
        intent.putExtra("events", events);
        startActivity(intent);
    }

    @Override
    public void onEventLongClick(int position) {
        // LongClick
    }
}