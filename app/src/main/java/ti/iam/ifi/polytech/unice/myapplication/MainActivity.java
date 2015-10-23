package ti.iam.ifi.polytech.unice.myapplication;

import android.app.ListActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static int[] images = {
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

    private ListView listView;

    private SensorManager sensorManager;
    private Sensor sensor;

    private int initScrollPos = 0;
    private float initialY, yThreshold = 2;
    private boolean isfirstRun = true, isReternedToOriginalPosition = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(this, images));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

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
                        initScrollPos += 3;
                        Log.e("", "Scrolling down 3 elements");
                    }
                } else {
                    if (listView.canScrollList(-1)) {
                        initScrollPos -= 3;
                        Log.e("", "Scrolling up 3 elements");
                    }
                }
                listView.smoothScrollToPositionFromTop(initScrollPos, 0, 100);
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