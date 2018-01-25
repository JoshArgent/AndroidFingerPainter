package psyja2.coursework1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ColourPicker extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_picker);

        // Setup the list of colours
        final ListView colourList = (ListView) findViewById(R.id.colourList);
        List<String> itemList = new ArrayList<String>();
        itemList.add("Black");
        itemList.add("Red");
        itemList.add("Green");
        itemList.add("Blue");
        itemList.add("Yellow");
        itemList.add("White");
        itemList.add("Cyan");
        itemList.add("Grey");
        itemList.add("Pink");
        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        colourList.setAdapter(items);

        // Listen for when an item is selected
        colourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Bundle bundle = new Bundle();
                String colorName = colourList.getAdapter().getItem(i).toString();

                // Add the selected colour to the bundle
                bundle.putString("selectedColour", colorName);

                // Add the bundle to the result and finish the activity
                Intent result = new Intent();
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }




}
