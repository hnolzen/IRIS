package eu.ecoepi.iris.resources;

import eu.ecoepi.iris.CohortStateTicks;
import eu.ecoepi.iris.components.Habitat;

import java.util.Map;

public class Parameters {

    public static final int TIME_STEPS = 365;
    public static final int GRID_WIDTH = 12;
    public static final int GRID_HEIGHT = 12;

    public static final int BEGIN_OF_DEVELOPMENT = 181;                     // Beginning of July
    public static final int END_OF_DEVELOPMENT_LARVAE_TO_NYMPHS = 289;      // Mid-October
    public static final int END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS = 274;      // Beginning of October
    public static final int END_OF_DEVELOPMENT_ADULTS_TO_LARVAE = 289;      // Mid-October

    public static final int START_LARVAE_QUESTING = 105; // Mid-April

    public static final int LATE_FEEDING_TIME = 232;                        // Mid-September

    public static final float ACTIVATION_NECESSARY_MAXIMAL_MAX_TEMP = 35f;   // Gray et al. 2016, McLeod 1935
    public static final float ACTIVATION_NECESSARY_MINIMAL_MAX_TEMP = 1.9f;  // Perret et al. 2000
    public static final float ACTIVATION_NECESSARY_MINIMAL_MEAN_TEMP = 1.2f; // Perret et al. 2000, Schulz et al. 2014
    public static final float ACTIVATION_NECESSARY_MINIMAL_HUMIDITY = 45.0f; // Greenfield 2011
    public static final float ACTIVATION_OPTIMAL_MINIMAL_MAX_TEMP = 10.5f;   // Perret et al. 2000
    public static final float ACTIVATION_OPTIMAL_MAXIMAL_MAX_TEMP = 26.0f;   // Greenfield 2011, Schulz et al. 2014 (25.9)
    public static final float ACTIVATION_OPTIMAL_MINIMAL_MEAN_TEMP = 6.0f;   // Gilbert et al. 2014
    public static final float ACTIVATION_OPTIMAL_MAXIMAL_MEAN_TEMP = 20.0f;  // Kubiak and Dziekońska−Rynko 2006

    public static final float SUBOPTIMAL_SHARE_OF_ACTIVATION_RATE = 0.05f; // In suboptimal conditions, only few ticks become active

    public static final float FREEZING_MIN_TEMP_WITHOUT_SNOW = -18.9f; // Gray et al. 2009
    public static final float DESICCATION_MINIMAL_HUMIDITY = 80.0f;    // Hauser et al. 2018, Medlock et al. 2013, Gray et al. 2016
    public static final float DESICCATION_MINIMAL_MEAN_TEMP = 15.0f;   // Ostfeld and Brunner 2015

    public static final int BEGIN_SPRING = 60;
    public static final int BEGIN_SUMMER = 150;
    public static final int BEGIN_AUTUMN = 240;
    public static final int BEGIN_WINTER = 330;

    public static final Map<Habitat.Type, Float> LOCAL_CLIMATE_SPRING_AUTUMN = Map.of(
            Habitat.Type.MEADOW, 0f,
            Habitat.Type.ECOTONE, -1f,
            Habitat.Type.WOOD, -2f
    );

    public static final Map<Habitat.Type, Float> LOCAL_CLIMATE_SUMMER = Map.of(    // Bonan 2016, Geiger et al. 1995
            Habitat.Type.MEADOW, 0f,
            Habitat.Type.ECOTONE, -2f,
            Habitat.Type.WOOD, -4.0f
    );

    public static final Map<Habitat.Type, Float> LOCAL_HUMIDITY = Map.of(
            Habitat.Type.MEADOW, 1.12f,    // estimation
            Habitat.Type.ECOTONE, 1.18f,    // estimation
            Habitat.Type.WOOD, 1.24f        // Boehnke 2017
    );

    public static final Map<Habitat.Type, Float> DESICCATION_RATE = Map.of(
            Habitat.Type.MEADOW, 0.10f,
            Habitat.Type.ECOTONE, 0.05f,
            Habitat.Type.WOOD, 0.02f
    );

    public static final Map<CohortStateTicks, Float> FREEZING_RATE = Map.of(
            CohortStateTicks.LARVAE_QUESTING, 0.03f,
            CohortStateTicks.NYMPHS_QUESTING, 0.03f,
            CohortStateTicks.ADULTS_QUESTING, 0.03f
    );

    public static final Map<CohortStateTicks, Float> FEEDING_RATE = Map.of(
            CohortStateTicks.LARVAE_QUESTING, 0.01f,
            CohortStateTicks.NYMPHS_QUESTING, 0.03f,
            CohortStateTicks.ADULTS_QUESTING, 0.05f
    );

    public static final float INFECTION_RATE = 0.01f;

    public static final double[] DISTANCE_PROB = {0.25, 0.25, 0.20, 0.15, 0.05, 0.04, 0.03, 0.02, 0.01};

    public static final Map<Integer, Integer> FRUCTIFICATION_INDEX = Map.of(
            2007, 2,
            2008, 1,
            2009, 4,
            2010, 1,
            2011, 4,
            2012, 1,
            2013, 2,
            2014, 2,
            2015, 1,
            2016, 4
    );

    public static float abundanceReductionDueToFructificationIndex(int year) {
        float abundanceReduction;
        switch (FRUCTIFICATION_INDEX.get(year - 2)) {
            case 1:
                abundanceReduction = 0.25f;
                break;
            case 2:
                abundanceReduction = 0.5f;
                break;
            case 3:
                abundanceReduction = 0.75f;
                break;
            case 4:
                abundanceReduction = 1.0f;
                break;
            default:
                throw new RuntimeException("Invalid fructification index");
        }
        return abundanceReduction;
    }

}
