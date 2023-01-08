package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String abbreviation;
    private String country;
    private String town;
    private int foundationYear;
    private int maxSquadSize;

    private int budget;

    private int squadValue;

    public int getSquadValue() {
        return squadValue;
    }


    public List<Player> squad;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public int getMaxSquadSize() {
        return maxSquadSize;
    }

    public void setMaxSquadSize(int maxSquadSize) {
        this.maxSquadSize = maxSquadSize;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Player> getSquad() {
        return squad;
    }



    public List<Sponsor> sponsors;


    public Team(String name, String abbreviation, String country, String town, int foundationYear, int maxSquadSize, int budget) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.country = country;
        this.town = town;
        this.foundationYear = foundationYear;
        this.maxSquadSize = maxSquadSize;
        this.squad = new ArrayList<>();
        this.sponsors = new ArrayList<>();
        this.budget = budget;
        this.squadValue = 0;
    }

    public void setSquadValue(int squadValue) {
        this.squadValue = squadValue;
    }

    /**
     * adds a player to a team
     *
     * @param player 1
     * @return false if the squad size is too big, and true if not
     */
    public boolean addPlayerToTeam(Player player) throws SQLException {
        if (this.squad.contains(player))
            return false;
        if (this.squad.size() >= this.maxSquadSize)
            return false;
        else {
            this.squad.add(player);
            this.setSquadValue(squadValue+ player.getMarketValue());
            player.setStatus("Playing at " + this.name);
            Connection connection1= DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "UPDATE PlayerMAP SET status=? WHERE id=? AND firstName=?";
            PreparedStatement preparedStatement=connection1.prepareStatement(sql);
            preparedStatement.setString(1,"Playing at " + this.name);
            preparedStatement.setObject(2,player.getId());
            preparedStatement.setObject(3,player.getFirstName());
            preparedStatement.execute();
            preparedStatement.close();
            connection1.close();
        }
        return true;
    }

    /**
     * removes a specific player from the squad
     *
     * @param player 1
     * @return true if the player can be removed, and false if not
     */
    public boolean removePlayerFromTeam(Player player) throws SQLException {
        if (this.squad.remove(player)) {
            player.setStatus("Free Agent");
            Connection connection1= DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "UPDATE PlayerMAP SET status=? WHERE id=? AND firstName=?";
            PreparedStatement preparedStatement=connection1.prepareStatement(sql);
            preparedStatement.setString(1,"Free Agent");
            preparedStatement.setObject(2,player.getId());
            preparedStatement.setObject(3,player.getFirstName());
            preparedStatement.execute();
            connection1.close();
            return true;

        } else {
            return false;
        }
    }

    /**
     * sells a player, and automatically, the budget is getting increased, and the player is gone
     *
     * @param player    1
     * @param otherTeam 1
     */
    public void sellPlayerFromOtherTeam(Player player, Team otherTeam) {
        otherTeam.budget += player.getMarketValue();
        otherTeam.squadValue -= player.getMarketValue();
        otherTeam.squad.remove(player);
    }

    /**
     * transfers the player from "OTHER TEAM" to team , and automatically, the team that lost the player, gets the money for him, and the other one, loses the money.
     *
     * @param player    1
     * @param otherTeam 1
     * @return true if the transfer can be made, false if not
     */
    public boolean transferPlayerToTeam(Player player, Team otherTeam) throws SQLException {
        if (this.squad.contains(player)) {
            return false;
        }

        if (!otherTeam.squad.contains(player))
        {
            return false;
        }

        if (this.squad.size() >= this.maxSquadSize) {
            return false;
        } else if (this.budget < player.getMarketValue()) {
            return false;
        }else {
            this.budget -= player.getMarketValue();
            this.squadValue += player.getMarketValue();
            this.squad.add(player);
            sellPlayerFromOtherTeam(player, otherTeam);
            player.setStatus("Playing at" + this.name);
            //schimbarea bugetului echipei care cumpara
            Connection connection1= DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "UPDATE TeamMAP SET budget=? WHERE name=? AND abbreviation=?";
            PreparedStatement preparedStatement=connection1.prepareStatement(sql);
            preparedStatement.setObject(1,this.budget);
            preparedStatement.setObject(2,this.getName());
            preparedStatement.setObject(3,this.getAbbreviation());
            preparedStatement.execute();
            preparedStatement.close();

            //sa i schimb statusul jucatorului
            String sql1= "UPDATE PlayerMAP SET status=? WHERE id=? AND firstName=?";
            PreparedStatement preparedStatement1=connection1.prepareStatement(sql1);
            preparedStatement1.setString(1,"Playing at " + this.getName());
            preparedStatement1.setObject(2,player.getId());
            preparedStatement1.setObject(3,player.getFirstName());
            preparedStatement1.execute();
            preparedStatement1.close();

            //schimbarea bugetului echipei de la care a fost cumparat
            String sql2= "UPDATE TeamMAP SET budget=? WHERE name=? AND abbreviation=?";
            PreparedStatement preparedStatement2=connection1.prepareStatement(sql2);
            preparedStatement2.setObject(1,otherTeam.getBudget());
            preparedStatement2.setObject(2,otherTeam.getName());
            preparedStatement2.setObject(3,otherTeam.getAbbreviation());
            preparedStatement2.execute();
            preparedStatement2.close();

            connection1.close();

            return true;
        }
    }

    public void disbandTeam() {
        for (Player player : this.squad)
            player.setStatus("Free Agent");
        for (Sponsor sponsor : this.sponsors)
            sponsor.stopSponsorTeam(this);

    }

    /**
     * printing format for a team
     */
    public void printTeam() {
        System.out.println("Team - " + this.name + " | " + " abbreviation - " + this.abbreviation + " | " + " Country -   " + this.country + " | " + " Town - " + this.town + " | " + " foundation year - " + this.foundationYear + " | " + " Squad Maximum Capacity - " + this.maxSquadSize + " | " + " Budget - " + this.budget + " Euro ");
    }
}