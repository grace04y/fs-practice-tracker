package model;

import org.json.JSONObject;
import persistence.Writable;

// represents an element with a name, attempts, and success ranking
public class Element implements Writable {
    protected String name;
    private int success;

    //EFFECT: constructs an element
    public Element(String name) {
        this.name = name;
        this.success = 0;
    }

    //REQUIRES: 1 <= success <= 3
    //MODIFIES: this
    //EFFECTS: inputs the success rating of the element on a scale of 1-3
    public void rateSuccess(int success) {
        this.success = success;
    }

    //getters
    public int getSuccess() {
        return success;
    }

    public String getName() {
        return name;
    }

    // Method taken from Thingy class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("success", success);
        return json;
    }
}
