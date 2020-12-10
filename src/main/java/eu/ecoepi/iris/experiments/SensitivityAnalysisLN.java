package eu.ecoepi.iris.experiments;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

import eu.ecoepi.iris.Model;

public class SensitivityAnalysisLN {
    public static void main(String[] args) throws Exception {
                var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var tasks = new ExecutorCompletionService(executor);
        var todo = 0;
        var done = 0;

        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);
            var abundanceReduction = Model.abundanceReductionDueToFructificationIndex(year);

            for (int larvae = 5; larvae <= 500; larvae += 5) {
                for (int nymphs = 5; nymphs <= 500; nymphs += 5) {
                    for (int activationRate = 2; activationRate <= 8; activationRate += 1) {
                        for (int startLarvaeQuesting = 0; startLarvaeQuesting <= 150; startLarvaeQuesting += 50) {
                            var options = new Model.Options();

                            var name = String.format("%d_%d_%d_%d_%d", year, larvae, nymphs,
                                    startLarvaeQuesting, (int) (100.0f * activationRate));

                            options.weather = weather;
                            options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                            options.initialLarvae = (int) (abundanceReduction * larvae);
                            options.initialNymphs = nymphs;
                            options.initialAdults = 150;

                            options.activationRate = (activationRate / 100.0f);

                            options.startLarvaeQuesting = startLarvaeQuesting;

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