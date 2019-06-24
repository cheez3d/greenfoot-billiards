/**
 * Clasa intermediara folosita pentru implementarea de noi functionalitati si
 * pentru rezolvarea anumitor probleme intalnite pe parcursul dezvoltarii aplicatiei.
 */

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.LinkedHashSet;
import java.util.Set;

import greenfoot.Actor;
import greenfoot.GreenfootImage;

public abstract class GameActor extends Actor {
    public static final double REF_APMS = 0.06;
    
    public enum PaintMethod { GREENFOOT, OVERLAY }
    
    
    private static final GreenfootImage IMG_PLACEHOLDER = new GreenfootImage(1, 1);
    
    
    private long actDelta;
    
    private int x, y; // coordonatele memorate inaintea apelarii metodei patchLocation
    private Vector2 pos = new Vector2(); // pozitia mai exacta (double), stocata intr-un vector
    private int rotation = 0; // rotatia in grade memorata inaintea apelarii metodei patchRotation
    private double rot = 0; // rotatia memorata in 'rotation', dar exprimata in radiani
    
    private GameWorld gameWorld;
    private PaintMethod paintMethod = PaintMethod.GREENFOOT;
    
    private Set<Attachment> attachments = new LinkedHashSet<Attachment>();
    
    private Phy2Obj phy2Obj;
    
    
    public GameActor() {}
    
    
    private long lastActTime = System.currentTimeMillis();
    private AffineTransform transf = new AffineTransform();
    @Override
    public void act() {
        long actTime = System.currentTimeMillis();
        
        actDelta = actTime-lastActTime;
        
        lastActTime = actTime;
        
        switch (paintMethod) {
            case GREENFOOT: {
                break;
            }
            
            case OVERLAY: {
                if (gameWorld != null) {
                    transf.setToTranslation(pos.getX(), pos.getY());
                    transf.rotate(rot);
                    
                    gameWorld.getOverlay().drawImageCentered(img, transf);
                }
                
                break;
            }
        }
    }
    
    
    /**
     * Metoda folosita pentru a rezolva problema actorilor care au imagini cu dimensiuni numere pare;
     * Problema se manifesta atunci cand actorul este rotit la un unghi multiplu de 90 de grade (90, 180, 270 etc.), exceptie facand 0 grade (in cazul acesta nu este rotit).
     * Cand este rotit in condita de mai sus, imaginea actorului este pozitionata pe x, pe y, sau pe ambele axe cu un pixel mai jos fata de cat ar trebui.
     * Acest lucru se datoreaza faptului ca dimensiunile imaginii fiind pare, centrul de rotatie este pozitionat pe pixelul din dreapta jos fata de centru.
     *
     * Evalueaza felul in care este rotit actorul si ii modifica pozitia in consecinta, pentru rezolvarea problemei pozitionarii.
     * Valorile cu care se modifica pozitia au fost alese empiric, prin observarea felului in care se pozitioneaza imaginile
     * cushonurilor in raport cu glow-ul mesei atunci cand sunt rotite.
     */
    private void patchLocation() {
        if (gameWorld != null) {
            GreenfootImage img = getImage();
            
            int width = img.getWidth();
            int height = img.getHeight();
            
            if (width%2 == 0 || height%2 == 0) {
                // folosim floorMod si nu '%' deoarece '%' poate returna rezultate negative
                // atunci cand unghiul de rotatie e negativ
                switch (Math.floorMod(rotation, 360)) {
                    case  90: super.setLocation(x-1,   y); break;
                    case 180: super.setLocation(x-1, y-1); break;
                    case 270: super.setLocation(  x, y-1); break;
                    
                    default: super.setLocation(x, y);
                }
            } else {
                super.setLocation(x, y);
            }
        }
    }
    
    /**
     * Momentan aceasta metoda nu schimba nimic.
     */
    private void patchRotation() {
        super.setRotation(rotation);
    }
    
    private void updateAttachment(Attachment attachment) {
        GameActor gameActor = attachment.getGameActor();
        
        if (gameWorld == null) {
            if (gameActor.gameWorld != null) {
                gameActor.gameWorld.removeGameActor(gameActor);
            }
            
            return;
        } else {
            gameActor.setPos(pos.add(attachment.getOffset()));
            gameActor.setRot(rot+attachment.getRot());
            
            if (gameActor.gameWorld != gameWorld) {
                gameWorld.addGameActor(gameActor);
            }
        }
    }
    
    private void updateAttachments() {
        for (Attachment attachment : attachments) { 
            updateAttachment(attachment);
        }
    }
    
