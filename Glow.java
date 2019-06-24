import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.lang.reflect.Method;
// https://github.com/ajmas/JH-Labs-Java-Image-Filters/blob/master/src/com/jhlabs/image/BoxBlurFilter.java
import com.jhlabs.image.BoxBlurFilter;

public class Glow extends GameActor {
    public enum Type { INNER, OUTER }
    
    
    private static final int BLUR_RADIUS = 4;
    private static final int BLUR_ITER = 3;
    private static final BoxBlurFilter BLUR = new BoxBlurFilter(BLUR_RADIUS, BLUR_RADIUS, BLUR_ITER);
    
    
    private Type type = Type.INNER;
    private int size = 4;
    private Color color = new Color(0, 0, 0, 127);
    private boolean blur = true;
    private Shape shape = new Rectangle2D.Double(0, 0, 32, 32);
    
    
    public Glow() {
        createImage();
        drawImage();
    }
    
     
    private int blurReach;
    
    private Rectangle2D bounds;
    private double imgWidth, imgHeight;
    private int imgIntWidth, imgIntHeight;
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        blurReach = size+BLUR_RADIUS*BLUR_ITER;
        
        bounds = shape.getBounds2D();
        
        switch (type) {
            case INNER: {
                imgWidth = bounds.getWidth();
                imgHeight = bounds.getHeight();
                
                break;
            }
            
            case OUTER: {
                imgWidth = blurReach+bounds.getWidth()+blurReach;
                imgHeight = blurReach+bounds.getHeight()+blurReach;
                
                break;
            }
        }
        
        imgIntWidth = (int)Math.round(imgWidth);
        imgIntHeight = (int)Math.round(imgHeight);
        
        greenfootImg = new GreenfootImage(imgIntWidth, imgIntHeight);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    public void drawImage() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, imgIntWidth, imgIntHeight);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        // strokeul nou creat se extinde de la marginea shape-ului atat la interior cat si la exterior
        // cu jumatate din grosimea specificata; de aceea inmultim size cu 2 pentru ca strokeul
        // sa se extinda cu grosimea specificata atat la interior cat si la exterior
        // mai apoi taiem excesul, interior sau exterior, in functie de ce tip de glow se utilizeaza
        Stroke stroke = new BasicStroke(2*size, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        
        Area glow = new Area(stroke.createStrokedShape(shape));
        
        switch (type) {
            case INNER: {
                // mold-ul va contine suprafata obtinuta prin scaderea suprafetei shape-ului din bounds
                // (adica restul suprafetei din bounds, complementul lui glow)
                Area mold = new Area(shape);
                mold.exclusiveOr(new Area(bounds)); // obtinem diferenta prin aplicarea operatiei xor
                
                glow.add(mold);
                
                imgG2d.setPaint(color);
                imgG2d.fill(glow);
                
                BLUR.filter(img, img);
                
                imgG2d.setComposite(AlphaComposite.Clear);
                imgG2d.fill(mold);
                imgG2d.setComposite(AlphaComposite.SrcOver);
                
                break;
            }
            
            case OUTER: {
                glow.add(new Area(shape));
                
                imgG2d.translate(blurReach, blurReach);
                
                imgG2d.setPaint(color);
                imgG2d.fill(glow);
                
                if (blur) BLUR.filter(img, img);
                
                imgG2d.setComposite(AlphaComposite.Clear);
                imgG2d.fill(shape);
                imgG2d.setComposite(AlphaComposite.SrcOver);
                
                imgG2d.translate(-blurReach, -blurReach);
                
                break;
            }
        }
    }
    
    
    public Glow setShape(Shape shape) { this.shape = shape; createImage(); drawImage(); return this; }
    public Glow setSize(int size) { this.size = size; drawImage(); return this; }
    public Glow setType(Type type) { this.type = type; createImage(); drawImage(); return this; }
    public Glow setColor(Color color) { this.color = color; drawImage(); return this; }
    public Glow setBlur(boolean enabled) { this.blur = enabled; drawImage(); return this; }
}
