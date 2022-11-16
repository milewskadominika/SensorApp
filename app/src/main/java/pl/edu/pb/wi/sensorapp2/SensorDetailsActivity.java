package pl.edu.pb.wi.sensorapp2;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_SENSOR_TYPE_PARAMETER = "EXTRA_SENSOR_TYPE";

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorNameTextView = findViewById(R.id.details_sensor_name);

        int sensorType = getIntent().getIntExtra(EXTRA_SENSOR_TYPE_PARAMETER, Sensor.TYPE_LIGHT);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(sensorType);

        if (sensorLight == null) {
            sensorNameTextView.setText(R.string.missing_sensor);
        } else {
            sensorNameTextView.setText(sensorLight.getName());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensorLight != null) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        float value = event.values[0];
        String str = getString(R.string.sensor_name, value);

        switch (type) {
            case Sensor.TYPE_LIGHT:
                sensorNameTextView.setText("Light sensor: " + str);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorNameTextView.setText("Linear acceleration: " + str);
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}