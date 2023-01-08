package repository.jdbc;

import model.Team;
import repository.TeamRepository;

import java.sql.*;
import java.util.ArrayList;

public class TeamRepositoryJDBC implements TeamRepository {

    private static TeamRepositoryJDBC single_instance=null;
    public ArrayList<Team> getAllTeams() {
        return allTeams;
    }


    private  final ArrayList<Team> allTeams = new ArrayList<>();

    public static TeamRepositoryJDBC getInstance()
    {
        if(single_instance==null)
        {
            single_instance=new TeamRepositoryJDBC();
            String connectionURL = "jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true";
            try {
                try (Connection connection = DriverManager.getConnection(connectionURL)) {

                    Statement select = connection.createStatement();
                    ResultSet resultSet = select.executeQuery("SELECT * FROM TeamMAP");
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if (count > 0) {
                        ResultSet result = select.executeQuery(" SELECT * FROM TeamMAP");
                        while (result.next()) {
                            Team team = new Team
                                    (result.getString("name"),result.getString("abbreviation"),result.getString("country"),result.getString("town"),result.getInt("foundationYear"),result.getInt("maxSquadSize"),result.getInt("budget"));
                            int contor=0;
                            for(Team team1 :TeamRepositoryJDBC.getInstance().getAllTeams())
                                if (team1.getAbbreviation().equals(team.getAbbreviation()) && team1.getName().equals(team.getName())) {
                                    contor = 1;
                                    break;
                                }
                            if(contor==0)
                                TeamRepositoryJDBC.getInstance().getAllTeams().add(team);
                        }
                    } else {
                        String insert_string = "INSERT INTO TeamMAP(name, abbreviation, country, town, foundationYear, maxSquadSize, budget) VALUES ('U Cluj', 'U', 'Romania', 'Cluj', 1919, 40, 8000000)";
                        Statement insert = connection.createStatement();
                        insert.executeUpdate(insert_string);
                        Team team=new Team("U Cluj", "U", "Romania", "Cluj", 1919, 40, 8000000);
                        TeamRepositoryJDBC.getInstance().getAllTeams().add(team);
                    }

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

    //public Connection connection;


    /**
     * we add a team in our database and also in our Java list
     * @param entity is a team
     * @throws SQLException because we work with SQL Tables/Databases
     */
    @Override
    public void add(Team entity) throws SQLException
    {
        allTeams.add(entity);
        String name=entity.getName();
        String abbreviation=entity.getAbbreviation();
        String country=entity.getCountry();
        String town = entity.getTown();
        int foundationYear=entity.getFoundationYear();
        int maxSquadSize=entity.getMaxSquadSize();
        int budget=entity.getBudget();
        Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
        String sql= "INSERT INTO TeamMAP (name,abbreviation,country,town,foundationYear,maxSquadSize,budget) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=connection1.prepareStatement(sql);
        preparedStatement.setObject(1,name);
        preparedStatement.setObject(2,abbreviation);
        preparedStatement.setObject(3,country);
        preparedStatement.setObject(4,town);
        preparedStatement.setObject(5,foundationYear);
        preparedStatement.setObject(6,maxSquadSize);
        preparedStatement.setObject(7,budget);
        preparedStatement.execute();
        preparedStatement.close();
        connection1.close();
    }


    /**
     * we remove a team that we search on, with the help of the name and the abbreviation
     * We remove the team from the Java list and from the SQL Table
     * @param s is the name
     * @param s1 is the abbreviation
     * @throws SQLException because we work with SQL Tables/Databases
     */
    @Override
    public void remove(String s, String s1) throws SQLException {
        if (findById(s, s1) != null) {
            String name = findById(s, s1).getName();
            String abbreviation = findById(s, s1).getAbbreviation();
            this.allTeams.remove(findById(s, s1));

            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "DELETE FROM TeamMAP WHERE name=? AND abbreviation=?";
            PreparedStatement statement=connection1.prepareStatement(sql);
            statement.setString(1,name);
            statement.setString(2,abbreviation);
            statement.executeUpdate();
            statement.close();


            String sql1="UPDATE PlayerMAP SET status=? WHERE status =?";
            PreparedStatement statement1=connection1.prepareStatement(sql1);
            statement1.setString(1,"Free Agent");
            statement1.setString(2,"Playing at " + name );
            statement1.executeUpdate();
            statement1.close();

            String sql2="UPDATE CoachMAP SET currentTeam=? WHERE currentTeam=?";
            PreparedStatement statement2 = connection1.prepareStatement(sql2);
            statement2.setString(1,"Free");
            statement2.setString(2,abbreviation);
            statement2.executeUpdate();
            statement2.close();
            connection1.close();

        }
    }


    /**
     * we update a team with a given name and abbreviation, by setting his values from the newEntity TeamObject
     * We update the team in our list and also in our SQL Table
     * @param s is the name
     * @param s1 is the abbreviation
     * @param newEntity is the Team Object that helps us to get the new values
     * @throws SQLException because we work with SQL Tables/Databases
     */
    @Override
    public void update(String s, String s1, Team newEntity) throws SQLException {
        if(findById(s,s1)!=null)
        {
            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String name = findById(s,s1).getName();
            String newName=newEntity.getName();
            String abbreviation= findById(s,s1).getAbbreviation();
            String newAbbreviation=newEntity.getAbbreviation();
            String newCountry=newEntity.getCountry();
            String newTown=newEntity.getTown();
            int newFoundationYear=newEntity.getFoundationYear();
            int newMaximumSquadSize=newEntity.getMaxSquadSize();
            int newBudget=newEntity.getBudget();

            String sql= "UPDATE TeamMAP SET name=?, abbreviation=?, country=?, town=?, foundationYear=?, maxSquadSize=?, budget=? WHERE name=? AND abbreviation=?";
            PreparedStatement preparedStatement= connection1.prepareStatement(sql);
            preparedStatement.setString(1,newName);
            preparedStatement.setString(2,newAbbreviation);
            preparedStatement.setString(3,newCountry);
            preparedStatement.setString(4,newTown);
            preparedStatement.setInt(5,newFoundationYear);
            preparedStatement.setInt(6,newMaximumSquadSize);
            preparedStatement.setInt(7,newBudget);
            preparedStatement.setString(8,name);
            preparedStatement.setString(9,abbreviation);
            preparedStatement.executeUpdate();



            this.findById(s, s1).setName(newEntity.getName());
            this.findById(newEntity.getName(), s1).setAbbreviation(newEntity.getAbbreviation());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setCountry(newEntity.getCountry());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setTown(newEntity.getTown());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setFoundationYear(newEntity.getFoundationYear());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setMaxSquadSize(newEntity.getMaxSquadSize());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setBudget(newEntity.getBudget());

            preparedStatement.close();
            connection1.close();
        }

    }


    /**
     * finds the team by the name and abbreviation
     * @param s is the team name
     * @param id2 is the abbreviation
     * @return TeamObject
     */
    @Override
    public Team findById(String s, String id2) {
        for (Team team : allTeams)
            if (s.equals(team.getName()) && id2.equals(team.getAbbreviation()))
                return team;
        return null;
    }

    /**
     * we give a given string as a abbreviation, and we get back a Team Object
     * @param abbreviation is the abbreviation of the team
     * @return team Object
     */
    public  Team getTeamByAbbreviation(String abbreviation)
    {
        for (Team team: allTeams)
            if(abbreviation.equals(team.getAbbreviation()))
                return team;
        return null;
    }




}