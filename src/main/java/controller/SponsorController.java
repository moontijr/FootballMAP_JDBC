package controller;

import model.Sponsor;
import model.Team;
import repository.jdbc.SponsorRepositoryJDBC;
import repository.jdbc.TeamRepositoryJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SponsorController {
    //private final SponsorRepositoryMemory sponsorRepositoryMemory;
    //private final TeamRepositoryMemory teamRepositoryMemory;
    private final SponsorRepositoryJDBC sponsorRepositoryJDBC;
    private final TeamRepositoryJDBC teamRepositoryJDBC;

    public SponsorController(SponsorRepositoryJDBC sponsorRepositoryJDBC, TeamRepositoryJDBC teamRepositoryJDBC) {
        this.teamRepositoryJDBC = teamRepositoryJDBC;
        this.sponsorRepositoryJDBC = sponsorRepositoryJDBC;
    }

    /**
     * we start a sponsorship between a team and a sponsor
     *
     * @param sponsor 1
     * @param team    1
     */
    public boolean startSponsorship(Sponsor sponsor, Team team) {
        if (team.sponsors.contains(sponsor))
            return false;
        else {
            sponsor.sponsorTeam(team);
            return true;
        }
    }

    /**
     * we end the sponsorship between a team and a firm
     *
     * @param sponsor 1
     * @param team    1
     */
    public boolean endSponsorship(Sponsor sponsor, Team team) {
        if (team.sponsors.contains(sponsor)) {
            sponsor.stopSponsorTeam(team);
            return true;
        } else {
            return false;
        }
    }

    /**
     * prints all the sponsors from the repo
     */
    public void printAllSponsors() {
        for (Sponsor sponsor : sponsorRepositoryJDBC.getAllSponsors())
            sponsor.printSponsor();
    }

    /**
     * sorts all our sponsors from our database by their name
     */
    public List<Sponsor> sortAllSponsorsByName() {
        List<Sponsor> sponsors = sponsorRepositoryJDBC.getAllSponsors();
        sponsors.sort(Comparator.comparing(Sponsor::getName));
        return sponsors;
    }

    /**
     * sorts all our sponsors from our database by their net worth
     */
    public List<Sponsor> sortAllSponsorsByNetWorth() {
        List<Sponsor> sponsors = sponsorRepositoryJDBC.getAllSponsors();
        sponsors.sort(Comparator.comparing(Sponsor::getNetWorth));
        return sponsors;
    }

    /**
     * gives us a list of all the teams, that the SPECIFIC sponsor is sponsoring
     *
     * @param sponsor from many teams
     */
    public List<Team> listAllTeamsFromASponsor(Sponsor sponsor) {
        List<Team> teams = new ArrayList<>(sponsor.sponsoredTeams);
        if (teams.size() > 0)
            return teams;
        else
            return null;
    }

    /**
     * sorts all the teams that are sponsored by a SPECIFIC sponsor
     *
     * @param sponsor from many teams
     */
    public List<Team> sortAllTeamsFromASponsorByMarketValue(Sponsor sponsor) {
        List<Team> teams = sponsor.sponsoredTeams;
        teams.sort(Comparator.comparing(Team::getBudget));
        if (teams.size() > 0)
            return teams;
        else
            return null;
    }

    /**
     * starting a sponsorship between a sponsor and a team, and checks if it already exists a sponsorship between these 2
     *
     * @param sponsor          sponsor
     * @param teamAbbreviation string
     */
    public boolean startSponsoring(Sponsor sponsor, String teamAbbreviation, int ammountOfMoney) throws SQLException {
        Team team1;
        int ok = 0;
        List <Team> teams=sponsor.getSponsoredTeams();
        for (Team team : teams) {
            if (team.getAbbreviation().equals(teamAbbreviation)) {
                ok = 1;
                break;
            }
        }
        if (ok == 0) {
            Connection connection2 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            for (Team team : teamRepositoryJDBC.getAllTeams())
                if (team.getAbbreviation().contains(teamAbbreviation)) {
                    team1 = team;
                    sponsor.sponsorTeam(team1);
                    int new_budget=team1.getBudget()+ammountOfMoney;
                    team1.setBudget(new_budget);


                    String sql1= "UPDATE TeamMAP SET budget=? WHERE abbreviation=?";
                    PreparedStatement preparedStatement1= connection2.prepareStatement(sql1);
                    preparedStatement1.setObject(1,new_budget);
                    preparedStatement1.setObject(2,team1.getAbbreviation());
                    preparedStatement1.execute();


                    Connection connection1 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
                    String sql = "INSERT INTO Sponsor_Teams (idTeam,idSponsor,AmmountOfMoney) VALUES (?,?,?)";
                    PreparedStatement preparedStatement = connection1.prepareStatement(sql);
                    preparedStatement.setObject(1, team1.getAbbreviation());
                    preparedStatement.setObject(2, sponsor.getAbbreviation());
                    preparedStatement.setObject(3, ammountOfMoney);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection1.close();
                    return true;
                }
        } else {
            System.out.println("Already a sponsorship between the 2 of them");
        }
        return false;

    }

    /**
     * ends a sponsorship between a sponsor and a team
     *
     * @param sponsor          sponsor
     * @param teamAbbreviation string
     */
    public boolean endSponsoring(Sponsor sponsor, String teamAbbreviation) throws SQLException {

                Team team1;
                Connection connection1 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
                String sql = "DELETE FROM Sponsor_Teams WHERE idTeam=? AND idSponsor=?";
                PreparedStatement preparedStatement = connection1.prepareStatement(sql);
                for (Team team2 : teamRepositoryJDBC.getAllTeams())
                    if (team2.getAbbreviation().contains(teamAbbreviation)) {
                        team1 = team2;
                        sponsor.stopSponsorTeam(team1);
                        preparedStatement.setObject(1, team1.getAbbreviation());
                        preparedStatement.setObject(2, sponsor.getAbbreviation());
                        preparedStatement.execute();
                        preparedStatement.close();
                        connection1.close();
                        //
                        return true;
                    }
        return false;
            }


    /**
     * gets all the sponsors from a specific team
     * @param team that we search for sponsors
     * @return a list of sponsors from a specific team
     */
    public List<Sponsor> allSponsorsFromATeam (Team team)
    {
        return new ArrayList<>(team.sponsors);
    }

}


