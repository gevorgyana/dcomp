package Lab8;

public class MainRunner {
    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000};

        for (int matSize : sizes) {
            SimpleMatrix.calculate(args, matSize);
            StringMatrix.calculate(args, matSize);
            FoxMatrix.calculate(args, matSize);
            CannonMatrix.calculate(args, matSize);
        }
    }
}