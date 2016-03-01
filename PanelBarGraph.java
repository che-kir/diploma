package formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelBarGraph extends JPanel implements ComponentListener
{
	private int columns;//���������� �������
	private int width, height;
	private int border, space;//����� �� ������� ������ � ������������ ����� ���������
	private float [] bars;
	private int [] drawBars;//integer value of bar(in px)
	
	
	
	public PanelBarGraph(float[] values, int width, int height, int border, int space)
	{
		this.columns = values.length;
		this.width = width;
		this.height = height;
		this.border = border;
		this.space = space;
		
		this.addComponentListener(this);
		
		bars = values;
		drawBars = new int[columns];
		
		
		setSize(width, height);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		// '- 30' at height - is a adjustment of vertical text position
		super.paint(g);
		width = getWidth();
		height = getHeight();
		
		int w = (width - border*2  - space * (columns-1)) / columns;
		g.setColor(Color.BLUE);
		
		
		g.drawRect( border , border, w * columns + space*(columns-1), height - 2 * border - 30);
		for(int i = 0; i < columns; ++i)
		{
			int sw = (int)g.getFontMetrics().getStringBounds(""+bars[i], g).getWidth() / 2;
			
			g.drawString(""+bars[i], border + i*(space+w) + w/2 - sw, height - 30 - border + 15*(i%2 + 1) );
			g.fillRect(border + i*(space+w), height - border - drawBars[i] - 30, w, drawBars[i]);
		}
	}
	
	private void calcDrawBars()
	{
		double max;
		max = bars[0];
		for(int i = 1; i < columns; ++i)
		{
			if(bars[i] > max)
				max = bars[i];
		}
		// '- 30' at height - is a adjustment of vertical text position
		for(int i = 0; i < columns; ++i)
		{
			//System.out.println( (int)(bars[i] / max * (height - 30 - (2.0 * border))) );
			drawBars[i] = (int)(bars[i] / max * (height - 30 - (2.0 * border)));
		}
	}

	

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		calcDrawBars();
		updateUI();
	}

	@Override
	public void componentHidden(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentShown(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}

/*package formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelBarGraph extends JPanel implements ComponentListener
{
	private int columns;//���������� �������
	private int width, height;
	private int border, space;//����� �� ������� ������ � ������������ ����� ���������
	private double [] bars;
	private int [] drawBars;//integer value of bar(in px)
	
	
	
	public PanelBarGraph(int columns, int width, int height, int border, int space)
	{
		this.columns = columns;
		this.width = width;
		this.height = height;
		this.border = border;
		this.space = space;
		
		this.addComponentListener(this);
		
		bars = new double[columns];
		drawBars = new int[columns];
		double v = 1.25;
		for(int i = 0; i < columns; ++i)
			bars[i] = v * (i+1);
		
		setSize(width, height);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		// '- 30' at height - is a adjustment of vertical text position
		super.paint(g);
		width = getWidth();
		height = getHeight();
		
		int w = (width - border*2  - space * (columns-1)) / columns;
		g.setColor(Color.BLUE);
		
		
		g.drawRect( border , border, w * columns + space*(columns-1), height - 2 * border - 30);
		for(int i = 0; i < columns; ++i)
		{
			int sw = (int)g.getFontMetrics().getStringBounds(""+bars[i], g).getWidth() / 2;
			
			g.drawString(""+bars[i], border + i*(space+w) + w/2 - sw, height - 30 - border + 15*(i%2 + 1) );
			g.fillRect(border + i*(space+w), height - border - drawBars[i] - 30, w, drawBars[i]);
		}
	}
	
	private void calcDrawBars()
	{
		double max;
		max = bars[0];
		for(int i = 1; i < columns; ++i)
		{
			if(bars[i] > max)
				max = bars[i];
		}
		// '- 30' at height - is a adjustment of vertical text position
		for(int i = 0; i < columns; ++i)
		{
			//System.out.println( (int)(bars[i] / max * (height - 30 - (2.0 * border))) );
			drawBars[i] = (int)(bars[i] / max * (height - 30 - (2.0 * border)));
		}
	}

	

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		calcDrawBars();
		updateUI();
	}

	@Override
	public void componentHidden(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentShown(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
*/