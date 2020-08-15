package eu.ecoepi.iris;

import eu.ecoepi.iris.components.Habitat;

import java.util.Map;

public class Parameters {

    public static final int TIME_STEPS = 365;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 12;
    public static final int BOUNDARY_ECOTONE = 4;
    public static final int BOUNDARY_PASTURE = 8;

    public static final int INITIAL_LARVAE = 10;
    public static final int INITIAL_NYMPHS = 10;
    public static final int INITIAL_ADULTS = 10;
    public static final int INITIAL_INFECTED_LARVAE = 0;
    public static final int INITIAL_INFECTED_NYMPHS = 0;
    public static final int INITIAL_INFECTED_ADULTS = 0;

    public static final float LARVAE_TO_NYMPHS = 0.4f;
    public static final float NYMPHS_TO_ADULTS = 0.3f;

    public static final float BIRTH_RATE = 0.5f;
    public static final float DEATH_RATE = 0.3f;

    public static final Map<Habitat.Type, Float> DESICCATION_RATE = Map.of(
            Habitat.Type.PASTURE, 0.35f,
            Habitat.Type.ECOTONE, 0.25f,
            Habitat.Type.WOOD, 0.2f
    );

    public static final Map<LifeCycleStage, Float> DISPERSAL_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.005f,
            LifeCycleStage.NYMPH, 0.05f,
            LifeCycleStage.ADULT, 0.1f
    );
}
