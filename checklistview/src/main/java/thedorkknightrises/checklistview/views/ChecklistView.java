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
import thedorkknightrises.checklistview.interfaces.OnChecklistEventListener;

/**
 * Created by Samriddha on 23-07-2017.
 */

public class ChecklistView extends LinearLayout implements OnChecklistEventListener {
    Context context;
    DragLinearLayout parent;
    boolean moveCheckedToBottom;
    Drawable itemBackground;

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

    public void setMoveCheckedToBottom(boolean moveCheckedToBottom) {
        this.moveCheckedToBottom = moveCheckedToBottom;
    }

    @Override
    public void onChecklistItemChecked(ChecklistItem item, boolean checked) {
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
            addItem(false, true);
        } else {
            parent.getChildAt(parent.getChildCount() - 1).requestFocus();
        }
    }

    @Override
    public void onLostFocus(ChecklistItem item) {
        item.delete.setVisibility(GONE);
        if (item.isEmpty()) {
            if (parent.getChildAt(parent.getChildCount() - 1).equals(item)) {
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
            if (!((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)).isEmpty()) {
                addItem(false, false);
            }
        }
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
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ChecklistItem) {
                arrayList.add(new ChecklistData(((ChecklistItem) child).isChecked(), ((ChecklistItem) child).getText().toString()));
            }
        }
        return arrayList;
    }

    public void setChecklistData(ArrayList<ChecklistData> checklistDatas) {
        boolean adjustLastItem = ((ChecklistItem) parent.getChildAt(parent.getChildCount() - 1)).add.getVisibility() == VISIBLE;
        if (adjustLastItem)
            parent.removeView(parent.getChildAt(parent.getChildCount() - 1));
        for (ChecklistData data : checklistDatas) {
            addItem(data.getText(), data.isChecked(), true, false);
        }
        if (adjustLastItem)
            addItem(false, false);
    }

}
