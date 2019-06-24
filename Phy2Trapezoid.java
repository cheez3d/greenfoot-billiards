/**
 * +---------------------------------------------------------+
 * | SCHEMA UNUI Phy2Trapezoid                               |
 * +---------------------------------------------------------+
 * |                                                         |
 * |    (l1)(3)--------(remainder)-------(2)(l2)     <-+     |
 * | angle1 /                              \ angle2    |     |
 * |       /                                \       (height) |
 * |      / angle1                    angle2 \         |     |
 * |    (0)--------------(width)-------------(1)     <-+     |
 * |                                                         |
 * +---------------------------------------------------------+
 * 
 * @version 2018-02-07
 */

import java.awt.Color;

public class Phy2Trapezoid extends Phy2Collider {
    private double width, height;
    private double angle1, angle2;
    
    private Vector2[] vertices = new Vector2[4];
    private Vector2[] vertexNormals = new Vector2[4];
    
    private Segment2[] sides = new Segment2[4];
    private Vector2[] sideNormals = new Vector2[4];
    
    
    public Phy2Trapezoid(double width, double height, double angle1, double angle2) {
        this.width = width;
        this.height = height;
        
        this.angle1 = angle1;
        this.angle2 = angle2;
        
        updateOutline();
        updateBoundingBoxSize();
    }
    
    
    @Override
    protected void updateOutline() {
        double l1 = height/Math.tan(angle1);
        double l2 = height/Math.tan(angle2);
        double remainder = width-(l1+l2);
        
        double[] pointsX = new double[]{     0, width, width-l2, l1};
        double[] pointsY = new double[]{height, height,       0,  0};
        
        // construim varfurile
        for (int i = 0; i < vertices.length; ++i) {
            double x = pointsX[i];
            double y = pointsY[i];
            
            x -= width/2;
            y -= height/2;
            
            Vector2 v = new Vector2(x, y);
            vertices[i] = v.rotate(getRot()-prevRot);
        }
        
        double bisect1 = angle1/2;
        double bisect2 = angle2/2;
        double bisect3 = (Math.PI-angle2)/2;
        double bisect4 = (Math.PI-angle1)/2;
        
        // construim normalele la varfuri
        vertexNormals[0] = new Vector2(-Math.cos(bisect1),  Math.sin(bisect1));
        vertexNormals[1] = new Vector2( Math.cos(bisect2),  Math.sin(bisect2));
        vertexNormals[2] = new Vector2( Math.cos(bisect3), -Math.sin(bisect3));
        vertexNormals[3] = new Vector2(-Math.cos(bisect4), -Math.sin(bisect4));
        
        // rotim normalele varfurilor
        for (int i = 0; i < vertexNormals.length; ++i) {
            vertexNormals[i] = vertexNormals[i].rotate(getRot()-prevRot);
        }
        
        
        // construim laturile
        for (int i = 0; i < sides.length; ++i) {
            int j = i+1;
            if (j == vertices.length) j = 0;
            
            Vector2 orig = vertices[i];
            Vector2 dest = vertices[j];
            
            sides[i] = new Segment2(orig, dest);
        }
        
        // construim normalele la laturi
        sideNormals[0] = new Vector2(                0,                 1);
        sideNormals[1] = new Vector2( Math.sin(angle2), -Math.cos(angle2));
        sideNormals[2] = new Vector2(                0,                -1);
        sideNormals[3] = new Vector2(-Math.sin(angle1), -Math.cos(angle1));
        
        // rotim doar normalele laturilor; laturile nu mai trebuie rotite pentru
        // ca sunt create folosind varfurile deja rotite
        for (int i = 0; i < sideNormals.length; ++i) {
            sideNormals[i] = sideNormals[i].rotate(getRot()-prevRot);
        }
    }
    
    @Override
    protected void updateBoundingBoxSize() {
        double minX = vertices[0].getX();
        double minY = vertices[0].getY();
        double maxX = minX;
        double maxY = minY;
        
        for (int i = 1; i < vertices.length; ++i) {
            Vector2 vertex = vertices[i];
            
            if (vertex.getX() < minX) minX = vertex.getX();
            else if (vertex.getX() > maxX) maxX = vertex.getX();
            
            if (vertex.getY() < minY) minY = vertex.getY();
            else if (vertex.getY() > maxY) maxY = vertex.getY();
        }
        
        getBoundingBox().setTopLeft(minX, minY);
        getBoundingBox().setBottomRight(maxX, maxY);
        
        super.updateBoundingBoxSize();
    }
    
    @Override
    protected void drawDebug(Overlay overlay) {
        if (!Game.getInstance().isDebug()) return;
        
        super.drawDebug(overlay);
        
        overlay.setPaint(Color.RED);
        
        for (int i = 0; i < sides.length; ++i) {
            overlay.drawSegment2(sides[i].add(getPos()));
        }
        
        final int NORMAL_SCALE = 10;
        for (int i = 0; i < sideNormals.length; ++i) {
            double ox = vertices[i].getX();
            double oy = vertices[i].getY();
            
            int j = (i+1 == vertices.length) ? 0 : i+1;
            double dx = vertices[j].getX();
            double dy = vertices[j].getY();
            
            double mx = (ox+dx)/2;
            double my = (oy+dy)/2;
            
            double nx = sideNormals[i].getX();
            double ny = sideNormals[i].getY();
            
            Segment2 seg = new Segment2(
                getX()+mx, getY()+my,
                getX()+mx+(nx*NORMAL_SCALE), getY()+my+(ny*NORMAL_SCALE)
            );
            
            overlay.drawSegment2(seg);
        }
        
        for (int i = 0; i < vertexNormals.length; ++i) {
            double vx = vertices[i].getX();
            double vy = vertices[i].getY();
            
            double nx = vertexNormals[i].getX();
            double ny = vertexNormals[i].getY();
            
            Segment2 seg = new Segment2(
                getX()+vx, getY()+vy,
                getX()+vx+(nx*NORMAL_SCALE), getY()+vy+(ny*NORMAL_SCALE)
            );
            
            overlay.drawSegment2(seg);
        }
    }
    
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public Vector2[] getVertices() {
        return vertices;
    }
    
    public Segment2[] getSides() {
        return sides;
    }
    
    public Vector2[] getSideNormals() {
        return sideNormals;
    }
    
    public Vector2[] getVertexNormals() {
        return vertexNormals;
    }
    
    
    private double prevRot; // rotatia anterioara apelarii setRot folosita in rotirea varfurilor
    @Override
    public Phy2Trapezoid setRot(double rot) {
        prevRot = getRot();
        
        super.setRot(rot);
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
    
    public Phy2Trapezoid setWidth(double width) {
        this.width = width;
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
    
    public Phy2Trapezoid setHeight(double height) {
        this.height = height;
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
    
    public Phy2Trapezoid setAngle1(double angle1) {
        this.angle1 = angle1;
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
    
    public Phy2Trapezoid setAngle2(double angle2) {
        this.angle2 = angle2;
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
    
    public Phy2Trapezoid setAngles(double angle1, double angle2) {
        this.angle1 = angle1;
        this.angle2 = angle2;
        
        updateOutline();
        updateBoundingBoxSize();
        
        return this;
    }
}
