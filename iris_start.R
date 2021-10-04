iris <- function(year, 
                 random_seed,
                 initial_larvae,
                 initial_nymphs,
                 initial_adults,
                 initial_infected_larvae,
                 initial_infected_nymphs,
                 activation_rate,
                 iris_main_dir,
                 weather_dir,
                 output_mode,
                 model_name
                 ) {
  
  iris_jar_dir <- paste0(iris_main_dir, "/target/")
  iris_input_dir <- paste0(iris_main_dir, "/input/")
  output_dir <- paste0(iris_main_dir, "/output/", model_name)
  
  if (!dir.exists(output_dir)){
    dir.create(output_dir, showWarnings = FALSE)
  }
  
  iris_exe <- "java.exe"
  iris_jar <- paste0("-jar ", iris_jar_dir, "IRIS-1.0-SNAPSHOT-jar-with-dependencies.jar eu.ecoepi.iris.experiments.AdHocSimulation")
  iris_seed <- paste0("-s ", random_seed)
  iris_adults <- paste0("-a ", initial_adults)
  iris_larvae <- paste0("-l ", initial_larvae)
  iris_nymphs <- paste0("-n ", initial_nymphs)
  iris_infected_larvae <- paste0("-i ", initial_infected_larvae)
  iris_infected_nymphs <- paste0("-j ", initial_infected_nymphs)
  iris_activation_rate <- paste0("-r ", activation_rate)
  iris_weather <- paste0("-w ", weather_dir, "weather_", year, ".csv")
  iris_output <- paste0("-o " , output_dir, "/iris_abundance_", year, ".csv")
  iris_output_mode <- paste0("-m ", output_mode)
  
  iris_run <- paste(iris_exe, 
                    iris_jar, 
                    iris_weather, 
                    iris_seed, 
                    iris_output, 
                    iris_larvae,
                    iris_nymphs,
                    iris_adults,
                    iris_infected_larvae,
                    iris_infected_nymphs,
                    iris_activation_rate,
                    iris_output_mode,
                    sep = " ")
  
  system(iris_run)
}


iris_main_dir <- dirname(rstudioapi::getSourceEditorContext()$path)
location <- "regensburg"
year_start <- 1949
year_end <- 2020
climate_simulation <- FALSE
random_seed <- 42
initial_larvae <- 150
initial_nymphs <- 150
initial_adults <- 150
initial_infected_larvae <- 0
initial_infected_nymphs <- 0
activation_rate <- 0.022
#output_mode <- "csv_timeseries"
#output_mode <- "csv_timeseries_summary"
#output_mode <- "csv_timeseries_summary_habitats"
#output_mode <- "csv_timeseries_nymphs"
#output_mode <- "csv_timeseries_nymphs_habitats"
output_mode <- "csv_timeseries_infection"

weather_dir <- paste0(iris_main_dir, "/input/weather/dwd_", location, "/")

if (climate_simulation) {
  #model_name <- "All_models_daily_mean"
  #model_name <- "CCCma-CanESM2_rcp85_r1i1p1_CLMcom-CCLM4-8-17_v1"
  #model_name <- "CCCma-CanESM2_rcp85_r1i1p1_GERICS-REMO2015_v1"
  #model_name <- "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_IPSL-WRF381P_v1"
  #model_name <- "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_KNMI-RACMO22E_v1"
  #model_name <- "IPSL-IPSL-CM5A-MR_rcp85_r1i1p1_SMHI-RCA4_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_CNRM-ALADIN63_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_DMI-HIRHAM5_v2"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_GERICS-REMO2015_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_ICTP-RegCM4-6_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_IPSL-WRF381P_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_KNMI-RACMO22E_v2"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_MOHC-HadREM3-GA7-05_v1"
  #model_name <- "MOHC-HadGEM2-ES_rcp85_r1i1p1_SMHI-RCA4_v1"
  model_name <- "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_GERICS-REMO2015_v1"
  #model_name <- "MPI-M-MPI-ESM-LR_rcp85_r3i1p1_SMHI-RCA4_v1"
  
  weather_dir <- paste0(iris_main_dir, "/input/climate/", model_name, "/csv_", location, "/")  
} else {
  model_name <- "DWD"
}

for (year in year_start : year_end) {
  iris(year, 
       random_seed,
       initial_larvae,
       initial_nymphs,
       initial_adults,
       initial_infected_larvae,
       initial_infected_nymphs,
       activation_rate,
       iris_main_dir,
       weather_dir,
       output_mode,
       model_name) 
}
