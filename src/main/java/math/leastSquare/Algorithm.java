package math.leastSquare;
public class Algorithm {  
    /** 
     * 多元一次方程求解 
     * @param a 系数数组 
     * @param b 结果数组 
     * @return 解答 
     */  
    public static double[] multiLinearEquationGroup(double[][] a,double[] b){  
        if (a==null||b==null||a.length==0||a.length!=b.length)  
            return null;  
        for (double[] x:a){  
            if (x==null||x.length!=a.length)  
                return null;  
        }  
          
        int len=a.length-1;  
        double[] result=new double[a.length];  
          
        if (len==0){  
            result[0]=b[0]/a[0][0];  
            return result;  
        }  
          
        double[][] aa=new double[len][len];  
        double[] bb=new double[len];  
        int posx=-1,posy=-1;  
        for (int i=0;i<=len;i++){  
            for (int j=0;j<=len;j++)  
                if (a[i][j]!=0.0d){  
                    posy=j;  
                    break;  
                }  
            if (posy!=-1){  
                posx=i;  
                break;  
            }  
        }  
        if (posx==-1)  
            return null;  
          
        int count=0;  
        for (int i=0;i<=len;i++){  
            if (i==posx)  
                continue;  
            bb[count]=b[i]*a[posx][posy]-b[posx]*a[i][posy];  
            int count2=0;  
            for (int j=0;j<=len;j++){  
                if (j==posy)  
                    continue;  
                aa[count][count2]=a[i][j]*a[posx][posy]-a[posx][j]*a[i][posy];  
                count2++;  
            }  
            count++;  
        }  
          
        double[] result2=multiLinearEquationGroup(aa,bb);  
          
        double sum=b[posx];  
        count=0;  
        for (int i=0;i<=len;i++){  
            if (i==posy)  
                continue;  
            sum-=a[posx][i]*result2[count];  
            result[i]=result2[count];  
            count++;  
        }  
        result[posy]=sum/a[posx][posy];  
          
        return result;  
    }  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        double[] r=multiLinearEquationGroup(new double[][]{{1,2,3,1},{2,0,1,0},{5,2,0,0},{7,1,1,0}},new double[]{18,5,9,12});  
        for (int i=0;i<r.length;i++){  
            System.out.println(r[i]);  
        }  
  
    }  
  
}  