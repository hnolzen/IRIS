import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy.optimize import curve_fit
import os

file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")

COLOR_BOUNDARY_LINE = "#969696"
COLOR_DWD = "#ff8c00"
COLOR_CLIMATE_FUTURE = "#3182bd95"
COLOR_CLIMATE_PAST = "#8226de"
COLOR_MODEL = "#feb24c"

SUMMER_START = 151   # 01 Jun 
SUMMER_END = 242     # 31 Aug

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


def get_data(y_start, y_end, obs, x_axis_type):
    year_values = []
    min_activity_values = []
    model_values = []
    x_axis_values = []

    for model in models:
        dirname = os.path.abspath(main_dir + "/output/" + observers[obs] + "/" + model)

        for file in os.listdir(dirname):
            filename = os.path.join(dirname, file)
            file_params = filename.split(".csv")[0].split("_")
            year = int(file_params[-1])

            if year in range(y_start, y_end + 1):
                try:
                    df = pd.read_csv(filename, header=0)

                    df_summer = df.iloc[SUMMER_START:SUMMER_END]

                    min_activity = df_summer["nymphs_questing"].min()

                    year_values.append(year)
                    min_activity_values.append(min_activity)
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
            "min_activity": min_activity_values,
            "model": model_values,
            f"{x_axis.get(x_axis_type)[0]}": x_axis_values,
        }
    )
    return summary


def reg_function(x0):
    return lambda x, a, b: a * np.exp(-b * (x - x0))


year_start = 1949
year_end = 2098
x_axis_type = 1
observer = 5
output_format = "png"
with_fits = False
with_past_climate_years = True
with_color_model = False
color_model = 1


data = get_data(year_start, year_end, observer, x_axis_type)

df_dwd = data[data["model"] == "DWD"]
df_clm = data[data["model"] != "DWD"]
df_clm_future = df_clm[df_clm["year"] > 2020]
df_clm_past = df_clm[df_clm["year"] < 2020]

x_ax_type = x_axis.get(x_axis_type)[0]

x_dwd = df_dwd[x_ax_type]
y_dwd = df_dwd["min_activity"]

x_clm_future = df_clm_future[x_ax_type]
y_clm_future = df_clm_future["min_activity"]

if with_past_climate_years:
    x_clm_past = df_clm_past[x_ax_type]
    y_clm_past = df_clm_past["min_activity"]

if with_color_model:
    df_clm_future_model = df_clm_future[df_clm_future["model"] == models[color_model]]
    x_clm_future_model = df_clm_future_model[x_ax_type]
    y_clm_future_model = df_clm_future_model["min_activity"]


fig, ax = plt.subplots()

ax.scatter(
    x_clm_future,
    y_clm_future,
    label="Climate data (2021 - 2099)",
    marker=".",
    s=20,
    facecolors="none",
    color=COLOR_CLIMATE_FUTURE,
)

if with_color_model:
    ax.scatter(
        x_clm_future_model,
        y_clm_future_model,
        label=models[color_model],
        marker=".",
        s=20,
        color=COLOR_MODEL,
    )

if with_past_climate_years:
    ax.scatter(
        x_clm_past,
        y_clm_past,
        label="Climate data (1971 - 2020)",
        marker=".",
        s=20,
        facecolors="none",
        color=COLOR_CLIMATE_PAST,
    )

ax.scatter(
    x_dwd,
    y_dwd,
    label="DWD data (1949 - 2020)",
    marker="x",
    s=15,
    color=COLOR_DWD,
)

if with_fits:
    popt_dwd, pcov_dwd = curve_fit(
        reg_function(1949), x_dwd, y_dwd
    )

    popt_climate, pcov_climate = curve_fit(
        reg_function(2021),
        x_clm_future,
        y_clm_future,
    )

    plt.plot(
        x_clm_future.unique(),
        reg_function(2021)(x_clm_future.unique(), *popt_climate),
        "r-",
        c="#525252",
        lw=1.0,
        ls="-",
        #label="fit_climate_projection: a=%5.2f, b=%5.2f" % tuple(popt_climate),
    )
    
    plt.plot(
        x_dwd,
        reg_function(1949)(x_dwd, *popt_dwd),
        "r-",
        c="black",
        lw=1.0,
        ls="--",
        #label="fit_dwd: a=%5.2f, b=%5.2f" % tuple(popt_dwd),
    )

plt.axvline(x=2020.5, color=COLOR_BOUNDARY_LINE, ls="--", lw=0.8)

lower_x_lim = x_axis.get(x_axis_type)[2]
upper_x_lim = x_axis.get(x_axis_type)[3]
ax.set_xlim(lower_x_lim, upper_x_lim)

ax.set_ylim(0, 4500)

x_ax_label = x_axis.get(x_axis_type)[1]
ax.set_xlabel(x_ax_label, fontsize=10, fontweight="bold")
ax.set_ylabel("Minimum number of questing nymphs", fontsize=10, fontweight="bold")

plt.title("Minimum questing activity in summer (JJA)",fontweight="bold",fontsize=8)

plt.legend(fontsize=6)

plt.tight_layout()

plt.savefig(
    f"min_summer_activity_nymphs_{x_ax_type}.{output_format}",
    dpi=400,
)
plt.close(fig)
