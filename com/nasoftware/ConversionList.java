package com.nasoftware;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zeyongshan on 5/25/17.
 */


class Message               // the class used to represent messages.
{
    private String content;
    private String name;
    // store the content of this message;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int second;
    // store the time of this message;


    public Message(String content, String name)
    {
        this.content = content;
        this.name = name;
        Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    public String getContent()
    {
        return content;
    }

    public String getTime()
    {
        return month + " " + day + " " + hour + ":" + min + ":" + second + " " + year;
    }

    public String getSender()
    {
        return name;
    }

    public String toString()
    {
        return "" + getTime() + "  :  " + getSender() + " => " + getContent() ;
    }

}


class Conversion
{
    private ConcurrentList<Message> messages;
    private String friendEmail;
    private String friendName;

    public Conversion(String email, String name)
    {
        this.friendEmail = email;
        this.friendName = name;
        messages = new ConcurrentList<Message>();
    }

    public void enqueue(String messageContent, String name)
    {
        Message message = new Message(messageContent, name);
        messages.add(message);
    }

    public String toString()
    {
        String value = "";
        value += "Name: " + friendName + "\n";
        for(Message x:messages)
        {
            value += x.toString() + "\n";
        }
        return value;
    }

    public boolean equals(Object s)
    {
        if(s instanceof Conversion)
        {
            Conversion temp = (Conversion) s;
            return this.friendEmail.equals(temp.friendEmail);
        }
        return false;
    }

}


public class ConversionList {
    private ConcurrentList<Conversion> conversions = new ConcurrentList<Conversion>();

    public boolean addConversion(String email, String name)
    {
        Conversion new_conversion = new Conversion(email, name);
        if(conversions.contains(new_conversion))
            return false;
        conversions.add(new_conversion);
        return true;
    }

    public boolean addMessageTo(String email, String message, String name)
    {
        Conversion new_conversion = new Conversion(email, "");
        Conversion target = conversions.retrieve(new_conversion);
        if(null == target)
            return false;
        target.enqueue(message, name);
        return true;
    }

    public String toString()
    {
        String result = "";
        for(Conversion s:conversions)
        {
            result += s.toString() + "\n";
        }
        //System.out.println("result: " + result);
        return result;
    }

}












