package satPlanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.plan.SequentialPlan;
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

import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

/**
 * The name is inaccurate, but it transform a problem into a SAT problem.
 * 
 * @author matvei
 *
 */
public class PddlToProblem {
	
	Problem problem;
	ISolverWrapper isolverW;
	String prefix = "[PddlToProblem] ";
	int step; 
	
	/**
	 * Construtor of the class, using a Problem from pddl4j .
	 * @param pb the Problem we're trying to solve
	 */
	public PddlToProblem(Problem pb) {
		problem = pb;
		isolverW = new ISolverWrapper();
		step = 0;
	}
	
	/**
	 * Translate back the positive actions from the satisfyable solvr into a plan
	 * @param model the model as an int[] as given by the isolver
	 * @return te Plan
	 */
	public Plan getPlan(int[] model) {
		Plan plan = new SequentialPlan();
		for (int index : model) {
			if (index > 0 ) {
				Utils.dPrint(prefix+" -> getPlan : * "+index, 1);
				String keyInISolverWrapper = this.isolverW.translateAction(index);
				if (!keyInISolverWrapper.equals("0")) {
					String[] keys = keyInISolverWrapper.split("_");
					plan.add(Integer.valueOf(keys[0]), problem.getActions().get(Integer.valueOf(keys[1])));
				} // else there's surely a mistake somewhere, but let's assume it never happens. 
			} else {
				Utils.dPrint(prefix+" -> getPlan : * "+index, 3);
			}
				
		}
		return plan;
	}
	
	/**
	 * Create the next Solver, so the solver for the next step. 
	 * @return the next ISolver
	 * @throws ContradictionException if something's impossible in the new Solver
	 */
	public ISolver nextProblem() throws ContradictionException{
		step += 1;
		this.isolverW.reset(); // remove the previous goals
		
		Utils.print("["+step+"] - ===== Next Problem =====", 0);
		this.processActions();
		Utils.print("["+step+"] - Actions processed", 1);
		this.processSingleActionPreviousStep();
		Utils.print("["+step+"] - Verified that a single action was taken last time", 1);
		this.processTransitions();
		Utils.print("["+step+"] - Calculated the possible transitions", 1);
		this.processGoal();
		Utils.print("["+step+"] - Added the goal(s)", 1);

		return isolverW.getSolver();
	}
	
	/**
	 * Create the initial state. If the problem is solveable at the initial state, 
	 * it won't be tested, because we couldn't find how ton run only the initial state, with the goals
	 * without having it always at true for some reason.
	 */
	public void processInitialState() throws ContradictionException {
		Utils.dPrint(prefix+"Process initial state");
		
		BitVector positives = problem.getInitialState().getPositiveFluents();
		// Initial state has no negatives
		
		Utils.dPrint(prefix+" * For each fluent, chose if we add it, or -it");
		for(int i=0;i<problem.getFluents().size();i++) {
			// if fluent is in the initial state :

			if (positives.get(i)) {
				//Utils.dPrint(prefix+" * * "+i);
				int curr = isolverW.indexAndStepToInt_Predicate(i, 0);
				this.isolverW.addClause(new int[] {curr});
			} else {
				//Utils.dPrint(prefix+" * * -"+i);
				int curr = isolverW.indexAndStepToInt_Predicate(i, 0);
				this.isolverW.addClause(new int[] {-curr});
			}

		}
		Utils.dPrint(prefix+"initial State processed : \n"+this.isolverW.toString());
	}
	
	/**
	 * Associate the goals in the current step with the ISolverWrapper
	 */
	public void processGoal() throws ContradictionException {
		Utils.dPrint(prefix+"Process goal");
		
		BitVector positives = problem.getGoal().getPositiveFluents();
		// goal should be all positive
		
		Utils.dPrint(prefix+" * For each goal, add it");
		for(int i=0;i<problem.getFluents().size();i++) {
			// if fluent is in the initial state :
			if (positives.get(i)) {
				Utils.dPrint(prefix+" * * goal "+i);
				this.isolverW.addGoal(i, step);
			}

		}
		Utils.dPrint(prefix+"goal processed : \n"+this.isolverW.toString());
	}
	
