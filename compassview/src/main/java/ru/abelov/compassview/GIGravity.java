package ru.abelov.compassview;

public class GIGravity {
    float m_norm;
    private float m_axisX;
    private float m_axisY;
    private float m_axisZ;

    //x-y
    private double yaw;
    //y-z
    private double pitch;
    //x-z
    private double roll;


    public GIGravity() {
        m_axisX = 0;
        m_axisY = 0;
        m_axisZ = 0;
        init();
    }

    public GIGravity(float x, float y, float z) {
        m_axisX = x;
        m_axisY = -y;
        m_axisZ = z;
        init();
    }

    public GIGravity(float[] xyz) {
        m_axisX = xyz[0];
        m_axisY = -xyz[1];
        m_axisZ = xyz[2];


        init();
    }

    private void init() {
        m_norm = (float) Math.sqrt(m_axisX * m_axisX + m_axisY * m_axisY + m_axisZ * m_axisZ);
        m_axisX = m_axisX / m_norm;
        m_axisY = m_axisY / m_norm;
        m_axisZ = m_axisZ / m_norm;

        yaw = Math.toDegrees(Math.atan(m_axisY/m_axisX));
        pitch = Math.toDegrees(Math.atan(m_axisY/m_axisZ));
        roll = Math.toDegrees(Math.atan(m_axisX/m_axisZ));
    }

    public float getX() {
        return m_axisX;
    }

    public float getY() {
        return m_axisY;
    }

    public float getZ() {
        return m_axisZ;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }
}
