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
	// 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    public void doPost(HttpServletRequest request,HttpServletResponse response)
    		throws ServletException, IOException {
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            return;
        }
        String id = null, name = null;
        Map<Object, Object> map = new HashMap<Object, Object>(); 
        SqlExecute	sql = new SqlExecute();
        sql.setDBTable("zhiwuxinzhi");
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        String tempPath = this.getServletContext().getRealPath("graduateWork"+ File.separator + "plantImg" + File.separator + "temp");
        File temp = new File(tempPath);
        factory.setRepository(temp);
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // 中文处理
        upload.setHeaderEncoding("UTF-8"); 
        String uploadPath = this.getServletContext().getRealPath("graduateWork"+ File.separator + "plantImg");
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    //判断是否为普通表单项
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
                        //保存到MYSQL的路径
                        filePath = this.getServletContext().getContextPath() + "/graduateWork/plantImg/" + fileName;
                    	map.put("image", filePath);
                        // 保存文件到硬盘
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
                    "错误信息: " + ex.getMessage());
        }
        
    }
    
}
        

