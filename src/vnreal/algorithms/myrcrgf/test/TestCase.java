package vnreal.algorithms.myrcrgf.test;

import java.util.Arrays;

import org.junit.Test;

import vnreal.algorithms.myrcrgf.util.Utils;

public class TestCase {
	@Test
	public void testDistribution() {
		int[] v1 = new int[6];
		int[] v2 = new int[6];
		for (int i = 1; i <= 5; i++) {
			v1[i] = v1[i - 1] + Utils.exponentialDistribution(3.0 / 100);
			v2[i] = Utils.exponentialDistribution(1.0 / 500);
		}
		System.out.println(Arrays.toString(v1));
		System.out.println(Arrays.toString(v2));
	}
}
