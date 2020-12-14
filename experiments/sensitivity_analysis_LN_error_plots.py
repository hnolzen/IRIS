import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import math

def plot_rmse(year, data_rmse_start_lq, max_round_rmse, ar):
    
    fig = plt.figure(figsize = (12,12), dpi = 200)
    ax = plt.axes(projection = '3d')
    
    z_points = data_rmse_ar['rsme']
    y_points = data_rmse_ar['nymphs']
    x_points = data_rmse_ar['larvae']
    ax.scatter3D(x_points, y_points, z_points, c = z_points, cmap = 'hsv');
    
    ax.set_zlim(0, max_round_rmse)
    
    ax.set_title(f'Year = {year} \n Activation rate = {ar}', fontsize = 30, weight = 'bold')
    
    ax.set_xlabel('larvae', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_ylabel('nymphs', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_zlabel('RMSE', fontsize = 24, weight = 'bold', rotation = 90, labelpad = 15)
    
    ax.xaxis.set_tick_params(labelsize = 22)
    ax.yaxis.set_tick_params(labelsize = 22)
    ax.zaxis.set_tick_params(labelsize = 22)
    
    plt.show()
    fig.savefig(f'summary_exp_3_{year}_{ar}')
    

# Get iris output data
data_rmse = pd.read_csv('rsme_exp_3.csv', header = 0)

# Get max RSME and round up to nearest ten for consistent z-axis limit for all years
max_round_rmse = int(math.ceil(data_rmse['rsme'].max() / 10.0)) * 10

# Create empty data frame for rsme summary statistics
min_rmse = pd.DataFrame(columns = ('year', 'larvae', 'nymphs', 'activation_rate', 'start_larvae_questing', 'rsme'))

for year in range(2009, 2018 + 1):
    data_rmse_year = data_rmse[data_rmse['year'] == year]
    
    for ar in range(2, 6 + 1):
        data_rmse_ar = data_rmse_year[data_rmse_year['activation_rate'] == ar]
        min_rmse_ar = data_rmse_ar.iloc[data_rmse_ar['rsme'].argmin()]
        min_rmse = min_rmse.append(min_rmse_ar, ignore_index = True) 
        plot_rmse(year, data_rmse_ar, max_round_rmse, ar)


# Save results in csv file
min_rmse.to_csv('min_rsme_exp_3.csv', index = False)