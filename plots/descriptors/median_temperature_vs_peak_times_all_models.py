import matplotlib.pyplot as plt
import pandas as pd
from scipy.stats import pearsonr
import os

file_dir = os.path.dirname(os.path.abspath('__file__'))
iris_main_dir = os.path.abspath(file_dir + '/..' + '/..')
iris_output_dir = os.path.abspath(iris_main_dir + '/output/summary_climate_models/')

models = []
for filename in os.listdir(iris_output_dir):   
    models.append(pd.read_csv(iris_output_dir + '/' + filename))

df_all = pd.concat(models).drop_duplicates()
df_all = df_all.rename(columns = {'time_max':'peak', 'type_weather_data': 'type'})

df_dwd = df_all[df_all['type'] == 'dwd']
df_gerics = df_all[df_all['type'] == 'gerics']
df_gerics_diff = df_all[df_all['type'] == 'gerics_diff']

corr_dwd = round(pearsonr(df_dwd['median_tmp'], df_dwd['peak'])[0], 2)
corr_gerics = round(pearsonr(df_gerics['median_tmp'], df_gerics['peak'])[0], 2)
corr_gerics_diff = round(pearsonr(df_gerics_diff['median_tmp'], df_gerics_diff['peak'])[0], 2)


fig, ax = plt.subplots()
ax.scatter(df_gerics['median_tmp'], df_gerics['peak'], marker = '.', s = 35, facecolors = 'none', color = '#fe9929', label = 'GERICS (future)')
ax.scatter(df_gerics_diff['median_tmp'], df_gerics_diff['peak'], marker = '.', s = 35, facecolors = 'none', color = '#de2d26', label = 'GERICS (past)')
ax.scatter(df_dwd['median_tmp'], df_dwd['peak'], marker = '.', s = 35, facecolors = 'none', color = '#31a354', label = 'DWD')

ax.set_ylim(0, 180)
ax.set_xlim(5, 16)

y_old = [1, 16, 31, 46, 60, 75, 90, 105, 120, 135, 150, 165, 180]
y_text = ['Jan. 01', '15', 'Feb. 01', '15', 'Mar. 01', '15', 'Apr. 01', '15','May. 01', '15', 'Jun. 01', '15', 'Jul. 01'] 

ax.set_yticks(y_old)
ax.set_yticklabels(y_text, minor = False)

ax.set_xlabel('Median Temperature (°C)', fontsize = 12, fontweight = 'bold')
ax.set_ylabel('Day of max peak', fontsize = 12, fontweight = 'bold')

plt.legend()
plt.title(f'Pearson (DWD: {corr_dwd}, GERICS future: {corr_gerics} GERICS past: {corr_gerics_diff}', fontsize = 8, fontweight = 'bold')
plt.tight_layout()
plt.savefig('median_temperature_vs_peak_time_all_models.png', dpi = 400, format = "png")
plt.savefig('median_temperature_vs_peak_time_all_models.pdf', dpi = 400, format = "pdf")
