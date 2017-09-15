package math.leastSquare;
public class LeastSquareMethod {  
    private double[] x;  //key
    private double[] y;  //value
    private double[] weight;  //weight
    private int m;  // Dimension 维度 元
  
    private double[] coefficient;  
  
    public LeastSquareMethod(double[] x, double[] y, int m) {  
        if (x == null || y == null || x.length < 2 || x.length != y.length  
                || m < 2)  
            throw new IllegalArgumentException("无效的参数");  
        this.x = x;  
        this.y = y;  
        this.m = m;  
        weight = new double[x.length];  
        for (int i = 0; i < x.length; i++) {  
            weight[i] = 1;  
        }  
    }  
  
    public LeastSquareMethod(double[] x, double[] y, double[] weight, int m) {  
        if (x == null || y == null || weight == null || x.length < 2  
                || x.length != y.length || x.length != weight.length || m < 2)  
            throw new IllegalArgumentException("无效的参数");  
        this.x = x;  
        this.y = y;  
        this.m = m;  
        this.weight = weight;  
    }  
  
    public double[] getCoefficient() {  
        if (coefficient == null)  
            compute();  
        return coefficient;  
    }  
  
    public double fit(double v) {  
        if (coefficient == null)  
            compute();  
        if (coefficient == null)  
            return 0;  
        double sum = 0;  
        for (int i = 0; i < coefficient.length; i++) {  
            sum += Math.pow(v, i) * coefficient[i];  
        }  
        return sum;  
    }  
  
    private void compute() {  
        if (x == null || y == null || x.length <= 1 || x.length != y.length  
                || x.length < m || m < 2)  
            return;  
  
        double[] s = new double[(m - 1) * 2 + 1];  
        for (int i = 0; i < s.length; i++) {  
            for (int j = 0; j < x.length; j++)  
                s[i] += Math.pow(x[j], i) * weight[j];  
        }  
        double[] f = new double[m];  
        for (int i = 0; i < f.length; i++) {  
            for (int j = 0; j < x.length; j++)  
                f[i] += Math.pow(x[j], i) * y[j] * weight[j];  
        }  
        double[][] a = new double[m][m];  
        for (int i = 0; i < m; i++) {  
            for (int j = 0; j < m; j++) {  
                a[i][j] = s[i + j];  
            }  
        }  
        coefficient = Algorithm.multiLinearEquationGroup(a, f);  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        LeastSquareMethod l = new LeastSquareMethod(  
                new double[] { 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008 },   
                new double[] { 37.84, 44.55, 45.74, 63.8, 76.67, 105.59, 178.48, 355.27, 409.92 },   
                new double[] { 11, 12, 13, 14, 15, 16, 17, 18, 19 },  
                2);  
        l = new LeastSquareMethod(  
                new double[] {1 ,3,4},   
                new double[] { 2,6 ,7},     
                2);  
        double[] x = l.getCoefficient();  
        for (double xx : x) {  
            System.out.println(xx);  
        }  
        System.out.println(l.fit(5));  
    }  
  
}  