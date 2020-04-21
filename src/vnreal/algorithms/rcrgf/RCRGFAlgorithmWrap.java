package vnreal.algorithms.rcrgf;

import junit.framework.Assert;
import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.network.Network;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualNetwork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static vnreal.algorithms.rcrgf.Util.*;

/**
 * Created on 2020/4/19
 * 调用算法的格式封装, 内部含有实际的算法
 */
public class RCRGFAlgorithmWrap extends AbstractAlgorithm{
    private RCRGFAlgorithm algorithm;
    private Iterator<VirtualNetwork> curIt = null;
    private Iterator<? extends Network<?, ?, ?>> curNetIt = null;

    //----------------测量指标
    private boolean success;
    private int execution;
    private double costToRevenue;

    public RCRGFAlgorithmWrap(NetworkStack network) {
        this.ns = network;
        this.algorithm = new RCRGFAlgorithm();
    }

    @Override
    protected boolean preRun() {
        // 预处理阶段
        Assert.assertTrue("the virtual network must be one", ns.getVirtuals().size() == 1);
        VirtualNetwork virtualNetwork = ns.getVirtuals().get(0);
        return RemoveEdge.work(virtualNetwork);
    }

    @Override
    protected void evaluate() {
        // 这里只处理一个底层网络和一个虚拟网络的情况
        long start = System.currentTimeMillis();
        if (hasNext()) {
            VirtualNetwork virtualNetwork = getNext();
            success = algorithm.work(ns.getSubstrate(), virtualNetwork);
        }
        execution = (int) (System.currentTimeMillis() - start);
        // 计算代价收益比
        costToRevenue = computeCostToRevenue(algorithm.getNodeLinkMapping());
    }

    @Override
    protected void postRun() {

    }

    protected boolean hasNext() {
        if (curNetIt == null) {
            curNetIt = ns.iterator();
        }
        if (curIt == null || !curIt.hasNext()) {
            if (curNetIt.hasNext()) {
                curNetIt.next();
                curIt = (Iterator<VirtualNetwork>) curNetIt;
                return hasNext();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    protected VirtualNetwork getNext() {
        if (!hasNext())
            return null;
        else {
            return curIt.next();
        }
    }

    // 返回结果计量参数
    @Override
    public List<AbstractAlgorithmStatus> getStati() {
        // 返回执行时间、接受率、收益代价比
        List<AbstractAlgorithmStatus> list = new ArrayList<>(4);
        list.add(new AbstractAlgorithmStatus("Mapped Success") {
            @Override
            public Number getValue() {
                return success ? 1 : 0;
            }

            @Override
            public Number getMaximum() {
                return 1.0;
            }
        });

        list.add(new AbstractAlgorithmStatus("Execution Rime") {
            @Override
            public Number getValue() {
                return execution;
            }

            @Override
            public Number getMaximum() {
                return 1.0;
            }
        });

        list.add(new AbstractAlgorithmStatus("Cost to Revenue") {
            @Override
            public Number getValue() {
                return costToRevenue;
            }

            @Override
            public Number getMaximum() {
                return 1.0;
            }
        });

        return list;
    }
}
