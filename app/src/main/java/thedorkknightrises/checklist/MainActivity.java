package thedorkknightrises.checklist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import thedorkknightrises.checklistview.ChecklistData;
import thedorkknightrises.checklistview.interfaces.OnChecklistInteractionListener;
import thedorkknightrises.checklistview.views.ChecklistItem;
import thedorkknightrises.checklistview.views.ChecklistView;

public class MainActivity extends AppCompatActivity implements OnChecklistInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ChecklistView checklistView = findViewById(R.id.checklistview);
        checklistView.addListener(this);
        checklistView.setContainerScrollView((ScrollView) findViewById(R.id.scrollView));

        final TextView textView = findViewById(R.id.text);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                ArrayList<ChecklistData> list = checklistView.getChecklistData();
                for (ChecklistData i : list) {
                    textView.append(i.getText() + " (Checked: " + i.isChecked() + ")\n");
                }
            }
        });

        ArrayList<ChecklistData> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new ChecklistData(false, "Item " + i));
        }
        checklistView.setChecklistData(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Toast.makeText(this, "Sample app for ChecklistView library by Samriddha Basu (TheDorkKnightRises)", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChecklistItemChanged(ChecklistItem item) {
        Log.d(getLocalClassName(), "Edited item\nText: " + item.getText() + "\nChecked: " + item.isChecked());
    }
}
