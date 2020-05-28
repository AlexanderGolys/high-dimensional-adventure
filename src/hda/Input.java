package hda;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import static java.lang.Math.abs;

public class Input extends GLFWKeyCallback {
    public static boolean[] keys_pressed = new boolean[65536];
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        keys_pressed[abs(key)] = action != GLFW.GLFW_RELEASE;
    }
}
