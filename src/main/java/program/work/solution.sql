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

select
 substring(trans_date, 1,7)  as mouth,
 country,
 count(1) as trans_count,
 sum(case when state ='approved' then 1 else 0 end) as approved_count,
 sum(amount) as  trans_total_amount ,
 sum(case when state = 'approved' then amount else 0 end ) as approved_total_amount
from Transactions
group by substring(trans_date, 1,7), country


select  a.employee_id,b.name,a.reports_count,a.average_age from
((select
 reports_to,
 count(1) as reports_count,
 sum(age)/count(1) as average_age
from
 Employees
where
   reports_to  is not null
 group by reports_to) a left join Employees b on a.reports_to=b.employee_id) order by a.employee_id


select employee_id from Employees
where salary<30000 and manager_id not in (select employee_id from Employees group by employee_id)
order by employee_id


select
  employee_id,
  department_id
from
 Employee
group by employee_id having(count(1)=1) union
select
 employee_id,
 department_id
from
 Employee
where
 primary_flag = 'Y'

select
 l1.Num as ConsecutiveNums
from
Logs l1,
Logs l2,
Logs l3
 where
 l1.id = l2.id-1
 and l2.id=l3.id-1
 and l1.Num=l2.Num
 and l2.Num=l3.Num

select
 x,
 y,
 z,
 case
   when x+y>z and x+z>y and y+z>x then 'Yes'
   else 'No'
  end as triangle
 from
Triangle