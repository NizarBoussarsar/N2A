package ti.iam.ifi.polytech.unice.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
            //Il y a toujours un probléme avec ça ..
            //http://stackoverflow.com/questions/29862577/how-to-check-whether-a-list-is-a-subset-of-another-list
            //retainAll() n'est pas la bonne méthode
            if (adapter.selectedImages.retainAll(correctImages)) {
                Toast.makeText(getApplicationContext(), "Good Job !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Try again please ..", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BEGIN Alert - Instructions
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        String instructions = "The goal of this game is to scoll over the pictures using the accelerometer.\n" +
                "In order to achieve the goal, you have to select all the pictures that represents TUNISIA.\n" +
                "Good luck with it !";
        builder.setMessage(instructions);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        // END Alert - Instructions

        // BEGIN Choice of correct images
        // J'ai juste fait un test : premiére et derniére
        correctImages = new ArrayList<>();
        correctImages.add(images[0]);
        correctImages.add(images[images.length - 1]);
        // END Choice of correct images

        // BEGIN Créations de ids correspendant aux images pour les passer au listView
        imagesIds = new Integer[images.length];
        for (int i = 0; i < images.length; i++) {
            imagesIds[i] = i;
        }
        // END Créations de ids correspendant aux images pour les passer au listView

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

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
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

        if (difference > yThreshold) {
            if (isReternedToOriginalPosition) {
                if (initialY - last_y > 0) {
                    /**
                     * @param direction Negative to check scrolling up, positive to check
                     *            scrolling down.
                     */
                    if (listView.canScrollList(1)) {
                        //initScrollPos += listView.getLastVisiblePosition();
                        initScrollPos += 3;
                    }
                } else {
                    if (listView.canScrollList(-1)) {
                        //initScrollPos -= listView.getFirstVisiblePosition();
                        initScrollPos -= 3;
                    }
                }
                listView.smoothScrollToPositionFromTop(initScrollPos, 0, 100);
            }
            isReternedToOriginalPosition = false;
        } else {
            isReternedToOriginalPosition = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}