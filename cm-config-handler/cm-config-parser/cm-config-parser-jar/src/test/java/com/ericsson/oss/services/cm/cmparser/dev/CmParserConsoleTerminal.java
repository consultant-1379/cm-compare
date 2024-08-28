package com.ericsson.oss.services.cm.cmparser.dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.ericsson.oss.services.cm.cmparser.CmCommandLexer;
import com.ericsson.oss.services.cm.cmparser.CmCommandParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class CmParserConsoleTerminal {

    private CmParserConsoleTerminal() {
        // Private constructor added to silence g-d PMD Singleton Warning
    }

    public static void main(final String[] args) {

        boolean showTokens = false, pretty = false;

        displayMessage();

        outer: while (true) {

            displayPrompt();

            final String command = readCommandLine();

            switch (command) {
                case "":
                    break;
                case "pr":
                case "pretty":
                    pretty = !pretty;
                    System.out.println("Pretty json: " + pretty);
                    break;
                case "tk":
                case "tokens":
                    showTokens = !showTokens;
                    System.out.println("Show tokens: " + showTokens);
                    break;
                case "exit":
                    break outer;
                default:
                    parseCommand(command, showTokens, pretty);
            }

        }

        System.out.println("Slan leat mo chara...");
    }

    private static void displayMessage() {
        System.out.println();
        System.out.println("o==================================================o");
        System.out.println("|     << Welcome to CM Parser Test Terminal >>     |");
        System.out.println("o==================================================o");
        System.out.println("| Type commands or...                              |");
        System.out.println("| 'tokens' / 'tk' : Show parsed tokens from lexer  |");
        System.out.println("| 'pretty' / 'pr' : Pretty printed json            |");
        System.out.println("o==================================================o");
        System.out.println();
    }

    private static void parseCommand(final String command, final boolean showTokens, final boolean pretty) {
        final ANTLRInputStream antlrInputStream = new ANTLRInputStream(command);
        final CmCommandLexer lexer = new CmCommandLexer(antlrInputStream);

        if (showTokens) {
            printLexer(lexer);
        }

        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final CmCommandParser parser = new CmCommandParser(tokens);

        final ParseTree tree = parser.command();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.out.println(parser.getNumberOfSyntaxErrors() + " syntax errors");
            System.out.println(tree.toStringTree(parser));
            return;
        }

        System.out.println(tree.toStringTree(parser));

        final CmParserConsoleTerminalWalker consoleWalker = new CmParserConsoleTerminalWalker();

        final ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(consoleWalker, tree);
        if (consoleWalker.getParserResult() != null) {
            System.out.println("Happy Days!");
            printJson(consoleWalker.getParserResult(), pretty);
        } else {
            System.out.println("Sad Times, null command");
        }
    }

    private static void printJson(final Object object, final boolean pretty) {
        final Gson gson = pretty ? new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create() : new GsonBuilder().disableHtmlEscaping()
                .create();
        final String json = gson.toJson(object);
        System.out.println(json);
    }

    private static void printLexer(final CmCommandLexer lexer) {
        for (final Token t : lexer.getAllTokens()) {
            System.out.println("[" + lexer.getTokenNames()[t.getType()] + "] : " + t.getText());
        }
        lexer.reset();
    }

    private static void displayPrompt() {
        System.out.print("cmparser> ");
    }

    private static String readCommandLine() {
        String command = "";
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {

            command = bufferedReader.readLine();

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return command;
    }
}