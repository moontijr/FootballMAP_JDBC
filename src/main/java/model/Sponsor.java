package model;

import java.util.*;

public class Sponsor {
    private String name;
    private int netWorth;

    private String abbreviation;

    public List<Team> sponsoredTeams = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(int netWorth) {
        this.netWorth = netWorth;
    }

    public List<Team> getSponsoredTeams() {
        return sponsoredTeams;
    }

    public void setSponsoredTeams(List<Team> sponsoredTeams) {
        this.sponsoredTeams = sponsoredTeams;
    }

    public Sponsor(String name, String abbreviation, int netWorth) {
        this.name = name;
        this.netWorth = netWorth;
        this.abbreviation = abbreviation;
    }

    public void sponsorTeam(Team team) {
        this.sponsoredTeams.add(team);
        team.sponsors.add(this);
    }

    public void stopSponsorTeam(Team team) {
        this.sponsoredTeams.remove(team);
        team.sponsors.remove(this);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void disbandSponsor() {
        for (Team team : this.sponsoredTeams)
            stopSponsorTeam(team);
    }

    public void printSponsor() {
        System.out.println("Name of The Sponsor : " + this.name + " | " + " Net Worth : " + this.netWorth + " Euro ");
    }

}

