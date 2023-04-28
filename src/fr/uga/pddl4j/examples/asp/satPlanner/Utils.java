package satPlanner;

import java.io.PrintStream;

/**
 * Utils function
 * @author matvei
 *
 */
public class Utils {
	final static boolean DEBUG = true;
	/**
	 * Print message only if debug is true
	 * @param b Condition of print
	 * @param args string to print
	 */
	public static void dPrint(Boolean b, String args) {
		if (b)
			System.out.println(args);
	}
	
	/**
	 * Print message only if debug is true
	 * @param args string to print
	 */
	public static void dPrint(String args) {
		Utils.dPrint(args, 1);	}
	
	/**
	 * Print message if debug is activated. 1 for yellow, 2 for red, 3 for green
	 * @param args string to print
	 */
	public static void dPrint(String args, int err) {
		String code = "";
	    if (err == 3) {
	        code = "\u001B[31m";
	    } else if (err == 2) {
	        code = "\u001B[33m";
	    } else if (err == 1) {
	        code = "\033[1;32m";
	    }
	    if (DEBUG)
	    	System.out.println(code + args + "\u001B[0m");
			
	}
	/**
	 * Print message. 1 for yellow, 2 for red, 3 for green
	 * @param args string to print
	 */
	public static void print(String args, int err) {
		String code = "";
	    if (err == 3) {
	        code = "\u001B[31m";
	    } else if (err == 2) {
	        code = "\u001B[33m";
	    } else if (err == 1) {
	        code = "\033[1;32m";
	    }
	    System.out.println(code + args + "\u001B[0m");
			
	}
	
	/**
	 * This was a function to convert an int[] to a string
	 * but we realized that Array.toString(arr) was a thing. 
	 * 
	 * @param arr the array
	 * @return a String version of the array
	 */
	public static String intArrToString(int[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int i =0; i<arr.length; i++) {
			if (i != arr.length-1) {
				sb.append(arr[i]+", ");
			} else {
				sb.append(arr[i]);
			}
			
		}
		return sb.toString();
	}
}
