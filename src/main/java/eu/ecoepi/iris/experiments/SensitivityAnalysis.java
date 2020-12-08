package eu.ecoepi.iris.experiments;

import java.util.ArrayList;
import java.util.Map;

import eu.ecoepi.iris.Model;

public class SensitivityAnalysis {
    public static void main(String[] args) throws Exception {
        Map<Integer, Integer> fructificationIndex = Map.of(
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
                
        var allOptions = new ArrayList<Model.Options>();
        
        for (int year = 2009; year <= 2018; year++) {
            float abundanceReduction;
            switch (fructificationIndex.get(year - 2)) {
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
                    
            for (int ticks = 5; ticks <= 500; ticks += 5) {
                for (float activationRate = 0.02f; activationRate <= 0.10f; activationRate += 0.02f) {
                    var options = new Model.Options();
                    
                    options.weather = String.format("./input/weather/dwd_regensburg/weather_%d.csv", year);
                    options.output = String.format("./output/sensitivity_analysis_%d_%d_%d.csv", year, ticks, (int)(100.0f * activationRate));
                    
                    options.initialLarvae = (int)(abundanceReduction * ticks);
                    options.initialNymphs = ticks;
                    options.initialAdults = ticks;
                    
                    options.activationRate = activationRate;
                    
                    allOptions.add(options);
                }
            }
        }
        
        for (var options : allOptions) {
            Model.run(options);
        }
    }
}