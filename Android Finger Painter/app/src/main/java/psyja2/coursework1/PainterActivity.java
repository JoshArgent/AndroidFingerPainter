package psyja2.coursework1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PainterActivity extends AppCompatActivity {

    static final int COLOUR_PICKER_REQUEST_CODE = 1;
    static final int BRUSH_PICKER_REQUEST_CODE = 2;
    static final int IMAGE_BROWSE_REQUEST_CODE = 3;

    private MyFingerPainterView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);

        canvas = (MyFingerPainterView) findViewById(R.id.paintCanvas);

        // Check to see if the app was launched with an image Uri to load
        Uri uri = getIntent().getData();
        if(uri != null)
        {
            canvas.uri = uri;
        }

        // Colour select button
        Button colourButton = (Button) findViewById(R.id.colourButton);
        colourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PainterActivity.this, ColourPicker.class);
                startActivityForResult(intent, COLOUR_PICKER_REQUEST_CODE);
            }
        });

        // Brush select button
        Button brushButton = (Button) findViewById(R.id.brushButton);
        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Launc the brush picker activity and pass the current brush settings
                Intent intent = new Intent(PainterActivity.this, BrushPickerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("brushSize", canvas.getBrushWidth());
                bundle.putSerializable("brushStyle", canvas.getBrush());
                intent.putExtras(bundle);
                startActivityForResult(intent, BRUSH_PICKER_REQUEST_CODE);
            }
        });

        // Clear button
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the canvas
                canvas.clear();

                // Display a toast message
                Toast.makeText(PainterActivity.this, "Canvas cleared", Toast.LENGTH_SHORT).show();
            }
        });

        // Open button
        Button openButton = (Button) findViewById(R.id.openButton);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_BROWSE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle)
    {
        super.onRestoreInstanceState(bundle);

        // Restore canvas properties
        canvas.setColour(bundle.getInt("paintColour"));
        canvas.setBrush((Paint.Cap) bundle.getSerializable("paintBrush"));
        canvas.setBrushWidth(bundle.getInt("paintSize"));
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);

        // Save canvas properties
        bundle.putInt("paintColour", canvas.getColour());
        bundle.putSerializable("paintBrush", canvas.getBrush());
        bundle.putInt("paintSize", canvas.getBrushWidth());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == COLOUR_PICKER_REQUEST_CODE) // Colour picker activity has ended
        {
            if(resultCode == RESULT_OK)
            {
                // Convert the colour name to it's int value
                String colourName = data.getExtras().getString("selectedColour");
                int colourID = 0;
                if(colourName.equalsIgnoreCase("Black"))
                    colourID = Color.BLACK;
                else if(colourName.equalsIgnoreCase("Red"))
                    colourID = Color.RED;
                if(colourName.equalsIgnoreCase("Green"))
                    colourID = Color.GREEN;
                if(colourName.equalsIgnoreCase("Blue"))
                    colourID = Color.BLUE;
                if(colourName.equalsIgnoreCase("Yellow"))
                    colourID = Color.YELLOW;
                if(colourName.equalsIgnoreCase("White"))
                    colourID = Color.WHITE;
                if(colourName.equalsIgnoreCase("Cyan"))
                    colourID = Color.CYAN;
                if(colourName.equalsIgnoreCase("Grey"))
                    colourID = Color.GRAY;
                if(colourName.equalsIgnoreCase("Pink"))
                    colourID = Color.MAGENTA;

                // Set the canvas paint colour
                canvas.setColour(colourID);

                // Display a toast message
                Toast.makeText(this, colourName + " selected", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == BRUSH_PICKER_REQUEST_CODE) // Brush picker activity has ended
        {
            if(resultCode == RESULT_OK)
            {
                // Set the brush properties for the canvas
                int brushSize = data.getExtras().getInt("brushSize");
                Paint.Cap brushStyle = (Paint.Cap) data.getExtras().getSerializable("brushStyle");
                canvas.setBrushWidth(brushSize);
                canvas.setBrush(brushStyle);

                // Display a toast message
                Toast.makeText(this, "Brush set", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == IMAGE_BROWSE_REQUEST_CODE) // Image browser activity has ended
        {
            if(resultCode == RESULT_OK)
            {
                // Load the image
                canvas.load(data.getData());

                // Display a toast message
                Toast.makeText(this, "Image loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
