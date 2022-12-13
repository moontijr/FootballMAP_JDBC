package model;

public class Player extends Person{
    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String position;

    private String status; // free agent, or is playing at a team

    private int marketValue;

    public int getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }

    public Player(String id ,String firstName, String lastName, int age, String nationality, String position, int marketValue) {
        super(id,firstName, lastName, age, nationality);
        this.position = position;
        this.status = "Free Agent";
        this.marketValue = marketValue;
    }

    public String getPosition() {
        return position;
    }

    public void printPlayer() {
        System.out.println("First Name - " + this.getFirstName() + " | " + " Last Name - " + this.getLastName() + " | " + " Age -   " + this.getAge() + " | " + " Nationality - " + this.getNationality() + " | " + " Position - " + this.getPosition() + " | " + " Status : " + this.status + " | " + " Market Value - " + this.getMarketValue() + " Euro ");
    }

}
