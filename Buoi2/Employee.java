package Buoi2;

public class Employee {
    // default : private
    //CRUD
    // Create, Read, Update, Delete --> CSDL
    String EmployeeCode;
    String FullName;
    //getter, setter
    public String getEmployeeCode() {
        return this.EmployeeCode;
    }
    public void setEmployeeCode(String _code) {
        this.EmployeeCode = _code;
    }
    public String getFullName() {
        return this.FullName;
    }
    public void setFullName(String _fullName) {
        this.FullName = _fullName;
    }
    public void SaveEmployee(Employee employee) {
        //logic xu ly lien quan den luu data --> csdl
    }
    public void EmployeeReport() {
        //Business logic
        // xuat ra cac thong tin employee --> bao cao (report)
        //-->Sata
    }
}
