<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="MediaCreator.Common.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.net.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	request.setCharacterEncoding("UTF-8");

		String RIS_ID = (String)request.getAttribute("RIS_ID");					//ris_id
		String DOCTOR_NAME = (String)request.getAttribute("DOCTOR_NAME");		//依頼医
		String SECTION_NAME = (String)request.getAttribute("SECTION_NAME");		//依頼科
		Timestamp REQUESTDATE = (Timestamp)request.getAttribute("REQUESTDATE");	//依頼日
		String KANJA_ID = (String)request.getAttribute("KANJA_ID");				//患者ID
		String KANJISIMEI = (String)request.getAttribute("KANJISIMEI");			//患者氏名
		String KANASIMEI = (String)request.getAttribute("KANASIMEI");			//カナ氏名
		String ORDERNO = (String)request.getAttribute("ORDERNO");				//AccessionNo
		String NYUGAI = (String)request.getAttribute("NYUGAI");					//入外


		String   ACCESSIONNO_1			=	(String)request.getAttribute(     "ACCESSIONNO_1");
		String MODALITY_TYPE_1			=	(String)request.getAttribute(   "MODALITY_TYPE_1");
		Timestamp KENSA_DATE_1			=	(Timestamp)request.getAttribute(   "KENSA_DATE_1");
		String STUDYDESCRIPTION_1		=	(String)request.getAttribute("STUDYDESCRIPTION_1");

		String ACCESSIONNO_2			=	(String)request.getAttribute(     "ACCESSIONNO_2");
		String MODALITY_TYPE_2			=	(String)request.getAttribute(   "MODALITY_TYPE_2");
		Timestamp KENSA_DATE_2			=	(Timestamp)request.getAttribute(   "KENSA_DATE_2");
		String STUDYDESCRIPTION_2		=	(String)request.getAttribute("STUDYDESCRIPTION_2");

		String ACCESSIONNO_3			=	(String)request.getAttribute(     "ACCESSIONNO_3");
		String MODALITY_TYPE_3			=	(String)request.getAttribute(   "MODALITY_TYPE_3");
		Timestamp KENSA_DATE_3			=	(Timestamp)request.getAttribute(   "KENSA_DATE_3");
		String STUDYDESCRIPTION_3		=	(String)request.getAttribute("STUDYDESCRIPTION_3");

		String ACCESSIONNO_4			=	(String)request.getAttribute(     "ACCESSIONNO_4");
		String MODALITY_TYPE_4			=	(String)request.getAttribute(   "MODALITY_TYPE_4");
		Timestamp KENSA_DATE_4			=	(Timestamp)request.getAttribute(   "KENSA_DATE_4");
		String STUDYDESCRIPTION_4		=	(String)request.getAttribute("STUDYDESCRIPTION_4");

		String ACCESSIONNO_5			=	(String)request.getAttribute(     "ACCESSIONNO_5");
		String MODALITY_TYPE_5			=	(String)request.getAttribute(   "MODALITY_TYPE_5");
		Timestamp KENSA_DATE_5			=	(Timestamp)request.getAttribute(   "KENSA_DATE_5");
		String STUDYDESCRIPTION_5		=	(String)request.getAttribute("STUDYDESCRIPTION_5");

		String ACCESSIONNO_6			=	(String)request.getAttribute(     "ACCESSIONNO_6");
		String MODALITY_TYPE_6			=	(String)request.getAttribute(   "MODALITY_TYPE_6");
		Timestamp KENSA_DATE_6			=	(Timestamp)request.getAttribute(   "KENSA_DATE_6");
		String STUDYDESCRIPTION_6		=	(String)request.getAttribute("STUDYDESCRIPTION_6");

		String ACCESSIONNO_7			=	(String)request.getAttribute(     "ACCESSIONNO_7");
		String MODALITY_TYPE_7			=	(String)request.getAttribute(   "MODALITY_TYPE_7");
		Timestamp KENSA_DATE_7			=	(Timestamp)request.getAttribute(   "KENSA_DATE_7");
		String STUDYDESCRIPTION_7		=	(String)request.getAttribute("STUDYDESCRIPTION_7");

		String ACCESSIONNO_8			=	(String)request.getAttribute(     "ACCESSIONNO_8");
		String MODALITY_TYPE_8			=	(String)request.getAttribute(   "MODALITY_TYPE_8");
		Timestamp KENSA_DATE_8			=	(Timestamp)request.getAttribute(   "KENSA_DATE_8");
		String STUDYDESCRIPTION_8		=	(String)request.getAttribute("STUDYDESCRIPTION_8");

		String ACCESSIONNO_9			=	(String)request.getAttribute(     "ACCESSIONNO_9");
		String MODALITY_TYPE_9			=	(String)request.getAttribute(   "MODALITY_TYPE_9");
		Timestamp KENSA_DATE_9			=	(Timestamp)request.getAttribute(   "KENSA_DATE_9");
		String STUDYDESCRIPTION_9		=	(String)request.getAttribute("STUDYDESCRIPTION_9");

		String ACCESSIONNO_10			=	(String)request.getAttribute(     "ACCESSIONNO_10");
		String MODALITY_TYPE_10			=	(String)request.getAttribute(   "MODALITY_TYPE_10");
		Timestamp KENSA_DATE_10			=	(Timestamp)request.getAttribute(   "KENSA_DATE_10");
		String STUDYDESCRIPTION_10		=	(String)request.getAttribute("STUDYDESCRIPTION_10");

		String ACCESSIONNO_11			=	(String)request.getAttribute(     "ACCESSIONNO_11");
		String MODALITY_TYPE_11			=	(String)request.getAttribute(   "MODALITY_TYPE_11");
		Timestamp KENSA_DATE_11			=	(Timestamp)request.getAttribute(   "KENSA_DATE_11");
		String STUDYDESCRIPTION_11		=	(String)request.getAttribute("STUDYDESCRIPTION_11");

		String ACCESSIONNO_12			=	(String)request.getAttribute(     "ACCESSIONNO_12");
		String MODALITY_TYPE_12			=	(String)request.getAttribute(   "MODALITY_TYPE_12");
		Timestamp KENSA_DATE_12			=	(Timestamp)request.getAttribute(   "KENSA_DATE_12");
		String STUDYDESCRIPTION_12		=	(String)request.getAttribute("STUDYDESCRIPTION_12");

		String ACCESSIONNO_13			=	(String)request.getAttribute(     "ACCESSIONNO_13");
		String MODALITY_TYPE_13			=	(String)request.getAttribute(   "MODALITY_TYPE_13");
		Timestamp KENSA_DATE_13			=	(Timestamp)request.getAttribute(   "KENSA_DATE_13");
		String STUDYDESCRIPTION_13		=	(String)request.getAttribute("STUDYDESCRIPTION_13");

		String ACCESSIONNO_14			=	(String)request.getAttribute(     "ACCESSIONNO_14");
		String MODALITY_TYPE_14			=	(String)request.getAttribute(   "MODALITY_TYPE_14");
		Timestamp KENSA_DATE_14			=	(Timestamp)request.getAttribute(   "KENSA_DATE_14");
		String STUDYDESCRIPTION_14		=	(String)request.getAttribute("STUDYDESCRIPTION_14");

		String ACCESSIONNO_15			=	(String)request.getAttribute(     "ACCESSIONNO_15");
		String MODALITY_TYPE_15			=	(String)request.getAttribute(   "MODALITY_TYPE_15");
		Timestamp KENSA_DATE_15			=	(Timestamp)request.getAttribute(   "KENSA_DATE_15");
		String STUDYDESCRIPTION_15		=	(String)request.getAttribute("STUDYDESCRIPTION_15");

		String ACCESSIONNO_16			=	(String)request.getAttribute(     "ACCESSIONNO_16");
		String MODALITY_TYPE_16			=	(String)request.getAttribute(   "MODALITY_TYPE_16");
		Timestamp KENSA_DATE_16			=	(Timestamp)request.getAttribute(   "KENSA_DATE_16");
		String STUDYDESCRIPTION_16		=	(String)request.getAttribute("STUDYDESCRIPTION_16");

		String ACCESSIONNO_17			=	(String)request.getAttribute(     "ACCESSIONNO_17");
		String MODALITY_TYPE_17			=	(String)request.getAttribute(   "MODALITY_TYPE_17");
		Timestamp KENSA_DATE_17			=	(Timestamp)request.getAttribute(   "KENSA_DATE_17");
		String STUDYDESCRIPTION_17		=	(String)request.getAttribute("STUDYDESCRIPTION_17");

		String ACCESSIONNO_18			=	(String)request.getAttribute(     "ACCESSIONNO_18");
		String MODALITY_TYPE_18			=	(String)request.getAttribute(   "MODALITY_TYPE_18");
		Timestamp KENSA_DATE_18			=	(Timestamp)request.getAttribute(   "KENSA_DATE_18");
		String STUDYDESCRIPTION_18		=	(String)request.getAttribute("STUDYDESCRIPTION_18");

		String ACCESSIONNO_19			=	(String)request.getAttribute(     "ACCESSIONNO_19");
		String MODALITY_TYPE_19			=	(String)request.getAttribute(   "MODALITY_TYPE_19");
		Timestamp KENSA_DATE_19			=	(Timestamp)request.getAttribute(   "KENSA_DATE_19");
		String STUDYDESCRIPTION_19		=	(String)request.getAttribute("STUDYDESCRIPTION_19");

		String ACCESSIONNO_20			=	(String)request.getAttribute(     "ACCESSIONNO_20");
		String MODALITY_TYPE_20			=	(String)request.getAttribute(   "MODALITY_TYPE_20");
		Timestamp KENSA_DATE_20			=	(Timestamp)request.getAttribute(   "KENSA_DATE_20");
		String STUDYDESCRIPTION_20		=	(String)request.getAttribute("STUDYDESCRIPTION_20");

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">

