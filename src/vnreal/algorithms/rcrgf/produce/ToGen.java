package vnreal.algorithms.rcrgf.produce;

import java.io.FileInputStream;
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
            double ration = Double.parseDouble(properties.getProperty("rcrgf.ratio")),
                    alpha = Double.parseDouble(properties.getProperty("rcrgf.alpha"));
            generateTopology.setSnodes(snodes);
            generateTopology.setRation(ration);
            generateTopology.setAlhpa(alpha);
            generateTopology.write();
            System.out.println("write: " + generateTopology);
        }
	}
}
