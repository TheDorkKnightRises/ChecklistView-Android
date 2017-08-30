package thedorkknightrises.checklistview.interfaces;

import thedorkknightrises.checklistview.views.ChecklistItem;

/**
 * Created by Samriddha on 23-07-2017.
 */

public interface OnChecklistItemEventListener {

    void onChecklistItemChecked(ChecklistItem item, boolean checked);

    void onChecklistItemRemoved(ChecklistItem item);

    void onEnterPressed(ChecklistItem item);

    void onLostFocus(ChecklistItem item);

    void onEdited(ChecklistItem item);

}
