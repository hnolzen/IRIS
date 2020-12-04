# R-Script to start IRIS with input parameters

# Select simulation options
climate_simulations <- FALSE
simulate_single_year <- TRUE
slope_one <- TRUE 
dwd_data <- "regensburg"
single_year <- 2018
year_start <- 2009
year_end <- 2018
random_seed <- 42

# Set options for sensitivity analysis
sensitivity_analysis <- TRUE
default_number_larvae <- 150
default_number_nymphs <- 150
default_number_adults <- 150

if (sensitivity_analysis) {
  from_larvae <- 5
  to_larvae <- 500
  by_larvae <- 5
  
  from_nymphs <- 5
  to_nymphs <- 500
  by_nymphs <- 5
} else {
  from_larvae <- default_number_larvae
  to_larvae <- default_number_larvae
  by_larvae <- 0
  
  from_nymphs <- default_number_nymphs
  to_nymphs <- default_number_nymphs
  by_nymphs <- 0
}

# Set directory of this file as working directory
iris_main_directory <- dirname(rstudioapi::getSourceEditorContext()$path)

# Set directory of FatJar to run the model
iris_jar_directory <- paste0(iris_main_directory, "/target/")

# Set model input directory
iris_input_directory <- paste0(iris_main_directory, "/input/")

# Get functions from input source file
input_functions <- paste0(iris_input_directory, "input_functions.r")
source(input_functions)

# Set model output directory
output_directory <- paste0(iris_main_directory, "/output/")

# Set climate weather directory
if (climate_simulations) {
  if (slope_one) {
    weather_directory <- "C:/Klimadaten/NC/MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1/time_series_downscaled_monthly_mean_slope1/"
  } else {
    weather_directory <- "C:/Klimadaten/NC/MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1/time_series_downscaled_monthly_mean/"
  }  
} else {
  weather_directory <- paste0(iris_main_directory, "./input/weather/dwd_", 
                              dwd_data, "/")
}

# Select year or time span
if (simulate_single_year) {
  year <- single_year
  year_start <- single_year
  year_end <- single_year
}

# Create output folder for current simulation run
current_date <- as.character(Sys.Date())
output_folder <- paste(output_directory, current_date, sep = "")
dir.create(output_folder)

# Set substrings to start IRIS from R
iris_exe <- "C:/Users/nolzen/.jdks/openjdk-14.0.1/bin/java.exe"
iris_jar <- paste0("-jar ", iris_jar_directory, "IRIS-1.0-SNAPSHOT-jar-with-dependencies.jar")
iris_seed <- paste0("-s ", random_seed)
iris_adults <- paste0("-a ", default_number_adults)

# Iterate over defined time span
for (year in year_start : year_end) {
  
  for(initial_larvae in seq(from = from_larvae, to = to_larvae, by = by_larvae)) {
    
    for(initial_nymphs in seq(from = from_nymphs, to = to_nymphs, by = by_nymphs)) {
    
      # Set substrings to start IRIS from R
      iris_larvae <- paste0("-l ", get_initial_larvae(year, initial_larvae))
      iris_nymphs <- paste0("-n ", initial_nymphs)
      iris_weather <- paste0("-w ", weather_directory, "weather_", year, ".csv")
      iris_output <- paste0("-o" , 
                            output_folder, "/iris_abundance_", 
                            year, "_", 
                            initial_larvae, "_", 
                            initial_nymphs, ".csv")
      
      # Combine sub strings  
      iris_run <- paste(iris_exe, 
                        iris_jar, 
                        iris_weather, 
                        iris_seed, 
                        iris_output, 
                        iris_larvae,
                        iris_nymphs,
                        iris_adults,
                        sep = " ")
        
      # Run IRIS
      system(iris_run)
    }
  }
}
