package com.agadimi.a3dcube;

public class Edge
{
    private float first, second;

    public Edge()
    {
    }

    public Edge(float first, float second)
    {
        this.first = first;
        this.second = second;
    }

    public float getFirst()
    {
        return first;
    }

    public void setFirst(float first)
    {
        this.first = first;
    }

    public float getSecond()
    {
        return second;
    }

    public void setSecond(float second)
    {
        this.second = second;
    }
}
