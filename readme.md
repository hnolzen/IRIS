# IRIS - Ixodes RIcinus Simulator 

IRIS is a population model for local dynamics of Ixodes ricinis ticks under changing climate parameters. It is designed as a spatially-explicit compartmental model that operates on a local scale. We plan to add a transmission model for borrelian dynamics in the near future.

This model is being developed as part of the [HICAM project][HICAM].

## Run

### Parameterisation

Command line arguments for model parameterisation:

| parameter    | default     | description                                                                                                                          |
|--------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------|
| -s           | 42          | random seed. Integer values >= 0 are valid                                                                                           |
| -l           | -           | initial number of inactive larvae. Integer values >= 0 are valid                                                                     |
| -w           | -           | path to weather input file                                                                                                           |
| -o           | -           | path to set directory of output file                                                                                                 |


## Documents and folders

| file / folder                                          | description                                                                                                         |
|--------------------------------------------------------|-------------------------------------------------------------------------------------------|
| [ODD.pdf](documents/odd/iris_odd.pdf)                  | Model description (ODD protocol)                                                          |
| `documents`                                            | Model documentation files                                                                 |
| `experiments`                                          | Location of files to run various predefined experiments (e.g. sensitivity analyses)       |
| `input`                                                | Location of input files (weather data, fructification data)                               |
| `output`                                               | Location of output files                                                                  |
| `plots`                                                | Location of scripts to visualise model outputs                                            |
| `src`                                                  | Java source files                                                                         |


[HICAM]: https://www.ufz.de/index.php?en=47573