package satPlanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.planners.AbstractPlanner;
import fr.uga.pddl4j.planners.ProblemNotSupportedException;
import fr.uga.pddl4j.planners.SearchStrategy;
import fr.uga.pddl4j.planners.statespace.search.StateSpaceSearch;
import fr.uga.pddl4j.problem.DefaultProblem;
import fr.uga.pddl4j.problem.Fluent;
import fr.uga.pddl4j.problem.InitialState;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.numeric.NumericAssignment;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.problem.operator.Condition;
import fr.uga.pddl4j.problem.operator.ConditionalEffect;
import fr.uga.pddl4j.problem.operator.Effect;
import fr.uga.pddl4j.util.BitSet;
import fr.uga.pddl4j.util.BitVector;
import picocli.CommandLine;

/**
 * OurPlanner class is used to plan, solve etc the problem.
 * It initializes everything and calls the execution loop
 * @author matvei
 *
 */
public class OurPlanner extends AbstractPlanner {
	private static final long serialVersionUID = 1L;
	
	private int DEFAULT_TIMEOUT = 3600; // 3600 sec is an hour
	private PddlToProblem pddlToProblem ; //translate the problem to a Sat4J solver


	@Override
	public Problem instantiate(DefaultParsedProblem arg0) {
		Utils.dPrint("[OurPlanner] * Instantiate problem", 0);
        final Problem pb = new DefaultProblem(arg0);
        pb.instantiate();
        Utils.dPrint("[OurPlanner] * Problem successfully instantiated", 1);
        if (isSupported(pb)) {
        	Utils.dPrint("[OurPlanner] * Problem supported !", 1);
        }
        return pb;
	}
	
	@Override
	public boolean isSupported(Problem arg0) {
		Utils.dPrint("[OurPlanner] * Check if problem is supported",0);
		// Not knowing how to make this function, we will always assume it's supported
		return true;
	}

	@Override
	public Plan solve(Problem problem) throws ProblemNotSupportedException {
		Utils.dPrint("[OurPlanner] * Solve the problem");
		try {
			Utils.dPrint("[OurPlanner] * * create pddlToProblem", 4);
			pddlToProblem = new PddlToProblem(problem);
			
			Utils.dPrint("[OurPlanner] * * process initial state", 4);
			pddlToProblem.processInitialState();

			Utils.dPrint("[OurPlanner] * * Create ISolver", 4);
			ISolver iSolver = null;
			boolean satisfyable = false;
			int deepness = 0;
			do {
				deepness ++ ;
				Utils.dPrint("[OurPlanner] * Running solver, step "+deepness);
				try {
					iSolver = nextSolver();
					satisfyable = iSolver.isSatisfiable();
				} catch (ContradictionException ex) {
					Utils.dPrint("Exception caught on step "+deepness, 3);
				}
			} while (!satisfyable);

			Utils.dPrint("[OurPlanner] * * Deepness "+deepness+" is satisfiable !", 1);
			IProblem iproblem = iSolver;
			int[] model = iproblem.findModel();
			return pddlToProblem.getPlan(model);
			
			
		} catch (org.sat4j.specs.TimeoutException | ContradictionException e) {
			// TODO Auto-generated catch block
			Utils.dPrint("[Main] * A Solution could not be found because of a Timeout. (or a Contradiction exception in the initial state)", 3);
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * Get the ISolver for the next step from the pddlToProblem class
	 * @return
	 * @throws ContradictionException
	 */
	private ISolver nextSolver() throws ContradictionException{
		ISolver is = pddlToProblem.nextProblem();
		is.setTimeout(DEFAULT_TIMEOUT);
		return is;
	}

	

}

