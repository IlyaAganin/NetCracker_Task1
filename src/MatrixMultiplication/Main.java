package MatrixMultiplication;

import java.io.IOException;

class Main
{
	/*private static void writeResult(final double[][] resultMatrix) throws IOException {
		StringBuilder builder = new StringBuilder();
	    //System.out.println("Values of resulting matrix:");
	    for (int i = 0; i < resultMatrix.length; i++)  {
        	//System.out.print("(");
	    	builder.append("( ");
        	for (int j = 0; j < resultMatrix[i].length; j++) {
        		//System.out.print(" " + resultMatrix[i][j] + " ");
        		builder.append(resultMatrix[i][j]+"");
        	      if(j < resultMatrix.length - 1)
        	         builder.append(" ");
        	}
        	//System.out.println(")");
        	builder.append(" )\n");
        }
	    try {
	    BufferedWriter writer = new BufferedWriter(new FileWriter("calcMatrix" + ".txt"));
	    writer.write(builder.toString());
	    writer.close();
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}*/

    public static void main(String[] args) throws IOException
    {
    	int max = 10;
    	int min = -max;
    	
    	/*"For matrix multiplication, the number of columns in the first matrix must be equal to the number of rows in the second matrix. 
        The result matrix has the number of rows of the first and the number of columns of the second matrix." */
    	
    	//int n = 5;
    	int n = 1000;
    	int fRows = n, fCols = n, sRows = fCols, sCols = n;
    	
    	//System.out.println("Values of first matrix:");
        final double[][] firstMatrix  = new double[fRows][fCols]; 
        for (int i = 0; i < fRows; i++) {
        	//System.out.print("(");
        	for (int j = 0; j < fCols; j++) {
        		firstMatrix[i][j] = (int)(Math.random() * ((max - min) + 1)) + min;
        		//System.out.print(" " + firstMatrix[i][j] + " ");
        	}
        	//System.out.println(")");
        }
        
        //System.out.println("Values of second matrix:");
        final double[][] secondMatrix = new double[sRows][sCols];  
        for (int i = 0; i < sRows; i++) {
        	//System.out.print("(");
        	for (int j = 0; j < sCols; j++) {
        		secondMatrix[i][j] = (int)(Math.random() * ((max - min) + 1)) + min;
        		//System.out.print(" " + secondMatrix[i][j] + " ");
        	}
        	//System.out.println(")");
        }
        
        long startTime = System.nanoTime();
        Task1.multiplyMatrix(firstMatrix, secondMatrix, Runtime.getRuntime().availableProcessors());
	    long stopTime = System.nanoTime();
	    Long time1 = stopTime - startTime;
	    System.out.println(" Measure execution time for method \"multiplyMatrix\" in nanoseconds:" + time1);
	    
	    startTime = System.nanoTime();
	    Task1.multiplyMatrixOneStream(firstMatrix, secondMatrix);
	    stopTime = System.nanoTime();
	    Long time2 = stopTime - startTime;
	    System.out.println(" Measure execution time for method \"multiplyMatrixOneStream\" in nanoseconds:" + time2);
	    //System.out.println(" Elapsed time comparasement: " + time1.compareTo(time2));
	    switch (time1.compareTo(time2)) {
	    	case (-1):
	    		System.out.println(" Method \"multiplyMatrix\" is faster than method \"multiplyMatrixOneStream\". ");
	    		break;
	    	case(0):
	    		System.out.println(" Both methods are viable.");
	    		break;
	    	case(1):
	    		System.out.println(" Method \"multiplyMatrixOneStream\" is faster than method \"multiplyMatrix\". ");
	    		break;
	    }
	  //final double[][] resultMatrix = Task1.multiplyMatrix(firstMatrix, secondMatrix, Runtime.getRuntime().availableProcessors());
	    //writeResult(resultMatrix);

    }
}


class Task1 {
	
	/** Multithreading matrix multiplication
    *
    * @param firstMatrix  First matrix
    * @param secondMatrix Second matrix
    * @param threadCount Number of threads
    * @return Resulting matrix
    */
   static double[][] multiplyMatrix(final double[][] firstMatrix,
                                    final double[][] secondMatrix,
                                    int threadCount)
   {
   	// Checking number of available threads  
       assert threadCount > 0;

       final int numberOfRows = firstMatrix.length;            
       final int numberOfColumns = secondMatrix[0].length;         
       final double[][] resultMatrix = new double[numberOfRows][numberOfColumns];  

       final int cellsForThread = (numberOfRows * numberOfColumns) / threadCount;  
       int firstIndex = 0;  
       final MatrixMulti[] MatrixMultis = new MatrixMulti[threadCount];  

       // loop for threads
       for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
           int lastIndex = firstIndex + cellsForThread;  
           if (threadIndex == 0) {
               lastIndex = numberOfRows * numberOfColumns;
           }
           // work of one of the threads
           MatrixMultis[threadIndex] = new MatrixMulti(firstMatrix, secondMatrix, resultMatrix, firstIndex, lastIndex);
           MatrixMultis[threadIndex].start();
           firstIndex = lastIndex;
       }

       try {
           for (final MatrixMulti MatrixMulti : MatrixMultis)
               MatrixMulti.join();
       }
       catch (InterruptedException e) {
       	// method from class Throwable 
           e.printStackTrace();
       }

       return resultMatrix;
   }
   
   /** Matrix multiplication
   *
   * @param firstMatrix  First matrix
   * @param secondMatrix Second matrix
   * @return Resulting matrix
   */
  static double[][] multiplyMatrixOneStream(final double[][] firstMatrix,
                                            final double[][] secondMatrix)
  {
      final int numberOfRows = firstMatrix.length;             
      final int numberOfColumns = secondMatrix[0].length;         
      final int sumLength = secondMatrix.length;           
      final double[][] resMatrix = new double[numberOfRows][numberOfColumns];  

      for (int i = 0; i < numberOfRows; i++) {  
          for (int j = 0; j < numberOfColumns; j++) {  
              int sum = 0;
              for (int k = 0; k < sumLength; k++)
                  sum += firstMatrix[i][k] * secondMatrix[k][j];
              resMatrix[i][j] = sum;
          }
      }

      return resMatrix;
  }
}



