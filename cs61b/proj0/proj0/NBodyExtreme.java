/* Added a flying squirrel that is affected by gravity and can be controlled using "w,a,s,d"
* ---IMPORTANT--- Dont forget to source ppl >:(
* */

public class NBodyExtreme{
    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        BodyExtreme[] bodies = readBodies(filename);
        BodyExtreme ship = new BodyExtreme(-radius/2,-radius/3,0,0,1.5e10,"ninjasquirrel_red.gif");

        StdDraw.setScale(-radius,radius);
        String background = "images/starfield.jpg";
        StdDraw.picture(0,0,background);
        StdDraw.enableDoubleBuffering();

        for(BodyExtreme body : bodies){
            body.draw();
        }
        ship.draw();
        StdDraw.show();

        for(int time = 0; time < T; time += dt){

            /* Key Pressed Listener */
            if(StdDraw.hasNextKeyTyped()){
                ship.KeyPressed(StdDraw.nextKeyTyped());
            }

            double[] xForces = new double[bodies.length];
            double[] yForces = new double[bodies.length];
            double xForceShip = ship.calcNetForceExertedByX(bodies);
            double yForceShip = ship.calcNetForceExertedByY(bodies);

            for(int i = 0; i < bodies.length; i++){
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
            }
            for(int j = 0; j < bodies.length; j++){
                bodies[j].update(dt, xForces[j], yForces[j]);
            }

            ship.update(dt, ship.xxVel + xForceShip, ship.yyVel + yForceShip);

            StdDraw.picture(0, 0, background);
            for(BodyExtreme body : bodies){
                body.draw();
            }
            ship.draw();
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

    public static BodyExtreme[] readBodies(String file){
        In in = new In(file);
        int numPlanets = in.readInt();
        BodyExtreme[] bodies = new BodyExtreme[numPlanets];
        double radius = in.readDouble();
        for(int i = 0; i < numPlanets; i++){
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            bodies[i] = new BodyExtreme(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return bodies;
    }

}
