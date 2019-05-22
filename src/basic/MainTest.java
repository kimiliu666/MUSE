package basic;

public class MainTest implements hahaInter {
	public static void main(String[] args) {
		
		Singleton s1 = Singleton.getSingleton();
		Singleton s2 = Singleton.getSingleton();
		Singleton s3 = Singleton.getSingleton();
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);
	}
}
