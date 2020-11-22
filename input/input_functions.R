# Calculate the fructifiction index of a given year y
get_fructification_index <- function(y) {
  
  # Fructification index data from Brugger et al. 2018
  mast_year <- c(2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016) 
  mast_index <- c(2, 1, 4, 1, 4, 1, 2, 2, 1, 4)
  
  fructification <- as.data.frame(cbind(mast_year, mast_index))
  colnames(fructification) <- c("year", "index")
  
  fructification_index <- subset(fructification, year == y)$index
  
  if (length(fructification_index) == 0) {
    return(4)
  } else {
    return(fructification_index)
  }
}
