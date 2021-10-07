import os
import pandas as pd
import matplotlib.pyplot as plt
from scipy import signal

file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")
out_dir = os.path.abspath(main_dir + "/output/")

GRID_CELLS = 144
MONTHLY_BOUNDARIES = "#d9d9d9"

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
    5: "CsvTimeSeriesWriterInfection",
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

cohorts = {
    0: ["larvae_questing", "Questing larvae", "#f29d00", "#ffb326"],
    1: ["nymphs_questing", "Questing nymphs", "#3182bd", "#9ecae1"],
    2: ["larvae_questing_inf", "Infected questing larvae", "#ff385c"],
    3: ["nymphs_questing_inf", "Infected questing nymphs", "#f21f00"],
    4: ["larvae_inactive_inf", "Infected inactive larvae", "#67000d"],
    5: ["nymphs_inactive_inf", "Infected inactive nymphs", "#ae017e"],
    6: ["larvae_engorged_inf", "Infected engorged larvae", "#6a51a3"],
    7: ["nymphs_engorged_inf", "Infected engorged nymphs", "#ae017e"],
    8: ["larvae_late_engorged_inf", "Infected late engorged larvae", "#238b45"],
    9: ["nymphs_late_engorged_inf", "Infected late engorged nymphs", "#7a0177"],
    10: ["larvae_feeding_events_inf","Feeding events (infected larvae)","#fd8d3c"],
    11: ["nymphs_feeding_events_inf", "Feeding events (infected nymphs)","#2171b5"],
    12: ["larvae_new_feeding_events_inf","Feeding events (new infected larvae)","#fed976"],
    13: ["nymphs_new_feeding_events_inf", "Feeding events (new infected nymphs)","#6baed6"],
    14: ["total_feeding_events_inf", "Total feeding events (infected ticks)","#3690c0"],
}


def read_csv(year, input_model):
    input_dir = models[input_model]
    file_path = (
        out_dir + f"/{observers[observer]}/{input_dir}/iris_abundance_{year}.csv"
    )
    try:
        df = pd.read_csv(file_path, header=0)
        return df

    except FileNotFoundError as ex:
        print("FileNotFoundError: The file ", ex.filename, " was not found.")


def set_y_axis(y, y_lim):
    y_axis_label_type = [
        ["Larvae", "larvae"],
        ["Nymphs", "nymphs"],
        ["Ticks", "ticks"],
    ]

    j_first = y[0] % 2
    j_current = y[0] % 2
    for j in y:
        if (j % 2 == j_first) & (j < 14):
            label_type = y_axis_label_type[j_current]
        else:
            label_type = y_axis_label_type[2]
            break

    plt.ylim(0, y_lim)
    ax.set_ylabel(f"Number of {label_type[1]}", fontsize=12)
    
    if with_density:
        plt.ylim(0, y_lim / 100)
        ax.set_ylabel(f"{label_type[0]} per 100 $m^2$", fontsize=12)


year = 2018
input_model = 0
observer = 5
y_axis_limit = 6000
with_density = True
with_color_fill = True
with_smoothing = True
output_format = "png"

with_questing_larvae = True
with_questing_nymphs = True

with_questing_larvae_inf = False
with_questing_nymphs_inf = False

with_inactive_larvae_inf = False
with_inactive_nymphs_inf = False

with_engorged_larvae_inf = False
with_engorged_nymphs_inf = False

with_late_engorged_larvae_inf = False
with_late_engorged_nymphs_inf = False

with_feeding_events_larvae_inf = False
with_feeding_events_nymphs_inf = False

with_new_feeding_events_larvae_inf = False
with_new_feeding_events_nymphs_inf = False

with_total_feeding_events_inf = False

cohorts_to_plot = [
    with_questing_larvae,
    with_questing_nymphs,
    with_questing_larvae_inf,
    with_questing_nymphs_inf,
    with_inactive_larvae_inf,
    with_inactive_nymphs_inf,
    with_engorged_larvae_inf,
    with_engorged_nymphs_inf,
    with_late_engorged_larvae_inf,
    with_late_engorged_nymphs_inf,
    with_feeding_events_larvae_inf,
    with_feeding_events_nymphs_inf,
    with_new_feeding_events_larvae_inf,
    with_new_feeding_events_nymphs_inf,
    with_total_feeding_events_inf,
]

data = read_csv(year, input_model)

fig, ax = plt.subplots(figsize=(8, 3))

for x_value in list(x_axis.keys())[2:24:2]:
    plt.axvline(x=x_value, color=MONTHLY_BOUNDARIES, ls="-", lw=0.5, alpha=0.5)

x = data["tick"]
y_labels = []
for i, v in enumerate(cohorts_to_plot):
    if v == True:
        y_labels.append(i)
        y = data[cohorts[i][0]]
        y_label = cohorts[i][1]
        c_line = cohorts[i][2]

        if with_density:
            y /= GRID_CELLS

        if with_smoothing:
            y = signal.savgol_filter(y, 53, 3)
            plt.plot(x, y, lw=1.0, color=c_line, label=y_label)

        if with_color_fill:
            if len(cohorts[i]) > 3:
                c_fill = cohorts[i][3]
                plt.fill_between(x, 0, y, color=c_fill, alpha=0.5)
              
set_y_axis(y_labels, y_axis_limit)

ax.set_xticks(list(x_axis.keys()))
ax.set_xticklabels(list(x_axis.values()), fontsize=10)

for tick in ax.xaxis.get_major_ticks()[1::2]:
    tick.tick1line.set_markersize(0)
    tick.tick2line.set_markersize(0)

ax.margins(x=0)
ax.margins(y=0)

plt.title(f"{year}", fontweight="bold", fontsize=12)

plt.legend(fontsize=10)

plt.tight_layout()

filename_cohorts = ""
for i in y_labels:
    filename_cohorts += cohorts[i][0] + "_"

plt.savefig(
    f"timeseries_{year}_{filename_cohorts}{models[input_model]}.{output_format}",
    dpi=400,
)
plt.close(fig)
