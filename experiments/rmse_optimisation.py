import pandas as pd
import math

all_rmse = {}

iris_output = pd.read_csv('rmse.csv')

for r in iris_output.iterrows():
    row = r[1]
    all_rmse[(row['activation_rate'], row['year'], row['ticks'])] = row['rsme']
    
min_min_initial_ticks = None
min_activation_rate = 0
min_sum_min_rmse = math.inf

range_min_activation_rate = int(min(iris_output.activation_rate))
range_max_activation_rate = int(max(iris_output.activation_rate))
range_min_year = int(min(iris_output.year))
range_max_year = int(max(iris_output.year))
range_min_initial_ticks = int(min(iris_output.ticks))
range_max_initial_ticks = int(max(iris_output.ticks))

for activation_rate in range(range_min_activation_rate, range_max_activation_rate):
    min_initial_ticks = {}
    min_rmse = {}
 
    for year in range(range_min_year, range_max_year):
        min_initial_ticks[year] = 0
        min_rmse[year] = math.inf
        
        for initial_ticks in range(range_min_initial_ticks, range_max_initial_ticks, 10):
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


print(min_min_initial_ticks)
print(min_activation_rate)
print(min_sum_min_rmse)
             