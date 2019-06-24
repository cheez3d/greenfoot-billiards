/**
 * Write a description of class PhyCollider here.
 */

import java.awt.Color;

public abstract class Phy2Collider {
    private Vector2 pos = new Vector2();
    private double rot = 0.0;
    private BoundingBox boundingBox = new BoundingBox();
    
    // private Phy2Obj obj;
    
    
    protected void updateOutline() {}
    
    protected void updateBoundingBoxPos() {
        boundingBox.setPos(pos);
    }
    
    protected void updateBoundingBoxSize() {}
    
    protected void drawDebug(Overlay overlay) {
        if (!Game.getInstance().isDebug()) return;
        
        overlay.setPaint(Color.GREEN);
        overlay.drawBoundingBox(boundingBox);
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
    
    public double getRot() {
        return rot;
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
    // public Phy2Obj getObj() {
        // return obj;
    // }
    
    
    public Phy2Collider setPos(Vector2 pos) {
        this.pos = pos;
        
        updateBoundingBoxPos();
        
        return this;
    }
    
    public Phy2Collider setRot(double rot) {
        this.rot = rot;
        
        return this;
    }
    
    // protected Phy2Collider setObj(Phy2Obj obj, boolean updateObj) {
        // Phy2Obj prevObj = obj;
        
        // this.obj = obj;
        
        // if (updateObj) {
            // if (prevObj != null) {
                // prevObj.removeCollider(this, false);
            // }
            
            // if (obj != null) {
                // obj.addCollider(this, false);
            // }
        // }
        
        // return this;
    // }
    
    // public Phy2Collider setObj(Phy2Obj obj) {
        // return setObj(obj, true);
    // }
}
