public class BodyExtreme{
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    private static final double GValue = 6.67e-11;
    public BodyExtreme(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public BodyExtreme(BodyExtreme b){
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }
    public double calcDistance(BodyExtreme a){
        double xDist = a.xxPos - xxPos;
        double yDist = a.yyPos - yyPos;
        return Math.sqrt(xDist*xDist + yDist*yDist);
    }
    public double calcForceExertedBy(BodyExtreme a){
        double r = calcDistance(a);
        return GValue * mass * a.mass / (r * r);
    }
    public double calcForceExertedByX(BodyExtreme a){
        double F = calcForceExertedBy(a);
        double r = calcDistance(a);
        return F * (a.xxPos - xxPos) / r;
    }
    public double calcForceExertedByY(BodyExtreme a){
        double F = calcForceExertedBy(a);
        double r = calcDistance(a);
        return F * (a.yyPos - yyPos) / r;
    }
    public double calcNetForceExertedByX(BodyExtreme[] bodies){
        double NetForce = 0;
        for(BodyExtreme body : bodies){
            if(!this.equals(body)){
                NetForce += calcForceExertedByX(body);
            }
        }
        return NetForce;
    }
    public double calcNetForceExertedByY(BodyExtreme[] bodies){
        double NetForce = 0;
        for(BodyExtreme body : bodies){
            if(!this.equals(body)){
                NetForce += calcForceExertedByY(body);
            }
        }
        return NetForce;
    }
    public void update(double dt, double fX, double fY){
        double a_x = fX / mass;
        double a_y = fY / mass;
        xxVel += dt * a_x;
        yyVel += dt * a_y;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    public void KeyPressed(char key){
        if(key == 'a'){
            xxVel -= 15000;
        }
        if(key == 'd'){
            xxVel += 15000;
        }
        if(key == 's'){
            yyVel -= 15000;
        }
        if(key == 'w') {
            yyVel += 15000;
        }
    }

    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}
