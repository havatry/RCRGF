package vnreal.algorithms.argf.util;

public class Statistics {
	private long startTime;
	private long endTime;
	private int totalVns;
	private int succVns;
	private double RevenToCost;
	private double AverageRevenToCost;
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
	public int getTotalVns() {
		return totalVns;
	}
	public void setTotalVns(int totalVns) {
		this.totalVns = totalVns;
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
	public double getAverageRevenToCost() {
		return AverageRevenToCost;
	}
	public void setAverageRevenToCost(double averageRevenToCost) {
		AverageRevenToCost = averageRevenToCost;
	}
	
	@Override
	public String toString() {
		//Created method stubs
		return "Process time is " + (endTime - startTime) + "ms";
	}
}
