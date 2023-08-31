import parcs.*;

public class FindNormMatrix implements AM {

    public void run(AMInfo info) {
        // отримання даних з каналу: параметра p, q i частину матриці
        int p = info.parent.readInt();
        int q = info.parent.readInt();
        double[][] data = (double[][]) info.parent.readObject();

        // Обчислення норми
        double norm = 0.0;
        for (int i = 0; i < data.length; i++) {
            double sum = 0.0;
            // сума елементів в степені p по рядках
            for (int j = 0; j < data[i].length; j++) {
                sum += Math.pow(data[i][j], p);
                System.out.println("sum " + sum);
            }

            sum = Math.pow(sum, (double)q/(double)p);
            System.out.println("summary " + sum);
            norm += sum;
        }

        // передача результату на батьківський вузол
        info.parent.write(norm);
    }
}