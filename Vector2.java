/**
 * Clasa pentru reprezentarea unui vector bidimensional.
 * 
 * @version 2018-01-14
 */


public final class Vector2 {
    public static final Vector2 ZERO = new Vector2();
    
    
    private final double x, y;
    
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }
    
    public Vector2() {
        this(0, 0);
    }
    
    
    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
    
    
    public Vector2 minus() {
        return new Vector2(-x, -y);
    }
    
    public Vector2 normalize() {
        double l = getLength();
        
        if (l > 0)
            return new Vector2(x/l, y/l);
        else
            return ZERO;
    }
    
    public Vector2 add(Vector2 o) {
        return new Vector2(x+o.x, y+o.y);
    }
    
    public Vector2 add(double x, double y) {
        return add(new Vector2(x, y));
    }
    
    public Vector2 sub(Vector2 o) {
        return new Vector2(x-o.x, y-o.y);
    }
    
    public Vector2 sub(double x, double y) {
        return sub(new Vector2(x, y));
    }
    
    public Vector2 mul(double f) {
        return new Vector2(f*x, f*y);
    }
    
    public Vector2 middle(Vector2 other) {
        return new Vector2((x+other.x)/2, (y+other.y)/2);
    }
    
    public Vector2 project(Vector2 on) {
        double f = dot(on)/on.dot(on);
        
        return new Vector2(f*on.x, f*on.y);
    }
    
    public Vector2 reflect(Vector2 normal) {
        return sub(normal.mul(2*dot(normal)));
    }
    
    public Vector2 rotate(double a) {
        // calculeaza sin si cos de unghiul de rotatie
        double c = Math.cos(a);
        double s = Math.sin(a);
        
        // calculeaza valorile noilor coordonate
        double nx = x*c - y*s;
        double ny = x*s + y*c;
        
        return new Vector2(nx, ny);
    }
    
    public double dot(Vector2 o) {
        return x*o.x + y*o.y;
    }
    
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public long getLongX() {
        return Math.round(x);
    }
    
    public long getLongY() {
        return Math.round(y);
    }
    
    public int getIntX() {
        return (int)getLongX();
    }
    
    public int getIntY() {
        return (int)getLongY();
    }
    
    public double getLengthSquared() {
        return x*x + y*y;
    }
    
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }
}
