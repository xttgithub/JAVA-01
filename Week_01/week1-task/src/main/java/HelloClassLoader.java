import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，
 * 此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。
 *
 * @author zhaoteng
 * @date 2021-01-15
 */
public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            // 调用class类中的方法
            HelloClassLoader helloClassLoader = new HelloClassLoader();
            Class<?> hello = helloClassLoader.findClass("Hello");
            Object object = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        // 文件路径
        String filePath = "src/main/resources/Hello.xlass";
        byte[] helloClassBytes = new byte[0];
        try {
            helloClassBytes = toByteArray(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 恢复字节码文件
        for (int i = 0; i < helloClassBytes.length; i++) {
            helloClassBytes[i] = (byte) (255 - helloClassBytes[i]);
        }
        return defineClass(name, helloClassBytes, 0, helloClassBytes.length);
    }

    /**
     * 读取文件并转换成字节数组
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private static byte[] toByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while (-1 != (len = bis.read(buffer, 0, bufferSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

}
