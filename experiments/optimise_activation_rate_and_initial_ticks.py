import pandas as pd
import math
import os
import shutil

all_rmse = {}

iris_output = pd.read_csv('rmse.csv')

for (_, row) in iris_output.iterrows():
    all_rmse[(row['activation_rate'], row['year'], row['ticks'])] = row['rsme']
    
min_min_initial_ticks = None
min_activation_rate = 0
min_sum_min_rmse = math.inf

activation_rate_range = pd.unique(iris_output.activation_rate)
year_range = pd.unique(iris_output.year)
initial_ticks_range = pd.unique(iris_output.ticks)

for activation_rate in activation_rate_range:
    min_initial_ticks = {}
    min_rmse = {}
 
    for year in year_range:
        min_initial_ticks[year] = 0
        min_rmse[year] = math.inf
        
        for initial_ticks in initial_ticks_range:
            rmse = all_rmse[(activation_rate, year, initial_ticks)]

            if min_rmse[year] > rmse:
                min_initial_ticks[year] = initial_ticks
                min_rmse[year] = rmse

    sum_min_rmse = 0

    for year in min_rmse:
          sum_min_rmse += min_rmse[year]

    if min_sum_min_rmse > sum_min_rmse:
       min_min_initial_ticks = min_initial_ticks
       min_activation_rate = activation_rate
       min_sum_min_rmse = sum_min_rmse

out_folder = '../output/'
opt_folder = '../output/sensitivity_analysis_global_optimisation_ticks_years'

if not os.path.exists(opt_folder):
    os.mkdir(opt_folder)

    for key in min_min_initial_ticks:   
        y = int(key)
        t = int(min_min_initial_ticks[key])
        a = int(min_activation_rate)
        opt_filename = f'sensitivity_analysis_{y}_{t}_105_{a}.csv'
        shutil.copy(out_folder + opt_filename, opt_folder)

print(min_min_initial_ticks)
print(min_activation_rate)
print(min_sum_min_rmse)
