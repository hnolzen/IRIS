package eu.ecoepi.iris.experiments;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;

import eu.ecoepi.iris.Model;

public class S2_LN_equal {
    public static void main(String[] args) throws Exception {
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var tasks = new ExecutorCompletionService(executor);
        var todo = 0;
        var done = 0;

        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);

            for (int ticks = 2; ticks <= 1000; ticks += 2) {
                for (int activationRate = 10; activationRate <= 30; activationRate += 1) {
                    var options = new Model.Options();

                    var name = String.format("%d_%d_%d", year, ticks, activationRate);

                    options.weather = weather;
                    options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                    options.initialInactiveLarvae = ticks;
                    options.initialInactiveNymphs = ticks;
                    options.initialInactiveAdults = ticks;

                    options.activationRate = activationRate / 1000.0f;

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

        while (done < todo) {
            tasks.take().get();
            done++;

            System.err.printf("%d out of %d tasks finished.\n", done, todo);
        }

        executor.shutdown();
    }
}