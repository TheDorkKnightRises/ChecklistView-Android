package thedorkknightrises.checklistview;

/**
 * Created by Samriddha on 06-08-2017.
 */

public class ChecklistData {
    private boolean checked;
    private String text;

    public ChecklistData(boolean checked, String text) {
        this.checked = checked;
        this.text = text;
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
}
