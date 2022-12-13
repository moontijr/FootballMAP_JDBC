package repository.jdbc;


import model.Coach;
import model.Team;
import repository.CoachRepository;

import java.sql.*;
import java.util.ArrayList;


public class CoachRepositoryJDBC implements CoachRepository {
    //private Connection connection;


    private static CoachRepositoryJDBC single_instance = null;

    private final ArrayList<Coach> allCoaches = new ArrayList<>();

    public static CoachRepositoryJDBC getInstance()
    {
        if(single_instance == null) {
            single_instance = new CoachRepositoryJDBC();
            String connectionURL = "jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true";
            try {
                System.out.print("Connecting to the server......");
                try (Connection connection = DriverManager.getConnection(connectionURL)) {
                    System.out.println("Connected to the Server.");

                    Statement select = connection.createStatement();
                    ResultSet resultSet = select.executeQuery("SELECT * FROM CoachMAP");
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if (count > 0) {
                        ResultSet result = select.executeQuery(" SELECT * FROM CoachMAP");
                        while (result.next()) {
                            String abbreviationTeam = result.getString("currentTeam");
                            for (Team team : TeamRepositoryJDBC.getInstance().getAllTeams()) {
                                if (team.getAbbreviation().equals(abbreviationTeam)) {
                                    Team team1 = new Team(team.getName(), team.getAbbreviation(), team.getCountry(), team.getTown(), team.getFoundationYear(), team.getMaxSquadSize(), team.getBudget());
                                    Coach coach = new Coach(result.getString("id"), result.getString("firstName"), result.getString("lastName"), result.getInt("age"), result.getString("nationality"), result.getString("playStyle"), team1);
                                    CoachRepositoryJDBC.getInstance().getAllCoaches().add(coach);
                                }
                            }

                        }
                    } else {
                        String insert_string = "INSERT INTO CoachMAP(id, firstName, lastName, age, nationality, playStyle, currentTeam) VALUES ('MS01','Marius', 'Sumudica', 58, 'Romania', 'Defensive','U')";
                        Statement insert = connection.createStatement();
                        insert.executeUpdate(insert_string);
                        Coach coach = new Coach("MS01", "Marius", "Sumudica", 58, "Romania", "Defensive", TeamRepositoryJDBC.getInstance().getTeamByAbbreviation("U"));
                        CoachRepositoryJDBC.getInstance().getAllCoaches().add(coach);
                    }
                    //System.out.println(allPlayers.size());

                } catch (Exception e) {
                    System.out.println("I am not connected to the Server");
                    e.printStackTrace();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return single_instance;
    }


    /**
     * when we initialize a coachRepo with jdbc, we need first to get the connection to the database, and then check if
     * there are already datas in our sql table, and if not, we gotta populate them firstly with some values that are hard-coded
     */


    public ArrayList<Coach> getAllCoaches() {
        return allCoaches;
    }


    /**
     * we add a coach into our database, and also in our list that we currently work with
     * @param entity of a coach that will be added
     * @throws SQLException because we work with sql tables and database
     */
    @Override
    public void add(Coach entity) throws SQLException {
        allCoaches.add(entity);
        String id=entity.getId();
        String firstName=entity.getFirstName();
        String lastName=entity.getLastName();
        int age = entity.getAge();
        String nationality= entity.getNationality();
        String playStyle=entity.getPlayStyle();
        Team currentTeam=entity.getTeam();
        String currentTeamToString=currentTeam.getAbbreviation();
        Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
        String sql= "INSERT INTO CoachMAP (id,firstName,lastName,age,nationality,playStyle,currentTeam) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=connection1.prepareStatement(sql);
        preparedStatement.setObject(1,id);
        preparedStatement.setObject(2,firstName);
        preparedStatement.setObject(3,lastName);
        preparedStatement.setObject(4,age);
        preparedStatement.setObject(5,nationality);
        preparedStatement.setObject(6,playStyle);
        preparedStatement.setObject(7,currentTeamToString);
        preparedStatement.execute();
        preparedStatement.close();
        connection1.close();


    }

    /**
     * the function removes also the coach from the list, and also from the sql table ( removes by searching on id and name)
     * @param s is the id
     * @param s1 is the name
     * @throws SQLException because we use sql tables and database
     */
    public void remove(String s, String s1) throws SQLException {
        if (findByIdForAdmin(s,s1) != null) {
            String id= findByIdForAdmin(s,s1).getId();
            String firstName=findByIdForAdmin(s,s1).getFirstName();
            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "DELETE FROM CoachMAP WHERE id=? AND firstName=?";
            PreparedStatement statement=connection1.prepareStatement(sql);
            statement.setString(1,id);
            statement.setString(2,firstName);
            statement.executeUpdate();
            statement.close();
            connection1.close();
            this.allCoaches.remove(findByIdForAdmin(s, s1));

        }
    }

    /**
     * when we update a player, we set his new "details" with the help from a newEntity. We also update the player in the list
     * and in the sql table/database
     * @param s is the id
     * @param s1 is the name
     * @param newEntity is gonna help us to set the new values of the coach we want to update
     * @throws SQLException because we work with sql tables and database
     */
    public void update(String s, String s1, Coach newEntity) throws SQLException {
        if(findByIdForAdmin(s,s1)!=null)
        {
            String id = findByIdForAdmin(s,s1).getId();
            String newId=newEntity.getId();
            String firstName= findByIdForAdmin(s,s1).getFirstName();
            String newFirstName=newEntity.getFirstName();
            String newLastName=newEntity.getLastName();
            int newAge=newEntity.getAge();
            String newNationality=newEntity.getNationality();
            String newPlaystyle=newEntity.getPlayStyle();
            Team currentTeam= TeamRepositoryJDBC.getInstance().getTeamByAbbreviation(newEntity.getTeam().getAbbreviation());
            String currentTeamToString=currentTeam.getAbbreviation();

            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");

            String sql= "UPDATE CoachMAP SET id=?, firstName=?, lastName=?, age=?, nationality=?, playStyle=?, currentTeam=? WHERE id=? AND firstName=?";
            PreparedStatement preparedStatement= connection1.prepareStatement(sql);

            preparedStatement.setString(1,newId);
            preparedStatement.setString(2,newFirstName);
            preparedStatement.setString(3,newLastName);
            preparedStatement.setInt(4,newAge);
            preparedStatement.setString(5,newNationality);
            preparedStatement.setString(6,newPlaystyle);
            preparedStatement.setString(7,currentTeamToString);
            preparedStatement.setString(8,id);
            preparedStatement.setString(9,firstName);
            preparedStatement.executeUpdate();




            this.findByIdForAdmin(s, s1).setId(newEntity.getId());
            this.findByIdForAdmin(newEntity.getId(), s1).setFirstName(newEntity.getFirstName());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setLastName(newEntity.getLastName());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setAge(newEntity.getAge());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setNationality(newEntity.getNationality());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setPlayStyle(newEntity.getPlayStyle());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setTeam(newEntity.getTeam());

            preparedStatement.close();
            connection1.close();
        }

    }




    /**
     * searches for a player by his first and last name
     * @param s is the firstName
     * @param id2 is the lastName
     * @return A Coach Object
     */
    @Override
    public Coach findById(String s, String id2) {
        for (Coach coach : allCoaches)
            if (s.equals(coach.getFirstName()) && id2.equals(coach.getLastName()))
                return coach;
        return null;
    }

    /**
     * the admin needs to search for the player with the help of the id and the firstName
     * @param s is the id
     * @param s1 is the first name
     * @return a Coach Object
     */
    public Coach findByIdForAdmin(String s, String s1) {
        for (Coach coach : allCoaches)
            if (s.equals(coach.getId()) && s1.equals(coach.getFirstName()))
                return coach;
        return null;
    }


    /**
     * is the same operation as findById that returns a boolean
     * @param firstName is the first name
     * @param secondName is the second name
     * @return true if the coach exists, false if not
     */
    public boolean existsCoach(String firstName, String secondName) {
        for (Coach coach : this.allCoaches)
            if (coach.getFirstName().equals(firstName) && coach.getLastName().equals(secondName))
                return true;
        return false;
    }

    /**
     * is the same operation as findByIdForAdmin that returns a boolean
     * @param id is the id
     * @param firstName is the firstName
     * @return true if that coach with the given id/first name exists and false if not
     */
    public boolean existsCoachForAdmin(String id, String firstName) {
        for (Coach coach : this.allCoaches)
            if (coach.getId().equals(id) && coach.getFirstName().equals(firstName))
                return true;
        return false;
    }


}