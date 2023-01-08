package repository.inmemory;

import model.Sponsor;
import repository.SponsorRepository;

import java.util.ArrayList;

public class SponsorRepositoryMemory implements SponsorRepository {

    private static SponsorRepositoryMemory single_instance = null;

    private final ArrayList<Sponsor> allSponsors = new ArrayList<>();

    public static SponsorRepositoryMemory getInstance() {
        if (single_instance == null) {
            single_instance = new SponsorRepositoryMemory();
            populate();
        }
        return single_instance;
    }

    private static void populate() {
        SponsorRepositoryMemory.getInstance().add(new Sponsor("NTTData", "NTT", 200000000));
    }


    @Override
    public void add(Sponsor entity) {
        this.allSponsors.add(entity);
    }

    @Override
    public void remove(String s, String id2) {
        this.allSponsors.remove(findById(s, id2));
    }

    @Override
    public void update(String s, String id2, Sponsor newEntity) {
        this.findById(s, id2).setName(newEntity.getName());
        this.findById(newEntity.getName(), id2).setAbbreviation(newEntity.getAbbreviation());
        this.findById(newEntity.getName(), newEntity.getAbbreviation()).setNetWorth(newEntity.getNetWorth());
    }

    @Override
    public Sponsor findById(String s, String id2) {
        for (Sponsor sponsor : this.allSponsors)
            if (sponsor.getName().equals(s) && sponsor.getAbbreviation().equals(id2))
                return sponsor;
        return null;
    }


}
