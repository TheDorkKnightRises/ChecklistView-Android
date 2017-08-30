package thedorkknightrises.checklistview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samriddha on 06-08-2017.
 */

public class ChecklistData implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChecklistData> CREATOR = new Parcelable.Creator<ChecklistData>() {
        @Override
        public ChecklistData createFromParcel(Parcel in) {
            return new ChecklistData(in);
        }

        @Override
        public ChecklistData[] newArray(int size) {
            return new ChecklistData[size];
        }
    };
    private boolean checked;
    private String text;

    public ChecklistData(boolean checked, String text) {
        this.checked = checked;
        this.text = text;
    }

    protected ChecklistData(Parcel in) {
        checked = in.readByte() != 0x00;
        text = in.readString();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (checked ? 0x01 : 0x00));
        dest.writeString(text);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChecklistData && ((ChecklistData) obj).getText().equals(text) && ((ChecklistData) obj).isChecked() == checked);
    }

    @Override
    public String toString() {
        return text + " (" + checked + ")";
    }
}
