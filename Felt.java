import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;
import greenfoot.World;

public class Felt extends TableComponent {
    private int width  = 1600;
    private int height = 900;
    private Color color1 = new Color(0, 150, 170); // culoarea din centru
    private Color color2 = new Color(0, 70, 70); // culoara din margini
    private Shape shape;
    
    
    public Felt() {
        createImage();
        drawImage();
    }
    
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        shape = new Rectangle2D.Double(0, 0, width, height); 
        
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
        
        // creeaza un gradient radial/circular intre cele doua culori
        Paint paint = new RadialGradientPaint(
            width/2, height/2, Math.max(width, height),
            new float[]{0.0f, 1.0f}, new Color[]{color1, color2}
        );
        
        imgG2d.setPaint(paint);
        imgG2d.fill(shape);
    }
    
    
    public Shape getShape() { return shape; }
    
    public Felt setSize(int width, int height) { this.width  = width; this.height = height; createImage(); drawImage(); return this; }
    public Felt setColor1(Color color1) { this.color1 = color1; drawImage(); return this; }
    public Felt setColor2(Color color2) { this.color2 = color2; drawImage(); return this; }
}
