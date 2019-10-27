package vnreal.algorithms.argf.util;

import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNode;

public class DTOVirtual {
	private VirtualNode upnode;
//	private VirtualNode downnode;
	private VirtualLink uplink;
//	private VirtualLink downlink;
	public VirtualNode getUpnode() {
		return upnode;
	}
	public void setUpnode(VirtualNode upnode) {
		this.upnode = upnode;
	}
//	public VirtualNode getDownnode() {
//		return downnode;
//	}
//	public void setDownnode(VirtualNode downnode) {
//		this.downnode = downnode;
//	}
	public VirtualLink getUplink() {
		return uplink;
	}
	public void setUplink(VirtualLink uplink) {
		this.uplink = uplink;
	}
//	public VirtualLink getDownlink() {
//		return downlink;
//	}
//	public void setDownlink(VirtualLink downlink) {
//		this.downlink = downlink;
//	}
}
