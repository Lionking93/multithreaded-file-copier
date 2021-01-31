/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.properties;

/**
 *
 * @author Leo Kallonen
 */
public class FileCopierProperties {
    private int bufferSize = 8192;

    public int getBufferSize() {
        return bufferSize;
    }

    public void getBufferSize(int pBufferSize) {
        this.bufferSize = pBufferSize;
    }
}
