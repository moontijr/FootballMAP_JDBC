package controller;

import model.Sponsor;
import model.Team;
import repository.jdbc.TeamRepositoryJDBC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamController {
    //private final TeamRepositoryMemory teamRepositoryMemory;
    private final TeamRepositoryJDBC teamRepositoryJDBC;

    public TeamController(TeamRepositoryJDBC teamRepositoryJDBC) {
        this.teamRepositoryJDBC=teamRepositoryJDBC;
    }

    /**
     * prints all the Teams from our database
     */
    public void printAllTeams() {
        for (Team team : teamRepositoryJDBC.getAllTeams())
            if(!(team.getName().equals("Free")))
                team.printTeam();
    }

    /**
     * sorts all our teams from the database (budget)
     */
    public List<Team> sortAllTeamsByBudget() {
        List<Team> allTeams = teamRepositoryJDBC.getAllTeams();
        allTeams.sort(Comparator.comparing(Team::getBudget));
        return allTeams;
    }

    /**
     * sorts all the teams from our database(foundation year)
     */
    public List<Team> sortAllTeamsByFoundationYear() {
        List<Team> allTeams = teamRepositoryJDBC.getAllTeams();
        allTeams.sort(Comparator.comparing(Team::getFoundationYear));
        return allTeams;
    }

    /**
     * prints all the teams that are affiliated with a specific sponsor
     *
     * @param sponsor 1
     */
    public List<Team> getAllTeamsAffiliatedWithSponsor(Sponsor sponsor) {
        List<Team> allTeams = new ArrayList<>(sponsor.sponsoredTeams);
        if (allTeams.size() > 0)
            return allTeams;
        else
            return null;
    }

    /**
     * we sort teams (that are sponsored by 1 specific sponsor ) by budget
     *
     * @param sponsor 1
     */
    public List<Team> sortSponsoredTeamsByBudget(Sponsor sponsor) {
        List<Team> allTeams = new ArrayList<>(sponsor.sponsoredTeams);
        allTeams.sort(Comparator.comparing(Team::getBudget));
        if (allTeams.size() > 0)
            return allTeams;
        else
            return null;
    }

    /**
     *
     * @returns a list of teams sorted by name
     */
    public List<Team> sortAllTeamsByName() {
        List<Team> allTeams = teamRepositoryJDBC.getAllTeams();
        allTeams.sort(Comparator.comparing(Team::getName));
        return allTeams;
    }

    /**
     *
     * @return a list of teams sorted by value
     */
    public List<Team> sortAllTeamsByValue() {
        List<Team> allTeams = teamRepositoryJDBC.getAllTeams();
        allTeams.sort(Comparator.comparing(Team::getSquadValue));
        return allTeams;
    }

    /**
     *
     * @param country a specific country, that we search teams from
     * @return a list of teams from that specific country
     */
    public List<Team> sortAllTeamsByCountry(String country) {
        List<Team> allTeamsFromACountry = new ArrayList<>();
        for (Team team : teamRepositoryJDBC.getAllTeams())
            if (team.getCountry().contains(country))
                allTeamsFromACountry.add(team);
        return allTeamsFromACountry;
    }
}
