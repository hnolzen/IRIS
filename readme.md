# IRIS - Ixodes RIcinus Simulator 

IRIS is a population model for local dynamics of Ixodes ricinis ticks under changing climate parameters. We plan to add a transmission model for borrelian dynamics in the near future.

It is designed as a spatially-explicit compartmental model that operates on a local scale.

It is being developed developed as part of the [HICAM project][HICAM].

## Run

### Parameterisation

Command line arguments for model parameterisation:

| parameter    | default     | description                                                                                                                          |
|--------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------|
| -s           | 42          | random seed. Integer values > 0 are valid                                                                                            |
| -f           | 4           | fructification index of the European beech (Fagus sylvatica). Valid Integer values: 1 = absent, 2 = scarce, 3 = common, 4 = full)    |
| -w           | -           | path to weather input file                                                                                                           |


## Documents and folders

| file / folder                                          | description                                                                                                         |
|--------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| tba                                                    | Model description (ODD protocol)                                                                                    |
| `documents`                                            | Model documentation files                                                                                           |
| `input`                                                | Location of input files (weather data, fructification data)                                                         |
| `output`                                               | Location of output files                                                                                            |
| `src`                                                  | Java source files                                                                                                   |


[HICAM]: https://www.ufz.de/index.php?en=47573