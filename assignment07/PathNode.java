package assignment07;

import java.util.ArrayList;

public class PathNode {
    char data;
    int xLocation;
    int yLocation;

    PathNode cameFrom_;

    ArrayList<PathNode> neighbors;
    public PathNode( char c, int x, int y){
        data = c;
        xLocation = x;
        yLocation = y;
        cameFrom_ = null;
        neighbors = new ArrayList<>( 4 );

        for(int i = 0; i < 4; i++){
            neighbors.add(null);
        }

    }

    public void addNeighbor( PathNode n, int index ){
        neighbors.add( index, n );
    }


}
