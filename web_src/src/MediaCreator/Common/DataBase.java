package MediaCreator.Common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class DataBase extends DataBaseCore{

	private static Logger logger = LogFactory.getLogger();

	private static String DBNAME_RIS = "java:comp/env/jdbc/oracle/rris";
	private static String DBNAME_UNITY = "java:comp/env/jdbc/oracle/unity";
	private static String DBNAME_ARQS = "java:comp/env/jdbc/oracle/arqs";

	private static String UNITYTYPE_LOCAL = "LOCAL";
	private static String UNITYTYPE_SERV_NML = "SERV_NML";
	private static String UNITYTYPE_SERV_EMG = "SERV_EMG";

	public static Connection getRisConnection(){
		return getConnection(DBNAME_RIS);
	}


	public static Connection getUnityConnection() throws Exception {

		String dbname = DBNAME_UNITY;

		Connection conn = null;

		//No27 接続Close漏れ
		try{
			conn = getRisConnection();

			DataTable Dat = getCurrentServerType(conn);

			if(Dat.getRowCount() > 0){
				String servertype = Dat.getRows().get(0).get("CURRENTTYPE").toString();

				logger.debug("CURRENTTYPE = " + servertype );

				if(servertype.equals(UNITYTYPE_SERV_NML)) {
					dbname = DBNAME_UNITY;
				}
				else if(servertype.equals(UNITYTYPE_SERV_EMG)){
					dbname = DBNAME_RIS;
				}
				else{
					dbname = DBNAME_RIS;
				}
			}
		}
		finally{
			closeConnection(conn);
		}
		return getConnection(dbname);
	}


	public static Connection getArqsConnection(){
		return getConnection(DBNAME_ARQS);
	}


	public static void closeConnection(Connection conn){

		try{
			conn.close();
		}
		catch(Exception e){
			//NULL;
		}

	}


	public static Timestamp getSysdate(Connection conn) throws Exception {

		DataTable Dat = new DataTable();


		Timestamp ret = null;

		String sql = "SELECT SYSDATE from dual";

		Object[] args = new Object[0];
		Dat = executeQuery(sql,args,conn);

		if(Dat.getRowCount() > 0){
			ret = (Timestamp)Dat.getRows().get(0).get("SYSDATE");
		}

		return ret;

	}


    public static DataTable getCurrentServerType(Connection conn) throws Exception{

    	DataTable Dat = new DataTable();
		ArrayList<Object> arglist = new ArrayList<Object>();

        String sql = "";
        String where = "";

        sql += "SELECT a.* ";
        sql += "  FROM currentservertype a ";

        sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

        return Dat;
    }

	public static DataTable getUserManage(String userid, String password, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.*, ";
		sql += "       b.licencetouse, ";
		sql += "       b.appcode ";
		sql += "  FROM usermanage a, ";
		sql += "       userappmanage b ";

		where += " WHERE a.hospitalid = 'HID' ";
		where += "   AND NVL(a.passwordexpirydate , SYSDATE + 1) >= SYSDATE ";
		where += "   AND a.userid = b.userid(+)";
		where += "   AND a.hospitalid = b.hospitalid(+)";
		where += "   AND b.appcode(+) = 'RIS' ";              //RIS
		where += "   AND b.licencetouse(+) != '0' ";          //使用不可以外

		where += "   AND a.userid = ? ";
		arglist.add(userid);

		if(password != null){
			where += "   AND a.password = MD5_DIGEST(?) ";
			arglist.add(password);
		}

		sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static DataTable getJisisya(String jisisyaid,Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM usermanage a ";

		where += " WHERE a.hospitalid = 'HID' ";
		where += "   AND a.userid = ? ";

		arglist.add(jisisyaid);

		sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static DataTable getPatientInfo(String patientid, Connection conn)  throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " SELECT a.*, ";
		sql += "        TRUNC((TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd')) - birthday)/10000,0) AS age,";
		sql += "        NULL AS nengo_char, ";
		sql += "        NULL AS nengo_year, ";
		sql += "        b.byoutou_name, ";
		sql += "        c.byousitu_name ";
		sql += "   FROM patientinfo a, ";
		sql += "        byoutoumaster b, ";
		sql += "        byousitumaster c ";

		where += " WHERE a.byoutou_id = b.byoutou_id(+) ";
		where += "   AND a.byousitu_id = c.byousitu_id(+) ";

		where += "   AND a.kanja_id = ? ";
		arglist.add(patientid);

		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);


		if(Dat.getRowCount() > 0) {
			if(Dat.getRows().get(0).get("BIRTHDAY").toString() != "") {
				DataTable jeraDat = getJeraBirthday(Long.parseLong(Dat.getRows().get(0).get("BIRTHDAY").toString()), conn);

				if(jeraDat.getRowCount() > 0) {
					Dat.getRows().get(0).put("NENGO_CHAR",jeraDat.getRows().get(0).get("JERA"));
					Dat.getRows().get(0).put("NENGO_YEAR",jeraDat.getRows().get(0).get("YEAR"));
				}

			}
		}

		return Dat;
	}


	public static DataTable getJeraBirthday(Timestamp birthday, Connection conn) throws Exception {

		String date = Common.toFormatString(birthday, "yyyyMMdd");

		return getJeraBirthday(Long.parseLong(date),conn);

	}

	public static DataTable getJeraBirthday(long birthday, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();


		sql += "SELECT a.*, ";
		sql += "       TO_NUMBER(TO_CHAR(TO_DATE('" + String.valueOf(birthday) + "','yyyymmdd'),'yyyy')) - TO_NUMBER(TO_CHAR(firstdate,'yyyy')) + 1 AS year";
		sql += "  FROM (SELECT * ";
		sql += "          FROM jeramaster ";
		sql += "         WHERE firstdate = (SELECT MAX(firstdate) FROM jeramaster WHERE firstdate <= TO_DATE('" + String.valueOf(birthday) + "','yyyymmdd')) ";
		sql += "       ) a ";

		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}


	public static DataTable getKensakikiMaster(String kensakikiid, Connection conn) throws Exception {

		String[] kikilist = new String[0];

		if(kensakikiid != null){
			kikilist = kensakikiid.split(",");
		}

		return getKensakikiMaster(kikilist, conn);
	}

	public static DataTable getKensakikiMaster(String[] kensakikiid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM kensakikimaster a ";

		where += "   WHERE a.useflag = 1 ";


		if(kensakikiid != null && kensakikiid.length > 0) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.kensakiki_id in (";

			String buff = "";
			for(int i = 0; i < kensakikiid.length; i++){
				if(!buff.equals("")){
					buff += ",";
				}
				buff += " ?";
				arglist.add(kensakikiid[i]);
			}

			where += buff + " )";
		}

		sql += where;

		sql += "ORDER BY a.showorder,a.kensakiki_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}


	public static DataTable getBuiMaster(String buiid, Connection conn) throws Exception {

		String[] builist = new String[0];

		if(buiid != null){
			builist = buiid.split(",");
		}

		return getBuiMaster(builist, conn);
	}


	public static DataTable getBuiMaster(String[] buiid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM buimaster a ";

		where += "   WHERE a.useflag = 1 ";


		if(buiid != null && buiid.length > 0) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.bui_id in (";

			String buff = "";
			for(int i = 0; i < buiid.length; i++){
				if(!buff.equals("")){
					buff += ",";
				}
				buff += " ?";
				arglist.add(buiid[i]);
			}

			where += buff + " )";
		}

		sql += where;

		sql += "ORDER BY a.showorder,a.bui_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static DataTable getSectionMaster(String doctorid, String sectionid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM sectionmaster a ";

		where += "   WHERE a.useflag = 1 ";


		if(doctorid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     EXISTS (SELECT * ";
			where += "               FROM sectiondoctormaster x ";
			where += "              WHERE x.doctor_id = ? ";
			where += "                AND useflag = '1' ";
			where += "                AND INSTR(',' || TRIM(x.tanto_section_id) || ',' , ',' || a.section_id || ',') > 0 ";
			where += "            ) ";
			arglist.add(doctorid);
		}


		if(sectionid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.section_id = ? ";
			arglist.add(sectionid);
		}

		sql += where;

		sql += "ORDER BY a.showorder,a.section_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}


	public static DataTable getKensaTypeMaster(String kensatypeid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM kensatypemaster a ";

		where += "   WHERE a.useflag = 1 ";


		if(kensatypeid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.kensatype_id = ? ";
			arglist.add(kensatypeid);
		}

		sql += where;

		sql += "ORDER BY a.showorder,a.kensatype_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}


	public static DataTable getSectionDoctorMaster(String sectionid, String doctorid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM sectiondoctormaster a ";

		where += "   WHERE a.useflag = 1 ";


		if(sectionid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.section_id = ? ";
			arglist.add(sectionid);
		}

		if(doctorid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.doctor_id = ? ";
			arglist.add(doctorid);
		}

		sql += where;

		sql += "ORDER BY a.showorder,a.doctor_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static DataTable getModalityList( Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT * ";
		sql += "  FROM (SELECT modality, ";
		sql += "               MIN(showorder) AS showorder ";
		sql += "          FROM kensatypemaster ";
		sql += "         WHERE useflag = '1' ";
		sql += "           AND modality IS NOT NULL ";
		sql += "        GROUP BY modality ";
		sql += "       ) a ";

		sql += where;

		sql += "ORDER BY a.modality, a.showorder ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static DataTable getSystemDefine( Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT * ";
		sql += "  FROM systemdefine ";

		sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	public static String createRisId(Connection conn) throws Exception {

		String ret = null;

		long seq = getSequenceValue("RisIdSequence",conn);

		Timestamp sysdate = getSysdate(conn);

		ret = new SimpleDateFormat("yyyyMMdd").format(sysdate);
		ret += String.valueOf(seq);

		return ret;
	}


	public static String createOrderNo(Connection conn) throws Exception {

		String ret = null;

		long seq = getSequenceValue("RisOrderSequence",conn);

		ret = String.valueOf(seq);

		return ret;
	}

	public static String createSutudyInstanceUid(String risid, String studyInstanceUid, Connection conn) throws Exception {

		String ret = null;

		DataTable Dat = getSystemDefine(conn);

		//"1.2.392.200045.6960.4.7.nnnnnn.ris_id"
		//ret = "1.2.392.200045.6960.4.7.";
		ret = studyInstanceUid;

		ret += ".";
		ret += Dat.getRows().get(0).get("LICENSENO").toString();

		ret += ".";
		ret += risid;

		return ret;
	}


	public static String createToCodonicsRequestId(Connection conn) throws Exception {

		String ret = null;

		long seq = getSequenceValue("ToCodonicsSequence",conn);

		ret = String.valueOf(seq);

		return ret;
	}


	private static long getSequenceValue(String sequensename, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT " + sequensename + ".nextval AS nextval ";
		sql += "  FROM dual ";

		sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);


		return Long.parseLong(Dat.getRows().get(0).get("NEXTVAL").toString());

	}


	public static DataTable getToCodonicsInfo(String requestid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM tocodonicsinfo a ";


		where += " WHERE requestid = ? ";
		arglist.add(requestid);

		sql += where;

		sql += " ORDER BY requestid ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;
	}


	public static DataTable getCodonicsMediaOrderTable(String risid, String patientid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.*, ";
		sql += "       b.doctor_name, ";
		sql += "       c.section_name, ";

		//sql += "       e.kensakiki_name, ";
		sql += "       NVL(e.kensakiki_name,l.kensakiki_name) AS kensakiki_name, ";		//MCHT_40-J-T33 BackLog No2 作成装置が表示されない

		sql += "       f.bui_name, ";

		//社内テストNo6 依頼ファイル作成失敗時のTransferStatus
		//失敗時はTransferStatuが"02"になる。
		//sql += "       CASE WHEN g.orderno IS NULL THEN '作成中' ELSE '完了' END AS sintyoku, ";
		//MCHT-04J-T33対応(AOC対応) 2014/09/22 S.Terakata(STI)
		//失敗時もファイル取り込むので条件変える。
		//sql += "       CASE WHEN g.orderno IS NULL THEN ";
		//sql += "                 CASE WHEN h.transferstatus = '02' THEN '失敗' ";	//jspで文字を見てるので変更する時は注意
		//sql += "                      ELSE '作成中' ";
		//sql += "                 END ";
		//sql += "            ELSE '完了' ";
		//sql += "       END AS sintyoku, ";
		sql += "       CASE WHEN g.orderno IS NULL THEN ";	//結果ファイルがない
		sql += "                 CASE WHEN h.transferstatus = '02' THEN '失敗' ";	//jspで文字を見てるので変更する時は注意
		sql += "                      ELSE '作成中' ";
		sql += "                 END ";
		sql += "            ELSE  ";						//結果ファイルがある(エラーでも取り込む)
		sql += "                 CASE WHEN h.transferstatus = '01' THEN '完了' ";	//jspで文字を見てるので変更する時は注意
		sql += "                      ELSE '失敗' ";
		sql += "                 END ";
		sql += "       END AS sintyoku, ";

		//sql += "       d.kanjisimei ";
		sql += "       NVL(d.kanjisimei,j.kanjisimei) AS kanjisimei ";	//MCHT_40-J-T33 BackLog No1 患者氏名が表示されない

		sql += "  FROM codonicsmediaordertable a, ";
		sql += "       sectiondoctormaster b, ";
		sql += "       sectionmaster c, ";
		sql += "       patientresultsinfo d, ";
		sql += "       kensakikimaster e, ";
		sql += "       buimaster f, ";
		sql += "       (SELECT DISTINCT orderno FROM codonicsmediacompletetable ) g, ";
		sql += "       tocodonicsinfo h, ";
		sql += "       exmaintable i, ";
		sql += "       patientinfo j, ";		//MCHT_40-J-T33 BackLog No1 患者氏名が表示されない
		sql += "       ordermaintable k, ";		//MCHT_40-J-T33 BackLog No2 作成装置が表示されない
		sql += "       kensakikimaster l ";		//MCHT_40-J-T33 BackLog No2 作成装置が表示されない
		where += " WHERE a.irai_doctor_id = b.doctor_id(+) ";
		where += "   AND a.irai_section_id = c.section_id(+) ";
		where += "   AND a.ris_id = d.ris_id(+) ";
		//where += "   AND a.kensakiki = e.kensakiki_id(+) ";
		where += "   AND a.order_bui_id = f.bui_id(+) ";
		where += "   AND a.orderno = g.orderno(+) ";
		where += "   AND a.ris_id = h.ris_id(+) ";		//同じRIS_IDで複数の作成依頼は出来ないはず。
		where += "   AND a.ris_id = i.ris_id(+) ";				//社内テストNo7 CodonicsMediaOrderTableのKensaKikiの値
		where += "   AND i.kensakiki_id = e.kensakiki_id(+) ";	//社内テストNo7 CodonicsMediaOrderTableのKensaKikiの値

		where += "   AND a.kanja_id = j.kanja_id(+) ";			//MCHT_40-J-T33 BackLog No1 患者氏名が表示されない
		where += "   AND a.ris_id = k.ris_id(+) ";				//MCHT_40-J-T33 BackLog No2 作成装置が表示されない
		where += "   AND k.kensakiki_id =l.kensakiki_id(+) ";	//MCHT_40-J-T33 BackLog No2 作成装置が表示されない



		if(patientid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.kanja_id = ? ";
			arglist.add(patientid);
		}

		if(risid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.ris_id = ? ";
			arglist.add(risid);
		}

		sql += where;

		sql += " ORDER BY a.ris_id ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;
	}

	public static DataTable getCodonicsMediaOrderStudyTable(String risid, String no, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM codonicsmediaorderstudytable a ";


		if(risid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.ris_id = ? ";
			arglist.add(risid);
		}

		if(no != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.no = ? ";
			arglist.add(no);
		}

		sql += where;

		sql += " ORDER BY ris_id, no ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;
	}


	public static DataTable getCodonicsMediaCompleteTable(String orderno, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM codonicsmediacompletetable a ";



		if(orderno != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.orderno = ? ";
			arglist.add(orderno);
		}


		sql += where;

		sql += " ORDER BY a.orderno, a.diskid ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;
	}

	public static DataTable getZoneCode(Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		//業務区分を取得する。
		sql += "SELECT b.today, ";
		sql += "       CASE WHEN c.hizuke IS NOT NULL THEN '4' ";		//休日の場合は'4':緊急
		sql += "            WHEN a.zone1_time  <= b.timenum AND b.timenum < NVL(a.zone2_time ,2400) THEN zone1_code ";
		sql += "            WHEN a.zone2_time  <= b.timenum AND b.timenum < NVL(a.zone3_time ,2400) THEN zone2_code ";
		sql += "            WHEN a.zone3_time  <= b.timenum AND b.timenum < NVL(a.zone4_time ,2400) THEN zone3_code ";
		sql += "            WHEN a.zone4_time  <= b.timenum AND b.timenum < NVL(a.zone5_time ,2400) THEN zone4_code ";
		sql += "            WHEN a.zone5_time  <= b.timenum AND b.timenum < NVL(a.zone6_time ,2400) THEN zone5_code ";
		sql += "            WHEN a.zone6_time  <= b.timenum AND b.timenum < NVL(a.zone7_time ,2400) THEN zone6_code ";
		sql += "            WHEN a.zone7_time  <= b.timenum AND b.timenum < NVL(a.zone8_time ,2400) THEN zone7_code ";
		sql += "            WHEN a.zone8_time  <= b.timenum AND b.timenum < NVL(a.zone9_time ,2400) THEN zone8_code ";
		sql += "            WHEN a.zone9_time  <= b.timenum AND b.timenum < NVL(a.zone10_time,2400) THEN zone9_code ";
		sql += "            WHEN a.zone10_time <= b.timenum AND b.timenum <                   2400  THEN zone10_code ";
		sql += "       END AS zone_code ";
		sql += "  FROM timezonetable a, ";
		sql += "       (SELECT b.today, ";				//週と曜日でその日の診療日区分を取得する
		sql += "               TO_NUMBER(TO_CHAR(SYSDATE,'HH24MI')) AS timenum, ";
		sql += "               CASE WHEN b.weeknum = 1 THEN a.week01 ";
		sql += "                    WHEN b.weeknum = 2 THEN a.week02 ";
		sql += "                    WHEN b.weeknum = 3 THEN a.week03 ";
		sql += "                    WHEN b.weeknum = 4 THEN a.week04 ";
		sql += "                    WHEN b.weeknum = 5 THEN a.week05 ";
		sql += "                    WHEN b.weeknum = 6 THEN a.week06 ";
		sql += "                    ELSE NULL ";
		sql += "               END AS dateclassification ";
		sql += "          FROM dayclassificationtable a, ";
		sql += "               (SELECT today, ";		//第何週の何曜日か取得する
		sql += "                       ((today - firstday) / 7) + 1 AS weeknum, ";
		sql += "                       TO_CHAR(today,'D') - 1 AS dayofweek, ";
		sql += "                       TO_CHAR(today,'DY') AS dayname ";
		sql += "                  FROM (SELECT TRUNC(SYSDATE) AS today, ";
		//sql += "                               NEXT_DAY(TRUNC(SYSDATE,'MM'),TO_NUMBER(TO_CHAR(SYSDATE ,'D'))) AS firstday ";
		sql += "                               NEXT_DAY(TRUNC(SYSDATE,'MM') - 1,TO_NUMBER(TO_CHAR(SYSDATE ,'D'))) AS firstday ";	//No28 業務時間のチェックが効かない
		sql += "                          FROM dual ";
		sql += "                       ) ";
		sql += "               ) b ";
		sql += "         WHERE a.dayofweek = b.dayofweek ";
		sql += "       ) b, ";
		sql += "       calendarmaster c ";
		sql += " WHERE a.dateclassification = b.dateclassification ";
		sql += "   AND TO_CHAR(b.today,'YYYY/MM/DD') = c.hizuke(+) ";


		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;
	}

	//ARQS
	public static DataTable getMasterStudy(	String acno,
											String patientid,
											String modality,
											String studyInstanceUid,
											String seriseInstanceUid,// HUFULL_90-J-T31-008対応 ADD
											Timestamp dateFrom,
											Timestamp dateTo,
											String[] excludeModality,	//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
											String[] otherimport,	// 他院画像取込 2017/04/03 S.Ichinose(Cosmo)
											String[] exclusionSeries,	// HUFULL_90-J-T31-008対応 ADD
											Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.*, ";
		//HUFULL_90-J-T31-008対応 ADD START
		sql += "      b.SERIESINSTANCEUID,b.SERIESNUMBER,b.SERIESDESCRIPTION,b.PROTOCOLNAME,b.SERIMAGESNUM, ";
		//HUFULL_90-J-T31-008対応 ADD END

		//HUFULL_90-J-T31-008対応 media_bikou復活 MOD START
		//MCHT-04J-T33対応(TWMU特注機能のため削除) 2014/09/24 S.Terakata(STI)
		//sql += "       CASE WHEN institutionaddress = 'OTHERINSTITUTE' THEN '他院画像'  ELSE NULL END AS media_bikou ";
		//sql += "       NULL AS media_bikou ";	//カラムだけ残す。
		sql += "       CASE WHEN a.institutionaddress = 'OTHERINSTITUTE' THEN '他院画像'  ELSE NULL END AS media_bikou ";

		//HUFULL_90-J-T31-008対応 MOD END

		sql += "  FROM masterstudy a ";
		//HUFULL_90-J-T31-008対応 ADD START
		sql += "  INNER JOIN MASTERSERIES b ";
		sql += "    ON a.STUDYINSTANCEUID = b.STUDYINSTANCEUID ";
		//HUFULL_90-J-T31-008対応 ADD END



		if(acno != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.accessionnumber = ? ";
			arglist.add(acno);
		}

		if(patientid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.patientid = ? ";
			arglist.add(patientid);
		}

		if(modality != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.modality = ? ";
			arglist.add(modality);
		}

		if(studyInstanceUid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.studyinstanceuid = ? ";
			arglist.add(studyInstanceUid);
		}
		//HUFULL_90-J-T31-008対応 ADD START

		if(seriseInstanceUid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     b.SERIESINSTANCEUID = ? ";
			arglist.add(seriseInstanceUid);
		}
		//HUFULL_90-J-T31-008対応 ADD END

		if(dateFrom != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     TRUNC(a.studydate) >= ? ";
			arglist.add(dateFrom);
		}

		if(dateTo != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     TRUNC(a.studydate) <= ? ";
			arglist.add(dateTo);
		}

		//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
		if(excludeModality != null){
			for (int i = 0; i < excludeModality.length; i++) {
				if (i == 0) {
					if (where == "") {
						where += " WHERE ";
					}
					else {
						where += " AND ";
					}
						where += " a.modality NOT IN (";
				}

				where += " ? ";
				arglist.add(excludeModality[i]);

				if (i == excludeModality.length - 1) {
					//最後
					where += " ) ";
				}
				else {
					where += " , ";
				}
			}
		}

		// 他院画像取込 2017/04/03 S.Ichinose(Cosmo)
		if(otherimport != null){
			for (int i = 0; i < otherimport.length; i++) {
				if (where == "") {
					where += " WHERE ";
				}
				else {
					where += " AND ";
				}

				where += " NVL(a.studydescription, ' ') NOT LIKE ? ";
				arglist.add(otherimport[i] + "%");
			}
		}
		//HUFULL_90-J-T31-008対応 ADD START
		if(exclusionSeries != null){
			for (int i = 0; i < exclusionSeries.length; i++) {
				if (i == 0) {
					if (where == "") {
						where += " WHERE ";
					}
					else {
						where += " AND ";
					}
						where += " b.SERVERAETITLE NOT IN (";
				}

				where += " ? ";
				arglist.add(exclusionSeries[i]);

				if (i == exclusionSeries.length - 1) {
					//最後
					where += " ) ";
				}
				else {
					where += " , ";
				}
			}
		}
		//HUFULL_90-J-T31-008対応 ADD END

		sql += where;

		//No25 検査一覧のソート順
		//sql += "ORDER BY a.studyid,a.studydate,a.studytime ";
		sql += "ORDER BY a.studydate desc ,a.studytime desc ";
		//HUFULL_90-J-T31-008対応 ADD START
		sql += ",a.STUDYINSTANCEUID asc ,b.SERIESINSTANCEUID asc ,b.SERIESNUMBER asc ";
		//HUFULL_90-J-T31-008対応 ADD END


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	//ARQS
	public static DataTable getUserManageVins(	String userid, Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM usermanageforvins a ";


		if(userid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.userid = ? ";
			arglist.add(userid);
		}

		sql += where;

		sql += "ORDER BY a.userid ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	//ARQS
	public static DataTable getModalityListFromArqs(String patientid,
													String[] excludeModality,	//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
													Connection conn) throws Exception {

		DataTable Dat = new DataTable();
		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT DISTINCT modality ";
		sql += "  FROM masterstudy a ";

		if(patientid != null) {
			if(where == "") {
				where += " WHERE ";
			}
			else {
				where += " AND ";
			}
			where += "     a.patientid = ? ";
			arglist.add(patientid);
		}

		//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
		if(excludeModality != null){
			for (int i = 0; i < excludeModality.length; i++) {
				if (i == 0) {
					if (where == "") {
						where += " WHERE ";
					}
					else {
						where += " AND ";
					}
						where += " a.modality NOT IN (";
				}

				where += " ? ";
				arglist.add(excludeModality[i]);

				if (i == excludeModality.length - 1) {
					//最後
					where += " ) ";
				}
				else {
					where += " , ";
				}
			}
		}

		sql += where;

		sql += "ORDER BY a.modality ";

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}

	//監査証跡登録
	public static void insertAuditTrail(String unitutype,
										Timestamp eventdate,
										String eventname,
										DataRow patientRow,
										DataRow loginRow,
										String iparrd,
										Connection conn
										) throws Exception  {

		String sql = "";
		//String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		String tablename = "audittrail";
		if(unitutype.equals(UNITYTYPE_SERV_NML)) {
			tablename = "arqs.audittrail";
		}

		sql += "INSERT INTO " + tablename + " ( ";
		sql += "            eventdatetime, ";
		sql += "            eventname, ";
		sql += "            hospitalid, ";
		sql += "            hostuserid, ";
		sql += "            hostusername, ";
		sql += "            hostipaddress, ";
		sql += "            patientid, ";
		sql += "            patientname, ";
		sql += "            studydate, ";
		sql += "            modality, ";
		sql += "            result, ";
		sql += "            additionalinfo1, ";
		sql += "            appcode ";
		sql += "            ) ";
		sql += "     VALUES ( ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            ?, ";
		sql += "            TO_DATE(?,'yyyymmdd'), ";
		sql += "            ?, ";
		sql += "            0, ";
		sql += "            ?, ";
		sql += "            ?  ";
		sql += "            ) ";




		String hospitalid = "HID";
		String patientid = "";
		String patientname = "";
		String modality = "";
		String studydate = "";
		String additional1 = "";

		if(patientRow != null) {
			patientid = patientRow.get("KANJA_ID").toString();
			patientname = patientRow.get("KANJISIMEI").toString();
			//modality = orderRow.get("KENSATYPE_NAME").toString();
			//studydate = orderRow.get("EX_KENSA_DATE").toString();
			//additional1 = "ORDER=" + orderRow.get("ORDERNO").toString();
		}

		arglist.add(eventdate);
		arglist.add(eventname);
		arglist.add(hospitalid);
		arglist.add(loginRow.get("USERID").toString());
		arglist.add(loginRow.get("USERNAME").toString());
		arglist.add(iparrd);
		arglist.add(patientid);
		arglist.add(patientname);

		arglist.add(studydate);

		arglist.add(modality);
		arglist.add(additional1);
		arglist.add("MediaCreator");

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);
		executeSQL(sql,args,conn);
	}

	// ↓↓↓ オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)
	public static DataTable getUserInfo_CA(String id,Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += "SELECT a.* ";
		sql += "  FROM userinfo_ca a ";

		where += " WHERE a.loginid = ? ";

		arglist.add(id);

		sql += where;


		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql,args,conn);

		return Dat;

	}
	// ↑↑↑ オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)
}
