package com.example.connectsport.main;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.connectsport.R;
import com.example.connectsport.utilities.Events;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NewEventsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton selectedImageButton;

    private String selectedeventType, eventTitle, eventIngredients, eventElaboration;

    ImageButton increase_time, increase_servings, decrease_time, decrease_servings, event_imageButton1, event_imageButton2, event_imageButton3;
    ImageView  person, group;
    EditText eteventTitle;
    EditText eteventIngredients;
    EditText eteventDirections;
    TextView tveventTime;
    TextView tveventServings;
    TextView tvTimeModifier;
    private int time_modifier_value = 5;
    private int eventTime = 0;
    private boolean isRange = false;
    private int servings = 1;
    ChipGroup chipGroup;
    Chip chipLow, chipMedium, chipHigh;
    SwitchMaterial switch_public_private;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_events);

        eteventTitle = findViewById(R.id.et_title);
        event_imageButton1 = findViewById(R.id.event_imageButton1);
        event_imageButton2 = findViewById(R.id.event_imageButton2);
        event_imageButton3 = findViewById(R.id.event_imageButton3);
        eteventIngredients = findViewById(R.id.et_ingredients);
        eteventDirections = findViewById(R.id.et_elaboration);
        tveventTime = findViewById(R.id.tv_time_value);
        tvTimeModifier = findViewById(R.id.time_amount);
        tveventServings = findViewById(R.id.tv_num_people_value);
        decrease_time = findViewById(R.id.btn_decrease_time);
        decrease_servings = findViewById(R.id.btn_decrease_servings);
        increase_time = findViewById(R.id.btn_increase_time);
        increase_servings = findViewById(R.id.btn_increase_servings);
        Spinner speventType = findViewById(R.id.sp_event_type);
        chipGroup = findViewById(R.id.chip_group);
        Button btnCreateevent = findViewById(R.id.btn_create_event);
        switch_public_private = findViewById(R.id.switch_public_private);

        person = findViewById(R.id.newevent_person);
        group = findViewById(R.id.newevent_group);

        // Listeners para añadir imagenes
        event_imageButton1.setOnClickListener(view -> {
            selectedImageButton = event_imageButton1;
            openGallery();
        });
        event_imageButton2.setOnClickListener(view -> {
            selectedImageButton = event_imageButton2;
            openGallery();
        });
        event_imageButton3.setOnClickListener(view -> {
            selectedImageButton = event_imageButton3;
            openGallery();
        });

        // Declaración de chips y asignación de colores
        chipLow = findViewById(R.id.low);
        chipLow.setChipBackgroundColorResource(R.color.chip_background_high_protein_unselected);
        chipLow.setChipStrokeColorResource(R.color.chip_stroke_high_protein_unselected);
        chipLow.setChipStrokeWidthResource(R.dimen.chip_stroke_width);
        chipLow.setTextColor(ContextCompat.getColor(this, R.color.chip_text_high_protein_unselected));
        chipLow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipLow.setChipBackgroundColorResource(R.color.chip_background_high_protein_selected);
                chipLow.setChipStrokeColorResource(R.color.chip_stroke_high_protein_selected);
                chipLow.setTextColor(ContextCompat.getColor(this, R.color.chip_text_high_protein_selected));
            } else {
                chipLow.setChipBackgroundColorResource(R.color.chip_background_high_protein_unselected);
                chipLow.setChipStrokeColorResource(R.color.chip_stroke_high_protein_unselected);
                chipLow.setTextColor(ContextCompat.getColor(this, R.color.chip_text_high_protein_unselected));
            }
        });

        chipMedium = findViewById(R.id.medium);
        chipMedium.setChipBackgroundColorResource(R.color.chip_background_low_fat_unselected);
        chipMedium.setChipStrokeColorResource(R.color.chip_stroke_low_fat_unselected);
        chipMedium.setChipStrokeWidthResource(R.dimen.chip_stroke_width);
        chipMedium.setTextColor(ContextCompat.getColor(this, R.color.chip_text_low_fat_unselected));
        chipMedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipMedium.setChipBackgroundColorResource(R.color.chip_background_low_fat_selected);
                chipMedium.setChipStrokeColorResource(R.color.chip_stroke_low_fat_selected);
                chipMedium.setTextColor(ContextCompat.getColor(this, R.color.chip_text_low_fat_selected));
            } else {
                chipMedium.setChipBackgroundColorResource(R.color.chip_background_low_fat_unselected);
                chipMedium.setChipStrokeColorResource(R.color.chip_stroke_low_fat_unselected);
                chipMedium.setTextColor(ContextCompat.getColor(this, R.color.chip_text_low_fat_unselected));
            }
        });

        chipHigh = findViewById(R.id.high);
        chipHigh.setChipBackgroundColorResource(R.color.chip_background_gluten_free_unselected);
        chipHigh.setChipStrokeColorResource(R.color.chip_stroke_gluten_free_unselected);
        chipHigh.setChipStrokeWidthResource(R.dimen.chip_stroke_width);
        chipHigh.setTextColor(ContextCompat.getColor(this, R.color.chip_text_gluten_free_unselected));
        chipHigh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chipHigh.setChipBackgroundColorResource(R.color.chip_background_gluten_free_selected);
                chipHigh.setChipStrokeColorResource(R.color.chip_stroke_gluten_free_selected);
                chipHigh.setTextColor(ContextCompat.getColor(this, R.color.chip_text_gluten_free_selected));
            } else {
                chipHigh.setChipBackgroundColorResource(R.color.chip_background_gluten_free_unselected);
                chipHigh.setChipStrokeColorResource(R.color.chip_stroke_gluten_free_unselected);
                chipHigh.setTextColor(ContextCompat.getColor(this, R.color.chip_text_gluten_free_unselected));
            }
        });

        // Establecemos los valores por defecto en los menús de tiempo y comensales
        tveventTime.setText(eventTime + " " + getString(R.string.time_value));
        tvTimeModifier.setText("±" + time_modifier_value + " " + getString(R.string.time_value));
        tveventServings.setText(servings + " " + getString(R.string.num_person_value));

        // Sistema para modificador del tiempo
        ConstraintLayout time_modifier = findViewById(R.id.time_layout_clickable);
        time_modifier.setOnClickListener(v -> {
            if (time_modifier_value == 5) {
                time_modifier_value = 10;
            } else if (time_modifier_value == 10) {
                time_modifier_value = 30;
            } else {
                time_modifier_value = 5;
            }
            tvTimeModifier.setText("±" + time_modifier_value + " " + getString(R.string.time_value));

        });

        // Sistema para aumentar o disminuir tiempo basado en el modificador
        increase_time.setOnClickListener(v -> {
            eventTime += time_modifier_value;
            if (eventTime >= 60) {
                int[] hoursAndMinutes = getHoursAndMinutes(eventTime);
                tveventTime.setText(hoursAndMinutes[0] + " h " + hoursAndMinutes[1] + " min");
            } else {
                tveventTime.setText(eventTime + " " + getString(R.string.time_value));
            }
        });

        decrease_time.setOnClickListener(v -> {
            if (eventTime > 0) {
                eventTime -= time_modifier_value;
                eventTime = Math.max(eventTime, 0); //establecer el tiempo total a 0 si ha bajado de 0 por los modificadores
                if (eventTime >= 60) {
                    int[] hoursAndMinutes = getHoursAndMinutes(eventTime);
                    tveventTime.setText(hoursAndMinutes[0] + " h " + hoursAndMinutes[1] + " min");
                } else {
                    tveventTime.setText(eventTime + " " + getString(R.string.time_value));
                }
            }
        });

        // Sistema para el modificador de comensales
        ConstraintLayout servings_modifier = findViewById(R.id.servings_layout_clickable);
        servings_modifier.setOnClickListener(v -> {
            isRange = !isRange;
            if (isRange) {
                group.setColorFilter(Color.parseColor("#FF9401"), PorterDuff.Mode.SRC_IN);
                person.setColorFilter(Color.parseColor("#E06B01"), PorterDuff.Mode.SRC_IN);
                if (servings == 9) servings = 8;
                tveventServings.setText(servings + " - " + (servings + 1) + " " + getResources().getString(R.string.num_people_value));
            } else {
                person.setColorFilter(Color.parseColor("#FF9401"), PorterDuff.Mode.SRC_IN);
                group.setColorFilter(Color.parseColor("#E06B01"), PorterDuff.Mode.SRC_IN);
                if (servings == 1) {
                    tveventServings.setText(servings + " " + getResources().getString(R.string.num_person_value));
                } else {
                    tveventServings.setText(servings + " " + getResources().getString(R.string.num_people_value));
                }
            }
        });

        // Sistema para aumentar o disminuir comensales basado en el modificador
        increase_servings.setOnClickListener(v -> {
            if (isRange) {
                if (servings < 49) {
                    servings += 1;
                    tveventServings.setText(servings + " - " + (servings + 1) + " " + getResources().getString(R.string.num_people_value));
                }
            } else {
                if (servings < 50) {
                    servings += 1;
                    if (servings == 1) {
                        tveventServings.setText(servings + " " + getResources().getString(R.string.num_person_value));
                    } else {
                        tveventServings.setText(servings + " " + getResources().getString(R.string.num_people_value));
                    }
                }
            }
        });

        decrease_servings.setOnClickListener(v -> {
            if (isRange) {
                if (servings > 1) {
                    servings -= 1;
                    tveventServings.setText(servings + " - " + (servings + 1) + " " + getResources().getString(R.string.num_people_value));
                }
            } else {
                if (servings > 1) {
                    servings -= 1;
                    if (servings == 1) {
                        tveventServings.setText(servings + " " + getResources().getString(R.string.num_person_value));
                    } else {
                        tveventServings.setText(servings + " " + getResources().getString(R.string.num_people_value));
                    }
                }
            }
        });

        // Set up Spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speventType.setAdapter(adapter);

        // Add OnItemSelectedListener to Spinner
        speventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item from the Spinner
                selectedeventType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Set up button click listener
        btnCreateevent.setOnClickListener(view -> {
            btnCreateevent.setEnabled(false);
            eventTitle = String.valueOf(eteventTitle.getText());
            eventIngredients = String.valueOf(eteventIngredients.getText());
            eventElaboration = String.valueOf(eteventDirections.getText());
            if (!isDefaultImage(event_imageButton1) || !isDefaultImage(event_imageButton2) || !isDefaultImage(event_imageButton3)) {
                if (!eventTitle.isEmpty() || !eventIngredients.isEmpty() || !eventElaboration.isEmpty()) {
                    if (eventTime != 0) {
                        if (!Objects.equals(selectedeventType, "-")) {
                            ProgressDialog progressDialog = new ProgressDialog(NewEventsActivity.this);
                            progressDialog.setMessage(getString(R.string.create_event_progress));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            // Tras manejar campos vacíos, abrimos nuevo hilo para crear la evento y bloqueamos el principal con el processDialog
                            new Thread(() -> {
                                createevent(progressDialog);
                                runOnUiThread(() -> {});
                            }).start();
                        }
                    }
                }
            } else {
                Toast.makeText(NewEventsActivity.this, getString(R.string.create_event_error_emptyfields), Toast.LENGTH_LONG).show();
                btnCreateevent.setEnabled(true);
            }
        });
    }

    public static int[] getHoursAndMinutes(int minutes) {
        int[] result = new int[2];
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        result[0] = hours;
        result[1] = remainingMinutes;
        return result;
    }

    // Método que se llama cuando se presiona un ImageButton
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Método que se llama cuando el usuario selecciona una imagen de la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            selectedImageButton.setImageURI(selectedImageUri);
        }
    }

    private boolean isDefaultImage(ImageButton imageButton) {
        Drawable currentDrawable = imageButton.getDrawable();
        Drawable defaultDrawable = getResources().getDrawable(R.drawable.ic_add_image);
        return currentDrawable.getConstantState().equals(defaultDrawable.getConstantState());
    }

    private void createevent(ProgressDialog progressDialog) {

        // Cargamos datos para crear evento
        eventTitle = eteventTitle.getText().toString().trim();
        eventIngredients = eteventIngredients.getText().toString().trim();
        eventElaboration = eteventDirections.getText().toString().trim();
        byte[] event_image1 = null;
        byte[] event_image2 = null;
        byte[] event_image3 = null;
        final String[] image1 = new String[1];
        final String[] image2 = new String[1];
        final String[] image3 = new String[1];

        // Comprobamos los chips seleccionados y los añadimos
        List<String> selectedTags = new ArrayList<>();
        GridLayout gridLayout = (GridLayout) chipGroup.getChildAt(0);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip.isChecked()) {
                    selectedTags.add(chip.getText().toString());
                }
            }
        }

        // Comprobamos las imagenes añadidas, las procesamos y las añadimos
        List<byte[]> imageBytesList = new ArrayList<>();
        if (!isDefaultImage(event_imageButton1)) {
            Drawable drawable = event_imageButton1.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            event_image1 = bitmapToPng(bitmap);
            imageBytesList.add(event_image1);
        }

        if (!isDefaultImage(event_imageButton2)) {
            Drawable drawable = event_imageButton2.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            event_image2 = bitmapToPng(bitmap);
            imageBytesList.add(event_image2);
        }

        if (!isDefaultImage(event_imageButton3)) {
            Drawable drawable = event_imageButton3.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            event_image3 = bitmapToPng(bitmap);
            imageBytesList.add(event_image3);
        }

        // Convertimos la lista de imagenes a array de bytes para pasarlos al objeto evento
        byte[][] imageBytes = new byte[imageBytesList.size()][];
        for (int i = 0; i < imageBytesList.size(); i++) {
            imageBytes[i] = imageBytesList.get(i);
        }

        // Obtenemos el valor de los comensales comprobando si es rango o no
        String eventServingsStr;
        if (isRange) {
            eventServingsStr = servings + " - " + (servings+1);
        } else {
            eventServingsStr = Integer.toString(servings);
        }

        // Manejamos la tarea de subir las imagenes primero
        Task<String> uploadTask = uploadImages(imageBytes);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si se han subido las imagenes correctamente, obtenemos su referencia, la añadimos a la evento y la subimos
                List<String> imageUrlList = Arrays.asList(task.getResult().split(","));

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String currentUserId = user.getUid();

                // Creamos una instancia de la clase event
                Events event = new Events(eventTitle, eventIngredients, eventElaboration, 1, eventServingsStr, tveventTime.getText().toString(), selectedeventType, selectedTags, imageUrlList, currentUserId, user.getDisplayName(), 0.0f, 0, new Date(), !switch_public_private.isChecked());

                // Subimos la evento a Firebase Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference eventsRef = db.collection("events");
                eventsRef.add(event).addOnSuccessListener(documentReference -> Toast.makeText(this, getString(R.string.new_event_created), Toast.LENGTH_SHORT).show())//documentReference.getId() para obtener el ID del objeto subido
                        .addOnFailureListener(e -> Toast.makeText(this, getString(R.string.new_event_error), Toast.LENGTH_SHORT).show());
                progressDialog.dismiss();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.new_event_image_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to upload images: ", task.getException());
            }
        });
    }

    private Task<String> uploadImages(byte[][] imageBytes) {
        int numImages = imageBytes.length;
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserId = user.getUid();

        // Arrays para almacenar la información de cada imagen
        String[] imageNames = new String[numImages];
        StorageReference[] imagesRefs = new StorageReference[numImages];
        Task<Uri>[] urlTasks = new Task[numImages];

        for (int i = 0; i < numImages; i++) {
            // Generar un nombre único para cada imagen
            String imageuid = UUID.randomUUID().toString();
            imageNames[i] = imageuid + ".png";

            // Subir la imagen a Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            imagesRefs[i] = storageRef.child("documents/users/" + currentUserId + "/events_images/" + imageNames[i]);
            UploadTask uploadTask = imagesRefs[i].putBytes(imageBytes[i]);

            int finalI = i;
            urlTasks[i] = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imagesRefs[finalI].getDownloadUrl();
            });
        }

        // Combinar todas las tareas en una sola tarea
        Tasks.whenAllComplete(urlTasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StringBuilder imageUrlBuilder = new StringBuilder();
                for (Task<Uri> urlTask : urlTasks) {
                    if (urlTask.isSuccessful()) {
                        Uri downloadUri = urlTask.getResult();
                        if (downloadUri != null) {
                            imageUrlBuilder.append(downloadUri.toString());
                            imageUrlBuilder.append(",");
                        }
                    } else {
                        taskCompletionSource.setException(urlTask.getException());
                        return;
                    }
                }
                // Devolver una cadena de texto con todas las URLs de las imágenes
                String imageUrlString = imageUrlBuilder.toString();
                if (imageUrlString.length() > 0) {
                    imageUrlString = imageUrlString.substring(0, imageUrlString.length() - 1);
                }
                taskCompletionSource.setResult(imageUrlString);
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }

    // Convertir imaganes a png para manejarlas todas mejor
    private byte[] bitmapToPng(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}