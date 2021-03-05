# R-Script to start IRIS with input parameters

iris <- function(year, 
                 random_seed,
                 initial_larvae,
                 initial_nymphs,
                 initial_adults,
                 activation_rate,
                 weather_directory,
                 output_summary
                 ) {
  
  # Set directory of FatJar to run the model
  iris_jar_directory <- paste0(iris_main_directory, "/target/")
  
  # Set model input directory
  iris_input_directory <- paste0(iris_main_directory, "/input/")
  
  # Get functions from input source file
  input_functions <- paste0(iris_input_directory, "input_functions.r")
  source(input_functions)
  
  # Set model output directory
  output_directory <- paste0(iris_main_directory, "/output/")
  
  # Create output folder for current simulation run
  current_date <- as.character(Sys.Date())
  output_folder <- paste(output_directory, current_date, sep = "")
  dir.create(output_folder)
  
  # Set substrings to start IRIS from R
  iris_exe <- "java.exe"
  iris_jar <- paste0("-jar ", iris_jar_directory, "IRIS-1.0-SNAPSHOT-jar-with-dependencies.jar eu.ecoepi.iris.experiments.AdHoc")
  iris_seed <- paste0("-s ", random_seed)
  iris_adults <- paste0("-a ", initial_adults)
  iris_larvae <- paste0("-l ", get_initial_larvae(year, initial_larvae))
  iris_nymphs <- paste0("-n ", initial_nymphs)
  iris_activation_rate <- paste0("-r ", activation_rate)
  iris_weather <- paste0("-w ", weather_directory, "weather_", year, ".csv")
  iris_output <- paste0("-o" , 
                        output_folder, "/iris_abundance_", 
                        year, "_", 
                        initial_larvae, "_", 
                        initial_nymphs, "_",
                        activation_rate,
                        ".csv")
  iris_output_summary <- paste0("-u ", output_summary)
  
  # Combine sub strings  
  iris_run <- paste(iris_exe, 
                    iris_jar, 
                    iris_weather, 
                    iris_seed, 
                    iris_output, 
                    iris_larvae,
                    iris_nymphs,
                    iris_adults,
                    iris_activation_rate,
                    iris_output_summary,
                    sep = " ")
  
  # Run IRIS
  system(iris_run)
}
