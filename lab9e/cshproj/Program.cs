using System;
using System.Diagnostics;
using System.Threading.Tasks;

class MultiplyMatrices
{
    static void MatrixMultiplyParallel(long[] A, long[] B, long[] C, int N) {
        int rows_a = N, cols_a = N;
        int rows_b = N, cols_b = N;
        
        Parallel.For(0, rows_a, i => {
            for (int j = 0; j < cols_b; j++) {
                long partial_res = 0;
                for (int k = 0; k < cols_a; k++)
                    partial_res += A[i* rows_a + k] * B[k* rows_b + j];
                C[i* rows_a + j] = partial_res;
            }
        });
    }

    static void Main(string[] args)
    {
        const int rows = 1000, cols = 1000;
        long[ ] mat1 = InitializeMatrix(rows, cols);
        long[ ] mat2 = InitializeMatrix(rows, cols);
        long[ ] result_matrix = new long[rows * cols];

        Stopwatch timer = new Stopwatch();
        timer.Start();
        MatrixMultiplyParallel(mat1, mat2, result_matrix, rows);
        timer.Stop();
        Console.WriteLine("1000 x 1000: {0} ms", timer.ElapsedMilliseconds);
    }

    static long[] InitializeMatrix(int rows, int cols) {
        long[] mat = new long[rows * cols];

        Random rand = new Random();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                mat[i* rows + j] = -100 + rand.Next(201);
        return mat;
    }
}