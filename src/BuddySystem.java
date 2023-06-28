// Anubhav Pal, axp200092
// Project 3
// Used an array as an implementation of a binary tree to perform the actions
// Three arrays were used, for the partition values, one for checking if the node was split or occupied and one for storing the alphabets

import java.io.*;
import java.util.*;

public class BuddySystem {

    // Main Function
    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        // Assign memory values to the array
        int[] memoryArray = new int[31];
        memoryArray[0] = 1024;
        for (int i = 1; i <= 2; i++) {
            memoryArray[i] = 512;
        }
        for (int i = 3; i <= 6; i++) {
            memoryArray[i] = 256;
        }
        for (int i = 7; i <= 14; i++) {
            memoryArray[i] = 128;
        }
        for (int i = 15; i <= 30; i++) {
            memoryArray[i] = 64;
        }

        // Starting the alphabet with an A
        char al = 'A';

        // An array which tells us which memory node is split
        // 0 for not split, 1 for split and > 1 for occupied
        int isSplit[] = new int[31];

        // Array stores the alphabet for the request
        char alphabet[] = new char[31];

        for (int i = 0; i < 31; i++) {
            isSplit[i] = 0;
        }

        // Printing the initial memory(empty)
        String out = printing(1024, 0, isSplit, alphabet, memoryArray, "") + "|";
        for (int i = 0; i < out.length(); i++)
            System.out.print("-");

        System.out.println();
        System.out.println(out);

        for (int i = 0; i < out.length(); i++)
            System.out.print("-");
        System.out.println();
        System.out.println();

