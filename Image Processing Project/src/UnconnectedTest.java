/**************************************************
 * Damon Black
 * CMSC 465 (Class 22669, Section 7980)
 * Project 4, Question 7
 * 5/6/2018
 **************************************************/

/* This program reads a black and white input image, wherein a black image is
 * situated on a background of white pixels. The program determines which, if
 * any, black pixels in the image would result in any line in the image
 * becoming disconnected from the rest of the image if said pixels were turned
 * white. In the output image, each of these pixels are highlighted as red
 * instead of black or white. A one-row-by-one-column white background is
 * maintained for the purposes of this program.
 */

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class UnconnectedTest
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
   public UnconnectedTest(String userFileString) throws Exception
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
      
      // fill real array with pixel values from image
      for (int g = 0; g < imageWidth; g++)
      {
         for (int h = 0; h < imageHeight; h++)
         {
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
      
      // finds pixels that fit program specifications
      imagePixels = findPixels(imagePixels, imageHeight, imageWidth);
      
      // writes result into new image file
      writeImageToFile(fillNewImage());
   }
   
   // finds and marks pixels that would result in unconnected pixels in image
   public static int[][] findPixels(int[][] pixelArray, int height, int width)
   {
      int count = 0;
      // identifies pixels that would result in unconnected pixels in image
      for (int r = 1; r < height - 1; r++) // background is always present
      {
         for (int c = 1; c < width - 1; c++)
         {
            // black pixels need to be considered for this problem
            if (pixelArray[r][c] == BLACK_PIXEL)
            {
               /* it is important here that the first part of each condition
                * is tested for being less than or equal to 1 - any pixel
                * with a value of 1 in the array is still a black pixel, that
                * just so happens to be one that would result in an unconnected
                * line in the image if removed.
                */
               
               /* calculates # of black-to-white clockwise transitions in the
                  pixels surrounding a current pixel */
               if (pixelArray[r - 1][c - 1] <= 1 && pixelArray[r - 1][c] > 1)
               {
                  count++;
               }
               if (pixelArray[r - 1][c] <= 1 && pixelArray[r - 1][c + 1] > 1 &&
                     pixelArray[r][c + 1] > 1)
               {
                  count++;
               }
               if (pixelArray[r - 1][c + 1] <= 1 && pixelArray[r][c + 1] > 1)
               {
                  count++;
               }
               if (pixelArray[r][c + 1] <= 1 && pixelArray[r + 1][c + 1] > 1 &&
                     pixelArray[r + 1][c] > 1)
               {
                  count++;
               }
               if (pixelArray[r + 1][c + 1] <= 1 && pixelArray[r + 1][c] > 1)
               {
                  count++;
               }
               if (pixelArray[r + 1][c] <= 1 && pixelArray[r + 1][c - 1] > 1 &&
                     pixelArray[r][c - 1] > 1)
               {
                  count++;
               }
               if (pixelArray[r + 1][c - 1] <= 1 && pixelArray[r][c - 1] > 1)
               {
                  count++;
               }
               if (pixelArray[r][c - 1] <= 1 && pixelArray[r - 1][c - 1] > 1 &&
                     pixelArray[r - 1][c] > 1)
               {
                  count++;
               }
       
               // marks all matching pixels with an intermediate value
               if (count > 1)
               {
                  pixelArray[r][c] = 1;
               }
               count = 0;        
            }
         }
      }
      return pixelArray;
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
            // highlights pixels that would result in unconnected pixels
            if (imagePixels[i][j] == 1)
            {
               newImage.setRGB(i, j, Color.RED.getRGB());
            }
         }
      }
      return newImage;
   }
   
   // writes resulting BufferedImage to a new image file
   private void writeImageToFile(Image image) throws Exception
   {
      outputFileName = "outputUnconnected.png";
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
      UnconnectedTest newUCTest = 
            new UnconnectedTest("ConnectedImageExample.png");
      // retrieves output file name and displays image in Java GUI
      String newImageFile = newUCTest.getOutputFileName();
      JFrame newFrame = new JFrame("Unconnected Image");
      JLabel newLabel = new JLabel(new ImageIcon(newImageFile));
      newFrame.setSize(300, 300);
      newFrame.add(newLabel);
      newFrame.setVisible(true);
   }
}