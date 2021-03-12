import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy.stats import pearsonr
import os

file_dir = os.path.dirname(os.path.abspath('__file__'))
iris_main_dir = os.path.abspath(file_dir + '/..' + '/..')
iris_output_dir = os.path.abspath(iris_main_dir + '/output/summary_climate_models_median_temperature/')

models = []
for filename in os.listdir(iris_output_dir):   
    models.append(pd.read_csv(iris_output_dir + '/' + filename))

df_all = pd.concat(models).drop_duplicates()
df_all = df_all.rename(columns = {'time_max':'peak', 'type_weather_data': 'type'})

df_dwd = df_all[df_all['type'] == 'dwd']
df_gerics = df_all[df_all['type'] == 'gerics_future']
df_gerics_past = df_all[df_all['type'] == 'gerics_past']

corr_dwd = round(pearsonr(df_dwd['median_tmp'], df_dwd['peak'])[0], 2)
corr_gerics = round(pearsonr(df_gerics['median_tmp'], df_gerics['peak'])[0], 2)
corr_gerics_past = round(pearsonr(df_gerics_past['median_tmp'], df_gerics_past['peak'])[0], 2)

m_dwd, n_dwd = np.polyfit(df_dwd['median_tmp'], df_dwd['peak'], 1)
m_gerics, n_gerics = np.polyfit(df_gerics['median_tmp'], df_gerics['peak'], 1)
m_gerics_past, n_gerics_past = np.polyfit(df_gerics_past['median_tmp'], df_gerics_past['peak'], 1)


fig, ax = plt.subplots()
ax.scatter(df_gerics['median_tmp'], 
            df_gerics['peak'],
            label = 'Simulations (2021 - 2099)',
            marker = '.', s = 35, facecolors = 'none', color = '#fe9929')

ax.scatter(df_gerics_past['median_tmp'],
            df_gerics_past['peak'], 
            label = 'Simulations (1971 - 2020)',
            marker = '.', s = 35, facecolors = 'none', color = '#de2d26')

ax.scatter(df_dwd['median_tmp'],
            df_dwd['peak'], 
            label = 'DWD (1949 - 2020)',
            marker = '.', s = 35, facecolors = 'none', color = '#31a354')

fit_length = np.arange(5, 17)
ax.plot(fit_length, n_gerics + m_gerics * fit_length, color = '#fe9929', linewidth = 0.75)
ax.plot(fit_length, n_gerics_past + m_gerics_past * fit_length, color = '#de2d26', linewidth = 0.75)
ax.plot(fit_length, n_dwd + m_dwd * fit_length, color = '#31a354', linewidth = 0.75)

ax.set_ylim(0, 180)
ax.set_xlim(5, 16)

y_old = [1, 16, 31, 46, 60, 75, 90, 105, 120, 135, 150, 165, 180]
y_text = ['Jan. 01', '15', 'Feb. 01', '15', 'Mar. 01', '15', 'Apr. 01', '15','May. 01', '15', 'Jun. 01', '15', 'Jul. 01'] 

ax.set_yticks(y_old)
ax.set_yticklabels(y_text, minor = False, fontsize = 8)

ax.set_xlabel('Median Temperature (°C)', fontsize = 14, fontweight = 'bold')
ax.set_ylabel('Day of max peak (-)', fontsize = 14, fontweight = 'bold')

plt.legend(fontsize = 7)
plt.title(f'Pearson (DWD: {corr_dwd}, Simulations (2021 - 2099): {corr_gerics} Simulations (1971 - 2020): {corr_gerics_past}', fontsize = 7, fontweight = 'bold')
plt.tight_layout()
plt.savefig('median_temperature_vs_peak_time_all_models.png', dpi = 400, format = "png")
plt.savefig('median_temperature_vs_peak_time_all_models.pdf', dpi = 400, format = "pdf")
