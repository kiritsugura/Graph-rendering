
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Transform;

/*A Graph that can be drawn when given a formated file. Can extend an AjacencyGraph or MatrixGraph.*/
public class DrawableGraph<N,E> extends AdjacencyGraph{
    /*HashSet of Drawable Nodes.*/
    private HashSet<DrawableNode> nodes;
    /*HashSet of drawable edges.*/
    private HashSet<DrawableEdge> drEdge;
    /*The Misc Lines present in the file format.*/
    private ArrayList<Float[]> pLines;
    /*If true, draws the name of the edge or node.*/
    public boolean showName;
    /*Constructor for a drawable graph.
      @param: filen is the name of the file that contains the graph data.*/
    public DrawableGraph(String filen){
        nodes=new HashSet(80,20,.8);
        drEdge=new HashSet(80,20,.8);
        pLines=new ArrayList();
        showName=false;
        try{
            super.loadFromFile(filen);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /*Processes a line and adds items to the graph based on the file contents.
      @param: line is the current line of data.*/
    public void processFileLine(String line){
        String[] lines=line.split(" ");
            if(lines[0].indexOf("n")>=0){
                N n=(N)lines[1];
                super.addNode(n);
                Node node=super.getLast();
                nodes.add(new DrawableNode(node,Float.valueOf(lines[2]),Float.valueOf(lines[3]),Float.valueOf(lines[4])));
            }else if(lines[0].indexOf("e")>=0){
                Node node1=getNodeNum(Integer.valueOf(lines[1])),node2=getNodeNum(Integer.valueOf(lines[2]));
                Edge newEdge=new Edge((E)lines[3],node1,node2);
                super.addEdge(newEdge, node1, node2, true);
                drEdge.add(new DrawableEdge(newEdge,getDNode(Integer.valueOf(lines[1])),getDNode(Integer.valueOf(lines[2]))));
            }else if(lines[0].indexOf("L")>=0){
                Float xp1=Float.valueOf(lines[1]),yp1=Float.valueOf(lines[2]),xp2=Float.valueOf(lines[3]),yp2=Float.valueOf(lines[4]);
                Float[] points=new Float[4];
                points[0]=xp1;
                points[1]=yp1;
                points[2]=xp2;
                points[3]=yp2;
                pLines.add(points);
            }   
    }
    /*@param: an index in the drawable node hashset nodes.
      @return: returns the DrawableNode with the index 'index'.*/
    public DrawableNode getDNode(int index){
        HashMapIterator it=nodes.iterator(true);
        while(it.hasNext()){
            DrawableNode drn=(DrawableNode)it.next();
            if(drn.getIndex()==index){
                return drn;
            }
        }
        return null;
    }
    /*Draw the current DrawableGraph.*/
    public void draw(Graphics g){
        Iterator iter=nodes.iterator(true);
        Iterator it=drEdge.iterator(true);
        Iterator listI=pLines.iterator();
        while(it.hasNext()){
            DrawableEdge edg=((DrawableEdge)it.next());
            edg.draw(g);
        }
        while(iter.hasNext()){
            ((DrawableNode)iter.next()).draw(g);
        }       
        while(listI.hasNext()){
            Float[] next=(Float [])listI.next();
            g.setColor(Color.blue);
            g.drawLine(next[0],next[1],next[2],next[3]);
        }
    }
    /*A drawable node.*/
    private class DrawableNode{
        protected float xCoor,yCoor,radius;
        protected Node me;
        public DrawableNode(Node node,float x,float y,float r){
            xCoor=x;
            yCoor=y;
            radius=r;
            me=node;
        }
        public void draw(Graphics g){
            g.setColor(Color.white);
            g.fillOval(xCoor,yCoor, radius,radius);
            g.setColor(Color.red);
            if(showName){               
                Font off=g.getFont();
                g.drawString(me.toString(), xCoor-off.getWidth(me.toString())/2,yCoor-off.getHeight(me.toString()));
            }
        }
        public int getIndex(){
            return me.index;
        }
    }
    /*A drawable edge.*/
    private class DrawableEdge{
        protected Edge me;
        protected DrawableNode con1,con2;
        public DrawableEdge(Edge edge,DrawableNode d1,DrawableNode d2){
            me=edge;
            con1=d1;
            con2=d2;
        }
        public void draw(Graphics g){
            g.setColor(Color.orange);
            g.drawLine(con1.xCoor+con1.radius/2,con1.yCoor+con1.radius/2,con2.xCoor+con2.radius/2,con2.yCoor+con2.radius/2);
            if(showName){
                g.setColor(Color.white);
                String s=me.edgeVal.toString().substring(0,me.edgeVal.toString().indexOf('.')+2);
                Font off=g.getFont();
                g.fillRect(((con1.xCoor+con2.xCoor)/2)-off.getWidth(s)/2, ((con1.yCoor+con2.yCoor)/2)-off.getHeight(s)/2,off.getWidth(s),off.getHeight(s));
                g.setColor(Color.black);
                g.drawString(s,((con1.xCoor+con2.xCoor)/2)-off.getWidth(s)/2,((con1.yCoor+con2.yCoor)/2)-off.getHeight(s)/2);
            }
        }
    }
    
}
