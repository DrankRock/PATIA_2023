package fr.uga.pddl4j.examples.asp;

import fr.uga.pddl4j.heuristics.state.StateHeuristic;

import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.parser.RequireKey;

import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.plan.SequentialPlan;

import fr.uga.pddl4j.planners.AbstractPlanner;
import fr.uga.pddl4j.planners.Planner;
import fr.uga.pddl4j.planners.PlannerConfiguration;
import fr.uga.pddl4j.planners.ProblemNotSupportedException;
import fr.uga.pddl4j.planners.SearchStrategy;
import fr.uga.pddl4j.planners.statespace.search.StateSpaceSearch;

import fr.uga.pddl4j.problem.DefaultProblem;
import fr.uga.pddl4j.problem.Fluent;
import fr.uga.pddl4j.problem.InitialState;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.State;
import fr.uga.pddl4j.problem.Task;
import fr.uga.pddl4j.problem.numeric.NumericAssignment;
import fr.uga.pddl4j.problem.numeric.NumericVariable;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.problem.operator.Condition;
import fr.uga.pddl4j.problem.operator.ConditionalEffect;
import fr.uga.pddl4j.problem.operator.Effect;
import fr.uga.pddl4j.problem.operator.Method;

import fr.uga.pddl4j.util.BitSet;
import fr.uga.pddl4j.util.BitVector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

import picocli.CommandLine;

/**
 * The class is an example. It shows how to create a simple A* search planner able to
 * solve an ADL problem by choosing the heuristic to used and its weight.
 *
 * @author D. Pellier
 * @version 4.0 - 30.11.2021
 */
@CommandLine.Command(name = "ASP",
        version = "ASP 1.0",
        description = "Solves a specified planning problem using A* search strategy.",
        sortOptions = false,
        mixinStandardHelpOptions = true,
        headerHeading = "Usage:%n",
        synopsisHeading = "%n",
        descriptionHeading = "%nDescription:%n%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n")

public class ASP extends AbstractPlanner {

    /**
     * The class logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(ASP.class.getName());

    /**
     * The weight of the heuristic.
     */
    private double heuristicWeight;

    /**
     * The name of the heuristic used by the planner.
     */
    private StateHeuristic.Name heuristic;

    private ArrayList<Fluent> allFluents;
    private HashMap<String, Fluent> fluentsHashMap;

    /*
    Attention tu avais mal compris
    quand tu fais pb.getFluents()
    tu obitnet deja des indices, genre

    0 at(1)
    1 bite()
    2 at(2)

    et dans initialState tu as un bitvector, qui par exemple ici, si tu as juste at(2), il va contenir
    0, 0, 1

    donc ça tu l'as deja !
     */

    /**
     * Instantiates the planning problem from a parsed problem.
     *
     * @param problem the problem to instantiate.
     * @return the instantiated planning problem or null if the problem cannot be instantiated.
     */

