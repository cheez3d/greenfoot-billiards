import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;
import greenfoot.World;

public class Ball extends GameActor {
    private int radius = 18;
    private int diameter = 2*radius;
    private Color color = Color.BLACK;
    private Shape shape;
    
    private Color glowColor = new Color(0, 0, 0, 127);
    private Glow glow = new Glow()
        .setType(Glow.Type.OUTER)
        .setSize(1)
        .setColor(glowColor);
    
    
    public Ball() {
        Phy2Circle phy2Circle = new Phy2Circle();
        phy2Circle.setRadius(radius);
        
        Phy2Obj phy2Obj = new Phy2Dynamic();
        phy2Obj.setCollider(phy2Circle);
        phy2Obj.setGameActor(this);
        
        
        Phy2Obj rayGuide = new Phy2Static();
        rayGuide.setCollider(new Phy2Circle().setRadius(radius+radius));
        
        Phy2Attachment phy2Attachment = new Phy2Attachment();
        phy2Attachment.setPhy2Obj(rayGuide);
        phy2Attachment.setAttachedTo(phy2Obj);
        
        
        updateShape();
        createImage();
        drawImage();
        
        Attachment attachment = new Attachment();
        attachment.setGameActor(glow);
        attachment.setAttachedTo(this);
    }
    
    
    private void updateShape() {
        shape = new Ellipse2D.Double(0, 0, diameter, diameter);
        
        glow.setShape(shape);
        
        Phy2Obj phy2Obj = getPhy2Obj();
        if (phy2Obj != null) {
            Phy2Collider phy2Collider = phy2Obj.getCollider();
            if (phy2Collider != null) {
                if (phy2Collider instanceof Phy2Circle) {
                    Phy2Circle phy2Circle = (Phy2Circle)phy2Collider;
                    
                    phy2Circle.setRadius(radius);
                }
            }
        }
    }
    
    protected GreenfootImage greenfootImg;
    protected BufferedImage img;
    protected Graphics2D imgG2d;
    
    protected void createImage() {
        greenfootImg = new GreenfootImage(diameter, diameter);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    protected void drawImage() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, diameter, diameter);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        if (this instanceof TexBall && ((TexBall)this).getType() == TexBall.Type.STRIPE) imgG2d.setPaint(Color.WHITE);
        else imgG2d.setPaint(color);
        
        imgG2d.fill(shape);
    }
    
    
    public int getRadius() {
        return radius;
    }
    
    public int getDiameter() {
        return diameter;
    }
    
    public Color getColor() {
        return color;
    }
    
    
    private int prevRadius;
    protected Ball setRadius(int radius, boolean updateImage) {
        prevRadius = this.radius;
        
        this.radius = radius;
        this.diameter = 2*radius;
        
        updateShape();
        
        if (updateImage) {
            createImage();
            drawImage();
        }
        
        return this;
    }
    
    public Ball setRadius(int radius) {
        return setRadius(radius, true);
    }
    
    protected Ball setColor(Color color, boolean updateImage) {
        this.color = color;
        
        if (updateImage) {
            drawImage();
        }
        
        return this;
    }
    
    public Ball setColor(Color color) {
        return setColor(color, true);
    }
}
