import java.awt.*;

import greenfoot.MouseInfo;
import greenfoot.Greenfoot;
import greenfoot.GreenfootSound;
import greenfoot.World;

public class Session extends GameWorld {
    public static final String[] BALL_BALL_SOUND_FILES = new String[]{
        "ball-ball-0.wav",
        "ball-ball-1.wav",
        "ball-ball-2.wav",
        "ball-ball-3.wav",
        "ball-ball-4.wav",
        "ball-ball-5.wav",
    };
    
    public static final GreenfootSound[] BALL_BALL_SOUNDS = new GreenfootSound[BALL_BALL_SOUND_FILES.length];
    static {
        for (int i = 0; i < BALL_BALL_SOUNDS.length; ++i) {
            BALL_BALL_SOUNDS[i] = new GreenfootSound(BALL_BALL_SOUND_FILES[i]);
        }
    }
    
    public static final String[] CUSHION_BALL_SOUND_FILES = new String[]{
        "cushion-ball-0.wav",
        "cushion-ball-1.wav",
        "cushion-ball-2.wav",
    };
    
    public static final GreenfootSound[] CUSHION_BALL_SOUNDS = new GreenfootSound[CUSHION_BALL_SOUND_FILES.length];
    static {
        for (int i = 0; i < CUSHION_BALL_SOUND_FILES.length; ++i) {
            CUSHION_BALL_SOUNDS[i] = new GreenfootSound(CUSHION_BALL_SOUND_FILES[i]);
        }
    }
    
    public static final String[] POCKET_BALL_SOUND_FILES = new String[]{
        "pocket-ball-0.wav",
        "pocket-ball-1.wav",
        "pocket-ball-2.wav",
        "pocket-ball-3.wav",
        "pocket-ball-4.wav",
    };
    
    public static final GreenfootSound[] POCKET_BALL_SOUNDS = new GreenfootSound[POCKET_BALL_SOUND_FILES.length];
    static {
        for (int i = 0; i < POCKET_BALL_SOUND_FILES.length; ++i) {
            POCKET_BALL_SOUNDS[i] = new GreenfootSound(POCKET_BALL_SOUND_FILES[i]);
        }
    }
    
    
    // numarul de bile de pe masa, cu exceptia cue ball-ului
    public static final int BALL_COUNT = 15;
    
    public static final int BALL_RADIUS = 18;
    
    public static final int CUE_LENGTH = 512;
    
    
    private static final Class[] PAINT_ORDER = new Class[]{
        Overlay.class,
        Cue.class,
        Ball.class,
        Glow.class,
        Cushion.class,
        Pocket.class,
        Felt.class,
        Base.class,
    };
    
    
    Table table = new Table();
    
    Ball cueBall = new TexBall()
        .setType(TexBall.Type.CUE);
    TexBall[] balls = new TexBall[BALL_COUNT];
    
