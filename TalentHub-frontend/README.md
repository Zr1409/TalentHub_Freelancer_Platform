# TalentHub Frontend

TalentHub Frontend là giao diện người dùng của hệ thống TalentHub Freelancer Platform, phát triển bằng React, TypeScript và TailwindCSS. Frontend cung cấp trải nghiệm hiện đại, responsive cho cả freelancer và khách hàng.

## Cấu trúc thư mục chính

- `src/`: Mã nguồn chính
  - `pages/`: Các trang chính (Home, Login, Register, ProjectList, Profile, Contract, Payment,...)
  - `components/`: Thành phần giao diện tái sử dụng (Header, Footer, ProjectCard, Modal, ...)
  - `services/`: Giao tiếp với API backend (axios, fetch,...)
  - `contexts/`: Quản lý state toàn cục (Auth, Theme,...)
  - `utils/`, `types/`: Tiện ích, định nghĩa kiểu dữ liệu.
- `public/`: Tài nguyên tĩnh (ảnh, manifest, robots.txt,...)
- `package.json`: Quản lý dependencies Node.js
- `vite.config.ts`: Cấu hình Vite

## Tính năng nổi bật
- Đăng nhập/đăng ký, xác thực JWT
- Quản lý hồ sơ, cập nhật thông tin
- Danh sách dự án, chi tiết dự án, tìm kiếm & lọc
- Ứng tuyển, quản lý hợp đồng, tiến độ, thanh toán
- Đánh giá, phản hồi
- Responsive UI, hỗ trợ đa ngôn ngữ (i18n)

## Công nghệ sử dụng
- React
- TypeScript
- TailwindCSS
- Vite
- Axios
- React Router

## Hướng dẫn chạy dự án
1. Cài đặt Node.js >= 18
2. Cài dependencies:
```bash
npm install
```
3. Chạy ứng dụng:
```bash
npm run dev
```
4. Truy cập tại: `http://localhost:5173` (hoặc port do Vite chỉ định)

## Cấu hình kết nối Backend
- Sửa file `src/config.ts` hoặc `.env` để trỏ đúng địa chỉ API backend.

## Đóng góp
- Tạo pull request hoặc issue để thảo luận.

## License
MIT
