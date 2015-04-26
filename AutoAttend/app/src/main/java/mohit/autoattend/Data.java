package mohit.autoattend;

import java.util.Comparator;

/**
 * Created by mohit on 26/4/15.
 */
public class Data
{
    public double dist;
    public int id;
    public Data(double dist, int id) {
        this.dist = dist;
        this.id = id;
    }
}

class Distance implements Comparator<Data> {

    public int compare(Data d, Data t) {
        // usually toString should not be used,
        // instead one of the attributes or more in a comparator chain
        return (t.dist < d.dist) ? 1 : -1;
    }
}

class Person
{
    String name;
    int id;
    public Person(String name, int id)
    {
        this.name = name;
        this.id = id;
    }
}