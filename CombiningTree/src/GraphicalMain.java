import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GraphicalMain extends JPanel
{
	static JFrame jfram;
	final static int N = 5;
	static final int X = 850;
	static final int Y = 600;
	static JButton buttons[]=new JButton[N];
	public static Semaphore threadControl[]=new Semaphore[N];
	public static final String ABCD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static CombiningTree counter = new CombiningTree((int) Math.pow(2,Math.ceil(Math.log10(N) / Math.log10(2))));
	static JTextField textflds[]=new JTextField[N];
	public GraphicalMain()                       // set up graphics window
    {
        super();
        setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g)  // draw graphics in the panel
    {
    	
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels

        super.paintComponent(g);            // call superclass to make panel display correctly

        // Drawing code goes here
        //while(true)
        counter.refreshTree(counter, g);
        System.out.println("refresh done");
    }

	
    public static void main(String[] args)
    {
    	JPanel controlPanel=new JPanel();
    	GraphicalMain panel = new GraphicalMain();                   // window for drawing
    	panel.setLayout(null);
    	jfram = new JFrame();                            // the program itself
        jfram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // set frame to exit
        
        System.out.println("main :"+Thread.currentThread().getId());
		Thread t[] = new Thread[N];
		for (int i = 0; i < N; i++) {
			final int j = i;
			final JButton jb=new JButton("Thread "+ABCD.charAt(i));
			buttons[i]=jb;
			JTextField jtf=new JTextField(ABCD.charAt(i)+":Starting",14);
			jtf.setEditable(false);
			textflds[i]=jtf;
			threadControl[j]=new Semaphore(0);
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tmpb=jb;
					jfram.repaint();
					System.out.println("Button"+j);
					tmpb.setEnabled(false);
					threadControl[j].release();
				}
				
			});
	        
			t[i] = new Thread(new MyRunnable(counter,j));
			t[i].setName(j+"");
			t[i].start();
		}
		for (int i = 0; i < N; i++) {
			textflds[i].setBounds((int)((i)*X*0.2)+8,450,(int)(X*0.19), 25);
			buttons[i].setBounds((int)((i)*X*0.2)+8,500,(int)(X*0.19), 25);
			panel.add(buttons[i]);
			panel.add(textflds[i]);
		}
		//jfram.setLayout(null);
		//GridBagConstraints c= new GridBagConstraints();
		
		jfram.add(panel);
		jfram.setSize(X+8,Y);
        jfram.setVisible(true); 
        
		// wait on philosopher
		try {
			for (int i = 0; i < N; i++)
				t[i].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    static void enableButton(int threadId,String threadStatus){
    	buttons[threadId].setEnabled(true);
    	textflds[threadId].setText(threadStatus);
    	jfram.repaint();
    }
    static void setStatus(int threadId,String threadStatus){
    	textflds[threadId].setText(threadStatus);
    	jfram.repaint();
    }

}