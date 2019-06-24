import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.DataBufferInt;
import java.awt.Font;
import java.awt.FontMetrics;
// import java.awt.font.LineMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import greenfoot.GreenfootImage;

// import com.jhlabs.image.BoxBlurFilter;
// import com.jhlabs.image.GaussianFilter;

public class TexBall extends Ball {
    public enum Type { CUE, SOLID, STRIPE }
    
    
    private static final double CIRCLE_SCALE = 0.35;
    private static final double STRIPE_SCALE = 0.4;
    
    // de cate ori mai mare sa fie textura fata de diametrul bieli
    // valori mici ofera performanta buna, insa lasa de dorit d.p.d.v. vizual
    // valori mari ofera detalii fine, insa lasa de dorit d.p.d.v al performantei
    private static final double TEX_SCALE = 1.25;
    // numarul maxim de pixeli peste care se poate sari, cu cat ne apropiem mai mult de poli;
    // citeste mai jos, unde este calculata variabila inc, pentru mai multe detalii;
    // valoarea 4 a fost aleasa empiric
    private static final int TEX_POLLING_MAX_INC = 4;
    
    private static final double TEXT_DIAMETER_SCALE = 0.75;
    
    // private static final BoxBlurFilter BLUR = new BoxBlurFilter(1, 1, 1);
    
    
    private Quaternion texRot = Quaternion.rot(Math.PI/2, 1, 0, 0);
    private Type type = Type.SOLID;
    private String text = "8";
    
    
    public TexBall() {
        createTexture();
        updateFont();
        drawTexture();
        
        createImage();
        drawImage();
    }
    
    
    private GreenfootImage greenfootTex; // TODO: after everything is working, change this to a BufferedImage instead
    private double texSize;
    private double texWidth, texHeight;
    private int texIntWidth, texIntHeight;
    private double texStartX, texStartY;
    private BufferedImage tex;
    private Graphics2D texG2d;
    private int[] texBuf;
    
    private double txtSize;
    private int txtIntSize;
    private BufferedImage txt;
    private Graphics2D txtG2d;
    
    private void createTexture() {
        // textura este de PI ori mai mare decat sfera finala fiindca astfel se acopera
        // toti pixelii, neramanand "gauri" in sfera;
        // am ales PI deoarece circumferinta maxima, adica cea de la ecuator este PI*diameter
        texSize = TEX_SCALE*Math.PI*getDiameter();
        
        // nu cream o textura de dimensiuni texSize pe texSize, ci o textura ce are dimensiunile
        // stric neceare, pentru a economisi din cicluri de calcul atunci cand sferizam textura;
        // variabilele texWidth si texHeight arata dimensiunile pe care le are, de fapt, textura,
        // iar variabilele texStartX si texStartY arata offsetul, unde s-ar aflat de fapt
        // continutul texturii daca ar fi pozitionat intr-o textura de dimensiuni texSize pe texSize
        switch (type) {
            case CUE: {
                texWidth = 0.25*CIRCLE_SCALE*(texSize/2);
                texHeight = 0.25*CIRCLE_SCALE*texSize;
                
                texStartX = (texSize/2-0.25*CIRCLE_SCALE*(texSize/2))/2;
                texStartY = (texSize-0.25*CIRCLE_SCALE*texSize)/2;
                
                break;
            }
            
            case SOLID: {
                // folosim texSize/2 pe axa x pentru ca cercul sa fie turtit pe lungime,
                // datorita modului in care se infasoara textura (ce este un patrat) pe sfera;
                // dupa sferizare cercul va arata normal
                texWidth = CIRCLE_SCALE*(texSize/2);
                texHeight = CIRCLE_SCALE*texSize;
                
                texStartX = (texSize/2-CIRCLE_SCALE*(texSize/2))/2;
                texStartY = (texSize-CIRCLE_SCALE*texSize)/2;
                
                break;
            }
            
            case STRIPE: {
                texWidth = texSize;
                texHeight = STRIPE_SCALE*texSize;
                
                texStartX = 0;
                texStartY = (texSize-STRIPE_SCALE*texSize)/2;
                
                break;
            }
        }
        
        texIntWidth = (int)Math.round(texWidth);
        texIntHeight = (int)Math.round(texHeight);
        
        greenfootTex = new GreenfootImage(texIntWidth, texIntHeight);
        tex = greenfootTex.getAwtImage();
        texG2d = tex.createGraphics();
        texG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        texG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        texBuf = ((DataBufferInt)(tex.getRaster().getDataBuffer())).getData();
        
        txtSize = CIRCLE_SCALE*texSize;
        txtIntSize = (int)Math.round(txtSize);
        txt = new BufferedImage(txtIntSize, txtIntSize, BufferedImage.TYPE_INT_ARGB);
        txtG2d = txt.createGraphics();
        txtG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        txtG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
    }
    
