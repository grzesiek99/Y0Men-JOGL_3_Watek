package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;



/**
 * GWatek.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class GWalat implements GLEventListener {
    
    //statyczne pola okreslajace rotacje wokol osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new GWalat());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        
        //OBs³uga klawiszy strza³ek
        frame.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_UP)
                xrot -= 1.0f;
                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                xrot +=1.0f;
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                yrot += 1.0f;
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                yrot -=1.0f;
            }
            public void keyReleased(KeyEvent e){}
            public void keyTyped(KeyEvent e){}
        });
        
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        //wy³¹czenie wewnetrznych stron prymitywów
        gl.glEnable(GL.GL_CULL_FACE);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    public void createShape(GL gl, float px, float py, float pz , float size) {
        float kat, x, y;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px,py,pz); //œrodek
        for(kat = 0.0f; kat < (2.0f*Math.PI);
        kat+=(Math.PI/32.0f))
        {
        x = size*(float)Math.sin(kat);
        y = size*(float)Math.cos(kat);
        gl.glVertex3f(x + px, y + py, pz); //kolejne punkty
        }
        gl.glEnd();
            }
    
       public void createTriangle(GL gl, float x1,float y1,float x2, float y2, float x3, float y3, float z)
    {
       gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(x1, y1, z);
        gl.glVertex3f(x2,y2, z);
        gl.glVertex3f(x3,y3, z);
        gl.glEnd(); 
    }
       
       public void sierpin(GL gl, float x1, float y1, float x2, float y2, float x3, float y3, int depth )
{
    float x12 =( x1 + x2 ) / 2.0f, y12 =( y1 + y2 ) / 2.0f,
    x13 =( x1 + x3 ) / 2.0f, y13 =( y1 + y3 ) / 2.0f,
    x23 =( x2 + x3 ) / 2.0f, y23 =( y2 + y3 ) / 2.0f;
    
    Random rd = new Random();
    float c1 = rd.nextFloat();
    float c2 = rd.nextFloat();
    float c3 = rd.nextFloat();
    if( depth == 1 )
    {
        gl.glBegin(GL.GL_TRIANGLES );
        gl.glColor3f(1/depth,1/depth,1/depth);
        gl.glVertex3f( x1, y1, 5.0f );
        gl.glVertex3f( x2, y2, 5.0f );
        gl.glVertex3f( x3, y3, 5.0f );
        gl.glEnd();
    }
    else
    {
        sierpin(gl, x1, y1, x12, y12, x13, y13, depth - 1 );
    }
}
    
    public void display(GLAutoDrawable drawable) {
//Tworzenie obiektu
GL gl = drawable.getGL();
//Czyszczenie przestrzeni 3D przed utworzeniem kolejnej klatki
 gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
 //Resetowanie macierzy transformacji
 gl.glLoadIdentity();

 gl.glTranslatef(0.0f, 0.0f, -6.0f);   //przesuniecie o 6 jednostek
 gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokol osi X
 gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokol osi Y
       // sierpin(gl,1f,1f,-1f,-1f,2f,2f,1);
 //Wykonanie wszystkich operacji znajduj¹cych siê w buforze
 gl.glBegin(GL.GL_QUADS);
//sciana gorna
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f); 
//œciana przednia
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
//sciana tylnia
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//œciana lewa
gl.glColor3f(0.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
//œciana prawa
gl.glColor3f(1.0f,1.0f,0.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
//œciana dolna
gl.glColor3f(1.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glEnd();
 
 gl.glFlush();
}

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

