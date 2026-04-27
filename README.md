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