    private void updateFont() {
        int fontSize = (int)Math.round(TEX_SCALE*TEXT_DIAMETER_SCALE*getDiameter());
        Font font;
        
        try {
            font = FontCache.getFont("montserrat-regular", Font.BOLD, fontSize);
        }
        catch (Exception e) {
            font = new Font(Font.DIALOG, Font.BOLD, fontSize);
        }
        
        txtG2d.setFont(font);
    }
    
    private void drawTexture() {
        FontMetrics fm = txtG2d.getFontMetrics();
        // LineMetrics lm = fm.getLineMetrics(text, txtG2d);
        
        txtG2d.setComposite(AlphaComposite.Clear);
        txtG2d.fillRect(0, 0, txtIntSize, txtIntSize);
        txtG2d.setComposite(AlphaComposite.SrcOver);
        
        // // DEBUG
        // txtG2d.setPaint(Color.GREEN);
        // txtG2d.fillRect(0, 0, txtIntSize, txtIntSize);
        // // DEBUG
        
        txtG2d.setPaint(Color.BLACK);
        txtG2d.drawString(text, (int)Math.round((txtSize-fm.stringWidth(text))/2), (int)Math.round((txtSize+fm.getAscent())/2));
        
        texG2d.setComposite(AlphaComposite.Clear);
        texG2d.fillRect(0, 0, texIntWidth, texIntHeight);
        texG2d.setComposite(AlphaComposite.SrcOver);
        
        // // DEBUG
        // texG2d.setPaint(Color.GREEN);
        // texG2d.fillRect(0, 0, texIntWidth, texIntHeight);
        // // DEBUG
        
        switch (type) {
            case CUE: {
                Shape circle = new Ellipse2D.Double(0, 0, texWidth, texHeight);
                
                texG2d.setPaint(Color.RED);
                texG2d.fill(circle);
                
                break;
            }
            
            case SOLID: {
                Shape circle = new Ellipse2D.Double(0, 0, texWidth, texHeight);
                
                texG2d.setPaint(Color.WHITE);
                texG2d.fill(circle);
                
                // textul este desenat pe textura in oglinda datorita modului de functionare a algoritmului de sferizare;
                // dupa sferizare textul va arata normal
                texG2d.drawImage(txt, (int)Math.round(txtSize/2), 0, -(int)Math.round(txtSize/2), txtIntSize, null);
                
                break;
            }
                
            case STRIPE: {
                // folosim texSize/2 pe axa x pentru ca cercul sa fie turtit  pe lungime;
                // dupa sferizare cercul va arata normal
                Area circle = new Area(new Ellipse2D.Double(
                    (texWidth/2-CIRCLE_SCALE*(texSize/2))/2,
                    (texHeight-CIRCLE_SCALE*texSize)/2,
                    CIRCLE_SCALE*(texSize/2),
                    CIRCLE_SCALE*texSize
                ));
                
                // folosim dimensiunile intregi la desenarea dreptunghiului
                // pentru a nu avea margini semi-transparente din cauza anti-aliasing-ului;
                // daca textura are margini transparente => la proiectarea pe sfera, in locul unde se intalnesc cele doua
                // capete alte texturii se va vedea o linie
                Area stripe = new Area(new Rectangle2D.Double(0, 0, texIntWidth, texIntHeight));
                stripe.subtract(circle);
                
                texG2d.setPaint(getColor());
                texG2d.fill(stripe);
                
                // textul este desenat pe textura in oglinda datorita modului de functionare a algoritmului de sferizare;
                // dupa sferizare textul va arata normal
                texG2d.drawImage(
                    txt,
                    (int)Math.round((texWidth/2+txtSize/2)/2), (int)Math.round((texHeight-txtSize)/2),
                    -(int)Math.round(txtSize/2), (int)Math.round(txtSize),
                    null
                );
                
                break;
            }
        }
    }
    
    
    private double sphSize;
    private int sphIntSize;
    private BufferedImage sph /*, sphCopy*/;
    private Graphics2D sphG2d;
    private int[] sphBuf;
    
    protected void createImage() {
        super.createImage();
        
        sphSize = TEX_SCALE*getDiameter();
        sphIntSize  = (int)Math.round(sphSize);
        sph = new BufferedImage(sphIntSize, sphIntSize, BufferedImage.TYPE_INT_ARGB);
        sphG2d = sph.createGraphics();
        sphG2d.setRenderingHint(RenderingHints.KEY_RENDERING, Game.getInstance().getRendering());
        sphG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, Game.getInstance().getAntiAliasing());
        sphBuf = ((DataBufferInt)(sph.getRaster().getDataBuffer())).getData();
        
