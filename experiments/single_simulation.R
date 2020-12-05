# R script to simulate a single year

# Get main directory and starting script
library(stringr)
iris_experiment_directory <- dirname(rstudioapi::getSourceEditorContext()$path)
iris_main_directory <- str_remove(iris_experiment_directory, "/experiments")
source(paste0(iris_main_directory, "/iris_start.r"))

# Set simulation year
year <- 2018

# Set input data
climate_simulations <- FALSE
slope_one <- TRUE 
dwd_data <- "regensburg"
climate_model <- "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1"

# Set random seed
random_seed <- 42

# Set weather input directory
if (climate_simulations) {
  if (slope_one) {
    weather_directory <- paste0(iris_main_directory,"/input/climate/", 
                                climate_model, "/monthly_mean_ds_slope1")
  } else {
    weather_directory <- paste0(iris_main_directory,"/input/climate/", 
                                climate_model, "/monthly_mean_ds")
  }
} else {
  weather_directory <- paste0(iris_main_directory, "/input/weather/dwd_", 
                              dwd_data, "/")   
}

# Set default number of initial ticks
initial_larvae <- 150
initial_nymphs <- 150
initial_adults <- 150

iris(year, 
     random_seed,
     initial_larvae,
     initial_nymphs,
     initial_adults,
     weather_directory)
