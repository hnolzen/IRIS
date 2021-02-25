package eu.ecoepi.iris.experiments;

import eu.ecoepi.iris.Model;

public class AdHocManyYears {
    public static void main(String[] args) throws Exception {
        for (int year = 2009; year <= 2018; year++) {
            var weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);

            for (int ticks = 10; ticks <= 1000; ticks += 10) {
                var options = new Model.Options();
                var name = String.format("%d_%d", year, ticks);

                options.weather = weather;
                options.output = String.format("./output/sensitivity_analysis_%s.csv", name);

                options.initialInactiveLarvae = ticks;
                options.initialInactiveNymphs = ticks;
                options.initialInactiveAdults = ticks;

                options.summary = true;

                Model.run(options);
            }
        }
        System.out.println("Simulation run finished.");
    }
}
