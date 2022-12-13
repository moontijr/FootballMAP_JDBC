package model;

public class Coach extends Person{
    private String playStyle;

    private Team team;

    public Coach(String id,String firstName, String lastName, int age, String nationality, String playStyle, Team currentTeam) {
        super(id,firstName, lastName, age, nationality);
        this.playStyle = playStyle;
        this.team = currentTeam;
    }

    public Team getTeam() {
        return team;
    }


    public String getPlayStyle() {
        return playStyle;
    }

    public void setPlayStyle(String playStyle) {
        this.playStyle = playStyle;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void printCoach() {
        System.out.println("First Name : " + this.getFirstName() + " | " + " Last Name : " + this.getLastName() + " | " + " Age : " + this.getAge() + " Year Old " + " | " + " Nationality : " + this.getNationality() + " | " + "Play Style : " + this.getPlayStyle() + " Currently Training the Team :" + this.getTeam().getName());
    }

}
