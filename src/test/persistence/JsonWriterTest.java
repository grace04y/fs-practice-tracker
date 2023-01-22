package persistence;

import model.Element;
import model.PracticeTracker;
import model.TrainingSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            PracticeTracker pt = new PracticeTracker("My practice tracker");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testWriterEmptyPracticeTracker() {
        try {
            PracticeTracker pt = new PracticeTracker("My practice tracker");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPracticeTracker.json");
            writer.open();
            writer.write(pt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPracticeTracker.json");
            pt = reader.read();
            assertEquals("My practice tracker", pt.getName());
            assertEquals(0, pt.getTrainingSessionList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown!");
        }
    }

    @Test
    void testWriterGeneralPracticeTracker() {
        Element tripleAxel = new Element("3A");
        tripleAxel.rateSuccess(2);
        Element stepSequence = new Element("StSq4");
        stepSequence.rateSuccess(3);

        TrainingSession ts1 = new TrainingSession("2022-02-01");
        ts1.addStartTime("1:00 pm");
        ts1.addEndTime("2:00 pm");
        ts1.addElement(tripleAxel);

        TrainingSession ts2 = new TrainingSession("2022-02-02");
        ts2.addStartTime("5:00 pm");
        ts2.addEndTime("6:00 pm");
        ts2.addElement(stepSequence);
        ts2.addElement(tripleAxel);

        try {
            PracticeTracker pt = new PracticeTracker("My practice tracker");
            pt.addTrainingSession(ts1);
            pt.addTrainingSession(ts2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPracticeTracker.json");
            writer.open();
            writer.write(pt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPracticeTracker.json");
            pt = reader.read();
            assertEquals("My practice tracker", pt.getName());
            List<TrainingSession> ts = pt.getTrainingSessionList();
            assertEquals(2, ts.size());

            //ADD MORE TESTS HERE:
            List<Element> elementList1 = pt.getTrainingSessionList().get(0).getElementsList();
            List<Element> elementList2 = pt.getTrainingSessionList().get(1).getElementsList();

            checkTrainingSession("2022-02-01", elementList1, ts.get(0));
            checkTrainingSession("2022-02-02", elementList2, ts.get(1));

        } catch (IOException e) {
            fail ("Exception should not have been thrown!");
        }
    }
}
