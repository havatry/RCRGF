package vnreal.algorithms.myAdapter;

import vnreal.algorithms.myRCRGF.test.GenerateTopology;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ToGenRCRGFAdapter {
    public String generateFile(int snode, double resourceRatio, double nodeRatio) throws IOException {
        GenerateTopology generateTopology = new GenerateTopology();
        generateTopology.setSnodes(snode);
        generateTopology.setRation(resourceRatio);
        generateTopology.setNodes_ration(nodeRatio);
        return generateTopology.write();
    }
}