    Cue cue = new Cue();
    GreenfootSound sound;
    public Session(int width, int height) {
        super(width, height, 1, false);
        
        // setam ordinea de desenare pentru PaintMethod-ul GREENFOOT;
        setPaintOrder(PAINT_ORDER);
        // PaintMethod-ul OVERLAY se foloseste de metoda 'act' a actorilor pentru desenarea acestora pe overlay;
        // de aceea setam act order-ul actorila in concordanta cu ordinea de desenare;
        setActOrder(PAINT_ORDER);
        
        table.setPos(width/2, height/2);
        table.setWidth(width-64);
        table.setHeight(height-64);
        addGameActor(table);
        
        int tableX = table.getX();
        int tableY = table.getY();
        int tableWidth = table.getWidth();
        int tableHeight = table.getHeight();
        
        cueBall.setPos(tableX-tableWidth/4, tableY);
        cueBall.setColor(Color.WHITE);
        // cueBall.setRadius(36); cueBall.getPhy2Obj().setMass(1); //*
        addGameActor(cueBall);
        
        for (int i = 0; i < balls.length; ++i) {
            TexBall texBall = new TexBall();
            
            texBall.setText(Integer.toString(i+1));
            if (i >= 8) texBall.setType(TexBall.Type.STRIPE);
            
            balls[i] = texBall;
        }
        
        if (BALL_COUNT == 9) {
            balls[0].setColor(new Color(255, 215,   0));
            balls[1].setColor(new Color(  0,   0, 255));
            balls[2].setColor(new Color(255,   0,   0));
            balls[3].setColor(new Color(138,  43, 226));
            balls[4].setColor(new Color(255,  69,   0));
            balls[5].setColor(new Color( 34, 139,  34));
            balls[6].setColor(new Color(165,  42,  42));
            balls[7].setColor(new Color(  0,   0,   0));
            balls[8].setColor(balls[0].getColor());
            
            
            Vector2 centerBallPos = new Vector2(tableX+tableWidth/4, tableY);
            
            balls[8].setPos(centerBallPos);
            
            double eqTriangleHeight = (double)(2*BALL_RADIUS)*Math.sqrt(3.0);
            Vector2 leftMostBallPos = new Vector2(centerBallPos.getX()-eqTriangleHeight, centerBallPos.getY());
            Vector2 rightMostBallPos = new Vector2(centerBallPos.getX()+eqTriangleHeight, centerBallPos.getY());
            
            Vector2 topMostBallPos = new Vector2(centerBallPos.getX(), centerBallPos.getY()-2*BALL_RADIUS);
            Vector2 bottomMostBallPos = new Vector2(centerBallPos.getX(), centerBallPos.getY()+2*BALL_RADIUS);
            
            Vector2[] ballsPos = new Vector2[]{
                leftMostBallPos,
                leftMostBallPos.middle(topMostBallPos),
                topMostBallPos,
                topMostBallPos.middle(rightMostBallPos),
                rightMostBallPos,
                rightMostBallPos.middle(bottomMostBallPos),
                bottomMostBallPos,
                bottomMostBallPos.middle(leftMostBallPos),
            };
            
            addGameActor(balls[8]);
        
            boolean[] ballPlaced = new boolean[ballsPos.length];
            for (int i = 0; i < ballsPos.length; ++i) {
                int pos_i = (int)Math.round(Math.random()*(ballsPos.length-1));
                
                while (ballPlaced[pos_i]) {
                    ++pos_i;
                    if (pos_i == ballsPos.length) pos_i = 0;
                }
                
                ballPlaced[pos_i] = true;
                
                addGameActor(balls[i], ballsPos[pos_i]);
            }
        } else if (BALL_COUNT == 15) {
            balls[0].setColor(new Color(255, 215,   0));
            balls[1].setColor(new Color(  0,   0, 255));
            balls[2].setColor(new Color(255,   0,   0));
            balls[3].setColor(new Color(138,  43, 226));
            balls[4].setColor(new Color(255,  69,   0));
            balls[5].setColor(new Color( 34, 139,  34));
            balls[6].setColor(new Color(165,  42,  42));
            balls[7].setColor(new Color(  0,   0,   0));
            balls[8].setColor(balls[0].getColor());
            balls[9].setColor(balls[1].getColor());
            balls[10].setColor(balls[2].getColor());
            balls[11].setColor(balls[3].getColor());
            balls[12].setColor(balls[4].getColor());
            balls[13].setColor(balls[5].getColor());
            balls[14].setColor(balls[6].getColor());
            
            
            Vector2 centerBallPos = new Vector2(tableX+tableWidth/4, tableY);
            
            balls[7].setPos(centerBallPos);
            
            double eqTriangleHeight = (double)(2*BALL_RADIUS)*Math.sqrt(3.0);
            Vector2 leftMostBallPos = new Vector2(centerBallPos.getX()-eqTriangleHeight, centerBallPos.getY());
            
            Vector2 topMostBallPos = new Vector2(centerBallPos.getX()+eqTriangleHeight, centerBallPos.getY()-4*BALL_RADIUS);
            Vector2 bottomMostBallPos = new Vector2(centerBallPos.getX()+eqTriangleHeight, centerBallPos.getY()+4*BALL_RADIUS);
            
            Vector2[] ballsPos = new Vector2[14];
            
            ballsPos[0] = leftMostBallPos;
            ballsPos[1] = topMostBallPos;
            ballsPos[2] = leftMostBallPos.middle(topMostBallPos);
            ballsPos[3] = leftMostBallPos.middle(ballsPos[2]);
            ballsPos[4] = ballsPos[2].middle(topMostBallPos);
            
            ballsPos[5] = bottomMostBallPos;
            ballsPos[6] = topMostBallPos.middle(bottomMostBallPos);
            ballsPos[7] = topMostBallPos.middle(ballsPos[6]);
            ballsPos[8] = ballsPos[6].middle(bottomMostBallPos);
            
            ballsPos[9] = bottomMostBallPos.middle(leftMostBallPos);
            ballsPos[10] = bottomMostBallPos.middle(ballsPos[9]);
            ballsPos[11] = ballsPos[9].middle(leftMostBallPos);
            
            ballsPos[12] = centerBallPos.middle(ballsPos[7]);
            ballsPos[13] = centerBallPos.middle(ballsPos[8]);
            
            
            addGameActor(balls[7]);
        
            boolean[] ballPlaced = new boolean[ballsPos.length];
            for (int i = 0; i < balls.length; ++i) {
                int pos_i = (int)Math.round(Math.random()*(ballsPos.length-1));
                
                while (ballPlaced[pos_i]) {
                    ++pos_i;
                    if (pos_i == ballsPos.length) pos_i = 0;
                }
                
                ballPlaced[pos_i] = true;
                
                if (i == 7) ++i;
                addGameActor(balls[i], ballsPos[pos_i]);
            }
        }
        
        
        
        
        // Cushion c = new Cushion();
        // c.setAngle2(1);
        // c.setAngle1(0.3);
        // addGameActor(c, width/4, height/4);
        // c.setRot(Math.PI/6);
        
        cue.setLength(CUE_LENGTH);
        cue.setPos(16, getHeight()/2); //*
        cue.setRot(-Math.PI/2); //*
        addGameActor(cue);
        cue.setPaintMethod(GameActor.PaintMethod.OVERLAY);
        
        Physics2 physics2 = new Physics2();
        physics2.setGameWorld(this);
        physics2.init();
        
        
        
        // CODE DEBUG
    }
    
