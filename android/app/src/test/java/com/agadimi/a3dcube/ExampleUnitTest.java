package com.agadimi.a3dcube;

import com.agadimi.a3dcube.glversion.ObjectLoader;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void edgeComparison_isCorrect()
    {
        ObjectLoader.Edge one = new ObjectLoader.Edge();
        one.first = 1;
        one.second = 2;

        ObjectLoader.Edge two = new ObjectLoader.Edge();
        two.first = 1;
        two.second = 3;

        assertTrue(one.equals(two));
    }
}