/**
 * 
 */

import greenfoot.Actor;

public class Phy2Dynamic extends Phy2Obj {
    private Vector2 accel = Vector2.ZERO;
    private Vector2 vel = Vector2.ZERO;
    private Vector2 dir = Vector2.ZERO;
    
    private double mu = 0.05; // coeficientul de frecare
    private Vector2 friction = Vector2.ZERO;
    
    private double restitution = 0.98;
    
    private boolean isMoving = false;
    
    
    public Phy2Dynamic() {}
    
    
    public Vector2 getAccel() {
        return accel;
    }
    
    public Vector2 getVel() {
        return vel;
    }
    
    public Vector2 getDir() {
        return dir;
    }
    
    public double getMu() {
        return mu;
    }
    
    public Vector2 getFriction() {
        return friction;
    }
    
    public double getRestitution() {
        return restitution;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    
    public Phy2Dynamic setAccel(Vector2 accel) {
        this.accel = accel;
        
        return this;
    }
    
    public Phy2Dynamic setAccel(double x, double y) {
        return setAccel(new Vector2(x, y));
    }
    
    public Phy2Dynamic addAccel(Vector2 deltaAccel) {
        return setAccel(accel.add(deltaAccel));
    }
    
    public Phy2Dynamic setVel(Vector2 vel) {
        this.vel = vel;
        this.dir = vel.normalize();
        
        if (vel.getLengthSquared() > 0) {
            if (!isMoving) {
                getPhysics2().incMovingObjs();
            }
            
            isMoving = true;
        } else {
            if (isMoving) {
                getPhysics2().decMovingObjs();
            }
            
            isMoving = false;
        }
        
        // cand viteza este zero atunci si vectorul directie e zero si atunci friction va fi tot 0,
        // deci nu trebuie sa verificam 'isMoving' si sa luam 2 cazuri intr-un if statement;
        friction = dir.mul(mu).minus();
        
        return this;
    }
    
    public Phy2Dynamic setVel(double x, double y) {
        return setVel(new Vector2(x, y));
    }
    
    public Phy2Dynamic addVel(Vector2 deltaVel) {
        return setVel(vel.add(deltaVel));
    }
    
    public Phy2Dynamic setMu(double mu) {
        this.mu = mu;
        
        friction = dir.mul(mu).minus();
        
        return this;
    }
    
    public Phy2Dynamic setRestitution(double restitution) {
        this.restitution = restitution;
        
        return this;
    }
}
