package ru.abelov.compassview;

/**
 * Created by artem on 10.05.18.
 */

public class Trigonometry {

    public static double angleBetweenPlanes(Vector gravity, Vector magnet, Vector axis){

        Vector normalAxisGravity = vectorMultiplication(axis, gravity);
        Vector normalMagnetGravity = vectorMultiplication(magnet, gravity);

        double cos = scalarMultiplication(normalAxisGravity, normalMagnetGravity)/(normalAxisGravity.module()*normalMagnetGravity.module());
        double angle = Math.acos(cos);

        double sign = vectorMultiplication(normalAxisGravity, normalMagnetGravity);

        return angle;
    }

    public static double modulVectorMultiplication(Vector one, Vector two)
    {
        //return vectorX()*edge.vectorY() - vectorY()*edge.vectorX();
        return one.x*two.y - two.x *one.y;
    }

    public static Vector vectorMultiplication(Vector a, Vector b){
        double i = a.y*b.z - a.z*b.y;
        double j = a.z*b.x - a.x*b.z;
        double k = a.x*b.y - a.y*b.x;

        return new Vector(i, j, k);
    }

    public static double scalarMultiplication(Vector a, Vector b){

        double result  = a.x*b.x + a.y*b.y + a.z+b.z;
        return result;
    }

    public static double module(Vector a) {
        double result  = Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
        return result;
    }

    //3D plane  A*x + B*y + C*z + D = 0
    public static class Plane {
        public double A;
        public double B;
        public double C;
        public double D;


        public Plane(Vector normal, Point point) {
            A = normal.x;
            B = normal.y;
            C = normal.z;

            D = -A*point.x - B*point.y - C*point.z;
        }

        public Plane(Vector normal){
//            this(normal, new Point());
            A = normal.x;
            B = normal.y;
            C = normal.z;
            D = 0;
        }

    }

    // 3D vector {x, y, z}
    public static class Vector {
        public double x;
        public double y;
        public double z;

        public Vector(){
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public Vector(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double module() {
            double result  = Math.sqrt(x*x + y*y + z*z);
            return result;
        }
    }

    // Point in 3D space (x, y, z)
    public static class Point {
        public double x;
        public double y;
        public double z;

        public Point(){
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}