        // sphCopy = new BufferedImage(sphIntSize, sphIntSize, BufferedImage.TYPE_INT_ARGB);
    }
    
    @Override
    protected void drawImage() {
        super.drawImage();
        
        sphG2d.setComposite(AlphaComposite.Clear);
        sphG2d.fillRect(0, 0, sphIntSize, sphIntSize);
        sphG2d.setComposite(AlphaComposite.SrcOver);
        
        // maparea cu valoarea din radius ar rezulta intr-o imagine cu dimensiuni cu un pixel in plus (adica diameter+1 pe diameter+1)
        // pentru ca duce punctele intr-o multime cu coordonate intregi de la -radius la radius, multime ce il contine si pe 0 care e in plus;
        // de aceea scadem 0.5 din radius pentru ca la mapare sa rezulte dimensiuni de diameter pe diameter
        double r = TEX_SCALE*(getRadius()-0.5);
        
        for (int v = 0; v < texIntHeight; ++v) {
            double phi = Math.PI*((v+texStartY)/(texSize-1)); // phi apartine [0, PI]
            
            // pixelii se distribuie diferit in functie de latitudine;
            // astfel, catre poli nu e nevoie de o precizie la fel de mare deoarece
            // randurile respective de pixeli se concentreaza intr-un spatiu din ce in ce mai mic;
            // aceasta functie returneaza in zona ecuatorului un increment de 1, iar la deplasarea spre
            // poli incrementul se mareste; la poli, valoarea incrementului este data de TEX_POLLING_MAX_INC;
            // cu alte cuvinte, in zona polilor sarim peste pixeli
            // care oricum nu ar fi contribuit cu nimic la imaginea finala
            int inc = (int)Math.round(TEX_POLLING_MAX_INC-(TEX_POLLING_MAX_INC-1)*Math.sin(phi));
            
            for (int u = 0; u < texIntWidth; u += inc) {
                double theta = 2*Math.PI*((u+texStartX)/texSize); // theta apartine [0, 2PI)
                
                double sinPhi = Math.sin(phi);
                
                double x = Math.cos(theta)*sinPhi;
                double y = Math.sin(theta)*sinPhi;
                double z = Math.cos(phi);
                
                Vector3 p = new Vector3(x, y, z)
                    .mul(r)
                    .rotate(texRot)
                    .add(r, r, 0); // translatam spatiul de la [-radius, radius] la [0, diameter]
                
                if (p.getZ() >= 0) {
                    int color = texBuf[v*texIntWidth+u];
                    
                    int i = p.getIntY()*sphIntSize+p.getIntX();
                    
                    if (i < 0 || i >= sphBuf.length) continue; // evita ArrayOutOfBounds exception
                    
                    sphBuf[i] = color;
                }
            }
        }
        
        // BLUR.filter(sph, sphCopy); // functia filter sterge ce se afla pe imaginea destinatie (sphCopy), deci nu mai trebuie sa ne ocupam de asta
        
        // SrcAtop face ca tot ce se afla in afara cercului sa nu fie luat in considerare, ramanand transparent;
        // astfel se obtine clipping la suprafata cercului
        imgG2d.setComposite(AlphaComposite.SrcAtop);
        imgG2d.drawImage(sph, -1, -1, getDiameter()+2, getDiameter()+2, null);
        imgG2d.drawImage(sph, 0, 0, getDiameter(), getDiameter(), null);
        imgG2d.setComposite(AlphaComposite.SrcOver);
        
        // setImage(greenfootTex); // TODO: [REMOVE] this line is used for easilly previewing texture
    }
    
    
    public Type getType() {
        return type;
    }
    
    public Quaternion getTexRot() {
        return texRot;
    }
    
    
    @Override
    public Ball setRadius(int radius) {
        super.setRadius(radius, false);
        
        createTexture();
        updateFont();
        drawTexture();
        
        createImage();
        drawImage();
        
        return this;
    }
    
    @Override
    public Ball setColor(Color color) {
        super.setColor(color, false);
        
        drawTexture();
        
        drawImage();
        
        return this;
    }
    
    protected TexBall setTexRot(Quaternion rot, boolean updateImage) {
        this.texRot = texRot;
        
        if (updateImage) {
            drawTexture();
            
            drawImage();
        }
        
        return this;
    }
    
    public TexBall setTexRot(Quaternion rot) {
        return setTexRot(rot, true);
    }
    
    protected TexBall addTexRot(Quaternion rot, boolean updateImage) {
        // roteste in world space, nu in object space
        // https://forum.unity.com/threads/understanding-rotations-in-local-and-world-space-quaternions.153330/#post-1051238
        this.texRot = rot.mul(this.texRot);
        
        if (updateImage) {
            drawTexture();
            
            drawImage();
        }
        
        return this;
    }
    
    public TexBall addTexRot(Quaternion rot) {
        return addTexRot(rot, true);
    }
    
    public TexBall setType(Type type) {
        this.type = type;
        
        createTexture();
        updateFont();
        drawTexture();
        
        drawImage();
        
        return this;
    }
    
    public TexBall setText(String text) {
        this.text = text;
        
        drawTexture();
        
        drawImage();
        
        return this;
    }
}
