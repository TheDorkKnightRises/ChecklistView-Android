package thedorkknightrises.checklistview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.ArrayList;

import thedorkknightrises.checklistview.ChecklistData;
import thedorkknightrises.checklistview.R;
import thedorkknightrises.checklistview.interfaces.OnChecklistInteractionListener;
import thedorkknightrises.checklistview.interfaces.OnChecklistItemEventListener;

/**
 * Created by Samriddha on 23-07-2017.
 */

public class ChecklistView extends LinearLayout implements OnChecklistItemEventListener {
    Context context;
    DragLinearLayout parent;
    boolean moveCheckedToBottom;
    Drawable itemBackground;
    OnChecklistInteractionListener listener;
    boolean editable = true;

    public ChecklistView(Context context) {
        super(context);
        init(context);
    }

    public ChecklistView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChecklistView);
        itemBackground = array.getDrawable(R.styleable.ChecklistView_checkListItemBackground);
        array.recycle();
        init(context);
    }

    public DragLinearLayout getDragLinearLayout() {
        return parent;
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_checklist, this, true);

        parent = findViewById(R.id.draggable_rootview);
        addItem(false, true);
    }

    public void addListener(OnChecklistInteractionListener listener) {
        this.listener = listener;
    }

    public void addItem(boolean draggable, boolean hasFocus) {
        ChecklistItem newItem = new ChecklistItem(context);
        if (itemBackground != null) newItem.setBackground(itemBackground);
        newItem.addListener(this);
        if (draggable) {
            parent.addDragView(newItem, newItem.dragHandle);
        } else {
            parent.addView(newItem);
        }
        if (hasFocus) newItem.requestFocus();
    }

    public void addItem(String text, boolean checked, boolean draggable, boolean hasFocus) {
        ChecklistItem newItem = new ChecklistItem(context, text, checked);
        if (itemBackground != null) newItem.setBackground(itemBackground);
        newItem.addListener(this);
        if (draggable) {
            parent.addDragView(newItem, newItem.dragHandle);
        } else {
            parent.addView(newItem);
        }
        if (hasFocus) newItem.requestFocus();
    }

    public void clear() {
        parent.removeAllViews();
    }

    public void setMoveCheckedToBottom(boolean moveCheckedToBottom) {
        this.moveCheckedToBottom = moveCheckedToBottom;
    }

    @Override
    public void onChecklistItemChecked(ChecklistItem item, boolean checked) {
        if (listener != null) listener.onChecklistItemChanged(item);
        if (moveCheckedToBottom) {
            parent.removeView(item);
            parent.addDragView(item, item.dragHandle);
        }
    }

    @Override
    public void onChecklistItemRemoved(ChecklistItem item) {
        parent.requestFocus();
        parent.removeDragView(item);
    }

    @Override
    public void onEnterPressed(ChecklistItem item) {
        parent.setViewDraggable(item, item.dragHandle);
        if (!((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)).isEmpty()) {
            if (((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)).add.getVisibility() == VISIBLE) {
                ((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)).listener.onLostFocus(((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)));
            }
        }
        parent.getChildAt(parent.getChildCount() - 1).requestFocus();
    }

    @Override
    public void onLostFocus(ChecklistItem item) {
        item.delete.setVisibility(GONE);
        if (item.isEmpty()) {
            if ((parent.getChildCount() != 0) && parent.getChildAt(parent.getChildCount() - 1).equals(item)) {
                item.checkbox.setVisibility(GONE);
                item.add.setVisibility(VISIBLE);
            } else {
                item.delete.setVisibility(VISIBLE);
            }
        } else {
            item.checkbox.setVisibility(VISIBLE);
            item.add.setVisibility(GONE);
            parent.setViewDraggable(item, item.dragHandle);
            item.dragHandle.setVisibility(VISIBLE);
            ChecklistItem i = (ChecklistItem) parent.getChildAt(parent.getChildCount() - 1);
            if (i != null && i.add.getVisibility() != VISIBLE) {
                addItem(false, false);
            }
        }
    }

    @Override
    public void onEdited(ChecklistItem item) {
        if (listener != null) listener.onChecklistItemChanged(item);
    }

    public void setContainerScrollView(ScrollView scrollView) {
        parent.setContainerScrollView(scrollView);
    }

    public void setChecklistItemBackground(Drawable drawable) {
        itemBackground = drawable;
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) instanceof ChecklistItem) {
                parent.getChildAt(i).setBackground(drawable);
            }
        }
    }

    public ArrayList<ChecklistData> getChecklistData() {
        ArrayList<ChecklistData> arrayList = new ArrayList<>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ChecklistItem && !((ChecklistItem) child).getText().toString().trim().equals("")) {
                arrayList.add(new ChecklistData(((ChecklistItem) child).isChecked(), ((ChecklistItem) child).getText().toString()));
            }
        }
        return arrayList;
    }

    public void setChecklistData(ArrayList<ChecklistData> checklistDatas) {
        clear();
        for (ChecklistData data : checklistDatas) {
            addItem(data.getText(), data.isChecked(), true, false);
        }
        addItem(false, false);
        setEditable(editable);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ChecklistItem) {
                if ("".equals(((ChecklistItem) child).editText.getText().toString().trim())) {
                    child.setVisibility(editable ? VISIBLE : GONE);
                }
                ChecklistItem item = (ChecklistItem) child;
                item.editText.setFocusable(editable);
                item.editText.setFocusableInTouchMode(editable);
                item.editText.setClickable(editable);
                item.editText.setLongClickable(editable);
                item.checkbox.setEnabled(editable);
                item.dragHandle.setVisibility(editable ? VISIBLE : GONE);
            }
        }
    }

}
