package eu.ecoepi.iris.experiments;

import org.apache.commons.cli.*;

import eu.ecoepi.iris.Model;
import java.util.Locale;

public class AdHocSimulation {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);

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
                .longOpt("inactiveLarvae")
                .build());

        cmdOptions.addOption(Option.builder("n")
                .hasArg()
                .longOpt("inactiveNymphs")
                .build());

        cmdOptions.addOption(Option.builder("a")
                .hasArg()
                .longOpt("inactiveAdults")
                .build());

        cmdOptions.addOption(Option.builder("i")
                .hasArg()
                .longOpt("infectedInactiveLarvae")
                .build());

        cmdOptions.addOption(Option.builder("j")
                .hasArg()
                .longOpt("infectedInactiveNymphs")
                .build());

        cmdOptions.addOption(Option.builder("u")
                .hasArg()
                .longOpt("rodents")
                .build());

        cmdOptions.addOption(Option.builder("v")
                .hasArg()
                .longOpt("infectedRodents")
                .build());

        cmdOptions.addOption(Option.builder("r")
                .hasArg()
                .longOpt("activation")
                .build());
                
        cmdOptions.addOption(Option.builder("m")
                .hasArg()
                .longOpt("output_mode")
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(cmdOptions, args);

        var options = new Model.Options();

        options.seed = Long.parseLong(cmd.getOptionValue("s", "42"));
        options.output = cmd.getOptionValue("o");
        options.weather = cmd.getOptionValue("w");

        options.initialInactiveLarvae = Integer.parseInt(cmd.getOptionValue("l", "150"));
        options.initialInactiveNymphs = Integer.parseInt(cmd.getOptionValue("n", "150"));
        options.initialInactiveAdults = Integer.parseInt(cmd.getOptionValue("a", "150"));
        options.initialInfectedInactiveLarvae = Integer.parseInt(cmd.getOptionValue("i", "0"));
        options.initialInfectedInactiveNymphs = Integer.parseInt(cmd.getOptionValue("j", "0"));

        options.initialRodents = Integer.parseInt(cmd.getOptionValue("u", "10"));
        options.initialInfectedRodents = Integer.parseInt(cmd.getOptionValue("v", "0"));

        options.activationRate = Float.parseFloat(cmd.getOptionValue("r", "0.05"));
        
        options.outputMode = cmd.getOptionValue("m");

        Model.run(options);
    }
}