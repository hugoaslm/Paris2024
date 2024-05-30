package fr.isep.algo.projetjo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country("France");
    }

    @Test
    void getName_initialName_returnsFrance() {
        assertEquals("France", country.getName());
    }

    @Test
    void getGoldMedals_initialState_returnsZero() {
        assertEquals(0, country.getGoldMedals());
    }

    @Test
    void getSilverMedals_initialState_returnsZero() {
        assertEquals(0, country.getSilverMedals());
    }

    @Test
    void getBronzeMedals_initialState_returnsZero() {
        assertEquals(0, country.getBronzeMedals());
    }

    @Test
    void getTotalMedals_initialState_returnsZero() {
        assertEquals(0, country.getTotalMedals());
    }

    @Test
    void addGoldMedals_addFiveGoldMedals_returnsFiveGoldMedals() {
        country.addGoldMedals(5);
        assertEquals(5, country.getGoldMedals());
    }

    @Test
    void addSilverMedals_addThreeSilverMedals_returnsThreeSilverMedals() {
        country.addSilverMedals(3);
        assertEquals(3, country.getSilverMedals());
    }

    @Test
    void addBronzeMedals_addTwoBronzeMedals_returnsTwoBronzeMedals() {
        country.addBronzeMedals(2);
        assertEquals(2, country.getBronzeMedals());
    }

    @Test
    void getTotalMedals_afterAddingMedals_returnsCorrectTotal() {
        country.addGoldMedals(5);
        country.addSilverMedals(3);
        country.addBronzeMedals(2);
        assertEquals(10, country.getTotalMedals());
    }

    @Test
    void setName_changeNameToGermany_returnsGermany() {
        country.setName("Germany");
        assertEquals("Germany", country.getName());
    }
}