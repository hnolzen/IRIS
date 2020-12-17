package eu.ecoepi.iris.experiments;

import org.apache.commons.cli.*;

import eu.ecoepi.iris.Model;

public class AdHoc {
    public static void main(String[] args) throws Exception {
        Options cmdOptions = new Options();

        cmdOptions.addOption(Option.builder("s")
                .hasArg()
                .longOpt("seed")
                .build());

        cmdOptions.addOption(Option.builder("w")
                .hasArg()
                .longOpt("weather")
                .required()
                .build());

        cmdOptions.addOption(Option.builder("o")
                .hasArg()
                .longOpt("output")
                .required()
                .build());

        cmdOptions.addOption(Option.builder("l")
                .hasArg()
                .longOpt("larvae")
                .build());

        cmdOptions.addOption(Option.builder("n")
                .hasArg()
                .longOpt("nymphs")
                .build());

        cmdOptions.addOption(Option.builder("a")
                .hasArg()
                .longOpt("adults")
                .build());

        cmdOptions.addOption(Option.builder("r")
                .hasArg()
                .longOpt("activation")
                .build());

        cmdOptions.addOption(Option.builder("p")
                .hasArg()
                .longOpt("precipitation")
                .build());


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(cmdOptions, args);

        var options = new Model.Options();

        options.seed = Long.parseLong(cmd.getOptionValue("s", "42"));

        options.output = cmd.getOptionValue("o");

        options.weather = cmd.getOptionValue("w");
        options.withPrecipitation = cmd.hasOption("p");

        options.initialLarvae = Integer.parseInt(cmd.getOptionValue("l", "150"));
        options.initialNymphs = Integer.parseInt(cmd.getOptionValue("n", "150"));
        options.initialAdults = Integer.parseInt(cmd.getOptionValue("a", "150"));
        
        options.activationRate = Float.parseFloat(cmd.getOptionValue("r", "0.05"));
                    
        Model.run(options);
    }
}