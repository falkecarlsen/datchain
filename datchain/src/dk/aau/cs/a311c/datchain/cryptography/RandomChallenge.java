package dk.aau.cs.a311c.datchain.cryptography;

import java.util.Random;

import static java.time.Instant.now;

public class RandomChallenge {

    private static final int challengeLength = 2048;
    private static final String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomChallenge() {
        //create new char array challengeLength long
        char[] challenge = new char[challengeLength];

        //for challengeLength, do
        for (int i = 0; i < challengeLength; i++) {
            //get random char between 0 and challengeLength and add to challenge
            challenge[i] = charset.charAt(randomIntInRange(0, charset.length()));
        }
        //return challenge as String
        return new String(challenge);
    }

    private static int randomIntInRange(int lower, int upper) {
        //seed java.util random function for greater resolution when called in rapid succession
        return new Random(now().getNano()).nextInt(upper - lower) + lower;
    }
}
