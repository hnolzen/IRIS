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

    public static final int INITIAL_NEXT_STAGE_LARVAE = 0;
    public static final int INITIAL_NEXT_STAGE_NYMPHS = 0;
    public static final int INITIAL_NEXT_STAGE_ADULTS = 0;

    public static final int INITIAL_DESICCATED_LARVAE = 0;
    public static final int INITIAL_DESICCATED_NYMPHS = 0;
    public static final int INITIAL_DESICCATED_ADULTS = 0;

    public static final int INITIAL_FROZEN_LARVAE = 0;
    public static final int INITIAL_FROZEN_NYMPHS = 0;
    public static final int INITIAL_FROZEN_ADULTS = 0;

    public static final int INITIAL_DEAD_ADULTS = 0;

    public static final int INITIAL_NEW_ACTIVE_LARVAE = 0;
    public static final int INITIAL_NEW_ACTIVE_NYMPHS = 0;
    public static final int INITIAL_NEW_ACTIVE_ADULTS = 0;

    public static final int INITIAL_NEW_INACTIVE_LARVAE = 0;
    public static final int INITIAL_NEW_INACTIVE_NYMPHS = 0;
    public static final int INITIAL_NEW_INACTIVE_ADULTS = 0;

    public static final float ACTIVATION_RATE = 0.05f;

    public static final int BEGIN_OF_DEVELOPMENT = 195;
    public static final int END_OF_DEVELOPMENT = 255;

    public static final float ACTIVATION_THRESHOLD_NECESSARY_MAXIMAL_MAX_TEMP = 35f;   // Gray et al. 2016, McLeod 1935
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MAX_TEMP = 1.9f;  // Perret et al. 2000
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MEAN_TEMP = 1.2f; // Perret et al. 2000, Schulz et al. 2014
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_HUMIDITY = 45.0f; // Greenfield 2011
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MAX_TEMP = 10.5f;   // Perret et al. 2000
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MAX_TEMP = 26.0f;   // Greenfield 2011, Schulz et al. 2014 (25.9)
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MEAN_TEMP = 6.0f;   // Gilbert et al. 2014
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MEAN_TEMP = 20.0f;  // Kubiak and Dziekońska−Rynko 2006

    public static final float INITIAL_SHARE_OF_ACTIVATION_RATE = 0.0f;
    public static final float OPTIMAL_SHARE_OF_ACTIVATION_RATE = 1.0f;
    public static final float SUBOPTIMAL_SHARE_OF_ACTIVATION_RATE = 0.05f; // In suboptimal conditions, only few ticks become active

    public static final float DEATH_THRESHOLD_FREEZING_MIN_TEMP_WITHOUT_SNOW = -18.9f; // Gray et al. 2009
    public static final float DEATH_THRESHOLD_FREEZING_MIN_TEMP_WITH_SNOW = -15.0f;    // Ostfeld and Brunner 2015, Jore et al. 2014
    public static final float DEATH_THRESHOLD_FREEZING_MINIMAL_SNOW_HEIGHT = 1.0f;     // Ostfeld and Brunner 2015, Jore et al. 2014
    public static final float DEATH_THRESHOLD_DESICCATION_MINIMAL_HUMIDITY = 70.0f;    // Ostfeld and Brunner 2015
    public static final float DEATH_THRESHOLD_DESICCATION_MINIMAL_MEAN_TEMP = 15.0f;   // Ostfeld and Brunner 2015

    public static final Map<Habitat.Type, Float> BIRTH_RATE = Map.of(
            Habitat.Type.PASTURE, 0.8f,
            Habitat.Type.ECOTONE, 0.85f,
            Habitat.Type.WOOD, 0.9f
    );

    public static final float NATURAL_DEATH_RATE = 0.1f;

    public static final Map<Habitat.Type, Float> DESICCATION_RATE = Map.of(
            Habitat.Type.PASTURE, 0.10f,
            Habitat.Type.ECOTONE, 0.05f,
            Habitat.Type.WOOD, 0.02f
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

    public static final double[] DISTANCE_PROB = {0.25, 0.25, 0.20, 0.15, 0.05, 0.04, 0.03, 0.02, 0.01};

}
