package com.moto.futbolito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private final float[] rotationSensorReading = new float[3];
    ImageView ballon;
    ImageView porteria1;
    ImageView porteria2;
    TextView goles1;
    TextView goles2;
    ConstraintLayout cancha;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancha = findViewById(R.id.cancha);
        ballon = findViewById(R.id.ballon);
        porteria1 = findViewById(R.id.porteria1);
        porteria2 = findViewById(R.id.porteria2);
        goles1 = findViewById(R.id.txtGoles1);
        goles2 = findViewById(R.id.txtGoles2);

        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get updates from the rotation and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acelerometer != null) {
            sensorManager.registerListener(this, acelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, rotationSensorReading,
                    0, rotationSensorReading.length);
        }
        Log.d("MySensor",rotationSensorReading[1] + "");
        width = cancha.getWidth(); // ancho absoluto en pixels
        height = cancha.getHeight(); // alto absoluto en pixels
        // sensor[0]
        // izquierda +
        // derecha -
        if(ballon.getX() < 0){
            ballon.setX(0);
        }else if((ballon.getX()+ballon.getHeight()) > width && width != 0){
            ballon.setX(width-ballon.getHeight());
        }else {
            ballon.setX(ballon.getX()-(rotationSensorReading[0]*10));
        }

        // Sensor [1]
        // arriba -
        // abajo +
        Log.d("Position",(ballon.getY()+ballon.getWidth())+"");
        if (ballon.getY() < 0){
            ballon.setY(0);
        }else if((ballon.getY()+ballon.getWidth()) > height && height != 0){
            ballon.setY(height-ballon.getWidth());
        }else {
            ballon.setY(ballon.getY()+(rotationSensorReading[1]*10));
        }

        if (ballon.getX() + ballon.getWidth() -15 >= porteria1.getX() && ballon.getX() - 15 <= porteria1.getX() + porteria1.getWidth()){
            if (ballon.getY() + 15 >= porteria1.getY() && ballon.getY() + 15 <= porteria1.getY() + porteria1.getHeight()) {
                goles2.setText(((Integer.parseInt(goles2.getText().toString()))+1)+"");
                ballon.setX(width/2);
                ballon.setY(height/2);
                /*goales2 += 1;
                goal2.setText("Goles: " + goales2);
                ballon.animate().setDuration(1);
                x = 0;
                y = 0;
                ballon.animate().translationX(x);
                ballon.animate().translationY(y);
                ballon.animate().setDuration(350);
                */
                Log.d("Position","Gol en la porteria 1");
            }
        }
        if (ballon.getX() + ballon.getWidth() - 15 >= porteria2.getX() && ballon.getX() - 15 <= porteria2.getX() + porteria2.getWidth()){
            if (ballon.getY() + ballon.getHeight() - 15 >= porteria2.getY() && ballon.getY() - 15 <= porteria2.getY() + porteria2.getHeight()) {
                goles1.setText(((Integer.parseInt(goles1.getText().toString()))+1)+"");
                ballon.setX(width/2);
                ballon.setY(height/2);
                /*goales1 += 1;
                goal1.setText("Goles: " + goales1);
                ballon.animate().setDuration(1);
                x = 0;
                y = 0;
                ballon.animate().translationX(x);
                ballon.animate().translationY(y);
                ballon.animate().setDuration(350);
                 */
                Log.d("Position","Gol en la porteria 2");
            }
        }

        // Sensor [2]
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}