    // EVENTS
    protected void onAddedToGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        
        updateAttachments();
    }
    
    protected void onRemovedFromGameWorld(GameWorld gameWorld) {
        this.gameWorld = null;
        
        updateAttachments();
        
        if (phy2Obj != null && gameWorld.getPhysics2() != null) {
            gameWorld.getPhysics2().removeObj(phy2Obj);
        }
    }
    
    
    public long getActDelta() {
        return actDelta;
    }
    
    public Set<Attachment> getAttachments() {
        return attachments;
    }
    
    /**
     * Returneaza coordonata x memorata inaintea chemarii metodei patchLocation.
     */
    @Override
    public int getX() {
        return x;
    }
    
    /**
     * Returneaza coordonata y memorata inaintea apelarii metodei patchLocation.
     */
    @Override
    public int getY() {
        return y;
    }
    
    public Vector2 getPos() {
        return pos;
    }
    
    /**
     * Returneaza rotatia memorata inaintea apelarii metodei patchRotation.
     */
    @Override
    public int getRotation() {
        return rotation;
    }
    
    /**
     * Returneaza echivalentul lui 'rotation' in radiani.
     */
    public double getRot() {
        return rot;
    }
    
    public GameWorld getGameWorld() {
        return gameWorld;
    }
    
    public PaintMethod getPaintMethod() {
        return paintMethod;
    }
    
    public int getTransparency() {
        return getImage().getTransparency();
    }
    
    public Phy2Obj getPhy2Obj() {
        return phy2Obj;
    }
    
    
    // /**
     // * Intercepteaza toate apelurile catre setLocation pentru pozitionarea actorilor.
     // */
    // @Override
    // public void setLocation(int x, int y) {
        // System.out.println(String.format("%d %d", x, y));
        
        // this.x = x;
        // this.y = y;
        
        // pos.set(x, y);
        
        // patchLocation();
        
        // updateAttachments();
        
        // if (phy2Obj != null) phy2Obj.setPos(x, y);
    // }
    
    protected GameActor setPos(Vector2 pos, boolean updatePhy2Obj) {
        this.pos = pos;
        
        this.x = pos.getIntX();
        this.y = pos.getIntY();
        
        patchLocation();
        
        updateAttachments();
        
        if (updatePhy2Obj && phy2Obj != null) {
            phy2Obj.setPos(pos, false);
        }
        
        return this;
    }
    
    public GameActor setPos(Vector2 pos) {
        return setPos(pos, true);
    }
    
    protected GameActor setPos(double x, double y, boolean updatePhy2Obj) {
        return setPos(new Vector2(x, y), updatePhy2Obj);
    }
    
    public GameActor setPos(double x, double y) {
        return setPos(x, y, true);
    }
    
    // /**
     // * Intercepteaza toate apelurile catre setRotation pentru rotirea actorilor.
     // */
    // @Override
    // public void setRotation(int rotation) {
        // System.out.println(rotation);
        
        // this.rotation = rotation;
        
        // this.rot = Math.toRadians(rotation);
        
        // patchLocation();
        // patchRotation();
        
        // if (phy2Obj != null) phy2Obj.setRot(rot);
    // }
    
    /**
     * Seteaza rotatia actorului in radiani.
     */
    public GameActor setRot(double rot) {
        this.rot = rot;
        
        this.rotation = (int)Math.round(Math.toDegrees(rot));
        
        patchLocation();
        patchRotation();
        
        updateAttachments();
        
        if (phy2Obj != null) { 
            phy2Obj.setRot(rot);
        }
        
        return this;
    }
    
    protected GameActor setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        
        return this;
    }
    
    private GreenfootImage greenfootImg;
    private BufferedImage img;
    public GameActor setPaintMethod(PaintMethod paintMethod) {
        if (paintMethod == this.paintMethod) return this;
        
        this.paintMethod = paintMethod;
        
        switch (paintMethod) {
            case GREENFOOT: {
                setImage(greenfootImg);
                
                break;
            }
            
            case OVERLAY: {
                greenfootImg = getImage();
                img = greenfootImg.getAwtImage();
                
                setImage(IMG_PLACEHOLDER);
                
                break;
            }
        }
        
        for (Attachment attachment : attachments) {
            attachment.getGameActor().setPaintMethod(paintMethod);
        }
        
        return this;
    }
    
    public GameActor setTransparency(int transparency) {
        getImage().setTransparency(transparency);
        
        return this;
    }
    
    protected GameActor setPhy2Obj(Phy2Obj phy2Obj, boolean updatePhy2Obj) {
        this.phy2Obj = phy2Obj;
        
        if (updatePhy2Obj) {
            phy2Obj.setGameActor(this, false);
        }
        
        return this;
    }
    
    protected GameActor setPhy2Obj(Phy2Obj phy2Obj) {
        return setPhy2Obj(phy2Obj, true);
    }
    
    protected GameActor addAttachment(Attachment attachment, boolean updateAttachment) {
        attachments.add(attachment);
        
        if (updateAttachment) {
            attachment.setAttachedTo(this, false);
        }
        
        updateAttachment(attachment);
        
        return this;
    }
    
    public GameActor addAttachment(Attachment attachment) {
        return addAttachment(attachment, true);
    }
    
    protected GameActor removeAttachment(Attachment attachment, boolean updateAttachment) {
        attachments.remove(attachment);
        
        if (updateAttachment) {
            attachment.setAttachedTo(null, false);
        }
        
        return this;
    }
    
    public GameActor removeAttachment(Attachment attachment) {
        return removeAttachment(attachment, true);
    }
}
