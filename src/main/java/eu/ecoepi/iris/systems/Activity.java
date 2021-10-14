package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.CohortStateTicks;
import eu.ecoepi.iris.resources.Parameters;
import eu.ecoepi.iris.resources.Randomness;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.Humidity;
import eu.ecoepi.iris.components.Temperature;
import eu.ecoepi.iris.components.TickAbundance;

@All({TickAbundance.class, Temperature.class, Humidity.class})
public class Activity extends IteratingSystem {

    private final float activationRate;
    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Wire
    Randomness randomness;

    @Wire
    TimeStep timestep;

    public Activity(float activationRate) {
        this.activationRate = activationRate;
    }

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        var shareOfActivationRate = 0.0f;

        if (temperature.getMaxTemperature() < Parameters.ACTIVATION_NECESSARY_MAXIMAL_MAX_TEMP &&
                temperature.getMaxTemperature() > Parameters.ACTIVATION_NECESSARY_MINIMAL_MAX_TEMP &&
                temperature.getMeanTemperature() > Parameters.ACTIVATION_NECESSARY_MINIMAL_MEAN_TEMP &&
                humidity.getRelativeHumidity() > Parameters.ACTIVATION_NECESSARY_MINIMAL_HUMIDITY
        ) {

            if (temperature.getMaxTemperature() > Parameters.ACTIVATION_OPTIMAL_MINIMAL_MAX_TEMP &&
                    temperature.getMaxTemperature() < Parameters.ACTIVATION_OPTIMAL_MAXIMAL_MAX_TEMP &&
                    temperature.getMeanTemperature() > Parameters.ACTIVATION_OPTIMAL_MINIMAL_MEAN_TEMP &&
                    temperature.getMeanTemperature() < Parameters.ACTIVATION_OPTIMAL_MAXIMAL_MEAN_TEMP
            ) {
                shareOfActivationRate = 1.0f;
            } else {
                shareOfActivationRate = Parameters.SUBOPTIMAL_SHARE_OF_ACTIVATION_RATE;
            }
        }

        var newQuestingLarvae = 0;
        var newInfectedQuestingLarvae = 0;
        if (timestep.getCurrent() > Parameters.START_LARVAE_QUESTING) {
            newQuestingLarvae = randomness.roundRandom(abundance.getStage(CohortStateTicks.LARVAE_INACTIVE) * activationRate * shareOfActivationRate);
            newInfectedQuestingLarvae = randomness.roundRandom(abundance.getStage(CohortStateTicks.LARVAE_INACTIVE_INFECTED) * activationRate * shareOfActivationRate);
        }
        var newQuestingNymphs = randomness.roundRandom(abundance.getStage(CohortStateTicks.NYMPHS_INACTIVE) * activationRate * shareOfActivationRate);
        var newQuestingAdults = randomness.roundRandom(abundance.getStage(CohortStateTicks.ADULTS_INACTIVE) * activationRate * shareOfActivationRate);
        var newInactiveLarvae = randomness.roundRandom(abundance.getStage(CohortStateTicks.LARVAE_QUESTING) * activationRate * (1 - shareOfActivationRate));
        var newInactiveNymphs = randomness.roundRandom(abundance.getStage(CohortStateTicks.NYMPHS_QUESTING) * activationRate * (1 - shareOfActivationRate));
        var newInactiveAdults = randomness.roundRandom(abundance.getStage(CohortStateTicks.ADULTS_QUESTING) * activationRate * (1 - shareOfActivationRate));

        var newInfectedQuestingNymphs = randomness.roundRandom(abundance.getStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED) * activationRate * shareOfActivationRate);
        var newInfectedInactiveLarvae = randomness.roundRandom(abundance.getStage(CohortStateTicks.LARVAE_QUESTING_INFECTED) * activationRate * (1 - shareOfActivationRate));
        var newInfectedInactiveNymphs = randomness.roundRandom(abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED) * activationRate * (1 - shareOfActivationRate));

        abundance.addToStage(CohortStateTicks.LARVAE_QUESTING, newQuestingLarvae - newInactiveLarvae);
        abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING, newQuestingNymphs - newInactiveNymphs);
        abundance.addToStage(CohortStateTicks.ADULTS_QUESTING, newQuestingAdults - newInactiveAdults);
        abundance.addToStage(CohortStateTicks.LARVAE_INACTIVE, newInactiveLarvae - newQuestingLarvae);
        abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE, newInactiveNymphs - newQuestingNymphs);
        abundance.addToStage(CohortStateTicks.ADULTS_INACTIVE, newInactiveAdults - newQuestingAdults);

        abundance.addToStage(CohortStateTicks.LARVAE_QUESTING_INFECTED, newInfectedQuestingLarvae - newInfectedInactiveLarvae);
        abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED, newInfectedQuestingNymphs - newInfectedInactiveNymphs);
        abundance.addToStage(CohortStateTicks.LARVAE_QUESTING_INFECTED, newInfectedInactiveLarvae - newInfectedQuestingLarvae);
        abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED, newInfectedInactiveNymphs - newInfectedQuestingNymphs);
    }
}
