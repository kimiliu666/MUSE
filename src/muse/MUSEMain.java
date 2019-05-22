package muse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import model.IndexBlock;
import model.MasterSecretKey;
import model.OwnerSecretKey;
import model.UserSecretKey;
import muse.util.Util;
import muse.util.publicParameter;

public class MUSEMain {


	private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
	HashMap<String, LinkedList<IndexBlock>> searchTable;

	static DMUABSE dmuabse;
	static MasterSecretKey masterSecretKey;
	static OwnerSecretKey owner1 ;
	static UserSecretKey user1 ;



	public static void main(String[] args) throws IOException {
		DMUABSE musabse = new DMUABSE();
		MasterSecretKey masterSecretKey = musabse.systemSetUp("a.properties");
		OwnerSecretKey owner1 = musabse.generateOwnerKey(masterSecretKey);
		UserSecretKey user1 = musabse.genereteUserKey(masterSecretKey);

		// Update
		long time1 = System.currentTimeMillis();
//		musabse.update(owner1, attibutes, att_number, "1", message1,"hello");
		HashMap<String, BigInteger> randomFiles = Util.getRandomFiles(3);
		for (String fileAddress : randomFiles.keySet()) {
			BigInteger fileMessageBigInteger = randomFiles.get(fileAddress);
			musabse.update(owner1,  fileAddress, fileMessageBigInteger, Util.getRandomKeyword(1));
		}
		long time2 = System.currentTimeMillis();
		System.out.println("Update Finish:" + (time2 - time1));

		// Search
		LinkedList<IndexBlock> searchResult = musabse.search(user1, Util.getRandomKeyword(1));
		long time3 = System.currentTimeMillis();
		System.out.println("Search Finish:返回结果" + searchResult.size() + "，用时：" + (time3 - time2));

		// Decrypt
		musabse.decrypt(user1, searchResult, true);
		long time4 = System.currentTimeMillis();
		System.out.println("Decrypt Finish:" + (time4 - time3));
	}

	/**
	 *
	 * @param fileNumbers 文档数量
	 * @param oWriter 输出文件
	 * @param searchTimes 测试次数
	 * @throws IOException
	 */
	public static void pbcExperiments(int fileNumbers, OutputStreamWriter oWriter, int searchTimes,int additionTimes)
			throws IOException {
		int keywordSize=publicParameter.KEYWORD_SIZE;

		oWriter.append("------Files:" + fileNumbers + " KeyWord:" + keywordSize);


		// BigInteger message1=publicParameter.MESSAGE_1;
		dmuabse = new DMUABSE();
		System.out.println("Setup start--->");
		masterSecretKey = dmuabse.systemSetUp("a.properties");
		 owner1 = dmuabse.generateOwnerKey(masterSecretKey);
		 user1 = dmuabse.genereteUserKey(masterSecretKey);

		updateWithAddition(fileNumbers, oWriter, additionTimes );


	}

	private static void updateWithAddition(int fileNumbers, OutputStreamWriter outputStreamWriter, int additionTimes) throws IOException {
		int k;
		long sum;
		long avg;
		long timeStart,timeEnd;
		int searchTimes=publicParameter.averageSearchTime;
		// 追加更新3
		// Update
		for (int j = 1; j <= additionTimes; j++) {
			int keywordSize=publicParameter.KEYWORD_SIZE;
			outputStreamWriter.append("\r\n------Files:" + fileNumbers * j + " KeyWord:" + keywordSize);
			HashMap<String, BigInteger> randomFiles = Util.getRandomFiles(fileNumbers);
			k = 0;
			System.out.println("Update start--->");
			for (String fileAddress : randomFiles.keySet()) {
				BigInteger fileMessageBigInteger = randomFiles.get(fileAddress);
				dmuabse.update(owner1, fileAddress, fileMessageBigInteger,Util.getRandomKeyword(keywordSize));
				k++;
				if (k % 200 == 0) {
					System.out.println(k + fileNumbers * 2 + "->" + SDF.format(new Date()));
				}
			}
			// Search
			System.out.println("Search start--->");
			sum = 0;
			for (int i = 0; i < searchTimes; i++) {
				System.out.println("========" + (i + 1) + "========");
				timeStart = System.currentTimeMillis();
				LinkedList<IndexBlock> search = dmuabse.search(user1, Util.getRandomKeyword(keywordSize));
				timeEnd = System.currentTimeMillis();
				long searchTime = timeEnd - timeStart;
				System.out.println("Search--->" + searchTime);
				sum += searchTime;
				// Decrypt
				long time3 = System.currentTimeMillis();
				dmuabse.decrypt(user1, search, false);
				long time4 = System.currentTimeMillis();
				System.out.println("Decrypt Finish--->" + (search == null ? 0 : search.size()) + "，用时：" + (time4 - time3));

				outputStreamWriter.append("\r\n Search:" + searchTime);
				outputStreamWriter.append(" Decrypt:" + (time4 - time3));
				outputStreamWriter.append(" 结果:" + (search == null ? 0 : search.size()));
			}
			avg = sum / searchTimes;
			System.out.println("Search Average--->" + avg);
			outputStreamWriter.append("\r\n Search Average:" + avg);
		}

	}


}
