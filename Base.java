import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;

public class Base extends TableComponent {
    private int width  = 1600;
    private int height = 900;
    private int cornerRadius = 64;
    private Color color = new Color(101, 66, 32);
    private Shape shape;
    
    
    public Base() {
        createImage();
        drawImage();
    }
    
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        shape = new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius);
        
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
    
    
    public Base setSize(int width, int height) { this.width  = width; this.height = height; createImage(); drawImage(); return this; }
    public Base setCornerRadius(int cornerRadius) { this.cornerRadius = cornerRadius; drawImage(); return this; }
    public Base setColor(Color color) { this.color = color; drawImage(); return this; }
}
