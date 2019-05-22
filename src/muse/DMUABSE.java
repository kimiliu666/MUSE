package muse;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import model.IndexBlock;
import model.MasterSecretKey;
import model.OwnerSecretKey;
import model.UserSecretKey;
import muse.util.Util;
import muse.util.publicParameter;

public class DMUABSE {
	private Pairing pairing;
	private Element g;
	private Element h;
	private Element A;
	private Element B;
	private Element N;

	private static int att_number=publicParameter.ATT_SIZE;
	private static String[] attributes = Arrays.copyOf(publicParameter.ATTRIBUTES,att_number);



	HashMap<String, LinkedList<IndexBlock>> searchTable=new HashMap<>();

	
	public MasterSecretKey systemSetUp (String properties) {
		this.pairing=PairingFactory.getPairing(properties);
		Element alpha = pairing.getZr().newRandomElement().getImmutable();
		Element beta = pairing.getZr().newRandomElement().getImmutable();
		Element nu = pairing.getZr().newRandomElement().getImmutable();
		this.g = pairing.getG1().newRandomElement().getImmutable();
		this.h = pairing.getG1().newRandomElement().getImmutable();
		this.A=pairing.pairing(g, g).powZn(alpha).getImmutable();
		this.B=g.powZn(beta).getImmutable();
		this.N=h.powZn(nu).getImmutable();
		MasterSecretKey masterSecretKey=new MasterSecretKey();
		masterSecretKey.alpha=alpha.duplicate().getImmutable();
		masterSecretKey.beta=beta.duplicate().getImmutable();
		masterSecretKey.nu=nu.duplicate().getImmutable();
		return masterSecretKey;
		
	}
	
	
	public OwnerSecretKey generateOwnerKey(MasterSecretKey masterKey) {
		Element mu = pairing.getZr().newRandomElement().getImmutable();
		
		Element M1=g.powZn(mu).getImmutable();
		Element M2=h.powZn(mu).getImmutable();
		Element RK=g.powZn(masterKey.nu.div(mu)).getImmutable();
		OwnerSecretKey ownerSecretKey=new OwnerSecretKey();
		ownerSecretKey.M1=M1.duplicate().getImmutable();
		ownerSecretKey.M2=M2.duplicate().getImmutable();
		ownerSecretKey.RK=RK.duplicate().getImmutable();
		return ownerSecretKey;
	} 
	
	
	public UserSecretKey genereteUserKey(MasterSecretKey masterKey) {
		Element mu = pairing.getZr().newRandomElement().getImmutable();
		
		Element r = pairing.getZr().newRandomElement().getImmutable();
		Element[] hash_xi=new Element[this.att_number];
		Element[] ri=new Element[this.att_number];
		Element[] Di=new Element[this.att_number];
		Element rsum=pairing.getZr().newZeroElement();
		Element D2=pairing.getG1().newOneElement();
		UserSecretKey userSecretKey=new UserSecretKey();
		userSecretKey.Di=new Element[this.att_number];
		for (int i = 0; i < this.att_number; i++) {
			hash_xi[i]=Util.h1_hashAttElement(pairing, this.attributes[i]);
			ri[i]=pairing.getZr().newRandomElement().getImmutable();
			rsum=rsum.add(ri[i]);
			Di[i]=g.powZn(ri[i]).mul(hash_xi[i].powZn(r)).getImmutable();
			userSecretKey.Di[i]=Di[i].duplicate().getImmutable();
			D2=D2.mul(hash_xi[i].powZn(masterKey.beta));
		}
		
		Element D1 = g.powZn(masterKey.alpha.add(rsum).div(masterKey.beta)).getImmutable();
		Element D3 = g.powZn(r).getImmutable();
		Element M1=g.powZn(mu).getImmutable();
		Element M2=h.powZn(mu).getImmutable();
		Element RK=g.powZn(masterKey.nu.div(mu)).getImmutable();
		userSecretKey.M1=M1.duplicate().getImmutable();
		userSecretKey.M2=M2.duplicate().getImmutable();
		userSecretKey.RK=RK.duplicate().getImmutable();
		userSecretKey.D1=D1.duplicate().getImmutable();
		userSecretKey.D2=D2.duplicate().getImmutable();
		userSecretKey.D3=D3.duplicate().getImmutable();
		return userSecretKey;
	}
	
