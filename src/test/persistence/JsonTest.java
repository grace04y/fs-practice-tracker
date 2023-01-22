package persistence;

import model.Element;
import model.TrainingSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkTrainingSession(String date, List<Element> elementList, TrainingSession ts) {
        assertEquals(date, ts.getDate());
        assertEquals(elementList, ts.getElementsList());
    }
}
