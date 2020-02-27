package vnreal.algorithms.myRCRGF.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ToGen {
	public static void main(String[] args) throws IOException {
		GenerateTopology generateTopology = new GenerateTopology();
        Properties properties = new Properties();
        properties.load(new FileInputStream("results/conf/rcrgf.properties"));
        for (int snodes = Integer.parseInt(properties.getProperty("rcrgf.snode.min"));
             snodes < Integer.parseInt(properties.getProperty("rcrgf.snode.max"));
             snodes += Integer.parseInt(properties.getProperty("rcrgf.snode.step"))) {
//			for (double ration = 0.01; ration < 0.105; ration += 0.01) {
            double ration = Double.parseDouble(properties.getProperty("rcrgf.ratio")),
                    alpha = Double.parseDouble(properties.getProperty("rcrgf.alpha"));
//			for (double ration = 0.01; ration < 0.105; ration += 0.01) {
//				for (double alpha = 0.3; alpha < 1.25; alpha += 0.1) {
            generateTopology.setSnodes(snodes);
            generateTopology.setRation(ration);
            generateTopology.setAlhpa(alpha);
            generateTopology.write();
            System.out.println("write: " + generateTopology);
        }
//			}
//		}
	}
}
