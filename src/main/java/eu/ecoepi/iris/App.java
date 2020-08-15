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
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IRIS
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

                var abundance = new TickAbundance(
                        Parameters.INITIAL_LARVAE,
                        Parameters.INITIAL_NYMPHS,
                        Parameters.INITIAL_ADULTS,
                        Parameters.INITIAL_INFECTED_LARVAE,
                        Parameters.INITIAL_INFECTED_NYMPHS,
                        Parameters.INITIAL_INFECTED_ADULTS);
                editor.add(abundance);

                var habitat = new Habitat(habitatType);
                editor.add(habitat);

                var temperature = new Temperature();
                editor.add(temperature);

                var humidity = new Humidity();
                editor.add(humidity);

            }
        }

        // Create HeatMap chart to display the model landscape with nymph abundance values
        HeatMapChart heatChart =
                new HeatMapChartBuilder()
                        .width(800)
                        .height(800)
                        .title("Tick Abundance Heatmap")
                        .xAxisTitle("x Coordinate")
                        .yAxisTitle("Y Coordinate")
                        .build();

        heatChart.getStyler().setChartTitleVisible(true);
        heatChart.getStyler().setPlotContentSize(1);
        heatChart.getStyler().setShowValue(true);
        heatChart.getStyler().setMax(100);
        heatChart.getStyler().setMin(0);
        Color[] rangeColors = {
                new Color(247, 252, 185),
                new Color(173, 221, 142),
                new Color(49, 163, 84)};
        heatChart.getStyler().setRangeColors(rangeColors);
        heatChart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        heatChart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

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
            heatChart.addSeries("Basic HeatMap", xData, yData, heatData);
        }

        // Create Line charts
        XYChart lineChartTotal = new XYChartBuilder()
                .width(1000)
                .height(800)
                .title("Tick Population Abundance")
                .xAxisTitle("Time (days)")
                .yAxisTitle("Number of Individuals (-)")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        lineChartTotal.getStyler().setChartTitleVisible(true);
        lineChartTotal.getStyler().setChartTitleBoxBorderColor(Color.BLACK);
        lineChartTotal.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        lineChartTotal.getStyler().setPlotGridLinesColor(Color.LIGHT_GRAY);
        lineChartTotal.getStyler().setPlotBackgroundColor(Color.WHITE);
        lineChartTotal.getStyler().setPlotBorderColor(Color.BLACK);
        lineChartTotal.getStyler().setPlotBorderVisible(true);
        lineChartTotal.getStyler().setAxisTickMarksColor(Color.BLACK);
        lineChartTotal.getStyler().setAxisTickLabelsColor(Color.BLACK);
        lineChartTotal.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        List<Integer> xDataDays = new ArrayList<>();
        List<Integer> yDataLarvae = new ArrayList<>();
        List<Integer> yDataNymphs = new ArrayList<>();
        List<Integer> yDataAdults = new ArrayList<>();
        xDataDays.add(0);
        yDataLarvae.add(0);
        yDataNymphs.add(0);
        yDataAdults.add(0);

        XYSeries seriesLarvae = lineChartTotal.addSeries("Larvae", xDataDays, yDataLarvae);
        seriesLarvae.setLineColor(new Color(49, 163, 84));
        seriesLarvae.setMarkerColor(new Color(49, 163, 84));

        XYSeries seriesNymphs = lineChartTotal.addSeries("Nymphs", xDataDays, yDataNymphs);
        seriesNymphs.setLineColor(new Color(0, 0, 0));
        seriesNymphs.setMarkerColor(new Color(0, 0, 0));

        XYSeries seriesAdults = lineChartTotal.addSeries("Adults", xDataDays, yDataAdults);
        seriesAdults.setLineColor(new Color(254, 153, 41));
        seriesAdults.setMarkerColor(new Color(254, 153, 41));

        // Show HeatMapChart
        final SwingWrapper<HeatMapChart> swHeat = new SwingWrapper<>(heatChart);
        swHeat.displayChart();

        // Show LineChart
        final SwingWrapper<XYChart> swLineTotal = new SwingWrapper<>(lineChartTotal);
        swLineTotal.displayChart();

        // Main loop
        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();

            Thread.sleep(100);

            int abundanceSumLarvae = 0;
            int abundanceSumNymphs = 0;
            int abundanceSumAdults = 0;

            int[][] heatData = new int[xData.length][yData.length];
            for (int i = 0; i < xData.length; i++) {
                for (int j = 0; j < yData.length; j++) {
                    var entityId = index.lookUp(new Position(i, j));
                    var abundance = world.getEntity(entityId.get()).getComponent(TickAbundance.class);
                    heatData[i][j] = abundance.getNymphs();
                    abundanceSumLarvae += abundance.getLarvae();
                    abundanceSumNymphs += abundance.getNymphs();
                    abundanceSumAdults += abundance.getAdults();
                }
            }

            xDataDays.add(timeStep.getCurrent());
            yDataLarvae.add(abundanceSumLarvae);
            yDataNymphs.add(abundanceSumNymphs);
            yDataAdults.add(abundanceSumAdults);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    heatChart.updateSeries("Basic HeatMap", xData, yData, heatData);
                    lineChartTotal.updateXYSeries("Larvae", xDataDays, yDataLarvae, null);
                    lineChartTotal.updateXYSeries("Nymphs", xDataDays, yDataNymphs, null);
                    lineChartTotal.updateXYSeries("Adults", xDataDays, yDataAdults, null);
                    swHeat.repaintChart();
                    swLineTotal.repaintChart();
                    try {
                        BitmapEncoder.saveBitmap(heatChart, "./sample_chart_", BitmapEncoder.BitmapFormat.PNG);
                        //BitmapEncoder.saveBitmap(chart, "./sample_chart_" + TimeStep.getCurrent(), BitmapEncoder.BitmapFormat.PNG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
