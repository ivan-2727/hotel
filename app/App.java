package app;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ComponentOrientation;
import java.awt.event.*;

import dbconnect.*;
import room.*;

public class App {

    public static List <Room> rooms; 

    public App() {}

    public static String makeLabel(int booked) {
        //argument is 0 for free, 1 for booked 
        ArrayList <ArrayList<Integer> > availableRanges = new ArrayList <ArrayList <Integer> > (); 
        int cur = -1; 
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).booked == 1-booked && cur != -1) {
                availableRanges.add(new ArrayList<Integer>( Arrays.asList(cur, i-1)));
                cur = -1;
            }
            if (rooms.get(i).booked == booked && cur == -1) {
                cur = i; 
            }
        }
        if (cur != -1) availableRanges.add(new ArrayList<Integer>( Arrays.asList(cur, rooms.size()-1)));
        String caption;
        if (booked == 0) caption = "Available rooms: "; 
        else caption = "Booked rooms: "; 
        for (int j = 0; j < availableRanges.size(); j++) {
            int l = availableRanges.get(j).get(0);
            int r = availableRanges.get(j).get(1); 
            cur = l; 
            for (int k = l+1; k <= r; k++) {
                if (rooms.get(k).number - rooms.get(k-1).number > 1) {
                    if (cur != k-1) caption += Integer.toString(rooms.get(cur).number) + "-" + Integer.toString(rooms.get(k-1).number) + ',';
                    else caption += Integer.toString(rooms.get(cur).number) + ",";
                    cur = k; 
                }
            }
            if (cur != r) caption += Integer.toString(rooms.get(cur).number) + "-" + Integer.toString(rooms.get(r).number) + ',';
            else caption += Integer.toString(rooms.get(cur).number) + ",";
        }
        if (availableRanges.size()==0) caption += "---";
        else caption = caption.substring(0, caption.length()-1);
        return caption; 
    }

    public static Set <Integer> parseInput(String input) {

        Set <Integer> res = new TreeSet <Integer> ();
        try {
            List <Integer> initRes = new ArrayList <Integer> ();
            List <Integer> t = new ArrayList <Integer> ();
            String tmp = "";
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) != ',' && input.charAt(i) != '-') {
                    tmp += input.charAt(i);
                }
                if (input.charAt(i) == ',') {
                    initRes.add(Integer.parseInt(tmp));
                    t.add(0);
                    tmp = ""; 
                }
                if (input.charAt(i) == '-') {
                    initRes.add(Integer.parseInt(tmp));
                    t.add(1);
                    tmp = ""; 
                }
            }
            initRes.add(Integer.parseInt(tmp));
            
            for (int i = 0; i < initRes.size()-1; i++) {
                if (t.get(i) == 0) res.add(initRes.get(i));
                else {
                    for (int k = initRes.get(i); k <= initRes.get(i+1); k++) res.add(k); 
                }
            }
            res.add(initRes.get(initRes.size()-1));
        }
        catch (Exception e) {
            System.out.println("Error while parsing input");
            res.add(-1); 
        }
        return res; 
    }

    public static JPanel makePanel() {
        JPanel panel = new JPanel();
        JLabel explain = new JLabel("To book/unbook, print room numbers and PRESS ENTER. Formatting: 1,2-5,10");
        explain.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(explain);

        JLabel labelFree = new JLabel(makeLabel(0));
        labelFree.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(labelFree); 

        JLabel labelBooked = new JLabel(makeLabel(1));
        labelBooked.setFont(new Font("Arial", Font.PLAIN, 20));
         

        JTextField bookField = new JTextField();
        bookField.setFont(new Font("Arial", Font.PLAIN, 20));
        bookField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    Set <Integer> booked = parseInput(bookField.getText()); 
                    for (int i = 0; i < rooms.size(); i++) {
                        if (booked.contains(rooms.get(i).number)) {
                            rooms.set(i, new Room(rooms.get(i).number, 1)); 
                        }
                    }
                    labelFree.setText(makeLabel(0)); 
                    labelBooked.setText(makeLabel(1)); 
                }
            }
        });
        panel.add(bookField);
        panel.add(labelBooked); 

        JTextField UNbookField = new JTextField();
        UNbookField.setFont(new Font("Arial", Font.PLAIN, 20));
        UNbookField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    Set <Integer> UNbooked = parseInput(UNbookField.getText()); 
                    for (int i = 0; i < rooms.size(); i++) {
                        if (UNbooked.contains(rooms.get(i).number)) {
                            rooms.set(i, new Room(rooms.get(i).number, 0)); 
                        }
                    }
                    labelFree.setText(makeLabel(0)); 
                    labelBooked.setText(makeLabel(1)); 
                }
            }
        });
        panel.add(UNbookField);

        JButton save = new JButton("Save changes to database");
        save.setFont(new Font("Arial", Font.PLAIN, 20));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DbConnect.executeUpdateRooms(rooms); 
            }
        });

        panel.add(save);

        return panel; 
    }

    public static void main(String[] args){

        JFrame frame = new JFrame("Rooms manager");
        rooms = DbConnect.executeQueryRooms();
        JPanel panel = makePanel(); 
        frame.add(panel);         
        panel.setLayout(new GridLayout(10,1));
        frame.setContentPane(panel);
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
         
         
         
    }
} 


   