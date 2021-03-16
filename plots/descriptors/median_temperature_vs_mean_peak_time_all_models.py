import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
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
df_gerics = df_all[df_all['type'] != 'dwd']

bins = np.arange(5.0, 16.25, 0.25)
df_median_dwd = df_dwd.groupby(pd.cut(df_dwd['median_tmp'], bins = bins)).mean().dropna()
df_median_gerics = df_gerics.groupby(pd.cut(df_gerics['median_tmp'], bins = bins)).mean().dropna()


fig, ax = plt.subplots()
ax.scatter(df_median_dwd['median_tmp'], df_median_dwd['peak'], marker = '.', s = 50, facecolors = 'none', color = '#31a354', label = 'DWD')
ax.scatter(df_median_gerics['median_tmp'], df_median_gerics['peak'], marker = '.', s = 50, facecolors = 'none', color = '#fe9929', label = 'GERICS (future)')

y_old = [1, 16, 31, 46, 60, 75, 90, 105, 120, 135, 150, 165, 180]
y_text = ['Jan. 01', '15', 'Feb. 01', '15', 'Mar. 01', '15', 'Apr. 01', '15','May. 01', '15', 'Jun. 01', '15', 'Jul. 01'] 

ax.set_ylim(0, 180)
ax.set_xlim(5, 16)

ax.set_yticks(y_old)
ax.set_yticklabels(y_text, minor = False)

ax.set_xlabel('Median Temperature (°C)', fontsize = 12, fontweight = 'bold')
ax.set_ylabel('Average day of max peak', fontsize = 12, fontweight = 'bold')

plt.legend()
plt.title('Bin size = 0.25,', fontsize = 8, fontweight = 'bold')
plt.tight_layout()
plt.savefig('median_temperature_bins_vs_mean_peak_time_all_models.png', dpi = 400, format = "png")
plt.savefig('median_temperature_bins_vs_mean_peak_time_all_models.pdf', dpi = 400, format = "pdf")
