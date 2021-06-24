import pandas as pd
import os
import numpy as np
from scipy.optimize import curve_fit
import matplotlib.pyplot as plt

file_dir = os.path.dirname(os.path.abspath("__file__"))
main_dir = os.path.abspath(file_dir + "/.." + "/..")

out_dir_dwd = os.path.abspath(main_dir + "/output/Output_CsvTimeSeriesWriterNymphs/DWD")
out_dirs = [out_dir_dwd]

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
    out_dir_climate = os.path.abspath(
        main_dir + "/output/Output_CsvTimeSeriesWriterNymphs/" + model
    )
    out_dirs.append(out_dir_climate)


def get_data(year_start, year_end, summer_start, summer_end):

    summary = []
    source = "DWD"

    for dirname in out_dirs:

        for file in os.listdir(dirname):
            filename = os.path.join(dirname, file)
            file_params = filename.split(".csv")[0].split("_")
            year = int(file_params[-1])

            if year in range(year_start, year_end + 1):
                try:
                    df = pd.read_csv(filename, header=0)
                    df_summer = df["questing_nymphs"].iloc[summer_start:summer_end]

                    min_activity = df_summer.min()

                    values = [year, min_activity, source]
                    summary.append(values)

                except Exception:
                    print(filename)
                    print("------")

        source = "climate model"

    summary = pd.DataFrame(
        summary,
        columns=("year", "min_activity", "source"),
    )
    return summary


def reg_function(x0):
    return lambda x, a, b: a * np.exp(-b * (x - x0))


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

start_dates = [151, 166, 181]
end_dates = [212, 227, 243, 258, 273]

start_year_climate_data = [1971, 2021]

for start_year in start_year_climate_data:
    for s_start in start_dates:
        for s_end in end_dates:
            if s_end <= s_start:
                continue

            data = get_data(y_start, y_end, s_start, s_end)

            data_all = data[(data["source"] == "DWD") | (data["year"] >= start_year)]
            data_dwd = data[data["source"] == "DWD"]
            data_climate = data[
                (data["source"] == "climate model") & (data["year"] >= start_year)
            ]

            popt_all, pcov_all = curve_fit(
                reg_function(1949),
                data_all["year"],
                data_all["min_activity"],
            )

            popt_dwd, pcov_dwd = curve_fit(
                reg_function(1949), data_dwd["year"], data_dwd["min_activity"]
            )

            popt_climate, pcov_climate = curve_fit(
                reg_function(start_year),
                data_climate["year"],
                data_climate["min_activity"],
            )

            fig, ax = plt.subplots()
            plt.figure(figsize=(5, 3))

            plt.scatter(
                data_climate["year"],
                data_climate["min_activity"],
                s=0.8,
                color="#feb24c",
            )

            plt.scatter(
                data_dwd["year"], data_dwd["min_activity"], s=1.0, color="#238b45"
            )

            plt.plot(
                data_all["year"].unique(),
                reg_function(1949)(data_all["year"].unique(), *popt_all),
                "r-",
                c="red",
                lw=0.8,
                ls="--",
                label="fit_all: a=%5.3f, b=%5.3f" % tuple(popt_all),
            )

            plt.plot(
                data_dwd["year"],
                reg_function(1949)(data_dwd["year"], *popt_dwd),
                "r-",
                c="#238b45",
                lw=0.8,
                ls="--",
                label="fit_dwd: a=%5.3f, b=%5.3f" % tuple(popt_dwd),
            )

            plt.plot(
                data_climate["year"].unique(),
                reg_function(start_year)(data_climate["year"].unique(), *popt_climate),
                "r-",
                c="grey",
                lw=0.8,
                ls="--",
                label="fit_climate: a=%5.3f, b=%5.3f" % tuple(popt_climate),
            )

            plt.axvline(x=2020.5, color="lightgrey", ls="--", lw=0.8)

            plt.tick_params(labelsize=8)

            plt.xlim(1949, 2100)
            plt.ylim(0, 5000)

            plt.xlabel("Year", fontsize=8, fontweight="bold")
            plt.ylabel("Number of questing nymphs", fontsize=8, fontweight="bold")

            from_date = date_dict.get(s_start)
            to_date = date_dict.get(s_end)

            plt.title(
                f"Minimum questing activity ({from_date} - {to_date}) ",
                fontweight="bold",
                fontsize=10,
            )

            plt.legend(fontsize=6)
            plt.tight_layout()

            plt.savefig(
                f"Year_vs_min_summer_activity_{from_date}_{to_date}_{start_year}_all_models.png",
                dpi=400,
                format="png",
            )
