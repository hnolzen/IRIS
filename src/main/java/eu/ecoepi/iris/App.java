package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.AbundanceWriter;
import eu.ecoepi.iris.systems.Dispersal;
import eu.ecoepi.iris.systems.PrintAbundance;
import eu.ecoepi.iris.systems.TickLifeCycle;
import eu.ecoepi.iris.systems.Weather;
import org.knowm.xchart.*;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        var config = new WorldConfigurationBuilder()
                .with(new TickLifeCycle())
                .with(new Dispersal())
                .with(new PrintAbundance())
                .with(new AbundanceWriter())
                .with(new Weather())
                .build()
                .register(new SpatialIndex())
                .register(new TimeStep())
                .register(new Randomness());

        var world = new World(config);

        var index = world.getRegistered(SpatialIndex.class);

        for (int x = 0; x < Parameters.GRID_WIDTH; ++x) {
            Habitat.Type habitatType;

            if (x >= Parameters.BOUNDARY_PASTURE) {
                habitatType = Habitat.Type.PASTURE;
            } else if (x >= Parameters.BOUNDARY_ECOTONE) {
                habitatType = Habitat.Type.ECOTONE;
            } else {
                habitatType = Habitat.Type.WOOD;
            }

            for (int y = 0; y < Parameters.GRID_HEIGHT; ++y) {
                var entityId = world.create();
                var editor = world.edit(entityId);

                var position = new Position(x, y);
                editor.add(position);
                index.insert(position, entityId);

                var abundance = new TickAbundance(100, 100, 100, 0, 0, 0);
                editor.add(abundance);

                var habitat = new Habitat(habitatType);
                editor.add(habitat);

                var temperature = new Temperature();
                editor.add(temperature);

                var humidity = new Humidity();
                editor.add(humidity);

            }
        }

        // Create Chart
        HeatMapChart chart =
                new HeatMapChartBuilder().width(800).height(800).title("Heatmap").build();

        chart.getStyler().setPlotContentSize(1);
        chart.getStyler().setShowValue(true);

        int[] xData = new int[Parameters.GRID_WIDTH];
        int[] yData = new int[Parameters.GRID_HEIGHT];
        {
            int[][] heatData = new int[xData.length][yData.length];

            for (int i = 0; i < xData.length; i++) {
                xData[i] = i;
            }

            for (int j = 0; j < yData.length; j++) {
                yData[j] = j;
            }

            for (int i = 0; i < xData.length; i++) {
                for (int j = 0; j < yData.length; j++) {
                    var entityId = index.lookUp(new Position(i, j));
                    var abundance = world.getEntity(entityId.get()).getComponent(TickAbundance.class);
                    heatData[i][j] = abundance.getNymphs();
                }
            }
            chart.addSeries("Basic HeatMap", xData, yData, heatData);
        }
        // Show it
        final SwingWrapper<HeatMapChart> sw = new SwingWrapper<HeatMapChart>(chart);
        sw.displayChart();

        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();

            Thread.sleep(100);

            int[][] heatData = new int[xData.length][yData.length];
            for (int i = 0; i < xData.length; i++) {
                for (int j = 0; j < yData.length; j++) {
                    var entityId = index.lookUp(new Position(i, j));
                    var abundance = world.getEntity(entityId.get()).getComponent(TickAbundance.class);
                    heatData[i][j] = abundance.getNymphs();
                }
            }

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    chart.updateSeries("Basic HeatMap", xData, yData, heatData);
                    //chart.updateXYSeries("sine", data[0], data[1], null);
                    sw.repaintChart();
                    try {
                        BitmapEncoder.saveBitmap(chart, "./sample_chart_", BitmapEncoder.BitmapFormat.PNG);
                        //BitmapEncoder.saveBitmap(chart, "./sample_chart_" + TimeStep.getCurrent(), BitmapEncoder.BitmapFormat.PNG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
