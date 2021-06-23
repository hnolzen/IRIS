import pandas as pd
import os
import numpy as np
from scipy.optimize import curve_fit
import matplotlib.pyplot as plt

file_dir = os.path.dirname(os.path.abspath("__file__"))
iris_main_dir = os.path.abspath(file_dir + "/.." + "/..")

iris_output_dirs = []

iris_output_dir_dwd = os.path.abspath(
    iris_main_dir + "/output/Output_CsvTimeSeriesWriterNymphs/DWD"
)
iris_output_dirs.append(iris_output_dir_dwd)

climate_models = [
    "CCCma-CanESM2_rcp85_r1i1p1_CLMcom-CCLM4-8-17_v1",
    "CCCma-CanESM2_rcp85_r1i1p1_GERICS-REMO2015_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_IPSL-WRF381P_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_KNMI-RACMO22E_v1",
    "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_SMHI-RCA4_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_CNRM-ALADIN63_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_DMI-HIRHAM5_v2",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_GERICS-REMO2015_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_ICTP-RegCM4-6_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1",
]

for model in climate_models:
    iris_output_dir_climate = os.path.abspath(
        iris_main_dir + "/output/Output_CsvTimeSeriesWriterNymphs/" + model
    )
    iris_output_dirs.append(iris_output_dir_climate)


def get_data(year_start, year_end, summer_start, summer_end):

    summary = []

    m = "DWD"
    for dirname in iris_output_dirs:

        for file in os.listdir(dirname):
            filename = os.path.join(dirname, file)
            file_params = filename.split(".csv")[0].split("_")
            year = int(file_params[-1])

            if year in range(year_start, year_end + 1):
                try:
                    df = pd.read_csv(filename, header=0)
                    df_summer = df["questing_nymphs"].iloc[summer_start:summer_end]

                    min_activity = df_summer.min()

                    values = [year, min_activity, m]
                    summary.append(values)

                except Exception:
                    print(filename)
                    print("------------")

        m = "Climate"

    summary = pd.DataFrame(
        summary,
        columns=("year", "min_activity", "model"),
    )
    return summary


def regression_function_1949(x, a, b):
    return a * np.exp(-b * (x - 1949))


def regression_function_2021(x, a, b):
    return a * np.exp(-b * (x - 2021))


y_start = 1949
y_end = 2098

date_dict = {
    151: "01 Jun",
    166: "15 Jun",
    181: "01 Jul",
    212: "01 Aug",
    227: "15 Aug",
    243: "01 Sep",
    258: "15 Sep",
    273: "01 Oct",
}

# start_dates = [151, 166, 181]
# end_dates = [212, 227, 243, 258, 273]

start_dates = [181]
end_dates = [273]

for s_start in start_dates:
    for s_end in end_dates:
        if s_end <= s_start:
            continue

        data = get_data(y_start, y_end, s_start, s_end)

        data_all = data[(data["model"] == "DWD") | (data["year"] >= 2021)]
        data_dwd = data[data["model"] == "DWD"]
        data_climate = data[data["year"] >= 2021]

        popt_all, pcov_all = curve_fit(
            regression_function_1949, data_all["year"], data_all["min_activity"]
        )

        popt_dwd, pcov_dwd = curve_fit(
            regression_function_1949, data_dwd["year"], data_dwd["min_activity"]
        )

        popt_climate, pcov_climate = curve_fit(
            regression_function_2021, data_climate["year"], data_climate["min_activity"]
        )

        fig, ax = plt.subplots()
        plt.figure(figsize=(5, 3))

        plt.scatter(
            data_climate["year"], data_climate["min_activity"], s=0.8, color="#feb24c"
        )

        plt.scatter(data_dwd["year"], data_dwd["min_activity"], s=1.0, color="#238b45")

        plt.plot(
            data_all["year"],
            regression_function_1949(data_all["year"], *popt_all),
            "r-",
            c="red",
            lw=0.8,
            ls="--",
            label="fit_all: a=%5.3f, b=%5.3f" % tuple(popt_all),
        )

        plt.plot(
            data_dwd["year"],
            regression_function_1949(data_dwd["year"], *popt_dwd),
            "r-",
            c="#238b45",
            lw=0.8,
            ls="--",
            label="fit_dwd: a=%5.3f, b=%5.3f" % tuple(popt_dwd),
        )

        plt.plot(
            data_climate["year"],
            regression_function_2021(data_climate["year"], *popt_climate),
            "r-",
            c="grey",
            lw=0.8,
            ls="--",
            label="fit_climate: a=%5.3f, b=%5.3f" % tuple(popt_climate),
        )

        plt.axvline(x=2020.5, color="lightgrey", ls="--", lw=0.8)

        plt.title(
            f"Minimum questing activity ({date_dict.get(s_start)} - {date_dict.get(s_end)}) ",
            fontweight="bold",
            fontsize=10,
        )

        plt.tick_params(labelsize=8)

        plt.xlim(1949, 2100)
        plt.ylim(0, 4000)

        plt.xlabel("Year", fontsize=8, fontweight="bold")
        plt.ylabel("Number of questing nymphs", fontsize=8, fontweight="bold")

        plt.legend(fontsize=6)

        plt.tight_layout()
        plt.savefig(
            f"Year_vs_min_summer_activity_{date_dict.get(s_start)}_{date_dict.get(s_end)}_all_models_curve_fit_2.png",
            dpi=400,
            format="png",
        )
