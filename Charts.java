/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectrex;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author ReaLgressA
 */
public class Charts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ChartData data = new ChartData();
        float values[] = new float[32];
        for(int i = 0; i < values.length; ++i)
        {
            values[i] = i;
        }
        data.addData("group #1", values);
       /* for(int i = values.length - 1; i >= 0; --i)
        {
            values[i] = (31 - i) * 3.71;
        }
        data.addData("group #2", values);
        for(int i = values.length - 1; i >= 0; --i)
        {
            values[i] = (31 - i) * 1.71;
        }
        data.addData("group #3", values);
        for(int i = values.length - 1; i >= 0; --i)
        {
            values[i] = (31 - i) * 2.22;
        }
        data.addData("group #4", values);*/
        Charts.createBarChart(data, "TestTitle", "Column label", "Row label", new Dimension(800, 600));
    }

    /**
     * @param data initial data for chart creation
     * @param title text which would be displayed at the top of the frame and in
     * the frame's title
     * @param columnLabel text which would be displayed on the left of the
     * Y-axis
     * @param rowLabel text which would be displayed under X-axis
     * @param frameSize size of chart frame
     * @return Bar chart JFrame link
     * @author ReaLgressA
     */
    public static JFrame createBarChart(ChartData data, String title, String columnLabel, String rowLabel, Dimension frameSize) 
    {
        JFreeChart chart = ChartFactory.createStackedBarChart(title, columnLabel, rowLabel, data.getDataset(), PlotOrientation.VERTICAL, true, true, true);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);
        ChartFrame frame = new ChartFrame(title, chart, true);
        frame.setVisible(true);
        frame.setSize(frameSize);
        frame.setResizable(true);
        return (JFrame) frame;
    }
}
