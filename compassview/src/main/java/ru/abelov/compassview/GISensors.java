package ru.abelov.compassview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;


public class GISensors {
//    private static GISensors instance;
    //	private LocationManager m_locationManager;
//	GIMNK2DFilter m_buffer;
    SensorManager m_sensor_manager;
    Sensor m_sensor_gravity;
    Sensor m_sensor_accelerometer;
    Sensor m_sensor_magnetic;
    Context m_context;
    float[] valuesGravity;
    float[] valuesAccelerometer;
    float[] valuesMagnet;
    int generalise_factor = 3;
    float[] valuesResult;
    float[] inR;
    float[] outR;
    GIGravity m_gravity;
    int m_rotation;
    GIConveyor m_azimuth;
    GIConveyor m_pitch;
    GIConveyor m_roll;

    SensorEventListener listener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.97f;

            switch (event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER: {
//					valuesAccelerometer = event.values.clone();
                    valuesAccelerometer[0] = alpha * valuesAccelerometer[0] + (1 - alpha) * event.values[0];
                    valuesAccelerometer[1] = alpha * valuesAccelerometer[1] + (1 - alpha) * event.values[1];
                    valuesAccelerometer[2] = alpha * valuesAccelerometer[2] + (1 - alpha) * event.values[2];
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD: {
//					valuesMagnet = event.values.clone();
                    valuesMagnet[0] = alpha * valuesMagnet[0] + (1 - alpha) * event.values[0];
                    valuesMagnet[1] = alpha * valuesMagnet[1] + (1 - alpha) * event.values[1];
                    valuesMagnet[2] = alpha * valuesMagnet[2] + (1 - alpha) * event.values[2];
                    break;
                }
                case Sensor.TYPE_GRAVITY: {
//					valuesGravity = event.values.clone();
                    valuesGravity[0] = alpha * valuesGravity[0] + (1 - alpha) * event.values[0];
                    valuesGravity[1] = alpha * valuesGravity[1] + (1 - alpha) * event.values[1];
                    valuesGravity[2] = alpha * valuesGravity[2] + (1 - alpha) * event.values[2];
                    int rotation = ((WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
                    switch (rotation) {
                        case Surface.ROTATION_0: {
                            break;
                        }
                        case Surface.ROTATION_90: {
                            m_gravity = new GIGravity(event.values[1], -event.values[0], event.values[2]);
                            break;
                        }
                        case Surface.ROTATION_180: {
                            m_gravity = new GIGravity(event.values[0], event.values[1], event.values[2]);
                            break;
                        }
                        case Surface.ROTATION_270: {
                            m_gravity = new GIGravity(-event.values[1], event.values[0], event.values[2]);
                            break;
                        }
                    }
                    break;
                }
            }
            getActualDeviceOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public GISensors(Context context) {
        m_context = context;
        m_sensor_manager = (SensorManager) m_context.getSystemService(Context.SENSOR_SERVICE);
        m_sensor_gravity = m_sensor_manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        m_sensor_accelerometer = m_sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensor_magnetic = m_sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        m_sensor_manager.registerListener(listener, m_sensor_gravity, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensor_manager.registerListener(listener, m_sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensor_manager.registerListener(listener, m_sensor_magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        m_gravity = new GIGravity();
        valuesGravity = new float[3];
        valuesAccelerometer = new float[3];
        valuesMagnet = new float[3];
        valuesResult = new float[3];
        inR = new float[9];
        outR = new float[9];
        m_azimuth = new GIConveyor();
        m_pitch = new GIConveyor();
        m_roll = new GIConveyor();
//		m_buffer = new GIMNK2DFilter(100);
//		m_locationManager = (LocationManager)m_context.getSystemService(Context.LOCATION_SERVICE);
    }

//	private LocationListener m_LocationListener = new LocationListener() {
////		public void onLocationChanged(Location location)
////		{
////			m_buffer.addValue(location);
////		}
//		public void onProviderDisabled(String provider) {}
//		public void onProviderEnabled(String provider) {}
//		public void onStatusChanged(String provider, int status, Bundle extras) {}
//	};

//    public static GISensors Instance(Context context) {
//        if (instance == null) {
//            instance = new GISensors(context);
//        }
//        instance.m_context = context;
//        return instance;
//    }

    public GIGravity getGravity() {
        return m_gravity;
    }

    public void run(boolean run) {
        if (!run) {
            m_sensor_manager.unregisterListener(listener);
//			m_locationManager.removeUpdates(m_LocationListener);
        } else {
            m_sensor_manager.registerListener(listener, m_sensor_gravity, SensorManager.SENSOR_DELAY_NORMAL);
            m_sensor_manager.registerListener(listener, m_sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            m_sensor_manager.registerListener(listener, m_sensor_magnetic, SensorManager.SENSOR_DELAY_NORMAL);

//			m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100 * 10, 10, m_LocationListener);
//			m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100 * 10, 10, m_LocationListener);
        }
    }

    private void getActualDeviceOrientation() {
        try {
            if (SensorManager.getRotationMatrix(inR, new float[9], valuesAccelerometer, valuesMagnet)) {
                int x_axis = SensorManager.AXIS_X;
                int y_axis = SensorManager.AXIS_Y;
                m_rotation = ((WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
                switch (m_rotation) {
                    case (Surface.ROTATION_0):
                        break;
                    case (Surface.ROTATION_90): {
                        x_axis = SensorManager.AXIS_Y;
                        y_axis = SensorManager.AXIS_MINUS_X;
                        break;
                    }
                    case (Surface.ROTATION_180): {
                        x_axis = SensorManager.AXIS_X;
                        y_axis = SensorManager.AXIS_MINUS_Y;
                        break;
                    }
                    case (Surface.ROTATION_270): {
                        x_axis = SensorManager.AXIS_MINUS_Y;
                        y_axis = SensorManager.AXIS_X;
                        break;
                    }
                    default:
                        break;
                }

                SensorManager.remapCoordinateSystem(inR, x_axis, y_axis, outR);
                SensorManager.getOrientation(outR, valuesResult);
                valuesResult[0] = (float) Math.toDegrees(valuesResult[0]);
                valuesResult[1] = (float) Math.toDegrees(valuesResult[1]);
                valuesResult[2] = (float) Math.toDegrees(valuesResult[2]);
                m_azimuth.addValue(valuesResult[0]);
                m_pitch.addValue(valuesResult[1]);
                m_roll.addValue(valuesResult[2]);
            }
        } catch (Exception e) {
            String res = e.toString();
        }
        return;
    }

    public float[] getOrientation() {
//		float[] res = new float[3];
//		res[0] = (float) m_azimuth.getValue();
//		res[1] = (float) m_pitch.getValue();
//		res[2] = (float) m_roll.getValue();
//		return res;

        return valuesResult;
    }


//
//	public GIMNK2DFilter.ParametricPoint getSpeedAsDelta()
//	{
//		return m_buffer.get_asDelta();
//	}
//	public GIMNK2DFilter.ParametricPoint[] getSpeedAsLine()
//	{
//		return m_buffer.getMNT_asLine();
//	}

}
