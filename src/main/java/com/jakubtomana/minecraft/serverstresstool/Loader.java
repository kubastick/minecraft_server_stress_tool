package com.jakubtomana.minecraft.serverstresstool;

import org.apache.commons.cli.*;

public class Loader {
    public static void main(String[] args)
    {
        //Stresser st = new Stresser("survivalcore.eu",25565,20,"bottest",5000);
        Options options = new Options();

        Option serverAdress = new Option("a", "ip", true, "server adress");
        serverAdress.setRequired(true);
        options.addOption(serverAdress);

        Option port = new Option("p", "port", true, "port number");
        port.setRequired(true);
        options.addOption(port);

        Option threadnum = new Option("t", "threadnum", true, "threads (bots) number");
        threadnum.setRequired(true);
        options.addOption(threadnum);

        Option basenick = new Option("n", "nick", true, "base string for nicknames");
        basenick.setRequired(true);
        options.addOption(basenick);

        Option logincommand = new Option("l", "logincmd", true, "login command withcout slash");
        logincommand.setRequired(true);
        options.addOption(logincommand);

        Option registercommand = new Option("r", "registercmd", true, "register command witchout slash");
        registercommand.setRequired(true);
        options.addOption(registercommand);

        Option delay = new Option("d", "delay", true, "delay in ms between bot joins (default 5000ms)");
        delay.setRequired(false);
        options.addOption(delay);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(0);
            return;
        }
        int delayT = 5000;
        delayT = Integer.parseInt(cmd.getOptionValue("delay"));
        Stresser st = new Stresser(
                cmd.getOptionValue("ip"),
                Integer.parseInt(cmd.getOptionValue("port")),
                Integer.parseInt(cmd.getOptionValue("threadnum")),
                cmd.getOptionValue("nick"),
                cmd.getOptionValue("logincmd"),
                cmd.getOptionValue("registercmd"),
                delayT);
        //st.getServerInfo();
        st.startStressTest();
    }
}

