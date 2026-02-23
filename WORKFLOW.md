# Clothify Store POS — Development Workflow

Project Folder: `G:\POS\clothify`
Architecture Style: **Thogakade-like Layered Architecture**
Stack: **JavaFX + MySQL + JasperReports**

---

## 1) Project Goal
Build a realistic standalone desktop POS system for a clothing store with:

- User authentication (Admin/Staff)
- Product/category management
- Inventory tracking
- Supplier and employee management
- Order placement + invoice generation
- Sales/inventory reports
- Search/filter features across key modules

The system must be simple, academic-quality, and business-practical.

---

## 2) Architecture Rules (Must Follow)

## 2.1 Layered Structure

```text
com.clothify
├─ controller       (JavaFX event handlers, UI logic)
├─ service          (business rules, validations, transactions)
├─ repository       (database CRUD/query operations)
├─ model            (entity/domain models)
├─ util             (DB connection, helpers, session, validation)
├─ view             (FXML files)
└─ report           (Jasper .jrxml files)
```

## 2.2 Responsibility Separation
- **Controller:** no SQL code.
- **Repository:** no UI code.
- **Service:** business validation + transaction flow.
- **Model:** plain data objects.

## 2.3 Simplicity Rules
- Keep code student-friendly and readable.
- Avoid overengineering/pattern-heavy complexity.
- Prefer direct, clean method flows.

---

## 3) Tech Setup Workflow

## 3.1 Maven Dependencies
- JavaFX (`controls`, `fxml`)
- MySQL connector
- JasperReports
- Apache POI (if report export enhancements needed)

## 3.2 Utility Setup
- `DBConnection` singleton (simple and robust)
- `SessionManager` for login session data
- `PasswordUtil` using built-in Java PBKDF2

## 3.3 Configuration Files
- `pom.xml`
- `src/main/resources/db.properties`
- report templates under `src/main/resources/report`

---

## 4) Database Workflow

## 4.1 Schema Design
Create and validate these tables:

1. `users`
2. `categories`
3. `products`
4. `inventory`
5. `suppliers`
6. `employees`
7. `orders`
8. `order_details`
9. `settings` (optional but recommended)

## 4.2 Data Integrity Rules
- PK/FK relationships in place
- Unique constraints (`username`, `product_code`)
- `is_active` soft-delete strategy where needed
- No negative stock

## 4.3 Seed Data
- Admin user
- Staff user
- Base categories
- Some sample products/suppliers

---

## 5) Module Build Plan

## Module 01 — Authentication
- Login screen
- Username/password validation
- Role-based session (admin/staff)
- Redirect to main dashboard

## Module 02 — Category Management
- Add/update/delete category
- Category listing table
- Search/filter by name

## Module 03 — Product Management
- Add/update/delete product
- Category link
- Price/size/description/image path
- Search/filter by name/code/category

## Module 04 — Inventory Management
- Current stock listing
- Stock update operations
- Low-stock warnings
- Real-time quantity updates after sales

## Module 05 — Supplier Management
- Supplier CRUD
- Search/filter support

## Module 06 — Employee Management
- Employee CRUD
- Role assignment support

## Module 07 — POS / Order Processing
- Add products to cart
- Quantity + totals
- Save order + order details
- Inventory deduction in one transaction
- Invoice generation

## Module 08 — Reporting (Jasper)
- Sales summary report
- Inventory summary report
- Product performance report
- Date-range filters + PDF generation

---

## 6) Transaction Workflow (Critical)

For order placement:

1. Validate cart items and stock
2. Begin DB transaction
3. Insert order
4. Insert order_details
5. Update inventory quantities
6. Commit if all succeed
7. Rollback on any failure

---

## 7) Validation Workflow

## UI-level validation
- Required fields
- Number/date format checks
- Friendly error alerts

## Service-level validation
- Duplicate username/product code checks
- Business rules (stock and active states)

## DB-level validation
- Final enforcement via constraints

---

## 8) Reporting Workflow

1. Prepare `.jrxml` templates in `resources/report`
2. Compile/fill reports from DB data
3. Export to PDF
4. Keep report layout clear and printable

---

## 9) UI/UX Workflow

- Keep design consistent across all screens
- Use simple form + table layout
- Use clear action buttons and user feedback alerts
- Make navigation obvious and fast

---

## 10) Quality Checklist (Per Module)

- CRUD works correctly
- Search/filter works
- Validation messages are clear
- No SQL in controllers
- No crashes during normal use
- DB data is correct after actions

---

## 11) Git Workflow

- Small, focused commits per feature/module
- English commit messages only
- No unnecessary files committed
- Keep repository clean (`target`, IDE files, logs ignored)

---

## 12) Delivery Readiness Checklist

Before final submission/demo:

- All core modules implemented
- Role-based login works
- POS order flow fully functional
- Reports generating correctly
- README with setup instructions
- SQL schema/setup script included

---

## 13) Live Progress Tracker

Use this section to mark progress continuously.

### ✅ Done
- Project workflow prepared
- Project folder initialized at `G:\POS\clothify`

### 🔄 In Progress
- Project scaffolding (Maven + package structure + db.properties)

### ⛔ Blocked
- None

### 📌 Next
1. Create Maven JavaFX project skeleton in this folder.
2. Add DB connection utility + schema script.
3. Build Module 01 (Authentication).
