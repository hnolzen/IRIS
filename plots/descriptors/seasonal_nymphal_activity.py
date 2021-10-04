import pandas as pd
import os
from scipy import stats
import matplotlib.pyplot as plt

file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")

GRID_CELLS = 144
COLOR_BOUNDARY_LINE = "#969696"

models = [
    "DWD",
    "CCCma-CanESM2_rcp85_r1i1p1_CLMcom-CCLM4-8-17_v1",
    "CCCma-CanESM2_rcp85_r1i1p1_GERICS-REMO2015_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_IPSL-WRF381P_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_KNMI-RACMO22E_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_SMHI-RCA4_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_CNRM-ALADIN63_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_DMI-HIRHAM5_v2",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_GERICS-REMO2015_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_ICTP-RegCM4-6_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_IPSL-WRF381P_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_KNMI-RACMO22E_v2",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1",
]

observers = {
    0: "CsvSummaryTimeSeriesWriter",
    1: "CsvSummaryTimeSeriesWriterHabitats",
    2: "CsvTimeSeriesWriter",
    3: "CsvTimeSeriesWriterNymphs",
    4: "CsvTimeSeriesWriterNymphsHabitats",
    5: "CsvTimeSeriesWriterInfection",
}

x_axis = {
    1: ["year", "Year", 1948, 2100],
    2: ["mean_annual", "Annual mean temperature (°C)", 5, 16],
    3: ["median_annual", "Annual median temperature (°C)", 5, 16],
}

seasons = {
    "Spring": [60, "#41ab5d"],
    "Summer": [150, "#fed976"],
    "Autumn": [240, "#993404"],
    "Winter": [334, "#74a9cf"],
}


def get_data(year_start, year_end, obs, x_axis_type):
    year_values = []
    season_values = []
    nymphs_values = []
    model_values = []
    x_axis_values = []

    for model in models:
        dirname = os.path.abspath(main_dir + "/output/" + observers[obs] + "/" + model)

        for file in os.listdir(dirname):
            filename = os.path.join(dirname, file)
            file_params = filename.split(".csv")[0].split("_")
            year = int(file_params[-1])

            if year in range(year_start, year_end + 1):
                try:
                    df = pd.read_csv(filename, header=0)

                    for k in seasons:
                        if k != "winter":
                            nymphs = df[
                                (df["tick"] >= seasons[k][0])
                                & (df["tick"] < seasons[k][0] + 1)
                            ]["questing_nymphs"].mean()
                        else:
                            nymphs = df[
                                (df["tick"] < 60) | (df["tick"] >= seasons[k][0] + 1)
                            ]["questing_nymphs"].mean()

                        if with_density:
                            nymphs /= GRID_CELLS

                        year_values.append(year)
                        season_values.append(k)
                        nymphs_values.append(nymphs)
                        model_values.append(model)

                        if x_axis_type == 1:
                            x_axis_values.append(year)

                        if x_axis_type == 2:
                            mean_annual = df["mean_temperature"].mean()
                            x_axis_values.append(mean_annual)

                        if x_axis_type == 3:
                            median_annual = df["mean_temperature"].median()
                            x_axis_values.append(median_annual)

                except pd.errors.EmptyDataError as ex_empty_data:
                    print("EmptyDataError: ", ex_empty_data, filename)

    summary = pd.DataFrame(
        {
            "year": year_values,
            "season": season_values,
            "nymphs": nymphs_values,
            "model": model_values,
            f"{x_axis.get(x_axis_type)[0]}": x_axis_values,
        }
    )
    return summary


year_start = 1949
year_end = 2098
x_axis_type = 2
observer = 3
output_format = "png"
with_density = True
with_fits = True

plot_data = get_data(year_start, year_end, observer, x_axis_type)


fig, ax = plt.subplots()

x_values = f"{x_axis.get(x_axis_type)[0]}"
x_label = f"{x_axis.get(x_axis_type)[1]}"

for i, season in enumerate(seasons):
    plot_data_season = plot_data[plot_data["season"] == season]

    plt.subplot(2, 2, i + 1)

    plt.scatter(
        plot_data_season[x_values],
        plot_data_season["nymphs"],
        marker="o",
        s=0.8,
        c=f"{seasons[season][1]}",
    )

    plt.ylim(0, 7000)
    if with_density:
        plt.ylim(0, 70)

    if x_axis_type == 1:
        plt.axvline(x=2020.5, color=COLOR_BOUNDARY_LINE, ls="--", lw=0.8)

    lower_x_lim = x_axis.get(x_axis_type)[2]
    upper_x_lim = x_axis.get(x_axis_type)[3]
    plt.xlim(lower_x_lim, upper_x_lim + 0.5)

    plt.tick_params(labelsize=8)

    plt.title(season, fontweight="bold", fontsize=8)

    if with_fits:
        res = stats.linregress(plot_data_season[x_values], plot_data_season["nymphs"])

        reg_line = res.intercept + res.slope * plot_data_season[x_values]

        plt.plot(
            plot_data_season[x_values],
            reg_line,
            c="black",
            linewidth=1.0,
            label="y={:.2f}x+{:.2f}".format(res.slope, res.intercept),
        )

    plt.xlabel(x_label, fontsize=8)

    plt.ylabel("Number of nymphs", fontsize=8)
    if with_density:
        plt.ylabel("Nymphs per 100 $m^2$", fontsize=8)

    plt.legend(fontsize=5)

plt.tight_layout()
plt.savefig(
    f"seasonal_nymphal_activity_{x_values}.{output_format}",
    dpi=400,
)
plt.close(fig)
