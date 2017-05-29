package com.nasoftware;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zeyongshan on 5/25/17.
 */


public class AccountManager{
    static public PersonSet accountSet = new PersonSet();
    static public Lock lock = new ReentrantLock();
    static public boolean logIn(String email, String password)
    {
        lock.lock();
        Person logInPerson = new Person(email, password, "");
        if(accountSet.FindMatch(logInPerson))
        {
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    static public boolean signUp(String email, String password, String name)
    {
        lock.lock();
        boolean result =  accountSet.addPerson(email, password, name);
        lock.unlock();
        return result;
    }

    static String getInfo()
    {
        lock.lock();
        String result =  accountSet.toString();
        lock.unlock();
        return result;
    }

}

class MessageManager
{
    static public boolean sendMessageTo(String fromEmail, String toEmail, String message)
    {
        AccountManager.lock.lock();
        boolean result = AccountManager.accountSet.sendMessage(fromEmail, toEmail, message);
        AccountManager.lock.unlock();
        return result;
    }

    static String getInfo()
    {
        AccountManager.lock.lock();
        String result =  AccountManager.accountSet.getConversion();
        AccountManager.lock.unlock();
        return result;
    }

}

class FriendManager
{
    static public boolean addFriend(String fromEmail, String toEmail)
    {
        AccountManager.lock.lock();
        boolean result = AccountManager.accountSet.addFriend(fromEmail, toEmail);
        AccountManager.lock.unlock();
        return result;
    }
}
