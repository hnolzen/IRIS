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
                var nextStageNymphs = randomness.roundRandom((float) abundance.getEngorgedLarvae() / (float) remainingDays);
                var nextStageInfectedNymphs = randomness.roundRandom((float) abundance.getInfectedEngorgedLarvae() / (float) remainingDays);
                abundance.addInactiveNymphs(nextStageNymphs);
                abundance.addEngorgedLarvae(-nextStageNymphs);
                abundance.addInfectedInactiveNymphs(nextStageInfectedNymphs);
                abundance.addInfectedEngorgedLarvae(-nextStageInfectedNymphs);
            }

            if (currentTimeStep < Parameters.END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS) {
                var remainingDays = Parameters.END_OF_DEVELOPMENT_NYMPHS_TO_ADULTS - currentTimeStep;
                var nextStageAdults = randomness.roundRandom(
                        ((float) abundance.getEngorgedNymphs() + (float) abundance.getInfectedEngorgedNymphs()) / (float) remainingDays);
                abundance.addInactiveAdults(nextStageAdults);
                abundance.addEngorgedNymphs(-nextStageAdults);
            }

            if (currentTimeStep < Parameters.END_OF_DEVELOPMENT_ADULTS_TO_LARVAE) {
                var remainingDays = Parameters.END_OF_DEVELOPMENT_ADULTS_TO_LARVAE - currentTimeStep;
                var nextStageLarvae = randomness.roundRandom((float) abundance.getEngorgedAdults() / (float) remainingDays);
                abundance.addInactiveLarvae(nextStageLarvae);
                abundance.addEngorgedAdults(-nextStageLarvae);
            }
        }
    }

    private void freezing(TickAbundance abundance, Temperature temperature) {
        if (temperature.getMinTemperature() < Parameters.FREEZING_MIN_TEMP_WITHOUT_SNOW) {

            var frozenLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_QUESTING));
            var frozenNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_QUESTING));
            var frozenAdults = randomness.roundRandom(abundance.getAdults() * Parameters.FREEZING_RATE.get(CohortStateTicks.ADULTS_QUESTING));

            var frozenInfectedLarvae = randomness.roundRandom(abundance.getInfectedLarvae() * Parameters.FREEZING_RATE.get(CohortStateTicks.LARVAE_QUESTING_INFECTED));
            var frozenInfectedNymphs = randomness.roundRandom(abundance.getInfectedNymphs() * Parameters.FREEZING_RATE.get(CohortStateTicks.NYMPHS_QUESTING_INFECTED));

            abundance.addLarvae(-frozenLarvae);
            abundance.addNymphs(-frozenNymphs);
            abundance.addAdults(-frozenAdults);

            abundance.addInfectedLarvae(-frozenInfectedLarvae);
            abundance.addInfectedNymphs(-frozenInfectedNymphs);
        }
    }

    private void desiccation(TickAbundance abundance, Habitat habitat, Temperature temperature, Humidity humidity) {
        if (humidity.getRelativeHumidity() < Parameters.DESICCATION_MINIMAL_HUMIDITY &&
                temperature.getMeanTemperature() > Parameters.DESICCATION_MINIMAL_MEAN_TEMP) {

            var desiccatedLarvae = randomness.roundRandom(abundance.getLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedNymphs = randomness.roundRandom(abundance.getNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedAdults = randomness.roundRandom(abundance.getAdults() * Parameters.DESICCATION_RATE.get(habitat.getType()));

            var desiccatedInfectedLarvae = randomness.roundRandom(abundance.getInfectedLarvae() * Parameters.DESICCATION_RATE.get(habitat.getType()));
            var desiccatedInfectedNymphs = randomness.roundRandom(abundance.getInfectedNymphs() * Parameters.DESICCATION_RATE.get(habitat.getType()));

            abundance.addLarvae(-desiccatedLarvae);
            abundance.addNymphs(-desiccatedNymphs);
            abundance.addAdults(-desiccatedAdults);

            abundance.addInfectedLarvae(-desiccatedInfectedLarvae);
            abundance.addInfectedNymphs(-desiccatedInfectedNymphs);
        }
    }
}