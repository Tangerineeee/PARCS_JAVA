import java.io.*;
import java.util.*;
import java.util.Random;
import parcs.*;


public class Solver implements AM
{
    // головний метод
    public static void main(String[] args)
    {
        System.out.println("The Solver class start method main");
        // запуск нової задачі
        task curtask = new task();
        // прив'язка jar-файлів
        curtask.addJarFile("Solver.jar");
        curtask.addJarFile("FindNormMatrix.jar");

        System.out.println("The Solver class method main adder jar files");
        // Запуск методу для виконання
        (new Solver()).run(new AMInfo(curtask, (channel)null));

        System.out.println("The Solver class method main finish work");
        curtask.end();
    }

    public void run(AMInfo info)
    {
        // параметри p i q, які є параметрами задачі
        int p, q;
        // параметри, які вказують на розмірність матриці і кількість демонів
        int m, n, deamons;

        try
        {
            // ініціалізація потоку для читання
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input_1.txt")));
            // зчитування кожного рядка з файлу у змінні
            p = Integer.parseInt(in.readLine());
            q = Integer.parseInt(in.readLine());
            m = Integer.parseInt(in.readLine());
            n = Integer.parseInt(in.readLine());
            deamons = Integer.parseInt(in.readLine());
        }
        catch (IOException e)
        {
            System.out.print("Reading input data error\n");
            e.printStackTrace();
            return;
        }

        /**
         * Генерація матриці за заданими розмірами
         * **/
        // ініціалізація рандомайзера
        Random random = new Random();
        int maxValue = 100;
        // генерація матриці та заповнення її рандомними числами
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(maxValue);
            }
        }

        // виведення на сервер
        System.out.println("The Solver class have read data from the parent server");
        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("m = " + m);
        System.out.println("n = " + n);

        // Обчислення часу виконання
        long tStart = System.nanoTime();
        // запуск методу для розподілу даних на парксі
        double res = solve(info, deamons, p, q, m, n, matrix);

        long tEnd = System.nanoTime();
        // виведення часу виконання
        System.out.println("Working time on matrix " + m + "x" + n + " processes: " + ((tEnd - tStart) / 1000000) + "ms");
        System.out.println("L"+ p+ ","+ q + " norm = " + res);
    }

    static public double solve(AMInfo info, int deamons, int p, int q, int m, int n, int[][] a)
    {
        // змінна для запису результата
        double Res = 0.0;

        List <point> points = new ArrayList<point>();
        List <channel> channels = new ArrayList<channel>();
        // Connection to points
        for (int versionNumber = 0; versionNumber < deamons; versionNumber++) {
            int x = versionNumber*n/deamons;
            int y = (versionNumber+1)*n/deamons - 1;
            double [][] matrix = new double[m][n/deamons];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n/deamons; j++) {
                   matrix [i][j] = a[i][x + j];
                   //System.out.println(matrix[i][j]);
                }
            }
            System.out.println(versionNumber);
            points.add(info.createPoint());
            System.out.println(points);
            channels.add(points.get(versionNumber).createChannel());
            points.get(versionNumber).execute("FindNormMatrix");
            channels.get(versionNumber).write(p);
            channels.get(versionNumber).write(q);
            channels.get(versionNumber).write(matrix);
        }

        // Mapping results
        for(int versionNumber = 0; versionNumber < deamons; versionNumber++)
        {
            Res += channels.get(versionNumber).readDouble();
            //System.out.println((double)channels.get(versionNumber).readObject());
        }
        double Result = 0.0;
        Result = Math.pow(Res, 1.0/(double)q);

        return Result;
    }
}