/**
 * Tuple to handle pairs of int more easily
 */
class Tuple {
    public int x;
    public int y;

    /**
     * Constructor of a tuple
     * @param o first int
     * @param t second int
     */
    public Tuple(int o, int t){
        x = o;
        y = t;
    }

    /**
     * Create a copy of this tuple and returns it
     * @return a copy of this tuple
     */
    public Tuple copy(){
        return new Tuple(x, y);
    }

    /**
     * Returns a String version of this tuple
     * @return a representation of this as a Tuple
     */
    public String toString(){
        return "["+x+";"+y+"]";
    }

    /**
     * Checks if another Tuple is equal to this
     * @param obj the tuple to compare
     * @return true if they contain the same values
     */

    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof Tuple)) {
            return false;
        } else {
            Tuple t = (Tuple) obj;
            return t.x == x && t.y == y;
        }
    }
}
