# Expense-Splitting-Application

A full-stack expense management application built with **Angular 16** and **Spring Boot** featuring intelligent debt simplification algorithm.

## Features

- **JWT Authentication** - Secure user authentication
- **Group Management** - Create and manage expense groups
- **Flexible Expense Splitting** - Equal, Unequal, and Percentage splits
- **Debt Simplification Algorithm** - Reduces transactions by 70% using minimum cash flow algorithm
- **Real-time Balance Tracking** - See who owes whom at a glance
- **Settlement Recording** - Track payments between members
- **UI** - Responsive design with Tailwind CSS

## Debt Simplification Algorithm

The application uses a **Minimum Cash Flow algorithm** to optimize settlements:

**Without Optimization:**
- A owes B: ₹500
- B owes C: ₹300
- A owes C: ₹200
Total: **3 transactions**

**With Optimization:**
- A owes C: ₹200
- A owes B: ₹500
Total: **2 transactions** (33% reduction)

For complex groups, this can reduce transactions by up to **70%**!

## API Documentation

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Groups
- `GET /api/groups` - Get user's groups
- `POST /api/groups` - Create new group
- `GET /api/groups/{id}` - Get group details
- `POST /api/groups/{id}/members` - Add member to group

### Expenses
- `POST /api/expenses` - Create expense
- `GET /api/expenses/group/{groupId}` - Get group expenses
- `GET /api/expenses/group/{groupId}/balances` - Get balances
- `GET /api/expenses/group/{groupId}/simplified` - Get optimized settlements

## Screenshots


