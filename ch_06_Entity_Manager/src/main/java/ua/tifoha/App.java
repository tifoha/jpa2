package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ua.tifoha.config.RootConfig;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		GenericApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		EntityManagerFactory emf = ctx.getBean(EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();
//		EmployeeManager manager = ctx.getBean(EmployeeManager.class);
//		Environment env = ctx.getEnvironment();
//		PlatformTransactionManager tm = ctx.getBean(PlatformTransactionManager.class);
////		System.out.println("START TX");
//		Employee emp2 = manager.getNew("E2");
//		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
////
//		Employee emp = new Employee();
//		emp.setName("Emploee1");
//		em.joinTransaction();
//
//		System.out.println("PERSIST");
//		em.persist(emp);
//		emp.setInfo(new Info("info"));
//		emp.getNickNames().add("frog");
////		em.close();
////		emp2.setName("EE2");
////		System.out.println(em.contains(emp2));
////		em.merge(emp2);
////		System.out.println(em.contains(emp2));
////
////		System.out.println("EM CLOSED.");
////		em.close();
//		System.out.println("END TX");
//
////		System.out.println(em.contains(emp2));
//		tm.commit(ts);
		Department dep = em.getReference(Department.class, 1L);
		Employee emp2 = em.find(Employee.class, 1L);
		em.getTransaction().begin();
//		System.out.println(em.contains(emp2));
//		emp2 = new Employee();
//		emp2.id = 29L;
		emp2.setName("3");
		emp2.setDepartment(dep);
//		em.merge(emp2);
//		System.out.println(em.contains(emp2));
		em.persist(emp2);
//		System.out.println(em.contains(emp2));
//
		final Employee entity = new Employee();
		em.persist(entity);
		em.getTransaction().commit();
		em.getTransaction().begin();
		em.remove(em.getReference(Employee.class, 83L));
		em.getTransaction().commit();

//		System.out.println("");
//        try(DaoFactory daoFactory = new DaoFactory("MainUnit")) {
//            GenericDao<Employee> employeeDao = daoFactory.getDao(Employee.class);
//
////            GenericDao<Firm> firmDao = daoFactory.getDao(Firm.class);
//            GenericDao<Department> departmentDao = daoFactory.getDao(Department.class);
//////            System.out.println(firmDao.findOne(1));
//            Department department = new Department();
//            department.setName("Dep");
//            department.setLastName("java");
//////            employeeDao.findOne(1L).ifPresent(e->department.getEmployees().add(e));
//            departmentDao.save(department);
////            Firm firm = new Firm();
////            firm.setName("Simple firm");
////            firmDao.save(firm);
////
//            Employee employee = new Employee();
//            final Set<String> nickNames = employee.getNickNames();
//            nickNames.add("xxx");
//            nickNames.add("aaa");
//            nickNames.add("mmm");
//
//            final Set<VacationEntry> vacations = employee.getVacation();
//            vacations.add(new VacationEntry(LocalDate.now(), 5));
//            vacations.add(new VacationEntry(LocalDate.now().plusDays(4), 5));
//            vacations.add(new VacationEntry(LocalDate.now().plusDays(3), 5));
//
//            final List<SerializableObject> objectSet = employee.getObjectSet();
//            objectSet.add(new SerializableObject("V1"));
//            objectSet.add(new SerializableObject("V2"));
//            objectSet.add(new SerializableObject("V3"));
//
//            final Map<PhoneType, String> phoneNumbers = employee.getPhoneNumbers();
//            phoneNumbers.put(WORK, "0971396134");
//            phoneNumbers.put(HOME, "0472451197");
//
////            employee.setDepartment(department);
////            departmentDao.flush();
////            firmDao.flush();
////            employee.setName("Vitaly sereda");
////            employee.setSalary(new BigDecimal(15));
////            employee.setEmbeddedField(new Employee.EmbeddedField("Value"));
//////            employee.setFirm(firm);
//            employeeDao.save(employee);
//
//            department.getEmployeesByCubicle().put("1C", employee);
//            department.getEmployeesByCubicle().put("3F", employee);
//            departmentDao.save(department);
//
//            employeeDao = daoFactory.getDao(Employee.class);
//            employee = employeeDao.findOne(employee.getId()).get();
//            System.out.println(employee.getNickNames());
//            System.out.println(employee.getVacation());
//            System.out.println(employee.getObjectSet());
//            final ListIterator<SerializableObject> iterator = employee.getObjectSet().listIterator(objectSet.size());
//            iterator.previous();
//            iterator.remove();
//            employeeDao.save(employee);
//
//            departmentDao = daoFactory.getDao(Department.class);
//            department = departmentDao.findOne(department.getId()).get();
//            System.out.println(department.getEmployeesByCubicle());
//
//            GenericDao<Firm> firmDao = daoFactory.getDao(Firm.class);
//            Firm firm = new Firm();
//            firm.setName("firm");
//            firmDao.save(firm);
//
//            department.setFirm(firm);
//            departmentDao.save(department);
//
//            firmDao = daoFactory.getDao(Firm.class);
//            firm = firmDao.findOne(firm.getId()).get();
//            System.out.println(firm.getDepartments());
//
////            System.out.println("Persisted " + employee);
////
////            employee = employeeDao.findOne(1).orElse(null);
////            System.out.println("Find " + employee);
//////            EntityManager em = employeeDao.getEntityManager();
////            // find all employees
////            employeeDao = daoFactory.getDao(Employee.class);
////                        EntityManager em = employeeDao.getEntityManager();
////
////            List<Employee> emps = employeeDao.findAll();
////            for (Employee e : emps) {
////                em.detach(e);
////                System.out.println("Found employee: " + e);
////            }
////            // update the employee
//////            employee.setSalary(new BigDecimal(20));
//////            employeeDao.save(employee);
//////            System.out.println("Updated " + employee);
////
////            // remove an employee
//////            employeeDao.remove(1);
//////            System.out.println("Removed Employee 1");
//        }
	}

}
