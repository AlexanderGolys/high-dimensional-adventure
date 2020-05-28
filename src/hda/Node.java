package hda;

public class Node {
    public int evaluation;
    public int value;
    public int[] coords;
    public Node(int value, int x, int y, int z)
    {
        this.evaluation = 2147483647;
        this.value = value;
        this.coords = new int[]{x, y, z};
    }
    public Node()
    {
        this.evaluation = 2147483647;
        this.value = 0;
        this.coords = new int[]{0, 0, 0};
    }
}
