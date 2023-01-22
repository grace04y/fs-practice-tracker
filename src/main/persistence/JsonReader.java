package persistence;

//Code modeled from JsonReader method in the JsonSerializationDemo project from CPSC210

import model.Element;
import model.PracticeTracker;
import model.TrainingSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Methods taken from JSONReader class in
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads PracticeTracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PracticeTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePracticeTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses PracticeTracker from JSON object and returns it
    private PracticeTracker parsePracticeTracker(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        PracticeTracker pt = new PracticeTracker(name);
        addTrainingSessions(pt, jsonObject);
        return pt;
    }

    // MODIFIES: pt
    // EFFECTS: parses training sessions from JSON object and adds them to workroom
    private void addTrainingSessions(PracticeTracker pt, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("sessions");
        for (Object json : jsonArray) {
            JSONObject nextTrainingSession = (JSONObject) json;
            addTrainingSession(pt, nextTrainingSession);
        }
    }

    // MODIFIES: pt
    // EFFECTS: parses training session from JSON object and adds it to practice tracker
    private void addTrainingSession(PracticeTracker pt, JSONObject jsonObject) {
        String date = jsonObject.getString("date");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        TrainingSession ts = new TrainingSession(date);
        ts.addStartTime(startTime);
        ts.addEndTime(endTime);
        pt.addTrainingSession(ts);
        addElements(ts, jsonObject);

    }

    //Repeat the above methods for elements??

//    // MODIFIES: ts
//    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addElements(TrainingSession ts, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("elements");
        for (Object json : jsonArray) {
            JSONObject nextElement = (JSONObject) json;
            addElement(ts, nextElement);
        }
    }

    // MODIFIES: ts
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addElement(TrainingSession ts, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int success = jsonObject.getInt("success");
        Element element = new Element(name);
        element.rateSuccess(success);
        ts.addElement(element);
    }
}
