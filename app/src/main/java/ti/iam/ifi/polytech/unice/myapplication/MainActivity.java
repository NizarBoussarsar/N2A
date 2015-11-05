package ti.iam.ifi.polytech.unice.myapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    Integer[] images = {
            R.drawable.image1,
            R.drawable.image5,
            R.drawable.image2,
            R.drawable.image6,
            R.drawable.image3,
            R.drawable.image7,
            R.drawable.image4,
            R.drawable.image8,
            R.drawable.image1,
            R.drawable.image5,
            R.drawable.image2,
            R.drawable.image6,
            R.drawable.image3,
            R.drawable.image7,
            R.drawable.image4,
            R.drawable.image8
    };

    Integer[] imagesIds;

    private ListView listView;
    private TextView textView;
    private Button button;

    private SensorManager sensorManager;
    private Sensor sensor;

    private int initScrollPos = 0;
    private float initialY, yThreshold = 2;
    private boolean isfirstRun = true, isReternedToOriginalPosition = true;
    private CustomAdapter adapter;

    private List<Integer> correctImages;
    View.OnClickListener buttonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (adapter.selectedImages.retainAll(correctImages)) {
                Toast.makeText(getApplicationContext(), "TRUE", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "FALSE", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if (isFirstTime()) {
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(new ActionViewTarget(this, ActionViewTarget.Type.HOME))
                    .setContentTitle("Instructions")
                    .setContentText("This is highlighting the Home button")
                    .hideOnTouchOutside()
                    .build();
        }
        */

        imagesIds = new Integer[images.length];

        correctImages = new ArrayList<>();
        correctImages.add(images[0]);
        correctImages.add(images[images.length - 1]);

        for (int i = 0; i < images.length; i++) {
            imagesIds[i] = i;
        }

        adapter = new CustomAdapter(this, imagesIds, images);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.invalidate();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(buttonHandler);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }
    */
    
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }


    /*
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        float difference, last_y = event.values[1];
        long curTime = System.currentTimeMillis();

        if (isfirstRun) {
            initialY = last_y;
            isfirstRun = false;
        }

        difference = Math.abs(initialY - last_y);

        Log.e("", "Initial position : " + initialY + ". Difference : " + difference);

        if (difference > yThreshold) {
            if (isReternedToOriginalPosition) {
                if (initialY - last_y > 0) {
                    /**
                     * @param direction Negative to check scrolling up, positive to check
                     *            scrolling down.
                     */
                    if (listView.canScrollList(1)) {
                        //Log.e("", "Scrolling down " + (listView.getLastVisiblePosition() - initScrollPos) + "  elements");
                        //initScrollPos += listView.getLastVisiblePosition();
                        initScrollPos += 3;
                        Log.e("", "Scrolling down 3 elements");
                    }
                } else {
                    if (listView.canScrollList(-1)) {
                        //Log.e("", "Scrolling up " + (listView.getLastVisiblePosition() - initScrollPos) + "  elements");
                        //initScrollPos -= listView.getFirstVisiblePosition();
                        initScrollPos -= 3;
                        Log.e("", "Scrolling up 3 elements");
                    }
                }
                listView.smoothScrollToPositionFromTop(initScrollPos, 0, 100);
                Log.e("", "initScrollPos " + initScrollPos);
            }
            isReternedToOriginalPosition = false;
        } else {
            Log.e("", "Not scrolling");
            isReternedToOriginalPosition = true;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}