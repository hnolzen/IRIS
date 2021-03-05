from netCDF4 import Dataset
import os
import math

file_dir = os.path.dirname(os.path.abspath('__file__'))

climate_model = 'MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1'
climate_variable = 'tasAdjust'

input_file = 'climate_variable' + '_' + climate_model + '-GERICS-ISIMIP3BASD-UFZ-1km-1971-2000.nc'

data = Dataset(climate_model + '/' + input_file)
print(data.variables.keys())

lat = data.variables['lat']
lon = data.variables['lon']

lat_bnds = data.variables['lat_bnds']
lon_bnds = data.variables['lon_bnds']

lat_location = 49.4083   #(Lat. Haselmühl)
lon_location = 11.8819   #(Lon. Haselmühl)

min_dist = math.inf
min_i = None
min_j = None
for i in range(412):
    for j in range(424):
        dist = (lat[i][j] - lat_location)**2 + (lon[i][j] - lon_location)**2
        if dist < min_dist:
            min_dist = dist
            min_i = i
            min_j = j

print(min_i, min_j, lat[min_i][min_j], lon[min_i][min_j])
print(lat_bnds[min_i][min_j])
print(lon_bnds[min_i][min_j])