<title>医療画像情報持出</title>

<script type="text/javascript" src="js/common.js"></script>

<%-- JQuery --%>
<script type="text/javascript" src="lib/jquery/js/jquery-1.8.3.js" ></script>
<script type="text/javascript" src="lib/jquery/js/jquery-ui-1.9.2.custom.js" ></script>
<script type="text/javascript" src="lib/jquery/js/i18n/jquery.ui.datepicker-ja.js" ></script>
<link rel="stylesheet" type="text/css" media="all"href="lib/jquery/css/mediacreator/jquery-ui-1.9.2.custom.css" />


<!-- <link rel="stylesheet" media="all" href="css/CommonStyle.css" type="text/css" /> -->
<link rel="stylesheet" media="all" href="css/PrintStyle.css" type="text/css"/>
<link rel="stylesheet" media="print" href="css/PrintStyleOnPrint.css" type="text/css"/>


</head>
<%--
	レイアウト編集の説明。

	使える変数は下記です。
	HTMLに埋め込む際には <%= と %> で括って下さい。


	/* メディア作成依頼情報 */
	RIS_ID 			//ris_id
	DOCTOR_NAME		//依頼医
	SECTION_NAME	//依頼科
	REQUESTDATE		//依頼日(日付型)
	KANJA_ID 		//患者ID
	KANJISIMEI		//患者氏名
	KANASIMEI		//カナ氏名
	ORDERNO			//オーダーNo(AccessionNo)
	NYUGAI			//入外

	/* メディア持ち出し画像情報 */
	/* 末尾の数字は1から20の最大20件まで使用できます。 */
	ACCESSIONNO_1		//AccessionNumber
	MODALITY_TYPE_1		//モダリティ
	KENSA_DATE_1		//検査日(日付型)
	STUDYDESCRIPTION_1	//STUDYDESCRIPTION


	■日付型の変数は、下記要領でフォーマットして使用して下さい。

		例：依頼日を 2013/11/18 の形式にする場合。
		Common.toFormatString(REQUESTDATE,"yyyy/MM/dd") → 2013/11/20

		和暦にする場合
		Common.toFormatStringJP(REQUESTDATE,"GGGGy年M月d日") → 平成25年11月20日


	■バーコードの表示
	バーコードはimgタグで表示します。
	下記の要領でsrcに表示する値とサイズを指定します。
	<img src="BarcodeImage?value=<%=KANJA_ID%>&width=100&height=30">

		value: バーコードの値
		width: バーコードの幅(px)
		height:バーコードの高さ(px)


 	ここから下(<body>から下)を編集して下さい。
 	変数の埋め込み以外は通常のHTMLです。

 --%>
