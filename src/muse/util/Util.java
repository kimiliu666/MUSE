package muse.util;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.codec.digest.Md5Crypt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class Util {

		private static int GlobalFilesAddress=0;

		public static Element h1_hashAttElement(Pairing pairing,String attribute) {
			byte[] att_bytes = attribute.getBytes();
			Element hashElement = pairing.getG1().newElement().setFromHash(att_bytes, 0, att_bytes.length).getImmutable();
			return hashElement;
		}
		
		public static Element h2_hashKeyWord(Pairing pairing,String keyword) {
			byte[] key_bytes = keyword.getBytes();
			Element hashElement = pairing.getZr().newElement().setFromHash(key_bytes, 0, key_bytes.length);
			return hashElement;
		}		
		
		public static String h3_hashPREresult(Element Lwp) {
			return Md5Crypt.apr1Crypt(Lwp.toString(),"DciHxqYl");
		}
		
		public static HashMap<String, BigInteger> getRandomFiles(int fileNumbers){
			HashMap<String, BigInteger> filesHashMap=new HashMap<>();
			Random random = new Random();
			for (int i = 0; i < fileNumbers; i++) {
				String fileAddress="1310000000000"+GlobalFilesAddress++;
				String fileMessageString=String.valueOf(random.nextLong());
				//System.out.println("生成文件，地址："+fileAddress+",明文："+fileMessageString);
				filesHashMap.put(fileAddress, new BigInteger(fileMessageString));
			}
			return filesHashMap;
		}
		
		public static HashMap<String, BigInteger> getRandomFiles2(int fileNumbers){
			HashMap<String, BigInteger> filesHashMap=new HashMap<>();
			Random random = new Random();
			for (int i = 0; i < fileNumbers; i++) {
				String fileAddress="1320000000000"+i;
				String fileMessageString=String.valueOf(random.nextLong());
				filesHashMap.put(fileAddress, new BigInteger(fileMessageString));
			}
			return filesHashMap;
		}
		
		public static HashMap<String, BigInteger> getRandomFiles3(int fileNumbers){
			HashMap<String, BigInteger> filesHashMap=new HashMap<>();
			Random random = new Random();
			for (int i = 0; i < fileNumbers; i++) {
				String fileAddress="1330000000000"+i;
				String fileMessageString=String.valueOf(random.nextLong());
				filesHashMap.put(fileAddress, new BigInteger(fileMessageString));
			}
			return filesHashMap;
		}
		
		
		public static String getRandomKeyword(int boundary) {
			Random random=new Random();
			return publicParameter.KEYWORD_DICIONARY[random.nextInt(boundary)];
			
		}
		
}
