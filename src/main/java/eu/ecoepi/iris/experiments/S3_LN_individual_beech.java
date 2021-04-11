package eu.ecoepi.iris.experiments;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

import eu.ecoepi.iris.Model;
import eu.ecoepi.iris.resources.Parameters;

public class S3_LN_individual_beech {
    public static void main(String[] args) throws Exception {
        var large = new Large();

        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);
            var abundanceReduction = Parameters.abundanceReductionDueToFructificationIndex(year);

            for (int larvae = 0; larvae <= 1000; larvae += 20) {
                for (int nymphs = 10; nymphs <= 1000; nymphs += 10) {
                    for (int activationRate = 10; activationRate <= 30; activationRate += 1) {
                        var options = new Model.Options();

                        var name = String.format("%d_%d_%d_%d", year, larvae, nymphs, activationRate);

                        options.weather = weather;
                        options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                        options.initialInactiveLarvae = (int) (abundanceReduction * larvae);
                        options.initialInactiveNymphs = nymphs;
                        options.initialInactiveAdults = 150;

                        options.activationRate = activationRate / 1000.0f;

                        options.summary = true;

                        large.addTask(name, options);
                    }
                }
            }
        }

        large.waitForCompletion();
    }
}