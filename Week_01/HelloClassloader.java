import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * @author daz2yy
 */
public class HelloClassloader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = new HelloClassloader().findClass("Hello");
        Object o = clazz.newInstance();

        Method hellMethod = clazz.getDeclaredMethod("hello", null);
        hellMethod.invoke(o);
    }

    @Override
    protected Class<?> findClass(String name) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             //路径为classes 路径
             FileInputStream fin = new FileInputStream(this.getClass().getClassLoader().getResource("Hello.xlass").getFile())) {
            byte[] buffer = new byte[1];
            int length = -1;
            while ((length = fin.read(buffer, 0, buffer.length)) != -1) {
                buffer[0] = (byte) (255 - buffer[0]);
                baos.write(buffer, 0, length);
            }
            baos.flush();
            byte[] resultByteArr = baos.toByteArray();
            return defineClass(name, resultByteArr, 0, resultByteArr.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

