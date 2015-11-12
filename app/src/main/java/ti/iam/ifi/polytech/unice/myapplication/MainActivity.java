package ti.iam.ifi.polytech.unice.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    public AlertDialog.Builder builder;
    Integer[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9,
            R.drawable.image10,
            R.drawable.image11,
            R.drawable.image12,
            R.drawable.image13,
            R.drawable.image14,
            R.drawable.image15,
            R.drawable.image16,
            R.drawable.image17,
            R.drawable.image18,
            R.drawable.image19,
            R.drawable.image20,
            R.drawable.image21,
            R.drawable.image22,
            R.drawable.image23
    };
    Integer[] imagesIds;
    private ListView listView;
    private Button button;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int initScrollPos = 0;
    private float initialY, yThreshold = 2;
    private boolean isfirstRun = true, isReternedToOriginalPosition = true;
    private long startTime, spentTime;
    private CustomAdapter adapter;
    private List<Integer> correctImages;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BEGIN Alert - Instructions
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        String instructions = "The goal of this game is to identify the pictures you think that represents Tunisia.\n\n" +
                "You can navigate either by normal scroll or accelerometer inclination.\n\n" +
                "You have 90 seconds to select the 5 correct pictures ;).\n\n" +
                "Have fun and good luck with it !";
        builder.setMessage(instructions);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        // END Alert - Instructions

        // BEGIN Créations de ids correspendant aux images pour les passer au listView
        imagesIds = new Integer[images.length];
        for (int i = 0; i < images.length; i++) {
            imagesIds[i] = i;
        }
        // END Créations de ids correspendant aux images pour les passer au listView

        // BEGIN Choice of correct images
        // J'ai juste fait un test : premiére et derniére
        correctImages = new ArrayList<Integer>();
        correctImages.add(imagesIds[3]);
        correctImages.add(imagesIds[7]);
        correctImages.add(imagesIds[12]);
        correctImages.add(imagesIds[19]);
        correctImages.add(imagesIds[21]);
        // END Choice of correct images

        adapter = new CustomAdapter(this, imagesIds, images);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.invalidate();

        startTime = System.currentTimeMillis();

        if (System.currentTimeMillis() - startTime > 10000) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        button = (Button) findViewById(R.id.button);
        button.setText("Done");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inside = (correctImages.size() == adapter.selectedImages.size());
                if (inside) for (Integer i : correctImages) {
                    inside = inside && adapter.selectedImages.contains(i);
                    if (!inside) break;
                }
                if (inside) {
                    spentTime = System.currentTimeMillis() - startTime;
                    int seconds = (int) (spentTime / 1000) % 60;
                    //Toast.makeText(getApplicationContext(), "Good Job ! seconds : " + seconds, Toast.LENGTH_SHORT).show();
                    // set title
                    builder.setTitle("Congratulations !");

                    builder
                            .setMessage("Very Good ! You spent " + seconds + " secondes to identify the correct pictures :).\n")
                            .setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    System.exit(0);
                                }
                            });

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Try again please ..", Toast.LENGTH_LONG).show();
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        countDownTimer = new MyCountDownTimer(90000, 1000);
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
        }
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

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            // set title
            builder.setTitle("Oups !");

            // set dialog message
            builder
                    .setMessage("You have no more time !\n Would you like to try again ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = builder.create();

            // show it
            alertDialog.show();
        }


        @Override
        public void onTick(long millisUntilFinished) {

        }
    }
}