package eu.ecoepi.iris;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import eu.ecoepi.iris.components.*;
import eu.ecoepi.iris.observers.CsvTimeSeriesWriter;
import eu.ecoepi.iris.systems.Diapause;
import eu.ecoepi.iris.systems.Dispersal;
import eu.ecoepi.iris.observers.ConsoleTimeSeriesWriter;
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
                .with(new ConsoleTimeSeriesWriter())
                .with(new CsvTimeSeriesWriter())
                .with(new Weather())
                .with(new Diapause())
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
                        Parameters.INITIAL_INACTIVE_LARVAE,
                        Parameters.INITIAL_INACTIVE_NYMPHS,
                        Parameters.INITIAL_INACTIVE_ADULTS,
                        Parameters.INITIAL_FED_LARVAE,
                        Parameters.INITIAL_FED_NYMPHS,
                        Parameters.INITIAL_FED_ADULTS,
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

                var precipitation = new Precipitation();
                editor.add(precipitation);
            }
        }


        // Create HeatMap chart to display the model landscape with nymph abundance values
        HeatMapChart heatChart =
                new HeatMapChartBuilder()
                        .width(1000)
                        .height(800)
                        .title("Tick Abundance Heatmap")
                        .xAxisTitle("x Coordinate")
                        .yAxisTitle("Y Coordinate")
                        .build();

        heatChart.getStyler().setChartTitleVisible(true);
        heatChart.getStyler().setPlotContentSize(1);
        heatChart.getStyler().setShowValue(true);
        heatChart.getStyler().setMax(250);
        heatChart.getStyler().setMin(0);
        Color[] rangeColors = {
                new Color(254,229,217),
                new Color(252,174,145),
                new Color(251,106,74),
                new Color(222,45,38),
                new Color(165,15,21)};

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
                    //heatData[i][j] = abundance.getNymphs();
                    heatData[i][j] = abundance.getNymphs() + abundance.getFedNymphs();
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

        List<Integer> yDataQuestingLarvae = new ArrayList<>();
        List<Integer> yDataQuestingNymphs = new ArrayList<>();
        List<Integer> yDataQuestingAdults = new ArrayList<>();

        List<Integer> yDataInactiveLarvae = new ArrayList<>();
        List<Integer> yDataInactiveNymphs = new ArrayList<>();
        List<Integer> yDataInactiveAdults = new ArrayList<>();

        List<Integer> yDataFedLarvae = new ArrayList<>();
        List<Integer> yDataFedNymphs = new ArrayList<>();
        List<Integer> yDataFedAdults = new ArrayList<>();

        List<Integer> yDataTotalLarvae = new ArrayList<>();
        List<Integer> yDataTotalNymphs = new ArrayList<>();
        List<Integer> yDataTotalAdults = new ArrayList<>();

        xDataDays.add(0);

        yDataQuestingLarvae.add(Parameters.INITIAL_LARVAE * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataQuestingNymphs.add(Parameters.INITIAL_NYMPHS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataQuestingAdults.add(Parameters.INITIAL_ADULTS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);

        yDataInactiveLarvae.add(Parameters.INITIAL_INACTIVE_LARVAE * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataInactiveNymphs.add(Parameters.INITIAL_INACTIVE_NYMPHS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataInactiveAdults.add(Parameters.INITIAL_INACTIVE_ADULTS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);

        yDataFedLarvae.add(Parameters.INITIAL_FED_LARVAE * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataFedNymphs.add(Parameters.INITIAL_FED_NYMPHS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataFedAdults.add(Parameters.INITIAL_FED_ADULTS * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);

        yDataTotalLarvae.add((Parameters.INITIAL_LARVAE + Parameters.INITIAL_INACTIVE_LARVAE + Parameters.INITIAL_FED_LARVAE) * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataTotalNymphs.add((Parameters.INITIAL_NYMPHS + Parameters.INITIAL_INACTIVE_NYMPHS + Parameters.INITIAL_FED_NYMPHS) * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);
        yDataTotalAdults.add((Parameters.INITIAL_ADULTS + Parameters.INITIAL_INACTIVE_ADULTS + Parameters.INITIAL_FED_ADULTS) * Parameters.GRID_WIDTH * Parameters.GRID_HEIGHT);

        XYSeries seriesQuestingLarvae = lineChartTotal.addSeries("Questing Larvae", xDataDays, yDataQuestingLarvae);
        seriesQuestingLarvae.setLineColor(new Color(49, 163, 84));
        seriesQuestingLarvae.setMarkerColor(new Color(49, 163, 84));

        XYSeries seriesQuestingNymphs = lineChartTotal.addSeries("Questing Nymphs", xDataDays, yDataQuestingNymphs);
        seriesQuestingNymphs.setLineColor(new Color(0, 0, 0));
        seriesQuestingNymphs.setMarkerColor(new Color(0, 0, 0));

        XYSeries seriesQuestingAdults = lineChartTotal.addSeries("Questing Adults", xDataDays, yDataQuestingAdults);
        seriesQuestingAdults.setLineColor(new Color(254, 153, 41));
        seriesQuestingAdults.setMarkerColor(new Color(254, 153, 41));

        XYSeries seriesInactiveLarvae = lineChartTotal.addSeries("Inactive Larvae", xDataDays, yDataInactiveLarvae);
        seriesInactiveLarvae.setLineColor(new Color(51, 95, 63));
        seriesInactiveLarvae.setMarkerColor(new Color(51, 95, 63));

        XYSeries seriesInactiveNymphs = lineChartTotal.addSeries("Inactive Nymphs", xDataDays, yDataInactiveNymphs);
        seriesInactiveNymphs.setLineColor(new Color(99, 98, 98));
        seriesInactiveNymphs.setMarkerColor(new Color(99, 98, 98));

        XYSeries seriesInactiveAdults = lineChartTotal.addSeries("Inactive Adults", xDataDays, yDataInactiveAdults);
        seriesInactiveAdults.setLineColor(new Color(239, 180, 110));
        seriesInactiveAdults.setMarkerColor(new Color(239, 180, 110));

        XYSeries seriesFedLarvae = lineChartTotal.addSeries("Fed Larvae", xDataDays, yDataFedLarvae);
        seriesFedLarvae.setLineColor(new Color(51, 77, 165));
        seriesFedLarvae.setMarkerColor(new Color(51, 77, 165));

        XYSeries seriesFedNymphs = lineChartTotal.addSeries("Fed Nymphs", xDataDays, yDataFedNymphs);
        seriesFedNymphs.setLineColor(new Color(51, 77, 165));
        seriesFedNymphs.setMarkerColor(new Color(51, 77, 165));

        XYSeries seriesFedAdults = lineChartTotal.addSeries("Fed Adults", xDataDays, yDataFedAdults);
        seriesFedAdults.setLineColor(new Color(51, 77, 165));
        seriesFedAdults.setMarkerColor(new Color(51, 77, 165));

        XYSeries seriesTotalLarvae = lineChartTotal.addSeries("Total Larvae", xDataDays, yDataTotalLarvae);
        seriesTotalLarvae.setLineColor(new Color(215, 27, 27));
        seriesTotalLarvae.setMarkerColor(new Color(215, 27, 27));

        XYSeries seriesTotalNymphs = lineChartTotal.addSeries("Total Nymphs", xDataDays, yDataTotalNymphs);
        seriesTotalNymphs.setLineColor(new Color(215, 27, 27));
        seriesTotalNymphs.setMarkerColor(new Color(215, 27, 27));

        XYSeries seriesTotalAdults = lineChartTotal.addSeries("Total Adults", xDataDays, yDataTotalAdults);
        seriesTotalAdults.setLineColor(new Color(215, 27, 27));
        seriesTotalAdults.setMarkerColor(new Color(215, 27, 27));

        // Show HeatMapChart
        final SwingWrapper<HeatMapChart> swHeat = new SwingWrapper<>(heatChart);
        swHeat.displayChart();

        // Show LineChart
        final SwingWrapper<XYChart> swLineTotal = new SwingWrapper<>(lineChartTotal);
        swLineTotal.displayChart();

        // Main loop
        for (var timeStep = world.getRegistered(TimeStep.class); timeStep.getCurrent() < Parameters.TIME_STEPS; timeStep.increment()) {
            world.process();

            Thread.sleep(80);

            int abundanceSumLarvae = 0;
            int abundanceSumNymphs = 0;
            int abundanceSumAdults = 0;

            int abundanceSumInactiveLarvae = 0;
            int abundanceSumInactiveNymphs = 0;
            int abundanceSumInactiveAdults = 0;

            int abundanceSumFedLarvae = 0;
            int abundanceSumFedNymphs = 0;
            int abundanceSumFedAdults = 0;

            int[][] heatData = new int[xData.length][yData.length];
            for (int i = 0; i < xData.length; i++) {
                for (int j = 0; j < yData.length; j++) {
                    var entityId = index.lookUp(new Position(i, j));
                    var abundance = world.getEntity(entityId.get()).getComponent(TickAbundance.class);
                    //heatData[i][j] = abundance.getNymphs();
                    heatData[i][j] = abundance.getNymphs() + abundance.getFedNymphs();
                    abundanceSumLarvae += abundance.getLarvae();
                    abundanceSumNymphs += abundance.getNymphs();
                    abundanceSumAdults += abundance.getAdults();
                    abundanceSumInactiveLarvae += abundance.getInactiveLarvae();
                    abundanceSumInactiveNymphs += abundance.getInactiveNymphs();
                    abundanceSumInactiveAdults += abundance.getInactiveAdults();
                    abundanceSumFedLarvae += abundance.getFedLarvae();
                    abundanceSumFedNymphs += abundance.getFedNymphs();
                    abundanceSumFedAdults += abundance.getFedAdults();
                }
            }

            xDataDays.add(timeStep.getCurrent());

            yDataQuestingLarvae.add(abundanceSumLarvae);
            yDataQuestingNymphs.add(abundanceSumNymphs);
            yDataQuestingAdults.add(abundanceSumAdults);

            yDataInactiveLarvae.add(abundanceSumInactiveLarvae);
            yDataInactiveNymphs.add(abundanceSumInactiveNymphs);
            yDataInactiveAdults.add(abundanceSumInactiveAdults);

            yDataFedLarvae.add(abundanceSumFedLarvae);
            yDataFedNymphs.add(abundanceSumFedNymphs);
            yDataFedAdults.add(abundanceSumFedAdults);

            yDataTotalLarvae.add(abundanceSumLarvae + abundanceSumFedLarvae + abundanceSumInactiveLarvae);
            yDataTotalNymphs.add(abundanceSumNymphs + abundanceSumFedNymphs + abundanceSumInactiveNymphs);
            yDataTotalAdults.add(abundanceSumAdults + abundanceSumFedAdults + abundanceSumInactiveAdults);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    heatChart.updateSeries("Basic HeatMap", xData, yData, heatData);
                    //lineChartTotal.updateXYSeries("Questing Larvae", xDataDays, yDataQuestingLarvae, null);
                    lineChartTotal.updateXYSeries("Questing Nymphs", xDataDays, yDataQuestingNymphs, null);
                    //lineChartTotal.updateXYSeries("Questing Adults", xDataDays, yDataQuestingAdults, null);
                    //lineChartTotal.updateXYSeries("Inactive Larvae", xDataDays, yDataInactiveLarvae, null);
                    //lineChartTotal.updateXYSeries("Inactive Nymphs", xDataDays, yDataInactiveNymphs, null);
                    //lineChartTotal.updateXYSeries("Inactive Adults", xDataDays, yDataInactiveAdults, null);
                    //lineChartTotal.updateXYSeries("Fed Larvae", xDataDays, yDataFedLarvae, null);
                    //lineChartTotal.updateXYSeries("Fed Nymphs", xDataDays, yDataFedNymphs, null);
                    //lineChartTotal.updateXYSeries("Fed Adults", xDataDays, yDataFedAdults, null);
                    //lineChartTotal.updateXYSeries("Total Larvae", xDataDays, yDataTotalLarvae, null);
                    //lineChartTotal.updateXYSeries("Total Nymphs", xDataDays, yDataTotalNymphs, null);
                    //lineChartTotal.updateXYSeries("Total Adults", xDataDays, yDataTotalAdults, null);

                    swHeat.repaintChart();
                    swLineTotal.repaintChart();
                    try {
                        BitmapEncoder.saveBitmap(heatChart, "./output/heatmap_" + timeStep.getCurrent(), BitmapEncoder.BitmapFormat.PNG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
