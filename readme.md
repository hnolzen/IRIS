# IRIS - Ixodes RIcinus Simulator 

IRIS (Ixodes RIcinus Simulator) is a population model for local dynamics of Ixodes ricinus ticks under changing climate. It is designed as a
 spatially-explicit compartmental model that operates on a local scale. We plan to add a transmission model for borrelian dynamics in the near future.

This model is being developed as part of the [HICAM project][HICAM].

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

The following available output observers  

| observer                         | ID  | description                                          | 
|----------------------------------|-----|------------------------------------------------------|
| csv_timeseries                   | 1   | writes out all output variables.                     |
| csv_timeseries_summary           | 2   | writes out summary output variables, i.e. the mean of all grid cells. |
| csv_timeseries_summary_habitats  | 3   | observer 2 differentiated by habitats.               | 
| csv_timeseries_nymphs            | 4   | writes out all output variables relevant for the analysis of questing nymphs. | 
| csv_timeseries_nymphs_habitats   | 5   | observer 4 differentiated by habitats.               |
| csv_timeseries_infection         | 6   | writes out all output variables that are relevant for the analysis of the borreliosis dynamics. |

## Documents and folders
| document                      | description                                                              |
|-------------------------------|--------------------------------------------------------------------------|
| [ODD.pdf][ODD]                | Model description (ODD protocol)                                         |


## Model files and folders

| file / folder        | description                                                                       |
|----------------------|-----------------------------------------------------------------------------------|
| `iris_start.py`      | Python Script to run IRIS                                                         |
| `documents`          | Location of model documentation files (ODD protocol, TRACE documentation)         |
| `experiments`        | Location of script files to run sensitivity analyses, perform parameter optimisation, etc.  |
| `input`              | Location of input files (weather data, fructification data)                       |
| `output`             | Location of model output files                                                    |
| `plots`              | Location of script files to visualise model outputs                               |
| `src`                | Location of Java source files                                                     |


### Contact

If you have questions regarding the research of the IRIS modelling project, please [create an issue][ISSUE] or contact us via email.


[HICAM]: https://www.ufz.de/index.php?en=47573
[ODD]: https://git.ufz.de/ecoepi/iris/-/jobs/artifacts/master/raw/iris_odd.pdf?job=build
[TRACE]: documents/trace/iris_trace.pdf
[ISSUE]: https://git.ufz.de/ecoepi/iris/-/issues