package eu.ecoepi.iris.observers;


import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import eu.ecoepi.iris.LifeCycleStage;
import eu.ecoepi.iris.Parameters;
import eu.ecoepi.iris.TimeStep;
import eu.ecoepi.iris.components.Position;
import eu.ecoepi.iris.components.TickAbundance;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@All({TickAbundance.class, Position.class})
public class XChartTimeSeriesPlotter extends IteratingSystem {

    ComponentMapper<TickAbundance> abundanceMapper;
    ComponentMapper<Position> positionMapper;

    HeatMapChart heatChart;
    XYChart lineChartTotal;

    int abundanceSumLarvae;
    int abundanceSumNymphs;
    int abundanceSumAdults;

    int abundanceSumInactiveLarvae;
    int abundanceSumInactiveNymphs;
    int abundanceSumInactiveAdults;

    int abundanceSumFedLarvae;
    int abundanceSumFedNymphs;
    int abundanceSumFedAdults;

    int[] xData;
    int[] yData;
    int[][] heatData;

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

    XYSeries seriesQuestingLarvae;
    XYSeries seriesQuestingNymphs;
    XYSeries seriesQuestingAdults;
    XYSeries seriesInactiveLarvae;
    XYSeries seriesInactiveNymphs;
    XYSeries seriesInactiveAdults;
    XYSeries seriesFedLarvae;
    XYSeries seriesFedNymphs;
    XYSeries seriesFedAdults;
    XYSeries seriesTotalLarvae;
    XYSeries seriesTotalNymphs;
    XYSeries seriesTotalAdults;

    final SwingWrapper<HeatMapChart> swHeat;
    final SwingWrapper<XYChart> swLineTotal;

    @Wire
    TimeStep timeStep;

    public XChartTimeSeriesPlotter() {

        heatChart = new HeatMapChartBuilder()
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
                new Color(254, 229, 217),
                new Color(252, 174, 145),
                new Color(251, 106, 74),
                new Color(222, 45, 38),
                new Color(165, 15, 21)};

        heatChart.getStyler().setRangeColors(rangeColors);
        heatChart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        heatChart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        xData = new int[Parameters.GRID_WIDTH];
        yData = new int[Parameters.GRID_HEIGHT];
        heatData = new int[xData.length][yData.length];

        for (int i = 0; i < xData.length; i++) {
            xData[i] = i;
        }

        for (int j = 0; j < yData.length; j++) {
            yData[j] = j;
        }

        heatChart.addSeries("Basic HeatMap", xData, yData, heatData);

        // Create Line charts
        lineChartTotal = new XYChartBuilder()
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

        seriesQuestingLarvae = lineChartTotal.addSeries("Questing Larvae", xDataDays, yDataQuestingLarvae);
        seriesQuestingLarvae.setLineColor(new Color(49, 163, 84));
        seriesQuestingLarvae.setMarkerColor(new Color(49, 163, 84));

        seriesQuestingNymphs = lineChartTotal.addSeries("Questing Nymphs", xDataDays, yDataQuestingNymphs);
        seriesQuestingNymphs.setLineColor(new Color(0, 0, 0));
        seriesQuestingNymphs.setMarkerColor(new Color(0, 0, 0));

        seriesQuestingAdults = lineChartTotal.addSeries("Questing Adults", xDataDays, yDataQuestingAdults);
        seriesQuestingAdults.setLineColor(new Color(254, 153, 41));
        seriesQuestingAdults.setMarkerColor(new Color(254, 153, 41));

        seriesInactiveLarvae = lineChartTotal.addSeries("Inactive Larvae", xDataDays, yDataInactiveLarvae);
        seriesInactiveLarvae.setLineColor(new Color(51, 95, 63));
        seriesInactiveLarvae.setMarkerColor(new Color(51, 95, 63));

        seriesInactiveNymphs = lineChartTotal.addSeries("Inactive Nymphs", xDataDays, yDataInactiveNymphs);
        seriesInactiveNymphs.setLineColor(new Color(99, 98, 98));
        seriesInactiveNymphs.setMarkerColor(new Color(99, 98, 98));

        seriesInactiveAdults = lineChartTotal.addSeries("Inactive Adults", xDataDays, yDataInactiveAdults);
        seriesInactiveAdults.setLineColor(new Color(239, 180, 110));
        seriesInactiveAdults.setMarkerColor(new Color(239, 180, 110));

        seriesFedLarvae = lineChartTotal.addSeries("Fed Larvae", xDataDays, yDataFedLarvae);
        seriesFedLarvae.setLineColor(new Color(51, 77, 165));
        seriesFedLarvae.setMarkerColor(new Color(51, 77, 165));

        seriesFedNymphs = lineChartTotal.addSeries("Fed Nymphs", xDataDays, yDataFedNymphs);
        seriesFedNymphs.setLineColor(new Color(51, 77, 165));
        seriesFedNymphs.setMarkerColor(new Color(51, 77, 165));

        seriesFedAdults = lineChartTotal.addSeries("Fed Adults", xDataDays, yDataFedAdults);
        seriesFedAdults.setLineColor(new Color(51, 77, 165));
        seriesFedAdults.setMarkerColor(new Color(51, 77, 165));

        seriesTotalLarvae = lineChartTotal.addSeries("Total Larvae", xDataDays, yDataTotalLarvae);
        seriesTotalLarvae.setLineColor(new Color(215, 27, 27));
        seriesTotalLarvae.setMarkerColor(new Color(215, 27, 27));

        seriesTotalNymphs = lineChartTotal.addSeries("Total Nymphs", xDataDays, yDataTotalNymphs);
        seriesTotalNymphs.setLineColor(new Color(215, 27, 27));
        seriesTotalNymphs.setMarkerColor(new Color(215, 27, 27));

        seriesTotalAdults = lineChartTotal.addSeries("Total Adults", xDataDays, yDataTotalAdults);
        seriesTotalAdults.setLineColor(new Color(215, 27, 27));
        seriesTotalAdults.setMarkerColor(new Color(215, 27, 27));

        swHeat = new SwingWrapper<>(heatChart);
        swLineTotal = new SwingWrapper<>(lineChartTotal);

        swHeat.displayChart(); // Show HeatMapChart
        swLineTotal.displayChart(); // Show LineChart
    }


    @Override
    protected void begin() {
        abundanceSumLarvae = 0;
        abundanceSumNymphs = 0;
        abundanceSumAdults = 0;

        abundanceSumInactiveLarvae = 0;
        abundanceSumInactiveNymphs = 0;
        abundanceSumInactiveAdults = 0;

        abundanceSumFedLarvae = 0;
        abundanceSumFedNymphs = 0;
        abundanceSumFedAdults = 0;

        heatData = new int[xData.length][yData.length];
    }

    @Override
    protected void process(int entityId) {

        var abundance = abundanceMapper.get(entityId);
        var position = positionMapper.get(entityId);
        heatData[position.getX()][position.getY()] = abundance.getNymphs() + abundance.getFedNymphs();
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

    @Override
    protected void end() {
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

        SwingUtilities.invokeLater(() -> {
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
        });
    }
}
