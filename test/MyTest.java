package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;

import room.*;
import compare.*;
import app.*;

public class MyTest {

   @Test
   public void makeFreeLabel() {
      App myApp = new App(); 
      List <Room> rs = new ArrayList <Room>();
      for (int k = 1; k <= 3; k++) rs.add(new Room(k,0));
      rs.add(new Room(4,1));
      rs.add(new Room(5,1));
      rs.add(new Room(6,0));
      rs.add(new Room(7,1));
      rs.add(new Room(9,0));
      rs.add(new Room(10,0));
      myApp.rooms = rs;
      System.out.println("Deliver information on free rooms as a string");
      assertEquals(myApp.makeLabel(0), "Available rooms: 1-3,6,9-10");
   }

   @Test
   public void makeBookedLabel() {
      App myApp = new App(); 
      List <Room> rs = new ArrayList <Room>();
      for (int k = 1; k <= 3; k++) rs.add(new Room(k,0));
      rs.add(new Room(4,1));
      rs.add(new Room(5,1));
      rs.add(new Room(6,0));
      rs.add(new Room(7,1));
      rs.add(new Room(9,0));
      rs.add(new Room(10,0));
      myApp.rooms = rs;
      System.out.println("Deliver information on booked rooms as a string");
      assertEquals(myApp.makeLabel(1), "Booked rooms: 4-5,7");
   }

   @Test
   public void parseInput() {
      App myApp = new App(); 
      Set <Integer> nums = new TreeSet <Integer>();
      for (int k = 1; k <= 3; k++) nums.add(k);
      nums.add(6);
      nums.add(9);
      nums.add(10);
      System.out.println("Parse input from user into a set of numbers");
      assertEquals(myApp.parseInput("1-3,6,9-10"), nums);
   }

}