package pl.edu.pb.wi.sensorapp2;

import static pl.edu.pb.wi.sensorapp2.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private static final String SENSOR_TAG = "SensorActivity";
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private final List<Integer> favourSensors = Arrays.asList(Sensor.TYPE_LIGHT, Sensor.TYPE_LINEAR_ACCELERATION);
    public static final int SENSOR_DETAILS_ACTIVITY_REQUEST_CODE = 1;
    public static final int LOCATION_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_sensor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String string = getString(R.string.sensors_count, sensorList.size());
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(string);
        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensorList.toArray().length; i++) {
            Log.d(SENSOR_TAG, "Nazwa: " + sensorList.get(i).getName());
            Log.d(SENSOR_TAG, "Dostawca:" + sensorList.get(i).getVendor());
            Log.d(SENSOR_TAG, "Maksymalny zasięg: " + sensorList.get(i).getMaximumRange());
        }

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private final List<Sensor> sensorList;

        public SensorAdapter(List<Sensor> sensorList) {

            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SensorHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(final SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }

    }

    public class SensorHolder extends RecyclerView.ViewHolder{
        private final TextView sensorNameTextView;
        private final TextView sensorTypeTextView;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            sensorNameTextView = itemView.findViewById(R.id.sensor_name);
            sensorTypeTextView = itemView.findViewById(R.id.sensor_type);
        }

        public void bind(Sensor sensor) {
            sensorNameTextView.setText(sensor.getName());
            sensorTypeTextView.setText(String.valueOf(sensor.getType()));
            View itemContainer = itemView.findViewById(R.id.list_item_sensor);
            if (favourSensors.contains(sensor.getType())) {
                itemContainer.setBackgroundColor(getResources().getColor(R.color.tempsw));
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivity(intent);
                });
            }
            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                itemContainer.setBackgroundColor(getResources().getColor(R.color.lokalizacja));
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                    startActivity(intent);
                });
            }
        }

    }

}