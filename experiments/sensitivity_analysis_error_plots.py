import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import math

def plot_rmse(year, data_rmse_start_lq, max_round_rmse, start_lq):
    
    fig = plt.figure(figsize = (12,12), dpi = 200)
    ax = plt.axes(projection = '3d')
    
    z_points = data_rmse_start_lq['rsme']
    y_points = data_rmse_start_lq['activation_rate']
    x_points = data_rmse_start_lq['ticks']
    ax.scatter3D(x_points, y_points, z_points, c = z_points, cmap = 'hsv');
    
    ax.set_ylim(1, 8)
    ax.set_zlim(0, max_round_rmse)
    
    ax.set_title(f'Year = {year} \n Begin larvae questing = {start_lq}', fontsize = 30, weight = 'bold')
    
    ax.set_xlabel('ticks', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_ylabel('activation_rate', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_zlabel('RMSE', fontsize = 24, weight = 'bold', rotation = 90, labelpad = 15)
    
    ax.xaxis.set_tick_params(labelsize = 22)
    ax.yaxis.set_tick_params(labelsize = 22)
    ax.zaxis.set_tick_params(labelsize = 22)
    
    plt.show()
    fig.savefig(f'summary_exp_1_{year}_{start_lq}')
    

# Get iris output data
data_rmse = pd.read_csv('rsme_exp_1.csv', header = 0)

# Get max RSME and round up to nearest ten for consistent z-axis limit for all years
max_round_rmse = int(math.ceil(data_rmse['rsme'].max() / 10.0)) * 10

# Create empty data frame for rsme summary statistics
min_rmse = pd.DataFrame(columns = ('year', 'ticks', 'activation_rate', 'start_larvae_questing', 'rsme'))

for year in range(2009, 2018 + 1):
    data_rmse_year = data_rmse[data_rmse['year'] == year]
    
    for start_lq in range(0, 150 + 1, 50):
        data_rmse_start_lq = data_rmse_year[data_rmse_year['start_larvae_questing'] == start_lq]
        min_rmse_start_lq = data_rmse_start_lq[data_rmse_start_lq['rsme'] == data_rmse_start_lq['rsme'].argmin()]
        min_rmse = min_rmse.append(min_rmse_start_lq, ignore_index = True) 
        plot_rmse(year, data_rmse_start_lq, max_round_rmse, start_lq)


# Save results in csv file
min_rmse.to_csv('min_rsme_exp_1.csv', index = False)