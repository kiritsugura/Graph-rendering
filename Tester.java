
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

public class Tester extends BasicGame implements MouseListener,KeyListener{
    /*The current zoom aspect.*/
    private float zoomFactor=1.0f;
    /*The graph that is currently being drawn.*/
    private DrawableGraph graph;
    /*Boolean for if a transaltion occurs, boolean for if a zoom occured.*/
    private boolean wasShifted,wasZM;
    /*The rate at which x is changing,rate at which y is changing,current x translation, current y translation.*/
    private float dx,dy,x,y;
    /*The current Graphics Object being used for rendering.*/
    private Graphics drawing;
    /*Constructor for Tester.
      @param: The name of this GUI.*/
    public Tester(String title){
        super(title);
        wasShifted=false;
        x=0;
        y=0;
    }
    @Override
    public void mouseWheelMoved(int change){
        /*Change the zoom.*/
        if(change>0 && zoomFactor<8.0){
            zoomFactor+=.20;
        }else if(zoomFactor>.60){
            zoomFactor-=.20;
        }
    }
    @Override
    public void keyPressed(int key,char c){
        /*Translate the screen.*/
        if(c=='w'){
            dy=.80f;
            wasShifted=true;
        }else if(c=='d'){
            dx=-.80f;
            wasShifted=true;
        }else if(c=='s'){
            dy=-.80f;
            wasShifted=true;
        }else if(c=='a'){
            dx=.80f;
            wasShifted=true;
        //toggle names of edges and nodes.    
        }else if(c=='e'){
            graph.showName=!graph.showName;
        }
            
    }
    @Override
    public void keyReleased(int key,char c){
        /*Stop the translation.*/
        if(c=='w' && wasShifted){
            dy=0;
        }else if(c=='d' && wasShifted){
            dx=0;
        }else if(c=='s' && wasShifted){
            dy=0;
        }else if(c=='a' && wasShifted){
            dx=0;
        }    
        if(dx==0 && dy==0){
            wasShifted=false;
        }
    }   
    @Override
    public void init(GameContainer gc) throws SlickException{ 
        gc.getInput().addMouseListener(this);
        graph=new DrawableGraph("map04.txt");
    }
    @Override
    public void update(GameContainer gc, int i) throws SlickException { 
        /*Update the translation.*/
        x+=dx*(float)i;
        y+=dy*(float)i;
    }
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException{
        drawing=g;
        drawing.scale(zoomFactor,zoomFactor);
        g.translate(x, y);
        graph.draw(g);
    }    
    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            Tester my_app = new Tester("Graphs");
            appgc = new AppGameContainer(my_app);
            appgc.setDisplayMode(640,480, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
