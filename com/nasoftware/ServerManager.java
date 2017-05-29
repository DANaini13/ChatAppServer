package com.nasoftware;

import java.io.IOException;

/**
 * Created by zeyongshan on 5/26/17.
 */
public class ServerManager {

    private int startPort;
    private Server[] servers;

    public ServerManager(int serverNumber, int startPort)
    {
        this.startPort = startPort;
        servers = new Server[serverNumber];
    }

    public void runServers() throws IOException
    {
        int len = servers.length;
        for(int i=0; i<len; ++i)
        {
            servers[i] = new Server(startPort + i);
	        servers[i].start();
        }
    }

}
