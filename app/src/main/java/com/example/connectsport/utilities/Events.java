package com.example.connectsport.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class Events implements Parcelable {

    private String eventsTitle;
    private String eventsIngredients;
    private String eventsElaboration;
    private int eventsAttend;
    private String tvEventsServings;
    private String tvEventsTime;
    private String selectedEventsType;
    private List<String> tags;
    private List<String> images;
    private String creatorUid;
    private String creatorUsername;
    private float rating;
    private int voteCounter;
    private Date createdAt;
    private Boolean isPublished;
    private DocumentReference Ref;

    public Events() {
        // Inicializa cualquier campo necesario si es necesario
    }
    public Events(String eventsTitle, String eventsIngredients, String eventsElaboration, int eventsAttend,String tvEventsServings,
                  String tvEventsTime, String selectedEventsType, List<String> tags, List<String> images, String creatorUid,
                  String creatorUsername, float rating, int voteCounter, Date createdAt, Boolean isPublished) {

        this.eventsTitle = eventsTitle;
        this.eventsIngredients = eventsIngredients;
        this.eventsElaboration = eventsElaboration;
        this.eventsAttend = eventsAttend;
        this.tvEventsServings = tvEventsServings;
        this.tvEventsTime = tvEventsTime;
        this.selectedEventsType = selectedEventsType;
        this.tags = tags;
        this.images = images;
        this.creatorUid = creatorUid;
        this.creatorUsername = creatorUsername;
        this.rating = rating;
        this.createdAt = createdAt;
        this.isPublished = isPublished;
        this.voteCounter = voteCounter;
    }

    protected Events(Parcel in) {
        eventsTitle = in.readString();
        eventsIngredients = in.readString();
        eventsElaboration = in.readString();
        eventsAttend = in.readInt();
        tvEventsServings = in.readString();
        tvEventsTime = in.readString();
        selectedEventsType = in.readString();
        tags = in.createStringArrayList();
        images = in.createStringArrayList();
        creatorUid = in.readString();
        creatorUsername = in.readString();
        rating = in.readFloat();
        voteCounter = in.readInt();
        createdAt = new Date(in.readLong());
        isPublished = in.readByte() != 0;
        String refPath = in.readString();
        if (refPath != null) {
            Ref = FirebaseFirestore.getInstance().document(refPath);
        }
    }

    // Definimos métodos para implementar Parcelable en la clase (para poder pasarlo como objeto entre actividades)
    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventsTitle);
        dest.writeString(eventsIngredients);
        dest.writeString(eventsElaboration);
        dest.writeInt(eventsAttend);
        dest.writeString(tvEventsServings);
        dest.writeString(tvEventsTime);
        dest.writeString(selectedEventsType);
        dest.writeStringList(tags);
        dest.writeStringList(images);
        dest.writeString(creatorUid);
        dest.writeString(creatorUsername);
        dest.writeFloat(rating);
        dest.writeInt(voteCounter);
        dest.writeLong(createdAt.getTime());
        dest.writeByte((byte) (isPublished ? 1 : 0));
        if (Ref != null) {
            dest.writeString(Ref.getPath());
        } else {
            dest.writeString(null);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getEventsTitle() {
        return eventsTitle;
    }

    public String getEventsIngredients() {
        return eventsIngredients;
    }

    public String getEventsElaboration() {
        return eventsElaboration;
    }

    public int getEventsAttend() {
        return eventsAttend;
    }

    public String getTvEventsServings() {
        return tvEventsServings;
    }

    public String getTvEventsTime() {
        return tvEventsTime;
    }

    public String getSelectedEventsType() {
        return selectedEventsType;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public float getRating() {
        return rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setRef(DocumentReference documentReference) {
        this.Ref = documentReference;
    }

    public DocumentReference getRef() {
        return Ref;
    }

    public int getVoteCounter() {
        return voteCounter;
    }

}
