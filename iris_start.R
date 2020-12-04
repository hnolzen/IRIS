# R-Script to start IRIS with input parameters

iris <- function(year_start, 
                year_end,
                random_seed,
                from_larvae,
                to_larvae,
                by_larvae,
                from_nymphs,
                to_nymphs,
                by_nymphs,
                defualt_number_adults,
                weather_directory
                ) {
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
}


