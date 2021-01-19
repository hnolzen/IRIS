import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import calendar
import math
import os

file_dir = os.path.dirname(os.path.abspath('__file__'))
iris_main_dir = os.path.abspath(os.path.join(file_dir, "..", ".."))
iris_output_sensitivity_dir = os.path.abspath(iris_main_dir + '/output/sensitivity_analysis_global_optimisation_ticks_years')

haselmühl_data = pd.read_excel(iris_main_dir + '/input/fructification_index/nymphs_haselmühl.xlsx', header = 1, skiprows = 2)
haselmühl_data['month'] = haselmühl_data['date'].str[5:7].astype(int)

for filename in os.listdir(iris_output_sensitivity_dir):
    
    file_params = filename.split('.csv')[0].split('_')
    year = int(file_params[2])
    initial_ticks = int(file_params[3])
    activation_rate = int(file_params[5])
    
    iris_output = pd.read_csv(iris_output_sensitivity_dir + '/' + filename, header = 0)
    
    dates = pd.DataFrame()
    dates['month'] = pd.date_range(f'{year}-01-01', f'{year}-12-31').month

    if calendar.isleap(year):
        dates = dates.drop(59)   # Remove February 29th
        dates = dates.reset_index(drop = True)
        
    iris_output['month'] = dates['month']
    
    haselmühl_data_year = haselmühl_data[haselmühl_data['date'].str.contains(f'{year}')]
        
    plot_data = []
    for month in range(1, 12 + 1):
        iris_output_month = iris_output[iris_output['month'] == month]
        haselmühl_data_month = haselmühl_data_year[haselmühl_data_year['month'] == month]
        
        iris_nymph_density = sum(iris_output_month['questing_nymphs']) / len(iris_output_month)
        haselmühl_density = haselmühl_data_month['nymphs.1'].values[0]
        
        plot_data.append([month, iris_nymph_density, haselmühl_density])
    
    plot_data = pd.DataFrame(plot_data, columns = ['month','iris_nymph_density','haselmühl_density'])
    
    rmse = math.sqrt(((plot_data['haselmühl_density'] - plot_data['iris_nymph_density'])**2).mean())
    rmse = round(rmse, 2)

    plot_data = plot_data.fillna(-1)

    x = np.arange(12)
    fig, ax = plt.subplots()
    ax.bar(x, plot_data['haselmühl_density'], color = '#bdbdbd')
    ax.plot(x, plot_data['iris_nymph_density'], marker = '.', color = 'black')
    ax.set_ylim(0, 150)
    ax.set_ylabel(r'Nymphs per 100 $m^2$', fontsize = 14)
    plt.xticks(x, ('Jan', 'Feb', 'Mar', 'Apr','May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'))
    plt.title(f'Year = {year}, RMSE = {rmse} \n Initial ticks = {initial_ticks}, Activation rate = {activation_rate / 1000}', fontweight='bold')
    
    plt.savefig(f'iris_vs_haselmühl_{year}_{rmse}_{initial_ticks}_{activation_rate}.png', dpi = 600)
