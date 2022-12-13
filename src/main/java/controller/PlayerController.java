package controller;

import model.Player;
import model.Team;
import repository.inmemory.PlayerRepositoryMemory;
import repository.inmemory.TeamRepositoryMemory;
import repository.jdbc.PlayerRepositoryJDBC;
import repository.jdbc.TeamRepositoryJDBC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerController {
    // private final PlayerRepositoryMemory playerRepositoryMemory;

    private final PlayerRepositoryJDBC playerRepositoryJDBC;
    //private final TeamRepositoryMemory teamRepositoryMemory;

    private final TeamRepositoryJDBC teamRepositoryJDBC;

    public PlayerController(PlayerRepositoryJDBC playerRepositoryJDBC, TeamRepositoryJDBC teamRepositoryJDBC) {
        this.playerRepositoryJDBC=playerRepositoryJDBC;
        this.teamRepositoryJDBC=teamRepositoryJDBC;
    }

    /**
     * sorts all the players from the database ( price)
     */
    public List<Player> sortAllPlayersByPrice() {
        List<Player> allPlayers = playerRepositoryJDBC.getAllPlayers();
        allPlayers.sort(Comparator.comparing(Player::getMarketValue));
        return allPlayers;
    }

    /**
     * sorts the squad list of a team (price)
     *
     * @param team specific one
     */
    public List<Player> sortPlayersFromSpecificTeamByPrice(Team team) {
        List<Player> allPlayers = new ArrayList<>(team.squad);
        allPlayers.sort(Comparator.comparing(Player::getMarketValue));
        return allPlayers;
    }

    /**
     * prints all the players from our database
     */
    public List<Player> printAllPlayers() {
        List<Player> players = playerRepositoryJDBC.getAllPlayers();
        if (players.size() > 0)
            return players;
        else
            return null;
    }

    /**
     * prints 2 lists, one of them is with all the free agents, and the other one is with the players that currently play for a team
     */
    public List<Player> sortPlayersByStatus(String string) {
        if (string.equals("Y") || string.equals("y")) {
            List<Player> freeAgents = new ArrayList<>();
            for (Player player : playerRepositoryJDBC.getAllPlayers())
                if (player.getStatus().contains("Free Agent"))
                    freeAgents.add(player);
            return freeAgents;
        } else {
            List<Player> playersWithContract = new ArrayList<>();
            for (Player player : playerRepositoryJDBC.getAllPlayers())
                if (!player.getStatus().contains("Free Agent"))
                    playersWithContract.add(player);
            return playersWithContract;
        }
    }

    /**
     * sorts All the players from the database, by age
     */
    public List<Player> sortAllPlayersByAge() {
        List<Player> allPlayers = playerRepositoryJDBC.getAllPlayers();
        allPlayers.sort(Comparator.comparing(Player::getAge));
        return allPlayers;
    }

    /**
     * gives us a list with all the players that have a specified position
     *
     * @param position string
     */
    public List<Player> sortAllPlayersByPosition(String position) {
        List<Player> allPlayersFromAPosition = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
            if (player.getPosition().contains(position))
                allPlayersFromAPosition.add(player);
        }
        if (allPlayersFromAPosition.size() > 0)
            return allPlayersFromAPosition;
        else
            return null;
    }

    /**
     * gives us a list of players from a SPECIFIC TEAM, that play in a SPECIFIC POSITION
     *
     * @param team     that we want
     * @param position that we search for
     */
    public List<Player> sortPlayersFromSpecificTeamByPosition(Team team, String position) {
        List<Player> allPlayersFromTeamByPosition = new ArrayList<>();
        for (Player player : team.squad)
            if (player.getPosition().contains(position))
                allPlayersFromTeamByPosition.add(player);
        if (allPlayersFromTeamByPosition.size() > 0)
            return allPlayersFromTeamByPosition;
        else
            return null;
    }


    /**
     * prints a list with players with a specific nationality
     *
     * @param nationality of a player
     */
    public List<Player> sortAllPlayersByNationality(String nationality) {
        List<Player> allPlayersFromANationality = new ArrayList<>();
        for (Player player : playerRepositoryJDBC.getAllPlayers())
            if (player.getNationality().contains(nationality))
                allPlayersFromANationality.add(player);
        if (allPlayersFromANationality.size() > 0)
            return allPlayersFromANationality;
        else
            return null;
    }

    /**
     * sorts all the players by their family Name
     */
    public List<Player> sortAllPlayersByName() {
        List<Player> allPlayers = playerRepositoryJDBC.getAllPlayers();
        allPlayers.sort(Comparator.comparing(Player::getLastName));
        return allPlayers;
    }

    /**
     * prints all the players from the database, WITHOUT YOURSELF
     *
     * @param player is yourself
     */
    public List<Player> seeAllOtherPlayersWithoutYourself(Player player) {
        List<Player> allPlayersWithoutYourself = new ArrayList<>();
        for (Player player2 : playerRepositoryJDBC.getAllPlayers()) {
            if (!(player2.getLastName().contains(player.getLastName()) && player.getFirstName().contains(player.getFirstName())))
                allPlayersWithoutYourself.add(player2);
        }
        if (allPlayersWithoutYourself.size() > 0)
            return allPlayersWithoutYourself;
        else
            return null;

    }

    /**
     * Shows the teams that can afford a specific player
     *
     * @param player is the specific player
     */
    public List<Team> isAffordable(Player player) {
        List<Team> allPotentiallyTeams = new ArrayList<>();
        for (Team team : teamRepositoryJDBC.getAllTeams()) {
            if (team.getBudget() >= player.getMarketValue() && team.getMaxSquadSize() > team.squad.size()) {
                allPotentiallyTeams.add(team);
            }
        }
        if (allPotentiallyTeams.size() > 0) {
            return allPotentiallyTeams;
        } else
            return null;
    }

    public boolean checkString(String sample)
    {
        char[] chars= sample.toCharArray();
        for (char c: chars)
            if(Character.isDigit(c))
                return false;
        return true;
    }


}
