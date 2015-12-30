/*
 * SimpleGraphView.java
 *
 * Created on March 8, 2007, 7:49 PM
 *
 * Copyright March 8, 2007 Grotto Networking
 */



import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class SimpleGraphView2 {
	DirectedSparseMultigraph<Integer, String> g;
    int edgeCount = 0;
    /** Creates a new instance of SimpleGraphView */
   /* public SimpleGraphView2() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        // Note showing the use of a SparseGraph rather than a SparseMultigraph
        g = new SparseGraph<Integer, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex((Integer)1);
        g.addVertex((Integer)2);
        g.addVertex((Integer)3); 
        // g.addVertex((Integer)1);  // note if you add the same object again nothing changes
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", 2, 3);  
    }*/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	SimpleGraphView2 sgv = new SimpleGraphView2();
       /* SimpleGraphView2 sgv = new SimpleGraphView2(); // This builds the graph
        // Layout<V, E>, VisualizationComponent<V,E>
        Layout<Integer, String> layout = new CircleLayout(sgv.g);
        layout.setSize(new Dimension(300,300));
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350));       
        // Setup up a new vertex to paint transformer...
        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };  
        
        DijkstraShortestPath alg = new DijkstraShortestPath(sgv.g, vertexPaint);
        		 List l = alg.getPath(sgv., n4);
        		 Number dist = alg.getDistance(n1, n4);
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
             BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);        
        
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);     */
    	
    	sgv.g = new DirectedSparseMultigraph();
    	 // Create some MyNode objects to use as vertices
    	//MyNode n1 = new MyNode(1); 
    	
    	MyNode n1 = new MyNode(1);
    	MyNode n2 = new MyNode(2);
    	MyNode n3 = new MyNode(3); 
    	MyNode n4 = new MyNode(4); 
    	MyNode n5 = new MyNode(5); 	// note n1-n5 declared elsewhere.
    	
    	sgv.g.addVertex(n1.id);
    	sgv.g.addVertex(n2.id);
    	sgv.g.addVertex(n3.id);
    	sgv.g.addVertex(n4.id);
    	sgv.g.addVertex(n5.id);
    	
    	//sgv.g.
    	 // Add some directed edges along with the vertices to the graph
    	 sgv.g.addEdge((new MyLink(2.0, 48)).toString(),n1.id, n2.id, EdgeType.DIRECTED); // This method
    	 sgv.g.addEdge((new MyLink(2.0, 48)).toString(),n2.id, n3.id, EdgeType.DIRECTED);
    	 sgv.g.addEdge((new MyLink(3.0, 192)).toString(), n3.id, n5.id, EdgeType.DIRECTED); 
    	 sgv.g.addEdge((new MyLink(2.0, 48)).toString(), n5.id, n4.id, EdgeType.DIRECTED); // or we can use
    	 sgv.g.addEdge((new MyLink(2.0, 48)).toString(), n4.id, n2.id); // In a directed graph the
    	 sgv.g.addEdge((new MyLink(2.0, 48)).toString(), n3.id, n1.id); // first node is the source 
    	 sgv.g.addEdge((new MyLink(10.0, 48)).toString(), n2.id, n5.id);// and the second the destination
    }
    
	class MyNode {
		int id; // good coding practice would have this as private

		public MyNode(int id) {
			this.id = id;
		}

		public String toString() { // Always a good idea for debuging
			return "V" + id; // JUNG2 makes good use of these.
		}
	}

	class MyLink {
		double capacity; // should be private
		double weight; // should be private for good practice
		int id;

		public MyLink(double weight, double capacity) {
			this.id = edgeCount++; // This is defined in the outer class.
			this.weight = weight;
			this.capacity = capacity;
		}

		public String toString() { // Always good for debugging
			return "E" + id;
		}

	}
    
}