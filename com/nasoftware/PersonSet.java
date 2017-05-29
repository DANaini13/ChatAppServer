package com.nasoftware;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zeyongshan on 5/25/17.
 */

class Account
{
    String email;
    String password;

    public Account(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Account))
            return false;
        return email.equals(((Account) obj).email);
    }

    public boolean matchPassword(Account acc)
    {
        return this.password.equals(acc.password);
    }

    public String toString()
    {
        String sum = "";
        sum += "Account: " + email;
        int length = sum.length();
        for(int i = 0; i<40 - length; ++i)
            sum += " ";
        sum += "Password: " + password;
        return sum;
    }
}

class Person extends Account
{
    public int test = 0;
    private ConversionList conversions;
    private ConcurrentList<Person> friends;
    private String name;

    public Person(String email, String password, String name)
    {
        super(email, password);
        this.name = name;
        conversions = new ConversionList();
        friends = new ConcurrentList<Person>();
    }

    public boolean addFriend(Person friend)
    {
        if(friends.contains(friend))
            return false;
        friends.add(friend);
        return true;
    }

    public boolean sendMessageTo(Person to, String message)
    {
        if(!friends.contains(to))
            return false;
        conversions.addConversion(to.email, to.name);
        return conversions.addMessageTo(to.email,  message, to.name);
    }

    public boolean sendMessageToSelf(Person to, String message)
    {
        if(!friends.contains(to))
            return false;
        conversions.addConversion(to.email, to.name);
        return conversions.addMessageTo(to.email,  message, this.name);
    }

    public String getConversion()
    {
        return conversions.toString();
    }

}

public class PersonSet {
    private ConcurrentList<Person> people = new ConcurrentList<Person>();

    public boolean addPerson(String email, String password, String name)
    {
        Person newPerson = new Person(email, password, name);
        if(people.contains(newPerson))
        {
            return false;
        }
        people.add(newPerson);
        return true;
    }

    public Person retrieve(String account)
    {
        Person temp = new Person(account, "", "");
        return people.retrieve(temp);
    }

    public boolean FindMatch(Person p)
    {
        Person temp = people.retrieve(p);
        if(temp == null)
            return false;
        return temp.matchPassword(p);
    }

    public boolean contains(Person p)
    {
        return people.contains(p);
    }

    public boolean sendMessage(String from, String to, String message)
    {
        Person p1 = this.retrieve(from);
        if(p1 == null)
            return false;
        Person p2 = this.retrieve(to);
        if(p2 == null)
            return false;
        boolean result =  p1.sendMessageTo(p2, message);
        if(!result)
            return false;
        return p2.sendMessageToSelf(p1, message);
    }

    public boolean addFriend(String from, String to)
    {
        Person p1 = this.retrieve(from);
        if(p1 == null)
            return false;
        Person p2 = this.retrieve(to);
        if(p1 == null)
            return false;
        return p1.addFriend(p2);
    }

    public String getConversion()
    {
        String sum = "";
        for(Person x:people)
        {
            sum += x + "\n";
            sum +=x.getConversion() + "\n";
        }
        return sum;
    }

    public String toString()
    {
        String sum = "";
        for(Person x:people)
        {
           sum += x + "\n";
        }
        return sum;
    }
}













