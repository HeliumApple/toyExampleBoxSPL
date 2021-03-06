package ca.umontreal.iro.geodes.boxspl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import java.util.Scanner;

import box.BoxFactory;
import box.BoxModel;

//import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.*;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.SATSolver;
import org.logicng.solvers.MiniSat;


/**
 * 
 * This class demonstrates a toy example of an annotative product line. Thep^pp
 * domain model is a "Box" model, where a top Box contains a bunch of Box
 * objects. The comma-separated-values (CSV) file spl.csv contains the feature
 * model, expressed as a propositional constraint, as well as the feature
 * mapping.
 * <p>
 * The main method constructs the domain model, then reads the CSV file, and
 * constructs an SMT-LIB query for the Z3 smt solver.
 * <p>
 * The CSV file is constructed according to the following pattern:
 * <ul>
 * <li>A line where the first CSV column says "var" and the second contains all
 * feature variables, separated by ";"</li>
 * <li>A line where the first CSV column says "featureModel" and the second
 * contains an SMT-LIB representation of the Feature Model as a single
 * constraint.</li>
 * <li>In each following line, the first column is the id of a Box and the
 * second column is an SMT-LIB representation of its presence condition.</li>
 * </ul>
 * 
 * @author Michalis Famelis
 *
 */
public class Example {

	private static final String FILENAME = "spl.csv";

	private static final String LINE_DELIMITER = "\n";
	private static final String CSV_DELIMITER = ",";

	private static final String VAR_DECL_KEYWORD = "var";
	private static final String FEATURE_MODEL_DECL_KEYWORD = "featureModel";
	private static final String FEATURE_VAR_DELIMITER = ";";

	public static void main(String[] args) throws FileNotFoundException, ParserException {

		// Define the domain model as a bunch of boxes
		/*Box domainModel = new Box("top");
		Box a = new Box("a");
		Box b = new Box("b");
		Box c = new Box("c");
		domainModel.addBox(a);
		domainModel.addBox(b);
		domainModel.addBox(c);
		System.out.println(domainModel);*/
		
		
		BoxFactory factory = BoxFactory.eINSTANCE;
		
		//define the domain model
		box.BoxModel domainModel = factory.createBoxModel();
		
		box.Box a = factory.createBox();
		a.setId("a");
		
		box.Box b = factory.createBox();
		b.setId("b");
		
		box.Box c = factory.createBox();
		c.setId("c");
		
		domainModel.getBoxes().add(a);
		domainModel.getBoxes().add(b);
		domainModel.getBoxes().add(c);
		System.out.println(domainModel);

		// declare a List to store all the variable declarations
		ArrayList<String> featureVariables = new ArrayList<>();

		// let's store here the feature model
		String featureModel = "";

		// declare a HashMap to store the mapping between box objects and Z3 strings
		HashMap<box.Box, String> featureMapping = new HashMap<>();

		// parse the CSV file that contains the annotative SPL information
		Scanner sc = new Scanner(new File(FILENAME));
		sc.useDelimiter(LINE_DELIMITER);
		while (sc.hasNext()) {
			String line = sc.next();
			String[] parts = line.split(CSV_DELIMITER);

			// if parts[0] is var then we have all the variable declarations
			if (parts[0].trim().equals(VAR_DECL_KEYWORD)) {
				String allVariables = parts[1];
				String[] vars = allVariables.split(FEATURE_VAR_DELIMITER); // look in the CSV, I assume var decls are
																			// separated by ";"
				for (String v : vars)
					featureVariables.add(v.trim());

			} else if (parts[0].trim().equals(FEATURE_MODEL_DECL_KEYWORD)) { // we found the feature model constraint
				featureModel = parts[1].trim();

			} else { // the line is in fact a feature mapping
				String boxName = parts[0].trim();
				String presenceCondition = parts[1].trim();

				box.Box box = findBox(boxName, domainModel);
				if (box != null)
					featureMapping.put(box, presenceCondition);
			}
		}
		sc.close();

		System.out.println("-------------------------");

		// Now the variables list has all variable declarations
		System.out.println(featureVariables.toString());

		// Now the mapping HashMap contains pars of Box objects and Z3 strings
		System.out.println(featureMapping.toString());

		System.out.println("-------------------------");

		// So let's build the z3 string!
		/*String z3 = "; You can verify that this happens to be UNSAT at https://rise4fun.com/Z3 \n";

		// first the declare-fun for the boolean variables
		for (String v : featureVariables)
			z3 += "(declare-fun " + v + " () Bool) \n";

		// then the feature model constraint
		z3 += "(assert " + featureModel + ") \n";

		// and now the presence conditions
		for (box.Box x : featureMapping.keySet())
			z3 += "(assert " + featureMapping.get(x).toString() + ") \n";

		// and finally:
		z3 += "(check-sat) \n";
		
		System.out.println(z3);*/
		
		//create a new formula factory, a propositional parser and a SATSolver
		FormulaFactory f =new FormulaFactory();
		PropositionalParser p = new PropositionalParser(f);
		Formula formula;
		SATSolver miniSat;
		String z3="";
		
		for(box.Box aBox:featureMapping.keySet()) {
			z3=z3+featureMapping.get(aBox).toString();
			z3=z3+"&";
		}
		//remove the extra &
		z3=z3.substring(0,z3.length()-1);
		
			//parse the feature condition
			formula = p.parse(z3);
			//solve the condition
			miniSat = MiniSat.miniSat(f);
			miniSat.add(formula);
			//print if the condition and if it can be satisfied
			System.out.println(miniSat.sat());

	}

	private static box.Box findBox(String name, BoxModel domainModel) {
		List<box.Box> knownBoxes = domainModel.getBoxes();
		for (box.Box b : knownBoxes) 
			if (b.toString().trim().equals(name))
				return b;
		return null;
	}

}
