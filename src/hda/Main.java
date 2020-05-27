package hda;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private long window;
    private Texture bg_tex;
    private int window_height;
    private int window_width;

    public void run_menu() {
        init();

        while ( !glfwWindowShouldClose(this.window) ) {
            update_menu();
            render_menu();
        }
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init()
    {
        this.window_height = 900;
        this.window_width = 1600;
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(this.window_width, this.window_height, "Hello World!", NULL, NULL);
        if ( this.window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(this.window, new Input());

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            glfwGetWindowSize(this.window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);
        GL.createCapabilities();
        glClearColor(1, 1, 1, 0);
        glEnable(GL_TEXTURE_2D);
        bg_tex = new Texture("./assets/title_bg.png");
    }

    private void update_menu()
    {
        if (Input.keys[GLFW_KEY_SPACE])
        {
            System.out.println("Siup");
        }
        glfwPollEvents();
    }

    private void render_menu()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        this.bg_tex.bind();
        this.draw_img(this.bg_tex, -0.1f, -0.5f, 0.9f, 0.9f);

        glfwSwapBuffers(this.window); // swap the color buffers
    }

    private void draw_img(Texture texture, float corner1x, float corner1y, float corner2x, float corner2y)
    {
        texture.bind();
        glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(corner1x, corner2y);

            glTexCoord2f(0, 1);
            glVertex2f(corner1x, corner1y);

            glTexCoord2f(1, 1);
            glVertex2f(corner2x, corner1y);

            glTexCoord2f(1, 0);
            glVertex2f(corner2x, corner2y);
        glEnd();
    }

    public static void main(String[] args) {
        new Main().run_menu();
    }

}