import pandas as pd
import os
import statistics as st

file_dir = os.path.dirname(os.path.abspath("__file__"))

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


def calculate_scenario_mean(years, model_dir):
    scenario_mean = []
    for year in years:
        file_name = f"weather_{year}.csv"
        file_path = os.path.join(model_dir, file_name)

        try:
            df = pd.read_csv(file_path, header=0)
            mean_temp = df["meanTemp"].mean()
            scenario_mean.append(mean_temp)

        except Exception:
            print(file_path)
            print("------")

    return scenario_mean


y_0_0K = list(range(1971, 2000 + 1))
y_1_5K = list(range(2012, 2041 + 1))
y_3_0K = list(range(2050, 2079 + 1))
y_4_0K = list(range(2070, 2099 + 1))


summary = []
for model in climate_models:
    model_dir = os.path.abspath(file_dir + f"/{model}/csv_regensburg/")

    model_0_0K = calculate_scenario_mean(y_0_0K, model_dir)
    model_1_5K = calculate_scenario_mean(y_1_5K, model_dir)
    model_3_0K = calculate_scenario_mean(y_3_0K, model_dir)
    model_4_0K = calculate_scenario_mean(y_4_0K, model_dir)

    mean_temp_0_0K = st.mean(model_0_0K)
    mean_temp_1_5K = st.mean(model_1_5K)
    mean_temp_3_0K = st.mean(model_3_0K)
    mean_temp_4_0K = st.mean(model_4_0K)

    std_temp_0_0K = st.stdev(model_0_0K)
    std_temp_1_5K = st.stdev(model_1_5K)
    std_temp_3_0K = st.stdev(model_3_0K)
    std_temp_4_0K = st.stdev(model_4_0K)

    diff_0_0K_vs_4_0K = mean_temp_4_0K - mean_temp_0_0K

    values = [
        model,
        mean_temp_0_0K,
        std_temp_0_0K,
        mean_temp_1_5K,
        std_temp_1_5K,
        mean_temp_3_0K,
        std_temp_3_0K,
        mean_temp_4_0K,
        std_temp_4_0K,
        diff_0_0K_vs_4_0K,
    ]
    summary.append(values)

summary = pd.DataFrame(
    summary,
    columns=(
        "model",
        "mean_0_0K",
        "std_0_0K",
        "mean_1_5K",
        "std_1_5K",
        "mean_3_0K",
        "std_3_0K",
        "mean_4_0K",
        "std_4_0K",
        "diff_0K_vs_4K",
    ),
)

print(summary)

summary.to_csv(
    "climate_models_mean_temp_0K_vs_4K.csv",
    header=True,
    index=False,
    encoding="utf-8",
)
