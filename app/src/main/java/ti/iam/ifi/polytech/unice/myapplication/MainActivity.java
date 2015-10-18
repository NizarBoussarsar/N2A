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

    //ImageView image1, image2, image3, image4;

    //ListView lv;
    //Context context;

    public static int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

    //private SimpleCursorAdapter adapter;
    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private float old, init;
    private boolean first = true;

    ListView lv;
    int scrollHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //context = this;
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CustomAdapter(this, images));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, acceleroMeter, SensorManager.SENSOR_DELAY_UI);

        scrollHeight = lv.getHeight();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acceleroMeter, SensorManager.SENSOR_DELAY_UI);
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

        float yDelta = event.values[1];

        if (first) {
            old = init = yDelta;
            first = false;
        }

        Log.w("init", String.valueOf(init));
        Log.w("old", String.valueOf(old));
        Log.w("yDelta", String.valueOf(yDelta));

        int threshold = 2;

        if (yDelta > old + threshold) {
            Log.d("UP", "Detected change in pitch up...");
            old = yDelta;
            //lv.smoothScrollBy(-scrollHeight, 0);
        } else if (yDelta < old - threshold) {
            Log.d("DOWN", "Detected change in pitch down...");
            old = yDelta;
            //+lv.smoothScrollBy(scrollHeight, 0);
        } else if (yDelta < init + threshold && yDelta > init - threshold) {
            Log.d("SAME", "No change...");
        } else {
            Log.d("ELSE", "ELSE...");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}