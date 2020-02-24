package vnreal.algorithms.rcrgf.util;

public class Statistics {
	private long startTime;
	private long endTime;
	private int succVns;
	private double RevenToCost;
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getSuccVns() {
		return succVns;
	}
	public void setSuccVns(int succVns) {
		this.succVns = succVns;
	}
	public double getRevenToCost() {
		return RevenToCost;
	}
	public void setRevenToCost(double revenToCost) {
		RevenToCost = revenToCost;
	}
	
	@Override
	public String toString() {
		//Created method stubs
		return (endTime - startTime) + "," + succVns + "," + RevenToCost;
	}
}
