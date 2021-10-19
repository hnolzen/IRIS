import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy.stats import pearsonr
import os

file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")

COLOR_BOUNDARY_LINE = "#969696"
COLOR_DWD_FIRST = "#41ab5d"
COLOR_DWD_SECOND = "#47ff50"
COLOR_CLIMATE_FUTURE_FIRST = "#3182bd"
COLOR_CLIMATE_FUTURE_SECOND = "#47a3ff"
COLOR_CLIMATE_PAST_FIRST = "#deaa26"
COLOR_CLIMATE_PAST_SECOND = "#debf26"
COLOR_MODEL = "#feb24c"

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
    2: ["mean_annual", "Annual mean temperature (°C)", 2, 16],
    3: ["median_annual", "Annual median temperature (°C)", 2, 16],
}

y_axis = {
    1: "Jan. 01",
    16: "15",
    31: "Feb. 01",
    46: "15",
    60: "Mar. 01",
    75: "15",
    90: "Apr. 01",
    105: "15",
    120: "May. 01",
    135: "15",
    150: "Jun. 01",
    165: "15",
    180: "Jul. 01",
    195: "15",
    211: "Aug 01",
    226: "15",
    242: "Sep 01",
    257: "15",
    272: "Oct 01",
    287: "15",
    303: "Nov 01",
    318: "15",
    334: "Dec 01",
}


def get_data(y_start, y_end, obs, x_axis_type):
    year_values = []
    peak_values_first = []
    peak_values_second = []
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

                    df_first = df.iloc[0:180]
                    df_second = df.iloc[181:365]

                    df_max_inf_nymphs_first = df_first["nymphs_questing_inf"]
                    df_max_inf_nymphs_second = df_second["nymphs_questing_inf"]

                    time_peak_activity_first = df_max_inf_nymphs_first.idxmax()
                    time_peak_activity_second = df_max_inf_nymphs_second.idxmax()

                    year_values.append(year)
                    peak_values_first.append(time_peak_activity_first)
                    peak_values_second.append(time_peak_activity_second)
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
            "peak_time_first": peak_values_first,
            "peak_time_second": peak_values_second,
            "model": model_values,
            f"{x_axis.get(x_axis_type)[0]}": x_axis_values,
        }
    )
    return summary


year_start = 1949
year_end = 2099
x_axis_type = 1
observer = 5
output_format = "png"
with_fits = False
with_past_climate_years = False
with_color_model = False
color_model = 1


data = get_data(year_start, year_end, observer, x_axis_type)

df_dwd = data[data["model"] == "DWD"]
df_clm = data[data["model"] != "DWD"]
df_clm_future = df_clm[df_clm["year"] > 2020]
df_clm_past = df_clm[df_clm["year"] < 2020]

x_ax_type = x_axis.get(x_axis_type)[0]

x_dwd = df_dwd[x_ax_type]
y_dwd_first = df_dwd["peak_time_first"]
y_dwd_second = df_dwd["peak_time_second"]

x_clm_future = df_clm_future[x_ax_type]
y_clm_future_first = df_clm_future["peak_time_first"]
y_clm_future_second = df_clm_future["peak_time_second"]

if with_past_climate_years:
    x_clm_past = df_clm_past[x_ax_type]
    y_clm_past_first = df_clm_past["peak_time_first"]
    y_clm_past_second = df_clm_past["peak_time_second"]

if with_color_model:
    df_clm_future_model = df_clm_future[df_clm_future["model"] == models[color_model]]
    x_clm_future_model = df_clm_future_model[x_ax_type]
    y_clm_future_model_first = df_clm_future_model["peak_time_first"]
    y_clm_future_model_second = df_clm_future_model["peak_time_second"]

if with_fits:
    corr_dwd_first = round(pearsonr(x_dwd, y_dwd_first)[0], 2)
    corr_dwd_second = round(pearsonr(x_dwd, y_dwd_second)[0], 2)
    corr_clm_first = round(pearsonr(x_clm_future, y_clm_future_first)[0], 2)
    corr_clm = round(pearsonr(x_clm_future, y_clm_future_second)[0], 2)

    if with_past_climate_years:
        corr_clm_past_first = round(pearsonr(x_clm_past, y_clm_past_first)[0], 2)
        corr_clm_past_second = round(pearsonr(x_clm_past, y_clm_past_second)[0], 2)

    m_dwd_first, n_dwd_first = np.polyfit(x_dwd, y_dwd_first, 1)
    m_dwd_second, n_dwd_second = np.polyfit(x_dwd, y_dwd_second, 1)
    m_clm_future_first, n_clm_future_first = np.polyfit(
        x_clm_future, y_clm_future_first, 1
    )
    m_clm_future_second, n_clm_future_second = np.polyfit(
        x_clm_future, y_clm_future_second, 1
    )

    if with_past_climate_years:
        m_clm_past_first, n_clm_past_first = np.polyfit(x_clm_past, y_clm_past_first, 1)
        m_clm_past_second, n_clm_past_second = np.polyfit(
            x_clm_past, y_clm_past_second, 1
        )


