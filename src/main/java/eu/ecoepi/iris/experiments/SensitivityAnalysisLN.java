package eu.ecoepi.iris.experiments;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

import eu.ecoepi.iris.resources.Model;

public class SensitivityAnalysisLN {
    public static void main(String[] args) throws Exception {
                var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var tasks = new ExecutorCompletionService(executor);
        var todo = 0;
        var done = 0;

        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);
            var abundanceReduction = Model.abundanceReductionDueToFructificationIndex(year);

            for (int larvae = 0; larvae <= 1000; larvae += 20) {
                for (int nymphs = 0; nymphs <= 1000; nymphs += 20) {
                    for (int activationRate = 1; activationRate <= 25; activationRate += 1) {
                        for (int startLarvaeQuesting = 0; startLarvaeQuesting <= 150; startLarvaeQuesting += 50) {
                            var options = new Model.Options();

                            var name = String.format("%d_%d_%d_%d_%d", year, larvae, nymphs,
                                    startLarvaeQuesting, activationRate);

                            options.weather = weather;
                            options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                            options.initialLarvae = (int) (abundanceReduction * larvae);
                            options.initialNymphs = nymphs;
                            options.initialAdults = 150;

                            options.activationRate = activationRate / 1000.0f;

                            options.startLarvaeQuesting = startLarvaeQuesting;
                            
                            options.summary = true;

                            tasks.submit(() -> {
                                System.err.printf("Starting task %s...\n", name);

                                try {
                                    Model.run(options);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }, null);
                            todo++;
                        }
                    }
                }
            }
        }

        while (done < todo) {
            tasks.take().get();
            done++;

            System.err.printf("%d out of %d tasks finished.\n", done, todo);
        }

        executor.shutdown();
    }
}