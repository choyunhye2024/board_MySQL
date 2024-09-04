package com.mysql.a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProcBoard {

	Connection con = null;
	Statement st = null;
	ResultSet result = null;
	Scanner sc = new Scanner(System.in);

	void run() {

		Display.showTitle();
		dbInit();
		loop: while (true) {

			dbPostCount();
			Display.showMainMenu();
			System.out.println("명령입력:");
			String cmd = sc.next();
			switch (cmd) {

			case "1":
				// 리스트
				System.out.println("======================================");
				System.out.println("=============== 리스트 ================");
				System.out.println("======================================");
				System.out.println(" 글번호 / 글제목 / 작성자 ID / 작성시간");
				try {

					result = st.executeQuery("select*from board");
					while (result.next()) {

						String no = result.getString("b_no");
						String title = result.getString("b_title");
						String id = result.getString("b_id");
						String datetime = result.getString("b_datetime");
						System.out.println(no + "" + title + "" + id + "" + datetime);
					}
				} catch (SQLException e) {

					e.printStackTrace();

				}

				break;
			case "2":
				// 읽기
				System.out.println("읽을 글 번호를 입력해주세요:");
				String readNo = sc.next();
				try {
					result = st.executeQuery("select*from board where b_no=" + readNo);
					result.next();
					String title = result.getString("b_title");
					String content = result.getString("b_text");
					System.out.println("글제목:" + title);
					System.out.println("글내용:" + content);
				} catch (SQLException e) {

					e.printStackTrace();

				}
				break;
			case "3":
				// 쓰기
				sc.nextLine();
				System.out.println("제목을 입력해주세요:");
				String title = sc.nextLine();
				System.out.println("글내용을 입력해주세요:");
				String content = sc.nextLine();
				System.out.println("작성자 id를 입력해주세요:");
				String id = sc.next();
				try {

					st.executeUpdate("insert into board(b_title, b_id, b_datetime, b_text,b_hit)" + "values ('" + title
							+ "','" + id + "',now(),'" + content + "',0)");
					System.out.println("글등록 완료:");

				} catch (SQLException e) {

					e.printStackTrace();

				}
				break;
			case "4":
				// 삭제
				System.out.println("삭제할 글 번호를 입력해주세요:");
				String delNo = sc.next();
				dbExecuteUpdate("delete from board where b_no = " + delNo);
				break;

			case "5":
				System.out.println("수정할 글 번호를 입력해주세요:");
				String editNo = sc.next();
				System.out.println("제목을 입력해주세요:");
				sc.nextLine();
				String edTitle = sc.nextLine();
				System.out.println("작성자 아이디를 입력해주세요:");
				String edId = sc.next();
				System.out.println("글 내용을 입력해주세요:");
				sc.nextLine();
				String edContent = sc.nextLine();
				dbExecuteUpdate("update board set b_title='" + edTitle + "',b_id='" + edId
						+ "',b_datetime = now(), b_text='" + edContent + "'where b_no" + editNo);
				// 수정
				break;
			case "0":
				// 관리자모드
				break;
			case "e":
				// 종료
				break;

			}

		}

	}

	private void dbInit() {

		try {

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_cat", "root", "root");
			st = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	private void dbExecuteUpdate(String query) {

		try {

			int resultCount = st.executeUpdate(query);
			System.out.println("처리된 행 수:" + resultCount);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	private void dbPostCount() {

		try {

			result = st.executeQuery("select count(*) from board");
			result.next();
			String count = result.getString("count(*)");
			System.out.println("글 수:" + count);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}