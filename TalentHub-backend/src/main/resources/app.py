from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import base64
import face_recognition
import cv2
import numpy as np
import cloudinary
import cloudinary.uploader
import cloudinary_config
import cloudinary.api


app = Flask(__name__)
CORS(app, resources={
    r"/api/*": {
        "origins": ["https://talenthub2025.netlify.app", "http://localhost:8080", "http://localhost:5173"]
    }
})

# Thư mục lưu ảnh đăng ký khuôn mặt
# REGISTERED_FACES_DIR = "local_face_auth"

# Tạo thư mục nếu chưa tồn tại
# os.makedirs(REGISTERED_FACES_DIR, exist_ok=True)


@app.route('/')
def index():
    return "Face Recognition API is running!"


# ==== API: Đăng ký khuôn mặt ====
@app.route('/api/register-face', methods=['POST'])
def register_face():
    data = request.get_json()
    user_id = data.get('userId')
    images = data.get('images')  # dạng {front: [], left: [], ...}

    total_images = sum(len(lst) for lst in images.values())
    print(f"[REGISTER] User: {user_id}")
    print(f"[REGISTER] Total images received: {total_images}")

    if total_images != 9:
        return jsonify({
            "success": False,
            "message": f"Không đủ ảnh. Hệ thống yêu cầu 9 ảnh, nhưng nhận được {total_images}."
        })

    # user_dir = os.path.join(REGISTERED_FACES_DIR, user_id)
    # os.makedirs(user_dir, exist_ok=True)
    uploaded_urls = []

    for direction, img_list in images.items():
        for idx, base64_img in enumerate(img_list):
            try:
                header, encoded = base64_img.split(",", 1)
                img_data = base64.b64decode(encoded)
                # Save local
                # filename = f"{direction}_{idx}.jpg"
                # filepath = os.path.join(user_dir, filename)
                # with open(filepath, "wb") as f:
                #     f.write(img_data)
                # Upload Cloudinary
                upload_result = cloudinary.uploader.upload(
                    img_data,
                    folder=f"talenthub_face_auth/{user_id}",
                    public_id=f"{direction}_{idx}",
                    overwrite=True,
                    resource_type="image"
                )
                uploaded_urls.append(upload_result["secure_url"])
            except Exception as e:
                print(f"[REGISTER] Error saving image: {e}")

    return jsonify({
        "success": True,
        "message": f"Đã nhận {total_images} ảnh từ {user_id}"
    })


# ==== API: Kiểm tra đã đăng ký khuôn mặt chưa ====
@app.route('/api/check-face-registered', methods=['GET'])
def check_face_registered():
    user_id = request.args.get("userId")

    if not user_id:
        return jsonify({
            "success": False,
            "message": "Thiếu tham số userId"
        }), 400
    # Check local
    # user_dir = os.path.join(REGISTERED_FACES_DIR, user_id)
    # local_registered = os.path.exists(user_dir) and len(os.listdir(user_dir)) >= 9  # bạn đang lưu đúng 9 ảnh
    # Check Cloudinary
    try:
        result = cloudinary.api.resources(type="upload", prefix=f"talenthub_face_auth/{user_id}/")
        cloud_registered = len(result.get("resources", [])) >= 9
    except Exception as e:
        print(f"[CHECK] Cloudinary error: {e}")
        cloud_registered = False

    # registered = local_registered or cloud_registered
    registered = cloud_registered
    return jsonify({
        "success": True,
        "registered": registered
    })


# ==== API: Xác thực khuôn mặt ====
@app.route('/api/verify-face', methods=['POST'])
def verify_face():
    data = request.get_json()
    user_id = data.get('userId')
    image_data = data.get('image')

    print(f"[VERIFY] User: {user_id}")

    if not image_data:
        return jsonify({
            "success": False,
            "message": "Không có ảnh gửi lên."
        })

    try:
        # Chuyển ảnh base64 → OpenCV image
        try:
            header, encoded = image_data.split(",", 1)
            img_bytes = base64.b64decode(encoded)
            nparr = np.frombuffer(img_bytes, np.uint8)

            frame = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

            if frame is None:
                print("[VERIFY] Lỗi giải mã ảnh: frame is None")
                return jsonify({
                    "success": False,
                    "message": "Ảnh không hợp lệ hoặc đã bị hỏng (frame None)."
                })
        except Exception as decode_error:
            print(f"[VERIFY] Lỗi giải mã ảnh base64: {decode_error}")
            return jsonify({
                "success": False,
                "message": "Không thể giải mã ảnh base64."
            })

        # Mã hóa khuôn mặt từ ảnh xác thực
        unknown_encodings = face_recognition.face_encodings(frame)
        if not unknown_encodings:
            return jsonify({
                "success": False,
                "message": "Không tìm thấy khuôn mặt trong ảnh xác thực."
            })

        unknown_encoding = unknown_encodings[0]
        matches_found = 0
        total_registered = 0

        # ===== So sánh với ảnh Local =====
        # user_dir = os.path.join(REGISTERED_FACES_DIR, user_id)
        # if os.path.exists(user_dir):
        #     for filename in os.listdir(user_dir):
        #         filepath = os.path.join(user_dir, filename)
        #         try:
        #             image = face_recognition.load_image_file(filepath)
        #             encodings = face_recognition.face_encodings(image)
        #
        #             if not encodings:
        #                 continue
        #
        #             total_registered += 1
        #             if face_recognition.compare_faces([encodings[0]], unknown_encoding)[0]:
        #                 matches_found += 1
        #         except Exception as img_err:
        #             print(f"[VERIFY-LOCAL] Lỗi xử lý {filename}: {img_err}")

        # ===== So sánh với ảnh Cloudinary =====
        try:
            import requests
            result = cloudinary.api.resources(type="upload", prefix=f"talenthub_face_auth/{user_id}/")
            for res in result.get("resources", []):
                try:
                    url = res["secure_url"]
                    img_resp = requests.get(url)
                    img_array = np.frombuffer(img_resp.content, np.uint8)
                    reg_img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)

                    encodings = face_recognition.face_encodings(reg_img)
                    if not encodings:
                        continue

                    total_registered += 1
                    if face_recognition.compare_faces([encodings[0]], unknown_encoding, tolerance=0.6)[0]:
                        matches_found += 1
                except Exception as e:
                    print(f"[VERIFY-CLOUD] Lỗi xử lý ảnh từ Cloudinary: {e}")
        except Exception as e:
            print(f"[VERIFY] Cloudinary API error: {e}")

        print(f"[VERIFY] Matches found: {matches_found}/{total_registered}")

        if matches_found >= 5:
            return jsonify({
                "success": True,
                "message": "Xác thực khuôn mặt thành công."
            })
        else:
            return jsonify({
                "success": False,
                "message": "Không khớp khuôn mặt."
            })

    except Exception as e:
        print(f"[VERIFY] Lỗi tổng quát: {e}")
        return jsonify({
            "success": False,
            "message": "Lỗi xử lý ảnh xác thực."
        })


# ==== Chạy server ====
if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(debug=True, host='0.0.0.0', port=port)
