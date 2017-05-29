package com.nasoftware;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zeyongshan on 5/25/17.
 */
public class ConcurrentList<type> extends ConcurrentLinkedDeque<type>
{

    public type retrieve(type element)
    {
        for(type x:this)
        {
            if(x.equals(element))
                return x;
        }
        return null;
    }

}
