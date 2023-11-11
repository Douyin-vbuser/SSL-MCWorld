package com.vbuser.ssl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class KeyGenerate {

    public static long time;

    public static BigInteger generatePrimeFromIPAddress(String ipAddress) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((ipAddress+time).getBytes());
            BigInteger hashValue = new BigInteger(1, hash);
            return hashValue.nextProbablePrime();
        }
        catch (NoSuchAlgorithmException e){
            return BigInteger.ZERO;
        }
    }

    public static BigInteger generatePrimeFromTime(){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(time).getBytes());
            BigInteger hashValue = new BigInteger(1, hash);
            return hashValue.nextProbablePrime();
        }
        catch (NoSuchAlgorithmException e){
            return BigInteger.ONE.add(BigInteger.ONE).add(BigInteger.ONE);
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

    public static BigInteger computeModularInverse(BigInteger a,BigInteger b){
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

    public static BigInteger encrypt(int m, BigInteger e, BigInteger n){
        return BigInteger.valueOf(m).modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n){
        return c.modPow(d, n);
    }
}
