/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectrex;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ReaLgressA
 */
public class ChartData 
{
    private final static String[] rows = {
        "2", "3", "4", "5", "6", "8", "11", "15",
        "20", "26", "36", "40", "65", "85", "120", "150",
        "210", "290", "300", "520", "700", "950", "1300", "1700",
        "2300", "3100", "4200", "5600", "7600", "10200", "13800", "18500"}; 

    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    /**
     * Add the new group to the chart data
     * @param groupName will be shown as row label in a chart legend. Every group name will have its own bar colour.
     * @param values array of values. Array length shouldn't be more than 32 elements
     */
    public void addData(String groupName, float values[])
    {
        for(int i = 0; i < values.length && i < rows.length; ++i)
        {
            dataset.addValue(values[i], groupName, rows[i]);
        }
    }
    
    public DefaultCategoryDataset getDataset()
    {
        return dataset;
    }
}
