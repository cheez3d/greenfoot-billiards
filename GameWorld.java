/**
 * Clasa intermediara folosita pentru implementarea de noi functionalitati si
 * pentru rezolvarea anumitor probleme intalnite pe parcursul dezvoltarii aplicatiei.
 * 
 * @version 2018-02-07
 */

import java.awt.Color;
import java.awt.Font;

import java.util.LinkedHashSet;
import java.util.Set;

import greenfoot.World;

public abstract class GameWorld extends World {
    public static final double REF_APMS = 0.06;
    
    
    private long actDelta;
    private Overlay overlay = new Overlay();
    private Physics2 physics2;
    private Set<GameActor> gameActors = new LinkedHashSet<GameActor>();
    
    
    protected GameWorld(int width, int height, int cellSize, boolean bounded) {
        super(width, height, cellSize, bounded);
        
        addGameActor(overlay, width/2, height/2);
        
        try {
            debugFont = FontCache.getFont(Font.SERIF, Font.BOLD, 16);
        } catch (Exception e) {
            debugFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        }
    }
    
    protected GameWorld(int width, int height, int cellSize) {
        this(width, height, cellSize, true);
    }
    
    
    private long lastActTime = System.currentTimeMillis();
    @Override
    public void act() {
        long actTime = System.currentTimeMillis();
        
        actDelta = actTime-lastActTime;
        
        lastActTime = actTime;
        
        overlay.clear();
        
        if (Game.getInstance().isDebug()) {
            drawDebug(overlay);
        }
    }
    
    
    private Font debugFont;
    private int fps;
    protected void drawDebug(Overlay overlay) {
        if (actDelta != 0) {
            fps = (int)(1000/actDelta);
        }
        
        overlay.setFont(debugFont);
        overlay.setPaint(Color.BLACK);
        overlay.drawString(String.format("fps: %d", fps), 4, 16);
    }
    
    
    public long getActDelta() {
        return actDelta;
    }
    
    public Overlay getOverlay() {
        return overlay;
    }
    
    public Physics2 getPhysics2() {
        return physics2;
    }
    
    
    protected GameWorld setPhysics2(Physics2 physics2, boolean updatePhysics2) {
        this.physics2 = physics2;
        
        if (physics2 != null) {
            if (updatePhysics2) {
                physics2.setGameWorld(this, false);
            }
            
            for (GameActor gameActor : gameActors) {
                if (gameActor.getPhy2Obj() != null) {
                    physics2.addObj(gameActor.getPhy2Obj());
                    gameActor.getPhy2Obj().setPhysics2(physics2); //*
                }
            }
        }
        
        return this;
    }
    
    protected GameWorld setPhysics2(Physics2 physics2) {
        return setPhysics2(physics2, true);
    }
    
    public GameWorld addGameActor(GameActor gameActor, Vector2 pos) {
        gameActor.setGameWorld(this);
        
        gameActors.add(gameActor);
        
        super.addObject(gameActor, 0, 0); // adaugam actorul in world
        gameActor.setPos(pos); // repozitionam actorul la coordonatele specificate
        
        // actualizare Physics2
        if (physics2 != null && gameActor.getPhy2Obj() != null) {
            physics2.addObj(gameActor.getPhy2Obj());
            gameActor.getPhy2Obj().setPhysics2(physics2); //*
        }
        
        gameActor.onAddedToGameWorld(this);
        
        return this;
    }
    
    public GameWorld addGameActor(GameActor gameActor, double x, double y) {
        return addGameActor(gameActor, new Vector2(x, y));
    }
    
    public GameWorld addGameActor(GameActor gameActor) {
        return addGameActor(gameActor, gameActor.getPos());
    }
    
    public void removeGameActor(GameActor gameActor) {
        gameActor.setGameWorld(null);
        
        gameActors.remove(gameActor);
        
        super.removeObject(gameActor);
        
        // actualizare Physics2
        if (physics2 != null && gameActor.getPhy2Obj() != null) {
            physics2.removeObj(gameActor.getPhy2Obj());
        }
        
        gameActor.onRemovedFromGameWorld(this);
    }
}
