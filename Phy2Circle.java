/**
 * Write a description of class PhyCollider here.
 * 
 * @version (a version number or a date)
 */

import java.awt.Color;

public class Phy2Circle extends Phy2Collider {
    private double radius = 16;
    private double diameter = 2*radius;
    
    
    Phy2Circle() {}
    
    
    @Override
    protected void updateBoundingBoxSize() {
        getBoundingBox().setTopLeft(-radius, -radius);
        getBoundingBox().setBottomRight(radius, radius);
        
        super.updateBoundingBoxSize();
    }
    
    @Override
    protected void drawDebug(Overlay overlay) {
        if (!Game.getInstance().isDebug()) return;
        
        super.drawDebug(overlay);
        
        int x = getPos().getIntX();
        int y = getPos().getIntY();
        
        int r = (int)Math.round(radius);
        int d = (int)Math.round(diameter);
        
        overlay.setPaint(Color.RED);
        overlay.drawOval(x-r, y-r, d, d);
    }
    
    
    /**
     * 
     */
    public double getRadius() {
        return radius;
    }
    
    /**
     * 
     */
    public double getDiameter() {
        return diameter;
    }
    
    
    /**
     * 
     */
    public Phy2Circle setRadius(double radius) {
        this.radius = radius;
        this.diameter = 2*radius;
        
        updateBoundingBoxSize();
        
        return this;
    }
}
