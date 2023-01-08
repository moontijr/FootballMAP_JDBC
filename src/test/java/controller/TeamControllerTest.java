package controller;

import model.Coach;
import model.Player;
import model.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.jdbc.CoachRepositoryJDBC;
import repository.jdbc.PlayerRepositoryJDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoachControllerTest {


    private final CoachRepositoryJDBC coachRepositoryJDBC= new CoachRepositoryJDBC();
    private final PlayerRepositoryJDBC playerRepositoryJDBC=new PlayerRepositoryJDBC();
    CoachController coachController=new CoachController(coachRepositoryJDBC,playerRepositoryJDBC);
    Player player1=new Player("P1","Ciprian","Deac",34,"Germany","Goalkeeper",1250000);
    Player player2=new Player("P2","Octavian","Popescu",20,"Romania","Forward",10000000);
    Player player3=new Player("P3","Ovidiu","Bic",34,"Romania","Forward",1250000);
    Player player4=new Player("P4","Ovidiu","Popescu",34,"Germany","Forward",1250000);
    Team team1=new Team("CFR Cluj", "CFR", "Romania", "Cluj",1907,40,2000000);
    Team team2=new Team("U Cluj", "U", "Romania", "Cluj",1919,40,8000000);

    Coach coach1=new Coach("C1","Vlad","Pasca",20,"Romania","Deffensive",team1);
    Coach coach2=new Coach("C2","Tudor","Muntean",19,"Romania","Offensive",team2);

    @BeforeEach
    void setUp() throws SQLException {
        team1.addPlayerToTeam(player1);
        team1.addPlayerToTeam(player2);
        team2.addPlayerToTeam(player3);
        team2.addPlayerToTeam(player4);
        coachRepositoryJDBC.add(coach1);
        coachRepositoryJDBC.add(coach2);
        playerRepositoryJDBC.add(player1);
        playerRepositoryJDBC.add(player2);
        playerRepositoryJDBC.add(player3);
        playerRepositoryJDBC.add(player4);


    }

    @AfterEach
    void deleteAfter() throws SQLException {
        coachRepositoryJDBC.remove(coach1.getId(),coach1.getFirstName());
        coachRepositoryJDBC.remove(coach2.getId(),coach2.getFirstName());
        playerRepositoryJDBC.remove(player1.getId(),player1.getFirstName());
        playerRepositoryJDBC.remove(player2.getId(),player2.getFirstName());
        playerRepositoryJDBC.remove(player3.getId(),player3.getFirstName());
        playerRepositoryJDBC.remove(player4.getId(),player4.getFirstName());
    }

    @Test
    void listYourSquadAsACoach() {
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        List<Player> players1=coachController.listYourSquadAsACoach(coach1);
        assertEquals(players,players1);
    }

    @Test
    void listAllPlayersOutsideYourTeam() {
        List<Player> players=new ArrayList<>();
        players.add(player3);
        players.add(player4);
        List<Player> players1=coachController.listAllPlayersOutsideYourTeam(coach1);
        assertEquals(players,players1);
    }

    @Test
    void sortCoachTeamByValue() {
        List<Player> players=new ArrayList<>();
        players.add(player1);
        players.add(player2);
        List<Player> players1=coachController.sortCoachTeamByValue(coach1);
        assertEquals(players,players1);
    }

    @Test
    void sortCoachTeamByAge() {
        List<Player> players=new ArrayList<>();
        players.add(player2);
        players.add(player1);
        List<Player> players1=coachController.sortCoachTeamByAge(coach1);
        assertEquals(players,players1);
    }

    @Test
    void addPlayer() throws SQLException {
        assertFalse(coachController.addPlayer(coach1,player1));
        assertFalse(coachController.addPlayer(coach1,player2));
        assertTrue(coachController.addPlayer(coach1,player3));
        assertTrue(coachController.addPlayer(coach1,player4));
    }

    @Test
    void transferPlayer() throws SQLException {
        assertFalse(coachController.transferPlayer(coach1,player3,team1));
        assertTrue(coachController.transferPlayer(coach1,player3,team2));
        assertFalse(coachController.transferPlayer(coach1,player3,team2));
    }

    @Test
    void removePlayer() throws SQLException {
        assertTrue(coachController.removePlayer(coach1,player1));
        assertFalse(coachController.removePlayer(coach1,player1));
        assertFalse(coachController.removePlayer(coach1,player3));
        assertTrue(coachController.removePlayer(coach2,player4));
    }

    @Test
    void printAllCoaches() {
        List<Coach> coaches=new ArrayList<>(coachRepositoryJDBC.getAllCoaches());
        assertEquals(coaches,coachController.printAllCoaches());
    }

    @Test
    void sortAllCoachesByAge() {
        List <Coach> coaches=new ArrayList<>();
        coaches.add(coach2);
        coaches.add(coach1);
        assertEquals(coaches,coachController.sortAllCoachesByAge());
    }

    @Test
    void sortAllCoachessByNationality() {
        List <Coach> coaches=new ArrayList<>();
        coaches.add(coach1);
        coaches.add(coach2);
        assertEquals(coaches,coachController.sortAllCoachesByNationality("Romania"));
    }

    @Test
    void sortAllCoachesByName() {
        List <Coach> coaches=new ArrayList<>();
        coaches.add(coach2);
        coaches.add(coach1);
        assertEquals(coaches,coachController.sortAllCoachesByName());
    }

    @Test
    void sortAllCoachessByPlayStyle() {
        List <Coach> coaches=new ArrayList<>();
        coaches.add(coach1);
        assertEquals(coaches,coachController.sortAllCoachesByPlayStyle("Deffensive"));
    }
}