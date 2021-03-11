import pandas as pd
import os

file_dir = os.path.dirname(os.path.abspath('__file__'))
iris_main_dir = os.path.abspath(file_dir + '/..' + '/..')
iris_input_climate_dir = os.path.abspath(iris_main_dir + '/input/climate/')

models = [
"CCCma-CanESM2_rcp85_r1i1p1_CLMcom-CCLM4-8-17_v1",
"CCCma-CanESM2_rcp85_r1i1p1_GERICS-REMO2015_v1",
"IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_IPSL-WRF381P_v1",
"IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_KNMI-RACMO22E_v1",
"IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_SMHI-RCA4_v1",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_CNRM-ALADIN63_v1",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_DMI-HIRHAM5_v2",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_GERICS-REMO2015_v1",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_ICTP-RegCM4-6_v1",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1",
"MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1",
"MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1",
"MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1"
]

mean_temp = []
for year in range(1971, 2099 + 1, 1):
    models_year = []
    for model in models:
        models_year.append(pd.read_csv(f'{iris_input_climate_dir}/{model}/csv_regensburg/weather_{year}.csv'))
    
    all_models_year = pd.concat(models_year)
    all_models_year_means = all_models_year.groupby(all_models_year['date']).mean()
    
    all_models_year_means.to_csv(f'{iris_input_climate_dir}/All_models_daily_mean/csv_regensburg/weather_{year}.csv', index = False)
    
    if year % 20 == 0:
        mean_temp.append(all_models_year_means['meanTemp'].to_list())

time_series = pd.DataFrame.from_records(mean_temp)
time_series = time_series.transpose()

time_series = time_series.assign(mean = time_series.mean(numeric_only = True, axis = 1))
time_series = time_series.assign(min = time_series.min(numeric_only = True, axis = 1))
time_series = time_series.assign(max = time_series.max(numeric_only = True, axis = 1))

time_series_summary = time_series[['mean', 'min', 'max']].copy()
time_series_summary.plot(legend = False)

