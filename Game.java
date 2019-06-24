/**
 * SINGLETON
 * 
 * Stocheaza toate setarile cu privire la joc.
 * 
 * @version (a version number or a date)
 */

import java.awt.RenderingHints;

import java.util.Date;

import java.text.SimpleDateFormat;

import greenfoot.Greenfoot;

public final class Game  {
    public static final int GREENFOOT_LMB = 1;
    public static final int GREENFOOT_MMB = 2;
    public static final int GREENFOOT_RMB = 3;
    
    
    private boolean isDebug = false;
    
    private Object rendering = RenderingHints.VALUE_RENDER_QUALITY;
    private Object antiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
    
    
    private Game() {}
    
    private static Game instance = null;
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        
        return instance;
    }
    
    
    public Game init() {
        // setDebug(true);
        
        // setam viteza maxima pentru evitarea efectului de tunneling la rezolvarea coliziunilor
        Greenfoot.setSpeed(100);
        
        GameWorld session = new Session(1600, 900);
        Greenfoot.setWorld(session);
        
        return this;
    }
    
    public Game printDebug(String str) {
        if (!isDebug) return this;
        
        // "yyyy-MM-dd HH:mm:ss.SSS"
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        
        System.out.print(String.format("[%s] %s", timestamp, str));
        
        return this;
    }
    
    public Game printDebugLine(String str) {
        if (!isDebug) return this;
        
        return printDebug(str + "\n");
    }
    
    
    public boolean isDebug() {
        return isDebug;
    }
    
    public Object getRendering() {
        return rendering;
    }
    
    public Object getAntiAliasing() {
        return antiAliasing;
    }
    
    
    public Game setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        
        return this;
    }
    
    public Game setRendering(Object rendering) {
        this.rendering = rendering;
        
        return this;
    }
    
    public Game setAntiAliasing(Object antiAliasing) {
        this.antiAliasing = antiAliasing;
        
        return this;
    }
}
