/**
 * TODO: change name to Phy2Actor
 */

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Phy2Obj {
    private Vector2 pos = Vector2.ZERO;
    private double rot = 0.0;
    private double mass = 0.5;
    
    private Phy2Collider collider;
    private boolean isSensor = false;
    
    private Set<Phy2Attachment> attachments = new LinkedHashSet<Phy2Attachment>();
    
    private Physics2 physics2;
    
    private GameActor gameActor;
    
    
    public Phy2Obj() {}
    
    
    private void updateAttachment(Phy2Attachment attachment) {
        Phy2Obj phy2Obj = attachment.getPhy2Obj();
        
        // if (gameWorld == null) {
            // if (gameActor.gameWorld != null) {
                // gameActor.gameWorld.removeGameActor(gameActor);
            // }
            
            // return;
        // } else {
            // gameActor.setPos(pos.add(attachment.getOffset()));
            // gameActor.setRot(rot+attachment.getRot());
            
            // if (gameActor.gameWorld != gameWorld) {
                // gameWorld.addGameActor(gameActor);
            // }
        // }
        
        phy2Obj.setPos(pos.add(attachment.getOffset()));
        phy2Obj.setRot(rot+attachment.getRot());
    }
    
    private void updateAttachments() {
        for (Phy2Attachment attachment : attachments) { 
            updateAttachment(attachment);
        }
    }
    
    
    // EVENTS
    public void onCollidesWith(Phy2Obj other) {}
    public void onCollidedWith(Phy2Obj other) {}
    
    
    public double getX() {
        return pos.getX();
    }
    
    public double getY() {
        return pos.getY();
    }
    
    public Vector2 getPos() {
        return pos;
    }
    
    public double getRot() {
        return rot;
    }
    
    public double getMass() {
        return mass;
    }
    
    public Phy2Collider getCollider() {
        return collider;
    }
    
    public boolean isSensor() {
        return isSensor;
    }
    
    public Set<Phy2Attachment> getAttachments() {
        return attachments;
    }
    
    public Physics2 getPhysics2() {
        return physics2;
    }
    
    public GameActor getGameActor() {
        return gameActor;
    }
    
    
    protected Phy2Obj setPos(Vector2 pos, boolean updateGameActor) {
        this.pos = pos;
        
        collider.setPos(pos);
        
        updateAttachments();
        
        if (updateGameActor && gameActor != null) {
            gameActor.setPos(pos, false);
        }
        
        return this;
    }
    
    public Phy2Obj setPos(Vector2 pos) {
        return setPos(pos, true);
    }
    
    protected Phy2Obj setPos(double x, double y, boolean updateGameActor) {
        return setPos(new Vector2(x, y), updateGameActor);
    }
    
    public Phy2Obj setPos(double x, double y) {
        return setPos(x, y, true);
    }
    
    public Phy2Obj addPos(Vector2 deltaPos) {
        return setPos(pos.add(deltaPos));
    }
    
    public Phy2Obj setRot(double rot) {
        this.rot = rot;
        
        collider.setRot(rot);
        
        return this;
    }
    
    public Phy2Obj setMass(double mass) {
        this.mass = mass;
        
        return this;
    }
    
    public Phy2Obj setCollider(Phy2Collider collider) {
        this.collider = collider;
        
        return this;
    }
    
    public Phy2Obj setIsSensor(boolean isSensor) {
        this.isSensor = isSensor;
        
        return this;
    }
    
    protected Phy2Obj addAttachment(Phy2Attachment attachment, boolean updateAttachment) {
        attachments.add(attachment);
        
        if (updateAttachment) {
            attachment.setAttachedTo(this, false);
        }
        
        updateAttachment(attachment);
        
        return this;
    }
    
    public Phy2Obj addAttachment(Phy2Attachment attachment) {
        return addAttachment(attachment, true);
    }
    
    protected Phy2Obj removeAttachment(Phy2Attachment attachment, boolean updateAttachment) {
        attachments.remove(attachment);
        
        if (updateAttachment) {
            attachment.setAttachedTo(null, false);
        }
        
        return this;
    }
    
    public Phy2Obj removeAttachment(Phy2Attachment attachment) {
        return removeAttachment(attachment, true);
    }
    
    protected Phy2Obj setPhysics2(Physics2 physics2) {
        this.physics2 = physics2;
        
        return this;
    }
    
    protected Phy2Obj setGameActor(GameActor gameActor, boolean updateGameActor) {
        this.gameActor = gameActor;
        
        if (updateGameActor) {
            gameActor.setPhy2Obj(this, false);
        }
        
        return this;
    }
    
    public Phy2Obj setGameActor(GameActor gameActor) {
        return setGameActor(gameActor, true);
    }
}
