package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PracticeTrackerTest {
    private PracticeTracker testTracker;
    TrainingSession s1 = new TrainingSession("2022-02-02");
    TrainingSession s2 = new TrainingSession("2022-02-03");
    Element e1 = new Element("3A");
    Element e2 = new Element("CCoSp4");


    @BeforeEach
    void runBefore() {
        testTracker = new PracticeTracker("My practice tracker");
        s1.addStartTime("1:00 pm");
        s1.addEndTime("2:00 pm");
        s2.addStartTime("5:00 pm");
        s2.addEndTime("5:45 pm");
    }

    @Test
    void testAddSession() {
        testTracker.addTrainingSession(s1);
        testTracker.addTrainingSession(s2);

        assertEquals(2, testTracker.sessionList.size());
        assertEquals(s1, testTracker.sessionList.get(0));
        assertEquals(s2, testTracker.sessionList.get(1));
    }

    @Test
    void testDeleteSession() {
        testTracker.addTrainingSession(s1);
        testTracker.addTrainingSession(s2);
        assertEquals(2, testTracker.sessionList.size());

        testTracker.deleteTrainingSession("2022-02-02");
        assertEquals(1, testTracker.sessionList.size());


        //TODO: check that the correct session is deleted
    }

}
