package repository.inmemory;

import model.Player;
import repository.PlayerRepository;

import java.util.ArrayList;

public class PlayerRepositoryMemory implements PlayerRepository {



    private static PlayerRepositoryMemory single_instance = null;
    private final ArrayList<Player> allPlayers = new ArrayList<>();

    public static PlayerRepositoryMemory getInstance() {
        if (single_instance == null) {
            single_instance = new PlayerRepositoryMemory();
            populate();
        }
        return single_instance;
    }

    private static void populate() {
        PlayerRepositoryMemory.getInstance().add(new Player("TM01", "Tudor", "Moldovan", 26, "Spain", "Forward", 12500));
    }


    @Override
    public void add(Player entity) {
        this.allPlayers.add(entity);
    }

    @Override
    public void remove(String s, String s1) {
        if (findByIdForAdmin(s,s1) != null)
            this.allPlayers.remove(findByIdForAdmin(s, s1));
    }

    @Override
    public void update(String s, String s1, Player newEntity) {

        if (findByIdForAdmin(s, s1) != null) {
            this.findByIdForAdmin(s,s1).setId(newEntity.getId());
            this.findByIdForAdmin(newEntity.getId(),s1).setFirstName(newEntity.getFirstName());
            this.findByIdForAdmin(newEntity.getId(),newEntity.getFirstName()).setLastName(newEntity.getLastName());
            this.findByIdForAdmin(newEntity.getId(),newEntity.getFirstName()).setAge(newEntity.getAge());
            this.findByIdForAdmin(newEntity.getId(),newEntity.getFirstName()).setNationality(newEntity.getNationality());
            this.findByIdForAdmin(newEntity.getId(),newEntity.getFirstName()).setPosition(newEntity.getPosition());
            this.findByIdForAdmin(newEntity.getId(),newEntity.getFirstName()).setMarketValue(newEntity.getMarketValue());
        }
    }


    @Override
    public Player findById(String s, String s1) {
        for (Player player : allPlayers)
            if (s.equals(player.getFirstName()) && s1.equals(player.getLastName()))
                return player;
        return null;
    }

    public Player findByIdForAdmin(String s, String s1)
    {
        for (Player player : allPlayers)
            if (s.equals(player.getId()) && s1.equals(player.getFirstName()))
                return player;
        return null;
    }

}
