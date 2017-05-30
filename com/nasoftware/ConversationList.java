package com.nasoftware;
import java.util.*;

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
    private boolean readed;
    // store the time of this message;


    public Message(String content, String name)
    {
        this.content = content;
        this.name = name;
        this.readed = false;
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

    public String getSimpleTime()
    {
        return hour + ":" + min + ":" + second;
    }

    public String getSender()
    {
        return name;
    }

    public String toString()
    {
        if(readed)
            return getTime() + "  :  " + "  read : " + getSender() + " => " + getContent() ;
        else
            return getTime() + "  :  " + "unread : " + getSender() + " => " + getContent() ;
    }

    public boolean isReaded()
    {
        return readed;
    }

    public String read()
    {
        readed = true;
        return getSimpleTime() + "  :  " + getSender() + " => " + getContent();
    }

}


class Conversation
{
    private ConcurrentList<Message> messages;
    private String friendEmail;
    private String friendName;
    private boolean newMessageSet;

    public Conversation(String email, String name)
    {
        this.friendEmail = email;
        this.friendName = name;
        this.newMessageSet = false;
        messages = new ConcurrentList<Message>();
    }

    public void enqueue(String messageContent, String name, boolean self)
    {
        Message message = new Message(messageContent, name);
        if(self)
            message.read();
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
        if(s instanceof Conversation)
        {
            Conversation temp = (Conversation) s;
            return this.friendEmail.equals(temp.friendEmail);
        }
        return false;
    }

    public boolean containsUnread()
    {
        for(Message x:messages)
        {
            if(!x.isReaded())
                return true;
        }
        return false;
    }

    public String getUnread()
    {
        String unread = "";
        boolean first = true;
        for(Message x:messages)
        {
            if(!x.isReaded() && first)
            {
                unread += x.read();
                first = false;
            }
            if(!x.isReaded() && !first)
                unread += "-" + x.read();
        }
        return unread;
    }

}


public class ConversationList {
    private ConcurrentList<Conversation> conversations = new ConcurrentList<Conversation>();

    public boolean addConversion(String email, String name)
    {
        Conversation new_conversation = new Conversation(email, name);
        if(conversations.contains(new_conversation))
            return false;
        conversations.add(new_conversation);
        return true;
    }

    public boolean addMessage(String email, String message, String name, boolean self)
    {
        Conversation new_conversation = new Conversation(email, "");
        Conversation target = conversations.retrieve(new_conversation);
        if(null == target)
            return false;
        target.enqueue(message, name, self);
        return true;
    }

    public String toString()
    {
        String result = "";
        for(Conversation s: conversations)
        {
            result += s.toString() + "\n";
        }
        return result;
    }

    public boolean containsUnread(String email)
    {
        Conversation conversation = new Conversation(email,"");
        conversation = conversations.retrieve(conversation);
        if(conversation.containsUnread())
            return true;
        return false;
    }

    public String getUnread(String email)
    {
        Conversation conversation = new Conversation(email,"");
        conversation = conversations.retrieve(conversation);
        if(conversation == null)
            return null;
        return conversation.getUnread();
    }

}












