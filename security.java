package security;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JButton;

public class security extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_tk;
	private JPasswordField textField_mk;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					security frame = new security();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public security() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 689, 452);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbl_TK = new JLabel("Tài khoản");
		lbl_TK.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_TK.setBounds(80, 81, 147, 37);
		contentPane.add(lbl_TK);

		textField_tk = new JTextField();
		textField_tk.setBounds(259, 81, 211, 34);
		contentPane.add(textField_tk);
		textField_tk.setColumns(10);

		JLabel lbl_MK = new JLabel("Mật khẩu");
		lbl_MK.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_MK.setBounds(80, 167, 147, 37);
		contentPane.add(lbl_MK);

		textField_mk = new JPasswordField();
		textField_mk.setColumns(10);
		textField_mk.setBounds(259, 167, 211, 34);
		contentPane.add(textField_mk);

		JButton btnDK = new JButton("Đăng ký");
		btnDK.setBounds(138, 272, 133, 37);
		contentPane.add(btnDK);
		btnDK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taotk();
			}
		});

		JButton btnDN = new JButton("Đăng nhập");
		btnDN.setBounds(339, 272, 131, 37);
		contentPane.add(btnDN);
		btnDN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dangnhap();
			}
		});
	}

	public void taotk() {
	    String tentk = textField_tk.getText();
	    String matkhau = textField_mk.getText();
	    
	    if (tentk.isEmpty() || matkhau.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng điền tên tài khoản và mật khẩu");
	        return;
	    }
	    
	    try {
	        String url = "jdbc:mysql://localhost:3306/taikhoann";
	        Connection conn = DriverManager.getConnection(url, "root", "1234");

	        // Mã hóa mật khẩu bằng bcrypt
	        String hashedPassword = BCrypt.hashpw(matkhau, BCrypt.gensalt());

	        String sql = "INSERT INTO baitap (taikhoan, matkhau) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, tentk);
	        pstmt.setString(2, hashedPassword);
	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Tài khoản đã được tạo thành công");
	            // Thực hiện các hành động sau khi tạo tài khoản thành công
	        } else {
	            JOptionPane.showMessageDialog(this, "Không thể tạo tài khoản. Vui lòng thử lại");
	        }

	        pstmt.close();
	        conn.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu");
	    }
	}


	public void dangnhap() {
	    String tentk = textField_tk.getText();
	    String matkhau = textField_mk.getText();
	    
	    if (tentk.isEmpty() || matkhau.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng điền tên tài khoản và mật khẩu");
	        return;
	    }
	    
	    try {
	        String url = "jdbc:mysql://localhost:3306/taikhoann";
	        Connection conn = DriverManager.getConnection(url, "root", "1234");
	        String sql = "SELECT * FROM baitap WHERE taikhoan = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, tentk);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            String hashedPassword = rs.getString("matkhau");
	            
	            // Kiểm tra mật khẩu đã mã hóa
	            boolean passwordMatch = BCrypt.checkpw(matkhau, hashedPassword);
	            
	            if (passwordMatch) {
	                JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
	                // Thực hiện các hành động sau khi đăng nhập thành công
	            } else {
	                JOptionPane.showMessageDialog(this, "Đăng nhập không thành công. Vui lòng kiểm tra lại tài khoản và mật khẩu");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Đăng nhập không thành công. Vui lòng kiểm tra lại tài khoản và mật khẩu");
	        }
	        
	        rs.close();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu");
	    }
	}

}
