package com.nasoftware;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
/*		//using this three lines if you are using IDE to test this program.
        args = new String[2];
        args[0] = "8";
        args[1] = "22000";
*/
        if(args.length <= 1)
        {
            System.out.println("no enough arguments!");
            return;
        }
        if(args.length > 2)
        {
            System.out.println("too much arguments");
            return;
        }
        ServerManager serverManager = new ServerManager(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        serverManager.runServers();
        InnerViewerManager.startInnerView();
    }
}
