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


public class ServerManager extends Thread {
    private ServerSocket serverSocket;
    private String password;
    private boolean matched;
    public ServerManager(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        password = "password";
	// modify the password here to change password, remember also change the password in Client
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
                Executor executor = new Executor(in, out);
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


class Executor extends Thread
{
    private int[] instructionMap;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean logInStatus;
    private boolean senderStarted;
    private String currentAccount;

    public Executor(DataInputStream in, DataOutputStream out)
    {
        instructionMap = new int[6];
        instructionMap[0] = 0;
        instructionMap[1] = 3;
        instructionMap[2] = 2;
        instructionMap[3] = 2;
        instructionMap[4] = 3;
        instructionMap[5] = 1;
        this.in = in;
        this.out = out;
        this.logInStatus = false;
        this.senderStarted = false;
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
            } finally {
                if(logInStatus && !senderStarted)
                {
                    senderStarted = false;
                }
            }
        }
    }

    private int executeInstruction(String instruction) throws IOException {
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
            case 5:
                System.out.println("case 5");
                if(!logInStatus||!currentAccount.equals(parts[1]))
                    return -1 * instruct;
                String[] messages = MessageManager.getUnreadMessages(parts[1]);
                if(messages != null)
                {
                    for(String x: messages) {
                        out.writeUTF("5-" + x);
                        System.out.println("sent :" + x);
                    }
                    return 100;
                }
                System.out.println("unread message is empty!");
                return -1 * instruct;
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
                case -1: case -2: case -3: case -4: case -5:
                    out.writeUTF(errorCode*(-1) + "-0");
			        break;
                default: break;
            }
        } catch (IOException e){
         //   e.printStackTrace();
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
    GetMessages 5       1 Content
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
