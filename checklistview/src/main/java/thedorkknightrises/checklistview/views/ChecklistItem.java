package thedorkknightrises.checklistview.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import thedorkknightrises.checklistview.R;
import thedorkknightrises.checklistview.interfaces.OnChecklistItemEventListener;

/**
 * Created by Samriddha on 23-07-2017.
 */

public class ChecklistItem extends LinearLayout {
    Context context;
    LinearLayout rootView;
    CheckBox checkbox;
    ImageButton add;
    EditText editText;
    ImageButton delete;
    ImageView dragHandle;
    OnChecklistItemEventListener listener;

    public ChecklistItem(Context context) {
        super(context);
        init(context);
    }

    public ChecklistItem(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        init(context);
    }

    public ChecklistItem(Context context, String text, boolean checked) {
        super(context);
        init(context);
        editText.setText(text);
        checkbox.setChecked(checked);
        dragHandle.setVisibility(VISIBLE);
        add.setVisibility(GONE);
        checkbox.setVisibility(VISIBLE);
    }

    public void init(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.view_checklist_item, this, true);

        rootView = view.findViewById(R.id.rootview);
        checkbox = view.findViewById(R.id.checklist_item_checkbox);
        add = view.findViewById(R.id.checklist_item_add_icon);
        editText = view.findViewById(R.id.checklist_item_edittext);
        delete = view.findViewById(R.id.checklist_item_delete);
        dragHandle = view.findViewById(R.id.checklist_item_drag_handle);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null)
                    listener.onChecklistItemChecked(ChecklistItem.this, isChecked);
            }
        });

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEnterPressed(ChecklistItem.this);
            }
        });

        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dragHandle.setVisibility(GONE);
                    if (add.getVisibility() != VISIBLE) {
                        delete.setVisibility(VISIBLE);
                    }
                    editText.setSelection(editText.getText().length());
                } else {
                    if (listener != null) listener.onLostFocus(ChecklistItem.this);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listener != null) listener.onEdited(ChecklistItem.this);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (listener != null) listener.onEnterPressed(ChecklistItem.this);
                    return true;
                } else {
                    return false;
                }
            }
        });

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem();
            }
        });
    }

    public void removeItem() {
        if (listener != null) listener.onChecklistItemRemoved(ChecklistItem.this);
    }

    public void addListener(OnChecklistItemEventListener listener) {
        this.listener = listener;
    }

    public Editable getText() {
        return editText.getText();
    }

    public boolean isEmpty() {
        return (getText().toString().trim().length() == 0);
    }

    public boolean isChecked() {
        return checkbox.isChecked();
    }

    @Override
    public void setBackground(Drawable background) {
        rootView.setBackground(background);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return editText.requestFocus(direction, previouslyFocusedRect);
    }
}
