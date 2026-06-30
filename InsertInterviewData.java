import java.sql.*;

public class InsertInterviewData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/recruitment_platform?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "123456";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // 张同学 - Java后端开发工程师 - 待面试
                pstmt.setLong(1, 11); pstmt.setLong(2, 1); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 2); pstmt.setString(6, "我对这个职位非常感兴趣");
                pstmt.setString(7, "2026-06-15 10:00:00"); pstmt.setString(8, "广州市天河区某某大厦A座301室");
                pstmt.executeUpdate();
                
                // 李同学 - Java后端开发工程师 - 通过
                pstmt.setLong(1, 11); pstmt.setLong(2, 2); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 3); pstmt.setString(6, "期待加入贵公司");
                pstmt.setString(7, "2026-06-14 14:30:00"); pstmt.setString(8, "腾讯会议：xxx-xxx-xxx");
                pstmt.executeUpdate();
                
                // 王同学 - 前端开发实习生 - 待面试
                pstmt.setLong(1, 11); pstmt.setLong(2, 3); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 2); pstmt.setString(6, "感谢您的考虑");
                pstmt.setString(7, "2026-06-16 09:00:00"); pstmt.setString(8, "-");
                pstmt.executeUpdate();
                
                // 赵同学 - 电商运营专员 - 淘汰
                pstmt.setLong(1, 11); pstmt.setLong(2, 4); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 4); pstmt.setString(6, "谢谢");
                pstmt.setString(7, "2026-06-13 15:00:00"); pstmt.setString(8, "广州市天河区某某大厦A座305室");
                pstmt.executeUpdate();
                
                // 孙同学 - Java后端开发工程师 - 通过
                pstmt.setLong(1, 11); pstmt.setLong(2, 5); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 3); pstmt.setString(6, "面试邀请已收到");
                pstmt.setString(7, "2026-06-12 10:30:00"); pstmt.setString(8, "腾讯会议：yyy-yyy-yyy");
                pstmt.executeUpdate();
                
                // 周同学 - 产品经理 - 待面试
                pstmt.setLong(1, 11); pstmt.setLong(2, 6); pstmt.setLong(3, 1); pstmt.setLong(4, 1);
                pstmt.setInt(5, 2); pstmt.setString(6, "期待加入贵公司");
                pstmt.setString(7, "2026-06-17 11:00:00"); pstmt.setString(8, "广州市天河区某某大厦A座301室");
                pstmt.executeUpdate();
                
                System.out.println("数据插入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}