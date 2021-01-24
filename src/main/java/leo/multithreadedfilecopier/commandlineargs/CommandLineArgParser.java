/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.commandlineargs;

/**
 *
 * @author Omistaja
 */
public class CommandLineArgParser {
    public static CommandLineArgParseResult parseCommandLineArgs(String[] args) {
        if (args.length != 2) {
            return new CommandLineArgParseResult("Two arguments should be given. First argument is source file path. "
                + "Second is destination file path.");
        }
        
        return new CommandLineArgParseResult(args[0], args[1]);
    }
}
