/**
 * Write a description of class Quaternion here.
 * 
 * @version 2018-02-15
 */

public final class Quaternion {
    public static final Quaternion ZERO = new Quaternion();
    public static final Quaternion IDENTITY = new Quaternion(1, 0, 0, 0);
    
    
    private final double w, x, y, z; 

    
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Quaternion(double w, Vector3 v) {
        this(w, v.getX(), v.getY(), v.getZ());
    }
    
    public Quaternion(Quaternion q) {
        this(q.w, q.x, q.y, q.z);
    }
    
    public Quaternion() {
        this(0, 0, 0, 0);
    }
    
    public static Quaternion rot(double w, double x, double y, double z) {
        double ha = w/2; // halfAngle
        
        return new Quaternion(
              Math.cos(ha),
            x*Math.sin(ha),
            y*Math.sin(ha),
            z*Math.sin(ha)
        );
    }
    
    public static Quaternion rot(double angle, Vector3 axis) {
        return rot(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    
    @Override
    public String toString() {
        return String.format("(%f, %f, %f, %f)", w, x, y, z);
    }
    
    
    public Quaternion mul(Quaternion o) {
        double nw = w*o.w - x*o.x - y*o.y - z*o.z;
        double nx = w*o.x + x*o.w + y*o.z - z*o.y;
        double ny = w*o.y - x*o.z + y*o.w + z*o.x;
        double nz = w*o.z + x*o.y - y*o.x + z*o.w;
        
        return new Quaternion(nw, nx, ny, nz);
    }
    
    public Quaternion normalize() {
        double l = getLength();
        
        if (l > 0)
            return new Quaternion(w/l, x/l, y/l, z/l);
        else
            return ZERO;
    }
    
    public Quaternion conj() {
        return new Quaternion(w, -x, -y, -z);
    }
    
    public Quaternion inv() {
        double ls = getLengthSquared();
        
        if (ls > 0)
            return new Quaternion(w/ls, -x/ls, -y/ls, -z/ls);
        else
            return this;
    }
    
    
    public double getW() {
        return w;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public double getLengthSquared() {
        return w*w+x*x+y*y+z*z;
    }
    
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }
}
