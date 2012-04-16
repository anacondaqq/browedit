package com.exnw.browedit.renderutils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.jogamp.common.nio.Buffers;

public class Vbo<T extends Vertex>
{
	private IntBuffer vbo;
	private int length; // length in bytes
	private T oneVertex;
	private int _vertexArray;
	
	public Vbo()
	{
		GL gl = GLContext.getCurrent().getGL();
		vbo = IntBuffer.allocate(1);
		gl.glGenBuffers(1, vbo);
		
	//    gl.glGenVertexArrays(1, _vertexArray);
		length = 0;
	}
	
	public void dispose()
	{
		GL gl = GLContext.getCurrent().getGL();
		gl.glDeleteBuffers(1, vbo);
	}
	
	public void bind()
	{
		GL gl = GLContext.getCurrent().getGL();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo.get(0));
	}
	
	public void generate(VertexList<T> vertices)
	{
		oneVertex = vertices.get(0);
		generate(vertices.GenerateFloatBuffer());
	}
	
	private void generate(FloatBuffer buffer)
	{
		this.bind();
		GL gl = GLContext.getCurrent().getGL();
		//glBufferData automatically throws away the old one
		
	    //glBindVertexArray(_vertexArray);
		
		
		
		gl.glBufferData(GL.GL_ARRAY_BUFFER, buffer.limit()*Buffers.SIZEOF_FLOAT, buffer, GL.GL_STATIC_DRAW);
		length = buffer.limit()*Buffers.SIZEOF_FLOAT;
	}
	public void put(int index, T v)
	{
		this.bind();
		GL gl = GLContext.getCurrent().getGL();

		VertexList<T> tmpList = new VertexList<T>();
		tmpList.add(v);
		
		int size = v.getSize();
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, index * (size*Buffers.SIZEOF_FLOAT), size*Buffers.SIZEOF_FLOAT, tmpList.GenerateFloatBuffer());
	}
	
	public FloatBuffer getMap()
	{
		this.bind();
		GL gl = GLContext.getCurrent().getGL();
		return gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL.GL_WRITE_ONLY).asFloatBuffer();
	}
	
	public void unMap()
	{
		this.bind();
		GL gl = GLContext.getCurrent().getGL();
		gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
	}

	public int size()
	{
		return length;
	}

	public void setPointers()
	{
		this.bind();
		if(oneVertex != null)
			oneVertex.setPointers(this);
	}
}