package com.example.connectsport.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.connectsport.R;
import com.example.connectsport.utilities.Events;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedEventsActivity extends AppCompatActivity {

    private Events mEvents;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference votesRef = db.collection("votes");

    private TextView eventTitle, eventIngredients, eventElaboration, eventAttend, tvEventServings, tvEventTime, tvEventCreatorUsername, tvEventCreatedAt, tvVoteCounter;
    private RatingBar ratingBar;
    private ViewPager mViewPager;
    private ChipGroup chipGroup;
    private Chip chip_1, chip_2, chip_3, chip_4;
    private View separador_chip;
    private ImageView servings_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_big_view);

        // Obtener el objeto Event enviado desde la actividad anterior
        mEvents = getIntent().getParcelableExtra("events");

        // Declaración de campos
        eventTitle = findViewById(R.id.tvEventTitle);
        ratingBar = findViewById(R.id.event_rating);
        eventIngredients = findViewById(R.id.tvEventIngredients);
        eventElaboration = findViewById(R.id.tvEventElaboration);
        eventAttend = findViewById(R.id.tvEventAttend);
        tvEventServings = findViewById(R.id.tvEventServings);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventCreatorUsername = findViewById(R.id.tvEventCreatorUsername);
        tvEventCreatedAt = findViewById(R.id.tvEventCreatedAt);
        tvVoteCounter = findViewById(R.id.tvVoteCounter);
        mViewPager = findViewById(R.id.event_viewPager);
        servings_icon = findViewById(R.id.iv_servings_big_view);
        chipGroup = findViewById(R.id.chipGroup);
        chip_1 = findViewById(R.id.chip_1);
        chip_2 = findViewById(R.id.chip_2);
        chip_3 = findViewById(R.id.chip_3);
        chip_4 = findViewById(R.id.chip_4);
        separador_chip = findViewById(R.id.separador_chip);
        Button btnDeleteEvent = findViewById(R.id.btnDelete);
        if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mEvents.getCreatorUid())) {
            btnDeleteEvent.setVisibility(View.GONE);
        }

        Button btnAttendEvent = findViewById(R.id.btnAttend);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String currentUserUid = auth.getCurrentUser().getUid();
            if (currentUserUid.equals(mEvents.getCreatorUid())) {
                btnAttendEvent.setVisibility(View.GONE);
            } else {
                btnAttendEvent.setVisibility(View.VISIBLE);
            }
        }

        // Verificar el estado de asistencia guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("asistencia_" + mEvents.getRef().getId(), Context.MODE_PRIVATE);
        boolean asistenciaConfirmada = sharedPreferences.getBoolean("asistencia_confirmada", false);

        if (asistenciaConfirmada) {
            btnAttendEvent.setText("Cancelar Asistencia");
            btnAttendEvent.setOnClickListener(v -> cancelarAsistencia());
        } else {
            btnAttendEvent.setText("Asistiré");
            btnAttendEvent.setOnClickListener(this::asistirEvento);
        }

    // Manejar el adaptador del recycler
        List<String> images = mEvents.getImages();
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, images);
        mViewPager.setAdapter(adapter);

        // Cargar título de la receta
        eventTitle.setText(mEvents.getEventsTitle());

        // Checks para comprobar si el número de comensales es singular o plural, y poner tanto el string como el recurso drawable correspondiente
        String servings = mEvents.getTvEventsServings();
        char lastChar = servings.charAt(servings.length() - 1);
        String servingsStr = (lastChar > '1')
                ? getApplicationContext().getString(R.string.num_people_value)
                : getApplicationContext().getString(R.string.num_person_value);
        int imageResId = (lastChar > '1')
                ? R.drawable.ic_people_newrecipe
                : R.drawable.ic_person_newrecipe;

        servings_icon.setImageResource(imageResId);
        tvEventServings.setText(servings + " " + servingsStr);

        // Cargamos mas datos de la receta
        eventIngredients.setText(mEvents.getEventsIngredients());
        eventElaboration.setText(mEvents.getEventsElaboration());
        tvEventTime.setText(mEvents.getTvEventsTime());

        // Verificar si la lista de etiquetas está vacía y cargarla según lo que haya
        if (mEvents.getTags() != null && mEvents.getTags().size() > 0) {
            for (int i = 0; i < mEvents.getTags().size(); i++) {
                String tag = mEvents.getTags().get(i);
                if (i == 0) {
                    chip_1.setText(tag);
                    chip_1.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    chip_2.setText(tag);
                    chip_2.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    chip_3.setText(tag);
                    chip_3.setVisibility(View.VISIBLE);
                } else if (i == 3) {
                    chip_4.setText(tag);
                    chip_4.setVisibility(View.VISIBLE);
                }
            }
        } else {
            chipGroup.setVisibility(View.GONE);
            separador_chip.setVisibility(View.GONE);
        }

        // Cargamos el rating.
        ratingBar.setRating(mEvents.getRating());

        // Cargamos el creador
        tvEventCreatorUsername.setText(getString(R.string.created_by) + " " + mEvents.getCreatorUsername());

        // Cargamos el creador
        eventAttend.setText("Participantes: " + mEvents.getEventsAttend());

        // Formateamos el Date y lo mostramos
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        String createdAtString = sdf.format(mEvents.getCreatedAt());
        tvEventCreatedAt.setText(createdAtString);

        // Mostramos el contador de votos
        tvVoteCounter.setText("(" + mEvents.getVoteCounter() + ")");

        // Manejar listener de ratingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                mostrarConfirmacion(rating);
            }
        });

    }

    private void mostrarConfirmacion(float rating) {
        String recipeId = mEvents.getRef().getId();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Consultar si el usuario ya ha votado esta receta
        Query query = votesRef.whereEqualTo("recipeId", recipeId)
                .whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                if (result != null && !result.isEmpty()) {
                    // Si ya existe un voto, mostrar mensaje de error
                    Toast.makeText(this, getString(R.string.already_voted), Toast.LENGTH_SHORT).show();
                    // Devolver el valor del ratingBar a antes de que el usuario intentara votar
                    ratingBar.setRating(mEvents.getRating());
                } else {
                    // Si no existe un voto, mostrar la confirmación al usuario
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(String.format(getString(R.string.vote_confirm), rating))
                            .setCancelable(false)
                            .setPositiveButton("Sí", (dialog, id) -> guardarValoracion(rating))
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {
                Toast.makeText(this, getString(R.string.vote_retrieve_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarValoracion(float valoracion) {
        // Obtenemos la referencia a la receta y el usuario actual
        DocumentReference ref = mEvents.getRef();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Incrementamos el contador de votos en firebase
        ref.update("voteCounter", FieldValue.increment(1));

        // Calcular y actualizar la valoración promedio
        ref.get().addOnSuccessListener(documentSnapshot -> {
            int voteCounter = documentSnapshot.getLong("voteCounter").intValue();
            float oldRating = documentSnapshot.getDouble("rating").floatValue();
            float newRating = (oldRating * (voteCounter - 1) + valoracion) / voteCounter;
            ref.update("rating", newRating);
            ratingBar.setRating(newRating);
        });

        // Registrar el voto del usuario actual en la colección "votes"
        Map<String, Object> voteData = new HashMap<>();
        voteData.put("recipeId", ref.getId());
        voteData.put("userId", userId);
        voteData.put("rating", valoracion);
        votesRef.add(voteData)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, getString(R.string.voted_correctly), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, getString(R.string.vote_add_error), Toast.LENGTH_SHORT).show());
    }

    // Gestión del viewPager para cargar y mostrar las imagenes scrolleables.
    public class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private List<String> mImages;

        public ViewPagerAdapter(Context context, List<String> images) {
            mContext = context;
            mImages = images;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext)
                    .load(Uri.parse(mImages.get(position)))
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView) object);
        }
    }
    public void borrarEvento(View view) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String creatorUid = mEvents.getCreatorUid();

        // Verificar si el usuario actual es el creador del evento
        if (currentUserId.equals(creatorUid)) {
            // Obtén la referencia al documento que deseas eliminar
            DocumentReference eventRef = mEvents.getRef();

            // Elimina el documento
            eventRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Evento eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        // Termina la actividad después de eliminar el evento si es necesario
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar evento", Toast.LENGTH_SHORT).show());
        } else {
            // Mostrar un mensaje si el usuario actual no es el creador del evento
            Toast.makeText(this, "No tienes permiso para eliminar este evento", Toast.LENGTH_SHORT).show();
        }
    }

    public void asistirEvento(View view) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String creatorUid = mEvents.getCreatorUid();

        SharedPreferences sharedPreferences = getSharedPreferences("asistencia_" + mEvents.getRef().getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!currentUserId.equals(creatorUid)) {
            // Agregar lógica para marcar o cancelar la asistencia aquí

            // Verificar si el usuario ya ha confirmado su asistencia previamente
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference attendanceRef = db.collection("asistencias");
            attendanceRef.whereEqualTo("userId", currentUserId)
                    .whereEqualTo("eventId", mEvents.getRef().getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Toast.makeText(this, "Has marcado tu asistencia", Toast.LENGTH_SHORT).show();
                            // Obtenemos la referencia a la receta y el usuario actual
                            DocumentReference ref = mEvents.getRef();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Incrementamos el contador de votos en firebase
                            ref.update("eventsAttend", FieldValue.increment(1));

                            // El usuario ya ha confirmado su asistencia, permitir cancelarla
                            Button btnAttendEvent = findViewById(R.id.btnAttend);
                            btnAttendEvent.setText("Cancelar Asistencia");
                            btnAttendEvent.setOnClickListener(v -> cancelarAsistencia());

                            // Guardar el estado de asistencia en SharedPreferences
                            editor.putBoolean("asistencia_confirmada", true);
                            editor.apply();
                        } else {
                            // El usuario aún no ha confirmado su asistencia, permitir confirmarla
                            Map<String, Object> asistenciaData = new HashMap<>();
                            asistenciaData.put("userId", currentUserId);
                            asistenciaData.put("eventId", mEvents.getRef().getId());

                            attendanceRef.add(asistenciaData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Has marcado tu asistencia", Toast.LENGTH_SHORT).show();
                                        DocumentReference ref = mEvents.getRef();
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        // Incrementamos el contador de votos en firebase
                                        ref.update("eventsAttend", FieldValue.increment(1));

                                        // Cambiar el texto del botón o su estado visual para reflejar que ya han marcado asistencia
                                        Button btnAttendEvent = findViewById(R.id.btnAttend);
                                        btnAttendEvent.setText("Cancelar Asistencia");
                                        btnAttendEvent.setOnClickListener(v -> cancelarAsistencia());

                                        // Guardar el estado de asistencia en SharedPreferences
                                        editor.putBoolean("asistencia_confirmada", true);
                                        editor.apply();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Error al marcar asistencia", Toast.LENGTH_SHORT).show());
                        }
                    });
        } else {
            Toast.makeText(this, "No puedes marcar asistencia en tu propio evento", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelarAsistencia() {
        DocumentReference ref = mEvents.getRef();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Incrementamos el contador de votos en firebase
        ref.update("eventsAttend", FieldValue.increment(-1));

        // Aquí, mostramos un Toast para indicar que la asistencia ha sido cancelada.
        Toast.makeText(this, "Has cancelado tu asistencia", Toast.LENGTH_SHORT).show();
        // Luego, puedes ajustar el texto del botón nuevamente para permitir que los usuarios marquen su asistencia.
        Button btnAttendEvent = findViewById(R.id.btnAttend);
        btnAttendEvent.setText("Asistiré");
        btnAttendEvent.setOnClickListener(this::asistirEvento);

        // Guardar el estado de asistencia en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("asistencia_" + mEvents.getRef().getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("asistencia_confirmada", false);
        editor.apply();
    }
}