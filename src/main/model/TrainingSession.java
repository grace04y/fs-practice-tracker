package model;

// represents a Training Session having a date, start time, end time,
// elements practiced, the type of program practiced, and notes

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

//represents a training session with a date, start time, end time,
//   and list of elements practiced
public class TrainingSession implements Writable {
    private String date; //(YYYY-MM-DD)
    private String startTime; //"HH:MM"
    private String endTime; //"HH:MM"
    protected ArrayList<Element> elementsList;


    //EFFECT: constructs an empty training session with given date
    public TrainingSession(String date) {
        this.date = date;
        this.startTime = "00:00";
        this.endTime = "00:00";
        this.elementsList = new ArrayList<>();
    }


    //REQUIRES: Time in the format HH:MM
    //MODIFIES: this
    //EFFECT: adds a start time to the training session
    public void addStartTime(String start) {
        this.startTime = start;
    }

    //REQUIRES: Time in the format HH:MM
    //MODIFIES: this
    //EFFECT: adds an end time of the training session
    public void addEndTime(String end) {
        this.endTime = end;
        EventLog.getInstance().logEvent(new Event(this.date + " training session from " + this.getStartTime() + " to "
                + this.getEndTime() + " created"));
    }

    //REQUIRES: Is an element that is not already on the list of practised elements
    //MODIFIES: this
    //EFFECT: add a new element to the end of a list of practised elements
    public void addElement(Element element) {
        elementsList.add(element);
        EventLog.getInstance().logEvent(new Event(element.getName() + " added to "
                + this.getDate() + " training session"));

    }

    //getters
    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public ArrayList<Element> getElementsList() {
        return elementsList;
    }

    // Method taken from WorkRoom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", date);
        json.put("startTime", startTime);
        json.put("endTime", endTime);
        json.put("elements", elementsToJson());
        return json;
    }

    // Method taken from WorkRoom class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    // EFFECTS: returns sessions in this practiceTracker as a JSON array
    private JSONArray elementsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Element e: elementsList) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }


}


