/**
 * +-------------------------
 * | SCHEMA UNUI BoundingBox
 * +-------------------------
 * | TODO
 * |
 * 
 * @version 2018-02-10
 */
public class BoundingBox {
    private Vector2 pos, topLeft, bottomRight;
    
    public BoundingBox(
        Vector2 pos,
        Vector2 topLeft,
        Vector2 bottomRight
    ) {
        this.pos = pos;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    
    public BoundingBox(
        double x, double y,
        double x1, double y1,
        double x2, double y2
    ) {
        this(
            new Vector2(x, y),
            new Vector2(x1, y1),
            new Vector2(x2, y2)
        );
    }
    
    public BoundingBox() {
        this(
            0, 0,
            0, 0,
            0, 0
        );
    }
    
    
    public boolean contains(Vector2 point) {
        double x1 = pos.getX()+topLeft.getX();
        double y1 = pos.getY()+topLeft.getY();
        double x2 = pos.getX()+bottomRight.getX();
        double y2 = pos.getY()+bottomRight.getY();
        
        return point.getX() >= x1
            && point.getY() >= y1
            && point.getX() <= x2
            && point.getY() <= y2;
    }
    
    public boolean intersects(BoundingBox other) {
        double x1 = pos.getX()+topLeft.getX();
        double y1 = pos.getY()+topLeft.getY();
        double x2 = pos.getX()+bottomRight.getX();
        double y2 = pos.getY()+bottomRight.getY();
        
        double ox1 = other.pos.getX()+other.topLeft.getX();
        double oy1 = other.pos.getY()+other.topLeft.getY();
        double ox2 = other.pos.getX()+other.bottomRight.getX();
        double oy2 = other.pos.getY()+other.bottomRight.getY();
        
        // test de intersectie AABB (Axis Aligned Bounding Box)
        return x2 >= ox1
            && y2 >= oy1
            && ox2 >= x1
            && oy2 >= y1;
    }
    
    
    public double getX() {
        return pos.getX();
    }
    
    public double getY() {
        return pos.getY();
    }
    
    public Vector2 getPos() {
        return pos;
    }
    
    public Vector2 getTopLeft() {
        return topLeft;
    }
    
    public Vector2 getBottomRight() {
        return bottomRight;
    }
    
    
    public BoundingBox setPos(Vector2 pos) {
        this.pos = pos;
        
        return this;
    }
    
    public BoundingBox setPos(double x, double y) {
        return setPos(new Vector2(x, y));
    }
    
    public BoundingBox setTopLeft(Vector2 topLeft) {
        this.topLeft = topLeft;
        
        return this;
    }
    
    public BoundingBox setTopLeft(double x, double y) {
        return setTopLeft(new Vector2(x, y));
    }
    
    public BoundingBox setBottomRight(Vector2 bottomRight) {
        this.bottomRight = bottomRight;
        
        return this;
    }
    
    public BoundingBox setBottomRight(double x, double y) {
        return setBottomRight(new Vector2(x, y));
    }
}
