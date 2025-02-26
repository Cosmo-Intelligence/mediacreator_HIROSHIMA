package MediaCreator.Web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MediaCreator.Common.Config;
import MediaCreator.Common.DataBase;
import MediaCreator.Common.DataRow;
import MediaCreator.Common.DataTable;
import MediaCreator.Common.LogFactory;
import MediaCreator.Common.SessionControler;

import org.apache.log4j.*;

/**
 * Servlet implementation class ShowViewC
 */
public class ShowViewC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Logger logger = LogFactory.getLogger();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowViewC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		response.setDateHeader("Expires", -1);
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control","no-store");
		response.setContentType("text/html; charset=UTF-8");
	
		doPost(request,response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		response.setDateHeader("Expires", -1);
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control","no-store");
		response.setContentType("text/html; charset=UTF-8");
	
		showViewC(request,response);
		
	}

	private void showViewC(HttpServletRequest request,HttpServletResponse response){
		
		String accessionno = request.getParameter("accessionno");
		String userid = "";
		String password = "";
		String url = "";
		
		Config config = null;
		
		Connection conn = null;
		
		
		try{
			
			conn = DataBase.getArqsConnection();

			config = (Config)SessionControler.getValue(request, SessionControler.SYSTEMCONFIG);
			
			try{
				
				DataRow userRow = (DataRow)SessionControler.getValue(request, SessionControler.LOGINUSER);
				
				userid = userRow.get("USERID").toString();
				
				//社内テストNo10 パスワード取得時のエラー処理
				DataTable Dat = DataBase.getUserManageVins(userid, conn);
				if(Dat.getRowCount() > 0){
					password = Dat.getRows().get(0).get("PASSWORD").toString();
				}

				url = config.getViewCUrl();

			}
			catch(Exception ee){
				logger.error(ee.toString(),ee);
			}
			

		    
		}
		catch(Exception e){
			logger.error(e.toString(),e);
		}
		finally{
			DataBase.closeConnection(conn);
		}
		
		try{
			logger.debug(url);
		
			url = String.format(url, userid, password, accessionno);
				
			response.sendRedirect(url);
		}
		catch(Exception e){
				logger.error(e.toString(),e);
		}

	}
	
	

}
