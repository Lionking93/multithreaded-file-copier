/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier.properties;

/**
 *
 * @author Omistaja
 */
public class FileCopierProperties {
    private int queueWriteTimeoutInSeconds = 60;
    private int queueReadTimeoutInSeconds = 60;
    private int queueSize = 20;

    public int getQueueWriteTimeoutInSeconds() {
        return queueWriteTimeoutInSeconds;
    }

    public void setQueueWriteTimeoutInSeconds(int queueWriteTimeoutInSeconds) {
        this.queueWriteTimeoutInSeconds = queueWriteTimeoutInSeconds;
    }

    public int getQueueReadTimeoutInSeconds() {
        return queueReadTimeoutInSeconds;
    }

    public void setQueueReadTimeoutInSeconds(int queueReadTimeoutInSeconds) {
        this.queueReadTimeoutInSeconds = queueReadTimeoutInSeconds;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
