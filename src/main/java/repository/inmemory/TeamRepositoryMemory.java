package repository.inmemory;

import model.Team;
import repository.TeamRepository;

import java.util.ArrayList;


public class TeamRepositoryMemory implements TeamRepository {

    private static TeamRepositoryMemory single_instance = null;
    private final ArrayList<Team> allTeams = new ArrayList<>();

    public static TeamRepositoryMemory getInstance() {
        if (single_instance == null) {
            single_instance = new TeamRepositoryMemory();
            populate();
        }
        return single_instance;
    }

    private static void populate() {
        TeamRepositoryMemory.getInstance().add(new Team("U Cluj", "U", "Romania", "Cluj", 1919, 40, 8000000));
    }


    @Override
    public void add(Team entity) {
        this.allTeams.add(entity);
    }

    @Override
    public void remove(String s, String id2) {
        findById(s, id2).disbandTeam();
        this.allTeams.remove(findById(s, id2));
    }

    @Override
    public void update(String s, String s1, Team newEntity) {
        if (findById(s, s1) != null) {
            this.findById(s, s1).setName(newEntity.getName());
            this.findById(newEntity.getName(), s1).setAbbreviation(newEntity.getAbbreviation());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setCountry(newEntity.getCountry());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setTown(newEntity.getTown());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setFoundationYear(newEntity.getFoundationYear());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setMaxSquadSize(newEntity.getMaxSquadSize());
            this.findById(newEntity.getName(), newEntity.getAbbreviation()).setBudget(newEntity.getBudget());
        }
    }

    @Override
    public Team findById(String s, String id2) {
        for (Team team : allTeams)
            if (s.equals(team.getName()) && id2.equals(team.getAbbreviation()))
                return team;
        return null;
    }

//    public boolean existsTeam(String name, String abbreviation)
//    {
//        for(Team team:this.allTeams)
//            if(team.getName().equals(name)&&team.getAbbreviation().equals(abbreviation))
//                return true;
//        return false;
//    }

}
