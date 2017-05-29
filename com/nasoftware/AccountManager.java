package com.nasoftware;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zeyongshan on 5/25/17.
 */


public class AccountManager{
    static public PersonSet accountSet = new PersonSet();
    static public boolean logIn(String email, String password)
    {
        LockHolder.lock.lock();
        Person logInPerson = new Person(email, password, "");
        if(accountSet.FindMatch(logInPerson))
        {
            LockHolder.lock.unlock();
            return true;
        }
        LockHolder.lock.unlock();
        return false;
    }

    static public boolean signUp(String email, String password, String name)
    {
        LockHolder.lock.lock();
        boolean result =  accountSet.addPerson(email, password, name);
        LockHolder.lock.unlock();
        return result;
    }

    static String getInfo()
    {
        LockHolder.lock.lock();
        String result =  accountSet.toString();
        LockHolder.lock.unlock();
        return result;
    }

}

class MessageManager
{
    static public boolean sendMessageTo(String fromEmail, String toEmail, String message)
    {
        LockHolder.lock.lock();
        boolean result = AccountManager.accountSet.sendMessage(fromEmail, toEmail, message);
        LockHolder.lock.unlock();
        return result;
    }

    static String getInfo()
    {
        LockHolder.lock.lock();
        String result =  AccountManager.accountSet.getConversion();
       	LockHolder.lock.unlock();
        return result;
    }

}

class FriendManager
{
    static public boolean addFriend(String fromEmail, String toEmail)
    {
        LockHolder.lock.lock();
        boolean result = AccountManager.accountSet.addFriend(fromEmail, toEmail);
        LockHolder.lock.unlock();
        return result;
    }
}

class LockHolder {
    static public Lock lock = new ReentrantLock();
}
