package com.cqfc.statistics.common;

/**
 * @author: giantspider@126.com
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class DateIterator implements Iterator<Date>, Iterable<Date> {

    private Calendar end = Calendar.getInstance();
    private Calendar current = Calendar.getInstance();

    public DateIterator(Date start, Date end) {
        this.end.setTime(end);
        this.end.add(Calendar.DATE, -1);
        this.current.setTime(start);
        this.current.add(Calendar.DATE, -1);
    }

    public boolean hasNext() {
        return !current.after(end);
    }

    public Date next() {
        current.add(Calendar.DATE, 1);
        return current.getTime();
    }

    public void remove() {
        throw new UnsupportedOperationException("cannot remove");
    }

    public Iterator<Date> iterator() {
        return this;
    }

    public static void main(String[] args) {
        Date d1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date d2 = cal.getTime();

        Iterator<Date> i = new DateIterator(d1, d2);
        while(i.hasNext())
        {
            Date date = i.next();
            System.out.println(date);
        }
    }
}
