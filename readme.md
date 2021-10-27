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
| -r           | 0.02        | activation rate                        | Float values >= 0.0 are valid      |
| -u           | true       | write only summary outputs             |                                    | 

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