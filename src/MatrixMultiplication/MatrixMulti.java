package MatrixMultiplication;


class MatrixMulti extends Thread
{

    private final double[][] firstMatrix;

    private final double[][] secondMatrix;

    private final double[][] resultMatrix;

    private final int firstIndex;

    private final int lastIndex;

    private final int sumLength;

    /**
     * @param firstMatrix  First matrix
     * @param secondMatrix Second matrix
     * @param resultMatrix Resulting matrix
     * @param firstIndex   First index for thread
     * @param lastIndex    Last index for thread
     */
    public MatrixMulti(final double[][] firstMatrix,
                       final double[][] secondMatrix,
                       final double[][] resultMatrix,
                       final int firstIndex,
                       final int lastIndex)
    {
        this.firstMatrix  = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.firstIndex   = firstIndex;
        this.lastIndex    = lastIndex;

        sumLength = secondMatrix.length;
    }

    private void findElementOfResultMatrix(final int row, final int col)
    {
        int sum = 0;
        for (int i = 0; i < sumLength; ++i)
            sum += firstMatrix[row][i] * secondMatrix[i][col];
        resultMatrix[row][col] = sum;
    }
 
    @Override
    public void run()
    {
        final int numberOfColumns = secondMatrix[0].length; 
        for (int index = firstIndex; index < lastIndex; ++index)
        	findElementOfResultMatrix(index / numberOfColumns, index % numberOfColumns);
    }
}
