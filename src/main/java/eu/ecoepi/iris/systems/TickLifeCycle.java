package eu.ecoepi.iris.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.CohortStateTicks;
import eu.ecoepi.iris.resources.Parameters;
import eu.ecoepi.iris.resources.Randomness;
import eu.ecoepi.iris.resources.TimeStep;
import eu.ecoepi.iris.components.*;

@All({TickAbundance.class, Habitat.class, Temperature.class, Humidity.class})
public class TickLifeCycle extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Habitat> habitatMapper;
    ComponentMapper<Temperature> temperatureMapper;
    ComponentMapper<Humidity> humidityMapper;

    @Wire
    TimeStep timestep;

    @Wire
    Randomness randomness;

    @Override
    protected void process(int entityId) {
        var abundance = abundanceMapper.get(entityId);
        var habitat = habitatMapper.get(entityId);
        var temperature = temperatureMapper.get(entityId);
        var humidity = humidityMapper.get(entityId);

        development(abundance);
        desiccation(abundance, habitat, temperature, humidity);
        freezing(abundance, temperature);

    }

    private void development(TickAbundance abundance) {
        var currentTimeStep = timestep.getCurrent();

        if (currentTimeStep >= Parameters.BEGIN_OF_DEVELOPMENT) {

            if (currentTimeStep < Parameters.END_OF_DEVELOPMENT_LARVAE_TO_NYMPHS) {
                var remainingDays = Parameters.END_OF_DEVELOPMENT_LARVAE_TO_NYMPHS - currentTimeStep;
                var nextStageNymphs = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_ENGORGED) / (float) remainingDays);
                var nextStageInfectedNymphs = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_ENGORGED_INFECTED) / (float) remainingDays);
                abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE, nextStageNymphs);
                abundance.addToStage(CohortStateTicks.LARVAE_ENGORGED, -nextStageNymphs);
                abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED, nextStageInfectedNymphs);
                abundance.addToStage(CohortStateTicks.LARVAE_ENGORGED_INFECTED, -nextStageInfectedNymphs);
            }

            /*
            The infection dynamics is currently not applied to adult ticks as the model only
            simulates a single year and we are primarily interested in infected nymphs.
            */
            if (currentTimeStep < Parameters.END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS) {
                var remainingDays = Parameters.END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS - currentTimeStep;
                var nextStageAdults = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_ENGORGED) / (float) remainingDays);
                var nextStageAdultsFromInfectedEngorgedNymphs =
                        randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_ENGORGED_INFECTED) / (float) remainingDays);
                abundance.addToStage(CohortStateTicks.ADULTS_INACTIVE, nextStageAdults + nextStageAdultsFromInfectedEngorgedNymphs);
                abundance.addToStage(CohortStateTicks.NYMPHS_ENGORGED, -nextStageAdults);
                abundance.addToStage(CohortStateTicks.NYMPHS_ENGORGED_INFECTED, -nextStageAdultsFromInfectedEngorgedNymphs);
            }

            if (currentTimeStep < Parameters.END_OF_DEVELOPMENT_ADULTS_TO_LARVAE) {
                var remainingDays = Parameters.END_OF_DEVELOPMENT_ADULTS_TO_LARVAE - currentTimeStep;
                var nextStageLarvae = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.ADULTS_ENGORGED) / (float) remainingDays);
                abundance.addToStage(CohortStateTicks.LARVAE_INACTIVE, nextStageLarvae);
                abundance.addToStage(CohortStateTicks.ADULTS_ENGORGED, -nextStageLarvae);
            }
        }
    }

    private void freezing(TickAbundance abundance, Temperature temperature) {
        if (temperature.getMinTemperature() < Parameters.FREEZING_MIN_TEMP_WITHOUT_SNOW) {

            var frozenInactiveLarvae =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_INACTIVE) * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_INACTIVE));
            var frozenInactiveNymphs =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_INACTIVE) * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_INACTIVE));
            var frozenInactiveAdults =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.ADULTS_INACTIVE) * Parameters.FREEZING_RATE.get(CohortStateTicks.ADULTS_INACTIVE));

            var frozenQuestingLarvae =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_QUESTING) * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_QUESTING));
            var frozenQuestingNymphs =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_QUESTING) * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_QUESTING));
            var frozenQuestingAdults =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.ADULTS_QUESTING) * Parameters.FREEZING_RATE.get(CohortStateTicks.ADULTS_QUESTING));

            var frozenInfectedInactiveLarvae =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_INACTIVE_INFECTED) * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_INACTIVE_INFECTED));
            var frozenInfectedInactiveNymphs =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED) * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_INACTIVE_INFECTED));

            var frozenInfectedQuestingLarvae =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_QUESTING_INFECTED) * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_QUESTING_INFECTED));
            var frozenInfectedQuestingNymphs =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED) * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_QUESTING_INFECTED));

            abundance.addToStage(CohortStateTicks.LARVAE_INACTIVE, -frozenInactiveLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE, -frozenInactiveNymphs);
            abundance.addToStage(CohortStateTicks.ADULTS_INACTIVE, -frozenInactiveAdults);

            abundance.addToStage(CohortStateTicks.LARVAE_QUESTING, -frozenQuestingLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING, -frozenQuestingNymphs);
            abundance.addToStage(CohortStateTicks.ADULTS_QUESTING, -frozenQuestingAdults);

            abundance.addToStage(CohortStateTicks.LARVAE_INACTIVE_INFECTED, -frozenInfectedInactiveLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_INACTIVE_INFECTED, -frozenInfectedInactiveNymphs);

            abundance.addToStage(CohortStateTicks.LARVAE_QUESTING_INFECTED, -frozenInfectedQuestingLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED, -frozenInfectedQuestingNymphs);
        }
    }

    private void desiccation(TickAbundance abundance, Habitat habitat, Temperature temperature, Humidity humidity) {
        if (humidity.getRelativeHumidity() < Parameters.DESICCATION_MINIMAL_HUMIDITY &&
                temperature.getMeanTemperature() > Parameters.DESICCATION_MINIMAL_MEAN_TEMP) {

            var desiccatedLarvae = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_QUESTING) * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedNymphs = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_QUESTING) * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedAdults = randomness.roundRandom((float) abundance.getStage(CohortStateTicks.ADULTS_QUESTING) * Parameters.DESICCATION_RATE.get(habitat.getType()));

            var desiccatedInfectedLarvae =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.LARVAE_QUESTING_INFECTED) * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedInfectedNymphs =
                    randomness.roundRandom((float) abundance.getStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED) * Parameters.DESICCATION_RATE.get(habitat.getType()));

            abundance.addToStage(CohortStateTicks.LARVAE_QUESTING, -desiccatedLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING, -desiccatedNymphs);
            abundance.addToStage(CohortStateTicks.ADULTS_QUESTING, -desiccatedAdults);

            abundance.addToStage(CohortStateTicks.LARVAE_QUESTING_INFECTED, -desiccatedInfectedLarvae);
            abundance.addToStage(CohortStateTicks.NYMPHS_QUESTING_INFECTED, -desiccatedInfectedNymphs);
        }
    }
}