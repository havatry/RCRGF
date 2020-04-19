package vnreal.algorithms.rcrgf;

import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.myRCRGF.core.RCRGFAlgorithm;
import vnreal.network.Network;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualNetwork;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 2020/4/19
 * 调用算法的格式封装, 内部含有实际的算法
 */
public class RCRGFAlgorithmWrap extends AbstractAlgorithm{
    private RCRGFAlgorithm algorithm;
    private Iterator<VirtualNetwork> curIt = null;
    private Iterator<? extends Network<?, ?, ?>> curNetIt = null;

    public RCRGFAlgorithmWrap(NetworkStack network) {
        this.ns = network;
        this.algorithm = new RCRGFAlgorithm();
    }

    @Override
    protected boolean preRun() {
        return false;
    }

    @Override
    protected void evaluate() {

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

    @Override
    public List<AbstractAlgorithmStatus> getStati() {
        return null;
    }
}
