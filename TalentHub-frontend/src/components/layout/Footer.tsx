import { Link, useNavigate } from 'react-router-dom';
import { Mail, Phone, Facebook, Linkedin, Twitter } from 'lucide-react';

const Footer = () => {
  const navigate = useNavigate();
  const userInfoString = localStorage.getItem('userInfo');
  const userInfo = userInfoString ? JSON.parse(userInfoString) : null;
  const role = userInfo?.role;

  const handleProtectedLink = (e, path) => {
    if (!userInfo) {
      e.preventDefault();
      navigate('/login');
    }
  };

  return (
    <footer className="bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-400">
      <div className="container mx-auto px-4 py-10">
        <div className="grid gap-12 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 xl:grid-cols-5">
          {/* Cột Logo & Giới thiệu */}
          <div className="col-span-full md:col-span-1 lg:col-span-2 space-y-4 ms-6">
            <Link to="/" className="flex items-center space-x-2">
              <img
                width="50"
                src="/favicon.png"
                alt="Logo TalentHub"
                className="shadow-lg"
              />
              <span className="text-xl font-bold bg-gradient-to-r from-primary-600 to-primary-800 bg-clip-text text-transparent">
                TalentHub
              </span>
            </Link>
            <p className="text-sm text-gray-500 max-w-sm">
              Nền tảng kết nối freelancer và doanh nghiệp, nơi tài năng tỏa sáng và công việc được hoàn thành xuất sắc.
            </p>
            <div className="flex items-center space-x-3 mt-6">
              <a
                href="https://www.facebook.com"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Facebook"
                className="p-2 rounded-full bg-gray-600 text-gray-300 hover:bg-sky-600 hover:text-white transition-all duration-300 shadow-md"
              >
                <Facebook size={18} />
              </a>
              <a
                href="https://www.linkedin.com"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="LinkedIn"
                className="p-2 rounded-full bg-gray-600 text-gray-300 hover:bg-sky-600 hover:text-white transition-all duration-300 shadow-md"
              >
                <Linkedin size={18} />
              </a>
              <a
                href="https://www.twitter.com"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Twitter"
                className="p-2 rounded-full bg-gray-600 text-gray-300 hover:bg-sky-600 hover:text-white transition-all duration-300 shadow-md"
              >
                <Twitter size={18} />
              </a>
            </div>

          </div>

          {/* Cột Dành cho Freelancer */}
          {role !== 'CLIENT' && (
            <div className="space-y-4">
              <h3 className="font-bold text-gray text-lg mb-2">Dành cho Freelancer</h3>
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/jobs"
                    onClick={(e) => handleProtectedLink(e, '/jobs')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Tìm việc làm
                  </Link>
                </li>
                <li>
                  <Link
                    to="/settingsfreelancer"
                    onClick={(e) => handleProtectedLink(e, '/settingsfreelancer')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Tạo hồ sơ
                  </Link>
                </li>
                <li>
                  <Link
                    to="/client/post-job"
                    onClick={(e) => handleProtectedLink(e, '/client/post-job')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Báo giá dự án
                  </Link>
                </li>
                <li>
                  <Link
                    to="/wallet"
                    onClick={(e) => handleProtectedLink(e, '/wallet')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Quản lý thu nhập
                  </Link>
                </li>
              </ul>
            </div>
          )}

          {/* Cột Dành cho Doanh nghiệp */}
          {role !== 'FREELANCER' && (
            <div className="space-y-4">
              <h3 className="font-bold text-gray mb-2 text-lg">Dành cho Doanh nghiệp</h3>
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/client/post-job"
                    onClick={(e) => handleProtectedLink(e, '/client/post-job')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Đăng việc
                  </Link>
                </li>
                <li>
                  <Link
                    to="/freelancers"
                    onClick={(e) => handleProtectedLink(e, '/freelancers')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Tìm Freelancer
                  </Link>
                </li>
                <li>
                  <Link
                    to="/about"
                    onClick={(e) => handleProtectedLink(e, '/about')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Giải pháp doanh nghiệp
                  </Link>
                </li>
                <li>
                  <Link
                    to="/pricing"
                    onClick={(e) => handleProtectedLink(e, '/pricing')}
                    className="text-sm hover:text-primary transition-colors"
                  >
                    Gói dịch vụ
                  </Link>
                </li>
              </ul>
            </div>
          )}

          {/* Cột Hỗ trợ */}
          <div className="space-y-4">
            <h3 className="font-bold text-gray text-lg mb-2">Hỗ trợ</h3>
            <ul className="space-y-2">
              <li>
                <Link
                  to="/contact"
                  className="text-sm hover:text-primary transition-colors"
                >
                  Trung tâm trợ giúp
                </Link>
              </li>
              <li>
                <Link
                  to="/terms"
                  className="text-sm hover:text-primary transition-colors"
                >
                  Điều khoản sử dụng
                </Link>
              </li>
              <li>
                <Link
                  to="/privacy"
                  className="text-sm hover:text-primary transition-colors"
                >
                  Chính sách bảo mật
                </Link>
              </li>
              <li>
                <Link
                  to="/contact"
                  className="text-sm hover:text-primary transition-colors"
                >
                  Liên hệ
                </Link>
              </li>
            </ul>
          </div>

          {/* Cột Liên hệ & thông tin */}
          <div className="space-y-4">
            <h3 className="font-bold text-gray text-lg mb-2">Liên hệ</h3>
            <ul className="space-y-3 text-sm">
              <li className="flex items-center space-x-2">
                <Phone size={16} className="text-gray-500" />
                <span>0358768117</span>
              </li>
              <li className="flex items-center space-x-2">
                <Mail size={16} className="text-gray-500" />
                <span>developer06101990@gmail.com</span>
              </li>
            </ul>
          </div>
        </div>

        <div className="mt-12 pt-8 border-t border-gray-700">
          <p className="text-center text-sm text-gray-500">
            © {new Date().getFullYear()} TalentHub. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;