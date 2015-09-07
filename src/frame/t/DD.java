package frame.t;

import org.apache.log4j.Logger;

public class DD {
	private Logger logger = Logger.getLogger(DD.class.getName());

	public static void main(String[] args) {
		new DD().xx();
	}

	public void xx() {
		System.out.println("***************");
		logger.info("------------info----------");
		logger.error("------------error----------");
		System.out.println("***************");
	}
}
