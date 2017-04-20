/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zigbeetool;

/**
 *
 * @author I14746
 */
public class SharedData {
private static SharedData ref;



    public static SharedData getSingletonObject()
    {
    if (ref == null)
        // it's ok, we can call this constructor
        ref = new SharedData();
    return ref;
    }
}
