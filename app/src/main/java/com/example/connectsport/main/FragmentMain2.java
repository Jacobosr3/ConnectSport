package com.example.connectsport.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectsport.R;
import com.example.connectsport.utilities.Events;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentMain2 extends Fragment implements OnEventsClickListener, OnEventsLongClickListener {

    private RecyclerView feed_events;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, container, false);
        feed_events = view.findViewById(R.id.events_feed_recycler_view);
        imageView = view.findViewById(R.id.buscarButton);

        imageView.setOnClickListener(view1 -> {
            androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(getContext(), imageView);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_option1:
                        // Cargamos los eventos
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        CollectionReference eventsRef = firestore.collection("events");
                        Query query = eventsRef.orderBy("eventsAttend", Query.Direction.DESCENDING);
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
                                return new EventsViewHolder(view, FragmentMain2.this, FragmentMain2.this);
                            }
                        };

                        feed_events.setAdapter(adapter);
                        adapter.startListening();

                        return true;
                    case R.id.menu_option2:
                        // Cargamos los eventos por orden de creación ascendente
                        firestore = FirebaseFirestore.getInstance();
                        eventsRef = firestore.collection("events");
                        query = eventsRef.orderBy("eventsAttend", Query.Direction.ASCENDING);
                        options = new FirestoreRecyclerOptions.Builder<Events>()
                                .setQuery(query, Events.class)
                                .build();

                        // Configurar el RecyclerView con un LinearLayoutManager
                        layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        feed_events.setLayoutManager(layoutManager);

                        // Configuramos el firestore recycler adapter
                        adapter = new FirestoreRecyclerAdapter<Events, EventsViewHolder>(options) {
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
                                return new EventsViewHolder(view, FragmentMain2.this, FragmentMain2.this);
                            }
                        };

                        feed_events.setAdapter(adapter);
                        adapter.startListening();

                        return true;
                    case R.id.menu_option3:
                        // Cargamos los eventos
                        firestore = FirebaseFirestore.getInstance();
                        eventsRef = firestore.collection("events");

                        query = eventsRef.whereEqualTo("selectedEventsType", "Fútbol");

                        options = new FirestoreRecyclerOptions.Builder<Events>()
                                .setQuery(query, Events.class)
                                .build();

                        // Configurar el RecyclerView con un LinearLayoutManager
                        layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        feed_events.setLayoutManager(layoutManager);

                        // Configuramos el firestore recycler adapter
                        adapter = new FirestoreRecyclerAdapter<Events, EventsViewHolder>(options) {
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
                                return new EventsViewHolder(view, FragmentMain2.this, FragmentMain2.this);
                            }
                        };

                        feed_events.setAdapter(adapter);
                        adapter.startListening();
                        return true;
                    case R.id.menu_option4:
                        // Code to delete the picture
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });

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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(FragmentMain2.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();
    }
}