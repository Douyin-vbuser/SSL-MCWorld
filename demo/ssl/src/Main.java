import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        BigInteger p = generatePrimeFromIPAddress(getServerIPAddress(Inet4Address.getLocalHost().getHostAddress()));
        System.out.println("p:"+p);
        long l = (long) (Math.random() * 1000000000000000000L);//TODO:edit it in Minecraft mod.
        BigInteger q = generatePrimeFromBlockPos(l);
        System.out.println("q:"+q);
        BigInteger n = p.multiply(q);
        System.out.println("n:"+n);
        BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
        System.out.println("phi_n:"+phi_n);
        BigInteger e = findValidE(phi_n);
        System.out.println("e:"+e);
        BigInteger d = computeModularInverse(e, phi_n);
        System.out.println("d:"+d);
    }

    public static String getServerIPAddress(String ipAddress) {
        if (ipAddress.length() < 7 || ipAddress.length() > 15 || !isValidIPv4(ipAddress)) {
            return "127.0.0.1";
        }
        return ipAddress;
    }

    public static boolean isValidIPv4(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
                if (part.length() > 1 && part.startsWith("0")) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static BigInteger generatePrimeFromIPAddress(String ipAddress) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ipAddress.getBytes());
            BigInteger hashValue = new BigInteger(1, hash);
            return hashValue.nextProbablePrime();
        }
        catch (NoSuchAlgorithmException e){
            return BigInteger.ZERO;
        }
    }

    public static BigInteger generatePrimeFromBlockPos(long blockPos){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(blockPos).getBytes());
            BigInteger hashValue = new BigInteger(1, hash);
            return hashValue.nextProbablePrime();
        }
        catch (NoSuchAlgorithmException e){
            return BigInteger.ONE.add(BigInteger.ONE);
        }
    }

    public static BigInteger findValidE(BigInteger phi_n) {
        Random rand = new Random();
        int maxTries = 100;

        for (int i = 0; i < maxTries; i++) {
            BigInteger e = new BigInteger(phi_n.bitLength(), rand);
            if (e.compareTo(phi_n) >= 0) {
                continue;
            }
            if (isCoprime(e, phi_n)) {
                return e;
            }
        }
        return BigInteger.ONE.add(BigInteger.ONE).add(BigInteger.ONE);
    }

    public static boolean isCoprime(BigInteger a, BigInteger b) {
        return a.gcd(b).equals(BigInteger.ONE);
    }

    public static BigInteger computeModularInverse(BigInteger a, BigInteger b){
        BigInteger m0 = b;
        BigInteger x0 = BigInteger.ZERO;
        BigInteger x1 = BigInteger.ONE;
        while(a.compareTo(BigInteger.ONE)>0){
            BigInteger q = a.divide(b);
            BigInteger t = b;
            b = a.mod(b);
            a = t;
            BigInteger t2 = x0;
            x0 = x1.subtract(q.multiply(x0));
            x1 = t2;
        }
        if(x1.compareTo(BigInteger.ZERO)<0){
            x1 =  x1.add(m0);
        }
        return x1;
    }
}