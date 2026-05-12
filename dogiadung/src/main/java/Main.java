import dao.Nga_CustomerDAO;
import model.Nga_Customer;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("--- ĐANG KIỂM TRA KẾT NỐI SQL SERVER ---");

            Nga_CustomerDAO dao = new Nga_CustomerDAO();
            List<Nga_Customer> list = dao.getAll();

            if (list != null && !list.isEmpty()) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
                System.out.println("Số lượng khách hàng lấy được: " + list.size());
                for (Nga_Customer c : list) {
                    System.out.println(">> " + c.getFullName() + " - " + c.getPhone());
                }
            } else {
                System.out.println("⚠️ Kết nối OK nhưng bảng nga_customers đang trống.");
            }

        } catch (Exception e) {
            System.out.println("❌ LỖI RỒI NGA ƠI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}