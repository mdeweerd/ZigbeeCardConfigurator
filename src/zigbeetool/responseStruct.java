/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zigbeetool;

/**
 *
 * @author I14746
 */
public class responseStruct {

    byte startByte;
    byte cmdLength;
    byte cmd0;
    byte cmd1;
    byte dataBuffer[];
    byte checksum;
    
    /** Ture if the checksum is ok. */
    boolean isValid=false;

    public responseStruct() {

    }
    
    public responseStruct(byte[] p) {
        if(   p.length>4
            &&((p[0]&0xFF)==0xFE)
        ) {
            // Validate checksum
            int len=(p[1]&0xFF)+5;
            if(len<=p.length) {
                int cksum=0;
                while(--len>0) {
                    cksum^=p[len];
                }
                if(cksum==0) {
                    // cksum is ok.       
                    this.startByte = p[0];
                    this.cmdLength = p[1];
                    this.cmd0 = p[2];
                    this.cmd1 = p[3];
                    this.dataBuffer = new byte[this.cmdLength];
                    System.arraycopy(p, 4, this.dataBuffer, 0, this.cmdLength);
                    this.checksum = p[cmdLength+4];
                }
                this.isValid=true;
            }
        } 
    }
    
    
    public int getSubSys() {
        return cmd1&0x1F;
    }

    public int getCmdType() {
        return (cmd1&0xE0)>>5;
    }
}
