import pandas as pd
import math
import os
import shutil

all_rmse = {}

iris_output = pd.read_csv('rmse.csv')

for (_, row) in iris_output.iterrows():
    all_rmse[(row['activation_rate'], row['year'], row['larvae'], row['nymphs'])] = row['rmse']
    
min_min_initial_larvae = None
min_min_initial_nymphs = None
min_activation_rate = 0
min_sum_min_rmse = math.inf

activation_rate_range = pd.unique(iris_output.activation_rate)
year_range = pd.unique(iris_output.year)
initial_larvae_range = pd.unique(iris_output.larvae)
initial_nymphs_range = pd.unique(iris_output.nymphs)

for activation_rate in activation_rate_range:
    min_initial_larvae = {}
    min_initial_nymphs = {}
    min_rmse = {}
 
    for year in year_range:
        min_initial_larvae[year] = 0
        min_initial_nymphs[year] = 0
        min_rmse[year] = math.inf
        
        for initial_larvae in initial_larvae_range:
            for initial_nymphs in initial_nymphs_range:
                rmse = all_rmse[(activation_rate, year, initial_larvae, initial_nymphs)]
                
                if min_rmse[year] > rmse:
                    min_initial_larvae[year] = initial_larvae
                    min_initial_nymphs[year] = initial_nymphs
                    min_rmse[year] = rmse

    sum_min_rmse = 0

    for year in min_rmse:
          sum_min_rmse += min_rmse[year]

    if min_sum_min_rmse > sum_min_rmse:
       min_min_initial_larvae = min_initial_larvae
       min_min_initial_nymphs = min_initial_nymphs
       min_activation_rate = activation_rate
       min_sum_min_rmse = sum_min_rmse

out_folder = '../output/'
opt_folder = '../output/sensitivity_analysis_global_optimisation_larvae_nymphs_years'

if not os.path.exists(opt_folder):
    os.mkdir(opt_folder)

    for key in min_min_initial_ticks:   
        y = int(key)
        l = int(min_min_initial_larvae[key])
        n = int(min_min_initial_nymphs[key])
        a = int(min_activation_rate)
        opt_filename = f'sensitivity_analysis_{y}_{l}_{n}_105_{a}.csv'
        shutil.copy(out_folder + opt_filename, opt_folder)

print(min_min_initial_larvae)
print(min_min_initial_nymphs)
print(min_activation_rate)
print(min_sum_min_rmse)
         