import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;

public class Cue extends GameActor {
    private int length = 512;
    private int grith = 8;
    private int tipGrith = 4;
    private Color color1 = new Color(239, 165, 99);
    private Color color2 = new Color(214, 133, 62);
    private Color stripeColor = new Color(114, 59, 0);
    private Shape shape;
    
    private Color glowColor = new Color(0, 0, 0, 127);
    private Glow glow = new Glow()
        .setType(Glow.Type.OUTER)
        .setSize(1)
        .setColor(glowColor);
    
        
    public Cue() {
        updateShape();
        createImage();
        drawImage();
        
        Attachment attachment = new Attachment();
        attachment.setGameActor(glow);
        attachment.setAttachedTo(this);
    }
    
    
    private void updateShape() {
        int[] pointsX = new int[]{0,             length,                   length,     0};
        int[] pointsY = new int[]{0, (grith-tipGrith)/2, grith-(grith-tipGrith)/2, grith};
        
        shape = new Polygon(pointsX, pointsY, pointsX.length);
        
        glow.setShape(shape);
    }
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    private void createImage() {
        greenfootImg = new GreenfootImage(length, grith);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    private void drawImage() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, length, grith);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        Paint paint = new LinearGradientPaint(
            0.0f, 0.0f, 0.0f, grith,
            new float[]{0.0f, 1.0f},
            new Color[]{color1, color2}
        );
        
        imgG2d.setPaint(paint);
        imgG2d.fill(shape);
        
        imgG2d.setComposite(AlphaComposite.SrcAtop);
        imgG2d.setPaint(Color.WHITE);
        imgG2d.fillRect(length-tipGrith, 0, tipGrith, grith);
        imgG2d.setPaint(Color.BLACK);
        imgG2d.fillRect(length-tipGrith-3*tipGrith, 0, 3*tipGrith, grith);
        imgG2d.setPaint(stripeColor);
        imgG2d.fillRect(length-4*tipGrith-4, 0, 2, grith);
        imgG2d.fillRect(0, 0, 2, grith);
    }
    
    
    public int getLength() {
        return length;
    }
    
    public int getGrith() {
        return grith;
    }
    
    public int getTipGrith() {
        return tipGrith;
    }
    
    public Color getColor1() {
        return color1;
    }
    
    public Color getColor2() {
        return color2;
    }
    
    public Color getStripeColor() {
        return stripeColor;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    
    public Cue setLength(int length) {
        this.length = length;
        
        updateShape();
        createImage();
        drawImage();
        
        return this;
    }
}
