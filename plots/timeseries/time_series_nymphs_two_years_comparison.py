import os
import pandas as pd
import matplotlib.pyplot as plt
from scipy import signal


file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")
out_dir = os.path.abspath(main_dir + "/output/")

GRID_CELLS = 144
LINE_COLOR_NYMPHS_YEAR_1 = "#3182bd"
LINE_COLOR_NYMPHS_INFECTED_YEAR_1 = "#f21f00"
FILL_COLOR_NYMPHS_YEAR_1 = "#78bee3"
FILL_COLOR_INFECTED_YEAR_1 = "#ff8370"

LINE_COLOR_NYMPHS_YEAR_2 = "#41ab5d"
LINE_COLOR_NYMPHS_INFECTED_YEAR_2 = "#f2007d"
FILL_COLOR_NYMPHS_YEAR_2 = "#5cd67c"
FILL_COLOR_INFECTED_YEAR_2 = "#ff4faa"


models = {
    0: "DWD",
    1: "CCCma-CanESM2_rcp85_r1i1p1_CLMcom-CCLM4-8-17_v1",
    2: "CCCma-CanESM2_rcp85_r1i1p1_GERICS-REMO2015_v1",
    3: "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_IPSL-WRF381P_v1",
    4: "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_KNMI-RACMO22E_v1",
    5: "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_SMHI-RCA4_v1",
    6: "MOHC-HadGEM2-ES_rcp85_r1i1p1_CNRM-ALADIN63_v1",
    7: "MOHC-HadGEM2-ES_rcp85_r1i1p1_DMI-HIRHAM5_v2",
    8: "MOHC-HadGEM2-ES_rcp85_r1i1p1_GERICS-REMO2015_v1",
    9: "MOHC-HadGEM2-ES_rcp85_r1i1p1_ICTP-RegCM4-6_v1",
    10: "MOHC-HadGEM2-ES_rcp85_r1i1p1_IPSL-WRF381P_v1",
    11: "MOHC-HadGEM2-ES_rcp85_r1i1p1_KNMI-RACMO22E_v2",
    12: "MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1",
    13: "MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1",
    14: "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1",
    15: "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1",
}

observers = {
    0: "CsvSummaryTimeSeriesWriter",
    1: "CsvSummaryTimeSeriesWriterHabitats",
    2: "CsvTimeSeriesWriter",
    3: "CsvTimeSeriesWriterNymphs",
    4: "CsvTimeSeriesWriterNymphsHabitats",
}

x_axis = {
    1: "",
    15: "Jan",
    31: "",
    46: "Feb",
    59: "",
    74: "Mar",
    90: "",
    105: "Apr",
    120: "",
    135: "May",
    151: "",
    166: "Jun",
    181: "",
    196: "Jul",
    212: "",
    227: "Aug",
    243: "",
    258: "Sep",
    273: "",
    288: "Oct",
    304: "",
    319: "Nov",
    334: "",
    349: "Dec",
    366: "",
}


def read_csv(year, input_model):
    input_dir = models[input_model]
    file_path = (
        out_dir + f"/{observers[observer]}/{input_dir}/iris_abundance_{year}.csv"
    )
    try:
        df = pd.read_csv(file_path, header=0)
        return df

    except Exception:
        print(file_path)
        if input_model == 0:
            if year > 2020:
                print("No observation data for years > 2020 available!")
        if input_model > 0:
            if year < 1971:
                print("No projection data for years < 1971 available!")
            if year == 2099:
                print("No projection data for year 2099 available!")
        print("------")


def plot_line(df_x, df_y, c_line, c_fill, l_name):
    if with_density:
        df_y = df_y.div(GRID_CELLS)

    if with_smoothing:
        df_y = signal.savgol_filter(df_y, 53, 3)
        plt.plot(df_x, df_y, lw=1.0, color=c_line, label=l_name)

        if with_color_fill:
            plt.fill_between(df_x, 0, df_y, color=c_fill, alpha=0.5)
    else:
        plt.plot(df_x, df_y, lw=1.0, color=c_line, label=l_name)

        if with_color_fill:
            plt.fill_between(df_x, 0, df_y, color=c_fill, alpha=0.5)


year_1 = 2092
year_2 = 2018
input_model_1 = 8
input_model_2 = 0
observer = 3
with_questing_nymphs = True
with_questing_nymphs_infected = False
with_density = True
with_color_fill = True
with_smoothing = True
output_format = "png"


data_y1 = read_csv(year_1, input_model_1)
data_y2 = read_csv(year_2, input_model_2)


fig, ax = plt.subplots(figsize=(8, 3))

for x_value in list(x_axis.keys())[2:24:2]:
    plt.axvline(x=x_value, color="#d9d9d9", ls="-", lw=0.5, alpha=0.5)

if with_questing_nymphs:
    plot_line(
        data_y1["tick"],
        data_y1["questing_nymphs"],
        LINE_COLOR_NYMPHS_YEAR_1,
        FILL_COLOR_NYMPHS_YEAR_1,
        f"Questing nymphs ({year_1})",
    )

    plot_line(
        data_y2["tick"],
        data_y2["questing_nymphs"],
        LINE_COLOR_NYMPHS_YEAR_2,
        FILL_COLOR_NYMPHS_YEAR_2,
        f"Questing nymphs ({year_2})",
    )

if with_questing_nymphs_infected:
    plot_line(
        data_y1["tick"],
        data_y1["questing_nymphs_infected"],
        LINE_COLOR_NYMPHS_INFECTED_YEAR_2,
        FILL_COLOR_INFECTED_YEAR_2,
        "Infected questing nymph ({year_1})",
    )

    plot_line(
        data_y2["tick"],
        data_y2["questing_nymphs_infected"],
        LINE_COLOR_NYMPHS_INFECTED_YEAR_2,
        FILL_COLOR_INFECTED_YEAR_2,
        "Infected questing nymphs ({year_2})",
    )

plt.ylim(0, 5000)
if with_density:
    plt.ylim(0, 50)

ax.set_xticks(list(x_axis.keys()))
ax.set_xticklabels(list(x_axis.values()), fontsize=10)

for tick in ax.xaxis.get_major_ticks()[1::2]:
    tick.tick1line.set_markersize(0)
    tick.tick2line.set_markersize(0)

ax.set_ylabel("Number of nymphs", fontsize=12)
if with_density:
    ax.set_ylabel("Nymphs per 100 $m^2$", fontsize=12)

ax.margins(x=0)
ax.margins(y=0)

plt.legend(loc="upper right", fontsize=10)
plt.tight_layout()

plt.savefig(
    f"time_series_nymphs_comparison_{year_1}_{year_2}.{output_format}",
    dpi=400,
    format=f"{output_format}",
)
plt.close(fig)
