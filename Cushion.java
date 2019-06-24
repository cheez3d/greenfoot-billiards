import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;

public class Cushion extends TableComponent {
    private int width = 256;
    private int height = 16;
    private double angle1 = Math.PI/4;
    private double angle2 = Math.PI/4;
    private Color color = new Color(83, 176, 186);
    private Shape shape;
    
    
    public Cushion() {
        Phy2Trapezoid phy2Trapezoid = new Phy2Trapezoid(width, height, angle1, angle2);
        
        Phy2Obj phy2Obj = new Phy2Static();
        phy2Obj.setCollider(phy2Trapezoid);
        phy2Obj.setGameActor(this);
        
        updateShape();
        createImage();
        drawImage();
    }
    
    
    private void updateShape() {
        // +----------------------------------------------+
        // | SCHEMA UNUI CUSHION                          |
        // +----------------------------------------------+
        // |                                              |
        // |    (l1)(0)-------(remainder)------(1)(l2)    |
        // | angle1 /                            \ angle2 |
        // |       /                              \       |
        // |      / angle1                  angle2 \      |
        // |    (3)------------(width)-------------(2)    |
        // |                                              |
        // +----------------------------------------------+
        
        int l1 = (int)Math.round(height/Math.tan(angle1));
        int l2 = (int)Math.round(height/Math.tan(angle2));
        int remainder = width-(l1+l2);
        
        int[] pointsX = new int[]{l1, width-l2,  width,      0};
        int[] pointsY = new int[]{0,         0, height, height};
        
        shape = new Polygon(pointsX, pointsY, pointsX.length);
        
        Phy2Obj phy2Obj = getPhy2Obj();
        if (phy2Obj != null) {
            Phy2Collider phy2Collider = phy2Obj.getCollider();
            if (phy2Collider != null) {
                if (phy2Collider instanceof Phy2Trapezoid) {
                    Phy2Trapezoid phy2Trapezoid = (Phy2Trapezoid)phy2Collider;
                    
                    phy2Trapezoid.setWidth(width);
                    phy2Trapezoid.setHeight(height);
                    phy2Trapezoid.setAngle1(angle1);
                    phy2Trapezoid.setAngle2(angle2);
                }
            }
        }
    }
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        greenfootImg = new GreenfootImage(width, height);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    private void drawImage() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, width, height);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        imgG2d.setPaint(color);
        imgG2d.fill(shape);
    }
    
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public double getAngle1() {
        return angle1;
    }
    
    public double getAngle2() {
        return angle2;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    
    public Cushion setWidth(int width) {
        this.width = width;
        
        updateShape();
        createImage();
        drawImage();
        
        return this;
    }
    
    public Cushion setHeight(int height) {
        this.height = height;
        
        updateShape();
        createImage();
        drawImage();
        
        return this;
    }
    
    public Cushion setAngle1(double angle1) {
        this.angle1 = angle1;
        
        updateShape();
        drawImage();
        
        return this;
    }
    
    public Cushion setAngle2(double angle2) {
        this.angle2 = angle2;
        
        updateShape();
        drawImage();
        
        return this;
    }
    
    public Cushion setColor(Color color) {
        this.color = color;
        
        drawImage();
        
        return this;
    }
}
