package controller;

import model.Sponsor;
import model.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.jdbc.TeamRepositoryJDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamControllerTests {
    TeamRepositoryJDBC teamRepositoryJDBC=new TeamRepositoryJDBC();
    TeamController teamController = new TeamController(teamRepositoryJDBC);
    Team team=new Team("CFR Cluj","CFR","Romania","Cluj-Napoca",1927,40,45000000);
    Team team1=new Team("Steaua Bucuresti","FCSB","Romania","Bucharest",1947,40,12500);

    Sponsor sponsor=new Sponsor("Nike","Nk",250000000);


    @BeforeEach
    void  setUp() throws SQLException {
        teamRepositoryJDBC.add(team);
        teamRepositoryJDBC.add(team1);
        sponsor.sponsorTeam(team);
        sponsor.sponsorTeam(team1);

    }

    @AfterEach
    void deleteAfter() throws SQLException {
        teamRepositoryJDBC.remove(team.getName(),team.getAbbreviation());
        teamRepositoryJDBC.remove(team1.getName(),team1.getAbbreviation());
    }
    @Test
    void sortAllTeamsByBudget() {
        List<Team> teams=new ArrayList<>();
        teams.add(team1);
        teams.add(team);
        List<Team> sortedTeams;
        sortedTeams = teamController.sortAllTeamsByBudget();
        assertEquals(teams,sortedTeams);
    }

    @Test
    void sortAllTeamsByFoundationYear() {
        List<Team> teams=new ArrayList<>();
        teams.add(team);
        teams.add(team1);
        List<Team> sortedTeams;
        sortedTeams = teamController.sortAllTeamsByFoundationYear();
        assertEquals(teams,sortedTeams);
    }

    @Test
    void printAllTeamsAffiliatedWithSponsor() {
        List<Team> teams=new ArrayList<>();
        teams.add(team);
        teams.add(team1);
        List<Team> sortedTeams;
        sortedTeams = teamController.getAllTeamsAffiliatedWithSponsor(sponsor);
        assertEquals(teams,sortedTeams);
    }

    @Test
    void sortSponsoredTeamsByBudget() {
        List<Team> teams=new ArrayList<>();
        teams.add(team1);
        teams.add(team);
        List<Team> sortedTeams;
        sortedTeams = teamController.sortSponsoredTeamsByBudget(sponsor);
        assertEquals(teams,sortedTeams);
    }
}