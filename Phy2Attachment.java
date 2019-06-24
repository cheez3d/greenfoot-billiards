/**
 * Write a description of class Phy2Attachment here.
 */

public class Phy2Attachment {
    private Vector2 offset = Vector2.ZERO;
    private double rot = 0.0;
    
    private Phy2Obj phy2Obj;
    
    private Phy2Obj attachedTo;
    
    
    public Phy2Attachment() {}
    
    
    public Vector2 getOffset() {
        return offset;
    }
    
    public double getRot() {
        return rot;
    }
    
    public Phy2Obj getPhy2Obj() {
        return phy2Obj;
    }
    
    public Phy2Obj getAttachedTo() {
        return attachedTo;
    }
    
    
    public Phy2Attachment setOffset(Vector2 offset) {
        this.offset = offset;
        
        if (phy2Obj != null) {
            phy2Obj.setPos(attachedTo.getPos().add(offset));
        }
        
        return this;
    }
    
    public Phy2Attachment setOffset(double x, double y) {
        return setOffset(new Vector2(x, y));
    }
    
    public Phy2Attachment setRot(double rot) {
        this.rot = rot;
        
        return this;
    }
    
    public Phy2Attachment setPhy2Obj(Phy2Obj phy2Obj) {
        this.phy2Obj = phy2Obj;
        
        return this;
    }
    
    protected Phy2Attachment setAttachedTo(Phy2Obj attachedTo, boolean updateAttachedTo) {
        Phy2Obj prevAttachedTo = this.attachedTo;
        
        this.attachedTo = attachedTo;
        
        if (prevAttachedTo != null) {
            prevAttachedTo.removeAttachment(this, false);
        }
        
        if (updateAttachedTo && attachedTo != null) {
            attachedTo.addAttachment(this, false);
        }
        
        return this;
    }
    
    public Phy2Attachment setAttachedTo(Phy2Obj attachedTo) {
        return setAttachedTo(attachedTo, true);
    }
}
