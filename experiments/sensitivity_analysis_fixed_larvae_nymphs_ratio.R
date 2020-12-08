# R script to systematically vary input parameters 

# Get main directory and starting script
library(stringr)
iris_experiment_directory <- dirname(rstudioapi::getSourceEditorContext()$path)
iris_main_directory <- str_remove(iris_experiment_directory, "/experiments")
source(paste0(iris_main_directory, "/iris_start.r"))

# Get input functions script to get weather directory
source(paste0(iris_main_directory, "/input/input_functions.r"))

# Set simulation time span
year_start <- 2009
year_end <- 2018

# Set input data
climate_simulations <- FALSE
slope_one <- TRUE 
dwd_data <- "regensburg"
climate_model <- "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1"

# Set random seed
random_seed <- 42

# Set variation options of larvae and nymphs
from_ticks <- 5
to_ticks <- 500
by_ticks <- 5

# Set variation options of activation rate
from_rate <- 0.02
to_rate <- 0.10
by_rate <- 0.02

# Set default number of adult ticks
initial_adults <- 150

# Get weather input directory
weather_directory <- get_weather_directory(climate_simulations, 
                                           iris_main_directory, 
                                           climate_model, 
                                           dwd_data,
                                           slope_one)

# Perform sensitivity analysis with selected parameters 
for (year in year_start : year_end) {
  
  for(initial_ticks in seq(from = from_ticks, to = to_ticks, by = by_ticks)) {
    
    for (activation_rate in seq(from = from_rate, to = to_rate, by = by_rate)) {
      
      iris(year, 
           random_seed,
           initial_ticks,
           initial_ticks,
           initial_adults,
           activation_rate,
           weather_directory) 
    }
  }
}
