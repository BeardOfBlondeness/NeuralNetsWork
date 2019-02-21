package CS2NN16;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MNSITReader {

	
	public static void main(String[] args) {
		toPNG();
	}
	
	private static void toPNG() {
		  // TODO Auto-generated method stub
        FileInputStream inImage = null;
        FileInputStream inLabel = null;

        String inputImagePath = "mnist/train-images.idx3-ubyte";
        String inputLabelPath = "mnist/train-labels.idx1-ubyte";

        String outputPath = "output/";
        
        String dataString = "";

        int[] hashMap = new int[10]; 

        try {
            inImage = new FileInputStream(inputImagePath);
            inLabel = new FileInputStream(inputLabelPath);

            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfRows  = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

            BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_INT_ARGB);
            int numberOfPixels = numberOfRows * numberOfColumns;
            int[] imgPixels = new int[numberOfPixels];

            dataString += Integer.toString(numberOfImages * numberOfPixels);
            dataString += " 0;";
            
            for(int p = 0; p < numberOfPixels; p++) {
            	dataString+= "x" + Integer.toString(p) + " ";
            }
            dataString += "Target;";
            
            for(int i = 0; i < 5; i++) {

                if(i % 100 == 0) {System.out.println("Number of images extracted: " + i);}

                for(int p = 0; p < numberOfPixels; p++) {
                    int gray = 255 - inImage.read();
                    imgPixels[p] = 0xFF000000 | (gray<<16) | (gray<<8) | gray;
                }

                image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);

                int label = inLabel.read();

                hashMap[label]++;
                File outputfile = new File(outputPath + label + "_0" + hashMap[label] + ".png");

               // ImageIO.write(image, "png", outputfile);
                for(int u = 0; u < imgPixels.length; u++) {
                    dataString += Integer.toString(imgPixels[u]) + " ";
                }
                dataString+= Integer.toString(hashMap[label]);
                dataString+= ";";
            }

            DataSet MNIST = new DataSet(dataString);
            MultiLayerNetwork MLN = new MultiLayerNetwork(numberOfPixels, 1, MNIST, new SigmoidLayerNetwork(numberOfPixels, 0, MNIST));
            MLN.presentDataSet(MNIST);
			MLN.doInitialise();
			System.out.println(MLN.doPresent());
			System.out.println("Weights " + MLN.getWeights());
			System.out.println(MLN.doLearn(10,  5,  0.6));
			System.out.println(MLN.doPresent());
			System.out.println("Weights " + MLN.getWeights());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inImage != null) {
                try {
                    inImage.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inLabel != null) {
                try {
                    inLabel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
	}
	
}
