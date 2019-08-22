public class Body{
	public double xxPos, yyPos, xxVel, yyVel, mass;
	public String imgFileName;
	private static final double GValue = 6.67e-11;
	public Body(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Body(Body b){
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}
	public double calcDistance(Body a){
		double xDist = a.xxPos - xxPos;
		double yDist = a.yyPos - yyPos;
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}
	public double calcForceExertedBy(Body a){
		double r = calcDistance(a);
		return GValue * mass * a.mass / (r * r);
	}
	public double calcForceExertedByX(Body a){
		double F = calcForceExertedBy(a);
		double r = calcDistance(a);
		return F * (a.xxPos - xxPos) / r;
	}
	public double calcForceExertedByY(Body a){
		double F = calcForceExertedBy(a);
                double r = calcDistance(a);
                return F * (a.yyPos - yyPos) / r;
	}
	public double calcNetForceExertedByX(Body[] bodies){
		double NetForce = 0;
		for(Body body : bodies){
			if(!this.equals(body)){
				NetForce += calcForceExertedByX(body);
			}	
		}
		return NetForce;
	}
	public double calcNetForceExertedByY(Body[] bodies){
                double NetForce = 0;
                for(Body body : bodies){
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
	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}

}
