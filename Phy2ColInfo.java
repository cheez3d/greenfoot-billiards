/**
 * Write a description of class Phy2ColInfo here.
 * 
 * TODO: change name from Phy2ColInfo to Phy2Collision
 */

import java.awt.Color;

public class Phy2ColInfo  {
    private boolean isCollision;
    private Vector2 normal, contact;
    private double penetration;
                    
    private Phy2Obj obj1, obj2;
    
    
    public Phy2ColInfo(Phy2Obj obj1, Phy2Obj obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
    
    
    private static void check(
        Phy2Circle circle1, Phy2Circle circle2,
        Phy2ColInfo colInfo
    ) {
        Vector2 relPos = circle1.getPos().sub(circle2.getPos());
        
        colInfo.isCollision = false;
        
        // verifica daca bounding box-urile celor 2 cercuri se intersecteaza
        // aceasta reprezinta o verificare preliminara care este mai putin costisitoare
        if (circle1.getBoundingBox().intersects(circle2.getBoundingBox())) {
            double r1 = circle1.getRadius();
            double r2 = circle2.getRadius();
            double r12 = r1+r2;
            
            // este distanta dintre cele doua cercuri mai mica decat suma razelor lor?
            if (relPos.getLengthSquared() <= r12*r12) {
                colInfo.isCollision = true;
                colInfo.normal = relPos.normalize();
                colInfo.contact = colInfo.normal.mul(r2).add(circle2.getPos());
                colInfo.penetration = r12-relPos.getLength();
                
                return;
            }
        }
    }
    
    private static void check(
        Phy2Circle circle, Phy2Trapezoid trapezoid,
        Phy2ColInfo colInfo
    ) {
        Vector2 relPos = circle.getPos().sub(trapezoid.getPos());
        
        colInfo.isCollision = false;
        
        if (circle.getBoundingBox().intersects(trapezoid.getBoundingBox())) {
            double r = circle.getRadius();
            
            // se intersecteaza cercul cu vreo latura a trapezului?
            Segment2[] sides = trapezoid.getSides();
            for (int i = 0; i < sides.length; ++i) {
                Segment2 side = sides[i];
                
                Vector2 span = relPos.sub(side.getOrig());
                Vector2 proj = span.project(side.getSpan());
                Vector2 dist = span.sub(proj);
                
                if (
                       (dist.getLengthSquared() <= r*r)
                    && (proj.getLengthSquared() <= side.getLengthSquared())
                    && (proj.dot(side.getSpan()) >= 0)
                ) {
                    colInfo.isCollision = true;
                    colInfo.normal = trapezoid.getSideNormals()[i];
                    colInfo.contact = circle.getPos().sub(dist);
                    colInfo.penetration = r-dist.getLength();
                    
                    return;
                }
            }
            
            // se intersecteaza cercul cu vreun varf al trapezului?
            Vector2[] vertices = trapezoid.getVertices();
            for (int i = 0; i < vertices.length; ++i) {
                Vector2 vertex = vertices[i];
                
                Vector2 dist = relPos.sub(vertex);
                
                if (dist.getLengthSquared() <= r*r) {
                    colInfo.isCollision = true;
                    colInfo.contact = trapezoid.getPos().add(vertex);
                    colInfo.normal = trapezoid.getVertexNormals()[i];
                    colInfo.penetration = r-dist.getLength();
                    
                    return;
                }
            }
        }
    }
    
    private static void check(
        Phy2Trapezoid trapezoid, Phy2Circle circle,
        Phy2ColInfo colInfo
    ) {
        check(circle, trapezoid, colInfo);
    }
    
    
    private static void handle(
        Phy2Dynamic dynamic1, Phy2Dynamic dynamic2,
        Phy2ColInfo colInfo
    ) {
        if (
               dynamic1.getCollider() instanceof Phy2Circle
            && dynamic2.getCollider() instanceof Phy2Circle
        ) {
            Vector2 normal = colInfo.normal;
            
            // daca normala este orientata spre corpul 2, si nu spre corpul 1, ii schimbam orientarea
            if (normal.dot(dynamic1.getPos().sub(colInfo.contact)) < 0) {
                normal = normal.minus();
            }
            Vector2 tangent = new Vector2(normal.getY(), -normal.getX());
            
            // STATIC RESPONSE
            // daca ambele corpuri se misca;
            if (dynamic1.isMoving() && dynamic2.isMoving()) {
                Vector2 resp1 = normal.mul(colInfo.penetration/2);
                dynamic1.addPos(resp1);
                
                Vector2 resp2 = resp1.minus();
                dynamic2.addPos(resp2);
                
            // daca doar corpul 1 se misca;
            } else if (dynamic1.isMoving()) {
                Vector2 resp = normal.mul(colInfo.penetration);
                dynamic1.addPos(resp);
                
            // daca doar corpul 2 se misca;
            } else if (dynamic2.isMoving()) {
                Vector2 resp = normal.mul(colInfo.penetration).minus();
                dynamic2.addPos(resp);
                
            // daca niciunul dinre corpuri nu se misca;
            } else {
                Vector2 resp1 = normal.mul(colInfo.penetration/2);
                dynamic1.addPos(resp1);
                
                Vector2 resp2 = resp1.minus();
                dynamic2.addPos(resp2);
            }
            
            // DYNAMIC RESPONSE
            Vector2 v1 = dynamic1.getVel();
            Vector2 v2 = dynamic2.getVel();
            
            // viteze tangential la linia de coliziune
            Vector2 tv1 = tangent.mul(v1.dot(tangent));
            Vector2 tv2 = tangent.mul(v2.dot(tangent));
            
            double m1 = dynamic1.getMass();
            double m2 = dynamic2.getMass();
            
            double nv1l = v1.dot(normal);
            double nv2l = v2.dot(normal);
            
            // vitezele normale la linia de coliziune, dupa coliziune;
            // pentru calcularea acestora se considera o coliziune unidimensionala pe normala de coliziune
            // si se foloseste conservarea impulsului;
            Vector2 nv1 = normal.mul((nv1l*(m1-m2)+2*m2*nv2l)/(m1+m2));
            Vector2 nv2 = normal.mul((nv2l*(m2-m1)+2*m1*nv1l)/(m1+m2));
            
            // pentru a obtine vitezele finale, pur si simplu adunam partile tangentiala si normala ale vitezelor
            dynamic1.setVel(tv1.add(nv1).mul(dynamic1.getRestitution()));
            dynamic2.setVel(tv2.add(nv2).mul(dynamic2.getRestitution()));
        }
    }
    
    private static void handle(
        Phy2Dynamic dynamic, Phy2Static _static,
        Phy2ColInfo colInfo
    ) {
        if (
               dynamic.getCollider() instanceof Phy2Circle
            && _static.getCollider() instanceof Phy2Trapezoid
        ) {
            Vector2 normal = colInfo.getNormal();
            
            // nu trebuie sa mai verificam orientarea normalei deoarece in cazul Phy2Trapezoid, normalele
            // sunt orientate spre exteriorul acestuia
            /* // daca normala este orientata spre static, si nu spre dynamic, ii schimbam orientarea
            if (normal.dot(dynamic.getPos().sub(colInfo.contact)) < 0) {
                normal = normal.minus();
            } */
            
            // STATIC RESPONSE
            Vector2 resp = dynamic.getPos()
                .sub(colInfo.contact)
                .normalize()
                .mul(colInfo.penetration);
            dynamic.addPos(resp);
            
            // DYNAMIC RESPONSE
            dynamic.setVel(dynamic.getVel().reflect(normal).mul(dynamic.getRestitution()));
        }
    }
    
    private static void handle(
        Phy2Static _static, Phy2Dynamic dynamic,
        Phy2ColInfo colInfo
    ) {
        handle(dynamic, _static, colInfo);
    }
    
    
    protected void drawDebug(Overlay overlay) {
        if (!Game.getInstance().isDebug()) return;
        
        if (isCollision) {
            overlay.setPaint(Color.WHITE);
            
            overlay.drawLine(obj1.getCollider().getPos(), contact);
            overlay.drawLine(obj2.getCollider().getPos(), contact);
        }
    }
    
    
    public Phy2ColInfo check() {
        Phy2Collider collider1 = obj1.getCollider();
        Phy2Collider collider2 = obj2.getCollider();
        
        if (collider1 instanceof Phy2Circle) {
            Phy2Circle circle1 = (Phy2Circle)collider1;
            
            if (collider2 instanceof Phy2Circle) {
                Phy2Circle circle2 = (Phy2Circle)collider2;
                
                check(circle1, circle2, this);
            } else if (collider2 instanceof Phy2Trapezoid) {
                Phy2Trapezoid trapezoid2 = (Phy2Trapezoid)collider2;
                
                check(circle1, trapezoid2, this);
            }
        } else if (collider1 instanceof Phy2Trapezoid) {
            Phy2Trapezoid trapezoid1 = (Phy2Trapezoid)collider1;
            
            if (collider2 instanceof Phy2Circle) {
                Phy2Circle circle2 = (Phy2Circle)collider2;
                
                check(trapezoid1, circle2, this);
            } else if (collider2 instanceof Phy2Trapezoid) {
                // INFO: neimplementat
            }
        }
        
        return this;
    }
    
    public Phy2ColInfo handle() {
        if (obj1 instanceof Phy2Dynamic) {
            Phy2Dynamic dynamic1 = (Phy2Dynamic)obj1;
            
            if (obj2 instanceof Phy2Dynamic) {
                Phy2Dynamic dynamic2 = (Phy2Dynamic)obj2;
                
                handle(dynamic1, dynamic2, this);
            } else if (obj2 instanceof Phy2Static) {
                Phy2Static static2 = (Phy2Static)obj2;
                
                handle(dynamic1, static2, this);
            }
        } else if (obj1 instanceof Phy2Static) {
            Phy2Static static1 = (Phy2Static)obj1;
            
            if (obj2 instanceof Phy2Dynamic) {
                Phy2Dynamic dynamic2 = (Phy2Dynamic)obj2;
                
                handle(static1, dynamic2, this);
            }
        }
        
        return this;
    }
    
    
    public boolean isCollision() {
        return isCollision;
    }
    
    public double getPenetration() {
        return penetration;
    }
    
    public Vector2 getContact() {
        return contact;
    }
    
    public Vector2 getNormal() {
        return normal;
    }
    
    public Phy2Obj getObj1() {
        return obj1;
    }
    
    public Phy2Obj getObj2() {
        return obj2;
    }
}
