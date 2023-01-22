package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainingSessionTest {
    private TrainingSession testSession;
    private Element testElement;

    @BeforeEach
    void runBefore() {
        testSession = new TrainingSession("2022-02-02");
        testElement = new Element("3A");
    }


    @Test
    void testAddStartTime(){
        testSession.addStartTime("12:30");
        assertEquals("12:30", testSession.getStartTime());
    }

    @Test
    void testAddEndTime(){
        testSession.addEndTime("13:45");
        assertEquals("13:45", testSession.getEndTime());
    }

    @Test
    void testAddElement() {
        assertEquals(0, testSession.elementsList.size());

        testSession.addElement(new Element("4A"));
        assertEquals(1, testSession.elementsList.size());

        testSession.addElement(new Element("4T + 3T"));
        assertEquals(2, testSession.elementsList.size());
    }

    @Test
    void testGetDate(){
        assertEquals("2022-02-02", testSession.getDate());
    }

//    @Test
//    void testDeleteElement() {
//        testSession.addElement(new Element("4A"));
//        testSession.addElement(new Element("4T + 3A"));
//
//        assertEquals(2, testSession.elementsList.size());
//
//        testSession.deleteElement(Element.getElement("4A")); //How do we call an Element in our ArrayList?
//        assertEquals(1, testSession.elementsList.size());
//    }

//    @Test
//    void testPracticeSP() {
//        assertFalse(testSession.getSP());
//
//        testSession.practiceSP("Y");
//        assertTrue(testSession.getSP());
//    }
//
//    @Test
//    void testPracticeLP() {
//        assertFalse(testSession.getLP());
//
//        testSession.practiceLP("Y");
//        assertTrue(testSession.getLP());
//    }




}