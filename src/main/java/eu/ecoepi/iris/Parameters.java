package eu.ecoepi.iris;

import eu.ecoepi.iris.components.Habitat;

import java.util.Map;

public class Parameters {

    public static final int TIME_STEPS = 365;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 12;

    public static final int INITIAL_LARVAE = 0;
    public static final int INITIAL_NYMPHS = 0;
    public static final int INITIAL_ADULTS = 0;

    public static final int INITIAL_INACTIVE_ADULTS = 150;

    public static final int INITIAL_FED_LARVAE = 0;
    public static final int INITIAL_FED_NYMPHS = 0;
    public static final int INITIAL_FED_ADULTS = 0;

    public static final int INITIAL_LATE_FED_LARVAE = 0;
    public static final int INITIAL_LATE_FED_NYMPHS = 0;

    public static final int INITIAL_INFECTED_LARVAE = 0;
    public static final int INITIAL_INFECTED_NYMPHS = 0;
    public static final int INITIAL_INFECTED_ADULTS = 0;

    public static final float ACTIVATION_RATE = 0.05f;

    public static final int BEGIN_OF_DEVELOPMENT = 181;                     // Beginning of July
    public static final int END_OF_DEVELOPMENT_LARVAE_TO_NYMPHS = 289;      // Mid-October
    public static final int END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS = 274;      // Beginning of October
    public static final int END_OF_DEVELOPMENT_ADULTS_TO_LARVAE = 289;         // Mid-October

    public static final int LATE_FEEDING_TIME = 232;                        // Mid-September

    public static final float ACTIVATION_THRESHOLD_NECESSARY_MAXIMAL_MAX_TEMP = 35f;   // Gray et al. 2016, McLeod 1935
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MAX_TEMP = 1.9f;  // Perret et al. 2000
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_MEAN_TEMP = 1.2f; // Perret et al. 2000, Schulz et al. 2014
    public static final float ACTIVATION_THRESHOLD_NECESSARY_MINIMAL_HUMIDITY = 45.0f; // Greenfield 2011
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MAX_TEMP = 10.5f;   // Perret et al. 2000
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MAX_TEMP = 26.0f;   // Greenfield 2011, Schulz et al. 2014 (25.9)
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MINIMAL_MEAN_TEMP = 6.0f;   // Gilbert et al. 2014
    public static final float ACTIVATION_THRESHOLD_OPTIMAL_MAXIMAL_MEAN_TEMP = 20.0f;  // Kubiak and Dziekońska−Rynko 2006

    public static final float INITIAL_SHARE_OF_ACTIVATION_RATE = 0.0f;
    public static final float OPTIMAL_SHARE_OF_ACTIVATION_RATE = 1.0f;
    public static final float SUBOPTIMAL_SHARE_OF_ACTIVATION_RATE = 0.05f; // In suboptimal conditions, only few ticks become active

    public static final float DEATH_THRESHOLD_FREEZING_MIN_TEMP_WITHOUT_SNOW = -18.9f; // Gray et al. 2009
    public static final float DEATH_THRESHOLD_DESICCATION_MINIMAL_HUMIDITY = 70.0f;    // Ostfeld and Brunner 2015
    public static final float DEATH_THRESHOLD_DESICCATION_MINIMAL_MEAN_TEMP = 15.0f;   // Ostfeld and Brunner 2015

    public static final int BEGIN_SPRING = 60;
    public static final int BEGIN_SUMMER = 150;
    public static final int BEGIN_AUTUMN = 240;
    public static final int BEGIN_WINTER = 330;

    public static final Map<Habitat.Type, Float> SET_LOCAL_CLIMATE_SPRING_AUTUMN = Map.of(
            Habitat.Type.PASTURE, 0f,
            Habitat.Type.ECOTONE, -1f,
            Habitat.Type.WOOD, -2f
    );

    public static final Map<Habitat.Type, Float> SET_LOCAL_CLIMATE_SUMMER = Map.of(    // Bonan 2016, Geiger et al. 1995
            Habitat.Type.PASTURE, 0f,
            Habitat.Type.ECOTONE, -2f,
            Habitat.Type.WOOD, -4.0f
    );

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

    public static final Map<LifeCycleStage, Float> FEEDING_RATE = Map.of(
            LifeCycleStage.LARVAE, 0.01f,
            LifeCycleStage.NYMPH, 0.03f,
            LifeCycleStage.ADULT, 0.05f
    );

    public static final double[] DISTANCE_PROB = {0.25, 0.25, 0.20, 0.15, 0.05, 0.04, 0.03, 0.02, 0.01};

}
