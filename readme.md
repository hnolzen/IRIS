# IRIS - Ixodes RIcinus Simulator 

IRIS (Ixodes RIcinus Simulator) is a population model to simulate spatio-temporal dynamics of *Ixodes ricinus* ticks. It is designed as a
spatially-explicit compartmental model that operates on a local scale.

This model was developed in 2020 and 2021 for a research project. It has not been further developed since then. The original repository can be found at: https://git.ufz.de/ecoepi/iris

## Run

### Parameterisation

Command line arguments for model parameterisation:

| parameter    | default     | description                            | notes                              |
|--------------|-------------|----------------------------------------|------------------------------------|
| -s           | 42          | random seed.                           | Integer values >= 0 are valid      |
| -w           | -           | path to weather input file.            | Explicit specification is required |
| -o           | -           | path to set directory of output files. | Explicit specification is required |
| -l           | 150         | initial number of inactive larvae.     | Integer values >= 0 are valid      |
| -n           | 150         | initial number of inactive nymphs.     | Integer values >= 0 are valid      |
| -a           | 150         | initial number of inactive adults.     | Integer values >= 0 are valid      |
| -i           | 0           | initial number of infected larvae.     | Integer values >= 0 are valid      |
| -j           | 0           | initial number of inactive nymphs.     | Integer values >= 0 are valid      |
| -u           | 10          | initial number of susceptible rodents. | Integer values >= 0 are valid      |
| -v           | 0           | initial number of infected rodents.    | Integer values >= 0 are valid      |
| -r           | 0.022       | activation rate                        | Float values >= 0.0 are valid      |
| -m           | -           | set csv output observer.               | {1, 2, 3, 4, 5, 6}                 |


#### Available output observers:

The following output observers are available: 

| observer                         | ID  | description                                          | 
|----------------------------------|-----|------------------------------------------------------|
| csv_timeseries                   | 1   | writes out all output variables.                     |
| csv_timeseries_summary           | 2   | writes out summary output variables, i.e. the mean of all grid cells. |
| csv_timeseries_summary_habitats  | 3   | observer 2 differentiated by habitats.               | 
| csv_timeseries_nymphs            | 4   | writes out all output variables relevant for the analysis of questing nymphs. | 
| csv_timeseries_nymphs_habitats   | 5   | observer 4 differentiated by habitats.               |
| csv_timeseries_infection         | 6   | writes out all output variables that are relevant for the analysis of the borreliosis dynamics. |

## Model files and folders

| file / folder        | description                                                                       |
|----------------------|-----------------------------------------------------------------------------------|
| `iris_start.py`      | Python Script to run IRIS                                                         |
| `documents`          | Location of model documentation files           |
| `experiments`        | Location of script files to run sensitivity analyses, perform parameter optimisation, etc.  |
| `input`              | Location of input files (weather data, fructification data)                       |
| `output`             | Location of model output files                                                    |
| `plots`              | Location of script files to visualise model outputs                               |
| `src`                | Location of Java source files                                                     |
