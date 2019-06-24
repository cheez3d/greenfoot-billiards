public final class Segment2 {
    public static final Segment2 ZERO = new Segment2();
    
    
    private final Vector2 orig;
    private final Vector2 dest;
    private final Vector2 span;
    
    
    public Segment2(Vector2 orig, Vector2 dest) {
        this.orig = orig;
        this.dest = dest;
        this.span = dest.sub(orig);
    }
    
    public Segment2(double ox, double oy, double dx, double dy) {
        this(
            new Vector2(ox, oy),
            new Vector2(dx, dy)
        );
    }
    
    public Segment2(Segment2 s) {
        this(s.orig, s.dest);
    }
    
    public Segment2() {
        this(0, 0, 0, 0);
    }
    
    
    @Override
    public String toString() {
        return String.format("(%s, %s)", orig, dest);
    }
    
    
    public Segment2 add(Vector2 v) {
        return new Segment2(orig.add(v), dest.add(v));
    }
    
    public Segment2 sub(Vector2 v) {
        return new Segment2(orig.sub(v), dest.sub(v));
    }
    
    public Segment2 mul(double f) {
        return new Segment2(orig.mul(f), dest.mul(f));
    }
    
    public Segment2 project(Segment2 on) {
        // calculeaza coordonata x a punctului de intesectie dintre dreapta asociata segmentului onto
        // si o dreapta perpendiculara pe aceasta, ce trece prin originea segmentului de proiectat (this);
        // formula pentru dreapta asociata segmentului onto este obtinuta din cele 2 puncte ale segmentului;
        // formula pentru dreapta perpendiculara este obtinuta avand in vedere ca produsul pantelor a 2 drepte
        // perpendiculare este -1; de aici obtinem panta dreptei perpendiculare, si cum dreapta trece prin
        // originea segmentului this => am obtinut formula dreptei;
        // prin calcularea coordonatei x de intersectie a celor doua drepte in prealabil se obtine formula de mai jos:
        
        double slope = on.span.getY()/on.span.getX();
        double invSlope = 1/slope;

        double projOx = ((orig.getY()-on.orig.getY())+(slope*on.orig.getX()+invSlope*orig.getX()))/(slope+invSlope);
        double projOy = slope*(projOx-on.orig.getX())+on.orig.getY();
        
        double projDx = projOx+span.getX();
        double projDy = projOy+span.getY();
        
        return new Segment2(projOx, projOy, projDx, projDy);
    }
    
    public Segment2 rotate(double a) {
        return new Segment2(orig.rotate(a), span.rotate(a));
    }
    
    
    public Vector2 getOrig() {
        return orig;
    }
    
    public double getOrigX() {
        return orig.getX();
    }
    
    public double getOrigY() {
        return orig.getY();
    }
    
    public Vector2 getDest() {
        return dest;
    }
    
    public double getDestX() {
        return dest.getX();
    }
    public double getDestY() {
        return dest.getY();
    }
    
    public Vector2 getSpan() {
        return span;
    }
    
    
    public double getSpanX() {
        return span.getX();
    }
    
    public double getSpanY() {
        return span.getY();
    }
    
    public double getLengthSquared() {
        return span.getLengthSquared();
    }
    
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }
}
