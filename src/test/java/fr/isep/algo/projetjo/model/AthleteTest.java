package fr.isep.algo.projetjo.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AthleteTest {

    static Athlete test;
    @BeforeAll
    static void create() {
        test = new Athlete("Nom", "Prénom", "France", 18, "Masculin", 1, 1);
    }

    @Test
    void testId() {
        assertEquals(test.getId(), 1);
        test.setId(3);
        assertNotEquals(test.getId(), 1);
        assertEquals(test.getId(), 3);
    }

    @Test
    void testPrint() {
        assertEquals(test.toString(), "Nom Prénom");
    }


}