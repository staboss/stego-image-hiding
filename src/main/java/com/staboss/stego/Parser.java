package com.staboss.stego;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public final class Parser {

    @Option(name = "-e", usage = "embed message", forbids = {"-d"})
    public boolean embed = false;

    @Option(name = "-d", usage = "extract message", forbids = {"-e"})
    public boolean extract = false;

    @Option(name = "-s", usage = "source image", required = true, metaVar = "FILE")
    public String sourceFile;

    @Option(name = "-r", usage = "result image", metaVar = "IMAGE")
    public String resultFile;

    @Option(name = "-t", usage = "secret message", metaVar = "TEXT_FILE")
    public String messageFile;

    @Option(name = "-k", usage = "binary key file (required for DCT)", metaVar = "KEY_FILE")
    public String keyFile;

    @Option(name = "-m", usage = "DCT or LSB", required = true, metaVar = "METHOD")
    public String method;

    private static Parser parser = null;
    private static CmdLineParser cmdLineParser = null;

    private Parser() {
    }

    public static Parser getInstance() {
        if (parser == null) {
            parser = new Parser();
            cmdLineParser = new CmdLineParser(parser);
        }
        return parser;
    }

    public boolean parseArgs(String[] args) {
        try {
            cmdLineParser.parseArgument(args);
            File img = new File(sourceFile);
            if (!img.exists()) {
                throw new IllegalArgumentException("Source file does not exist!");
            }
            if ((!embed && !extract)) {
                throw new IllegalArgumentException("Program mode is not specified, check -e or -d options!");
            }
            if (method.isEmpty()) {
                throw new IllegalArgumentException("Method type is empty! Please, specify DCT or LSB");
            }
            if (embed && messageFile.isEmpty()) {
                throw new IllegalArgumentException("Secret message is required for embedding! Check option -m");
            }
            if (!method.toLowerCase().equals("dct") && !method.toLowerCase().equals("lsb")) {
                throw new IllegalArgumentException("Method type does not exist! Please, specify DCT or LSB");
            }
            if (method.toLowerCase().equals("dct")) {
                if (keyFile == null || keyFile.isEmpty()) {
                    throw new IllegalArgumentException("Key file does not exist! This is necessary for DCT!");
                }
                File key = new File(keyFile);
                if (!key.exists()) {
                    throw new IllegalArgumentException("Key file does not exist! This is necessary for DCT!");
                }
            }
            return true;
        } catch (IllegalArgumentException | CmdLineException e) {
            System.err.println(e.getMessage() + "\n");
            usage();
            return false;
        }
    }

    public static void usage() {
        System.err.println("usage: java -jar stego-image-hiding.jar -e|-d -m METHOD -s IMAGE [-r IMAGE] [-t TEXT_FILE] [-k KEY_FILE]\n");
        System.err.println(arguments);
    }

    private static final String arguments = "optional arguments:\n" +
            "  -d           : extract message\n" +
            "  -e           : embed message\n" +
            "  -s IMAGE     : source image\n" +
            "  -r IMAGE     : result image\n" +
            "  -m METHOD    : DCT or LSB\n" +
            "  -k KEY_FILE  : binary key file [required for DCT]\n" +
            "  -t TEXT_FILE : secret message";
}
