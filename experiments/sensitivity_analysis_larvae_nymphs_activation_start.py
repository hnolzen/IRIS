import pandas as pd
import datetime
import math
import itertools
import multiprocessing
import functools

def compute_errors_indices(data_haselmühl, params):

    year, larvae, nymphs, activation_rate, start_larvae_questing = params
    
    # Get Haselmühl data of year
    data_haselmühl_year = data_haselmühl[data_haselmühl.apply(lambda row: row['date'].startswith(str(year)) , axis = 1)]['nymphs.1'].reset_index(drop = True)

    # Get iris output data
    iris_output = pd.read_csv(f'output/sensitivity_analysis_{year}_{larvae}_{nymphs}_{start_larvae_questing}_{activation_rate}.csv', header = 0)
    
    # Aggregate the data by time steps (= 'tick')
    time_series_questing_nymphs = iris_output.groupby(['tick'])['questing_nymphs'].mean()  
    
    time_series_questing_nymphs_monthly = time_series_questing_nymphs.groupby(lambda tick: (datetime.datetime(year, 1, 1) + datetime.timedelta(tick - 1)).month - 1).mean()

    # Calculate Root Mean Square Error (RMSE)
    rmse = math.sqrt(((data_haselmühl_year - time_series_questing_nymphs_monthly)**2).mean())

    # Calculate Mean Absolute Error (MAE)
    mae = abs(data_haselmühl_year - time_series_questing_nymphs_monthly).mean()
  
    # Add to dictionary
    return {'year':year, 
            'larvae':larvae,
            'nymphs':nymphs,
            'activation_rate':activation_rate, 
            'start_larvae_questing':start_larvae_questing, 
            'rmse':rmse,
            'mae':mae}
        
if __name__ == '__main__':

    with multiprocessing.Pool(50) as pool:
    
        params = itertools.product(range(2009, 2018 + 1), 
                                   range(5, 1000 + 1, 5),
                                   range(5, 1000 + 1, 5), 
                                   range(1, 30 + 1, 1), 
                                   range(105, 105 + 1, 50))
        
        #Read in Haselmühl data
        data_haselmühl = pd.read_excel('input/fructification_index/nymphs_haselmühl.xlsx', header = 1, skiprows = 2)
        
        # Create Data Frame to store RMSE and MAE for each parameter combination
        errors_indices_all_years = pd.DataFrame(columns = ['year', 'larvae', 'nymphs', 'activation_rate', 'start_larvae_questing', 'rmse', 'mae'])
        
        for row in pool.imap_unordered(functools.partial(compute_errors_indices, data_haselmühl), params):
            print(row)
            errors_indices_all_years = errors_indices_all_years.append(row, ignore_index = True)

        # Save results in csv file
        errors_indices_all_years.to_csv('rmse.csv', index = False)
