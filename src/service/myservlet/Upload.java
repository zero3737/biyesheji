package service.myservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.io.File;
import dao.mysql.SqlExecute;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1L;
	// �ϴ�����
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    public void doPost(HttpServletRequest request,HttpServletResponse response)
    		throws ServletException, IOException {
        // ����Ƿ�Ϊ��ý���ϴ�
        if (!ServletFileUpload.isMultipartContent(request)) {
            // ���������ֹͣ
            return;
        }
        String id = null, name = null;
        Map<Object, Object> map = new HashMap<Object, Object>(); 
        SqlExecute	sql = new SqlExecute();
        sql.setDBTable("zhiwuxinzhi");
        // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��ٽ�ֵ - �����󽫲�����ʱ�ļ����洢����ʱĿ¼��
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // ������ʱ�洢Ŀ¼
        String tempPath = this.getServletContext().getRealPath("graduateWork"+ File.separator + "plantImg" + File.separator + "temp");
        File temp = new File(tempPath);
        factory.setRepository(temp);
        ServletFileUpload upload = new ServletFileUpload(factory);
        // ��������ļ��ϴ�ֵ
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // �����������ֵ (�����ļ��ͱ�����)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // ���Ĵ���
        upload.setHeaderEncoding("UTF-8"); 
        String uploadPath = this.getServletContext().getRealPath("graduateWork"+ File.separator + "plantImg");
        // ���Ŀ¼�������򴴽�
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            // ���������������ȡ�ļ�����
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // ����������
                for (FileItem item : formItems) {
                    //�ж��Ƿ�Ϊ��ͨ����
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if(fileName.equals("")){
                        	if(!id.equals("")){
                        		String[] str = {"image"};
                        		String image = (String)sql.getTabletoList(str, "id", id).get(0);
                        		map.put("image", image);
                        	}
                    		continue;
                        }
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        //���浽MYSQL��·��
                        filePath = this.getServletContext().getContextPath() + "/graduateWork/plantImg/" + fileName;
                    	map.put("image", filePath);
                        // �����ļ���Ӳ��
                        item.write(storeFile);
                        item.delete();
                    }else{
                    	name = item.getFieldName();
                    	if(name.equalsIgnoreCase("id"))
                    		id = item.getString("UTF-8");
                    	else
                    		map.put(name, item.getString("UTF-8"));
                    }
                }
            }
        	if(id == null)
				try {
					sql.addOrUpdate(map);
		        	response.sendRedirect("/biyesheji/graduateWork/admin/submitsucceed.html");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			else
				try{
					sql.addOrUpdate(map, id);
		        	response.sendRedirect("/biyesheji/graduateWork/admin/submitsucceed.html");
				}catch (SQLException e){
					e.printStackTrace();
				}
        }catch(Exception ex){
            request.setAttribute("message",
                    "������Ϣ: " + ex.getMessage());
        }
        
    }
    
}
        

