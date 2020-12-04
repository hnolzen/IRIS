# R script to systematically vary input parameters 

# Get main directory and starting script
library(stringr)
iris_experiment_directory <- dirname(rstudioapi::getSourceEditorContext()$path)
iris_main_directory <- str_remove(iris_experiment_directory, "/experiments")
source(paste0(iris_main_directory, "/iris_start.r"))

# Set simulation time span
year_start <- 2009
year_end <- 2018

# Set input data
climate_simulations <- FALSE
slope_one <- TRUE 
dwd_data <- "regensburg"

# Set random seed
random_seed <- 42

# Set variation options of larvae
from_larvae <- 5
to_larvae <- 10
by_larvae <- 5

# Set variation options of nymphs
from_nymphs <- 5
to_nymphs <- 10
by_nymphs <- 5

# Set default number of adult ticks
initial_adults <- 150

# Set calibration data
if (slope_one) {
  weather_directory <- "C:/Klimadaten/NC/MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1/time_series_downscaled_monthly_mean_slope1/"
} else {
  weather_directory <- "C:/Klimadaten/NC/MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1/time_series_downscaled_monthly_mean/"
}  

for (year in year_start : year_end) {
  
  for(initial_larvae in seq(from = from_larvae, to = to_larvae, by = by_larvae)) {
    
    for(initial_nymphs in seq(from = from_nymphs, to = to_nymphs, by = by_nymphs)) {

      iris(year, 
           random_seed,
           initial_larvae,
           initial_nymphs,
           initial_adults,
           weather_directory)
    }
  }
}