    /**
     *
     * @param problem
     * @return
     */
    @Override
    public Problem instantiate(DefaultParsedProblem problem) {
        final Problem pb = new DefaultProblem(problem);
        pb.instantiate();
        System.out.println("Problem instanciated : ");

        List<ConditionalEffect> lce;
        List<Condition> preconditions;
        int i;
        /*System.out.println("------- FLUENTS ------");
        List<Fluent> fluentList = pb.getFluents();
        int i = 0;
        for (Fluent f : fluentList){
            System.out.println("[ "+i+" ] - "+pb.toString(f));
            i++;
        }
        System.out.println("------- INITIAL STATE-");
        InitialState state = pb.getInitialState();
        final BitVector positive = state.getPositiveFluents();
        System.out.println("---- Positives : ");
        for (i = positive.nextSetBit(0); i >= 0; i = positive.nextSetBit(i + 1)) {
            // For each state in initial state, I want to see the action linked to it

            //System.out.println(pb.toString(pb.getFluents().get(i)));
            System.out.println("["+i+"] - "+pb.getFluents().get(i));
            System.out.println("   Effects :");

        }*/
        /*
        final BitVector negative = state.getNegativeFluents();
        System.out.println("---- Negatives : ");
        for (i = negative.nextSetBit(0); i >= 0; i = negative.nextSetBit(i + 1)) {
            System.out.println(pb.toString(pb.getFluents().get(i)));
        }*/


        System.out.println("------- GOAL   -------");
        System.out.println(pb.getGoal());
/*

        System.out.println("------- ACTIONS -------");
        List<Action> listActions = pb.getActions();
        for (Action a : listActions){
            System.out.println("--------------\n"+pb.toString(a));
            lce = a.getConditionalEffects();
            System.out.println("--------------\n");
            for (ConditionalEffect ce : lce){
                System.out.println("   Cond : "+ce.getCondition()+", Effect : "+ce.getEffect());
                int ii = 0; // do nothing
            }
        }*/
        /*
        System.out.println("------- METHODS -------");
        List<Method> listMethods = pb.getMethods();
        if (listMethods != null) {
            for (Method m : listMethods) {
                // System.out.println(m.toString());
                int iii = 0; // do nothing
            }
        }
        System.out.println("------- TASKS   -------");
        List<Task> listTasks = pb.getTasks();
        if (listTasks != null) {
            for (Task t : listTasks) {
                System.out.println(t.toString());
            }
        }
        System.out.println("---------------");
        System.out.println("Probleme to string : "+pb.toString());*/

        System.out.println("Step 1 :: ========================");
        System.out.println("------- INITIAL STATE-");
        InitialState state = pb.getInitialState();
        final BitVector positive = state.getPositiveFluents();
        System.out.println("---- Positives : ");
        for (i = positive.nextSetBit(0); i >= 0; i = positive.nextSetBit(i + 1)) {
            // For each state in initial state, I want to see the action linked to it
            //System.out.println(pb.toString(pb.getFluents().get(i)));
            System.out.println("["+i+"] - "+pb.getFluents().get(i));
        }
        System.out.println("---- Possible Actions :");
        for (Action action : pb.getActions()){
            // Preconditions :
            System.out.println("  "+action.getName());
            Condition precond = action.getPrecondition();
            System.out.println("Precondition : ");
            System.out.println(pb.toString(precond));

            BitVector positivFluents = precond.getPositiveFluents();
            System.out.println("   Positive Fluents : \n"+positivFluents);
            ArrayList<Integer> arrPosFluents = this.bitvectorToArrayList(positivFluents);
            boolean conditionsTrue = true;
            for (int b : arrPosFluents){
                System.out.println("   "+b);
            }
            System.out.println("Effects : "+action.getConditionalEffects());
            List<ConditionalEffect> conditionalEffectList = action.getConditionalEffects();
            for (ConditionalEffect ef : conditionalEffectList){
                ArrayList<Integer> arrEff = effectToArrayList(ef.getEffect());
                for (int k : arrEff){
                    System.out.println("   "+k);
                }
            }
            //Effect eff = .getEffect();
            //ArrayList<Integer> arrEff = effectToArrayList(eff);
            //for (int k : arrEff){
            //    System.out.println("   "+k);
            //}
            //System.out.println("   Negative Fluents : \n"+precond.getNegativeFluents());
            //System.out.println(pb.toString(action.getPrecondition()));
            System.out.println("--------");
            System.out.println(pb.toString(action));
            System.out.println("================");
            /*lce = action.getPreCondition();
            System.out.println(pb.toString(action));
            System.out.println("--------------\nPreconditions : \n");
            String preconds = "";
            for (ConditionalEffect ce : lce){
                preconds += pb.toString(ce.gePretCondition())+"; ";
                //System.out.println("   Cond : "+ce.getCondition()+", Effect : "+ce.getEffect());
                int ii = 0; // do nothing
            }*/
            //System.out.println("   "+preconds);
        }

        return pb;
    }

    /**
     * This function is exactly like the toString function from
     * the Bitset class, but returns an arrayList instead of
     * a String
     * @return the BitVector as an ArrayList
     */
    public ArrayList<Integer> bitvectorToArrayList(BitVector bv) {
        ArrayList<Integer> list = new ArrayList<>();
        int i = bv.nextSetBit(0);
        if (i != -1) {
            list.add(i);
            while(true) {
                ++i;
                if (i < 0 || (i = bv.nextSetBit(i)) < 0) {
                    break;
                }
                int endOfRun = bv.nextClearBit(i);
                while(true) {
                    list.add(i);
                    ++i;
                    if (i == endOfRun) {
                        break;
                    }
                }
            }
        }
        return list;
    }

    public void useSat4J(){
        final int MAXVAR = 1000000;
        final int NBCLAUSES = 500000;

        ISolver solver = SolverFactory.newDefault();

        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        // Feed the solver using Dimacs format, using arrays of int
        // (best option to avoid dependencies on SAT4J IVecInt)
        for (int i=0;i<NBCLAUSES;i++) {
            int [] clause = {1, 2}; // get the clause from somewhere
                    // the clause should not contain a 0, only integer (positive or negative)
                    // with absolute values less or equal to MAXVAR
                    // e.g. int [] clause = {1, -3, 7}; is fine
                    // while int [] clause = {1, -3, 7, 0}; is not fine
                    solver.addClause(new VecInt(clause)); // adapt Array to IVecInt
        }

        // we are done. Working now on the IProblem interface
        IProblem problem = solver;
        if (problem.isSatisfiable()) {
            System.out.println("Error : Problem is not satisfyable");
            System.exit(1);
        } else {
            System.out.println("Problem has been solved");
            System.exit(1);
        }
    }

