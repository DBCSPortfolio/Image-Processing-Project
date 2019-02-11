/**************************************************
 * Damon Black
 * CMSC 465 (Class 22669, Section 7980)
 * Project 4, Question 6
 * 5/6/2018
 * 
 * Dependencies:
 *    - Dilation.java
 *    - Erosion.java
 **************************************************/

/* This program performs a closing operation on a black and white image;
 * that is, a dilation operation is performed on an input image, and the
 * resulting image is then subjected to an erosion operation. A one-row-by-
 * one-column white background is maintained for the purposes of this program.
 */

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Closing
{
   // final constants
   public static final int BLACK_PIXEL = 0;
   public static final int WHITE_PIXEL = 255;
   
   // private member data
   private int[][] imagePixels; // contains initial pixel values in image
   private File imageFile; // variable for filename validation
   private BufferedImage image; // holds an existing image file
   private int imageWidth; // width (in pixels) of image passed to program
   private int imageHeight; // height (in pixels) of image passed to program
   private Color tempColor; // stores RGB value for each pixel in input image
   private String outputFileName; // stores name for modified image file
   
   // constructor
   public Closing(String userFileString) throws Exception
   {
      imageFile = new File(userFileString);
      
      // checks if provided file actually exists
      if (!validateFile(imageFile))
      {
         throw new FileNotFoundException("You provided a nonexistent file.");
      }
      
      // reads file
      image = ImageIO.read(imageFile);
      imageWidth = image.getWidth();
      imageHeight = image.getHeight();
      
      // makes sure image is of valid size
      if (!validateSize(imageWidth, imageHeight))
      {
         throw new Exception("Image file is invalid!");
      }
      
      // initializing arrays for program
      imagePixels = new int[imageWidth][imageHeight];
      
      // fills pixel array with modified pixel values from image
      for (int g = 0; g < imageWidth; g++)
      {
         for (int h = 0; h < imageHeight; h++)
         {
            // gets RGB value of each pixel in original image
            tempColor = new Color(image.getRGB(g, h));
            
            if (tempColor.getRed() == WHITE_PIXEL &&
                  tempColor.getGreen() == WHITE_PIXEL &&
                  tempColor.getBlue() == WHITE_PIXEL)
            {
               imagePixels[g][h] = WHITE_PIXEL;
            }
            
            if (tempColor.getRed() == BLACK_PIXEL &&
                  tempColor.getGreen() == BLACK_PIXEL &&
                  tempColor.getBlue() == BLACK_PIXEL)
            {
               imagePixels[g][h] = BLACK_PIXEL;
            }
         }
      }
      
      // performs dilation, then erosion
      imagePixels = Dilation.dilateImage(imagePixels, imageHeight, imageWidth);
      imagePixels = Erosion.erodeImage(imagePixels, imageHeight, imageWidth);
      
      // writes result into new image file
      writeImageToFile(fillNewImage());
   }
   
   // fills pixels of a BufferedImage object with modified pixel array
   private Image fillNewImage()
   {
      BufferedImage newImage = new BufferedImage(imageHeight, imageWidth,
            BufferedImage.TYPE_INT_ARGB);
      for (int i = 0; i < imageWidth; i++)
      {
         for (int j = 0; j < imageHeight; j++)
         {
            if (imagePixels[i][j] == BLACK_PIXEL)
            {
               newImage.setRGB(i, j, Color.BLACK.getRGB());
            }
            if (imagePixels[i][j] == WHITE_PIXEL)
            {
               newImage.setRGB(i, j, Color.WHITE.getRGB());
            }
         }
      }
      return newImage;
   }
   
   // writes resulting BufferedImage to a new image file
   private void writeImageToFile(Image image) throws Exception
   {
      outputFileName = "outputClosing.png";
      try
      {
         BufferedImage bi = (BufferedImage) image;
         File outputFile = new File(outputFileName);
         ImageIO.write(bi, "png", outputFile);
      }
      catch (IOException e)
      {
         throw new IOException("File cannot be written.");
      }
   }
   
   // accessor for output image file name
   public String getOutputFileName()
   {
      return outputFileName;
   }
   
   // determines whether or not passed file name exists
   private static boolean validateFile(File userFile)
   {
      if (userFile.isFile())
      {
         return true;
      }
      return false;
   }
   
   // determines if image is of valid size (same dimension size, power of 2)
   private static boolean validateSize(int width, int height)
   {
      // dimensions must be same
      if ((width != height))
      {
         return false;
      }
      
      // accounts for image of 1 pixel only - 1 is a power of two!
      if (width == 1 && height == 1)
      {
         return true;
      }
      
      // tests if dimensions are powers of 2
      int powerTest = width;
      while (((powerTest != 2) && powerTest % 2 == 0) || powerTest == 1)
      {
         powerTest = powerTest / 2;
      }
      
      if (powerTest != 2)
      {
         return false;
      }
      return true;
   }
   
   public static void main(String[] args) throws Exception
   {
      Closing newClosing = new Closing("ExampleImage1.png");
      // retrieves output file name and displays image in Java GUI
      String newImageFile = newClosing.getOutputFileName();
      JFrame newFrame = new JFrame("Closed Image");
      JLabel newLabel = new JLabel(new ImageIcon(newImageFile));
      newFrame.setSize(300, 300);
      newFrame.add(newLabel);
      newFrame.setVisible(true);
   }
}