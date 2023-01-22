package persistence;

import model.Element;
import model.PracticeTracker;
import model.TrainingSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            PracticeTracker pt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }

    }

    @Test
    void testReaderEmptyPracticeTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPracticeTracker.json");
        try {
            PracticeTracker pt = reader.read();
            assertEquals("My practice tracker", pt.getName());
            assertEquals(0, pt.getTrainingSessionList().size());
        } catch (IOException e) {
            fail("Couldn't read from file!");
        }
    }

    @Test //Add additional tests?
    void testReaderGeneralPracticeTracker() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPracticeTracker.json");

        try {
            PracticeTracker pt = reader.read();
            assertEquals("My practice tracker", pt.getName());
            List<TrainingSession> sessionList = pt.getTrainingSessionList();
            assertEquals(2, sessionList.size());

            TrainingSession ts1 = pt.getTrainingSessionList().get(0);
            TrainingSession ts2 = pt.getTrainingSessionList().get(1);

            checkTrainingSession("2022-02-01", ts1.getElementsList(), ts1);
            checkTrainingSession("2022-02-02", ts2.getElementsList(), ts2);

            assertEquals("1:00 pm", ts1.getStartTime());
            assertEquals("2:00 pm", ts1.getEndTime());
            assertEquals(1, ts1.getElementsList().size());

            assertEquals("5:00 pm", ts2.getStartTime());
            assertEquals("6:00 pm", ts2.getEndTime());
            assertEquals(2, ts2.getElementsList().size());

            Element ts1element1 = ts1.getElementsList().get(0);
            Element ts2element1 = ts2.getElementsList().get(0);
            Element ts2element2 = ts2.getElementsList().get(1);

            assertEquals("3A", ts1element1.getName());
            assertEquals("StSq4", ts2element1.getName());
            assertEquals("3A", ts2element2.getName());

            assertEquals(2, ts1element1.getSuccess());
            assertEquals(3, ts2element1.getSuccess());
            assertEquals(2, ts2element2.getSuccess());

        } catch (IOException e) {
            fail("Couldn't read from file!");
        }

    }
}
