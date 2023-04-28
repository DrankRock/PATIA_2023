package satPlanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.sat4j.core.VecInt;
import org.sat4j.maxsat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

/**
 * Wrapper of the solver object to be able to properly add clauses and goals, 
 * remove the previous goals, and translate back (hopefully transkating *)
 * @author matvei
 *
 */
public class ISolverWrapper {
	ISolver isolver;
	
	ArrayList<VecInt> clauses ;
	ArrayList<VecInt> goal ;
	HashMap<String, Integer> translation ;
	HashMap<String, Integer> translationAction ;
	
	private int lastDigitAdded;
	private int currentStep;
	
	/**
	 * Constructor, takes nothing but gives much, such a robinhood.
	 */
	public ISolverWrapper() {
		clauses = new ArrayList<>();
		goal = new ArrayList<>();
		translation = new HashMap<>();
		translationAction = new HashMap<>();
		isolver = SolverFactory.newDefault();
		isolver.setTimeout(3600);
		lastDigitAdded = 1;
		currentStep = 0;
	}
	
	/**
	 * Send back the String that was used to initialize an action, thus giving the possibility to associate
	 * an index with a "real" index and a step.
	 * @param index
	 * @return
	 */
	public String translateAction(int index) {
		for (Entry<String, Integer> entry : this.translationAction.entrySet()) {
			if (entry.getValue() == index) {
				return entry.getKey();
			}
		}
		return "0";
	}
	
	/**
	 * Add clause as an array of ints
	 * @param arr the list of clauses to add
	 */
	public void addClause(int[] arr) {
		this.addClause(new VecInt(arr));
	}
	
	/**
	 * Add a clause as a step'd number (index)
	 * @param num the index of the clause
	 * @param step the current step
	 */
	public void addClause(int num, int step) {
		clauses.add(indexAndStepToVecInt_Predicate(num, step));
	}
	
	/**
	 * Add a goal as a step'd number (index)
	 * @param num the index of the goal
	 * @param step the current step
	 */
	public void addGoal(int num, int step) {
		VecInt newGoal = indexAndStepToVecInt_Predicate(num, step);
		goal.add(newGoal);
	}
	
	/**
	 * Add an action as a step'd number (index)
	 * @param num the index of the action
	 * @param step the current step
	 */
	public void addAction(int num, int step) {
		clauses.add(indexAndStepToVecInt_Action(num, step));
	}
	
	/**
	 * Add a clause using a VecInt forming the indexs to add
	 * @param vect the VectInt to add
	 */
	public void addClause(VecInt vect) {
		clauses.add(vect);
	}
	
	/**
	 * Give a new VecInt associated with a predicate from its index and the step it's at
	 * @param number the index of the predicate
	 * @param step the step in which we're looking for it
	 * @return a VecInt containing the associated index 
	 */
	public VecInt indexAndStepToVecInt_Predicate(int number, int step) {
		int isNegative = 1;
		int numberr = number;
		if (number < 0) {
			isNegative = -1;
			numberr = -number;
		}
		String key = ""+step+"_"+numberr;
		if (!translation.containsKey(key)) {
			translation.put(key, lastDigitAdded);
			lastDigitAdded += 1;
		}
		return new VecInt(new int[] {isNegative*translation.get(key)});
	}
	
	/**
	 * Give a new VecInt associated with an action from its index and the step it's at
	 * @param number the index of the action
	 * @param step the step in which we're looking for it
	 * @return a VecInt containing the associated index 
	 */
	public VecInt indexAndStepToVecInt_Action(int number, int step) {
		int isNegative = 1;
		int numberr = number;
		if (number < 0) {
			isNegative = -1;
			numberr = -number;
		}
		String key = ""+step+"_"+numberr;
		if (!translationAction.containsKey(key)) {
			translationAction.put(key, lastDigitAdded);
			lastDigitAdded += 1;
		}
		return new VecInt(new int[] {isNegative*translationAction.get(key)});
	}
	
	/**
	 * Give a new integer associated with a predicate from its index and the step it's at
	 * @param number the index of the predicate
	 * @param step the step in which we're looking for it
	 * @return the associated index 
	 */
	public int indexAndStepToInt_Predicate(int number, int step) {
		int isNegative = 1;
		int numberr = number;
		if (number < 0) {
			isNegative = -1;
			numberr = -number;
		}
		String key = ""+step+"_"+numberr;
		if (!translation.containsKey(key)) {
			translation.put(key, lastDigitAdded);
			lastDigitAdded += 1;
		}
		return isNegative*translation.get(key);
	}
	
	/**
	 * Give a new integer associated with an action from its index and the step it's at
	 * @param number the index of the action
	 * @param step the step in which we're looking for it
	 * @return the associated index 
	 */
	public int indexAndStepToInt_Action(int number, int step) {
		int isNegative = 1;
		int numberr = number;
		if (number < 0) {
			isNegative = -1;
			numberr = -number;
		}
		String key = ""+step+"_"+numberr;
		if (!translationAction.containsKey(key)) {
			translationAction.put(key, lastDigitAdded);
			lastDigitAdded += 1;
		}
		return isNegative*translationAction.get(key);
	}
	
	

	/**
	 * Create a solver from the current actions, predicates and goals in this class, and returns it
	 * @return A new ISolver from the content of this class
	 * @throws ContradictionException if something is impossible in this new solver
	 */
	public ISolver getSolver() throws ContradictionException{
		isolver = SolverFactory.newDefault();
		isolver.setTimeout(3600);
		for (int i=0; i<clauses.size(); i++) {
			VecInt vi = clauses.get(i);
			isolver.addClause(vi);
		}
		for (int i=0; i<goal.size(); i++) {
			VecInt vi = goal.get(i);
			isolver.addClause(vi);
		}
		
		return isolver ;	
	}
	
	/**
	 * Returns a String containing the String version of this class
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Clauses : \n");
		for (VecInt vi : this.clauses) {
			sb.append(vi.toString()+"; ");
		}
		sb.append("\nGoal : \n");
		for (VecInt vi : this.goal) {
			sb.append(vi.toString()+"; ");
		}
		return sb.toString();
	}
	
	/**
	 * Translate back from a key, but to be honest this was never tested
	 * @param key the key of the object we're looking for
	 * @return the associated integers as an array
	 */
	private int[] translateBack(String key) {
		String[] array = key.split("_")[1].split(",");
		int[] out = new int[array.length];
		for (int i=0; i<array.length; i++) {
			out[i] = Integer.valueOf(array[i]);
		}
		return out;
	}
	
	/**
	 * Reset the goals
	 */
	public void reset() {
		goal.clear();
	}
}








