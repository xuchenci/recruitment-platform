import java.sql.*;
public class InsertData {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/recruitment_platform?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        try (Connection conn = DriverManager.getConnection(url, "root", "123456")) {
            String[] data = {
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 1, 1, 1, 2, '我对这个职位非常感兴趣', NOW(), '2026-06-15 10:00:00', '广州市天河区某某大厦A座301室')",
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 2, 1, 1, 3, '期待加入贵公司', NOW(), '2026-06-14 14:30:00', '腾讯会议：xxx-xxx-xxx')",
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 3, 1, 1, 2, '感谢您的考虑', NOW(), '2026-06-16 09:00:00', '-')",
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 4, 1, 1, 4, '谢谢', NOW(), '2026-06-13 15:00:00', '广州市天河区某某大厦A座305室')",
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 5, 1, 1, 3, '面试邀请已收到', NOW(), '2026-06-12 10:30:00', '腾讯会议：yyy-yyy-yyy')",
                "INSERT INTO job_application (user_id, job_id, student_id, enterprise_id, status, cover_letter, apply_time, interview_time, interview_location) VALUES (11, 6, 1, 1, 2, '期待加入贵公司', NOW(), '2026-06-17 11:00:00', '广州市天河区某某大厦A座301室')"
            };
            try (Statement stmt = conn.createStatement()) {
                for (String sql : data) stmt.executeUpdate(sql);
                System.out.println("Done");
            }
        }
    }
}