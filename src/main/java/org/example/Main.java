package org.example;

import controller.CoachController;
import controller.PlayerController;
import controller.SponsorController;
import controller.TeamController;
import repository.jdbc.CoachRepositoryJDBC;
import repository.jdbc.PlayerRepositoryJDBC;
import repository.jdbc.SponsorRepositoryJDBC;
import repository.jdbc.TeamRepositoryJDBC;
import view.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)  {
        String connectionURL = "jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true";
        try {
            System.out.print("Connecting to the server......");
            try (Connection connection = DriverManager.getConnection(connectionURL)) {
                System.out.println("Connected to the Server.");
                TeamRepositoryJDBC teamRepositoryJDBC= TeamRepositoryJDBC.getInstance();
                SponsorRepositoryJDBC sponsorRepositoryJDBC= SponsorRepositoryJDBC.getInstance();
                PlayerRepositoryJDBC playerRepositoryJDBC = PlayerRepositoryJDBC.getInstance();
                CoachRepositoryJDBC coachRepositoryJDBC=CoachRepositoryJDBC.getInstance(teamRepositoryJDBC);
        Scanner scanner = new Scanner(System.in);
        PlayerController playerController = new PlayerController(playerRepositoryJDBC, teamRepositoryJDBC);
        UI ui = new UI(scanner,playerRepositoryJDBC,sponsorRepositoryJDBC,teamRepositoryJDBC,coachRepositoryJDBC,playerController,new TeamController(teamRepositoryJDBC),new CoachController(coachRepositoryJDBC,playerRepositoryJDBC),new SponsorController(sponsorRepositoryJDBC,teamRepositoryJDBC)
                );
        ui.loginMenu();



            } catch (Exception e) {
                System.out.println("I am not connected to the Server");
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


