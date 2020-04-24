package Lab9.Task1;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Main {

    static class Task extends RecursiveAction {
        private int[] A, B, C;
        private int ID;
        private final int N = 1000;
        private final int parts = 20;

        Task(int[] a, int[] b, int[] c) {
            this(a, b, c, -1);
        }

        void mult() {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    C[ID * N + i] += A[ID * N + j] * B[j * N + i];
        }

        @Override
        protected void compute() {
            if (ID < 0) {
                ArrayList tasks = new ArrayList<Task>();
                for (int i = 0; i < N; i++)
                    tasks.add(new Task(A, B, C, i));
                invokeAll(tasks);
            }
            else {
                mult();
            }
        }

        Task(int[] a, int[] b, int[] c, int id) {
            A = a;
            B = b;
            C = c;
            ID = id;
        }
    }

    public static int[] getRandomSquareMatrix(int N) {
        Random rand = new Random();
        int[] mat = new int[N*N];
        for (int i = 0; i < N * N; i++)
            mat[i] = -100 + rand.nextInt(201);
        return mat;
    }

    public static void main(String[] args) {
        final int N = 1000;
        int[] A = getRandomSquareMatrix(N);
        int[] B = getRandomSquareMatrix(N);
        int[] C = new int[N * N];


        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        forkJoinPool.invoke(new Task(A, B, C));
        long end = System.currentTimeMillis();
        System.out.println("1000 x 1000: " + (end - start) + " ms");
    }
}