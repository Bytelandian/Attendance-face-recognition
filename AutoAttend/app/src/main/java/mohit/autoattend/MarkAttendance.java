package mohit.autoattend;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MarkAttendance extends IntentService {


    public MarkAttendance() {
        super("MarkAttendance");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Attendance", "ENERTED");
        if (intent != null) {
            ResultReceiver receiver = intent.getParcelableExtra("receiver");

            Bundle inp=intent.getExtras();

            String path = inp.getString("path");

            Log.d("Attendance",path);


            int inRow = 0, inCol = 0;
            int[] inImage = null;						// mn x 1
            double[] meanImage = null, diff = null;		// mn x 1
            int mnsize = 0;

            double[][] eigenFace = null; 				// mn x p-1
            int eRow = 0, eCol = 0;

            double[] projTestImg = null;				// p-1 x 1

            double[][] projImages = null;				// p-1 x train_no
            int projRow = 0, projCol = 0;

            double[] eucDist = null;


            int dRow = 0;
            int train_no = 1;
            int projTestRow = 0, projTestCol = 0;

            BufferedReader trainInfoReader = null;
            BufferedReader mReader = null;
            BufferedReader eigenReader = null;
            BufferedReader projImgReader = null;

            String line;
            int i,j;

            Log.d("Attendance","Reading Training Models");
            try {
                mReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/mean"));
                eigenReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/EigenFaces"));
                projImgReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/projImages"));
                trainInfoReader = new BufferedReader(new FileReader("/storage/emulated/0/AutoAttend/model/train_info"));

                mnsize = Integer.parseInt(mReader.readLine());
                meanImage = new double[mnsize];
                line = mReader.readLine();
                String[] t2 = line.split(" ",mnsize);
                for(i=0; i<mnsize; i++)
                {
                    meanImage[i] = Double.parseDouble(t2[i]);
                }

                eRow = Integer.parseInt(eigenReader.readLine());
                eCol = Integer.parseInt(eigenReader.readLine());
                eigenFace = new double[eRow][eCol];

                for(j=0; j<eRow; j++)
                {
                    String[] t3 = eigenReader.readLine().split(" ",eCol);
                    for(i=0; i<eCol; i++)
                        eigenFace[j][i] = Double.parseDouble(t3[i]);
                    //System.out.println(t[i]);
                }

                projRow = Integer.parseInt(projImgReader.readLine());
                projCol = Integer.parseInt(projImgReader.readLine());
                projImages = new double[projRow][projCol];

                for(j=0; j<projRow; j++)
                {
                    String[] t4 = projImgReader.readLine().split(" ",projCol);
                    for(i=0; i<projCol; i++)
                        projImages[j][i] = Double.parseDouble(t4[i]);
                }


                train_no = Integer.parseInt(trainInfoReader.readLine());
                Person[] person = new Person[(train_no+1)];

                for(i=1; i<=train_no; i++)
                {
                    String[] t1 = trainInfoReader.readLine().split("->",2);
//				System.out.println(t1[0] + " " + t1[1]);
                    person[i] = new Person(t1[1], Integer.parseInt(t1[0]));
//				person[i].name = t1[1];
//				person[i].id = Integer.parseInt(t1[0]);
                }



            Log.d("Attendance","Read Training Models");

//                File file = new File("E:\\Educational\\Study\\sem6\\Contemporary computing platforms\\check\\PCA_based Face Recognition System\\PCA_based Face Recognition System\\new\\TestDatabase\\3.jpg");
            Log.d("Attendance","Reading Image");
                Bitmap img;
                img = BitmapFactory.decodeFile(path);//"/storage/emulated/0/AutoAttend/2.jpg");// ImageIO.read(file);
                inImage = new int[img.getHeight() * img.getWidth()];
                int[] rgb;

                int counter = 0;
                for(int i1 = 0; i1 < img.getHeight(); i1++){
                    for(int j1 = 0; j1 < img.getWidth(); j1++){
                        rgb = getPixels(img, j1, i1);
                        inImage[counter] = rgb[0];
                        counter++;
                    }
                }
                inRow = img.getHeight();
                inCol = img.getWidth();

            diff = new double[inRow*inCol];
            difference(diff, meanImage, inImage, inRow*inCol);



            projTestImg = new double[eCol];
            conj_multiply(eigenFace, eRow, eCol, diff, dRow, projTestImg);
            train_no = eCol ;


            eucDist = new double[train_no];
            Data[] distance = new Data[train_no];

            euc_dist(train_no, projImages, projRow, projCol, projTestImg, projTestRow, projTestCol, eucDist, distance);

            for(i=0;i<train_no;i++)
	    {
	    	System.out.println(distance[i].dist + " " + distance[i].id);
	    }


                Arrays.sort(distance, new Distance());

	    for(i=0;i<train_no;i++)
	    {
	    	System.out.println(distance[i].dist + " " + distance[i].id + "  "+ person[distance[i].id].name);
	    }


//		int index = get_min_index(eucDist, train_no);
            System.out.println(" Closest matching index is : "+(distance[0].id));

//            JSONArray res=new JSONArray();

            List<String> res = new ArrayList<String>();


      //      CharSequence[] res=new CharSequence[0];

            for (i=0;i<train_no;i++)
            {
                if (!res.contains(person[distance[i].id].name))
                res.add(person[distance[i].id].name);


//                res.put(distance[i].id);
                if (distance[i].dist / distance[0].dist > 10)
                    break;
            }
            final CharSequence[] returnResult = res.toArray(new CharSequence[res.size()]);
            Log.d("Attendance",returnResult.toString());

            Bundle b=new Bundle();

//            Log.e("Attendance",returnResult.toString());
            b.putCharSequenceArray("result",returnResult);

            Log.d("Attendance", MainActivity.RESULT_OK+ "  ");
            receiver.send(MainActivity.RESULT_OK,b);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (trainInfoReader != null)trainInfoReader.close();
                    if (mReader != null)mReader.close();
                    if (eigenReader != null)eigenReader.close();
                    if (projImgReader != null)projImgReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
    static int[] getPixels(Bitmap img, int x, int y) {
        int argb = img.getPixel(x,y);

        int rgb[] = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };
        return rgb;
    }

    static void difference(double[] diff, double[] meanImage, int[] inImage, int n)
    {
        int i;
//		System.out.println("Diffenrence matrix is : ");
        for(i=0; i<n; i++)
        {
            diff[i] = inImage[i] - meanImage[i];
//			System.out.print(diff[i] + " ");
        }
    }
    static void conj_multiply(double[][] eigFace, int eRow, int eCol, double[] diff, int dRow, double[] projTestImg)
    {
        int i,j;
        double sum;
//		System.out.println("ProjTestImg");
        for (i=0; i<eCol; i++)
        {
            sum=0;
            for (j=0; j<eRow; j++)
            {
                sum += eigFace[j][i]*diff[j];
            }
            projTestImg[i] = sum;
//			System.out.println(projTestImg[i]);
        }
//		System.out.println("Done!");
    }

    static void euc_dist(int train_no, double[][] projImages, int projRow, int projCol, double[] projTestImg, int projTestRow, int projTestCol, double[] eucDist, Data[] distance)
    {
		/*
		 * Euc_dist = [];
			for i = 1 : Train_Number
    			q = ProjectedImages(:,i);
    			temp = ( norm( ProjectedTestImage - q ) )^2;
    			Euc_dist = [Euc_dist temp];
			end
*/
        int i,j;
        double sum;
//		System.out.println("trainno: "+ train_no);
//		System.out.println("projRow" + projRow + " projCol: "+ projCol);

        for(i=0; i<train_no ; i++)
        {
            sum=0;
            for(j=0; j<projRow; j++)
            {
//				System.out.println("i= "+ i + "j= "+ j);
                sum += Math.pow( projTestImg[j] - projImages[j][i], 2) ;

            }

            eucDist[i] = sum;

            distance[i] = new Data(eucDist[i], i+1);
//			System.out.println(distance[i].dist + " " + distance[i].id);
        }


    }

}
