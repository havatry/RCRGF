package vnreal.algorithms.rcrgf.test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ToGen {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		GenerateTopology generateTopology = new GenerateTopology();
		for (int snodes = 100; snodes <= 550; snodes += 50) {
			for (double ration = 0.01; ration < 0.105; ration += 0.01) {
				for (double alpha = 0.3; alpha < 1.25; alpha += 0.1) {
					generateTopology.setSnodes(snodes);
					generateTopology.setRation(ration);
					generateTopology.setAlhpa(alpha);
					generateTopology.write();
					System.out.println("write: " + generateTopology);
				}
			}
		}
	}
}
