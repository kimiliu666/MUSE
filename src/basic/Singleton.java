package basic;

public class Singleton {
	private Singleton() {}
	private volatile static Singleton singleton;
	public static class Holder{ 
		public static final Singleton INSTANCE=new Singleton(); 
		}
	
	public static Singleton getSingleton() {
		if (singleton==null) {
			synchronized (Singleton.class) {
				if (singleton==null ) {
					synchronized (Singleton.class) {
						singleton=new Singleton();
					}
				}
				singleton=new Singleton();
			}
		}
		return singleton;
	}
	
	public static Singleton getSingleton2() {
		return Holder.INSTANCE;
	}
	
	
}
