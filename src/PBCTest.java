import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.codec.digest.Md5Crypt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import model.IndexBlock;
import muse.util.Util;
import muse.util.publicParameter;


public class PBCTest {
	private static final SimpleDateFormat SDF=new SimpleDateFormat("HH:mm:ss");
	HashMap<String, LinkedList<IndexBlock>> searchTable;
	
	public static void main(String[] args) {
		System.out.println(publicParameter.ATTRIBUTES.length);
		System.out.println(publicParameter.KEYWORD_DICIONARY.length);
		System.out.println(SDF.format(new Date()));
	}
	
	
	public static void main2(String[] args) {
		
		long start = System.currentTimeMillis();
		
		Pairing pairing = PairingFactory.getPairing("a.properties");
		HashMap<String, LinkedList<HashMap<String, Object>>> searchTable = new HashMap<>();
		
		//BigInteger M = new BigInteger(UUID.randomUUID().toString());
		BigInteger M = new BigInteger("1231254125343544855685");
		
		//参数
		
		String[] attributes= {"tall","handsome","male","old","single",
				"tall","handsome","male","old","single",
				"tall","handsome","male","old","single",
				"tall","handsome","male","old","single",
				"tall","handsome","male","old","single"};
		String[] attributes2= {"short","handsome","male","old","single"};
		int att_number=attributes.length;
		
		//master secret key
		Element alpha = pairing.getZr().newRandomElement().getImmutable();
		Element beta = pairing.getZr().newRandomElement().getImmutable();
		Element mu = pairing.getZr().newRandomElement().getImmutable();
		Element nu = pairing.getZr().newRandomElement().getImmutable();
		
		
		//public key
		Element g = pairing.getG1().newRandomElement().getImmutable();
		Element h = pairing.getG2().newRandomElement().getImmutable();
		Element A=pairing.pairing(g, g).powZn(alpha).getImmutable();
		Element B=g.powZn(beta).getImmutable();
		Element N=h.powZn(nu).getImmutable();
		
		//搜索私钥
		Element M1=g.powZn(mu).getImmutable();
		Element M2=h.powZn(mu).getImmutable();
		Element RK=g.powZn(nu.div(mu)).getImmutable();
		
		
		//私钥

		Element r = pairing.getZr().newRandomElement().getImmutable();
		Element[] hash_xi=new Element[att_number];
		Element[] ri=new Element[att_number];
		Element[] Di=new Element[att_number];
		Element rsum=pairing.getZr().newZeroElement();
		Element D2=pairing.getG1().newOneElement();
		
		for (int i = 0; i < att_number; i++) {
//			byte[] att_bytes = attributes[i].getBytes();
//			hash_xi[i] = pairing.getG1().newElement().setFromHash(att_bytes, 0, att_bytes.length).getImmutable();
			hash_xi[i]=Util.h1_hashAttElement(pairing, attributes[i]);
			ri[i]=pairing.getZr().newRandomElement().getImmutable();
			rsum=rsum.add(ri[i]);
			Di[i]=g.powZn(ri[i]).mul(hash_xi[i].powZn(r));
			D2=D2.mul(hash_xi[i].powZn(beta));
		}
		
		Element D1 = g.powZn(alpha.add(rsum).div(beta)).getImmutable();
		Element D3 = g.powZn(r).getImmutable();
		
		
		
		//owner生成索引
		Element s = pairing.getZr().newRandomElement().getImmutable();
		Element s2 = pairing.getZr().newRandomElement().getImmutable();
		
		Element C1=B.powZn(s).getImmutable();
		Element C2=g.powZn(s).getImmutable();
		BigInteger C3=A.powZn(s).toBigInteger().multiply(M);
		System.out.println("C3:"+C3);
		Element[] Ci=new Element[att_number];
		for (int i = 0; i < att_number; i++) {
			byte[] att_bytes = attributes[i].getBytes();
			Element hash_wi = pairing.getG1().newElementFromHash(att_bytes, 0, att_bytes.length);
			Ci[i]=hash_wi.powZn(s).getImmutable();
		}
		
		
		
		//文件ID、地址
		String fileAddress="1";
		IndexBlock indexBlock=new IndexBlock(C1,C2,C3,Ci,fileAddress);
		
		//关键字
		String wString="keyword";
		byte[] wBytes = wString.getBytes();
		Element hash_w = pairing.getZr().newElement().setFromHash(wBytes, 0, wBytes.length);
		Element Lw1=M1.powZn(hash_w).mul(M2.powZn(s2)).getImmutable();
		Element Lw2=g.powZn(s2).getImmutable();
		
		//代理服务器重加密
		Element Lwp=pairing.pairing(Lw1, RK).div(pairing.pairing(Lw2, N)).getImmutable();
		System.out.println(Lwp);
		String lwp_md5 = Md5Crypt.apr1Crypt(Lwp.toString(),"DciHxqYl");
		
		HashMap<String, LinkedList<IndexBlock>> searchTable2=new HashMap<>();
		//云服务器存放矩阵
		LinkedList<IndexBlock> searchBlockResult = searchTable2.get(lwp_md5);
		if (searchBlockResult==null) {
			LinkedList<IndexBlock> indexBlockList=new LinkedList<IndexBlock>();
			indexBlockList.add(indexBlock);
			searchTable2.put(lwp_md5, indexBlockList);
		}else {
			searchBlockResult.add(indexBlock);
			searchTable2.put(lwp_md5, searchBlockResult);
		}		
		//user搜索token
		Element t = pairing.getZr().newRandomElement().getImmutable();
		Element tk1=D2.powZn(t);
		Element tk2=B.powZn(t);
		//PRE
		Element test=pairing.pairing(g.powZn(nu), g.powZn(hash_w)).getImmutable();
		String test_md5 = Md5Crypt.apr1Crypt(test.toString(),"DciHxqYl");
		System.out.println("Index配对："+lwp_md5.equals(test_md5));
		
		LinkedList<IndexBlock> searchIndexBlockList = searchTable2.get(test_md5);
		for (IndexBlock searchIndexBlock : searchIndexBlockList) {
			Element RC1=searchIndexBlock.C1;
			Element RC2=searchIndexBlock.C2;
			BigInteger RC3=searchIndexBlock.C3;
			Element[] RCi=searchIndexBlock.Ci;
			
		
			//Test
			Element test1=pairing.pairing(tk1, RC2);
			Element Ci_mul=pairing.getG1().newOneElement();
			for (int i = 0; i < att_number; i++) {
				Ci_mul=Ci_mul.mul(RCi[i]).getImmutable();
			}
			Element test2=pairing.pairing(Ci_mul, tk2);
			System.out.println("Test匹配："+test1.equals(test2));
			
			//解密
			Element E=pairing.pairing(g.powZn(rsum), g.powZn(s)).getImmutable();
			Element Ei_mul = pairing.getGT().newOneElement();
			for (int i = 0; i < att_number; i++) {
				Element div = pairing.pairing(Di[i], C2).div(pairing.pairing(D3, Ci[i]));
				Ei_mul=Ei_mul.mul(div);
			}
			System.out.println("E匹配："+E.equals(Ei_mul));
			Element decyptElement=pairing.pairing(D1, C1).div(Ei_mul).getImmutable();
			System.out.println("Decrypt解密:"+C3.divide(decyptElement.toBigInteger()).equals(M));
			if(test1.equals(test2)) {
				System.out.println("文件："+searchIndexBlock.fileAddress+"，密文："+C3.divide(decyptElement.toBigInteger()));
			}
		}
		long end=System.currentTimeMillis();
		System.out.println(end-start);
		
	}
	
	
	
}