    public ArrayList<Integer> effectToArrayList(Effect effect){
        ArrayList<Integer> out = new ArrayList<Integer>();
        BitSet positive = effect.getPositiveFluents();

        for(int j = positive.nextSetBit(0); j >= 0; j = positive.nextSetBit(j + 1)) {
            //str.append(this.toString((Fluent)this.getFluents().get(j)));
            //str.append("\n  ");
            //printed = true;
            out.add(j);
        }

        BitSet negative = effect.getNegativeFluents();

        for(int i = negative.nextSetBit(0); i >= 0; i = negative.nextSetBit(i + 1)) {
            //str.append("(not ");
            //str.append(this.toString((Fluent)this.getFluents().get(i)));
            //str.append("\n  ");
            //printed = true;
            out.add(-i);
        }

        for(Iterator var9 = effect.getNumericAssignments().iterator(); var9.hasNext();) {
            NumericAssignment assignment = (NumericAssignment)var9.next();
            //str.append(this.toString(assignment));
            //str.append("\n  ");
            System.out.println("HELLO NUMERIC ASSIGNEMENT");
        }

        return out;
    }



    /**
     * Search a solution plan to a specified domain and problem using A*.
     *
     * @param problem the problem to solve.
     * @return the plan found or null if no plan was found.
     */
    @Override
    public Plan solve(final Problem problem) {
        // Creates the A* search strategy
        StateSpaceSearch search = StateSpaceSearch.getInstance(SearchStrategy.Name.ASTAR,
                this.getHeuristic(), this.getHeuristicWeight(), this.getTimeout());
        LOGGER.info("* Starting A* search \n");
        // Search a solution
        Plan plan = search.searchPlan(problem);
        // If a plan is found update the statistics of the planner and log search information
        if (plan != null) {
            LOGGER.info("* A* search succeeded\n");
            this.getStatistics().setTimeToSearch(search.getSearchingTime());
            this.getStatistics().setMemoryUsedToSearch(search.getMemoryUsed());
        } else {
            LOGGER.info("* A* search failed\n");
        }
        // Return the plan found or null if the search fails.
        return plan;
    }


    @Override
    public boolean isSupported(Problem p){
        return true;
    }

    /**
     * Sets the weight of the heuristic.
     *
     * @param weight the weight of the heuristic. The weight must be greater than 0.
     * @throws IllegalArgumentException if the weight is strictly less than 0.
     */
    @CommandLine.Option(names = {"-w", "--weight"}, defaultValue = "1.0",
            paramLabel = "<weight>", description = "Set the weight of the heuristic (preset 1.0).")
    public void setHeuristicWeight(final double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight <= 0");
        }
        this.heuristicWeight = weight;
    }

    /**
     * Set the name of heuristic used by the planner to the solve a planning problem.
     *
     * @param heuristic the name of the heuristic.
     */
    @CommandLine.Option(names = {"-e", "--heuristic"}, defaultValue = "FAST_FORWARD",
            description = "Set the heuristic : AJUSTED_SUM, AJUSTED_SUM2, AJUSTED_SUM2M, COMBO, "
                    + "MAX, FAST_FORWARD SET_LEVEL, SUM, SUM_MUTEX (preset: FAST_FORWARD)")
    public void setHeuristic(StateHeuristic.Name heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Returns the name of the heuristic used by the planner to solve a planning problem.
     *
     * @return the name of the heuristic used by the planner to solve a planning problem.
     */
    public final StateHeuristic.Name getHeuristic() {
        return this.heuristic;
    }

    /**
     * Returns the weight of the heuristic.
     *
     * @return the weight of the heuristic.
     */
    public final double getHeuristicWeight() {
        return this.heuristicWeight;
    }

    /**
     * The main method of the <code>ASP</code> planner.
     *
     * @param args the arguments of the command line.
     */
    public static void main(String[] args) {
        System.out.println("Starting ASP.java");
        try {
            final ASP planner = new ASP();
            System.out.println("planner created");
            CommandLine cmd = new CommandLine(planner);
            System.out.println("cmd created");
            cmd.execute(args);
            System.out.println("ran cmd");
        } catch (IllegalArgumentException e) {
            LOGGER.fatal(e.getMessage());
        }
    }
}