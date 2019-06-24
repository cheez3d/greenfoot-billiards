/**
 * Write a description of class Attachment here.
 * 
 * @version 2018-02-10
 */

public class Attachment {
    private Vector2 offset = Vector2.ZERO;
    private double rot = 0.0;
    
    private GameActor gameActor;
    
    private GameActor attachedTo;
    
    
    public Attachment() {}
    
    
    public Vector2 getOffset() {
        return offset;
    }
    
    public double getRot() {
        return rot;
    }
    
    public GameActor getGameActor() {
        return gameActor;
    }
    
    public GameActor getAttachedTo() {
        return attachedTo;
    }
    
    
    public Attachment setOffset(Vector2 offset) {
        this.offset = offset;
        
        if (gameActor != null) {
            gameActor.setPos(attachedTo.getPos().add(offset));
        }
        
        return this;
    }
    
    public Attachment setOffset(double x, double y) {
        return setOffset(new Vector2(x, y));
    }
    
    public Attachment setRot(double rot) {
        this.rot = rot;
        
        return this;
    }
    
    public Attachment setGameActor(GameActor gameActor) {
        GameActor prevGameActor = gameActor;
        
        this.gameActor = gameActor;
        
        return this;
    }
    
    protected Attachment setAttachedTo(GameActor attachedTo, boolean updateAttachedTo) {
        GameActor prevAttachedTo = this.attachedTo;
        
        this.attachedTo = attachedTo;
        
        if (prevAttachedTo != null) {
            prevAttachedTo.removeAttachment(this, false);
        }
        
        if (updateAttachedTo && attachedTo != null) {
            attachedTo.addAttachment(this, false);
        }
        
        return this;
    }
    
    public Attachment setAttachedTo(GameActor attachedTo) {
        return setAttachedTo(attachedTo, true);
    }
}
