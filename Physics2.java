/**
 * TODO: Change name to Phy2World
 */

import java.util.ArrayList;
import java.util.List;

import greenfoot.GreenfootSound;

public class Physics2 {
    private List<Phy2Obj> objs = new ArrayList<Phy2Obj>();
    
    // TODO: change these to Set instead of List
    private List<Phy2Dynamic> dynamics = new ArrayList<Phy2Dynamic>();
    private List<Phy2Static> statics = new ArrayList<Phy2Static>();
    
    private List<Phy2Obj> rayGuides = new ArrayList<Phy2Obj>();
    
    private List<Phy2ColInfo> colInfos = new ArrayList<Phy2ColInfo>();
    
    private int movingObjs = 0;
    
    private GameWorld gameWorld;
    
    
    public Physics2 init() {
        for (Phy2Obj obj : objs) {
            if (obj instanceof Phy2Dynamic) dynamics.add((Phy2Dynamic)obj);
            else if (obj instanceof Phy2Static) statics.add((Phy2Static)obj);
        }
        
        // imperechem fiecare dynamic cu fiecare static in parte
        for (int i = 0; i < dynamics.size(); ++i) {
            for (int j = 0; j < statics.size(); ++j) {
                Phy2ColInfo colInfo = new Phy2ColInfo(dynamics.get(i), statics.get(j));
                
                colInfos.add(colInfo);
            }
        }
        
        // imperechem toate dynamicele intre ele
        for (int i = 0; i < dynamics.size(); ++i) {
            for (int j = i+1; j < dynamics.size(); ++j) {
                Phy2ColInfo colInfo = new Phy2ColInfo(dynamics.get(i), dynamics.get(j));
                
                colInfos.add(colInfo);
            }
        }
        
        Game.getInstance().printDebugLine("Physics2 initialized.");
        
        return this;
    }
    
    // de cate ori pe millisecunda sa se roteasca texturile bilelor
    private static double TEX_BALL_ROTATION_RATE = GameWorld.REF_APMS;
    
    private double texBallRotationCounter = 0.0;
    
    public Physics2 step() {
        double actCount = gameWorld.getActDelta()*GameWorld.REF_APMS;
        
        texBallRotationCounter += gameWorld.getActDelta()*TEX_BALL_ROTATION_RATE;
        
        // actualizeaza dynamicele
        for (int i = 0; i < dynamics.size(); ++i) {
            Phy2Dynamic dynamic = dynamics.get(i);
            
            if (dynamic.isMoving()) {
                Vector2 dir = dynamic.getDir();
                
                dynamic.addVel(dynamic.getFriction().mul(actCount));
                
                // oprim fortat corpul cand viteza devine isi schimba sensul pentru
                // ca acesta sa nu ramana intr-un stadiu in care "se misca" continuu cu o viteza foarte mica;
                if (dynamic.getVel().dot(dir) < 0) {
                    dynamic.setVel(Vector2.ZERO);
                    
                    Game.getInstance().printDebugLine("Clamped movement for " + dynamic);
                }
                
                dynamic.addVel(dynamic.getAccel().mul(actCount));
                
                dynamic.addPos(dynamic.getVel().mul(actCount));
                
                if (dynamic.getGameActor() instanceof TexBall) {
                    TexBall texBall = (TexBall)dynamic.getGameActor();
                    
                    Vector3 rotAxis = new Vector3(-dir.getY(), dir.getX(), 0);
                    Quaternion rot = Quaternion.rot(dynamic.getVel().getLength()/texBall.getRadius()*actCount, rotAxis);
                    
                    boolean updateImage = (texBallRotationCounter >= 1.0);
                    texBall.addTexRot(rot, updateImage);
                }
            }
        }
        
        for (Phy2ColInfo colInfo : colInfos) {
            colInfo.check();
        }
        
        for (Phy2ColInfo colInfo : colInfos) {
            if (colInfo.isCollision()) {
                Phy2Obj obj1 = colInfo.getObj1();
                Phy2Obj obj2 = colInfo.getObj2();
                
                onObjsCollide(obj1, obj2);
                
                obj1.onCollidesWith(obj2);
                obj2.onCollidesWith(obj1);
                
                if (!(obj1.isSensor() || obj2.isSensor())) {
                    colInfo.handle();
                }
                
                onObjsCollided(obj1, obj2);
                
                obj1.onCollidedWith(obj2);
                obj2.onCollidedWith(obj1);
            }
        }
        
        if (gameWorld != null) {
            for (Phy2Obj obj : objs) {
                Phy2Collider collider = obj.getCollider();
                
                if (collider != null) {
                    collider.drawDebug(gameWorld.getOverlay());
                }
            }
            
            // for (Phy2Obj obj : rayGuides) { //*
                // Phy2Collider collider = obj.getCollider();
                
                // if (collider != null) {
                    // collider.drawDebug(gameWorld.getOverlay());
                // }
            // }
            
            for (Phy2ColInfo colInfo : colInfos) {
                colInfo.drawDebug(gameWorld.getOverlay());
            }
        }
        
        if (texBallRotationCounter >= 1.0) {
            texBallRotationCounter = 0.0;
        }
        
        return this;
    }
    
    
    // EVENTS
    protected void onObjsCollide(Phy2Obj obj1, Phy2Obj obj2) {
        
    }
    
