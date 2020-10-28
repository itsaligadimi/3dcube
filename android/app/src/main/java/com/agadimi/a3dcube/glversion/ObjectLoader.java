package com.agadimi.a3dcube.glversion;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by Kristoffer on 15-08-06.
 */
public class ObjectLoader
{
    private static int BUFFER_READER_SIZE = 65536;

    /**
     * Load 3D model mesh from inputstream.
     * A Wavefront formatted file (.obj) is expected as input and may be loaded
     * remotely or as a local file.
     *
     * @param is input file in form of a .obj "text file".
     * @return a object containing vertices, normals and textureCoordinates.
     */
    public static List<MeshArray> loadModelMeshFromStream(InputStream is)
    {
        Timber.v("Loading the model...");
        long startTime = System.currentTimeMillis();

        BufferedReader bufferedReader = null;
        try
        {
            // Lists to keep all data when reading file
            ArrayList<Float> vlist = new ArrayList<Float>();
            List<MeshArray> meshArrays = new ArrayList<>();

            // Result buffers
            FloatBuffer mVertexBuffer;

            int numVerts = 0;

            String str;
            String[] tmp;

            bufferedReader = new BufferedReader(new InputStreamReader(is), BUFFER_READER_SIZE);

            while ((str = bufferedReader.readLine()) != null)
            {
                // Replace double spaces. Some files may have it. Ex. files from 3ds max.
                str = str.replace("  ", " ");
                tmp = str.split(" ");

                if(tmp.length == 0)
                {
                    continue;
                }


                if (tmp[0].equalsIgnoreCase("v"))
                {
                    for (int i = 1; i < 4; i++)
                    {
                        vlist.add(Float.parseFloat(tmp[i]));
                    }
                    numVerts++;
                }

                if (tmp[0].equalsIgnoreCase("f"))
                {


                    ByteBuffer vbb = ByteBuffer.allocateDirect((tmp.length - 1) * 3 * 4);
                    vbb.order(ByteOrder.nativeOrder());
                    mVertexBuffer = vbb.asFloatBuffer();

                    for (byte i = 1; i < tmp.length; i++)
                    {
                        int vertex = (Integer.decode(tmp[i].split("/")[0]) - 1) * 3;
                        mVertexBuffer.put(vlist.get(vertex));
                        mVertexBuffer.put(vlist.get(vertex + 1));
                        mVertexBuffer.put(vlist.get(vertex + 2));
                    }
                    mVertexBuffer.rewind();

                    MeshArray meshArray = new MeshArray();
                    meshArray.setBuffer(mVertexBuffer);
                    meshArray.setVertices((byte) (tmp.length - 1));
                    meshArrays.add(meshArray);
                }
            }

            Timber.d("Loaded the model with %d faces in %d ms.",
                    meshArrays.size(),
                    System.currentTimeMillis() - startTime);
            return meshArrays;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }
}