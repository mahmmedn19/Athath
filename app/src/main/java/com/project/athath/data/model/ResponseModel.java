package com.project.athath.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ResponseModel {
    private List<DetectedObject> objects;

    public List<DetectedObject> getObjects() {
        return objects;
    }

    public void setObjects(List<DetectedObject> objects) {
        this.objects = objects;
    }

    // ✅ Make DetectedObject Parcelable
    public static class DetectedObject implements Parcelable {
        private String image;  // Base64 Image
        private String label;  // Object Label (e.g., "Chair")

        public DetectedObject(String image, String label) {
            this.image = image;
            this.label = label;
        }

        protected DetectedObject(Parcel in) {
            image = in.readString();
            label = in.readString();
        }

        public static final Creator<DetectedObject> CREATOR = new Creator<DetectedObject>() {
            @Override
            public DetectedObject createFromParcel(Parcel in) {
                return new DetectedObject(in);
            }

            @Override
            public DetectedObject[] newArray(int size) {
                return new DetectedObject[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(image);
            parcel.writeString(label);
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
