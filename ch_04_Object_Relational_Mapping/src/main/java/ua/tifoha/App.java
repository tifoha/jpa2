package ua.tifoha;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        try(DaoFactory daoFactory = new DaoFactory("MainUnit")) {
            GenericDao<Employee> employeeDao = daoFactory.getDao(Employee.class);
            GenericDao<Firm> firmDao = daoFactory.getDao(Firm.class);
            GenericDao<Department> departmentDao = daoFactory.getDao(Department.class);
//            System.out.println(firmDao.findOne(1));
            Department department = new Department();
            department.setName("Dep");
//            employeeDao.findOne(1L).ifPresent(e->department.getEmployees().add(e));
            departmentDao.save(department);
//            Firm firm = new Firm();
//            firm.setName("Simple firm");
//            firmDao.save(firm);
//
//            Employee employee = new Employee(1);
//            departmentDao.flush();
//            firmDao.flush();
//            employee.setName("Vitaly sereda");
//            employee.setSalary(new BigDecimal(15));
//            employee.setEmbeddedField(new Employee.EmbeddedField("Value"));
////            employee.setFirm(firm);
//            employeeDao.save(employee);
//            System.out.println("Persisted " + employee);
//
//            employee = employeeDao.findOne(1).orElse(null);
//            System.out.println("Find " + employee);
////            EntityManager em = employeeDao.getEntityManager();
//            // find all employees
//            employeeDao = daoFactory.getDao(Employee.class);
//                        EntityManager em = employeeDao.getEntityManager();
//
//            List<Employee> emps = employeeDao.findAll();
//            for (Employee e : emps) {
//                em.detach(e);
//                System.out.println("Found employee: " + e);
//            }
//            // update the employee
////            employee.setSalary(new BigDecimal(20));
////            employeeDao.save(employee);
////            System.out.println("Updated " + employee);
//
//            // remove an employee
////            employeeDao.remove(1);
////            System.out.println("Removed Employee 1");
        }
    }
}
