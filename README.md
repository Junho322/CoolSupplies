# CoolSupplies
The CoolSupplies application is designed for schools to manage school supplies for students (grades 1 to 12) and enable parents to order these supplies. The application includes the following key features:

**User Accounts:**

A built-in admin account (admin@cool.ca) with an initial password of admin (modifiable).
Parents register with an email and password, optionally adding their name and phone number.
The admin can delete parent accounts if needed.

**School Management:**

The admin configures the grades offered by the school, supporting various numbering systems (e.g., 1–12, A–H).
A bundle of supplies is defined for each grade, with items categorized as mandatory, recommended, or optional.
Discounts can be applied to bundles with at least two items.
The admin adds student names to grades and can update supplies annually.

**Parent Orders:**

Parents select the student(s) for whom they are ordering supplies.
Orders include individual items and/or bundles, with options to include mandatory, recommended, or all items in bundles.
Orders can be adjusted, finalized, and paid. Payment details are limited to an authorization code.

**Order and Payment Management:**

Orders must be paid before the school year starts; late payments incur a penalty requiring separate transactions.
Parents can cancel unpaid orders before the school year begins.
Paid orders are prepared for pickup at the start of the school year, and the application records when supplies are picked up.

**Annual Updates:**

At the end of the school year, all orders are removed, and the admin updates the system for the next year.
Features include promoting students to the next grade and applying a percentage increase to item prices (prices remain fixed during the school year).
This streamlined application helps admins efficiently manage supplies and ensures parents can order and track items for their children easily.

**To log in**

Email: admin@cool.ca
Password: Pwd#

**Some functionalities**

1. Update school admin password
2. Register parent, update it, and delete it (from the list of parents)
3. Add student, update it (including its grade), and delete it (from the list of students)
4. Add item, update it, and delete it (from the list of items)
5. Add bundle, update it (including its grade), and delete it (from the list of bundles)
6. Add item to bundle, update it, and remove item from bundle (i.e., remove it from the list of items
of the bundle)
7. Add student to parent and remove student from parent (i.e., remove it from the list of students
of the parent)
8. Start an order, update it (only purchase level and student), and cancel it
9. Pick up an order
10. Add item to order, update it (quantity only), and delete it (from the list of items of the order)
11. Pay for order and pay penalty for order
12. View individual order (including parent, student, status, number, date, level, authorization codes,
individual items and items in bundles including their prices and deducted discounts, and total
price)
13. Start school year
14. Add grade, update it, and delete it (from the list of grades)
15. View all orders

**Persistence**
All data are stored in app.data file
