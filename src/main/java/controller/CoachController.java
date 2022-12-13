package controller;

import model.Coach;
import model.Player;
import model.Team;
import repository.jdbc.CoachRepositoryJDBC;
import repository.jdbc.PlayerRepositoryJDBC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CoachController {
//    private final CoachRepositoryMemory coachRepositoryMemory;
//    private final PlayerRepositoryMemory playerRepositoryMemory;

    private final CoachRepositoryJDBC coachRepositoryJDBC;
    private final PlayerRepositoryJDBC playerRepositoryJDBC;

    public CoachController(CoachRepositoryJDBC coachRepositoryJDBC, PlayerRepositoryJDBC playerRepositoryJDBC) {
        this.coachRepositoryJDBC=coachRepositoryJDBC;
        this.playerRepositoryJDBC=playerRepositoryJDBC;
    }

    /**
     * lists all the players from the squad that a coach trains
     */
    public List<Player> listYourSquadAsACoach(Coach coach) {
        List<Player> allPlayers = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
            if (player.getStatus().contains(coach.getTeam().getName())) {
                allPlayers.add(player);
            }
        }
        if (allPlayers.size() > 0)
            return allPlayers;
        else
            return null;

    }

    /**
     * lists all the players without the ones from the squad that the coach trains
     *
     * @param coach 1
     */
    public List<Player> listAllPlayersOutsideYourTeam(Coach coach) {
        List<Player> allPlayers = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
            if (!(player.getStatus().contains(coach.getTeam().getName()))) {
                allPlayers.add(player);
            }
        }

        if (allPlayers.size() > 0)
            return allPlayers;
        else
            return null;
    }

    /**
     * sorts the team that a coach trains (by the players value)
     *
     * @param coach 1
     */
    public List<Player> sortCoachTeamByValue(Coach coach) {
        List<Player> allPlayers = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
            if (player.getStatus().contains(coach.getTeam().getName())) {
                allPlayers.add(player);
            }
        }
        allPlayers.sort(Comparator.comparing(Player::getMarketValue));

        if (allPlayers.size() > 0)
            return allPlayers;
        else
            return null;
    }

    /**
     * sorts the team that a coaches train., by age
     *
     * @param coach 1
     */
    public List<Player> sortCoachTeamByAge(Coach coach) {
        List<Player> allPlayers = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
            if (player.getStatus().contains(coach.getTeam().getName())) {
                allPlayers.add(player);
            }
        }
        allPlayers.sort(Comparator.comparing(Player::getAge));

        if (allPlayers.size() > 0)
            return allPlayers;
        else
            return null;
    }

    /**
     * adds a player to the list (team)
     *
     * @param coach  1
     * @param player 1
     */
    public boolean addPlayer(Coach coach, Player player) throws SQLException {
        return coach.getTeam().addPlayerToTeam(player);
    }

    /**
     * transfers players
     *
     * @param coach  1
     * @param player 1
     * @param team   1
     */
    public boolean transferPlayer(Coach coach, Player player, Team team) throws SQLException {
        return coach.getTeam().transferPlayerToTeam(player, team);
    }


    /**
     * removes player ( makes him free agent )
     *
     * @param coach  1
     * @param player 1
     */
    public boolean removePlayer(Coach coach, Player player) throws SQLException {
        Team team = coach.getTeam();
        return team.removePlayerFromTeam(player);
    }

    /**
     * prints all the coaches from our database
     */
    public List<Coach> printAllCoaches() {
        List<Coach> coaches = new ArrayList<>(coachRepositoryJDBC.getAllCoaches());
        if (coaches.size() > 0)
            return coaches;
        else
            return null;
    }

    /**
     * sorts all our coaches from our database
     */
    public List<Coach> sortAllCoachesByAge() {
        List<Coach> coaches = new ArrayList<>(coachRepositoryJDBC.getAllCoaches());
        coaches.sort(Comparator.comparing(Coach::getAge));
        if (coaches.size() > 0)
            return coaches;
        else
            return null;

    }

    /**
     * gives us a list of Coaches with a SPECIFIC NATIONALITY
     *
     * @param nationality that we search for
     */
    public List<Coach> sortAllCoachesByNationality(String nationality) {
        List<Coach> allCoachesFromANationality = new ArrayList<>();
        for (Coach coach : coachRepositoryJDBC.getAllCoaches())
            if (coach.getNationality().contains(nationality))
                allCoachesFromANationality.add(coach);
        if (allCoachesFromANationality.size() > 0)
            return allCoachesFromANationality;
        else
            return null;
    }

    /**
     * sorts all our coaches from the database by their name
     */
    public List<Coach> sortAllCoachesByName() {
        List<Coach> coaches = new ArrayList<>(coachRepositoryJDBC.getAllCoaches());
        coaches.sort(Comparator.comparing(Coach::getFirstName));
        if (coaches.size() > 0)
            return coaches;
        else
            return null;
    }

    /**
     * gives us a list of all our coaches by a SPECIFIC playstyle
     *
     * @param playStyle that we search for
     */
    public List<Coach> sortAllCoachesByPlayStyle(String playStyle) {
        List<Coach> allCoachesWithAPlaystyle = new ArrayList<>();
        for (Coach coach : coachRepositoryJDBC.getAllCoaches())
            if (coach.getPlayStyle().contains(playStyle))
                allCoachesWithAPlaystyle.add(coach);
        if (allCoachesWithAPlaystyle.size() > 0)
            return allCoachesWithAPlaystyle;
        else
            return null;
    }

}
