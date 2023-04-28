
class Tuple {
    public int x;
    public int y;
    public Tuple(int o, int t){
        x = o;
        y = t;
    }
    public Tuple copy(){
        return new Tuple(x, y);
    }
    public String toString(){
        return "["+x+";"+y+"]";
    }

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
