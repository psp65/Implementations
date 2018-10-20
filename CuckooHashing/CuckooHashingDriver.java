package psp170230;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author Param Parikh
 */
public class CuckooHashingDriver {

    public static void main(String[] args) throws Exception {
        Scanner sc1, sc2;

        if (args.length == 0) {
            System.err.print("Provide path to input file.");
            System.exit(1);
        }
        
        File file = new File(args[0]);
        sc1 = new Scanner(file);
        sc2 = new Scanner(file);



        HashSet<Integer> hs = new HashSet<>();
		int count = 0;
        Timer timer2 = new Timer();
        while (sc2.hasNext()) {
            if (hs.add(Integer.parseInt(sc2.next()))) {
                count++;
            }
        }
        timer2.end();
        System.out.print("Unique from Java: " + count + " || ");
        System.out.println(timer2);

        
        CuckooHashing<Integer> cuckoo = new CuckooHashing<>();
        count = 0;
        Timer timer1 = new Timer();
        while (sc1.hasNext()) {
            if (cuckoo.add(Integer.parseInt(sc1.next()))) {
                count++;
            }
        }
        timer1.end();
        System.out.print("Unique from Cuckoo: " + count + " || ");
        System.out.println(timer1);


    }
}
