package repository.jdbc;


import model.Player;
import model.Team;
import repository.PlayerRepository;

import java.sql.*;
import java.util.ArrayList;

public class PlayerRepositoryJDBC implements PlayerRepository {

    private  ArrayList<Player> allPlayers = new ArrayList<>();




    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    private static PlayerRepositoryJDBC single_instance=null;

    public static PlayerRepositoryJDBC getInstance()
    {
        if(single_instance == null)
        {
            single_instance= new PlayerRepositoryJDBC();
            String connectionURL = "jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true";
            try {
                System.out.print("Connecting to the server......");
                try (Connection connection = DriverManager.getConnection(connectionURL)) {
                    System.out.println("Connected to the Server.");

                    Statement select = connection.createStatement();
                    ResultSet resultSet = select.executeQuery("SELECT * FROM PlayerMAP");
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if (count > 0) {
                        ResultSet result = select.executeQuery(" SELECT * FROM PlayerMAP");
                        while (result.next()) {
                            Player player = new Player
                                    (result.getString("id"), result.getString("firstName"), result.getString("lastName"), result.getInt("age"), result.getString("nationality"), result.getString("position"), result.getInt("marketValue"));
                            player.setStatus(result.getString("status"));
                            PlayerRepositoryJDBC.getInstance().allPlayers.add(player);
                            for(Team team : TeamRepositoryJDBC.getInstance().getAllTeams())
                            {
                                if(player.getStatus().contains(team.getName()))
                                {
                                    team.getSquad().add(player);
                                }
                            }

                        }
                    } else {
                        String insert_string = "INSERT INTO PlayerMAP(id, firstName, lastName, age, nationality, position, marketValue, status) VALUES ('P1','Dan','Mic',18,'Romania','Forward',1250,'Free Agent')";
                        Statement insert = connection.createStatement();
                        insert.executeUpdate(insert_string);
                        Player player = new Player("P1", "Dan", "Mic", 18, "Romania", "Forward", 1250);
                        player.setStatus("Free Agent");
                        PlayerRepositoryJDBC.getInstance().allPlayers.add(player);
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
     * when we first initialize our playerRepoJDBC, we have to look if there are already some given datas in our database
     * if yes, then we get them into our list that we currently work with, if not, we populate the list and the databse with some player Objects
     */

    /**
     * we add a player in our list that we work with, and also in our database
     *
     * @param entity of a player Object
     * @throws SQLException because we work with sql database/table
     */
    @Override
    public void add(Player entity) throws SQLException {
        allPlayers.add(entity);
        String id = entity.getId();
        String firstName = entity.getFirstName();
        String lastName = entity.getLastName();
        int age = entity.getAge();
        String nationality = entity.getNationality();
        String position = entity.getPosition();
        int marketValue = entity.getMarketValue();
        Connection connection1 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
        String sql = "INSERT INTO PlayerMAP (id,firstName,lastName,age,nationality,position,marketValue,status) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection1.prepareStatement(sql);
        preparedStatement.setObject(1, id);
        preparedStatement.setObject(2, firstName);
        preparedStatement.setObject(3, lastName);
        preparedStatement.setObject(4, age);
        preparedStatement.setObject(5, nationality);
        preparedStatement.setObject(6, position);
        preparedStatement.setObject(7, marketValue);
        preparedStatement.setString(8,"Free Agent");
        preparedStatement.execute();
        preparedStatement.close();
        connection1.close();

    }

    /**
     * we remove the player with the given id and firstName from our list, and from our database
     *
     * @param s  is the id
     * @param s1 is the firstName
     * @throws SQLException because we work with SQL Tables/Databases
     */
    @Override
    public void remove(String s, String s1) throws SQLException {
        if (findByIdForAdmin(s, s1) != null) {
            String id = findByIdForAdmin(s, s1).getId();
            String firstName = findByIdForAdmin(s, s1).getFirstName();
            Connection connection1 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql = "DELETE FROM PlayerMAP WHERE id=? AND firstName=?";
            PreparedStatement statement = connection1.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, firstName);
            statement.executeUpdate();
            statement.close();
            connection1.close();


            this.allPlayers.remove(findByIdForAdmin(s, s1));

        }
    }

    /**
     * we update the player with the given id and firstName with help from the newEntity
     *
     * @param s         is the id
     * @param s1        is the firstName
     * @param newEntity is the Entity that will help us to set the new values to our old Player Object
     * @throws SQLException because we work with SQL Tables/Databases
     */
    @Override
    public void update(String s, String s1, Player newEntity) throws SQLException {
        if (findByIdForAdmin(s, s1) != null) {
            Connection connection1 = DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String id = findByIdForAdmin(s, s1).getId();
            String newId = newEntity.getId();
            String firstName = findByIdForAdmin(s, s1).getFirstName();
            String newFirstName = newEntity.getFirstName();
            String newLastName = newEntity.getLastName();
            int newAge = newEntity.getAge();
            String newNationality = newEntity.getNationality();
            String newPosition = newEntity.getPosition();
            int newMarketValue = newEntity.getMarketValue();

            this.findByIdForAdmin(s, s1).setId(newEntity.getId());
            this.findByIdForAdmin(newEntity.getId(), s1).setFirstName(newEntity.getFirstName());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setLastName(newEntity.getLastName());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setAge(newEntity.getAge());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setNationality(newEntity.getNationality());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setPosition(newEntity.getPosition());
            this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setMarketValue(newEntity.getMarketValue());

            String sql = "UPDATE PlayerMAP SET id=?, firstName=?, lastName=?, age=?, nationality=?, position=?, marketValue=? WHERE id=? AND firstName=?";
            PreparedStatement preparedStatement = connection1.prepareStatement(sql);
            preparedStatement.setString(1, newId);
            preparedStatement.setString(2, newFirstName);
            preparedStatement.setString(3, newLastName);
            preparedStatement.setInt(4, newAge);
            preparedStatement.setString(5, newNationality);
            preparedStatement.setString(6, newPosition);
            preparedStatement.setInt(7, newMarketValue);
            preparedStatement.setString(8, id);
            preparedStatement.setString(9, firstName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection1.close();


        }

    }


    /**
     * we search a player by his firstName and SecondName
     * @param s is the firstName
     * @param id2 is the lastName
     * @return a player Object
     */
    @Override
    public Player findById(String s, String id2) {
        for (Player player : allPlayers)
            if (s.equals(player.getFirstName()) && id2.equals(player.getLastName()))
                return player;
        return null;
    }

    /**
     * the admin needs to search the player by the ID, and firstName
     * @param s is the id
     * @param s1 is the firstName
     * @return a player with the given id and firstName
     */
    public Player findByIdForAdmin(String s, String s1) {
        for (Player player : allPlayers)
            if (s.equals(player.getId()) && s1.equals(player.getFirstName()))
                return player;
        return null;
    }

    /**
     * same method as findByIdFor admin but returns true/false if it exists/doesnt instead of the Object
     * @param id is the id
     * @param firstName is the firstsName
     * @return true if it exists, false if not
     */
    public boolean existsPlayerForAdmin(String id, String firstName) {
        for (Player player : this.allPlayers)
            if (player.getId().equals(id) && player.getFirstName().equals(firstName))
                return true;
        return false;
    }


}
