package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import DTO.Department;
import Utils.DepartmentMenu;
import Utils.SingletonHelper;


public class DepartmentDao {
	
	
	
    // 1. 부서목록조회(전체조회)
    public static List<Department> getDeptAllList() {
    	List<Department> deptlist = new ArrayList<Department>(); // 여러건의 데이터 담는 클래스
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = SingletonHelper.getConnection("oracle");
            String sql = "select deptno, dname from department order by deptno";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Department department = new Department(); // 하나의 row 담기 위한 객체
                department.setDeptno(rs.getInt("deptno"));
                department.setDname(rs.getString("dname"));
                deptlist.add(department); // 배열에 객체 담는 것
                
            }
        } catch (Exception e) {
            System.out.println("오류"+e.getMessage());
        } finally {
            SingletonHelper.close(rs);
            SingletonHelper.close(pstmt);
        }
        return deptlist;
    }
    // 2. 부서등록
    // public int insertDept(int deptno , String dname , String loc) ..(x)
    public static void insertDept() {
    	Connection conn = null;
        PreparedStatement pstmt = null;
        int rowcount = 0;
        
    	try {
	    	System.out.print("부서코드:");
	    	int deptno = Integer.parseInt(DepartmentMenu.sc.nextLine());
	    	if(isExist(deptno)) {
	    		return;
	    	}
	    	System.out.print("부서이름:");
	    	String dname = DepartmentMenu.sc.nextLine();
	    	if(isExist(dname)) {
	    		return;
	    	}
            conn = SingletonHelper.getConnection("oracle");
            String sql = "insert into department(deptno,dname) values(?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptno);
            pstmt.setString(2, dname);
            rowcount = pstmt.executeUpdate();
            conn.commit();
    		if(rowcount > 0) {
    			System.out.println("INSERT ROW : " + rowcount);
    			System.out.println("INSERT DATA : " + deptno+" : "+dname);
    		}else {
    			System.out.println("INSERT FAIL");
    		}
        } catch (Exception e) {
            System.out.println("올바른 값을 입력하세요." + e.getMessage());
        } finally {
            SingletonHelper.close(pstmt);
        }
    }
    // 3. 부서변경
    public static void updateDept() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowcount = 0;
    	
        try {
    	System.out.print("변경할부서명:");
    	String dname = DepartmentMenu.sc.nextLine();
    	Department temp = getDeptListByDeptno(dname); //toString
    	System.out.println("Department [deptno="+temp.getDeptno()+", dname="+temp.getDname()+"]");
    	//tostring
    	System.out.println("[부서 변경 정보 입력]");
    	System.out.print("부서코드:");
    	int deptno = Integer.parseInt(DepartmentMenu.sc.nextLine());
    	try {
			if(isExist(deptno)) {
				return;
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
    	
    	System.out.print("부서이름:");
    	String dname1 = DepartmentMenu.sc.nextLine();
    	try {
			if(isExist(dname1)) {
				return;
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}        
            conn = SingletonHelper.getConnection("oracle");
            String sql = "update department set dname=?, deptno=? where deptno=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dname1);
            pstmt.setInt(2, deptno);
            pstmt.setInt(3, getDeptListByDeptno(dname).getDeptno());
            rowcount = pstmt.executeUpdate();
            System.out.println("변경된 ROW:"+rowcount);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            SingletonHelper.close(pstmt);
        }
    }
    
    //3-1. 조건검색
	public static Department getDeptListByDeptno(String dname) {
		Department dept = null; //
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = SingletonHelper.getConnection("oracle");
			String sql = "select deptno, dname from department where dname=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dname);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dept = new Department(); // 하나의 row 담기 위한 객체
				dept.setDeptno(rs.getInt("deptno"));
				dept.setDname(rs.getString("dname"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			SingletonHelper.close(rs);
			SingletonHelper.close(pstmt);
		}

		return dept;
	}
	

	//4. 부서삭제
    public static void deleteDepartment() {
        // delete from department where deptno=?
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowcount = 0;
        try {
    	System.out.print("삭제할부서명:");
    	String dname = DepartmentMenu.sc.nextLine();
    	try {
			if(!isExist(dname)) {
				return;
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} 
            conn = SingletonHelper.getConnection("oracle");
            String sql = "delete from department where dname=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dname);
            rowcount = pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            SingletonHelper.close(pstmt);
        }
    }
    
     //5. 부서검색 (where dname like '%A%')
        public static List<Department> getDepartmentListByDeptno() {
        	List<Department> deptlist = new ArrayList<Department>();
           	System.out.print("검색할부서명:");
        	String dname = DepartmentMenu.sc.nextLine();
        	
            Department department = null; //
    
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
    
            try {
                conn = SingletonHelper.getConnection("oracle");
                String sql = "select deptno, dname from department where dname like ?";
                pstmt = conn.prepareStatement(sql);
    
                pstmt.setString(1, "%"+dname+"%");
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    department = new Department(); // 하나의 row 담기 위한 객체
                    department.setDeptno(rs.getInt("deptno"));
                    department.setDname(rs.getString("dname"));
                    deptlist.add(department);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                SingletonHelper.close(rs);
                SingletonHelper.close(pstmt);
            }
    
            return deptlist;
        }   
        
    	public static void DeptPrint(Department dept) {
    		System.out.println(dept.toString());
    	}
    	
    	public static void DeptPrint(List<Department> list) {
    		if(list !=null) {
        		for(Department data : list) {
        			System.out.println(data.toString());
        		}
    		}else {
    			System.out.println("리스트가 비어있는데요??");
    		}

    	}
    	
    	public static boolean isExist(int deptno) throws Exception {
            Connection conn = null;
            PreparedStatement pstmt = null;
            int rowcount = 0;
            try {
                conn = SingletonHelper.getConnection("oracle");
                conn.setAutoCommit(false);
                String sql = "update department set dname=? where deptno=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "임시");
                pstmt.setInt(2, deptno);
                rowcount = pstmt.executeUpdate();
                if(rowcount==0) {
                	System.out.println("값이 없습니다.");
                	return false;
                }
                System.out.println("값이 있습니다.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
            	conn.rollback();
                SingletonHelper.close(pstmt);
            }
            return true;
        }   
    	
    	public static boolean isExist(String dname) throws Exception {
            Connection conn = null;
            PreparedStatement pstmt = null;
            int rowcount = 0;
            try {
                conn = SingletonHelper.getConnection("oracle");
                conn.setAutoCommit(false);
                String sql = "update department set dname=? where dname=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "임시");
                pstmt.setString(2, dname);
                rowcount = pstmt.executeUpdate();
                if(rowcount==0) {
                	System.out.println("값이 없습니다.");
                	return false;
                }
                System.out.println("값이 있습니다.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
            	conn.rollback();
                SingletonHelper.close(pstmt);
            }
            System.out.println("(중복값 발생!) 다른 값을 입력하세요.");
            return true;
        }   
}   