        // Exception handling for file to be read.
        try {

            sc = new Scanner(new File("abc.txt"));

            while (sc.hasNextLine()) {

                String s = sc.nextLine();
                System.out.println(s);

                String line[] = s.split(" ");

                // If the line is a request
                if (line[0].equals("Request")) {

                    // Get the memory size
                    int size = Integer.parseInt(line[1].substring(0, line[1].length() - 1));

                    // allocate the memory, and also return the index of the array being modified
                    int ind = allocate(memoryArray, isSplit, size);

                    // If the allocation was successful, we update the alphabet array
                    if (ind != -1)
                        alphabet[ind] = al;

                    // Calling the Printing function that returns the memory string to be printed
                    out = printing(1024, 0, isSplit, alphabet, memoryArray, "") + "|";

                    // We Print the line along with upper and lower boundary lines
                    for (int i = 0; i < out.length(); i++)
                        System.out.print("-");

                    System.out.println();
                    System.out.println(out);

                    for (int i = 0; i < out.length(); i++)
                        System.out.print("-");

                    System.out.println();
                    System.out.println();

                    // We increment the alphabet to next one as current is already in use
                    al = (char) (al + 1);
                }

                // If the line was release
                else if (line[0].equals("Release")) {

                    // call the release function that releases the memory if requested earlier
                    release(line[1].charAt(0), isSplit, alphabet);

                    // returns the updated memory
                    out = printing(1024, 0, isSplit, alphabet, memoryArray, "") + "|";

                    // Printing the updated memory
                    for (int i = 0; i < out.length(); i++)
                        System.out.print("-");

                    System.out.println();
                    System.out.println(out);

                    for (int i = 0; i < out.length(); i++)
                        System.out.print("-");
                    System.out.println();
                    System.out.println();

                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

    }

    // Allocate function, takes 2 arrays, the memory indexes one and isSplit
    public static int allocate(int[] memory, int[] isSplit, int size) {

        // Max Size is 1024
        int maxSz = 1024;
        int level = 0;

        // If incorrect request is made
        if (size > 1024 || size < 64) {
            System.out.println("Not enough memory or too small to add to memory");
            return -1;

        }

        // Loop that tells us which level of the tree should the memory be updated
        while (true) {
            if (size <= maxSz && size > maxSz / 2) {
                break;
            } else {
                maxSz = maxSz / 2;
                level++;
            }
        }

        int i = -1;

        // iterating through each node of the particular level
        for (i = (int) Math.pow(2, level) - 1; i <= (int) Math.pow(2, level + 1) - 2; i++) {

            // if the current node is not divided and checks if the parent has already been
            // allocated
            if (isSplit[i] == 0 && place(i, isSplit)) {
                isSplit[i] = size;

                // Splits the parent
                makeDivided(i, isSplit);
                break;

            }
        }

        // If there are no free node then we print not enough memory
        if (i == (int) Math.pow(2, level + 1) - 1) {
            System.out.println("Not enough memory!");
            i = -1;

        }

        // Returns the index of the array we modified and allocated the memory to
        return i;

    }

    // checks if the node's parent has already been allocated, if it has then we
    // return false else true
    public static boolean place(int index, int[] isSplit) {

        while (index != 0) {
            // For left and right child node
            index = (index % 2 == 0) ? (index - 1) / 2 : index / 2;
            if (isSplit[index] > 1) {
                return false;
            }
        }
        return true;

    }

    // Splits the parent into 2
    public static void makeDivided(int index, int[] isSplit) {
        while (index != 0) {
            index = (index % 2 == 0) ? (index - 1) / 2 : index / 2;
            isSplit[index] = 1;
        }

    }

    // Releases the request
    public static void release(char c, int isSplit[], char[] al) {

        // Index to 0
        int i = 0;

        // Searching for the wanted Alphabet or Request to be released
        while (true) {
            if (al[i] == c) {
                break;
            } else {
                i++;

                // If there is no such request we print error message
                if (i > 30) {
                    System.out.println(c + " is not present in the array so cannot be released");
                    return;
                }
            }

        }
        // We also change the alphabet array as we are about to release the request
        al[i] = '\0';
        // We change the memory back to 0, remember this array would have either,0,1 or
        // the memory size as its values
        // 0 means it is empty and not been split, 1 means split and more means memory
        // allocated
        isSplit[i] = 0;

        // Merging the parent if necessary
        while (i != 0) {
            if (isSplit[i] == 0 && isSplit[(i % 2 == 0) ? (i - 1) : i + 1] == 0) {
                isSplit[(i % 2 == 0) ? (i - 1) / 2 : i / 2] = 0;
                i = (i % 2 == 0) ? (i - 1) / 2 : i / 2;
            } else {
                break;
            }
        }

    }

    // Returns the current state of the memory as a string
    // Takes total memory and all 3 arrays as input, and an empty string which is
    // modified recursively and returned at the end
    public static String printing(int total, int node, int[] isSplit, char[] al, int[] mem, String out) {

        // Permission, upper limit, lower limit
        int perm = 0, llimit, ulimit;

        // If the node is the first node, the max memory
        if (node == 0) {
            perm = 1;
        }

        // If it is left child node
        else if (node % 2 == 0) {

            // If the current node is split, we dont need to add that to the output
            perm = isSplit[(node - 1) / 2] == 1 ? 1 : 0;
        }

        // If the right child node
        else {
            // Similar to last statement
            perm = isSplit[node / 2] == 1 ? 1 : 0;
        }

        // If the node is not split or doesn't have children
        if (perm == 1) {

            // Upper and lower limit = 0
            llimit = ulimit = 0;

            // Calculating upper and lower limit recursively
            while (true) {
                if (node >= llimit && node <= ulimit)
                    break;
                else {

                    llimit = ulimit + 1;
                    ulimit = 2 * ulimit + 2;
                }
            }

            // If the node has been allocated, update the string with the alphabet
            if (isSplit[node] > 1) {

                out = out + "| " + al[node] + "     " + mem[node] + "K";

                // If split do nothing
            } else if (isSplit[node] == 1) {

                // If empty add that to string
            } else {

                out = out + "|       " + mem[node] + "K";

            }

            // Recursively traverse the entire tree through this
            out = printing(total, 2 * node + 1, isSplit, al, mem, out);
            out = printing(total, 2 * node + 2, isSplit, al, mem, out);

        }

        // Return the current state of the memory
        return out;
    }

}
