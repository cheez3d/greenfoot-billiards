import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;
import greenfoot.World;

public class Overlay extends GameActor {
    private int imgIntWidth, imgIntHeight;
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    private Graphics2D imgG2d;
    
    
    public Overlay() {}
    
    
    private void createImage() {
        imgIntWidth = getGameWorld().getWidth();
        imgIntHeight = getGameWorld().getHeight();
        
        greenfootImg = new GreenfootImage(imgIntWidth, imgIntHeight);
        img = greenfootImg.getAwtImage();
        imgG2d = img.createGraphics();
        imgG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        imgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        
        setImage(greenfootImg);
    }
    
    private void drawImage() {
        // Stroke stroke = new BasicStroke(16, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        
        // Shape shape = stroke.createStrokedShape(new Rectangle2D.Double(0, 0, imgIntWidth, imgIntHeight));
        
        // imgG2d.setPaint(Color.BLACK);
        // imgG2d.fill(shape);
    }
    
    
    protected void onAddedToGameWorld(GameWorld gameWorld) {
        createImage();
        drawImage();
    }
    
    
    public Overlay drawLine(int ox, int oy, int dx, int dy) {
        imgG2d.drawLine(ox, oy, dx, dy);
        
        return this;
    }
    
    public Overlay drawLine(Vector2 p1, Vector2 p2) {
        return drawLine(p1.getIntX(), p1.getIntY(), p2.getIntX(), p2.getIntY());
    }
    
    public Overlay drawOval(int x, int y, int width, int height) {
        imgG2d.drawOval(x, y, width, height);
        
        return this;
    }
    
    public Overlay drawSegment2(Segment2 s) {
        Vector2 orig = s.getOrig();
        Vector2 span = s.getSpan();
        
        imgG2d.drawLine(
            orig.getIntX(), orig.getIntY(),
            orig.getIntX()+span.getIntX(), orig.getIntY()+span.getIntY()
        );
        
        return this;
    }
    
    public Overlay drawBoundingBox(BoundingBox boundingBox) {
        int x = boundingBox.getPos().getIntX();
        int y = boundingBox.getPos().getIntY();
        
        int x1 = x+boundingBox.getTopLeft().getIntX();
        int y1 = y+boundingBox.getTopLeft().getIntY();
        
        int x2 = x+boundingBox.getBottomRight().getIntX();
        int y2 = y+boundingBox.getBottomRight().getIntY();
        
        drawLine(x1, y1, x2, y1);
        drawLine(x2, y1, x2, y2);
        drawLine(x2, y2, x1, y2);
        drawLine(x1, y2, x1, y1);
        
        return this;
    }
    
    public Overlay drawImage(Image img, int x, int y, int width, int height) {
        imgG2d.drawImage(img, x, y, width, height, null);
        
        return this;
    }
    
    public Overlay drawImageCentered(Image img, int x, int y, int width, int height) {
        return drawImage(img, x-width/2, y-height/2, width, height);
    }
    
    public Overlay drawImage(Image img, AffineTransform t) {
        imgG2d.drawImage(img, t, null);
        
        return this;
    }
    
    public Overlay drawImageCentered(Image img, AffineTransform t) {
        double dx = -img.getWidth(null)/2;
        double dy = -img.getHeight(null)/2;
        
        t.translate(dx, dy);
        drawImage(img, t);
        t.translate(-dx, -dy); // reda statutul initial al transformarii
        
        return this;
    }
    
    public Overlay drawString(String str, int x, int y) {
        imgG2d.drawString(str, x, y);
        
        return this;
    }
    
    public Overlay clear() {
        imgG2d.setComposite(AlphaComposite.Clear);
        imgG2d.fillRect(0, 0, imgIntWidth, imgIntHeight);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        return this;
    }
    
    
    public Font getFont() {
        return imgG2d.getFont();
    }
    
    public Paint getPaint() {
        return imgG2d.getPaint();
    }
    
    
    
    public Overlay setFont(Font font) {
        imgG2d.setFont(font);
        
        return this;
    }
    
    public Overlay setPaint(Paint paint) {
        imgG2d.setPaint(paint);
        
        return this;
    }
}
