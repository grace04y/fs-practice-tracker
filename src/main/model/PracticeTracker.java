package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

//represents a practice tracker with a name and a list of training sessions
public class PracticeTracker implements Writable {
    private String name;
    protected ArrayList<TrainingSession> sessionList;

    //Constructor
    public PracticeTracker(String name) {
        this.name = name;
        sessionList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    //MODIFIES: this
    //EFFECTS: adds new training session to the end of a list of training sessions
    public void addTrainingSession(TrainingSession s) {
        sessionList.add(s);
    }

    //MODIFIES: this
    //REQUIRES: at least one session in the list (USE EXCEPTIONS?)
    //EFFECTS: deletes session with the given date and start time
    public void deleteTrainingSession(String date) {
        for (int i = 0; i <= sessionList.size(); i++) {
            for (TrainingSession s : sessionList) {
                if (date.equals(s.getDate())) {
                    sessionList.remove(i);
                }
            }
        }
    }


    //getter
    public ArrayList<TrainingSession> getTrainingSessionList() {
        return sessionList;
    }

    // Method taken from WorkRoom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("sessions", sessionsToJson());

        return json;
    }

    // Method taken from WorkRoom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: returns sessions in this practiceTracker as a JSON array
    private JSONArray sessionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (TrainingSession ts : sessionList) {
            jsonArray.put(ts.toJson());
        }

        return jsonArray;
    }
}
