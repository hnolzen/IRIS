# Get directory of this file
iris_main_directory <- dirname(rstudioapi::getSourceEditorContext()$path)
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
weather_directory <- paste0(iris_main_directory, "./input/weather/dwd_", 
                            dwd_data, "/")

# Set default number of initial ticks
initial_larvae <- 150
initial_nymphs <- 150
initial_adults <- 150

iris(year, 
     year,
     random_seed,
     initial_larvae,
     initial_larvae,
     0,
     initial_nymphs,
     initial_nymphs,
     0,
     initial_adults,
     weather_directory)
