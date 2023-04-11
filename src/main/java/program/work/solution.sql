select
 Name from
((select * from Customers) a left join
(select * from Orders) b on a.id=b.CustomersId)

where b.id is null


#两个字段的in

select
    Department.name as Deparment,
    Employee.name as Employee,
    Salary
from
    Employee
    join
    Department on Employee.DepartmentId = Department.id
where (Employee.DepartmentId, Salary) in (
select
    max(Salary),
    DepartmentId
from
    Employee
group by DepartmentId
)


