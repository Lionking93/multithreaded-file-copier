/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.commandlineargs;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Omistaja
 */
public class CommandLineArgParseResult {
    private final String sourceFile;
    private final String destFile;
    private final boolean parseSuccessful;
    private final Charset fileEncoding;
    private final String errorText;
    
    public CommandLineArgParseResult(String pSourceFile, String pDestFile) {
        this.sourceFile = pSourceFile;
        this.destFile = pDestFile;
        this.parseSuccessful = true;
        this.fileEncoding = StandardCharsets.UTF_8;
        this.errorText = "";
    }
    
    public CommandLineArgParseResult(String pSourceFile, String pDestFile, Charset pFileEncoding) {
        this.sourceFile = pSourceFile;
        this.destFile = pDestFile;
        this.parseSuccessful = true;
        this.fileEncoding = pFileEncoding;
        this.errorText = "";
    }
    
    public CommandLineArgParseResult(String pErrorText) {
        this.sourceFile = "";
        this.destFile = "";
        this.parseSuccessful = false;
        this.fileEncoding = StandardCharsets.UTF_8;
        this.errorText = pErrorText;
    }
    
    public String getSourceFile() {
        return this.sourceFile;
    }
    
    public String getDestFile() {
        return this.destFile;
    }
    
    public boolean isParseSuccessful() {
        return this.parseSuccessful;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public Charset getFileEncoding() {
        return this.fileEncoding;
    }
}
