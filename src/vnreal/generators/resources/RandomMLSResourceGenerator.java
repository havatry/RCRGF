package vnreal.generators.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tests.generators.GeneratorParameter;
import vnreal.algorithms.utils.mls.MLSLattice;
import vnreal.constraints.resources.MLSResource;
import vnreal.core.oldFramework.ConversionHelper;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;

/**
 * The random {@link MLSResource} Generator 
 * 
 * @author Fabian Kokot
 *
 */
@GeneratorParameter(
		parameters = { "Networks:Networks", "TR:MLS_SecLevels", "TR:MLS_NumCategories" }
		)
public class RandomMLSResourceGenerator extends AbstractResourceGenerator<List<MLSResource>> {

	
	@Override
	public List<MLSResource> generate(ArrayList<Object> parameters) {
		Random random = new Random();
		
		NetworkStack ns = (NetworkStack)parameters.get(0);
		Integer sLevels = ConversionHelper.paramObjectToInteger(parameters.get(1));
		Integer sCats = ConversionHelper.paramObjectToInteger(parameters.get(2));
		
		MLSLattice lattice = createLattice(sLevels, sCats);
		
		SubstrateNetwork sNetwork = ns.getSubstrate();
		
		int maxLevel = lattice.getNumberOfLevels() - 1;
		ArrayList<String> cats = lattice.getCategories();
		
		ArrayList<MLSResource> resList = new ArrayList<MLSResource>();
		
		//Label all Substratenodes (actual Random)
		for (SubstrateNode n :  sNetwork.getVertices()) {
			//Create Random resource
			int resDem = (int)((maxLevel + 1) * random.nextDouble());
			int resProv = (int)((maxLevel + 1) * random.nextDouble());
			
			//we need at least one Category
			int countCat;
			do {
				countCat = (int)((cats.size()+1) * random.nextDouble());
			} while (countCat < 0);
			ArrayList<String> chosenCats = new ArrayList<String>();
			for (int c = 0; c < countCat; c++) {
				String cat;
				do {
					cat = cats.get((int)(cats.size() * random.nextDouble()));
				} while (chosenCats.contains(cat));
				chosenCats.add(cat);
			}
			
			MLSResource res = new MLSResource(n, resDem, resProv, chosenCats);
			n.add(res);
			resList.add(res);
		}
		return resList;
	}

	/**
	 * Create the lattice
	 * 
	 * @param sLevels  Number of Levels
	 * @param sCats Number of Categories
	 * @return {@link MLSLattice}
	 */
	public static  MLSLattice createLattice(Integer sLevels, Integer sCats) {
		ArrayList<String> cats = new ArrayList<String>();
		for(int i = 0; i < sCats; i++) {
			cats.add("Category_"+i);
		}
		return new MLSLattice(sLevels, cats);

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
