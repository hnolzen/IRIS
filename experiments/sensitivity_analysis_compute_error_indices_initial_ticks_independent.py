import pandas as pd
import datetime
import math
import itertools
import multiprocessing
import functools

def compute_errors_indices(data_haselmühl, params):
    year, larvae, nymphs, activation_rate = params

    data_haselmühl_year = data_haselmühl[data_haselmühl.apply(lambda row: row['date'].startswith(str(year)) , axis = 1)]['nymphs.1'].reset_index(drop = True)

    iris_output = pd.read_csv(f'output/sensitivity_analysis_{year}_{larvae}_{nymphs}_{activation_rate}.csv', header = 0)
    time_series_questing_nymphs = iris_output.groupby(['tick'])['questing_nymphs'].mean()
    time_series_questing_nymphs_monthly = time_series_questing_nymphs.groupby(lambda tick: (datetime.datetime(year, 1, 1) + datetime.timedelta(tick - 1)).month - 1).mean()

    rmse = math.sqrt(((data_haselmühl_year - time_series_questing_nymphs_monthly)**2).mean())
    mae = abs(data_haselmühl_year - time_series_questing_nymphs_monthly).mean()

    return (year, larvae, nymphs, activation_rate, rmse)
        
if __name__ == '__main__':

    with multiprocessing.Pool(50) as pool:
    
        params = itertools.product(range(2009, 2018 + 1), 
                                   range(5, 1000 + 1, 5),
                                   range(5, 1000 + 1, 5), 
                                   range(1, 30 + 1, 1))

        data_haselmühl = pd.read_excel('input/fructification_index/nymphs_haselmühl.xlsx', header = 1, skiprows = 2)

        year = []
        larvae = []
        nymphs = []
        activation_rate = []
        rmse = []

        for row in pool.imap_unordered(functools.partial(compute_errors_indices, data_haselmühl), params):
            print(row)
            year.append(row[0])
            larvae.append(row[1])
            nymphs.append(row[2])
            activation_rate.append(row[3])
            rmse.append(row[4])

        errors_indices_all_years = pd.DataFrame({'year':year, 'larvae':larvae, 'nymphs':nymphs, 'activation_rate':activation_rate, 'rmse':rmse})
        errors_indices_all_years.to_csv('rmse.csv', index = False)
