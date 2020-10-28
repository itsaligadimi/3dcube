package com.agadimi.a3dcube.glversion;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class MeshArray
{
    public Buffer buffer;
    public byte vertices;

    public MeshArray()
    {
    }

    public MeshArray(Buffer buffer, byte vertices)
    {
        this.buffer = buffer;
        this.vertices = vertices;
    }

    public Buffer getBuffer()
    {
        return buffer;
    }

    public void setBuffer(Buffer buffer)
    {
        this.buffer = buffer;
    }

    public byte getVertices()
    {
        return vertices;
    }

    public void setVertices(byte vertices)
    {
        this.vertices = vertices;
    }
}
