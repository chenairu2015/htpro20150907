import java.text.MessageFormat;

public class De {
	public static void main22(String[] args) {
		String sqlModel = "INSERT INTO bim_links (innerid,"
				+ "classid,  frominnerid,  fromclassid,  toinnerid,  toclassid,  rootinnerid,"
				+ "rootclassid)VALUES(''{0}'',''bim_links'',''{1}'',''bim_menus'',''{2}'',''bim_menus'',''{1}'',''bim_menus'')"
				+ ";";
		String[] p = new String[] {"9","9","9","13","13","13","13","13"};
		String[] c = new String[] {"10","11","12",   "14","15","16","17","18"};
		for (int i = 0; i < p.length; i++) {
			System.out.println(MessageFormat.format(sqlModel, "bim_menus" + i,
					p[i], c[i]));
		}
	}
	public static void main(String[] args) {
		String sqlModel = "insert into bim_links"
				+ "(innerId,       classId,     fromInnerId,fromClassId,   toInnerId,  toClassId,    rootInnerId,rootClassId,flag)"
				+"values(''{0}'',''bim_links'',''{1}'',     ''bim_menus'', ''{2}'',    ''bim_menus'',''{3}'',''bim_menus'',''true'')";
		for (int i = 1; i < 20; i++) {
			if(i<10||i==13||i==19){
				System.out.println(MessageFormat.format(sqlModel, "bim_menus" + i ,0,i,0)
						+";");
			}else if(i==10|| i==11||i==12){
				System.out.println(MessageFormat.format(sqlModel, "bim_menus" + i ,9,i,0)
						+";");
			}else {
				System.out.println(MessageFormat.format(sqlModel, "bim_menus" + i ,13,i,0)
						+";");
			}
		}
	}
}
