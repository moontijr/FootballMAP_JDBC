package repository.jdbc;

import model.Sponsor;
import model.Team;
import repository.SponsorRepository;

import java.sql.*;
import java.util.ArrayList;

public class SponsorRepositoryJDBC implements SponsorRepository {

    private static SponsorRepositoryJDBC single_instance=null;

    private final ArrayList<Sponsor> allSponsors = new ArrayList<>();

    public static SponsorRepositoryJDBC getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new SponsorRepositoryJDBC();
            String connectionURL = "jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true";
            try {
                try (Connection connection = DriverManager.getConnection(connectionURL)) {

                    Statement select = connection.createStatement();
                    ResultSet resultSet = select.executeQuery("SELECT * FROM SponsorMAP");
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if (count > 0) {
                        ResultSet result = select.executeQuery(" SELECT * FROM SponsorMAP");
                        while (result.next()) {
                            Sponsor sponsor = new Sponsor
                                    (result.getString("name"),result.getString("abbreviation"),result.getInt("netWorth"));
                            SponsorRepositoryJDBC.getInstance().getAllSponsors().add(sponsor);
                        }
                        ResultSet result1=select.executeQuery("SELECT * FROM Sponsor_Teams");
                        int countForSponsorTeams=0;
                        while(result1.next())
                        {
                            countForSponsorTeams++;
                        }
                        if(countForSponsorTeams>0)
                        {
                            ResultSet result2= select.executeQuery("SELECT * FROM Sponsor_Teams");
                            while(result2.next())
                            {
                                Sponsor sponsor=SponsorRepositoryJDBC.getInstance().getSponsorByAbbreviation(result2.getString("idSponsor"));
                                Team team = TeamRepositoryJDBC.getInstance().getTeamByAbbreviation((result2.getString("idTeam")));
                                sponsor.sponsorTeam(team);
                            }
                        }

                    } else {
                        String insert_string = "INSERT INTO SponsorMAP(name, abbreviation, netWorth) VALUES ('NTTData', 'NTT', 200000000)";
                        Statement insert = connection.createStatement();
                        insert.executeUpdate(insert_string);
                        Sponsor sponsor=new Sponsor("NTTData", "NTT", 200000000);
                        SponsorRepositoryJDBC.getInstance().getAllSponsors().add(sponsor);
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

    //private Connection connection;

    public ArrayList<Sponsor> getAllSponsors() {
        return allSponsors;
    }


    /**
     * we add a sponsor in our list in java, and also in our sql database
     * @param entity of a sponsor object
     * @throws SQLException because we work SQL Tables/Database
     */
    @Override
    public void add(Sponsor entity) throws SQLException
    {
        allSponsors.add(entity);
        String name=entity.getName();
        String abbreviation=entity.getAbbreviation();
        int netWorth=entity.getNetWorth();
        Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
        String sql= "INSERT INTO SponsorMAP (name, abbreviation,netWorth) VALUES (?,?,?)";
        PreparedStatement preparedStatement=connection1.prepareStatement(sql);
        preparedStatement.setObject(1,name);
        preparedStatement.setObject(2,abbreviation);
        preparedStatement.setObject(3,netWorth);
        preparedStatement.execute();
        preparedStatement.close();
        connection1.close();
    }

    /**
     * we remove a sponsor, by searching on his name and abbreviation, and if it exists, then we remove him
     * @param s is the name
     * @param s1 is the abbreviation
     * @throws SQLException because we work with SQL Database/Tables
     */
    @Override
    public void remove(String s, String s1) throws SQLException {
        if (findById(s, s1) != null) {
            String name = findById(s, s1).getName();
            String abbreviation = findById(s, s1).getAbbreviation();
            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String sql= "DELETE FROM SponsorMAP WHERE name=? AND abbreviation=?";
            PreparedStatement statement=connection1.prepareStatement(sql);
            statement.setString(1,name);
            statement.setString(2,abbreviation);
            statement.executeUpdate();
            statement.close();
            connection1.close();
            this.allSponsors.remove(findById(s, s1));
        }
    }

    /**
     * we search the Sponsor on id and firstName, and then we update his details with the help of the newEntity that is a Sponsor Object
     * @param s is the name
     * @param s1 is the abbreviation
     * @param newEntity is a sponsor Object
     * @throws SQLException because we work with SQL Tables/Database
     */
    @Override
    public void update(String s, String s1, Sponsor newEntity) throws SQLException {
        if(findById(s,s1)!=null)
        {
            Connection connection1=DriverManager.getConnection("jdbc:sqlserver://localhost:52448;databaseName=MAP;user=user1;password=1234;encrypt=true;trustServerCertificate=true");
            String name = findById(s,s1).getName();
            String newName=newEntity.getName();
            String abbreviation= findById(s,s1).getAbbreviation();
            String newAbbreviation=newEntity.getAbbreviation();
            int newNetWorth=newEntity.getNetWorth();
            String sql= "UPDATE SponsorMAP SET name=?, abbreviation=?, netWorth=?  WHERE name=? AND abbreviation=?";
            PreparedStatement preparedStatement= connection1.prepareStatement(sql);
            preparedStatement.setString(1,newName);
            preparedStatement.setString(2,newAbbreviation);
            preparedStatement.setInt(3,newNetWorth);
            preparedStatement.setString(4,name);
            preparedStatement.setString(5,abbreviation);
            preparedStatement.executeUpdate();



            this.findById(s, s1).setName(newEntity.getName());
            this.findById(newEntity.getName(), s1).setAbbreviation(newEntity.getAbbreviation());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setNetWorth(newEntity.getNetWorth());

            connection1.close();
            preparedStatement.close();
        }

    }


    /**
     * we get the Sponsor Object by search on the name and abbreviation
     * @param s is the name
     * @param id2 is the abbreviation
     * @return a sponsor Object
     */
    @Override
    public Sponsor findById(String s, String id2) {
        for (Sponsor sponsor : this.allSponsors)
            if (sponsor.getName().equals(s) && sponsor.getAbbreviation().equals(id2))
                return sponsor;
        return null;
    }

    /**
     * same method as findById but it returns true/false if the sponsors exists/doesnt exist
     * @param name is the name
     * @param abbreviation is the abbreviation
     * @return true/false (boolean)
     */
    public boolean existsSponsor(String name, String abbreviation) {
        for (Sponsor sponsor : this.allSponsors)
            if (sponsor.getName().equals(name) && sponsor.getAbbreviation().equals(abbreviation))
                return true;
        return false;
    }

    /**
     * we get a sponsor object by only searching on his abbreviation
     * @param s is the abbreviation
     * @return a sponsor object
     */
    public Sponsor getSponsorByAbbreviation(String s)
    {
        for(Sponsor sponsor: this.allSponsors)
            if(sponsor.getAbbreviation().equals(s))
                return sponsor;
        return null;
    }


}

