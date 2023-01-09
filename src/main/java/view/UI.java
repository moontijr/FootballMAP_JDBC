package view;

import controller.CoachController;
import controller.PlayerController;
import controller.SponsorController;
import controller.TeamController;
import exceptions.*;
import model.Coach;
import model.Player;
import model.Sponsor;
import model.Team;
import repository.jdbc.CoachRepositoryJDBC;
import repository.jdbc.PlayerRepositoryJDBC;
import repository.jdbc.SponsorRepositoryJDBC;
import repository.jdbc.TeamRepositoryJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UI {
    private final Scanner userInput;
    private final PlayerRepositoryJDBC playerRepositoryJDBC;

    private final TeamRepositoryJDBC teamRepositoryJDBC;

    private final SponsorRepositoryJDBC sponsorRepositoryJDBC;

    private final CoachRepositoryJDBC coachRepositoryJDBC;

    private final PlayerController playerController;
    private final TeamController teamController;
    private final CoachController coachController;
    private final SponsorController sponsorController;


    public UI(Scanner userInput, PlayerRepositoryJDBC playerRepositoryJDBC, SponsorRepositoryJDBC sponsorRepositoryJDBC, TeamRepositoryJDBC teamRepositoryJDBC, CoachRepositoryJDBC coachRepositoryJDBC, PlayerController playerController, TeamController teamController, CoachController coachController, SponsorController sponsorController) {
        this.userInput = userInput;
        this.playerRepositoryJDBC = playerRepositoryJDBC;
        this.sponsorRepositoryJDBC = sponsorRepositoryJDBC;
        this.teamRepositoryJDBC = teamRepositoryJDBC;
        this.coachRepositoryJDBC = coachRepositoryJDBC;
        this.playerController = playerController;
        this.teamController = teamController;
        this.coachController = coachController;
        this.sponsorController = sponsorController;
    }


    public void loginMenu() throws SQLException {
        System.out.println("""
                Welcome to our football database! You can enter the database as a player, as a coach, as a sponsor, as a guest or as the admin.
                Please enter your credentials:
                Type anything to get into the Guest Menu, where you can see detailed informations about our Players/Coaches/Teams/Sponsors
                Type the specific login credentials to get into the Player/Coach/Sponsor SubMenu where you can do specific stuff
                Type the specific login credentials to get into the ADMIN Menu, for the CRUD Functions. Thank you!
                For Entering the Player subMenu, User: player Pword:player
                For Entering the Coach subMenu, User: coach Pword:coach
                For Entering the Sponsor subMenu, User : sponsor Pword: Sponsor
                For Entering the Admin subMenu, you know it :D
 
                """);
        System.out.println("Username: ");
        String username = this.userInput.nextLine();
        System.out.println("Password: ");
        String password = this.userInput.nextLine();
        int hashedpassword = password.hashCode();
        String hashedpasswordString = (String.valueOf(hashedpassword));
        String credentials = username + hashedpasswordString;
        switch (credentials) {
            case "player-985752863" -> {
                System.out.println("You are currently in Player Mode.");
                this.playerMenu();
            }
            case "coach94831770" -> {
                System.out.println("You are currently in Coach Mode.");
                this.coachMenu();
            }
            case "sponsor-1998892262" -> {
                System.out.println("You are currently in Sponsor Mode.");
                this.sponsorMenu();
            }
            case "admin92668751" -> {
                System.out.println("You are currently in Admin Mode.");
                this.adminDBsMenu();
            }
            default -> {
                System.out.println("You are currently in Guest Mode.");
                this.dataBasesMenu();
            }
        }
    }

    /***
     * Old starting method before loginMenu
     */
//    public void startMenu() throws SQLException {
//        System.out.println("""
//                Welcome to our football database!
//                Which user mode do you choose? (Please choose a number between 1 and 6)
//                1.Player
//                2.Coach
//                3.Sponsor
//                4.Guest
//                5.Admin
//                6.Exit
//                """);
//
//
//        int choice = this.userInput.nextInt();
//        boolean validator = false;
//
//        try {
//            while (!validator) {
//                if (choice > 0 && choice < 7)
//                    validator = true;
//                else {
//                    System.out.println("Please choose a number between 1 and 6!");
//                    choice = userInput.nextInt();
//                }
//            }
//        } catch (NumberFormatException numberFormatException) {
//            System.out.println("Please enter a number. Press anything to go back");
//            this.userInput.nextLine();
//            this.loginMenu();
//        }
//        switch (choice) {
//            case 1 -> {
//                System.out.println("You are currently in Player Mode.");
//                this.playerMenu();
//            }
//            case 2 -> {
//                System.out.println("You are currently in Coach Mode.");
//                this.coachMenu();
//            }
//            case 3 -> {
//                System.out.println("You are currently in Sponsor Mode.");
//                this.sponsorMenu();
//            }
//            case 4 -> {
//                System.out.println("You are currently in Guest Mode.");
//                this.dataBasesMenu();
//            }
//            case 5 -> {
//                System.out.println("You are currently in Admin Mode.");
//                this.adminDBsMenu();
//            }
//            case 6 -> System.exit(0);
//        }
//
//    }

    public void playerMenu() throws SQLException {
        System.out.println("""
                Do you want to register yourself in our database? Please choose a number between 1 and 3
                1. Yes
                2. I am already there
                3. Go back
                """);
        //int choice = this.userInput.nextInt();
        boolean validator = false;

        try {
            int choice = this.userInput.nextInt();
            while (!validator) {

                if (choice > 0 && choice < 4)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 3!");
                    this.playerMenu();
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Tell us your details");
                    this.userInput.nextLine();
                    System.out.println("Your Unique Id:");
                    String id = this.userInput.nextLine();
                    try {
                        for (Player player : playerRepositoryJDBC.getAllPlayers()) {
                            if (player.getId().equals(id))
                                throw new AlreadyPlayerException();
                        }
                    } catch (AlreadyPlayerException alreadyPlayerException) {
                        System.out.println(alreadyPlayerException.getMessage());
                        this.userInput.nextLine();
                        this.playerMenu();
                    }
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.playerMenu();
                    }

                    System.out.println("Last Name: ");
                    String lastName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(lastName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException2) {
                        System.out.println(noDigitsInPersonNameException2.getMessage());
                        this.userInput.nextLine();
                        this.playerMenu();
                    }

                    System.out.println("Age: ");
                    try {
                        int age = Integer.parseInt(this.userInput.nextLine());
                        if (age < 0)
                            throw new ImproperIntegerException();

                        System.out.println("Nationality: ");
                        String nationality = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(nationality)) {
                                throw new NoDigitsInNationalityException();
                            }
                        } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                            System.out.println(noDigitsInNationalityException.getMessage());
                            this.userInput.nextLine();
                            this.playerMenu();
                        }

                        System.out.println("Position: ");
                        String position = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(position)) {
                                throw new NoDigitsInPositionException();
                            }
                        } catch (NoDigitsInPositionException noDigitsInPositionException) {
                            System.out.println(noDigitsInPositionException.getMessage());
                            this.userInput.nextLine();
                            this.playerMenu();
                        }

                        System.out.println("Market value: ");
                        try {
                            int marketValue = Integer.parseInt(this.userInput.nextLine());
                            if (marketValue < 0)
                                throw new ImproperIntegerException();

                            Player newPlayer = new Player(id, firstName, lastName, age, nationality, position, marketValue);
                            if (!playerRepositoryJDBC.existsPlayerForAdmin(id, firstName))
                                this.playerRepositoryJDBC.add(newPlayer);
                            else System.out.println("Player is already there");
                            subMenuPlayer(newPlayer);
                        } catch (ImproperIntegerException | NumberFormatException marketValueException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.playerMenu();
                        }
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.playerMenu();
                    }
                }


                case 2 -> {
                    System.out.println("Great! Tell us your details:");
                    this.userInput.nextLine();
                    System.out.println("ID : ");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.playerMenu();
                    }
                    for (Player player : playerRepositoryJDBC.getAllPlayers()) {
                        if (player.getFirstName().contains(firstName) && player.getId().contains(id))
                            subMenuPlayer(player);
                    }
                    System.out.println("There is no player with that name and ID. Please try again with a different name");
                    this.playerMenu();
                }
                case 3 -> {
                    this.userInput.nextLine();
                    this.loginMenu();
                }
            }

        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.playerMenu();
        }


    }

    public void subMenuPlayer(Player player) throws SQLException {
        System.out.println("""
                You have the following options:
                1. See all other players
                2. See teams that could afford you
                3. See informations about your team
                4. See players that have a higher market value than you
                5. Go back
                6. Exit
                """);

        try {
            int choice2 = this.userInput.nextInt();
            boolean validator2 = false;

            while (!validator2) {
                if (choice2 > 0 && choice2 < 7)
                    validator2 = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice2 = userInput.nextInt();
                }
            }
            switch (choice2) {
                case 1:
                    List<Player> allOtherPlayers = playerController.seeAllOtherPlayersWithoutYourself(player);
                    if (allOtherPlayers == null)
                        System.out.println("There are no players");
                    else
                        for (Player otherPlayer : allOtherPlayers) {
                            otherPlayer.printPlayer();
                        }
                    this.subMenuPlayer(player);
                case 2:
                    List<Team> potentialTeams;
                    potentialTeams = playerController.isAffordable(player);
                    if (potentialTeams == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : potentialTeams) {
                            team.printTeam();
                        }
                    }
                    this.subMenuPlayer(player);
                case 3:
                    for(Team team : teamRepositoryJDBC.getAllTeams())
                        if(player.getStatus().contains(team.getName())&&!player.getStatus().contains("Free"))
                            team.printTeam();
                    if(player.getStatus().contains("Free"))
                    {
                        System.out.println("You are currently a free agent, you dont have a team!");
                    }
                    this.subMenuPlayer(player);
                case 4:
                    if(playerController.allHigherPlayers(player)==null)
                    {
                        System.out.println("No such players ");
                        this.subMenuPlayer(player);
                    }
                    else
                    {
                        for(Player player1 : playerController.allHigherPlayers(player))
                            player1.printPlayer();
                        this.subMenuPlayer(player);
                    }
                case 5:
                    this.userInput.nextLine();
                    this.playerMenu();
                case 6:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.subMenuPlayer(player);
        }

    }

    private void coachMenu() throws SQLException {
        System.out.println("""
                Do you want to register yourself in our database? Please choose a number between 1 and 3
                1. Yes
                2. I am already there
                3. Go back
                """);
        int choice = this.userInput.nextInt();
        boolean validator = false;

        try {
            while (!validator) {
                if (choice > 0 && choice < 4)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 3!");
                    this.coachMenu();
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("Tell us your details");
                    this.userInput.nextLine();
                    System.out.println("Your unique ID");
                    String id = this.userInput.nextLine();
                    try
                    {
                        for (Coach coach : coachRepositoryJDBC.getAllCoaches())
                        {
                            if (coach.getId().equals(id))
                                throw new AlreadyCoachException();
                        }
                    } catch (AlreadyCoachException alreadyCoachException)
                    {
                        System.out.println(alreadyCoachException.getMessage());
                        this.userInput.nextLine();
                        this.coachMenu();
                    }
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.coachMenu();
                    }
                    System.out.println("Last Name: ");
                    String lastName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(lastName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.coachMenu();
                    }
                    System.out.println("Age: ");
                    try {
                        int age = Integer.parseInt(this.userInput.nextLine());
                        if (age < 0)
                            throw new ImproperIntegerException();

                        System.out.println("Nationality: ");
                        String nationality = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(nationality)) {
                                throw new NoDigitsInNationalityException();
                            }
                        } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                            System.out.println(noDigitsInNationalityException.getMessage());
                            this.userInput.nextLine();
                            this.coachMenu();
                        }

                        System.out.println("Playstyle: ");
                        String playstyle = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(playstyle))
                                throw new NoDigitsInPlaystyleException();
                        } catch (NoDigitsInPlaystyleException noDigitsInPlaystyleException) {
                            System.out.println(noDigitsInPlaystyleException.getMessage());
                            this.userInput.nextLine();
                            this.coachMenu();
                        }

                        System.out.println("Your current team(abbreviation): ");
                        String myTeam = this.userInput.nextLine();
                        int counter = 0;
                        for (Team team : teamRepositoryJDBC.getAllTeams())
                            if (team.getAbbreviation().contains(myTeam))
                                counter++;
                        if (counter == 0) {
                            System.out.println("There is no team with such an abbreviation in our database");
                            this.coachMenu();
                        } else {
                            Team newTeam = null;
                            for (Team team : teamRepositoryJDBC.getAllTeams())
                                if (team.getAbbreviation().contains(myTeam))
                                    newTeam = team;

                            Coach newCoach = new Coach(id, firstName, lastName, age, nationality, playstyle, newTeam);
                            if (!this.coachRepositoryJDBC.existsCoachForAdmin(newCoach.getId(), newCoach.getFirstName()))
                                coachRepositoryJDBC.add(newCoach);
                            else

                                System.out.println("Coach already exists");
                            this.subMenuCoach(newCoach);
                        }
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.coachMenu();
                    }

                }


                case 2 -> {
                    System.out.println("Great! Tell us your details:");
                    this.userInput.nextLine();
                    System.out.println("ID : ");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.coachMenu();
                    }
                    for (Coach coach : coachRepositoryJDBC.getAllCoaches()) {
                        if (coach.getFirstName().contains(firstName) && coach.getId().contains(id))
                            if(coach.getTeam().getAbbreviation().equals("Free")) {
                                subMenuFreeCoach(coach);
                            }
                            else
                            {
                                subMenuCoach(coach);
                            }
                    }
                    System.out.println("There is no coach with that name, please try again with a different name");
                    this.coachMenu();
                }

                case 3 -> {
                    this.userInput.nextLine();
                    loginMenu();
                }
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.coachMenu();
        }

    }

    private void subMenuFreeCoach(Coach coach) throws SQLException {
        System.out.println("""
                You have the following options. Please choose a number between 1 and 5:
                1. See all players
                2. See all teams
                3. See all other coaches
                4. Go back
                5. Exit
                """);
        try {
            int choice2 = this.userInput.nextInt();
            boolean validator2 = false;

            while (!validator2) {
                if (choice2 > 0 && choice2 <= 6)
                    validator2 = true;
                else {
                    System.out.println("Please choose a number between 1 and 5!");
                    choice2 = userInput.nextInt();
                }
            }
            switch (choice2) {
            case 1:
                List<Player> allPlayers = playerController.printAllPlayers();
                if (allPlayers == null)
                    System.out.println("There are no players\n");
                else {
                    for (Player player : allPlayers) {
                        player.printPlayer();
                    }
                }
                this.subMenuFreeCoach(coach);
                break;
            case 2:
                teamController.printAllTeams();
                this.subMenuFreeCoach(coach);
                break;
                case 3:
                    List <Coach> allCoachesWithoutCoach=coachRepositoryJDBC.getAllCoaches();
                    allCoachesWithoutCoach.remove(coach);
                    for(Coach coach1 : allCoachesWithoutCoach)
                        coach1.printCoach();
                    this.subMenuFreeCoach(coach);
                case 4:
                    this.coachMenu();
                case 5:
                    System.exit(0);
            }


        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.subMenuFreeCoach(coach);
        }
    }

    private void subMenuCoach(Coach coach) throws SQLException {
        System.out.println(
                """
                You have the following options. Please choose a number between 1 and 9:
                1. See your squad(if you coach one currently)
                2. See players outside your team
                3. Sort your players by value
                4. Sort player by position
                5. Sign a player for your team
                6. Sign a free agent player
                7. Release a player from your team
                8. Go back
                9. Exit
                """);

        try {
            for (Team team : teamRepositoryJDBC.getAllTeams())
                if(team.getAbbreviation().contains(coach.getTeam().getAbbreviation()))
                    coach.setTeam(team);
            int choice2 = this.userInput.nextInt();
            boolean validator2 = false;

            while (!validator2) {
                if (choice2 > 0 && choice2 <= 9)
                    validator2 = true;
                else {
                    System.out.println("Please choose a number between 1 and 9!");
                    choice2 = userInput.nextInt();
                }
            }
            switch (choice2) {
                case 1:
                    List<Player> myTeam = coachController.listYourSquadAsACoach(coach);
                    if (myTeam == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : myTeam) {
                            player.printPlayer();
                        }
                    }
                    this.subMenuCoach(coach);
                    break;
                case 2:
                    List<Player> otherPlayers = coachController.listAllPlayersOutsideYourTeam(coach);
                    if (otherPlayers == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : otherPlayers) {
                            player.printPlayer();
                        }
                    }
                    this.subMenuCoach(coach);
                    break;
                case 3:
                    List<Player> myTeamSorted = playerController.sortPlayersFromSpecificTeamByPrice(coach.getTeam());
                    if (myTeamSorted == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : myTeamSorted) {
                            player.printPlayer();
                        }
                    }
                    this.subMenuCoach(coach);
                    break;
                case 4:
                    this.userInput.nextLine();
                    System.out.println("Please choose a position");
                    String position = userInput.nextLine();
                    List<Player> myTeamSpecificPosition = playerController.sortPlayersFromSpecificTeamByPosition(coach.getTeam(), position);
                    if (myTeamSpecificPosition == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : myTeamSpecificPosition) {
                            player.printPlayer();
                        }
                    }
                    this.subMenuCoach(coach);
                    break;
                case 5:
                    Player player1 = null;
                    Team team1 = null;
                    System.out.println("Tell us the details of the followed player:");
                    this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    System.out.println("Last Name: ");
                    String lastName = this.userInput.nextLine();
                    if(playerRepositoryJDBC.findById(firstName,lastName)!=null) {
                        System.out.println("Tell us the team that the followed player is currently playing at");
                        String hisCurrentTeam = this.userInput.nextLine();
                        int ok = 0;
                        for (Team team : teamRepositoryJDBC.getAllTeams())
                            if (team.getAbbreviation().equals(hisCurrentTeam)) {
                                ok = 1;
                                break;
                            }
                        if (ok == 1) {
                            for (Player player : playerRepositoryJDBC.getAllPlayers())
                                if (player.getFirstName().contains(firstName) && player.getLastName().contains(lastName))
                                    player1 = player;
                            for (Team team : teamRepositoryJDBC.getAllTeams()) {
                                if (team.getAbbreviation().contains(hisCurrentTeam)) {
                                    team1 = team;
                                }
                            }

                            assert team1 != null;
                            if (coach.getTeam().getSquad().size() >= coach.getTeam().getMaxSquadSize()) {
                                System.out.println("No more places in the squad");
                            } else {
                                assert player1 != null;
                                if (coach.getTeam().getBudget() < player1.getMarketValue()) {
                                    System.out.println("Budget is too low");
                                }
                            }
                            coach.getTeam().transferPlayerToTeam(player1, team1);
                            this.subMenuCoach(coach);
                            break;
                        }
                        else
                        {
                            System.out.println("No team with such abbreviation.");
                            this.subMenuCoach(coach);

                        }
                        }
                    else
                    {
                        System.out.println("No such Player with that name");
                        this.subMenuCoach(coach);
                    }
                case 6:
                    Player player2 = null;
                    System.out.println("Tell us the details of the followed player:");
                    this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName2 = this.userInput.nextLine();
                    System.out.println("Last Name: ");
                    String lastName2 = this.userInput.nextLine();
                    for (Player player : playerRepositoryJDBC.getAllPlayers())
                        if (player.getFirstName().contains(firstName2) && player.getLastName().contains(lastName2))
                            player2 = player;
                    assert player2 != null;
                    if(player2.getStatus().contains("Free"))
                        coach.getTeam().addPlayerToTeam(player2);
                    else
                        System.out.println("He already plays for a team, he is not a free agent");

                    this.subMenuCoach(coach);


                case 7:
                    System.out.println("Tell us the details of player you want to release:");
                    this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName3 = this.userInput.nextLine();
                    System.out.println("Last Name: ");
                    String lastName3 = this.userInput.nextLine();
                    Player player3 = null;
                    for (Player player : playerRepositoryJDBC.getAllPlayers())
                        if (player.getFirstName().contains(firstName3) && player.getLastName().contains(lastName3)) {
                            player3 = player;
                        }
                    assert player3 != null;
                    if (player3.getStatus().contains(coach.getTeam().getName())) {
                        coach.getTeam().removePlayerFromTeam(player3);
                        player3.setStatus("Free Agent");
                        Connection connection1= DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
                        String sql= "UPDATE PlayerMAP SET status=? WHERE id=? AND firstName=?";
                        PreparedStatement preparedStatement=connection1.prepareStatement(sql);
                        preparedStatement.setString(1,"Free Agent");
                        preparedStatement.setObject(2,player3.getId());
                        preparedStatement.setObject(3,player3.getFirstName());
                        preparedStatement.execute();
                        connection1.close();
                        System.out.println("Player Removed");
                    } else {
                        System.out.println("Player wasn't in the squad");
                    }
                    this.subMenuCoach(coach);
                    break;
                case 8:
                    this.userInput.nextLine();
                    this.coachMenu();
                case 9:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.subMenuCoach(coach);
        }
    }


    private void sponsorMenu() throws SQLException {
        System.out.println("""
                Do you want to register your firm in our database? Please choose a number between 1 and 3
                1. Yes
                2. It's already there
                3. Go back
                """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 4)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 3!");
                    this.sponsorMenu();
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("Tell us the firm's details:");
                    this.userInput.nextLine();
                    System.out.println("Name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    try
                    {
                        for (Team team : teamRepositoryJDBC.getAllTeams())
                        {
                            if(team.getAbbreviation().equals(abbreviation))
                                throw new AlreadyTeamException();
                        }
                    }
                    catch (AlreadyTeamException alreadyTeamException)
                    {
                        System.out.println(alreadyTeamException.getMessage());
                        this.userInput.nextLine();
                        this.sponsorMenu();
                    }
                    System.out.println("Net worth: ");
                    try {
                        int netWorth = Integer.parseInt(this.userInput.nextLine());
                        if (netWorth < 0)
                            throw new ImproperIntegerException();
                        Sponsor newSponsor = new Sponsor(name, abbreviation, netWorth);
                        if (!this.sponsorRepositoryJDBC.existsSponsor(name, abbreviation))
                            sponsorRepositoryJDBC.add(newSponsor);
                        else
                            System.out.println("Sponsor already exists");
                        this.subMenuSponsor(newSponsor);
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.sponsorMenu();
                    }
                case 2:
                    System.out.println("Great! Tell us your name of the firm");
                    this.userInput.nextLine();
                    String nameOfTheFirm = this.userInput.nextLine();
                    System.out.println("Tell us the abbreviation of the firm");
                    String abbreviationOfTheFirm=this.userInput.nextLine();
                    for (Sponsor sponsor1 : sponsorRepositoryJDBC.getAllSponsors())
                        if (sponsor1.getName().contains(nameOfTheFirm) && sponsor1.getAbbreviation().contains(abbreviationOfTheFirm))
                            subMenuSponsor(sponsor1);
                    break;
                case 3:
                    this.userInput.nextLine();
                    this.loginMenu();
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.sponsorMenu();
        }
    }

    private void subMenuSponsor(Sponsor sponsor) throws SQLException {
        System.out.println("""
                You have the following options. Please choose a number from 1 to 6:
                1. List all teams that you are currently sponsoring
                2. Start a sponsorship
                3. End a sponsorship
                4. Sort sponsored teams by budget
                5. Go back
                6. Exit
                """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1:
                    List<Team> sameSponsorTeams = sponsorController.listAllTeamsFromASponsor(sponsor);
                    if (sameSponsorTeams == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : sameSponsorTeams)
                            team.printTeam();
                    }
                    this.subMenuSponsor(sponsor);
                    break;
                case 2:
                    System.out.println("Please type the abbreviation from the team you wish to sponsor");
                    this.userInput.nextLine();
                    String inputString = userInput.nextLine();
                    System.out.println("How much money to sponsor with?:");
                    int ammountOfMoney = this.userInput.nextInt();
                    sponsorController.startSponsoring(sponsor, inputString, ammountOfMoney);
                    this.subMenuSponsor(sponsor);
                    break;
                case 3:
                    System.out.println("Please type the abbreviation from the team you wish to stop sponsoring");
                    this.userInput.nextLine();
                    String inputString1 = userInput.nextLine();
                    sponsorController.endSponsoring(sponsor, inputString1);
                    this.subMenuSponsor(sponsor);

                case 4:
                    List<Team> sponsoredTeams = teamController.sortSponsoredTeamsByBudget(sponsor);
                    if (sponsoredTeams == null)
                        System.out.println("There are no teams.");
                    else {
                        for (Team team : sponsoredTeams)
                            team.printTeam();
                    }
                    this.subMenuSponsor(sponsor);
                case 5:
                    this.userInput.nextLine();
                    this.loginMenu();
                case 6:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.subMenuSponsor(sponsor);
        }

    }

    private void dataBasesMenu() throws SQLException {
        System.out.println("""
                Access one of the following databases(please choose a number from 1 to 6):
                1. Players
                2. Coaches
                3. Teams
                4. Sponsors
                5. Go back
                6. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("You are currently in the Players Database.");
                    this.playerDBMenu();
                }
                case 2 -> {
                    System.out.println("You are currently in the Coaches Database.");
                    this.coachDBMenu();
                }
                case 3 -> {
                    System.out.println("You are currently in the Teams Database.");
                    this.teamsDBMenu();
                }
                case 4 -> {
                    System.out.println("You are currently in the Sponsors Database.");
                    this.sponsorDBMenu();
                }
                case 5 -> {
                    this.userInput.nextLine();
                    this.loginMenu();
                }

                case 6 -> System.exit(0);


            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.dataBasesMenu();
        }
    }

    private void playerDBMenu() throws SQLException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 9):
                1. List all players
                2. Sort players by value
                3. Filter players by position
                4. Filter players by status
                5. Sort players by age
                6. Filter players by nationality
                7. Sort players by name
                8. Go back
                9. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 10)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 9!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1:
                    List<Player> allPlayers = playerController.printAllPlayers();
                    if (allPlayers == null)
                        System.out.println("There are no players\n");
                    else {
                        for (Player player : allPlayers) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 2:
                    List<Player> sortedPlayers = playerController.sortAllPlayersByPrice();
                    if (sortedPlayers == null)
                        System.out.println("There are no players\n");
                    else {
                        for (Player player : sortedPlayers) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 3:
                    System.out.println("Please choose a position");
                    this.userInput.nextLine();
                    String inputString1 = userInput.nextLine();
                    List<Player> playersWithSelectedPosition = playerController.sortAllPlayersByPosition(inputString1);
                    if (playersWithSelectedPosition == null)
                        System.out.println("There are no players\n");
                    else {
                        for (Player player : playersWithSelectedPosition) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 4:
                    System.out.println("Do you want to see the free agents?\n (Y/y, else, we will list players that have a team");
                    this.userInput.nextLine();
                    String answer = this.userInput.nextLine();
                    List<Player> statusPlayers = playerController.sortPlayersByStatus(answer);
                    if (statusPlayers.size() == 0)
                        System.out.println("There are no players\n");
                    else {
                        for (Player player : statusPlayers) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 5:
                    List<Player> playersByAge = playerController.sortAllPlayersByAge();
                    if (playersByAge == null)
                        System.out.println("There are no players\n");
                    else {
                        for (Player player : playersByAge) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 6:
                    System.out.println("Please choose a nationality");
                    this.userInput.nextLine();
                    String inputString = userInput.nextLine();
                    List<Player> specificCountry = playerController.sortAllPlayersByNationality(inputString);
                    if (specificCountry == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : specificCountry) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 7:
                    List<Player> sortedByName = playerController.sortAllPlayersByName();
                    if (sortedByName == null)
                        System.out.println("There are no players");
                    else {
                        for (Player player : sortedByName) {
                            player.printPlayer();
                        }
                    }
                    this.playerDBMenu();
                    break;
                case 8:
                    this.dataBasesMenu();
                case 9:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.playerDBMenu();
        }
    }

    private void coachDBMenu() throws SQLException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 8):
                1. List all coaches
                2. Filter coaches by playing style
                3. Sort coaches by age
                4. See all the coaches-teams pair
                5. Filter coaches by nationality
                6. Sort coaches by name
                7. Go back
                8. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 9)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 8!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1:
                    List<Coach> allCoaches = coachController.printAllCoaches();
                    if (allCoaches == null)
                        System.out.println("There are no coaches");
                    else {
                        for (Coach coach : allCoaches)
                            coach.printCoach();
                    }
                    this.coachDBMenu();
                    break;
                case 2:
                    System.out.println("Please choose the playing style of the coaches you want to see");
                    this.userInput.nextLine();
                    String playstyle = userInput.nextLine();
                    List<Coach> allCoachesSortedByPlaystyle = coachController.sortAllCoachesByPlayStyle(playstyle);
                    if (allCoachesSortedByPlaystyle == null)
                        System.out.println("There are no coaches with this playstyle");
                    else {
                        for (Coach coach : allCoachesSortedByPlaystyle)
                            coach.printCoach();
                    }
                    this.coachDBMenu();
                    break;
                case 3:
                    List<Coach> allCoachesSortedByAge = coachController.sortAllCoachesByAge();
                    if (allCoachesSortedByAge == null)
                        System.out.println("There are no coaches");
                    else {
                        for (Coach coach : allCoachesSortedByAge)
                            coach.printCoach();
                    }
                    this.coachDBMenu();
                    break;
                case 4:
                    for (Coach coach : coachRepositoryJDBC.getAllCoaches()) {
                        System.out.println(coach.getFirstName() + " " + coach.getLastName() + " training the Team " + coach.getTeam().getName());
                    }
                    this.coachDBMenu();
                    break;
                case 5:
                    System.out.println("Please choose a nationality");
                    this.userInput.nextLine();
                    String country = userInput.nextLine();
                    List<Coach> allCoachesSortedByNationality = coachController.sortAllCoachesByNationality(country);
                    if (allCoachesSortedByNationality == null)
                        System.out.println("There are no coaches from this nationality");
                    else {
                        for (Coach coach : allCoachesSortedByNationality)
                            coach.printCoach();
                    }
                    this.coachDBMenu();
                    break;
                case 6:
                    List<Coach> allCoachesSortedByName = coachController.sortAllCoachesByName();
                    if (allCoachesSortedByName == null)
                        System.out.println("There are no coaches");
                    else {
                        for (Coach coach : allCoachesSortedByName)
                            coach.printCoach();
                    }
                    this.coachDBMenu();
                    break;
                case 7:
                    this.dataBasesMenu();
                case 8:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.coachDBMenu();
        }
    }

    private void teamsDBMenu() throws SQLException {
        System.out.println("""
                You have the following options. Please choose a number between 1 and 8:
                1. List all teams
                2. Sort teams by market value
                3. Sort teams by name
                4. Filter teams by country
                5. Sort teams by foundation year
                6. Sort by budget
                7. Go back
                8. Exit
                """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 9)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 8!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1:
                    teamController.printAllTeams();
                    this.teamsDBMenu();
                    break;
                case 2:
                    for (Team team : teamRepositoryJDBC.getAllTeams())
                        for(Player player : team.squad)
                            if(!Objects.equals(team.getName(), "Free"))
                                team.setSquadValue(team.getSquadValue()+player.getMarketValue());
                    teamController.sortAllTeamsByValue();
                    List<Team> allTeamsSortedByValue = teamController.sortAllTeamsByValue();
                    if (allTeamsSortedByValue == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : allTeamsSortedByValue)
                            if(!(team.getName().equals("Free")))
                                team.printTeam();
                    }
                    this.teamsDBMenu();
                    break;
                case 3:
                    teamController.sortAllTeamsByName();
                    List<Team> allTeamsSortedByName = teamController.sortAllTeamsByName();
                    if (allTeamsSortedByName == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : allTeamsSortedByName)
                            if(!(team.getName().equals("Free")))
                                team.printTeam();
                    }
                    this.teamsDBMenu();
                    break;
                case 4:
                    System.out.println("Please choose the country ");
                    this.userInput.nextLine();
                    String country = userInput.nextLine();
                    List<Team> teamsFromCountry = teamController.sortAllTeamsByCountry(country);
                    if (teamsFromCountry.size() == 0)
                        System.out.println("There are no teams from this country");
                    else
                        for (Team team : teamsFromCountry)
                            if(!(team.getName().equals("Free")))
                                team.printTeam();
                    this.teamsDBMenu();
                    break;
                case 5:
                    List<Team> sortedByYear = teamController.sortAllTeamsByFoundationYear();
                    if (sortedByYear == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : sortedByYear)
                            if(!(team.getName().equals("Free")))
                                team.printTeam();
                    }
                    this.teamsDBMenu();
                    break;
                case 6:
                    List<Team> sortedByBudget = teamController.sortAllTeamsByBudget();
                    if (sortedByBudget == null)
                        System.out.println("There are no teams");
                    else {
                        for (Team team : sortedByBudget)
                            if(!(team.getName().equals("Free")))
                                team.printTeam();
                    }
                    this.teamsDBMenu();
                    break;
                case 7:
                    this.dataBasesMenu();
                case 8:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.teamsDBMenu();
        }
    }

    private void sponsorDBMenu() throws SQLException {
        System.out.println("""
                You have the following options. Please choose a number between 1 and 7:
                1. List all sponsors
                2. Sort sponsors by name
                3. Sort sponsors by networth
                4. See all the teams with the same sponsor
                5. See all sponsors from a team
                6. Go back
                7. Exit
                """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 8)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 7!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1:
                    sponsorController.printAllSponsors();
                    this.sponsorDBMenu();
                    break;
                case 2:
                    List<Sponsor> sponsorsSortedByName = sponsorController.sortAllSponsorsByName();
                    if (sponsorsSortedByName == null)
                        System.out.println("There are no sponsors");
                    else {
                        for (Sponsor sponsor : sponsorsSortedByName)
                            sponsor.printSponsor();
                    }
                    this.sponsorDBMenu();
                    break;
                case 3:
                    List<Sponsor> sponsorsSortedByNetworth = sponsorController.sortAllSponsorsByNetWorth();
                    if (sponsorsSortedByNetworth == null)
                        System.out.println("There are no sponsors");
                    else {
                        for (Sponsor sponsor : sponsorsSortedByNetworth)
                            sponsor.printSponsor();
                    }
                    this.sponsorDBMenu();
                    break;
                case 4:
                    System.out.println("Tell us the sponsor's name:");
                    this.userInput.nextLine();
                    String sponsor = this.userInput.nextLine();
                    System.out.println("Sponsor abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (sponsorRepositoryJDBC.findById(sponsor, abbreviation) != null) {
                        List<Team> sameSponsorTeams = teamController.getAllTeamsAffiliatedWithSponsor(sponsorRepositoryJDBC.findById(sponsor, abbreviation));
                        if (sameSponsorTeams == null)
                            System.out.println("There are no Teams");
                        else {
                            for (Team team : sameSponsorTeams)
                                team.printTeam();
                        }
                    } else {
                        System.out.println("There is no such sponsor.");
                    }
                    this.sponsorDBMenu();
                    break;
                case 5:
                    System.out.println("Tell us the team's name:");
                    this.userInput.nextLine();
                    String team = this.userInput.nextLine();
                    System.out.println("Team abbreviation: ");
                    String abbreviation1 = this.userInput.nextLine();
                    if (teamRepositoryJDBC.findById(team, abbreviation1) != null) {
                        List<Sponsor> allSponsorsFromAteam = sponsorController.allSponsorsFromATeam(teamRepositoryJDBC.findById(team, abbreviation1));
                        if (allSponsorsFromAteam.size() == 0)
                            System.out.println("There are no Sponsors");
                        else {
                            for (Sponsor sponsor1 : allSponsorsFromAteam)
                                sponsor1.printSponsor();
                        }
                    } else {
                        System.out.println("There is no such team.");
                    }
                    this.sponsorDBMenu();
                    break;
                case 6:
                    this.dataBasesMenu();
                case 7:
                    System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.sponsorDBMenu();
        }
    }


    private void adminDBsMenu() throws SQLException {
        System.out.println("""
                Access one of the following databases(please choose a number from 1 to 6):
                1. Players
                2. Coaches
                3. Teams
                4. Sponsors
                5. Go back
                6. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("You are currently in the Players Database.");
                    this.adminPlayerDBMenu();
                }
                case 2 -> {
                    System.out.println("You are currently in the Coaches Database.");
                    this.adminCoachDBMenu();
                }
                case 3 -> {
                    System.out.println("You are currently in the Teams Database.");
                    this.adminTeamsDBMenu();
                }
                case 4 -> {
                    System.out.println("You are currently in the Sponsors Database.");
                    this.adminSponsorDBMenu();
                }
                case 5 -> {
                    this.userInput.nextLine();
                    this.loginMenu();
                }

                case 6 -> System.exit(0);


            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.adminDBsMenu();
        }
    }


    private void adminPlayerDBMenu() throws SQLException, RuntimeException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 6):
                1. Create players
                2. Search players
                3. Update players
                4. Delete players
                5. Go back
                6. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Tell us the details");
                    this.userInput.nextLine();
                    System.out.println("ID:");
                    String id = this.userInput.nextLine();
                    try
                    {
                        for (Player player : playerRepositoryJDBC.getAllPlayers())
                        {
                            if(player.getId().equals(id))
                                throw new AlreadyPlayerException();
                        }
                    } catch (AlreadyPlayerException alreadyPlayerException)
                    {
                        System.out.println(alreadyPlayerException.getMessage());
                        this.userInput.nextLine();
                        this.adminPlayerDBMenu();
                    }
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.adminPlayerDBMenu();
                    }

                    System.out.println("Last Name: ");
                    String lastName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(lastName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException2) {
                        System.out.println(noDigitsInPersonNameException2.getMessage());
                        this.userInput.nextLine();
                        this.adminPlayerDBMenu();
                    }

                    System.out.println("Age: ");
                    try {
                        int age = Integer.parseInt(this.userInput.nextLine());
                        if (age < 0)
                            throw new ImproperIntegerException();

                        System.out.println("Nationality: ");
                        String nationality = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(nationality)) {
                                throw new NoDigitsInNationalityException();
                            }
                        } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                            System.out.println(noDigitsInNationalityException.getMessage());
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }

                        System.out.println("Position: ");
                        String position = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(position)) {
                                throw new NoDigitsInPositionException();
                            }
                        } catch (NoDigitsInPositionException noDigitsInPositionException) {
                            System.out.println(noDigitsInPositionException.getMessage());
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }
                        System.out.println("Market value: ");
                        try {
                            int marketValue = Integer.parseInt(this.userInput.nextLine());
                            if (marketValue < 0)
                                throw new ImproperIntegerException();

                            Player newPlayer = new Player(id, firstName, lastName, age, nationality, position, marketValue);
                            if (!playerRepositoryJDBC.existsPlayerForAdmin(id, firstName))
                                this.playerRepositoryJDBC.add(newPlayer);
                            else System.out.println("Player is already there");
                            this.adminPlayerDBMenu();
                        } catch (ImproperIntegerException | NumberFormatException marketValueException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.adminPlayerDBMenu();
                    }
                }
                case 2 -> {
                    System.out.println("Who are you looking for?");
                    this.userInput.nextLine();
                    System.out.println("ID :");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    if (playerRepositoryJDBC.existsPlayerForAdmin(id, firstName))
                        playerRepositoryJDBC.findByIdForAdmin(id, firstName).printPlayer();
                    else {
                        System.out.println("Player does not exist");
                    }
                    this.adminPlayerDBMenu();
                }
                case 3 -> {
                    System.out.println("Who are you looking for?");
                    this.userInput.nextLine();
                    System.out.println("ID :");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    if (playerRepositoryJDBC.findByIdForAdmin(id, firstName) != null) {
                        playerRepositoryJDBC.findByIdForAdmin(id, firstName).printPlayer();
                        System.out.println("""
                                Please enter the new details:
                                (You don't have to change everything)
                                Press ENTER to start
                                """);
                        this.userInput.nextLine();
                        System.out.println("ID:");
                        String newID = this.userInput.nextLine();
                        System.out.println("First Name:");
                        String newFirstName = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(newFirstName)) {
                                throw new NoDigitsInPersonNameException();
                            }
                        } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                            System.out.println(noDigitsInPersonNameException.getMessage());
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }
                        System.out.println("Last Name:");
                        String newLastName = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(newLastName)) {
                                throw new NoDigitsInPersonNameException();
                            }
                        } catch (NoDigitsInPersonNameException noDigitsInPersonNameException2) {
                            System.out.println(noDigitsInPersonNameException2.getMessage());
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }
                        System.out.println("Age: ");
                        try {
                            int newAge = Integer.parseInt(this.userInput.nextLine());
                            if (newAge < 0)
                                throw new ImproperIntegerException();

                            System.out.println("Nationality: ");
                            String newNationality = this.userInput.nextLine();
                            try {
                                if (playerController.checkString(newNationality)) {
                                    throw new NoDigitsInNationalityException();
                                }
                            } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                                System.out.println(noDigitsInNationalityException.getMessage());
                                this.userInput.nextLine();
                                this.adminPlayerDBMenu();
                            }

                            System.out.println("Position: ");
                            String newPosition = this.userInput.nextLine();
                            try {
                                if (playerController.checkString(newPosition)) {
                                    throw new NoDigitsInPositionException();
                                }
                            } catch (NoDigitsInPositionException noDigitsInPositionException) {
                                System.out.println(noDigitsInPositionException.getMessage());
                                this.userInput.nextLine();
                                this.adminPlayerDBMenu();
                            }
                            System.out.println("Market Value ");
                            try {
                                int newMarketValue = Integer.parseInt(this.userInput.nextLine());
                                if (newMarketValue < 0)
                                    throw new ImproperIntegerException();
                                Player player = new Player(newID, newFirstName, newLastName, newAge, newNationality, newPosition, newMarketValue);
                                playerRepositoryJDBC.update(id, firstName, player);
                            } catch (ImproperIntegerException | NumberFormatException marketValueException) {
                                System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                                this.userInput.nextLine();
                                this.adminPlayerDBMenu();
                            }

                        } catch (ImproperIntegerException | NumberFormatException marketValueException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminPlayerDBMenu();
                        }


                    } else
                        System.out.println("Player does not exist");
                    this.adminPlayerDBMenu();
                }

                case 4 -> {
                    System.out.println("Who are you looking for?");
                    this.userInput.nextLine();
                    System.out.println("ID: ");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    if (playerRepositoryJDBC.findByIdForAdmin(id, firstName) != null) {
                        playerRepositoryJDBC.remove(id, firstName);
                        System.out.println("Player has been removed");
                    } else {
                        System.out.println("Player does not exist");
                    }
                    this.adminPlayerDBMenu();
                }
                case 5 -> adminDBsMenu();
                case 6 -> System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.adminPlayerDBMenu();
        }
    }

    private void adminCoachDBMenu() throws SQLException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 6):
                1. Create coaches
                2. Search coaches
                3. Update coaches
                4. Delete coaches
                5. Go back
                6. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Tell us the details:");
                    this.userInput.nextLine();
                    System.out.println("ID:");
                    String id = this.userInput.nextLine();
                    try
                    {
                        for(Coach coach : coachRepositoryJDBC.getAllCoaches())
                            if(coach.getId().equals(id))
                                throw new AlreadyCoachException();
                    } catch (AlreadyCoachException alreadyCoachException)
                    {
                        System.out.println(alreadyCoachException.getMessage());
                        this.userInput.nextLine();
                        this.adminCoachDBMenu();
                    }
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(firstName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.adminCoachDBMenu();
                    }
                    System.out.println("Last Name: ");
                    String lastName = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(lastName)) {
                            throw new NoDigitsInPersonNameException();
                        }
                    } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                        System.out.println(noDigitsInPersonNameException.getMessage());
                        this.userInput.nextLine();
                        this.adminCoachDBMenu();
                    }
                    System.out.println("Age: ");
                    try {
                        int age = Integer.parseInt(this.userInput.nextLine());
                        if (age < 0)
                            throw new ImproperIntegerException();

                        System.out.println("Nationality: ");
                        String nationality = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(nationality)) {
                                throw new NoDigitsInNationalityException();
                            }
                        } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                            System.out.println(noDigitsInNationalityException.getMessage());
                            this.userInput.nextLine();
                            this.adminCoachDBMenu();
                        }

                        System.out.println("Playstyle: ");
                        String playstyle = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(playstyle))
                                throw new NoDigitsInPlaystyleException();
                        } catch (NoDigitsInPlaystyleException noDigitsInPlaystyleException) {
                            System.out.println(noDigitsInPlaystyleException.getMessage());
                            this.userInput.nextLine();
                            this.adminCoachDBMenu();
                        }

                        System.out.println("Your current team: ");
                        String myTeam = this.userInput.nextLine();
                        int counter = 0;
                        for (Team team : teamRepositoryJDBC.getAllTeams())
                            if (team.getAbbreviation().contains(myTeam))
                                counter++;
                        if (counter == 0) {
                            System.out.println("There is no team with such an abbreviation in our database");
                        } else {
                            Team newTeam = null;
                            for (Team team : teamRepositoryJDBC.getAllTeams())
                                if (team.getAbbreviation().contains(myTeam))
                                    newTeam = team;

                            Coach newCoach = new Coach(id, firstName, lastName, age, nationality, playstyle, newTeam);
                            if (!this.coachRepositoryJDBC.existsCoach(newCoach.getFirstName(), newCoach.getLastName()))
                                coachRepositoryJDBC.add(newCoach);
                            else
                                System.out.println("Coach already exists");
                        }
                        this.adminCoachDBMenu();
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.adminCoachDBMenu();
                    }

                }
                case 2 -> {
                    System.out.println("Tell us his details:");
                    this.userInput.nextLine();
                    System.out.println("ID : ");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    if (coachRepositoryJDBC.existsCoachForAdmin(id, firstName))
                        coachRepositoryJDBC.findByIdForAdmin(id, firstName).printCoach();
                    else {
                        System.out.println("Coach does not exist");
                    }
                    this.adminCoachDBMenu();
                }
                case 3 -> {
                    System.out.println("Tell us his details:");
                    this.userInput.nextLine();
                    System.out.println("ID:");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
//                System.out.println("Last Name: ");
//                String lastName = this.userInput.nextLine();
                    if (coachRepositoryJDBC.findByIdForAdmin(id, firstName) != null) {
                        System.out.println("Tell us the new details:");
                        this.userInput.nextLine();
                        System.out.println("ID : ");
                        String newID = this.userInput.nextLine();
                        System.out.println("First Name: ");
                        String newFirstName = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(newFirstName)) {
                                throw new NoDigitsInPersonNameException();
                            }
                        } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                            System.out.println(noDigitsInPersonNameException.getMessage());
                            this.userInput.nextLine();
                            this.adminCoachDBMenu();
                        }
                        System.out.println("Last Name: ");
                        String newLastName = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(newLastName)) {
                                throw new NoDigitsInPersonNameException();
                            }
                        } catch (NoDigitsInPersonNameException noDigitsInPersonNameException) {
                            System.out.println(noDigitsInPersonNameException.getMessage());
                            this.userInput.nextLine();
                            this.adminCoachDBMenu();
                        }
                        System.out.println("Age: ");
                        try {
                            int newAge = Integer.parseInt(this.userInput.nextLine());
                            if (newAge < 0)
                                throw new ImproperIntegerException();

                            System.out.println("Nationality: ");
                            String newNationality = this.userInput.nextLine();
                            try {
                                if (playerController.checkString(newNationality)) {
                                    throw new NoDigitsInNationalityException();
                                }
                            } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                                System.out.println(noDigitsInNationalityException.getMessage());
                                this.userInput.nextLine();
                                this.adminCoachDBMenu();
                            }

                            System.out.println("Playstyle: ");
                            String newPlaystyle = this.userInput.nextLine();
                            try {
                                if (playerController.checkString(newPlaystyle))
                                    throw new NoDigitsInPlaystyleException();
                            } catch (NoDigitsInPlaystyleException noDigitsInPlaystyleException) {
                                System.out.println(noDigitsInPlaystyleException.getMessage());
                                this.userInput.nextLine();
                                this.adminCoachDBMenu();
                            }
                            System.out.println("Your current team: ");
                            String myTeam = this.userInput.nextLine();
                            int counter = 0;
                            for (Team team : teamRepositoryJDBC.getAllTeams())
                                if (team.getAbbreviation().contains(myTeam))
                                    counter++;
                            if (counter == 0) {
                                System.out.println("There is no team with such an abbreviation in our database");
                                this.adminCoachDBMenu();
                            } else {
                                Team newTeam = null;
                                for (Team team : teamRepositoryJDBC.getAllTeams())
                                    if (team.getAbbreviation().contains(myTeam))
                                        newTeam = team;

                                Coach newCoach = new Coach(newID, newFirstName, newLastName, newAge, newNationality, newPlaystyle, newTeam);
                                coachRepositoryJDBC.update(id, firstName, newCoach);
                            }
                        } catch (ImproperIntegerException | NumberFormatException ageException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminCoachDBMenu();
                        }


                    } else
                        System.out.println("Coach was not found!");
                    this.adminCoachDBMenu();
                }
                case 4 -> {
                    System.out.println("Tell us his details:");
                    this.userInput.nextLine();
                    System.out.println("ID : ");
                    String id = this.userInput.nextLine();
                    System.out.println("First Name: ");
                    String firstName = this.userInput.nextLine();
                    if (this.coachRepositoryJDBC.findByIdForAdmin(id, firstName) != null)
                        this.coachRepositoryJDBC.remove(id, firstName);
                    else
                        System.out.println("Coach was not found!");
                    this.adminCoachDBMenu();
                }
                case 5 -> adminDBsMenu();
                case 6 -> System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.adminCoachDBMenu();
        }
    }

    private void adminTeamsDBMenu() throws SQLException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 6):
                1. Create teams
                2. Search teams
                3. Update teams
                4. Delete teams
                5. Go back
                6. Exit""");
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("Please tell us the details of the new team: ");
                    this.userInput.nextLine();
                    System.out.println("Team name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Team abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    try
                    {
                        for(Team team : teamRepositoryJDBC.getAllTeams())
                            if (team.getAbbreviation().equals(abbreviation))
                                throw new AlreadyTeamException();

                    } catch (AlreadyTeamException alreadyTeamException)
                    {
                        System.out.println(alreadyTeamException.getMessage());
                        this.userInput.nextLine();
                        this.adminTeamsDBMenu();
                    }
                    System.out.println("Country: ");
                    String country = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(country))
                            throw new NoDigitsInNationalityException();
                    } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                        System.out.println(noDigitsInNationalityException.getMessage());
                        this.userInput.nextLine();
                        this.adminTeamsDBMenu();
                    }
                    System.out.println("Town: ");
                    String town = this.userInput.nextLine();
                    try {
                        if (playerController.checkString(town))
                            throw new NoDigitsInCityException();
                    } catch (NoDigitsInCityException noDigitsInCityException) {
                        System.out.println(noDigitsInCityException.getMessage());
                        this.userInput.nextLine();
                        this.adminTeamsDBMenu();
                    }
                    System.out.println("Foundation Year: ");
                    try {
                        int foundationYear = Integer.parseInt(this.userInput.nextLine());
                        if (foundationYear < 0)
                            throw new ImproperIntegerException();

                        System.out.println("Squad capacity: ");
                        try {
                            int maxSquadSize = Integer.parseInt(this.userInput.nextLine());
                            if (maxSquadSize < 0)
                                throw new ImproperIntegerException();

                            System.out.println("Budget: ");
                            try {
                                int budget = Integer.parseInt(this.userInput.nextLine());
                                if (budget < 0)
                                    throw new ImproperIntegerException();
                                Team newTeam = new Team(name, abbreviation, country, town, foundationYear, maxSquadSize, budget);
                                if (this.teamRepositoryJDBC.findById(newTeam.getName(), newTeam.getAbbreviation()) == null)
                                    this.teamRepositoryJDBC.add(newTeam);
                                else
                                    System.out.println("Team already exists");
                                this.adminTeamsDBMenu();
                            } catch (ImproperIntegerException | NumberFormatException ageException) {
                                System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                                this.userInput.nextLine();
                                this.adminTeamsDBMenu();
                            }
                        } catch (ImproperIntegerException | NumberFormatException ageException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminTeamsDBMenu();
                        }

                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.adminTeamsDBMenu();
                    }

                }
                case 2 -> {
                    System.out.println("Tell us the details of the team: ");
                    this.userInput.nextLine();
                    System.out.println("Team name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (teamRepositoryJDBC.findById(name, abbreviation) != null)
                        teamRepositoryJDBC.findById(name, abbreviation).printTeam();
                    else
                        System.out.println("This team does not exist!");
                    this.adminTeamsDBMenu();
                }
                case 3 -> {
                    System.out.println("Tell us the details of the team: ");
                    this.userInput.nextLine();
                    System.out.println("Team name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (teamRepositoryJDBC.findById(name, abbreviation) != null) {
                        System.out.println("Please tell us the new details of the team: ");
                        System.out.println("(You don't have to change everything)");
                        this.userInput.nextLine();
                        System.out.println("Team name: ");
                        String newName = this.userInput.nextLine();
                        System.out.println("Team abbreviation: ");
                        String newAbbreviation = this.userInput.nextLine();
                        System.out.println("Country: ");
                        String country = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(country))
                                throw new NoDigitsInNationalityException();
                        } catch (NoDigitsInNationalityException noDigitsInNationalityException) {
                            System.out.println(noDigitsInNationalityException.getMessage());
                            this.userInput.nextLine();
                            this.adminTeamsDBMenu();
                        }

                        System.out.println("Town: ");
                        String town = this.userInput.nextLine();
                        try {
                            if (playerController.checkString(town))
                                throw new NoDigitsInCityException();
                        } catch (NoDigitsInCityException noDigitsInCityException) {
                            System.out.println(noDigitsInCityException.getMessage());
                            this.userInput.nextLine();
                            this.adminTeamsDBMenu();
                        }

                        System.out.println("Foundation Year: ");
                        try {
                            int foundationYear = Integer.parseInt(this.userInput.nextLine());
                            if (foundationYear < 0)
                                throw new ImproperIntegerException();

                            System.out.println("Squad capacity: ");
                            try {
                                int maxSquadSize = Integer.parseInt(this.userInput.nextLine());
                                if (maxSquadSize < 0)
                                    throw new ImproperIntegerException();

                                System.out.println("Budget: ");
                                try {
                                    int budget = Integer.parseInt(this.userInput.nextLine());
                                    if (budget < 0)
                                        throw new ImproperIntegerException();
                                    Team updatedTeam = new Team(newName, newAbbreviation, country, town, foundationYear, maxSquadSize, budget);
                                    teamRepositoryJDBC.update(name, abbreviation, updatedTeam);
                                } catch (ImproperIntegerException | NumberFormatException ageException) {
                                    System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                                    this.userInput.nextLine();
                                    this.adminTeamsDBMenu();
                                }
                            } catch (ImproperIntegerException | NumberFormatException ageException) {
                                System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                                this.userInput.nextLine();
                                this.adminTeamsDBMenu();
                            }

                        } catch (ImproperIntegerException | NumberFormatException ageException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminTeamsDBMenu();
                        }
                    } else {
                        System.out.println("Team  was not found!");
                    }
                    this.adminTeamsDBMenu();
                }
                case 4 -> {
                    Team trial = new Team("Free", "Free", "Free", "Free", 0, 100, 0);
                    System.out.println("Tell us the details of the team: ");
                    this.userInput.nextLine();
                    System.out.println("Team name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (teamRepositoryJDBC.findById(name, abbreviation) != null) {
                        teamRepositoryJDBC.findById(name, abbreviation).disbandTeam();
                        for (Coach coach : coachRepositoryJDBC.getAllCoaches()) {
                            if (coach.getTeam().getAbbreviation().equals(abbreviation))
                                coach.setTeam(trial);
                        }
                        teamRepositoryJDBC.remove(name, abbreviation);
                    } else
                        System.out.println("Team was not found!");
                    this.adminTeamsDBMenu();


                }
                case 5 -> adminDBsMenu();
                case 6 -> System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.adminTeamsDBMenu();
        }
    }

    private void adminSponsorDBMenu() throws SQLException {
        System.out.println("""
                You have the following options(please choose a number from 1 to 6):
                1. Create sponsors
                2. Search sponsors
                3. Update sponsors
                4. Delete sponsors
                5. Go back
                6. Exit
                 """);
        try {
            int choice = this.userInput.nextInt();
            boolean validator = false;

            while (!validator) {
                if (choice > 0 && choice < 7)
                    validator = true;
                else {
                    System.out.println("Please choose a number between 1 and 6!");
                    choice = userInput.nextInt();
                }
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("Tell us the firm's details:");
                    this.userInput.nextLine();
                    System.out.println("Name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    try
                    {
                        for(Sponsor sponsor : sponsorRepositoryJDBC.getAllSponsors())
                            if(sponsor.getAbbreviation().equals(abbreviation))
                                throw new AlreadySponsorException();
                    } catch (AlreadySponsorException alreadySponsorException)
                    {
                        System.out.println(alreadySponsorException.getMessage());
                        this.userInput.nextLine();
                        this.adminSponsorDBMenu();
                    }
                    System.out.println("Net worth: ");
                    try {
                        int netWorth = Integer.parseInt(this.userInput.nextLine());
                        if (netWorth < 0)
                            throw new ImproperIntegerException();
                        Sponsor newSponsor = new Sponsor(name, abbreviation, netWorth);
                        if (!this.sponsorRepositoryJDBC.existsSponsor(name, abbreviation))
                            sponsorRepositoryJDBC.add(newSponsor);
                        else
                            System.out.println("Sponsor already exists");
                        this.adminSponsorDBMenu();
                    } catch (ImproperIntegerException | NumberFormatException ageException) {
                        System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                        this.userInput.nextLine();
                        this.adminTeamsDBMenu();
                    }

                }
                case 2 -> {
                    System.out.println("Tell us the firm's details:");
                    this.userInput.nextLine();
                    System.out.println("Name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (this.sponsorRepositoryJDBC.existsSponsor(name, abbreviation))
                        this.sponsorRepositoryJDBC.findById(name, abbreviation).printSponsor();
                    else
                        System.out.println("Sponsor does not exist");
                    this.adminSponsorDBMenu();
                }
                case 3 -> {
                    System.out.println("Tell us the firm's details:");
                    this.userInput.nextLine();
                    System.out.println("Name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (this.sponsorRepositoryJDBC.findById(name, abbreviation) != null) {
                        System.out.println("Tell us the new details of the firm:");
                        this.userInput.nextLine();
                        System.out.println("Name: ");
                        String newName = this.userInput.nextLine();
                        System.out.println("Abbreviation: ");
                        String newAbbreviation = this.userInput.nextLine();
                        System.out.println("Net worth: ");
                        try {
                            int newNetWorth = Integer.parseInt(this.userInput.nextLine());
                            if (newNetWorth < 0)
                                throw new ImproperIntegerException();
                            Sponsor updatedSponsor = new Sponsor(newName, newAbbreviation, newNetWorth);
                            if (sponsorRepositoryJDBC.findById(name, abbreviation) != null) {
                                sponsorRepositoryJDBC.update(name, abbreviation, updatedSponsor);
                            }
                        } catch (ImproperIntegerException | NumberFormatException ageException) {
                            System.out.println("Please provide a positive Integer. Any strings or negative numbers are not accepted! Press anything to go back");
                            this.userInput.nextLine();
                            this.adminTeamsDBMenu();
                        }
                    } else
                        System.out.println("Sponsor was not found!");
                    this.adminSponsorDBMenu();
                }

                case 4 -> {
                    System.out.println("Tell us the firm's details:");
                    this.userInput.nextLine();
                    System.out.println("Name: ");
                    String name = this.userInput.nextLine();
                    System.out.println("Abbreviation: ");
                    String abbreviation = this.userInput.nextLine();
                    if (this.sponsorRepositoryJDBC.findById(name, abbreviation) != null) {
                        this.sponsorRepositoryJDBC.findById(name, abbreviation).disbandSponsor();
                        this.sponsorRepositoryJDBC.remove(name, abbreviation);
                    } else
                        System.out.println("Sponsor was not found!");
                    this.adminSponsorDBMenu();
                }
                case 5 -> adminDBsMenu();
                case 6 -> System.exit(0);
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Please enter a number. Press anything to go back");
            this.userInput.nextLine();
            this.adminSponsorDBMenu();
        }
    }
}