    protected void onObjsCollided(Phy2Obj obj1, Phy2Obj obj2) {
        if (obj1.getGameActor() instanceof Ball && obj2.getGameActor() instanceof Ball
        && obj1 instanceof Phy2Dynamic && obj2 instanceof Phy2Dynamic) {
            Phy2Dynamic dynamic1 = (Phy2Dynamic)obj1;
            Phy2Dynamic dynamic2 = (Phy2Dynamic)obj2;
            
            if (dynamic1.isMoving() && dynamic2.isMoving()) {
                int sound_i;
                GreenfootSound sound;
                
                sound_i = (int)Math.round(Math.random()*(Session.BALL_BALL_SOUNDS.length-1));
                sound = Session.BALL_BALL_SOUNDS[sound_i];
                
                sound.play();
            }
            
        } else if (obj1.getGameActor() instanceof Ball && obj2.getGameActor() instanceof Cushion
        && obj1 instanceof Phy2Dynamic && obj2 instanceof Phy2Static) {
            Phy2Dynamic dynamic1 = (Phy2Dynamic)obj1;
            
            if (dynamic1.isMoving()) {
                int sound_i;
                GreenfootSound sound;
                
                sound_i = (int)Math.round(Math.random()*(Session.CUSHION_BALL_SOUNDS.length-1));
                sound = Session.CUSHION_BALL_SOUNDS[sound_i];
                
                sound.play();
            }
            
        // } else if (obj1.getGameActor() instanceof Cushion && obj2.getGameActor() instanceof Ball
        // && obj1 instanceof Phy2Static && obj2 instanceof Phy2Dynamic) { System.out.println("reached");
            // Phy2Dynamic dynamic2 = (Phy2Dynamic)obj2;
            
            // if (dynamic2.isMoving()) {
                // int sound_i;
                // GreenfootSound sound;
                
                // sound_i = (int)Math.round(Math.random()*(Session.CUSHION_BALL_SOUNDS.length-1));
                // sound = Session.CUSHION_BALL_SOUNDS[sound_i];
                
                // sound.play();
            // }
            
        } else if (obj1.getGameActor() instanceof Ball && obj2.getGameActor() instanceof Pocket
        && obj1 instanceof Phy2Dynamic && obj2 instanceof Phy2Static) {
            Phy2Dynamic dynamic1 = (Phy2Dynamic)obj1;
            
            if (dynamic1.isMoving()) {
                int sound_i;
                GreenfootSound sound;
                
                sound_i = (int)Math.round(Math.random()*(Session.POCKET_BALL_SOUNDS.length-1));
                sound = Session.POCKET_BALL_SOUNDS[sound_i];
                
                sound.play();
            }
        }
    }
    
    
    public Physics2 addObj(Phy2Obj obj) {
        objs.add(obj);
        
        for (Phy2Attachment attachment : obj.getAttachments()) {
            rayGuides.add(attachment.getPhy2Obj()); //*
        }
        
        return this;
    }
    
    public Physics2 removeObj(Phy2Obj obj) {
        objs.remove(obj);
        
        return this;
    }
    
    
    public void incMovingObjs() {
        ++movingObjs;
    }
    
    public void decMovingObjs() {
        --movingObjs;
    }
    
    public int getMovingObjs() {
        return movingObjs;
    }
    
    public List<Phy2Obj> getObjs() {
        return objs;
    }
    
    public GameWorld getGameWorld() {
        return gameWorld;
    }
    
    
    protected Physics2 setGameWorld(GameWorld gameWorld, boolean updateGameWorld) {
        this.gameWorld = gameWorld;
        
        if (updateGameWorld) {
            gameWorld.setPhysics2(this, false);
        }
        
        return this;
    }
    
    protected Physics2 setGameWorld(GameWorld gameWorld) {
        return setGameWorld(gameWorld, true);
    }
}
