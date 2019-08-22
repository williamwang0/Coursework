public class NBody{
	public static void main(String[] args){
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Body[] bodies = readBodies(filename);

		StdDraw.setScale(-radius,radius);
                String background = "images/starfield.jpg";
                StdDraw.picture(0,0,background);
                StdDraw.enableDoubleBuffering();
                
                for(Body body : bodies){
                        body.draw();
                }              
                StdDraw.show();

		for(int time = 0; time < T; time += dt){
                        double[] xForces = new double[bodies.length];
			double[] yForces = new double[bodies.length];
			for(int i = 0; i < bodies.length; i++){
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
			}
			for(int j = 0; j < bodies.length; j++){
				bodies[j].update(dt, xForces[j], yForces[j]);
			}
			StdDraw.picture(0, 0, background);
			for(Body body : bodies){
				body.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
                }
	
		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
    			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
			bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
			bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
		}		
	}

	public static double readRadius(String file){
		In in = new In(file);
		int numPlanets = in.readInt();
		double radius = in.readDouble();
		return radius;		
	}

	public static Body[] readBodies(String file){
		In in = new In(file);
		int numPlanets = in.readInt();
		Body[] bodies = new Body[numPlanets];
		double radius = in.readDouble();
		for(int i = 0; i < numPlanets; i++){
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			bodies[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
		}
		return bodies;		
	}






}
