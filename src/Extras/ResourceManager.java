/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author iTuhh Z
 */
public class ResourceManager {
    public static void save(Serializable data, String filename) throws Exception{
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)))){
            oos.writeObject(data);
        }
    }
    
    public static Object load(String filename) throws Exception{
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))){
            return ois.readObject();
        }
    }
}
