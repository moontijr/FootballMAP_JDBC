package controller;

import model.Player;
import model.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.jdbc.PlayerRepositoryJDBC;
import repository.jdbc.TeamRepositoryJDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerControllerTest {


    PlayerRepositoryJDBC playerRepositoryJDBC=new PlayerRepositoryJDBC();
    TeamRepositoryJDBC teamRepositoryJDBC=new TeamRepositoryJDBC();
    PlayerController playerController = new PlayerController(playerRepositoryJDBC,teamRepositoryJDBC);
    Player player=new Player("P1","Ciprian","Deac",34,"Romania","Goalkeeper",10000);
    Player player1=new Player("P2","Ciprian","Petre",35,"Germany","Forward",1200000);
    Player player2=new Player("P3","Mamadou","Thiam",29,"Nigeria","Forward",25000);

    Team team=new Team("CFR Cluj","CFR","Romania","Cluj-Napoca",1927,40,45000000);

    @BeforeEach
    void  setUp() throws SQLException {
        playerRepositoryJDBC.add(player);
        playerRepositoryJDBC.add(player1);
        teamRepositoryJDBC.add(team);
        team.addPlayerToTeam(player);
        team.addPlayerToTeam(player1);


    }

    @AfterEach
    void deleteAfter() throws SQLException
    {
        playerRepositoryJDBC.remove(player.getId(),player.getFirstName());
        playerRepositoryJDBC.remove(player1.getId(),player1.getFirstName());
        teamRepositoryJDBC.remove(team.getName(),team.getAbbreviation());
    }

    @Test
    void sortAllPlayersByPrice() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByPrice();
        assertEquals(players,sortedPlayer);
    }

    @Test
    void sortPlayersFromSpecificTeamByPrice() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortPlayersFromSpecificTeamByPrice(team);
        assertEquals(players,sortedPlayer);
    }

    @Test
    void printAllPlayers() {
    }

    @Test
    void sortPlayersByStatus() {
        List <Player> players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        assertEquals(players,playerController.sortPlayersByStatus("Playing"));

    }

    @Test
    void testSortAllPlayersByPrice() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByPrice();
        assertEquals(players,sortedPlayer);
    }

    @Test
    void testSortPlayersFromSpecificTeamByPrice() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortPlayersFromSpecificTeamByPrice(team);
        assertEquals(players,sortedPlayer);
    }

    @Test
    void testPrintAllPlayers() {
        List <Player> players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        assertEquals(players,playerController.printAllPlayers());
    }

    @Test
    void SortPlayersByStatus() throws SQLException {
        playerRepositoryJDBC.add(player2);
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer=playerController.sortPlayersByStatus("Playing");
        assertEquals(sortedPlayer,players);
        playerRepositoryJDBC.remove(player2.getId(),player2.getFirstName());
    }

    @Test
    void sortAllPlayersByAge() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByAge();
        assertEquals(players,sortedPlayer);
    }

    @Test
    void sortAllPlayersByPosition() {
        List<Player>players=new ArrayList<>();
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByPosition("Forward");
        assertEquals(players,sortedPlayer);

    }

    @Test
    void sortPlayersFromSpecificTeamByPosition() {
        List<Player>players=new ArrayList<>();
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortPlayersFromSpecificTeamByPosition(team,"Forward");
        assertEquals(players,sortedPlayer);

    }

    @Test
    void sortAllPlayersByNationality() {
        List<Player>players=new ArrayList<>();
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByNationality("Germany");
        assertEquals(players,sortedPlayer);
    }

    @Test
    void sortAllPlayersByName() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.sortAllPlayersByName();
        assertEquals(players,sortedPlayer);
    }

    @Test
    void seeAllOtherPlayersWithoutYourself() {
        List<Player>players=new ArrayList<>();
        players.add(player);
        List<Player> sortedPlayer;
        sortedPlayer = playerController.seeAllOtherPlayersWithoutYourself(player1);
        assertEquals(players,sortedPlayer);
    }

    @Test
    void isAffordable() {
        List <Team> teams=new ArrayList<>();
        teams.add(team);
        List<Team> sortedTeams;
        sortedTeams= playerController.isAffordable(player);
        assertEquals(teams,sortedTeams);
    }
}