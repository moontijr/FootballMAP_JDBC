package repository.inmemory;

import model.Coach;
import model.Team;
import repository.CoachRepository;

import java.util.ArrayList;

public class CoachRepositoryMemory implements CoachRepository {
    private static CoachRepositoryMemory single_instance = null;

    private final ArrayList<Coach> allCoaches = new ArrayList<>();

    public static CoachRepositoryMemory getInstance() {
        if (single_instance == null) {
            single_instance = new CoachRepositoryMemory();
            populate();
        }
        return single_instance;
    }

    private static void populate() {
        CoachRepositoryMemory.getInstance().add(new Coach("MS01","Marius", "Sumudica", 58, "Romania", "Defensive", new Team("Trial", "TR", "Romania", "Medias", 1999, 40, 28000)));
    }



    @Override
    public void add(Coach entity) {
        this.allCoaches.add(entity);
    }

    @Override
    public void remove(String s, String id2) {
        this.allCoaches.remove(findByIdForAdmin(s, id2));
    }

    @Override
    public void update(String s, String id2, Coach newEntity) {
        this.findByIdForAdmin(s, id2).setId(newEntity.getId());
        this.findByIdForAdmin(newEntity.getId(), id2).setFirstName(newEntity.getFirstName());
        this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setLastName(newEntity.getLastName());
        this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setAge(newEntity.getAge());
        this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setNationality(newEntity.getNationality());
        this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setTeam(newEntity.getTeam());
        this.findByIdForAdmin(newEntity.getId(), newEntity.getFirstName()).setPlayStyle(newEntity.getPlayStyle());
    }

    @Override
    public Coach findById(String s, String id2) {
        for (Coach coach : allCoaches)
            if (s.equals(coach.getFirstName()) && id2.equals(coach.getLastName()))
                return coach;
        return null;
    }

    public Coach findByIdForAdmin(String s, String s1) {
        for (Coach coach : allCoaches)
            if (s.equals(coach.getId()) && s1.equals(coach.getFirstName()))
                return coach;
        return null;
    }






}
