import os
import subprocess
import numpy as np

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
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_IPSL-WRF381P_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_KNMI-RACMO22E_v2",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1",
    "MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1",
    "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1",
]

observers = {
    1: "csv_timeseries",
    2: "csv_timeseries_summary",
    3: "csv_timeseries_summary_habitats",
    4: "csv_timeseries_nymphs",
    5: "csv_timeseries_nymphs_habitats",
    6: "csv_timeseries_infection",
}


def create_output_folder(output_dir):
    os.makedirs(output_dir, exist_ok=True)


def iris(
    year,
    random_seed,
    initial_larvae,
    initial_nymphs,
    initial_adults,
    initial_infected_larvae,
    initial_infected_nymphs,
    initial_rodents,
    initial_infected_rodents,
    activation_rate,
    main_dir,
    jar_dir,
    weather_dir,
    observer,
):

    subprocess.run(
        [
            "java.exe",
            "-jar", jar_dir + "/IRIS-1.0-SNAPSHOT-jar-with-dependencies.jar",
            "eu.ecoepi.iris.experiments.AdHocSimulation",
            "-s", str(random_seed),
            "-a", str(initial_adults),
            "-l", str(initial_larvae),
            "-n", str(initial_nymphs),
            "-i", str(initial_infected_larvae),
            "-j", str(initial_infected_nymphs),
            "-u", str(initial_rodents),
            "-v", str(initial_infected_rodents),
            "-r", str(activation_rate),
            "-w", weather_dir + "weather_" + str(year) + ".csv",
            "-o", output_dir + "/iris_output_" + str(year) + ".csv",
            "-m", observer,
        ],
        check=True,
    )


main_dir = os.path.dirname(os.path.abspath("__file__"))
jar_dir = os.path.abspath(main_dir + "/target")

location = "regensburg"
random_seed = 42
activation_rate = 0.022
observer = observers.get(6)

initial_larvae = 100
initial_nymphs = 100
initial_adults = 150
initial_infected_larvae = 50
initial_infected_nymphs = 50
initial_rodents = 10
initial_infected_rodents = 0

with_dwd = False
with_future_climate = False
with_past_climate = False
with_all_climate_models = True

individual_simulation = True
year_start = 2018
year_end = 2018
with_climate_simulation = False
climate_model = climate_models[13]


if individual_simulation:
    print("individual_simulation:")
    with_dwd = False
    with_past_climate = False
    with_future_climate = False

    years = np.arange(year_start, year_end + 1)

    if with_climate_simulation:
        weather_dir = (
            main_dir + "/input/climate/" + climate_model + "/csv_" + location + "/"
        )
        output_dir = os.path.abspath(main_dir + "/output/" + climate_model)
        create_output_folder(output_dir)
    else:
        weather_dir = main_dir + "/input/weather/dwd_" + location + "/"
        output_dir = os.path.abspath(main_dir + "/output/DWD/")
        create_output_folder(output_dir)

    for year in years:
        iris(
            year,
            random_seed,
            initial_larvae,
            initial_nymphs,
            initial_adults,
            initial_infected_larvae,
            initial_infected_nymphs,
            initial_rodents,
            initial_infected_rodents,
            activation_rate,
            main_dir,
            jar_dir,
            weather_dir,
            observer,
        )


if with_dwd:
    print("with_dwd:")
    years_dwd = np.arange(1949, 2020 + 1)
    weather_dir = main_dir + "/input/weather/dwd_" + location + "/"
    output_dir = os.path.abspath(main_dir + "/output/DWD/")
    create_output_folder(output_dir)

    for year in years_dwd:
        iris(
            year,
            random_seed,
            initial_larvae,
            initial_nymphs,
            initial_adults,
            initial_infected_larvae,
            initial_infected_nymphs,
            initial_rodents,
            initial_infected_rodents,
            activation_rate,
            main_dir,
            jar_dir,
            weather_dir,
            observer,
        )

if with_future_climate:
    print("with_future_climate:")
    years_future_climate = np.arange(2021, 2099 + 1)

    for model in climate_models:
        print(model)
        weather_dir = main_dir + "/input/climate/" + model + "/csv_" + location + "/"
        output_dir = os.path.abspath(main_dir + "/output/" + model)
        create_output_folder(output_dir)

        for year in years_future_climate:
            iris(
                year,
                random_seed,
                initial_larvae,
                initial_nymphs,
                initial_adults,
                initial_infected_larvae,
                initial_infected_nymphs,
                initial_rodents,
                initial_infected_rodents,
                activation_rate,
                main_dir,
                jar_dir,
                weather_dir,
                observer,
            )

if with_past_climate:
    print("with_past_climate:")
    years_past_climate = np.arange(1971, 2020 + 1)

    for model in climate_models:
        print(model)
        weather_dir = main_dir + "/input/climate/" + model + "/csv_" + location + "/"
        output_dir = os.path.abspath(main_dir + "/output/" + model)
        create_output_folder(output_dir)

        for year in years_past_climate:
            iris(
                year,
                random_seed,
                initial_larvae,
                initial_nymphs,
                initial_adults,
                initial_infected_larvae,
                initial_infected_nymphs,
                initial_rodents,
                initial_infected_rodents,
                activation_rate,
                main_dir,
                jar_dir,
                weather_dir,
                observer,
            )

print("Run(s) finished.")
