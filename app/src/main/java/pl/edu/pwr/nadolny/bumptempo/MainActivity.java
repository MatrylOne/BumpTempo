package pl.edu.pwr.nadolny.bumptempo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.pwr.nadolny.bumptempo.TempoBundle.BpmCalculator;

public class MainActivity extends AppCompatActivity implements SensorEventListener{


    private final static int  SENSOR_UPDATE_TIME = 100;
    private static final int SHAKE_THRESHOLD = 2000;

    private BpmCalculator bpmCalculator;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private TextView bpmText;
    private Button tapButton;

    private long lastUpdate = 0;
    private float[] lastPosition = new float[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bpmText = (TextView) findViewById(R.id.bpmText);
        tapButton = (Button) findViewById(R.id.tapButton);
        bpmCalculator = new BpmCalculator();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        tapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onTapButtonClick();
            }
        });
    }

    private void onTapButtonClick(){
        bpmCalculator.addPoint();
        if(bpmCalculator.getNumOfPoints() > 5){
            bpmText.setText("" + bpmCalculator.getBpm());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if ((curTime - lastUpdate) > SENSOR_UPDATE_TIME) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - lastPosition[0] - lastPosition[1] - lastPosition[2])/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    onTapButtonClick();
                    System.out.println(speed);
                }

                lastPosition[0] = x;
                lastPosition[1] = y;
                lastPosition[2] = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
