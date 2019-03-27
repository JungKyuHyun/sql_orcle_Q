package Utils;

import java.util.Scanner;

import DAO.DepartmentDao;

public class DepartmentMenu {
	public static Scanner sc;
	private String key;
	static {
		sc = new Scanner(System.in);
	}
	
	public void showMenu() {
		

		
		do {
			System.out.println("**********************");
			System.out.println("*1.부서목록");
			System.out.println("*2.부서등록");
			System.out.println("*3.부서변경");
			System.out.println("*4.부서삭제");
			System.out.println("*5.부서검색");
			System.out.println("*6.프로그램종료");
			System.out.println("**********************");
			System.out.print("작업번호선택: ");
			key = sc.nextLine();
			System.out.println("**********************");
			switch (key) {
			case "1":	//부서목록조회(전체조회)	
				DepartmentDao.DeptPrint(DepartmentDao.getDeptAllList());
				break;
			case "2":		//부서등록
				DepartmentDao.insertDept();
				break;
			case "3":		//부서변경
				DepartmentDao.updateDept();
				break;
			case "4":		//부서삭제
				DepartmentDao.deleteDepartment();
				break;
			case "5":		//부서검색
				DepartmentDao.DeptPrint(DepartmentDao.getDepartmentListByDeptno());
				break;
			case "6":		//종료
				System.out.println("시스템을 종료합니다.");
				System.exit(0);
			default:
				break;
			}
		}while(true);

	}
}
