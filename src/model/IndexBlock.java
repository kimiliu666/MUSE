package model;
import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;




public class IndexBlock {
	private int att_number;
	public Element C1;
	public Element C2;
	public BigInteger C3;
	public Element[] Ci;
	public String fileAddress;
	public IndexBlock(Element c1, Element c2, BigInteger c3, Element[] ci, String fileAddress) {
		super();
		C1 = c1;
		C2 = c2;
		C3 = c3;
		Ci = ci;
		this.fileAddress = fileAddress;
	}
	
	
}
