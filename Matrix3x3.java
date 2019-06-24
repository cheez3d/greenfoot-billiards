/**
 * 
 */

public class Matrix3x3 {
    public static final Matrix3x3 ZERO = new Matrix3x3();
    public static final Matrix3x3 IDENTITY = new Matrix3x3(
                                                 1, 0, 0,
                                                 0, 1, 0,
                                                 0, 0, 1
                                             );
    
    
    private final double m[][] = new double[3][3];
    
    
    public Matrix3x3(
        double m00, double m01, double m02,
        double m10, double m11, double m12,
        double m20, double m21, double m22
    ) {
        m[0][0] = m00; m[0][1] = m01; m[0][2] = m02;
        m[1][0] = m10; m[1][1] = m11; m[1][2] = m12;
        m[2][0] = m20; m[2][1] = m21; m[2][2] = m22;
    }

    public Matrix3x3(Matrix3x3 m) {
        this(
            m.m[0][0], m.m[0][1], m.m[0][2],
            m.m[1][0], m.m[1][1], m.m[1][2],
            m.m[2][0], m.m[2][1], m.m[2][2]
        );
    }
    
    public Matrix3x3() {
        this(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        );
    }
    
    
    @Override
    public String toString() {
        return String.format(
            "%f, %f, %f" + "\n"
          + "%f, %f, %f" + "\n"
          + "%f, %f, %f",
          
            m[0][0], m[0][1], m[0][2],
            m[1][0], m[1][1], m[1][2],
            m[2][0], m[2][1], m[2][2]
        );
    }
    
    
    public Matrix3x3 mul(Matrix3x3 o) {
        double nm00 = m[0][0]*o.m[0][0] + m[0][1]*o.m[1][0] + m[0][2]*o.m[2][0];
        double nm01 = m[0][0]*o.m[0][1] + m[0][1]*o.m[1][1] + m[0][2]*o.m[2][1];
        double nm02 = m[0][0]*o.m[0][2] + m[0][1]*o.m[1][2] + m[0][2]*o.m[2][2];
        
        double nm10 = m[1][0]*o.m[0][0] + m[1][1]*o.m[1][0] + m[1][2]*o.m[2][0];
        double nm11 = m[1][0]*o.m[0][1] + m[1][1]*o.m[1][1] + m[1][2]*o.m[2][1];
        double nm12 = m[1][0]*o.m[0][2] + m[1][1]*o.m[1][2] + m[1][2]*o.m[2][2];
        
        double nm20 = m[2][0]*o.m[0][0] + m[2][1]*o.m[1][0] + m[2][2]*o.m[2][0];
        double nm21 = m[2][0]*o.m[0][1] + m[2][1]*o.m[1][1] + m[2][2]*o.m[2][1];
        double nm22 = m[2][0]*o.m[0][2] + m[2][1]*o.m[1][2] + m[2][2]*o.m[2][2];
        
        return new Matrix3x3(
            nm00, nm01, nm02,
            nm10, nm11, nm12,
            nm20, nm21, nm22
        );
    }
    
    
    public double get00() {
        return m[0][0];
    }
    
    public double get01() {
        return m[0][1];
    }
    
    public double get02() {
        return m[0][2];
    }
    
    public double get10() {
        return m[1][0];
    }
    
    public double get11() {
        return m[1][1];
    }
    
    public double get12() {
        return m[1][2];
    }
    
    public double get20() {
        return m[2][0];
    }
    
    public double get21() {
        return m[2][1];
    }
    
    public double get22() {
        return m[2][2];
    }
}
