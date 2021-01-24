/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.properties;

/**
 *
 * @author Omistaja
 */
public class FileCopierProperties {
    private int stackWriteTimeoutInSeconds = 60;
    private int stackReadTimeoutInSeconds = 60;
    private int stackSize = 20;

    public int getStackWriteTimeoutInSeconds() {
        return this.stackWriteTimeoutInSeconds;
    }

    public void setStackWriteTimeoutInSeconds(int pStackWriteTimeoutInSeconds) {
        this.stackWriteTimeoutInSeconds = pStackWriteTimeoutInSeconds;
    }

    public int getStackReadTimeoutInSeconds() {
        return this.stackReadTimeoutInSeconds;
    }

    public void setStackReadTimeoutInSeconds(int pStackReadTimeoutInSeconds) {
        this.stackReadTimeoutInSeconds = pStackReadTimeoutInSeconds;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int pStackSize) {
        this.stackSize = pStackSize;
    }
}
