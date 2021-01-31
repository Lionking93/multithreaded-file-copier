package leo.multithreadedfilecopier.commandlineargs;

import java.nio.charset.Charset;

/**
 * Helper class for handling command line args. If too few or too many command line args,
 * show help text. Otherwise store the results to CommandLineArgParseResult object.
 * 
 * @author Leo Kallonen
 */
public class CommandLineArgParser {
    public static CommandLineArgParseResult parseCommandLineArgs(String[] args) {
        if (args.length < 2 || args.length > 3) {
            return new CommandLineArgParseResult("At least two arguments should be given. First argument is source file path. "
                + "Second is destination file path. Optionally, you can give file encoding like this: UTF-8.");
        } else if (args.length == 2) {
            // Command line args contain source file path and destination file path.
            return new CommandLineArgParseResult(args[0], args[1]);
        } else {
            // Command line args contain source file path, destination file path and chosen file encoding.
            return new CommandLineArgParseResult(args[0], args[1], Charset.forName(args[2]));
        }       
    }
}
