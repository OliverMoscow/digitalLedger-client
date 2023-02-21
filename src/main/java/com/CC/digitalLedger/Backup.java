package com.CC.digitalLedger;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

//Not related to any api/db functionality
public class Backup {
    public Secret secret = null;
    public String name = null;
    public boolean isInitialized = false;

    public Backup() {
        try {
            this.secret = load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public Secret load() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File f = new File("secret.txt");
        if(f.createNewFile()) {
            Secret s = new Secret();
            return s;
        } else {
            this.isInitialized = true;
            return read();
        }
    }

    private Secret read() throws FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
        File f = new File("secret.txt");
        Scanner reader = new Scanner(f);
        reader.nextLine();
        String publicKey = reader.nextLine();
        reader.nextLine();
        String privateKey = reader.nextLine();
        reader.nextLine();
        name = reader.nextLine();
        return new Secret(publicKey,privateKey);
    }

    public void save(String name) throws IOException {
        this.name = name;
        PrintStream fileStream = new PrintStream(new File("secret.txt"));
        fileStream.println("### PUBLIC KEY ###");
        fileStream.println(secret.publicKeyAsString());
        fileStream.println("### PRIVATE KEY ###");
        fileStream.println(secret.privateKeyAsString());
        fileStream.println("### USERNAME ###");
        fileStream.println(name);
        fileStream.println("");
        fileStream.println("DO NOT DELETE THIS FILE!!");
        fileStream.println("This file stores your private and public key which are required to retrieve your funds in the ledger. You should probably back this up somewhere.");
    }
}

class Secret {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Secret() throws NoSuchAlgorithmException {
        System.out.println("Generating public and private keypair");
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        //Base64.Encoder encoder = Base64.getEncoder();

    }

    public Secret(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.publicKey = Secret.stringToPublic(publicKey);
        this.privateKey = Secret.stringToPrivate(privateKey);
    }

    public static PrivateKey stringToPrivate(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey) ;
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    public static PublicKey stringToPublic(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(publicKey) ;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

//    public PublicKey publicKey() {
//        return publicKey;
//    }
    public PrivateKey privateKey() {
        return privateKey;
    }
    public String publicKeyAsString() {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKey.getEncoded());
    }
    public String privateKeyAsString() {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(privateKey.getEncoded());
    }
}
