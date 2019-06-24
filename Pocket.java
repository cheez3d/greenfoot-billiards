import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;
import greenfoot.World;

public class Pocket extends TableComponent {
    private int radius = 32;
    private int diameter = 2*radius;
    private Color color = new Color(60, 60, 60);
    private Shape shape;
    
    private Color glowColor = new Color(0, 0, 0, 127);
    private Glow glow = new Glow()
        .setType(Glow.Type.INNER)
        .setSize(4)
        .setColor(glowColor);
    
    
    public Pocket() {
        Phy2Circle phy2Circle = new Phy2Circle();
        phy2Circle.setRadius(radius);
        
        Phy2Obj phy2Obj = new Phy2Static() {
            @Override
            public void onCollidedWith(Phy2Obj other) {
                super.onCollidedWith(other);
                
                GameActor gameActor = other.getGameActor();
                
                if (gameActor instanceof Ball) {
                    TexBall tb = (TexBall)gameActor;
                    Phy2Dynamic d = (Phy2Dynamic)tb.getPhy2Obj();
                    
                    if (tb.getType() == TexBall.Type.CUE) {
                        d.setPos(tb.getGameWorld().getWidth()/2, tb.getGameWorld().getHeight()/2);
                        d.setVel(Vector2.ZERO);
                    } else {
                        gameActor.setTransparency(0);
                        d.setPos(-100, -100);
                        d.setVel(Vector2.ZERO);
                    }
                }
            }
        };
        phy2Obj.setCollider(phy2Circle);
        phy2Obj.setIsSensor(true);
        phy2Obj.setGameActor(this);
        
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
                    
                    phy2Circle.setRadius(radius-Session.BALL_RADIUS);
                }
            }
        }
    }
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        greenfootImg = new GreenfootImage(diameter, diameter);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    private void drawImage() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, diameter, diameter);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        imgG2d.setPaint(color);
        imgG2d.fill(shape);
    }
    
    
    public int getRadius() {
        return radius;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    
    public Pocket setRadius(int radius) {
        this.radius = radius;
        this.diameter = 2*radius;
        
        updateShape();
        createImage();
        drawImage();
        
        return this;
    }
    
    public Pocket setColor(Color color) {
        this.color = color;
        
        drawImage();
        
        return this;
    }
}
