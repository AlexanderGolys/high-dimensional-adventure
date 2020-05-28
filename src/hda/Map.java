package hda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;

public class Map {
    public static int size = 11;
    public static Node[][][] terrain;
    public static int[] starting_point = new int[]{5, 5, 5};
    public static int[] ending_point = new int[]{1, 1, 1};

    public static void init(int difficulty)
    {
        generator();
        int len = pathfinding();
        switch(difficulty)
        {
            case 0:
                while (len < 0 || len >= 20)
                {
                    generator();
                    len = pathfinding();
                }

            case 1:
                while (len < 20 || len >= 60)
                {
                    generator();
                    len = pathfinding();
                }
            case 2:
                while (len < 60 || len >= 100)
                {
                    generator();
                    len = pathfinding();
                }
            case 3:
                while (len < 200)
                {
                    generator();
                    len = pathfinding();
                }
        }
    }

    private static void generator()
    {
        for (int x=0; x<size; x++)
            for (int y = 0; y < size; y++)
                for (int z = 0; z < size; z++) {
                    int value = ThreadLocalRandom.current().nextInt(0, 5);
                    terrain[x][y][z] = new Node(value, x, y, z);
                    if (x*y*z == 0 || x == size - 1 || y == size - 1 || z == size - 1)
                        terrain[x][y][z].value = 6;
                }
    }

    private static int pathfinding()
    {
        PriorityQueue<Node> to_check_set = new PriorityQueue<Node>();
        Node start = terrain[starting_point[0]][starting_point[1]][starting_point[2]];
        start.evaluation = 0;
        to_check_set.add(start);
        Set<Node> trash = new HashSet<Node>();
        boolean found = false;
        Node checking = new Node();

        while (!to_check_set.isEmpty())
        {
            checking = to_check_set.poll();
            if (checking.coords == ending_point)
            {
                found = true;
                break;
            }
            Node[] expanded = expand(checking);
            for (int i=0; i<expanded.length; i++)
            {
                if (!trash.contains(expanded[i]))
                {
                    to_check_set.add(expanded[i]);
                }
            }
            trash.add(checking);
        }
        if (!found)
            return -1;
        else
            return checking.evaluation;
    }

    private static Node[] expand(Node e)
    {
        int x = e.coords[0];
        int y = e.coords[1];
        int z = e.coords[2];
        ArrayList<Node> ret = new ArrayList<>();
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++)
                for (int dz = -1; dz < 2; dz++)
                    if (abs(e.value - terrain[x+dx][y+dy][z+dz].value) < 2 &&
                            abs(dx) + abs(dy) + abs(dz) > 0)
                        ret.add(terrain[x+dx][y+dy][z+dz]);
        Node[] ret_array = new Node[ret.size()];
        ret.toArray(ret_array);
        return ret_array;
    }
}