fig, ax = plt.subplots()

ax.scatter(
    x_dwd,
    y_dwd_first,
    label="First peak (DWD data, 1949 - 2020)",
    marker="x",
    s=15,
    color=COLOR_DWD_FIRST,
)

ax.scatter(
    x_dwd,
    y_dwd_second,
    label="Second peak (DWD data, 1949 - 2020)",
    marker="x",
    s=15,
    color=COLOR_DWD_SECOND,
)

ax.scatter(
    x_clm_future,
    y_clm_future_first,
    label="First peak (Climate data, 2021 - 2099)",
    marker=".",
    s=20,
    facecolors="none",
    color=COLOR_CLIMATE_FUTURE_FIRST,
)


ax.scatter(
    x_clm_future,
    y_clm_future_second,
    label="Second peak (Climate data, 2021 - 2099)",
    marker=".",
    s=20,
    facecolors="none",
    color=COLOR_CLIMATE_FUTURE_SECOND,
)


if with_color_model:
    ax.scatter(
        x_clm_future_model,
        y_clm_future_model_first,
        label=models[color_model],
        marker=".",
        s=20,
        color=COLOR_MODEL,
    )

    ax.scatter(
        x_clm_future_model,
        y_clm_future_model_second,
        label=models[color_model],
        marker=".",
        s=20,
        color=COLOR_MODEL,
    )

if with_past_climate_years:
    ax.scatter(
        x_clm_past,
        y_clm_past_first,
        label="First peak (Climate data, 1971 - 2020)",
        marker=".",
        s=20,
        facecolors="none",
        color=COLOR_CLIMATE_PAST_FIRST,
    )

    ax.scatter(
        x_clm_past,
        y_clm_past_second,
        label="Second peak (Climate data, 1971 - 2020)",
        marker=".",
        s=20,
        facecolors="none",
        color=COLOR_CLIMATE_PAST_SECOND,
    )

if with_fits:
    fit_length = np.arange(0, x_axis.get(x_axis_type)[3])

    ax.plot(
        fit_length,
        n_dwd_first + m_dwd_first * fit_length,
        color=COLOR_DWD_FIRST,
        lw=0.75,
    )

    ax.plot(
        fit_length,
        n_clm_future_first + m_clm_future_first * fit_length,
        color=COLOR_CLIMATE_FUTURE_FIRST,
        lw=0.75,
    )

    ax.plot(
        fit_length,
        n_dwd_second + m_dwd_second * fit_length,
        color=COLOR_DWD_SECOND,
        lw=0.75,
    )

    ax.plot(
        fit_length,
        n_clm_future_second + m_clm_future_second * fit_length,
        color=COLOR_CLIMATE_FUTURE_SECOND,
        lw=0.75,
    )

    if with_past_climate_years:
        ax.plot(
            fit_length,
            n_clm_past_first + m_clm_past_first * fit_length,
            color=COLOR_CLIMATE_PAST_FIRST,
            lw=0.75,
        )

        ax.plot(
            fit_length,
            n_clm_past_second + m_clm_past_second * fit_length,
            color=COLOR_CLIMATE_PAST_SECOND,
            lw=0.75,
        )


plt.axvline(x=2020.5, color=COLOR_BOUNDARY_LINE, ls="--", lw=0.8)
plt.axhline(y=180, color=COLOR_BOUNDARY_LINE, ls="--", lw=0.8)

lower_x_lim = x_axis.get(x_axis_type)[2]
upper_x_lim = x_axis.get(x_axis_type)[3]
ax.set_xlim(lower_x_lim, upper_x_lim)

ax.set_ylim(0, 180)
ax.set_yticks(list(y_axis.keys()))
ax.set_yticklabels(list(y_axis.values()), fontsize=8)

x_ax_label = x_axis.get(x_axis_type)[1]
ax.set_xlabel(x_ax_label, fontsize=12, fontweight="bold")
ax.set_ylabel(
    "Day of maximum activity\n" " of infected questing nymphs",
    fontsize=12,
    fontweight="bold",
)

plt.legend(fontsize=6)

plt.tight_layout()

plt.savefig(
    f"peak_activity_inf_nymphs_two_peaks_{x_ax_type}.{output_format}",
    dpi=400,
)
plt.close(fig)
