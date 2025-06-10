package util;

import JOBO.CurrentHost;

import java.io.*;

public class SerializeJOPO {

    private static final String serializeFile = "currenthost.ser";

    public static void Serialize(CurrentHost currentHost) throws IOException {
        File file = new File(serializeFile);
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(currentHost);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static CurrentHost UnSerialize() throws IOException, ClassNotFoundException {
        File file = new File(serializeFile);
        if (!file.exists()){
            System.out.println("The serialized file does not exist");
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        CurrentHost currentHost = (CurrentHost) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return currentHost;
    }
}
