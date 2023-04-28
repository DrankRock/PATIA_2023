package satPlanner;

import java.io.IOException;

import fr.uga.pddl4j.planners.InvalidConfigurationException;

/**
 * Main class to use all the other and launch the solving
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Couldn't launch because of incorrect arguments. \nUsage : java Main.java <domain.pddl> <problem.pddl>");
		}
		
		String domain = args[0];
		String problem = args[1];
		
        Utils.dPrint("[Main] * Domain : '"+domain+"', Problem : '"+problem+"'");
		
		Utils.dPrint("[Main] * Creating OurPlanner");
		OurPlanner planner = new OurPlanner();
		try {
			Utils.dPrint("[Main] * Parsing problem");
			planner.setDomain(domain);
			planner.setProblem(problem);
			planner.parse();
			Utils.dPrint("[Main] * Solving problem");
			planner.solve();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
