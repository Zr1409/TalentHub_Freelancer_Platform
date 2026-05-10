# TalentHub Backend

TalentHub Backend là thành phần phía máy chủ của hệ thống TalentHub Freelancer Platform, xây dựng bằng Java Spring Boot. Backend chịu trách nhiệm cung cấp API, xử lý nghiệp vụ, xác thực, quản lý dữ liệu và kết nối với cơ sở dữ liệu.

## Cấu trúc thư mục chính

- `pom.xml`: Quản lý dependencies Maven.
- `src/main/java/`: Mã nguồn Java, chia theo các package:
  - `controller/`: Xử lý các request từ frontend, định nghĩa REST API.
  - `service/`: Xử lý logic nghiệp vụ.
  - `repository/`: Tương tác với database (JPA/Hibernate).
  - `model/`: Định nghĩa các entity, DTO.
  - `security/`: Cấu hình bảo mật, JWT, xác thực.
- `src/main/resources/`:
  - `application.properties`: Cấu hình ứng dụng (DB, port, JWT,...).
  - `static/`, `templates/`: Tài nguyên tĩnh (nếu có).
- `uploads/`: Lưu file upload (PDF, hợp đồng,...).

## Tính năng nổi bật
- Đăng ký, đăng nhập, xác thực JWT.
- Quản lý hồ sơ freelancer & khách hàng.
- Đăng/tìm kiếm dự án, ứng tuyển.
- Quản lý hợp đồng, tiến độ, thanh toán.
- Đánh giá, phản hồi.
- Quản lý phân quyền (ROLE_USER, ROLE_ADMIN,...).

## Công nghệ sử dụng
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (MySQL/PostgreSQL)
- Maven

## Hướng dẫn chạy dự án
1. Cài đặt Java 17+ và Maven.
2. Cấu hình database trong `src/main/resources/application.properties`.
3. Chạy lệnh:
```bash
./mvnw spring-boot:run
```
4. API mặc định chạy tại: `http://localhost:8080`

## Tài liệu API
- Sử dụng Swagger tại: `http://localhost:8080/swagger-ui.html` (nếu bật Swagger)

## Đóng góp
- Tạo pull request hoặc issue để thảo luận.

## License
MIT
