public final class Vector3 {
    public static final Vector3 ZERO = new Vector3();
    
    
    private final double x, y, z;
    
    
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(Vector3 v) {
        this(v.x, v.y, v.z);
    }
    
    public Vector3(Vector2 v) {
        this(v.getX(), v.getY(), 0);
    }
    
    public Vector3() {
        this(0, 0, 0);
    }
    
    
    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
    
    
    public Vector3 minus() {
        return new Vector3(-x, -y, -z);
    }
    
    public Vector3 add(Vector3 o) {
        return new Vector3(x+o.x, y+o.y, z+o.z);
    }
    
    public Vector3 add(double x, double y, double z) {
        return add(new Vector3(x, y, z));
    }
    
    public Vector3 sub(Vector3 o) {
        return new Vector3(x-o.x, y-o.y, z-o.z);
    }
    
    public Vector3 sub(double x, double y, double z) {
        return sub(new Vector3(x, y, z));
    }
    
    public Vector3 mul(double f) {
        return new Vector3(f*x, f*y, f*z);
    }
    
    /**
     * Executa inmultirea unui Vector3 cu un Matrix3x3 (vectorul e considerat column-major, adica o matrice 3x1 (3 randuri, o coloana)).
     * @return Returneaza un nou vector ce a fost inmultit cu matricea.
     */
    public Vector3 mul(Matrix3x3 m) {
        double nx = m.get00()*x + m.get01()*y + m.get02()*z;
        double ny = m.get10()*x + m.get11()*y + m.get12()*z;
        double nz = m.get20()*x + m.get21()*y + m.get22()*z;
        
        return new Vector3(nx, ny, nz);
    }
    
    public Vector3 normalize() {
        double l = getLength();
        
        if (l > 0)
            return new Vector3(x/l, y/l, z/l);
        else
            return ZERO;
    }
    
    public Vector3 rotate(double x, double y, double z) {
        double sx = Math.sin(x), cx = Math.cos(x);
        double sy = Math.sin(y), cy = Math.cos(y);
        double sz = Math.sin(z), cz = Math.cos(z);
        
        Matrix3x3 rx = new Matrix3x3(
            1,  0,   0,
            0, cx, -sx,
            0, sx,  cx
        );
        
        Matrix3x3 ry = new Matrix3x3(
             cy, 0, sy,
              0, 1,  0,
            -sy, 0, cy
        );
        
        Matrix3x3 rz = new Matrix3x3(
            cz, -sz, 0,
            sz,  cz, 0,
            0,    0, 1
        );
        
        return mul(ry.mul(rx).mul(rz));
    }
    
    public Vector3 rotate(Vector3 r) {
        return rotate(r.x, r.y, r.z);
    }
    
    public Vector3 rotate(Quaternion r) {
        // // IMPLEMENTARE VECHE:
        // Quaternion p = new Quaternion(0, x, y, z); // reprezentam punctul sub forma de cuaternion
        // p = r.mul(p).mul(r.inv()); // rotim punctul (r*p*(r^-1))
        // return new Vector3(p.getX(), p.getY(), p.getZ()); // returnam partea vectoriala a cuaternionului obtinut
        
        Vector3 u = new Vector3(r.getX(), r.getY(), r.getZ()); // partea vectoriala a cuaternionului
        double s = r.getW(); // partea scalara  a cuaternionului
        
        Vector3 v = this;
        
        return u.mul(2*u.dot(v))
            .add(v.mul(s*s-u.dot(u)))
            .add(u.cross(v).mul(2*s));
    }
    
    public double dot(Vector3 o) {
        return x*o.x + y*o.y + z*o.z;
    }
    
    public Vector3 cross(Vector3 o) {
        return new Vector3(
            y*o.z - z*o.y,
            z*o.x - x*o.z,
            x*o.y - y*o.x
        );
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
    
    public long getLongX() {
        return Math.round(x);
    }
    
    public long getLongY() {
        return Math.round(y);
    }
    
    public long getLongZ() {
        return Math.round(z);
    }
    
    public int getIntX() {
        return (int)getLongX();
    }
    
    public int getIntY() {
        return (int)getLongY();
    }
    
    public int getIntZ() {
        return (int)getLongZ();
    }
    
    public double getLengthSquared() {
        return x*x+y*y+z*z;
    }
    
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }
}
