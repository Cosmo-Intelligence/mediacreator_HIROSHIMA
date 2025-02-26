package MediaCreator.Common;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Config implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogFactory.getLogger();


	private static final String FILE_NAME = "/WEB-INF/MediaCreator.config.xml";


	private String kensatypeid = null;
	private String sakuseiKbn = null;
	private String sakuseiSouti = null;
	private int patientLength = 8;
	private String viewCUrl = null;
	private boolean checkWorkTime = true;

	private String studyInstanceUid = null;

	private String informationSection = null;

	private String columnOrder = null;

	private String receiptSheetJspName = null;

	private String mediaType = null;

	private String kensasituID = null;

	private String jisisyaID = null;

	private String kaikeiDialog = null;

	//MCHT-04J-T33対応(未受けモード対応) 2014/09/22 S.Terakata(STI)
	private String statusMode = null;

	//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
	private String excludeModality = null;

	// オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)
	private String keepLogonGroup = null;

	// 他院画像取込対応 2017/04/03 S.Ichinose(Cosmo)
	private String otherImport = null;

	// 2019/12/24 Add START @COSMO
	// 「HUFULL_90-J-T31-008[MED-002]画像選択エリアの表示対象外とするServerAETitleを指定する」対応
	private String exclusionSeries = null;

	// 「HUFULL_90-J-T31-008[MED-002]他院取り込み画像かどうかを表示する」対応
	private String otherInstitusion = "★";
	// 2019/12/24 Add START @COSMO

	//「備考欄へのコメント格納方法変更」対応
	private String ChangeUnit = null;
	// 2020/3/10 Add START @COSMO

	private Config(){
	}


	public String getRecelptSheetJspName() {
		return receiptSheetJspName;
	}

	public void setRecelptSheetJspName(String receiptSheetJspName) {
		this.receiptSheetJspName = receiptSheetJspName;
	}

	public String getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(String columnOrder) {
		this.columnOrder = columnOrder;
	}

	public String getInformationSection() {
		return informationSection;
	}

	public void setInformationSection(String informationSection) {
		this.informationSection = informationSection;
	}



	public String getKensatypeid() {
		return kensatypeid;
	}

	public void setKensatypeid(String kensatypeid) {
		this.kensatypeid = kensatypeid;
	}

	public String getSakuseiKbn() {
		return sakuseiKbn;
	}

	public void setSakuseiKbn(String sakuseiKbn) {
		this.sakuseiKbn = sakuseiKbn;
	}

	public String getSakuseiSouti() {
		return sakuseiSouti;
	}

	public void setSakuseiSouti(String sakuseiSouti) {
		this.sakuseiSouti = sakuseiSouti;
	}

	public int getPatientLength() {
		return patientLength;
	}

	public void setPatientLength(int patientLength) {
		this.patientLength = patientLength;
	}

	public String getViewCUrl() {
		return viewCUrl;
	}

	public void setViewCUrl(String viewCUrl) {
		this.viewCUrl = viewCUrl;
	}

	public boolean isCheckWorkTime() {
		return checkWorkTime;
	}

	public void setCheckWorkTime(boolean checkWorkTime) {
		this.checkWorkTime = checkWorkTime;
	}

	public static Config getConfig(ServletContext ctx){

		Config config = loadConfig(ctx);

		return config;
	}

	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}

	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
	}


	private static Config loadConfig(ServletContext ctx){

		Config config = null;

		try {

			URL url = ctx.getResource(FILE_NAME);
			logger.debug(FILE_NAME + " = " + url);

			InputStream stream = ctx.getResourceAsStream(FILE_NAME);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(stream);
			Element root = doc.getDocumentElement();

			HashMap<String,String> map = new HashMap<String,String>();
			for(int i = 0; i < root.getChildNodes().getLength(); i++){
				//logger.debug(root.getChildNodes().item(i).getNodeName() + ":" + root.getChildNodes().item(i).getTextContent());
				map.put(root.getChildNodes().item(i).getNodeName(), Common.toNullString(root.getChildNodes().item(i).getTextContent()));
			}

			config = new Config();

			config.setKensatypeid(map.get("KensaTypeId"));
			config.setSakuseiKbn(map.get("BuiId"));
			config.setSakuseiSouti(map.get("KensaKikiId"));
			config.setPatientLength(Integer.parseInt(map.get("PatientLength")));
			config.setViewCUrl(map.get("ViewCURL"));
			config.setStudyInstanceUid(map.get("StudyInstanceUID"));

			String flg = map.get("CheckWorkTime");
			if(flg.equals("Y")){
				config.setCheckWorkTime(true);
			}
			else{
				config.setCheckWorkTime(false);
			}

			config.setInformationSection(map.get("InformationSection"));
			config.setColumnOrder(map.get("ColumnOrder"));
			config.setRecelptSheetJspName(map.get("ReceiptSheetJSP"));

			config.setMediaType(map.get("MediaType"));

			config.setKensasituID(map.get("KensasituID"));

			config.setJisisyaID(map.get("JisisyaID"));

			//MIS受入テストNo43
			config.setKaikeiDialog(map.get("KaikeiDialog"));


			//MCHT-04J-T33対応(未受けモード対応) 2014/09/22 S.Terakata(STI)
			config.setStatusMode(map.get("StatusMode"));

			//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
			config.setExcludeModality(map.get("ExcludeModality"));

			// オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)
			config.setKeepLogonGroup(map.get("KeepLogonGroup"));

			// 他院画像対応 2017/04/03 S.Ichinose(Cosmo)
			config.setOtherImport(map.get("OtherImport"));

			// 画像選択エリアの表示対象外対応 2019/12/24 @Cosmo
			//もし空でない場合にのみセットする。
			if(!"".equals(map.get("ExclusionSeries"))){
				config.setExclusionSeries(map.get("ExclusionSeries"));
			}
			// 他院対応 2019/12/24 @Cosmo
			config.setOtherInstitusion(map.get("OtherInstitusion"));

			//「備考欄へのコメント格納方法変更」対応
			if(map.get("ChangeUnit") == null) {
				config.setChangeUnit("");
			}else{
				config.setChangeUnit(map.get("ChangeUnit"));
			}
			// 2020/3/10 Add START @COSMO
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			config = null;
		}

		return config;
	}


	public String getMediaType() {
		return mediaType;
	}


	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}


	public String getKensasituID() {
		return kensasituID;
	}


	public void setKensasituID(String kensasituID) {
		this.kensasituID = kensasituID;
	}


	public String getJisisyaID() {
		return jisisyaID;
	}


	public void setJisisyaID(String jisisyaID) {
		this.jisisyaID = jisisyaID;
	}


	public String getKaikeiDialog() {
		return kaikeiDialog;
	}


	public void setKaikeiDialog(String kaikeiDialog) {
		this.kaikeiDialog = kaikeiDialog;
	}


	//MCHT-04J-T33対応(未受けモード対応) 2014/09/22 S.Terakata(STI)
	/**
	 * @return statusMode
	 */
	public String getStatusMode() {
		return statusMode;
	}


	//MCHT-04J-T33対応(未受けモード対応) 2014/09/22 S.Terakata(STI)
	/**
	 * @param statusMode セットする statusMode
	 */
	public void setStatusMode(String statusMode) {
		this.statusMode = statusMode;
	}


	//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
	/**
	 * @return excludeModality
	 */
	public String getExcludeModality() {
		return excludeModality;
	}


	//MCHT-04J-T33対応(非表示モダリティ) 2014/09/22 S.Terakata(STI)
	/**
	 * @param excludeModality セットする excludeModality
	 */
	public void setExcludeModality(String excludeModality) {
		this.excludeModality = excludeModality;
	}

	// ↓↓↓ オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)
	/**
	 * @return keepLogonGroup
	 */
	public String getKeepLogonGroup() {
		return keepLogonGroup;
	}

	/**
	 * @param keepLogonGroup セットする keepLogonGroup
	 */
	public void setKeepLogonGroup(String keepLogonGroup) {
		this.keepLogonGroup = keepLogonGroup;
	}
	// ↑↑↑ オートログオフ対応 2016/10/20 S.Ichinose(Cosmo)

	// ↓↓↓ 他院画像対応 2017/04/03 S.Ichinose(Cosmo)
	public String getOtherImport() {
		return otherImport;
	}


	public void setOtherImport(String otherImport) {
		this.otherImport = otherImport;
	}
	// ↑↑↑ 他院画像対応 2017/04/03 S.Ichinose(Cosmo)


	// 2019/12/24 Add START @COSMO
	public String getExclusionSeries() {
		return exclusionSeries;
	}


	public void setExclusionSeries(String exclusionSeries) {
		this.exclusionSeries = exclusionSeries;
	}


	public String getOtherInstitusion() {
		return otherInstitusion;
	}


	public void setOtherInstitusion(String otherInstitusion) {
		if(0 < otherInstitusion.length()){
			this.otherInstitusion = otherInstitusion;
		}
	}
	// 2019/12/24 Add End @COSMO

	public String getChangeUnit() {
		return ChangeUnit;
	}

	public void setChangeUnit(String changeUnit) {
		ChangeUnit = changeUnit;
	}
	//2020/03/10 Add End @COSMO
}
