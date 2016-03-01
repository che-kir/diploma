package formas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BarGraph extends JFrame implements ActionListener
{
	public PanelBarGraph panel;
	
        /**
         * 
         * @param values
         * array of numbers to show in graph
         * @param width
         * width of popup window (px.)
         * @param height
         * height of popup window (px.)
         * @param border
         * free space between graph and window border (px.)
         * @param space 
         * free space between columns of graph (px.)
         */
	public BarGraph(float []values, int width, int height, int border, int space)
	{
		panel = new PanelBarGraph(values, width, height, border, space);
              //  new PanelBarGraph(border, width, height, border, space);
		
		this.setVisible(true);
		this.setContentPane(panel);
		//this.setVisible(true);
		// setup frame
		this.setTitle("BarGraph");
		this.setSize(width, height);
		this.setResizable(true);
		this.setLocationRelativeTo(null);//Set location to center
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		

	}

}


/*package formas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BarGraph extends JFrame implements ActionListener
{
	private PanelBarGraph panel;
	
	public BarGraph(int columns, int width, int height, int border, int space)
	{
		panel = new PanelBarGraph(columns, width, height, border, space);
		
		this.setVisible(true);
		this.setContentPane(panel);
		//this.setVisible(true);
		// setup frame
		this.setTitle("BarGraph");
		this.setSize(width, height);
		this.setResizable(true);
		this.setLocationRelativeTo(null);//Set location to center
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		

	}
	
        
        public static void main(String [] args)
        {
            new BarGraph(100, 100, 100, 100, 100);
        }

}
*/