package fr.isep.algo.projetjo.dao;


import fr.isep.algo.projetjo.model.Athlete;
import fr.isep.algo.projetjo.model.Sport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class athleteDAOTest {

    static Athlete testAthlete;

    @BeforeAll
    static void createInit(){
        athleteDAO.addAthlete("Nom", "Prénom", "France", 18, "Masculin", 70);
        List<Athlete> athletes = athleteDAO.getAllAthletes();
        testAthlete = athletes.getLast();

    }

    @Test
    void test() {
        athleteDAO.modifierAthlete(testAthlete.getId(), "Nom3", "Prénom3", "France3", 182, "Masculin2", 70);
        int idBddAthlete = athleteDAO.getAthleteIdByName("Nom3");
        String pays = athleteDAO.getCountryFromAthleteId(idBddAthlete);
        assertEquals(idBddAthlete, testAthlete.getId());
        assertEquals(pays, "France3");

    }

    @Test
    void testDelete() {
        athleteDAO.deleteAthlete(testAthlete);
        int idBddAthlete = athleteDAO.getAthleteIdByName("Nom3");
        assertNotEquals(idBddAthlete, testAthlete.getId());

    }


}


