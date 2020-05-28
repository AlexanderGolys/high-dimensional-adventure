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
    private int window_height;
    private int window_width;
    private float button_h;
    private float button_w;
    private int bnumber;

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

    public void run_game()
    {
        // generate map
        while ( !glfwWindowShouldClose(this.window) ) {
            update_game();
            render_game();
        }
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init()
    {
        this.bnumber = 0;
        this.window_height = 900;
        this.window_width = 1600;
        this.button_h = 0.2f;
        this.button_w = 3*button_h*this.window_height/this.window_width;
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(this.window_width, this.window_height, "High Dimensional Adventure!", NULL, NULL);
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
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    private void update_menu()
    {
        glfwPollEvents();
        if (Input.keys_pressed[GLFW_KEY_SPACE])
        {
            this.bnumber = (this.bnumber + 1) % 5;
            Input.keys_pressed[GLFW_KEY_SPACE] = false;
        }
        if (Input.keys_pressed[GLFW_KEY_UP])
        {
            this.bnumber = (this.bnumber + 1) % 5;
            Input.keys_pressed[GLFW_KEY_UP] = false;
        }
        if (Input.keys_pressed[GLFW_KEY_ENTER])
        {
            this.run_game();
        }


    }

    private void render_menu()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        this.draw_img(Graphics.background_img_menu, -1f, -1f, 1f, 1f);
        this.draw_button(Graphics.about_button, -0.97f, -0.95f, 4);
        this.draw_button(Graphics.uhard_button, -0.97f, -0.80f, 3);
        this.draw_button(Graphics.hard_button, -0.97f, -0.65f, 2);
        this.draw_button(Graphics.medium_button, -0.97f, -0.5f, 1);
        this.draw_button(Graphics.easy_button, -0.97f, -0.35f, 0);
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

    private void draw_button(Texture[] texture, float corner1x, float corner1y, int number)
    {
        if (this.bnumber == number){
            this.draw_img(texture[1], corner1x, corner1y,
                    corner1x + this.button_w, corner1y + this.button_h);
        }
        else {
            this.draw_img(texture[0], corner1x, corner1y,
                    corner1x + this.button_w, corner1y + this.button_h);
        }
    }

    public void print(int x)
    {
        System.out.println(x);
    }

    public static void main(String[] args) {
        new Main().run_menu();
    }

}