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

    public static final float LARVAE_TO_NYMPHS = 0.5f;
    public static final float NYMPHS_TO_ADULTS = 0.5f;

    public static final Map<Habitat.Type, Float> BIRTH_RATE_GOOD = Map.of(
            Habitat.Type.PASTURE, 0.8f,
            Habitat.Type.ECOTONE, 0.85f,
            Habitat.Type.WOOD, 0.9f
    );

    public static final Map<Habitat.Type, Float> BIRTH_RATE_POOR = Map.of(
            Habitat.Type.PASTURE, 0.25f,
            Habitat.Type.ECOTONE, 0.3f,
            Habitat.Type.WOOD, 0.35f
    );

    public static final float NATURAL_DEATH_RATE = 0.3f;

    public static final Map<Habitat.Type, Float> DESICCATION_RATE = Map.of(
            Habitat.Type.PASTURE, 0.5f,
            Habitat.Type.ECOTONE, 0.4f,
            Habitat.Type.WOOD, 0.2f
    );

    public static final Map<LifeCycleStage, Float> FREEZING_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.45f,
            LifeCycleStage.NYMPH, 0.35f,
            LifeCycleStage.ADULT, 0.35f
    );

    public static final Map<LifeCycleStage, Float> DISPERSAL_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.3f,
            LifeCycleStage.NYMPH, 0.45f,
            LifeCycleStage.ADULT, 0.5f
    );
}
