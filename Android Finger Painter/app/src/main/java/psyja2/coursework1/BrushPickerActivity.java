package psyja2.coursework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

public class BrushPickerActivity extends AppCompatActivity {

    private BrushPreviewView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_picker);

        // Setup the preview View
        preview = new BrushPreviewView(this);
        preview.size = 50;
        preview.style = Paint.Cap.ROUND;
        ((FrameLayout) findViewById(R.id.brush_preview)).addView(preview);

        // Setup listener for when the brush size seekbar is adjusted
        final SeekBar sizeSelector = (SeekBar) findViewById(R.id.brush_size);
        sizeSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                // Update the brush preview
                preview.size = i;
                preview.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Setup listener for when the brush type drop down is selected
        final Spinner typeSelector = (Spinner) findViewById(R.id.brush_style);
        typeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Update the brush preview
                preview.style = getCapFromName(typeSelector.getItemAtPosition(i).toString());
                preview.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Setup listener for when the save button is pressed
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Create the bundle to return
                Bundle bundle = new Bundle();
                bundle.putInt("brushSize", sizeSelector.getProgress());
                bundle.putSerializable("brushStyle", getCapFromName(typeSelector.getSelectedItem().toString()));

                // Add the bundle to the result and finish the activity
                Intent result = new Intent();
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        // Set the properties to the values passed in the bundle
        Bundle bundle = getIntent().getExtras();
        sizeSelector.setProgress(bundle.getInt("brushSize"));
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) typeSelector.getAdapter();
        typeSelector.setSelection(adapter.getPosition(getCapNameFromCap((Paint.Cap) bundle.getSerializable("brushStyle"))));
    }

    /*
    Convert a string representation of a brush type to the Paint.Cap type
     */
    private Paint.Cap getCapFromName(String name)
    {
        if(name.equalsIgnoreCase("Round"))
            return Paint.Cap.ROUND;
        if(name.equalsIgnoreCase("Square"))
            return Paint.Cap.SQUARE;
        return Paint.Cap.SQUARE;
    }

    /*
    Convert a Paint.Cap type to a brush type name string
     */
    private String getCapNameFromCap(Paint.Cap cap)
    {
        if(cap == Paint.Cap.ROUND)
            return "Round";
        else if(cap == Paint.Cap.SQUARE)
            return "Square";
        return "Square";
    }

    /*
    A simple view that will paint a preview of the brush size/shape in the centre
     */
    class BrushPreviewView extends View {

        public int size;
        public Paint.Cap style;

        public BrushPreviewView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // clear the canvas and make it transparent
            canvas.drawColor(Color.TRANSPARENT);

            // Draw using the same paint brush code used in the FingerPainterView
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(size);
            paint.setStrokeCap(style);
            paint.setARGB(255, 0, 0, 0);

            // Use a single point path
            Path path = new Path();
            path.moveTo(this.getWidth() - (this.getWidth() / 2) + (size / 4), this.getHeight() - (this.getHeight() / 2) + (size / 4));
            path.close();
            canvas.drawPath(path, paint);
        }
    }
}
