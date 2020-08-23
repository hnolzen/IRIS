package eu.ecoepi.iris;

import eu.ecoepi.iris.components.Habitat;

import java.util.Map;

public class Parameters {

    public static final int TIME_STEPS = 365;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 12;
    public static final int BOUNDARY_ECOTONE = 4;
    public static final int BOUNDARY_PASTURE = 8;

    public static final int INITIAL_LARVAE = 0;
    public static final int INITIAL_NYMPHS = 0;
    public static final int INITIAL_ADULTS = 0;
    public static final int INITIAL_INACTIVE_LARVAE = 150;
    public static final int INITIAL_INACTIVE_NYMPHS = 150;
    public static final int INITIAL_INACTIVE_ADULTS = 150;
    public static final int INITIAL_FED_LARVAE = 0;
    public static final int INITIAL_FED_NYMPHS = 0;
    public static final int INITIAL_FED_ADULTS = 0;

    public static final int INITIAL_INFECTED_LARVAE = 0;
    public static final int INITIAL_INFECTED_NYMPHS = 0;
    public static final int INITIAL_INFECTED_ADULTS = 0;

    public static final float ADULTS_TO_LARVAE = 0.02f;
    public static final float LARVAE_TO_NYMPHS = 0.02f;
    public static final float NYMPHS_TO_ADULTS = 0.02f;

    public static final float ACTIVITY_RATE = 0.05f;

    public static final Map<Habitat.Type, Float> BIRTH_RATE = Map.of(
            Habitat.Type.PASTURE, 0.8f,
            Habitat.Type.ECOTONE, 0.85f,
            Habitat.Type.WOOD, 0.9f
    );

    public static final float NATURAL_DEATH_RATE = 0.1f;

    public static final Map<Habitat.Type, Float> DESICCATION_RATE = Map.of(
            Habitat.Type.PASTURE, 0.20f,
            Habitat.Type.ECOTONE, 0.10f,
            Habitat.Type.WOOD, 0.05f
    );

    public static final Map<LifeCycleStage, Float> FREEZING_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.03f,
            LifeCycleStage.NYMPH, 0.03f,
            LifeCycleStage.ADULT, 0.03f
    );

    public static final Map<LifeCycleStage, Float> DISPERSAL_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.01f,
            LifeCycleStage.NYMPH, 0.03f,
            LifeCycleStage.ADULT, 0.05f
    );
}
