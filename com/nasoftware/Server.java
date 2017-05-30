package com.nasoftware;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by zeyongshan on 5/26/17.
 */


public class Server extends Thread {
    private ServerSocket serverSocket;
    private String password;
    private boolean matched;
    public Server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        password = "BIANhao5213";
    }

    public void run() {
        while (true)
        {
            try
            {
                Socket server = serverSocket.accept();
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                String password = in.readUTF();
                if(!this.password.equals(password)) {
                    out.writeUTF("wrong!");
                    server.close();
                    continue;
                }
                Executor executor = new Executor(in, out, server);
                executor.start();
                System.out.println("port " + serverSocket.getLocalPort() + " just connected!");
            } catch (SocketTimeoutException e) {
//                System.out.println("port " + serverSocket.getLocalPort() + " time out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


/*
Stream explanation (In):
    CommandID-Content1-Content2-...
    Close       0       0 Content
    SignUp:     1       3 Content
    LogIn:      2       2 Content
    AddFriend   3       2 Content
    SendMessage 4       3 Content
*/

/*
Stream explanation (Out):
    CommandID-Content1-Content2-...
    WrongFormat 0       0
    SignUp:     1       1
    LogIn:      2       1
    AddFriend   3       1
    SendMessage 4       1
    NewMessage  5       1
*/

class Executor extends Thread
{
    private int[] instructionMap;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket server;
    private boolean logInStatus;
    private String currentAccount;

    public Executor(DataInputStream in, DataOutputStream out, Socket server)
    {
        instructionMap = new int[5];
        instructionMap[0] = 0;
        instructionMap[1] = 3;
        instructionMap[2] = 2;
        instructionMap[3] = 2;
        instructionMap[4] = 3;
        this.in = in;
        this.out = out;
        this.server = server;
        this.logInStatus = false;
    }

    public void run()
    {
        while (true)
        {
            try {
                String instruction = in.readUTF();
                int result = executeInstruction(instruction);
                returnMessages(result);
            } catch (IOException e) {
                //e.printStackTrace();
                break;
            }
        }
    }
    /* error code explain:
        0:  wrong format instruction.
        1:  signUp successful!
        2:  logIn successful!
        3:  addFriend successful!
        4:  sendMessage successful!
        -1: signUp fail.
        -2: logIn fail.
        -3: addFriend fail.
        -4: sendMessage fail.
    */
    private int executeInstruction(String instruction)
    {
        String[] parts = instruction.split("-");
        Integer instruct = Integer.parseInt(parts[0]);
        if(instruct == null)
        {
            return 0;
        }
        if(parts.length - 1 != instructionMap[instruct])
        {
            return 0;
        }
        boolean result;
        switch (instruct)
        {
            case 1:
                result = AccountManager.signUp(parts[1], parts[2], parts[3]);
                if(!result)
                    return -1 * instruct;
                this.logInStatus = true;
                this.currentAccount = parts[1];
                break;
            case 2:
                result = AccountManager.logIn(parts[1], parts[2]);
                if(!result)
                    return -1 * instruct;
                this.logInStatus = true;
                this.currentAccount = parts[1];
                break;
            case 3:
                if(!logInStatus||!currentAccount.equals(parts[1]))
                    return -1 * instruct;
                result = FriendManager.addFriend(parts[1], parts[2]);
                if(!result)
                    return -1 * instruct;
                break;
            case 4:
                if(!logInStatus||!currentAccount.equals(parts[1]))
                    return -1 * instruct;
                result = MessageManager.sendMessageTo(parts[1], parts[2], parts[3]);
                if(!result)
                    return -1 * instruct;
                break;
            default: return 0;
        }
        return instruct;
    }

    private void returnMessages(int errorCode)
    {
        try {
            switch (errorCode) {
                case 0:
                    out.writeUTF("0");
			        break;
                case 1: case 2: case 3: case 4:
                    out.writeUTF(errorCode + "-1");
			        break;
                case -1: case -2: case -3: case -4:
                    out.writeUTF(errorCode*(-1) + "-0");
			        break;
                default: throw new IOException();
            }
        } catch (IOException e){
         //   e.printStackTrace();
        }
    }

}
