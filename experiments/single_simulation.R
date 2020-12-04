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

# Set random seed
random_seed <- 42

# Set weather input directory
weather_directory <- paste0(iris_main_directory, "/input/weather/dwd_", 
                            dwd_data, "/")

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
