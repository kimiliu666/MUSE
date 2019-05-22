package question;

import java.util.Scanner;
import java.util.Arrays;
public class DaShuXiangCheng{
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        String s1=s.next();
        String s2=s.next();
        
        char[] chars1=s1.toCharArray();
        char[] chars2=s2.toCharArray(); 
        int l1=chars1.length;
        int l2=chars2.length;
        int l3=l1+l2;
        
        int[] ints1=new int[l1];
        int[] ints2=new int[l2];
        
        int temp,a,b,n;
        
        for(int i=0;i<l1;i++){
            ints1[i]=chars1[l1-1-i]-'0';
        }
        for(int i=0;i<l2;i++){
            ints2[i]=chars2[l2-1-i]-'0';
        }
        int[] result=new int[l3];
        for(int i=0;i<l2;i++){
            for(int j=0;j<l1;j++){
                n=i+j;
                temp=ints2[i]*ints1[j];
                a=temp/10;
                b=temp%10;
                result[n]+=b;
                result[n+1]+=a;
            }
        }
        for (int i = 0; i < result.length; i++) {
			if (result[i]>=10) {
				result[i+1]+=result[i]/10;
				result[i]%=10;
			}
		}
        System.out.println(Arrays.toString(result));
        StringBuilder sBuilder=new StringBuilder();
        int tag=0;
        for (int i = 0; i < result.length; i++) {
			if (result[l3-1-i]==0 && tag==0) {
				tag=1;
				continue;
			}
        	sBuilder.append(result[l3-1-i]);
		}
        System.out.println(sBuilder.toString());
    }
}
