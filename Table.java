import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import greenfoot.Actor;
import greenfoot.World;

public class Table extends GameActor {
    private int width  = 1400;
    private int height = 800;
    private int margin = 48;
    
    private int cushionHeight = 16;
    private double cushionAngle1 = Math.PI/4;
    private double cushionAngle2 = Math.toRadians(70);
    
    private int cornerPocketRadius = 32;
    private int middlePocketRadius = 22;
    
    private Base base = new Base()
        .setCornerRadius(64)
        .setColor(new Color(101, 66, 32));
    private Attachment baseAttachment = new Attachment()
        .setGameActor(base)
        .setAttachedTo(this);
    
    private Felt felt = new Felt()
        .setColor1(new Color(0, 150, 170))
        .setColor2(new Color(0, 70, 70));
    private Attachment feltAttachment = new Attachment()
        .setGameActor(felt)
        .setAttachedTo(this);
    
    private Pocket[] pockets = new Pocket[6];
    private Attachment[] pocketAttachments = new Attachment[pockets.length];
    
    private Cushion[] cushions = new Cushion[6];
    private Attachment[] cushionAttachments = new Attachment[cushions.length];
    
    private Area innerArea;
    
    private Glow glow = new Glow()
        .setSize(4);
    private Attachment glowAttachment = new Attachment()
        .setGameActor(glow)
        .setAttachedTo(this);;
    
    
    public Table() {
        for (int i = 0; i < cushions.length; ++i) {
            Cushion cushion = new Cushion();
            
            cushion.setHeight(cushionHeight);
            
            cushions[i] = cushion;
            
            Attachment cushionAttachment = new Attachment();
                cushionAttachment.setGameActor(cushion);
                cushionAttachment.setAttachedTo(this);
                
            cushionAttachments[i] = cushionAttachment;
        }
        
        cushions[0].setAngle1(cushionAngle2); cushions[0].setAngle2(cushionAngle1);
        cushions[1].setAngle1(cushionAngle1); cushions[1].setAngle2(cushionAngle2);
        cushions[2].setAngle1(cushionAngle1); cushions[2].setAngle2(cushionAngle1);
        cushions[3].setAngle1(cushionAngle2); cushions[3].setAngle2(cushionAngle1);
        cushions[4].setAngle1(cushionAngle1); cushions[4].setAngle2(cushionAngle2);
        cushions[5].setAngle1(cushionAngle1); cushions[5].setAngle2(cushionAngle1);
        
        for (int i = 0; i < pockets.length; ++i) {
            Pocket pocket = new Pocket();
            
            if (i == 1 || i == 4) pocket.setRadius(middlePocketRadius);
            else pocket.setRadius(cornerPocketRadius);
            
            pockets[i] = pocket;
            
            Attachment pocketAttachment = new Attachment()
                .setGameActor(pocket)
                .setAttachedTo(this);
                
            pocketAttachments[i] = pocketAttachment;
        }
        
        cushionAttachments[0].setRot(Math.PI);
        cushionAttachments[1].setRot(Math.PI);
        cushionAttachments[2].setRot(-Math.PI/2);
        cushionAttachments[3].setRot(0);
        cushionAttachments[4].setRot(0);
        cushionAttachments[5].setRot(Math.PI/2);
        
        Attachment glowAttachment = new Attachment();
        glowAttachment.setGameActor(glow);
        glowAttachment.setAttachedTo(this);
        
        updateComponents();
    }
    
    
    private void updateComponents() {
        base.setSize(width, height);
        felt.setSize(width-2*margin, height-2*margin);
        
        int horizontalCushionWidth = width/2-margin-cornerPocketRadius-middlePocketRadius;
        int verticalCushionWidth = height-2*margin-2*cornerPocketRadius;
        
        for (int i = 0; i < cushions.length; ++i) {
            Cushion cushion = cushions[i];
            
            if (i == 2 || i == 5) cushion.setWidth(verticalCushionWidth);
            else cushion.setWidth(horizontalCushionWidth);
        }
        
        cushionAttachments[0].setOffset(-width/2+margin+pockets[0].getRadius()+cushions[0].getWidth()/2, -height/2+margin+cushions[0].getHeight()/2);
        cushionAttachments[1].setOffset(width/2-margin-pockets[2].getRadius()-cushions[1].getWidth()/2, -height/2+margin+cushions[0].getHeight()/2);
        cushionAttachments[2].setOffset(width/2-margin-cushions[2].getHeight()/2, 0);
        cushionAttachments[3].setOffset(width/2-margin-pockets[3].getRadius()-cushions[3].getWidth()/2, height/2-margin-cushions[0].getHeight()/2);
        cushionAttachments[4].setOffset(-width/2+margin+pockets[5].getRadius()+cushions[4].getWidth()/2, height/2-margin-cushions[0].getHeight()/2);
        cushionAttachments[5].setOffset(-width/2+margin+cushions[5].getHeight()/2, 0);
        
        pocketAttachments[0].setOffset(-width/2+margin, -height/2+margin);
        pocketAttachments[1].setOffset(0, -height/2+margin);
        pocketAttachments[2].setOffset(width/2-margin, -height/2+margin);
        pocketAttachments[3].setOffset(width/2-margin, +height/2-margin);
        pocketAttachments[4].setOffset(0, height/2-margin);
        pocketAttachments[5].setOffset(-width/2+margin, height/2-margin);
        
        // suprafata care va ramane dupa ce o scadem pe cea acoperita de pocket-uri si cushion-uri
        innerArea = new Area(felt.getShape());
        
        for (int i = 0; i < cushions.length; ++i) {
            Cushion cushion = cushions[i];
            
            Area cushionArea = new Area(cushions[i].getShape());
            
            AffineTransform transf = new AffineTransform();
            transf.setToTranslation(-cushion.getWidth()/2, -cushion.getHeight()/2); // translatam area-ul la punctul de rotatie (centrul cushionului)
            cushionArea.transform(transf);
            transf.setToRotation(cushion.getRot()); // rotim area-ul
            cushionArea.transform(transf);
            transf.setToTranslation(cushion.getWidth()/2, cushion.getHeight()/2); // translatam area-ul inapoi unde era la inceput
            cushionArea.transform(transf);
            
            // translatam area-ul astfel incat sa se suprapuna pe cushionul propriu-zis
            transf.setToTranslation(
                -margin-(getX()-width/2)+(cushion.getX()-cushion.getWidth()/2),
                -margin-(getY()-height/2)+(cushion.getY()-cushion.getHeight()/2)
            );
            
            cushionArea.transform(transf);
            
            innerArea.subtract(cushionArea);
        }
        
        for (int i = 0; i < pockets.length; ++i) {
            Pocket pocket = pockets[i];
            
            Area pocketArea = new Area(pockets[i].getShape());
            
            // translatam area-ul astfel incat sa se suprapuna peste pocketul propriu-zis
            AffineTransform transf = new AffineTransform();
            transf.setToTranslation(
                (-margin-(getX()-width/2))+(pocket.getX()-pocket.getRadius()),
                (-margin-(getY()-height/2))+(pocket.getY()-pocket.getRadius())
            );
            pocketArea.transform(transf);
            
            innerArea.subtract(pocketArea);
        }
        
        glow.setShape(innerArea);
    }
    
    
    @Override
    protected void onAddedToGameWorld(GameWorld gameWorld) {
        updateComponents();
    }
    
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    
    public Table setWidth(int width) {
        this.width = width;
        
        updateComponents();
        
        return this;
    }
    
    public Table setHeight(int height) {
        this.height = height;
        
        updateComponents();
        
        return this;
    }
}