<body>
	<input type="button" value="印刷" class="cmdbtn" onclick="window.print();">
	<table class="print-frame" align="center">
		<tr align="center">
			<td>
				<H1>放射線画像メディア受取票</H1>
			</td>
		</tr>
		<tr align="right">
			<td>

			</td>
		</tr>
		<tr align="right">
			<td>
				<img src="BarcodeImage?value=<%=KANJA_ID%>&width=200&height=50">
			</td>
		</tr>
		<tr>
			<td>
				<table class="print-table" width="100%">
					<col width="140px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<col width="100px"/>
					<tr class="print-cell">
						<td class="print-cell" style="text-align:center;">
							依頼年月日
						</td>
						<td  class="print-cell" colspan="5">
							<%=Common.toFormatString(REQUESTDATE,"yyyy 年 MM 月 dd 日 ( E )") %>
						</td>
					</tr>
					<tr>
						<td class="print-cell" style="text-align:center;">
							患者ID
						</td>
						<td  class="print-cell" colspan="4">
							<%=KANJA_ID %>
						</td>
						<td class="print-cell" style="text-align:center;">
							<%=NYUGAI %>
						</td>
					</tr>
					<tr>
						<td class="print-cell" style="text-align:center;">
							患者氏名
						</td>
						<%--患者漢字氏名を非表示、かな氏名に変更　2014/10/30 --%>
						<%--<td  class="print-cell" colspan="5"> --%>
						<%--	<span style="font-size:x-small"><%=KANASIMEI %></span> --%>
						<%--	<br/><%=KANJISIMEI %> --%>
						<td  class="print-cell" colspan="5">
							<%=KANASIMEI %>
						</td>
					</tr>
					<tr>
						<td class="print-cell" style="text-align:center;">
							依頼科(受診科)
						</td>
						<td  class="print-cell" colspan="2">
							<%=SECTION_NAME %>
						</td>
						<td class="print-cell" style="text-align:center;">
							依頼者名
						</td>
						<td  class="print-cell" colspan="2">
							<%=DOCTOR_NAME %>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							&nbsp;
						</td>
						<td  class="print-cell"  style="text-align:center;">
							連絡先部署
						</td>
						<td  class="print-cell" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="3">
							&nbsp;
						</td>
						<td  class="print-cell"  style="text-align:center;">
							内線・PHS
						</td>
						<td   class="print-cell" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="6">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td  class="print-cell" rowspan="3" style="text-align:center;">
							伝達事項
						</td>
						<td  class="print-cell" rowspan="3"colspan="5" >
							&nbsp;
						</td>
					</tr>
					<tr></tr><tr></tr>
					<tr>
						<td  class="print-cell"  style="text-align:center;">
							画像持出日
						</td>
						<td class="print-cell"  colspan="5">
							　　　　　年　　　　　月　　　　　日　　(　　　)
						</td>
					</tr>
					<tr>
						<td colspan="2">
							&nbsp;
						</td>
						<td  class="print-cell" colspan="2" style="text-align:center;">
							メディア引渡し担当者氏名
						</td>
						<td  class="print-cell" colspan="2" style="text-align:center;">
							受取者氏名
						</td>
					</tr>
					<tr>
						<td rowspan="2" colspan="2">
							&nbsp;
						</td>
						<td rowspan="2" class="print-cell" colspan="2">
							&nbsp;
						</td>
						<td rowspan="2" class="print-cell" colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="bottom">
			<td>
				<span style="font-size:small;">*地下１階・放射線受付にこの用紙をお持ちください。</span>
			</td>
		</tr>
	</table>

</body>
</html>