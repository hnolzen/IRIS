import pandas as pd
import matplotlib.pyplot as plt
import math

def plot_rmse(year, data_rmse_year, max_round_rmse):
    
    fig = plt.figure(figsize = (12,12), dpi = 200)
    ax = plt.axes(projection = '3d')
    
    z_points = data_rmse_year['rmse']
    y_points = data_rmse_year['activation_rate']
    x_points = data_rmse_year['ticks']
    ax.scatter3D(x_points, y_points, z_points, c = z_points, cmap = 'hsv');
    
    ax.set_ylim(1, 8)
    ax.set_zlim(0, max_round_rmse)
    
    ax.set_title(f'Year = {year}', fontsize = 30, weight = 'bold')
    
    ax.set_xlabel('ticks', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_ylabel('activation_rate', fontsize = 24, weight = 'bold', labelpad = 15)
    ax.set_zlabel('RMSE', fontsize = 24, weight = 'bold', rotation = 90, labelpad = 15)
    
    ax.xaxis.set_tick_params(labelsize = 22)
    ax.yaxis.set_tick_params(labelsize = 22)
    ax.zaxis.set_tick_params(labelsize = 22)
    
    fig.savefig(f'sensitivity_analysis_rmse_activation_ticks_{year}')

data_rmse = pd.read_csv('rmse.csv', header = 0)
max_round_rmse = int(math.ceil(data_rmse['rmse'].max() / 10.0)) * 10
min_rmse = pd.DataFrame(columns = ('year', 'ticks', 'activation_rate', 'rmse'))

for year in range(2009, 2018 + 1):
    data_rmse_year = data_rmse[data_rmse['year'] == year]     
    min_rmse_year = data_rmse_year.iloc[data_rmse_year['rmse'].argmin()]
    min_rmse = min_rmse.append(min_rmse_year, ignore_index = True) 
    plot_rmse(year, data_rmse_year, max_round_rmse)

min_rmse.to_csv('min_rmse.csv', index = False)