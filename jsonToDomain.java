import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Class used to convert an input json file with a certain format into a pddl file suiting our
 * implementation of the Sokoban problem
 */
public class jsonToDomain {
    enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    };
    char[][] charLevel;
    String[][] variableName;

    boolean DEBUG = true;

    ArrayList<String> printConnected_AL = new ArrayList<String>(); // printed 6
    ArrayList<String> printDirection_AL = new ArrayList<String>(); // printed 7
    ArrayList<String> printIsClear_AL = new ArrayList<String>(); // printed 5
    ArrayList<String> printIsTarget_AL = new ArrayList<String>(); // printed 2
    ArrayList<String> printNotIsTarget_AL = new ArrayList<String>();
    ArrayList<String> printHasBoxOn_AL = new ArrayList<String>(); // printed 3
    ArrayList<String> printPusherIsOn_AL = new ArrayList<String>(); //printed 1
    ArrayList<String> printOnTarget_AL = new ArrayList<String>(); // printed 4

    HashMap<Tuple, String> floorsList;
    HashMap<Tuple, String> boxesList;
    HashMap<Tuple, String> targetList;

    String problem_name;

    public void run(String [] args){
        problem_name= "Sokoban-problem";
        if (args.length != 3){
            System.out.println("Error in the arguments.\nUsage : java jsonToDomain.java <path/to/json/file.json>");
            System.exit(0);
            
        }
        problem_name = args[1];
        DEBUG = Boolean.valueOf(args[2].toLowerCase());
        /* Read level design from file */
        BufferedReader reader;
        String levelDesign = null;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			String line = reader.readLine();
            
			while (line != null) {
				if (line.contains("testIn")){
                    levelDesign = line;
                    break;
                }
                line = reader.readLine().trim();
			}
            reader.close();

            if (line == null){
                throw new IllegalArgumentException("Error : Json file contained no parsable level");
            }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        /* Convert data from design level to domain.pddl */
        levelDesign = levelDesign.replace("\\n", "newline").replace("\"testIn\": \"", "").replace("\",", "");
        String[] level = levelDesign.split("newline");
        int maxWidth=-1;
        for(int i=0; i<level.length; i++){
            if (level[i].length() > maxWidth){
                maxWidth = level[i].length();
            }
        }
        for(int i=0; i<level.length; i++){
            int n = maxWidth - level[i].length();
            level[i] = level[i]+" ".repeat(n);
            dPrintln(level[i]);
        }

        charLevel = new char[level[0].length()][level.length];

        variableName = new String[level[0].length()][level.length];
        Tuple player = null;
        //HashMap<String, Tuple> boxLoc = new HashMap<>();
        //HashMap<String, Tuple> targetLoc = new HashMap<>();
        //HashMap<String, Tuple> floorsLoc = new HashMap<>();

        floorsList = new HashMap<Tuple, String>();
        boxesList = new HashMap<Tuple, String>();
        targetList = new HashMap<Tuple, String>();

        for(int i=0; i<level.length; i++){
            for(int j=0; j<level[i].length(); j++){
                char it = level[i].charAt(j);
                int x=j; int y=i;
                Tuple currentTuple = new Tuple(x, y);
                String floorName = "floor"+"_"+x+"_"+y;

                charLevel[x][y] = it;
                variableName[x][y] = floorName;
                dPrint(floorName+" ");
                switch(it){
                    case ' ': // empty floor
                        floorsList.put(currentTuple.copy(), floorName);
                        this.printIsClear_AL.add(printIsClear(floorName));
                        this.printNotIsTarget_AL.add(printNotIsTarget(floorName));
                        //variableName[x][y] = "floor"+"_"+x+"_"+y;
                        //floorsLoc.put("floor"+"_"+x+"_"+y, currentTuple.copy());
                        break;
                    case '.': // empty floor with target
                        floorsList.put(currentTuple.copy(), floorName);
                        String targetName = "target"+"_"+x+"_"+y;
                        targetList.put(currentTuple.copy(), targetName);
                        printIsTarget_AL.add(printIsTarget(floorName));
                        printIsClear_AL.add(printIsClear(floorName));
                        //targetLoc.put("target"+"_"+x+"_"+y, currentTuple).copy();
                        //floorsLoc.put("floor"+"_"+x+"_"+y, currentTuple.copy());
                        break;
                    case '$': // floor with box
                        floorsList.put(currentTuple.copy(), floorName);
                        String boxName = "box"+"_"+x+"_"+y;
                        boxesList.put(currentTuple.copy(), boxName);
                        printHasBoxOn_AL.add(printHasBoxOn(floorName, boxName));
                        this.printNotIsTarget_AL.add(printNotIsTarget(floorName));
                        //boxLoc.put("box"+"_"+x+"_"+y, currentTuple.copy());
                        //floorsLoc.put("floor"+"_"+x+"_"+y, currentTuple.copy());
                        break;
                    case '*': // box on target
                        floorsList.put(currentTuple.copy(), floorName);
                        String boxName2 = "box"+"_"+x+"_"+y;
                        boxesList.put(currentTuple.copy(), boxName2);
                        String targetName2 = "target"+"_"+x+"_"+y;
                        targetList.put(currentTuple.copy(), targetName2);

                        printHasBoxOn_AL.add(printHasBoxOn(floorName, boxName2));
                        printIsTarget_AL.add(printIsTarget(floorName));
                        printOnTarget_AL.add(printOnTarget(boxName2));
                        break;
                    case '@': //player on tile
                        floorsList.put(currentTuple.copy(), floorName);
                        if (player != null){
                            throw new IllegalArgumentException("Error while parsing : two guards found");
                        }
                        player =  currentTuple.copy();
                        printPusherIsOn_AL.add(printPusherIsOn(floorName));
                        this.printNotIsTarget_AL.add(printNotIsTarget(floorName));
                        //floorsLoc.put("floor"+"_"+x+"_"+y, currentTuple.copy());
                        break;
                    case '+': //player on target
                        floorsList.put(currentTuple.copy(), floorName);
                        if (player != null){
                            throw new IllegalArgumentException("Error while parsing : two guards found");
                        }
                        player =  currentTuple.copy();
                        printPusherIsOn_AL.add(printPusherIsOn(floorName));
                        String targetName3 = "target"+"_"+x+"_"+y;
                        targetList.put(currentTuple.copy(), targetName3);
                        printIsTarget_AL.add(printIsTarget(floorName));
                        //player = new Tuple(i, j);
                        //targetLoc.put("target"+"_"+x+"_"+y, currentTuple.copy());
                        //floorsLoc.put("floor"+"_"+x+"_"+y, currentTuple.copy());
                        break;
                    case '#': // walls are ignored
                    default:
                        break;
                }
            }
            dPrintln("");
        }
        // Arrays filled

        if (player == null){
            throw new IllegalArgumentException("Error : no guard was found");
        }
        /* Add connections and directions from level array and tilename array */
        //System.out.println("Current player location : "+player);
        findConnections(player);
        print_all();
    }
    public ArrayList<Character> cellsAround(int x, int y){
        ArrayList<Character> ret = new ArrayList<>();
        if (x-1 > 0){
            ret.add(charLevel[x-1][y]);
        }
        if (x+1 < charLevel.length){
            ret.add(charLevel[x+1][y]);
        }
        if (y-1 > 0){
            ret.add(charLevel[x][y-1]);
        }
        if (y+1 < charLevel[0].length){
            ret.add(charLevel[x][y+1]);
        }
        return ret;
    }
    public String printConnected(String name1, String name2){
        return "(connected "+name1+" "+name2+")";
    }
    public String printDirection(String from, String to, Direction direction){
        return "(tileInDirectionOf "+direction+" "+from+" "+to+")";
        // to est au "nord" de from
    } 

    public String printIsClear(String floor){
        return "(isClear "+floor+")";
    } 
    public String printIsTarget(String target){
        return "(isTarget "+target+")";
    } 
    public String printHasBoxOn(String floor, String box){
        return "(hasBoxOn "+floor+" "+box+")";
    }  
    public String printPusherIsOn(String floor){
        return "(pusherIsOn "+floor+")";
    }  
    public String printOnTarget(String box){
        return "(onTarget "+box+")";
    }
    public String printNotIsTarget(String floor){
        return "(NotIsTarget "+floor+")";
    }

    public void findConnections(Tuple playerLocation){
        ArrayList<Tuple> seenTuples = new ArrayList<>();
        ArrayList<Tuple> toSee = new ArrayList<>();
        toSee.add(playerLocation.copy());

        while (toSee.size() != 0){
            Tuple currentTuple = toSee.remove(0);
            seenTuples.add(currentTuple);
            int currentX = currentTuple.x;
            int currentY = currentTuple.y;
            String currentName = variableName[currentX][currentY];
            //System.out.println(String.format("[%d, %d] - '%c', '%s'", currentX, currentY, currentChar, currentName));
            HashMap<Tuple, Direction> neighbors = findAround(currentTuple);
            for (HashMap.Entry<Tuple, Direction> entry : neighbors.entrySet()) {
                Tuple neighbor = entry.getKey();
                Direction direction = entry.getValue();
                String neighbor_name = variableName[neighbor.x][neighbor.y];
                if ( !seenTuples.contains(neighbor) ) {
                    toSee.add(neighbor);
                }
                printDirection_AL.add(printDirection(currentName, neighbor_name, direction));
                printConnected_AL.add(printConnected(currentName, neighbor_name));
            }
        }

    }

    public void print_all(){
        String toPrint ="";
        printTab(0, "(define (problem "+problem_name+")");
        printTab(1, "(:domain SOKOBAN)");
        printTab(1, "(:objects ");
        // Print Types :
        /*
         *  box tile direction - objects
            floor - tile
            // target is directly as isTarget or onTarget
         */
        // Directions : 
        for (Direction d : Direction.values()){
            toPrint+=d.name()+" ";
        }
        toPrint+=" - direction";
        printTab(2, toPrint);
        // Floor :
        toPrint = "";
        for (Entry<Tuple, String> entry : floorsList.entrySet()){
            toPrint+=entry.getValue()+" ";
        }
        toPrint += " - floor";
        printTab(2, toPrint);
        // boxes :
        toPrint = "";
        for (Entry<Tuple, String> entry : boxesList.entrySet()){
            toPrint+=entry.getValue()+" ";
        }
        toPrint += "- box";
        printTab(2, toPrint);
        // end objects :
        printTab(1, ")");
        printTab(1, "");
        // start init
        printTab(1, "(:init");
        // print pusher :
        printTab(2, "");
        printTab(2, "; Pusher is on tile :");
        for(String pusher : printPusherIsOn_AL){
            printTab(2, pusher);
        }
        // print targets :
        printTab(2, "");
        printTab(2, "; Targets are on tiles :");
        for(String target : printIsTarget_AL){
            printTab(2, target);
        }
        // print not targets :
        printTab(2, "");
        printTab(2, "; Targets are NOT on tiles :");
        for(String target : printNotIsTarget_AL){
            printTab(2, target);
        }
        // print boxes :
        printTab(2, "");
        printTab(2, "; Bpxes are on tiles :");
        for(String box : printHasBoxOn_AL){
            printTab(2, box);
        }
        // print boxes on target
        printTab(2, "");
        printTab(2, "; Boxes are on target :");
        for(String box : printOnTarget_AL){
            printTab(2, box);
        }
        // print clear floors
        printTab(2, "");
        printTab(2, "; Floor is clear :");
        for(String floor : printIsClear_AL){
            printTab(2, floor);
        }
        // print connected floors
        printTab(2, "");
        printTab(2, "; Floor 1 is connected to Floor 2:");
        for(String floor : printConnected_AL){
            printTab(2, floor);
        }
        // print direction floors, so the graph
        printTab(2, "");
        printTab(2, "; Going from floor 1, in the direction Direct, you'll find floor 2");
        for(String floor : printDirection_AL){
            printTab(2, floor);
        }
        printTab(1, ")");
        printTab(1, "");
        printTab(1, "(:goal");
        printTab(2, "(and");
        for (Entry<Tuple, String> entry : boxesList.entrySet()){
            printTab(3, "(onTarget "+entry.getValue()+")");
        }
        printTab(2, ")"); 
        printTab(1, ")"); 
        printTab(0, ")"); 

    }

    private void printTab(int n_tab, String s){
        String toPrint = "";
        for (int i=0; i<n_tab; i++){
            toPrint+="\t";
        }
        System.out.println(toPrint+s);
    }

    private void dPrintln(String s){
        if (DEBUG) {
            System.out.println(s);
        }
    }
    private void dPrint(String s){
        if (DEBUG) {
            System.out.print(s);
        }
    }

    public HashMap<Tuple, Direction> findAround(Tuple current){
        HashMap<Tuple, Direction> ret = new HashMap<Tuple, Direction>();
        int x = current.x; int y = current.y;
        if (x-1 > 0 && charLevel[x-1][y] != '#'){
            ret.put(new Tuple(x-1, y), Direction.LEFT);
        }
        if (x+1 < charLevel.length && charLevel[x+1][y] != '#'){
            ret.put(new Tuple(x+1, y), Direction.RIGHT);
        }
        if (y-1 > 0 && charLevel[x][y-1] != '#'){
            ret.put(new Tuple(x, y-1), Direction.UP);
        }
        if (y+1 < charLevel[0].length && charLevel[x][y+1] != '#'){
            ret.put(new Tuple(x, y+1), Direction.DOWN);
        }
        return ret;
    }
/**
 * (isClear ?f - floor) ;tile of type floor has nothing on it
    (isTarget ?f - floor) ; tile of type floor is a target
    (hasBoxOn ?f - floor ?b - box) ; floor f is occupied by box b
    (pusherIsOn ?f - floor) ;position of pusher on f
    (connected ?from - floor ?to - floor) ;connected tiles
    (tileInDirectionOf ?d - direction ?t1 - floor ?t2 - floor) ; t1 --d--> t2
    (onTarget ?b - box)
)
 * @param args
 */

    public static void main(String[] args){
        jsonToDomain jtd = new jsonToDomain();
        Tuple tuple = new Tuple(0, 0);
        //System.out.println("Tuple : "+tuple);
        jtd.run(args);
    }
}