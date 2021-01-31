/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.commandlineargs;

import java.nio.charset.Charset;

/**
 *
 * @author Omistaja
 */
public class CommandLineArgParser {
    public static CommandLineArgParseResult parseCommandLineArgs(String[] args) {
        if (args.length < 2 || args.length > 3) {
            return new CommandLineArgParseResult("At least two arguments should be given. First argument is source file path. "
                + "Second is destination file path. Optionally, you can give file encoding like this: UTF-8.");
        } else if (args.length == 2) {
            return new CommandLineArgParseResult(args[0], args[1]);
        } else {
            return new CommandLineArgParseResult(args[0], args[1], Charset.forName(args[2]));
        }       
    }
}
