    /****************************************************
    *               Final Project: OS                   *
    *      Diego Guzman, Nathan Fixx, Misha Ward        *
    *                                                   *
    ****************************************************/
public class Directory {
    private static int maxChars = 30;
    private int fileSize[];
    private char fileNames[][];
    private int dirSize;

    // Directory constructor
    public Directory(int maxNum) {
        fileSize = new int[maxNum];
        for (int i = 0; i < maxNum; i++) {
            fileSize[i] = 0;
        }
        fileNames = new char[maxNum][maxChars];
        dirSize = maxNum;  // set
        String root = "/";
        fileSize[0] = root.length( );
        root.getChars( 0, fileSize[0], fileNames[0], 0 );
    }
    // creates directory with size
    public void bytes2directory(byte data[]) {
        int offset = 0;
        for (int i = 0; i < dirSize; i++) {
            fileSize[i] = SysLib.bytes2int(data, offset);
            offset += 4;  // adjusting for block
        }
        for (int i = 0; i < dirSize; i++) {
            String tempName = new String(data, offset, maxChars * 2);
            tempName.getChars(0, fileSize[i], fileNames[i], 0);
            offset += maxChars * 2;
        }
    }
    // returns bytes from directory
    public byte[] directory2bytes() {
        byte[] newDir = new byte[64 * (dirSize)];
        int offset = 0;
        for (int i = 0; i < dirSize; i++) {
            SysLib.int2bytes(fileSize[i], newDir, offset);
            offset += 4;
        }
        for (int i = 0; i < dirSize; i++) {
            String name = new String(fileNames[i], 0, fileSize[i]);
            byte[] bytes = name.getBytes();
            System.arraycopy(bytes, 0, newDir, offset, bytes.length);
            offset += maxChars * 2;
        }
        return newDir;
    }
    // returns the file index when the file has nothing
    public short ialloc(String fileName) {
        for (int i = 1; i < dirSize; i++) {
            if (fileSize[i] == 0) {
                fileSize[i] = Math.min(fileName.length(), maxChars);
                fileName.getChars(0, fileSize[i], fileNames[i], 0);
                return (short)i;
            }
        }
        return -1;
    }
    // frees up space in the file
    public boolean ifree(short num) {
        if (fileSize[num] > 0) {
            fileSize[num] = 0;
            return true;
        } else {
            return false;
        }
    }
    // sets name for the file.
    public short namei(String fileName) {
        for (int i = 0; i < dirSize; i++) {
            String tempName = new String(fileNames[i], 0, fileSize[i]);
            if (fileName.equals(tempName)) {
                return (short)i;
            }
        }
        return -1;
    }
}
