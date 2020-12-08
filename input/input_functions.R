# Calculate the fructification index of a given year y
get_fructification_index <- function(y) {
  
  # Fructification index data from Brugger et al. 2018
  mast_year <- c(2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016) 
  mast_index <- c(2, 1, 4, 1, 4, 1, 2, 2, 1, 4)
  
  fructification <- as.data.frame(cbind(mast_year, mast_index))
  colnames(fructification) <- c("year", "index")
  fructification_index <- subset(fructification, year == y)$index
  
  if (length(fructification_index) == 0) {
    return(2)
  } else {
    return(fructification_index)
  }
}


get_initial_larvae <- function(year, number_larvae) {
  
  index <- get_fructification_index(year - 2)
  
  if (index == 1) {
    rate = 0.25;
  } else if (index == 2) {
    rate = 0.5;
  } else if (index == 3) {
    rate = 0.75;
  } else {
    rate = 1.0;
  }
  
  initial_number_larvae <- round(rate * number_larvae, digits = 0)
    
  return(initial_number_larvae)
}


get_weather_directory <- function(climate_simulations, 
                                  iris_main_directory, 
                                  climate_model, 
                                  dwd_data,
                                  slope_one) {
  
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
}
