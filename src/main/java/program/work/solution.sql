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


select a.customer_number from
 select
 customer_number,
 count(1) as sum
 from
 Orders
 group by customer_number
 order by sum desc) a limit 1

 select
    name,
    population,
    area
  from
   World
  where
    area>=3000000 or population>= 25000000;

select
 product_id
from
 Products
where
 low_fats='Y'
 and recyclable = 'Y'

select
 name
from
 customer
where
 referee_id !='2'

select b.unique_id, a.name from
 ((select * from Employees) a
  left join (select * from EmployeeUNI)b on a.id=b.id)

select name from Employee where id in
(select count(1) as sum, managerId from Employee having(sum) >=5)

select * from cinema where description!='boring' and id mod 2==1 order by rating

select a.product_id, round(sum(a.price*b.units)/sum(units), 2) as average_price  from
  ((select * from Prices) a  left join (select * from UnitsSold) b on
   a.start_date<=b.purchase_date  and a.end_date>=b.purchase_date and a.product_id = b.product_id)
   group by product_id

select class from Courses group by class  having count(1) > 5;
