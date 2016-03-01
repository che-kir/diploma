package spectrex;
/*
import java.util.Scanner;
import com.googlecode.fannj.Fann;
import javax.swing.JOptionPane;

//import java.;

public class NNClassifier {

    private Fann ann;

    public NNClassifier(String path) {
        String mylib = System.getProperty("java.library.path");
     //   Runtime.getRuntime().loadLibrary("fannfloat");
       // System.loadLibrary("fannfloat");
        System.setProperty("java.library.path", mylib);

        System.loadLibrary("fannfloat");
        

      //  System.load("C:\\Windows\\System32\\fannfloat.dll");
    //    System.out.println(System.getProperty("java.library.path"));
     //   System.load("C:\\Users\\Kirill\\Desktop\\projectLug\\spectrex\\bin\\fannfloat.dll");
        ann = new Fann(path);
    }

    
    public int classify(float[] inputs) {
        if (inputs.length != 32) {
            return -1;
        }

        float[] outputs;
        String value = "";

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] /= 100;
        }

        outputs = ann.run(inputs);

        //we need to leave only the maximum element here
        float max = -1;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
            }
        }
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == max) {
                value += '1';
            } else {
                value += '0';
            }
        }

        return Integer.parseInt(value, 2);
    }

    public static void main(String[] args) {     
         NNClassifier nn = new NNClassifier("trmse0.net");
        float[] inp = new float[32];
        try (Scanner in = new Scanner(System.in)) {
            for (int i = 0; i < 32; i++) {
                inp[i] = (float) in.nextDouble();
            }
            System.out.print(nn.classify(inp));
        }
    }
}
*/


import java.util.Scanner;
import com.googlecode.fannj.Fann;


public class NNClassifier {
	
	private Fann annp, annb, annn, annv, annh;
	
	public NNClassifier(String path)
	{
		annp = new Fann(path+"p-a.net");
		annb = new Fann(path+"b-a.net");
		annn = new Fann(path+"n-a.net");
		//annv = new Fann(path+"v-a.net");
		//annh = new Fann(path+"h-a.net");
	}
	
    /**
     * Computes the class of input data
     * 
     * @param  inputs - an array of 32 float values in any range
     * @return integer - the code of class. Can be 1, 2 or 4. (001, 010, 100)
     */
	public int classify(float [] inputs)
	{
		if(inputs.length != 32) return -1;
		
		float [] outputs;
		
		for(int i = 0 ; i < inputs.length; i++) inputs[i] /= 100;
		
		outputs = annb.run(inputs);            
		if(outputs[0] > 0.5) return 2;
		else
		{
		        outputs = annp.run(inputs);
		        if(outputs[0] > 0.5) return 3;
		        else
		        {
		                outputs = annn.run(inputs);
		                if(outputs[0] > 0.5) return 1;
		                else return -1;
		        }
		}
	}


	
	public static void main(String [] args)
	{

		NNClassifier nn = new NNClassifier("ova93735\\");
		float []inp = new float[32];
				
		int trues = 0, falses = 0;
		
		Scanner in = new Scanner(System.in);
		int cnt = in.nextInt();
		for(int c = 0; c < cnt; c++)
		{
			for(int i = 0; i < 32; i++)
			{
				inp[i] = (float)in.nextDouble();
			}
			int pr = nn.classify(inp);
			int res = in.nextInt();
			String t = Integer.toString(c) + " > " +Integer.toString(pr) + " : " + Integer.toString(res);
			if(pr == res)
			{
				trues++;
				
			}
			else
			{
				falses++;
			}
			System.out.println(t);

		}
		System.out.println("==============================================");
		float percents = (float)trues/(float)cnt;
		System.out.println(Float.toString(percents*100)+"%, true: "+Integer.toString(trues)+", false: "+Integer.toString(falses));
		in.close();	
	}
}
/*
   	42,75	37,33	10,99	4,34	   	1,44	2,18	   	   	   	   	   	   	   	   	   	   	0,55	0,4	   	   	   	   	   	   	   	   	   	   	   	   


 0 26,38 61,91 11,71
0 0 0 0 
0 0 0 0 
0 0 0 0 
0 0 0 0 
0 0 0 0 
0 0 0 0 
0 0 0 0 
0 0 0 0
 
60 60 60 60
60 60 60 60
60 60 60 60
60 60 60 60
60 60 60 60
60 60 60 60
60 60 60 60
60 60 60 60



68,35	30,56	44,04	9,78	
2,18	2,57	2,20	1,69	
5,76	6,95	3,08	0,07	
6,76	8,86	3,43	6,36	
9,64	3,11	2,34	0,88	
0,45	1,77	0,28	0	
0	0	0	0	
0,22	0,23	0,05	0

*/