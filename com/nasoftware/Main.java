package com.nasoftware;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        args = new String[1];
        args[0] = "22000";
        if(args.length < 1)
        {
            System.out.println("no enough arguments!");
            return;
        }
        if(args.length >= 2)
        {
            System.out.println("too much arguments");
            return;
        }
        ServerManager serverManager = new ServerManager(Integer.parseInt(args[0]));
        serverManager.start();
        InnerViewerManager.startInnerView();
    }
}
