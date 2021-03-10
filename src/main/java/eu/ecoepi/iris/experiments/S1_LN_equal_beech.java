package eu.ecoepi.iris.experiments;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

import eu.ecoepi.iris.Model;

public class S1_LN_equal_beech {
    public static void main(String[] args) throws Exception {
        var large = new Large();
        
        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);
            var abundanceReduction = Model.abundanceReductionDueToFructificationIndex(year);

            for (int ticks = 2; ticks <= 1000; ticks += 2) {
                for (int activationRate = 10; activationRate <= 30; activationRate += 1) {
                    var options = new Model.Options();

                    var name = String.format("%d_%d_%d", year, ticks, activationRate);

                    options.weather = weather;
                    options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                    options.initialInactiveLarvae = (int)(abundanceReduction * ticks);
                    options.initialInactiveNymphs = ticks;
                    options.initialInactiveAdults = ticks;

                    options.activationRate = activationRate / 1000.0f;

                    options.summary = true;

                    large.addTask(name, options);
                }
            }
        }

        large.waitForCompletion();
    }
}