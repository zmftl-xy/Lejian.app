package central.stu.fucklegym;

public class Encrypter {//先简单写写吧
    public static int encrypt(String str){
        int ret = Math.abs(str.hashCode());
        ret^=19260817;
        return ret;
    }
    public static final String run_salt = "3soJs320~*%$wd";
}