	public void update(OwnerSecretKey ownerSecretKey,String fileAddress,BigInteger message,String keyword) {
		Element s = pairing.getZr().newRandomElement().getImmutable();
		Element s2 = pairing.getZr().newRandomElement().getImmutable();
		Element C1=B.powZn(s).getImmutable();
		Element C2=g.powZn(s).getImmutable();
		BigInteger C3=A.powZn(s).toBigInteger().multiply(message);
		Element[] Ci=new Element[this.att_number];
		for (int i = 0; i < this.att_number; i++) {
			Element hash_wi = Util.h1_hashAttElement(pairing, this.attributes[i]);
			Ci[i]=hash_wi.powZn(s).getImmutable();
		}
		IndexBlock indexBlock=new IndexBlock(C1,C2,C3,Ci,fileAddress);
		//关键字
		Element Lw1=ownerSecretKey.M1.powZn(Util.h2_hashKeyWord(pairing, keyword)).mul(ownerSecretKey.M2.powZn(s2)).getImmutable();
		Element Lw2=g.powZn(s2).getImmutable();
		//代理服务器重加密
		Element Lwp=pairing.pairing(Lw1, ownerSecretKey.RK).div(pairing.pairing(Lw2, N)).getImmutable();
		String lwp_md5 = Util.h3_hashPREresult(Lwp);
		//云服务器存放矩阵
		LinkedList<IndexBlock> searchBlockResult = searchTable.get(lwp_md5);
		if (searchBlockResult==null) {
			LinkedList<IndexBlock> indexBlockList=new LinkedList<IndexBlock>();
			indexBlockList.add(indexBlock);
			searchTable.put(lwp_md5, indexBlockList);
		}else {
			searchBlockResult.add(indexBlock);
			searchTable.put(lwp_md5, searchBlockResult);
		}
	}


	
	
	public LinkedList<IndexBlock> search(UserSecretKey userSecretKey,String searchKeyWord) {
		System.out.println("搜索关键字："+searchKeyWord);
		//user搜索token
		Element t = pairing.getZr().newRandomElement().getImmutable();
		Element tk1=userSecretKey.D2.powZn(t);
		Element tk2=B.powZn(t);
		
		//关键字
		Element Lw1=userSecretKey.M1.powZn(Util.h2_hashKeyWord(pairing, searchKeyWord)).mul(userSecretKey.M2.powZn(t)).getImmutable();
		Element Lw2=g.powZn(t).getImmutable();
		//代理服务器重加密
		Element test=pairing.pairing(Lw1, userSecretKey.RK).div(pairing.pairing(Lw2, N)).getImmutable();
		String test_md5 = Util.h3_hashPREresult(test);
		LinkedList<IndexBlock> searchIndexBlockList = searchTable.get(test_md5);
		if (searchIndexBlockList==null) {
			System.out.println("搜索结果空");
			return new LinkedList<IndexBlock>();
		}
		LinkedList<IndexBlock> resultIndexBlocks= new LinkedList<>();
		
		for (IndexBlock searchIndexBlock : searchIndexBlockList) {
			Element RC1=searchIndexBlock.C1;
			Element RC2=searchIndexBlock.C2;
			BigInteger RC3=searchIndexBlock.C3;
			Element[] RCi=searchIndexBlock.Ci;
			//Test
			Element test1=pairing.pairing(tk1, RC2);
			Element Ci_mul=pairing.getG1().newOneElement();
			for (int i = 0; i < this.att_number; i++) {
				Ci_mul=Ci_mul.mul(RCi[i]).getImmutable();
			}
			Element test2=pairing.pairing(Ci_mul, tk2);
			//System.out.println(searchIndexBlock.fileAddress+"Test匹配："+test1.equals(test2));
			if(test1.equals(test2)) {
				resultIndexBlocks.add(searchIndexBlock);
			}	
		}
		return searchIndexBlockList;
	}
	
	
	public void decrypt(UserSecretKey userSecretKey,LinkedList<IndexBlock> resultIndexBlocks,boolean printInfo) {
		if (resultIndexBlocks==null) {
			System.out.println("解密链表空");
			return;
		}
		for (IndexBlock searchIndexBlock : resultIndexBlocks) {
			Element RC1=searchIndexBlock.C1;
			Element RC2=searchIndexBlock.C2;
			BigInteger RC3=searchIndexBlock.C3;
			Element[] RCi=searchIndexBlock.Ci;
			//解密
			Element E = pairing.getGT().newOneElement();
			for (int i = 0; i < att_number; i++) {
				Element div = pairing.pairing(userSecretKey.Di[i], RC2).div(pairing.pairing(userSecretKey.D3, RCi[i]));
				E=E.mul(div);
			}
			Element decyptElement=pairing.pairing(userSecretKey.D1, RC1).div(E).getImmutable();
			BigInteger message = RC3.divide(decyptElement.toBigInteger());
			if (printInfo==true) {
				System.out.println("文件地址："+searchIndexBlock.fileAddress+",解密:"+message);
			}
			
		}
	}
	
	
}
