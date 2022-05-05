package compare;

import java.sql.*;
import java.util.*;
import room.Room;

public class Compare implements Comparator<Room>
{
    public int compare(Room r1, Room r2) {
        return r1.number - r2.number;
    }
}