    // daca valoarea lui 'cueDrag' este mai mica ca cea de aici nu se va initia cue pushing-ul;
    // acest lucru le ofera jucatorilor sansa de a-si repozitiona tacul, daca nu l-au pozitionat bine;
    private static final double CUE_DRAG_TOLERANCE = 4.0;
    
    // tragerea tacului in spate inaintea lovirii bilei este asemuita cu tragerea de un arc;
    // astfel, avem nevoie de o constanta de elasticitate pentru a putea influenta
    // viteza pe care o primeste bila in urma interactiunii cu tacul;
    private static final double CUE_SPRING_CONSTANT = 4.0;
    
    // timpul, in secunde, cat interactioneaza tacul cu cue ball-ul;
    // acesta influenteaza viteza pe care o primeste cue ball-ul;
    private static final double CUE_BALL_INTERACTION = 0.01;
    
    private Vector2 mousePressPos = Vector2.ZERO;
    private double cueAngle = 0.0;
    private Vector2 cueDir = Vector2.ZERO;
    private double cueMaxAllowedDrag = CUE_LENGTH*0.67;
    
    private double cueMaxSpeed = cueMaxAllowedDrag/8;
    private double cueDrag = 0.0, cueMaxDrag;
    private boolean draggingCue = false;
    private boolean pushingCue = false;
    @Override
    public void act() {
        super.act();
        
        double actCount = getActDelta()*REF_APMS;
        
        if (Greenfoot.mouseMoved(null) && !draggingCue && !pushingCue && (getPhysics2().getMovingObjs() == 0)) {
            MouseInfo mouseInfo = Greenfoot.getMouseInfo();
            
            // unghiul format de segmentul ce uneste pozitia cue ball-ului de pozitia mouse-ului;
            cueAngle = Math.atan2(
                cueBall.getY()-mouseInfo.getY(),
                cueBall.getX()-mouseInfo.getX()
            );
            
            // directia tacului, adica orientarea pe care o are acesta fata de cue ball;
            // acest vector este normalizat;
            cueDir = new Vector2(Math.cos(cueAngle), Math.sin(cueAngle)).minus();
            
            cue.setPos(cueBall.getPos()
                .add(cueDir.mul(cue.getLength()/2+cueBall.getRadius()))
            );
            cue.setRot(cueAngle);
        }
        
        if (Greenfoot.mousePressed(null) && !draggingCue && !pushingCue && (getPhysics2().getMovingObjs() == 0)) {
            MouseInfo mouseInfo = Greenfoot.getMouseInfo();
            
            mousePressPos = new Vector2(mouseInfo.getX(), mouseInfo.getY());
            draggingCue = true;
            
            Game.getInstance().printDebugLine("Cue dragging started.");
        }
        
        if (Greenfoot.mouseDragged(null)) {
            if (draggingCue) {
                MouseInfo mouseInfo = Greenfoot.getMouseInfo();
                
                if (mouseInfo.getButton() == Game.GREENFOOT_LMB) {
                    // pozitia curenta a mouse-ului
                    Vector2 mousePos = new Vector2(mouseInfo.getX(), mouseInfo.getY());
                    
                    // vectorul distanta dintre pozitia curenta a mouse-ului
                    // si pozitia acestuia cand a fost apasat, aceasta distanta
                    // fiind proiectata pe directia tacului pentru ca acesta
                    // sa poata fi miscat doar atunci cand mouse-ul se misca pe directia tacului;
                    Vector2 distVec = mousePos.sub(mousePressPos).project(cueDir);
                    
                    // distanta scalara pentru vectorul distVec;
                    // s-a folosit produsul scalar si nu getLength la calcularea acesteia pentru
                    // a sti cand mouse-ul este miscata astfel incat tacul sa "intre" in bila;
                    // cand tacul "intra" in bila valoarea lui 'cueDrag' va fi negativa si atunci o putem reseta la 0
                    // pentru ca tacul sa nu "intre" in bila;
                    cueDrag = distVec.dot(cueDir);
                    
                    // clamp the displacement
                    if (cueDrag < 0) {
                        cueDrag = 0;
                    } else if (cueDrag > cueMaxAllowedDrag) {
                        cueDrag = cueMaxAllowedDrag;
                    }
                    
                    cue.setPos(cueBall.getPos()
                        .add(cueDir.mul(cue.getLength()/2+cueBall.getRadius()+cueDrag))
                    );
                }
            }
        }
        
        if (Greenfoot.mouseClicked(null) && draggingCue && !pushingCue) {
            draggingCue = false;
            cueMaxDrag = cueDrag;
            
            if (cueMaxDrag > CUE_DRAG_TOLERANCE) {
                pushingCue = true;
                
                Game.getInstance().printDebugLine("Cue pushing started.");
            } else {
                cue.setPos(cueBall.getPos()
                    .add(cueDir.mul(cue.getLength()/2+cueBall.getRadius()))
                );
            }
            
            Game.getInstance().printDebugLine("Cue dragging ended.");
        }
        
        if (pushingCue) {
            cue.setPos(cueBall.getPos()
                .add(cueDir.mul(cue.getLength()/2+cueBall.getRadius()+cueDrag))
            );
            
            cueDrag -= cueMaxSpeed*(cueMaxDrag/cueMaxAllowedDrag)*actCount;
            
            if (cueDrag <= 0) {
                if (cueDrag < 0) cueDrag = 0;
                pushingCue = false;
                
                Game.getInstance().printDebugLine("Cue pushing ended.");
                
                Phy2Dynamic dynamic = (Phy2Dynamic)cueBall.getPhy2Obj();
                
                double k = CUE_SPRING_CONSTANT;
                double dt = CUE_BALL_INTERACTION;
                double x = cueMaxDrag;
                double m = dynamic.getMass();
                
                // folosim legea lui Hooke pentru calcularea vitezei
                double speed = k*dt*x*(1/m);
                
                Vector2 vel = cueDir.mul(speed).minus();
                dynamic.setVel(vel);
                
                cue.setPos(16, getHeight()/2); //*
                cue.setRot(-Math.PI/2); //*
                
                Game.getInstance().printDebugLine(String.format("Cue ball struck with speed %f.", speed));
            }
        }
        
        getPhysics2().step();
    }
}
