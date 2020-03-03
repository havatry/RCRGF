package vnreal.algorithms.myAdapter;

import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.AvailableResources;
import vnreal.algorithms.CoordinatedMapping;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myAEF.strategies.AEFAlgorithm;
import vnreal.algorithms.myAEF.strategies.NRMAlgorithm;
import vnreal.algorithms.myRCRGF.core.RCRGFStackAlgorithm;
import vnreal.algorithms.myRCRGF.util.SubgraphIsomorphismCompare;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * �����㷨ӳ�����һ����������ʱ д����
 */
public class EventProcess {
    private String format_text;
    private Path p;
    private AbstractAlgorithm algorithm;

    public EventProcess(AbstractAlgorithm algorithm, int id, String type) {
        // ����Ŀ¼
        p = Paths.get("results/data", String.valueOf(id));
        if (!Files.exists(p)) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.algorithm = algorithm;
        switch (type) {
            case "�ֶ�����":
                format_text = convert(algorithm) + "_VNE.txt";
                break;
            case "һ�׶�ʵ��":
                format_text = convert(algorithm) + "_%d_VNE.txt";
                break;
            case "���׶�Э��ʵ��":
                format_text = convert(algorithm) + "_VNE_%d.txt";
                break;
            default:
                format_text = "";
                break;
        }
    }

    public void process(int index, SubstrateNetwork substrateNetwork,
                               VirtualNetwork virtualNetwork, boolean succ) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(Files.newOutputStream(p.resolve(String.format(format_text, index))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("��������:");
        out.println(substrateNetwork);
        out.println("��������:");
        out.println(virtualNetwork);
        out.println();
        if (succ) {
            out.println("�ڵ�ӳ��:");
            out.println(nodeMapFromAlg());
            out.println("��·ӳ��:");
            out.println(linkMapFromAlg());
            out.println();
            out.println("ӳ��ɹ�");
        } else {
            out.println("ӳ��ʧ��");
        }
        out.close();
        // ���ӳ��
        nodeMapFromAlg().clear();
        linkMapFromAlg().clear();
    }

    private String convert(AbstractAlgorithm algorithm) {
        if (algorithm instanceof  AEFAlgorithm) {
            if (((AEFAlgorithm)algorithm).isAdvanced()) {
                return "AEF_Advance";
            } else {
                return "AEF_Baseline";
            }
        } else if (algorithm instanceof SubgraphIsomorphismStackAlgorithm
                || algorithm instanceof SubgraphIsomorphismCompare) {
            return "SubgraphIsomorphism";
        } else if (algorithm instanceof NRMAlgorithm) {
            return "NRM";
        } else if (algorithm instanceof CoordinatedMapping) {
            return "D-ViNE_SP";
        } else if (algorithm instanceof AvailableResources) {
            return "Greedy";
        } else if (algorithm instanceof RCRGFStackAlgorithm) {
            return "RCRGF";
        } else {
            return "NULL";
        }
    }

    private Map<VirtualNode, SubstrateNode> nodeMapFromAlg() {
        if (algorithm instanceof AEFAlgorithm) {
            return ((AEFAlgorithm)algorithm).getNodeMapping();
        } else if (algorithm instanceof SubgraphIsomorphismStackAlgorithm) {
            return ((SubgraphIsomorphismStackAlgorithm)algorithm).getNodeMapping();
        } else if (algorithm instanceof SubgraphIsomorphismCompare) {
            return ((SubgraphIsomorphismCompare)algorithm).getNodeMapping();
        } else if (algorithm instanceof NRMAlgorithm) {
            return ((NRMAlgorithm)algorithm).getNodeMapping();
        } else if (algorithm instanceof CoordinatedMapping) {
            return ((CoordinatedMapping)algorithm).getNodeMapping();
        } else if (algorithm instanceof AvailableResources) {
            return ((AvailableResources)algorithm).getNodeMapping();
        } else if (algorithm instanceof RCRGFStackAlgorithm) {
            return ((RCRGFStackAlgorithm)algorithm).getNodeMapping();
        } else {
            return null;
        }
    }

    private Map<VirtualLink, List<SubstrateLink>> linkMapFromAlg() {
        if (algorithm instanceof AEFAlgorithm) {
            return ((AEFAlgorithm)algorithm).getLinkMapping();
        } else if (algorithm instanceof SubgraphIsomorphismStackAlgorithm) {
            return ((SubgraphIsomorphismStackAlgorithm)algorithm).getLinkMapping();
        } else if (algorithm instanceof SubgraphIsomorphismCompare) {
            return ((SubgraphIsomorphismCompare)algorithm).getLinkMapping();
        } else if (algorithm instanceof NRMAlgorithm) {
            return ((NRMAlgorithm)algorithm).getLinkMapping();
        } else if (algorithm instanceof CoordinatedMapping) {
            return ((CoordinatedMapping)algorithm).getLinkMapping();
        } else if (algorithm instanceof AvailableResources) {
            return ((AvailableResources)algorithm).getLinkMapping();
        } else if (algorithm instanceof RCRGFStackAlgorithm) {
            return ((RCRGFStackAlgorithm)algorithm).getLinkMapping();
        } else {
            return null;
        }
    }
}
