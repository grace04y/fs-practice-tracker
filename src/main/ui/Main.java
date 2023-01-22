package ui;

import model.TrainingSession;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new PracticeTrackerApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found!");
        }
    }
}
