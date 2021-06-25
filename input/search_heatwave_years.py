import pandas as pd
import os

file_dir = os.path.dirname(os.path.abspath("__file__"))
iris_main_dir = os.path.abspath(file_dir + "/..")

climate_model = "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1"
iris_input_dir_dwd = os.path.abspath(iris_main_dir + "/input/weather/dwd_regensburg/")
iris_input_dir_climate = os.path.abspath(
    iris_main_dir + "/input/climate/" + climate_model + "/csv_regensburg/"
)

iris_input_dirs = [iris_input_dir_dwd, iris_input_dir_climate]

heatwave_duration = 9
max_temp = 30
heatwave_years = []

for dirname in iris_input_dirs:

    for file in os.listdir(dirname):

        filename = os.path.join(dirname, file)

        file_params = filename.split(".csv")[0].split("_")
        year = int(file_params[-1])

        df = pd.read_csv(filename, header=0)
        df = df["maxTemp"]

        current_heatwave = 0
        longest_heatwave = 0
        for day in range(0, df.size):
            if df.iloc[day] < max_temp:
                current_heatwave = 0
            else:
                current_heatwave += 1
                if longest_heatwave < current_heatwave:
                    longest_heatwave = current_heatwave

        if longest_heatwave > heatwave_duration:
            heatwave_years.append(year)

print(heatwave_years)
