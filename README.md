# TalentHub Freelancer Platform

TalentHub Freelancer Platform là một hệ thống quản lý và kết nối giữa freelancer và khách hàng, hỗ trợ quy trình đăng dự án, tìm kiếm freelancer, quản lý hợp đồng, thanh toán và đánh giá.

## Cấu trúc dự án

- **TalentHub-backend/**: Backend sử dụng Java Spring Boot, quản lý API, xử lý nghiệp vụ, kết nối cơ sở dữ liệu.
  - `pom.xml`: Quản lý dependencies Maven.
  - `src/main/java/`: Mã nguồn Java.
  - `src/main/resources/`: File cấu hình, tài nguyên.
  - `uploads/`: Thư mục lưu trữ file upload (ví dụ: PDF).
- **TalentHub-frontend/**: Frontend sử dụng React + TypeScript, giao diện người dùng hiện đại, responsive.
  - `src/`: Mã nguồn React.
  - `public/`: Tài nguyên tĩnh (ảnh, manifest, robots.txt,...).
  - `package.json`: Quản lý dependencies Node.js.

## Tính năng chính

### Backend
- Đăng ký, đăng nhập, xác thực người dùng (JWT).
- Quản lý hồ sơ freelancer & khách hàng.
- Đăng dự án, tìm kiếm & lọc dự án.
- Ứng tuyển, quản lý hợp đồng, tiến độ công việc.
- Thanh toán, xuất hóa đơn.
- Đánh giá, phản hồi giữa freelancer và khách hàng.

### Frontend
- Giao diện đăng nhập/đăng ký, quản lý tài khoản.
- Trang chủ, danh sách dự án, chi tiết dự án.
- Tìm kiếm, lọc freelancer/dự án.
- Quản lý hợp đồng, tiến độ, thanh toán.
- Trang đánh giá, phản hồi.

## Công nghệ sử dụng
- **Backend**: Java 17+, Spring Boot, Maven, JWT, MySQL/PostgreSQL.
- **Frontend**: React, TypeScript, TailwindCSS, Vite.
- **Khác**: Docker (tùy chọn), CI/CD (tùy chọn).

## Hướng dẫn cài đặt nhanh

### Backend
```bash
cd TalentHub-backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd TalentHub-frontend
npm install
npm run dev
```

## Đóng góp
Mọi đóng góp đều được hoan nghênh! Vui lòng tạo pull request hoặc issue để thảo luận.

## License
Dự án sử dụng giấy phép MIT.


## Tài liệu chi tiết

- [Hướng dẫn Backend](TalentHub-backend/README.md)
- [Hướng dẫn Frontend](TalentHub-frontend/README.md)

cd TalentHub-frontend
🟢 1. Init project
git add package.json package-lock.json index.html vite.config.ts
git commit -m "chore: initialize project with Vite + React + TypeScript"
🟢 2. Setup TypeScript
git add tsconfig*.json
git commit -m "chore: configure TypeScript settings"
🟢 3. Setup ESLint
git add eslint.config.js
git commit -m "chore: setup ESLint configuration"
🟢 4. Setup TailwindCSS
git add tailwind.config.js postcss.config.js src/index.css
git commit -m "style: configure TailwindCSS and global styles"
🟢 5. Setup base app structure
git add src/main.tsx src/App.tsx src/App.css
git commit -m "feat: setup base React app structure"
🟢 6. Create project structure
git add src/components src/pages src/contexts src/lib
git commit -m "chore: initialize project folder structure"
🟢 8. Add gitignore
git add .gitignore
git commit -m "chore: configure gitignore"
git push
🟢 9. Setup UI config
git add components.json
git commit -m "chore: configure UI components settings"
git push
🟢 10. Add first UI components
git add src/components
git commit -m "feat: add initial reusable UI components"
🟢 12. Setup routes
git add src/credentials
git commit -m "feat: add custom React credentials"
git push



🟢 13. Setup utilities
git add src/utils
git commit -m "feat: add shared TypeScript utils"
git push
🟢 14. Setup testing (Playwright + Vitest)
git add test playwright.config.ts vitest.config.ts playwright-fixture.ts
git commit -m "test: setup testing environment with Playwright and Vitest"
🟢 15. Add base test
git add test
git commit -m "test: add initial test cases"
🟢 16. Setup deployment config
git add vercel.json
git commit -m "chore: configure deployment for Vercel"
🟢 17. Add README
git add README.md
git commit -m "docs: add project documentation"
🟢 18. Cleanup & finalize
git add .
git commit -m "refactor: clean up project structure and minor fixes"

🚀 BẮT ĐẦU
cd TalentHub-backend
✅ 1. Commit core project (pom + main)
git add pom.xml src/main/java/com/backend/BackendApplication.java
git commit -m "feat: init Spring Boot project"
✅ 2. Commit config (security, cors, oauth)
git add src/main/java/com/backend/config
git commit -m "feat: setup config"
git push
✅ 3. Commit entity + repository
git add src/main/java/com/backend/entity src/main/java/com/backend/repository
git commit -m "feat: add JPA entities and repositories"
git push
✅ 4. Commit service + impl
git add src/main/java/com/backend/service
git commit -m "feat: implement business logic services"
git push
✅ 6. Commit DTO + exception
git add src/main/java/com/backend/dto src/main/java/com/backend/exception
git commit -m "feat: add DTOs and global exception handling"
git push
✅ 7. Commit util
git add src/main/java/com/backend/enums
git commit -m "feat: add enums classes"
git push

git add public
git commit -m "doc: add public"
git push

git add 
git commit -m "feat: add file mvn"
git push

✅ 8. Commit resources (config + SQL)
git add src/main/resources
git commit -m "chore: add application config and database scripts"
git push
✅ 5. Commit controller (API layer)
git add src/main/java/com/backend/controller
git commit -m "feat: add REST API controllers"
git push
⚠️ 9. Commit config files (Docker, env)
git add Dockerfile .env.example render.yaml
git commit -m "chore: add deployment and environment config"
git push