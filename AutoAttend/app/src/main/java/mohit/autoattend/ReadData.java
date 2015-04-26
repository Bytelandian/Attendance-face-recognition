package mohit.autoattend;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mohit on 27/4/15.
 */
public class ReadData {

    public int inRow = 0, inCol = 0;
    public int[] inImage = null;                        // mn x 1
    public double[] meanImage = null, diff = null;        // mn x 1
    public int mnsize = 0;

    public double[][] eigenFace = null;                // mn x p-1
    public int eRow = 0, eCol = 0;

    public double[] projTestImg = null;                // p-1 x 1

    public double[][] projImages = null;                // p-1 x train_no
    public int projRow = 0, projCol = 0;

    public double[] eucDist = null;

    public Person[] person = null;

    public int dRow = 0;
    public int train_no = 1;
    public int projTestRow = 0, projTestCol = 0;

    public BufferedReader trainInfoReader = null;
    public BufferedReader mReader = null;
    public BufferedReader eigenReader = null;
    public BufferedReader projImgReader = null;

    String line;
    int i, j;

    ReadData() {
        try {
            mReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/mean"));
            eigenReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/EigenFaces"));
            projImgReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/projImages"));
            trainInfoReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/train_info"));

            mnsize = Integer.parseInt(mReader.readLine());
            meanImage = new double[mnsize];
            line = mReader.readLine();
            String[] t2 = line.split(" ", mnsize);
            for (i = 0; i < mnsize; i++) {
                meanImage[i] = Double.parseDouble(t2[i]);
            }

            eRow = Integer.parseInt(eigenReader.readLine());
            eCol = Integer.parseInt(eigenReader.readLine());
            eigenFace = new double[eRow][eCol];

            for (j = 0; j < eRow; j++) {
                String[] t3 = eigenReader.readLine().split(" ", eCol);
                for (i = 0; i < eCol; i++)
                    eigenFace[j][i] = Double.parseDouble(t3[i]);
                //System.out.println(t[i]);
            }

            projRow = Integer.parseInt(projImgReader.readLine());
            projCol = Integer.parseInt(projImgReader.readLine());
            projImages = new double[projRow][projCol];

            for (j = 0; j < projRow; j++) {
                String[] t4 = projImgReader.readLine().split(" ", projCol);
                for (i = 0; i < projCol; i++)
                    projImages[j][i] = Double.parseDouble(t4[i]);
            }


            train_no = Integer.parseInt(trainInfoReader.readLine());
            person = new Person[(train_no + 1)];

            for (i = 1; i <= train_no; i++) {
                String[] t1 = trainInfoReader.readLine().split("->", 2);
//				System.out.println(t1[0] + " " + t1[1]);
                person[i] = new Person(t1[1], Integer.parseInt(t1[0]));
//				person[i].name = t1[1];
//				person[i].id = Integer.parseInt(t1[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
