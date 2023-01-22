package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementTest {
    private Element testElement;

    @BeforeEach
    void runBefore() {
        testElement = new Element("4A");
    }

    @Test
    void testRateSuccess() {
        testElement.rateSuccess(3);
        assertEquals(3, testElement.getSuccess());
    }

    @Test
    void testGetName() {
        assertEquals("4A", testElement.getName());
    }

}