	/**
	 * Process the actions in the problem, so verify that everything is coherent in term of actions, 
	 * their preconditions and effects. These are formulas from the class.
	 */
	private void processActions() throws ContradictionException {
		Utils.dPrint(prefix+"Process Actions");
		// for each action
		for(int actionIndex=0;actionIndex<problem.getActions().size();actionIndex++) {
			Action action = problem.getActions().get(actionIndex);
			//Utils.dPrint(prefix+" * * ["+i+"] \n"+problem.toString(action));
			
			// to know which are precond, neg or positives
			BitVector positive = action.getUnconditionalEffect().getPositiveFluents();
			BitVector negative = action.getUnconditionalEffect().getNegativeFluents();
			BitVector preconditions = action.getPrecondition().getPositiveFluents();
			
			for(int positiveFluentIndex = 0; positiveFluentIndex < positive.size(); positiveFluentIndex ++) {
				if(preconditions.get(positiveFluentIndex)) {
					this.isolverW.addClause(new VecInt(new int[] {
							-isolverW.indexAndStepToInt_Action(actionIndex, step-1),
							isolverW.indexAndStepToInt_Predicate(positiveFluentIndex, step-1)
					}));
				}
				
				if(positive.get( positiveFluentIndex )) {
					this.isolverW.addClause(new VecInt(new int[] {
							-isolverW.indexAndStepToInt_Action(actionIndex, step-1),
							isolverW.indexAndStepToInt_Predicate(positiveFluentIndex, step)
					}));
				}
				
				if(negative.get( positiveFluentIndex )) {
					this.isolverW.addClause(new VecInt(new int[] {
							-isolverW.indexAndStepToInt_Action(actionIndex, step-1),
							-isolverW.indexAndStepToInt_Predicate(positiveFluentIndex, step)
					}));
				}
			}
			
			
		}
	}
	
	/**
	 * Assert that a single action was done last step 
	 * (if more than one action is true, this will make it incoherent)
	 */
	public void processSingleActionPreviousStep() throws ContradictionException {
		for(int i=0;i<problem.getActions().size();i++) {
			for(int j=0;j<problem.getActions().size();j++) {
				if(i!=j) {
					this.isolverW.addClause(new VecInt(new int[] {
							-isolverW.indexAndStepToInt_Action(i, step-1),
							-isolverW.indexAndStepToInt_Action(j, step-1)
					}));
				}
			}
		}
	}
	
	/**
	 * Make the transitions happen, inspired by the example in the pddl4j exercices (like most of these functions though)
	 * @throws ContradictionException
	 */
	public void processTransitions() throws ContradictionException{
		for(int fluentIndex = 0;fluentIndex < problem.getFluents().size() ; fluentIndex ++ ) {
			// for each fluent
			ArrayList<Integer> positiveActionEffect = new ArrayList<>();
			ArrayList<Integer> negativeActionEffect = new ArrayList<>();
			
			for(int actionIndex=0;actionIndex<problem.getActions().size();actionIndex++) {
				// for each action
				Action action = problem.getActions().get(actionIndex);
				BitVector actionPositives = action.getUnconditionalEffect().getPositiveFluents();
				BitVector actionNegatives = action.getUnconditionalEffect().getNegativeFluents();
				// if this action has a positive fluent effect 
				if(actionPositives.get(fluentIndex)) {
					positiveActionEffect.add(actionIndex); // add it to the positive actions list
				} else if(actionNegatives.get(fluentIndex)) { 
					negativeActionEffect.add(actionIndex);
				}
			}
			
			// if there were positive action effects
			if(positiveActionEffect.size()!=0) {
				int[] positiveEffectClause = new int[positiveActionEffect.size() + 2];
				
				// this fluent is false at this step
				positiveEffectClause[0] = -isolverW.indexAndStepToInt_Predicate(fluentIndex, step);
				// or it was true at the previous step
				positiveEffectClause[1] = isolverW.indexAndStepToInt_Predicate(fluentIndex, step-1);
				// for each positive action effect found
				for(int i=0;i<positiveActionEffect.size();i++) {
					// the positives should be true at the previous step
					positiveEffectClause[i+2] = isolverW.indexAndStepToInt_Action(positiveActionEffect.get(i), step-1);
				}
				this.isolverW.addClause(new VecInt(positiveEffectClause));
			}
			
			// if there are negative action effects
			if(negativeActionEffect.size()!=0) {
				int[] negativeEffectClause = new int[negativeActionEffect.size() + 2];
				
				// the fluent is true at the current step
				negativeEffectClause[0] = isolverW.indexAndStepToInt_Predicate(fluentIndex, step);
				// or false at the previous
				negativeEffectClause[1] = -isolverW.indexAndStepToInt_Predicate(fluentIndex, step-1);
				for(int i=0;i<negativeActionEffect.size();i++) {
					negativeEffectClause[i+2] = isolverW.indexAndStepToInt_Action(negativeActionEffect.get(i), step-1);
				}
				this.isolverW.addClause(new VecInt(negativeEffectClause));
			}
		}
	}

	
}
